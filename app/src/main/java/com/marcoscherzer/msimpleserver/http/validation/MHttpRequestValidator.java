package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._400_BAD_REQUEST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._405_METHOD_NOT_ALLOWED;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._413_PAYLOAD_TOO_LARGE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._415_UNSUPPORTED_MEDIA_TYPE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._505_HTTP_VERSION_NOT_SUPPORTED;
import static com.marcoscherzer.msimpleserver.http.validation.MParameterMode.POST;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.MRequestValidator;
import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;
import com.marcoscherzer.msimpleserver.http.request.MHttpContentMap;
import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestValidator.MHttpRequestData;
import com.marcoscherzer.msimpleserver.http.validation.MHttpVersion.MValidationPattern;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocket;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpRequestValidator extends MRequestValidator<MHttpRequestData, MHttpVersion> {
    //private static Pattern INVALID_CHARACTERS = Pattern.compile("[^\\x20-\\x7E]");
    private static final Pattern INVALID_CHARACTERS = Pattern.compile("[^\\x20-\\x7E\\r\\n]");

    private final MUrlParser urlParser = new MUrlParser();

    private MHttpContentMap contentMap;

    private int MAX_HEADER_SIZE = 8192; // Maximale Größe der Header
    private boolean upgradeUnencrypted;
    private MHttpResponseStatusCodes[] errorsToSendPagesForInsteadOfPlain;
    private final MParameterMode mode;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator(MParameterMode mode, MHttpVersion... supportedProtocols) {
        super(supportedProtocols);
        this.mode = mode;
    }
//private static final Pattern HEADER_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+:\\s.*$");

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean validateHeader(String line, MValidationPattern p) {
        mout.println("Checking header: " + line);
        if (line.startsWith(p.getHeaderName())) {
            if (!line.matches(p.getPattern())) {
                mout.println("Error: Invalid header. Checked header: " + line);
                return false;
            }
        }
        return true;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static String sanitizeLine(String line) {
        return line.replaceAll(INVALID_CHARACTERS.toString(), ""); // Entfernt nicht druckbare Zeichen
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isInvalidCharacter(char ch) {
        return INVALID_CHARACTERS.matcher(Character.toString(ch)).find();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setMaxHeaderSize(int maxHeaderSize) {
        MAX_HEADER_SIZE = maxHeaderSize;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setMaxUrlLength(int maxUrlLength) {
        urlParser.maxUriLength = maxUrlLength;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setUpgradeUnencrypted(boolean upgradeUnencrypted) {
        this.upgradeUnencrypted = upgradeUnencrypted;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer,
     * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
     * Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public MHttpRequestData isValidRequest(Socket socket) {
        MHttpRequestData outData = new MHttpRequestData();
        StringBuilder request = new StringBuilder();
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int ch;
            while ((ch = reader.read()) != -1) { //throws IOException
                if (isInvalidCharacter((char) ch)) {
                    String name = Character.getName(ch);
                    if (name == null) name = "unassigned Character";
                    mout.println("Fehler: Ungültiges Zeichen erkannt - " + name);
                    outData.responseCode = _400_BAD_REQUEST;
                    return outData;
                }
                request.append((char) ch);
                // Überprüfe auf das Ende der Header (CRLF gefolgt von CRLF)
                if (request.length() >= 4 &&
                        request.substring(request.length() - 4).equals("\r\n\r\n")) {
                    break;
                }
            }

            String[] lines = request.toString().split("\r\n");

            mout.println("Überprüfe die Request-Line: " + lines[0]);
            if (lines.length == 0) {
                mout.println("Fehler: Fehlende Request-Line.");
                outData.responseCode = _400_BAD_REQUEST;
                return outData;
            }

            String[] parts = lines[0].split(" ");
            if (parts.length < 3) {
                mout.println("Fehler: Das Format der Request-Line ist falsch. Überprüfte Teile: " + Arrays.toString(parts));
                outData.responseCode = _400_BAD_REQUEST;
                return outData;
            }

            String method = parts[0];
            String protocol = parts[2];

            MHttpVersion version = null;
            for (int i = 0; i < getSupportedProtocols().size(); i++) {
                version = getSupportedProtocols().get(i);
                if (version.getVersion() == outData.protocol) break;
            }

            // Überprüfe Protokoll
            if (!this.getSupportedProtocolsPattern().matcher(protocol).matches()) {
                mout.println("Fehler: Ungültiges Protokoll: " + protocol);
                outData.responseCode = _505_HTTP_VERSION_NOT_SUPPORTED;
                return outData;
            }
            //(Protokoll ist vorhanden und supported)

            // Überprüfe Methode
            if (!version.getSupportedMethods().matcher(method).matches()) {
                mout.println("Fehler: Ungültige Methode: " + method);
                outData.responseCode = _405_METHOD_NOT_ALLOWED;
                return outData;
            }

            //Überprüfe Url und setze data-attribute für url meth name und query-parameters oder return errorResponseCode
            outData = urlParser.parseUrl(parts[1], outData);
            if (outData.responseCode != null) return outData;

            outData.requestMethod = method;
            outData.protocol = protocol;

            // Upgrade unsicherer Verbindungen
            if (!(socket instanceof SSLSocket) && upgradeUnencrypted) {
                mout.println("Fehler: Request kam über unsicheres http");
                outData.responseCode = MHttpResponseStatusCodes._302_FOUND;
                return outData;
            }

            //----------------------------------------- HTTP-Version > 0.9 -------------------------------------------
            outData.mode = mode;
            // Überprüfe Header
            validateHeaders(version, lines, outData);
            if (outData.responseCode != VALID_AND_COMPLETE) return outData;

            /*
             * benutzungskonventionsdefinition: POST wirkt überschreibend bzgl kompletter URL-Parameter
             * diese werden neu gesetzt
             */
            //toDo
            if (mode == POST) { // evtl später wieder ohne mode nur über supported protocolls
                // Content-Length aus Header holen
                String cl = outData.getHeaders().get("Content-Length");
                if (cl == null) {
                    mout.println("Fehler: Content-Length fehlt.");
                    outData.responseCode = _400_BAD_REQUEST;
                    return outData;
                }
                int contentLength;
                try {
                    contentLength = Integer.parseInt(cl.trim());
                } catch (NumberFormatException e) {
                    mout.println("Fehler: Ungültiger Content-Length.");
                    outData.responseCode = _400_BAD_REQUEST;
                    return outData;
                }

                // Body-Bytes lesen
                byte[] bodyBytes = new byte[contentLength];
                int readTotal = 0;
                while (readTotal < contentLength) {
                    int n = inputStream.read(bodyBytes, readTotal, contentLength - readTotal);
                    if (n == -1) break;
                    readTotal += n;
                }
                if (readTotal < contentLength) {
                    mout.println("Fehler: Body unvollständig gelesen.");
                    outData.responseCode = _400_BAD_REQUEST;
                    return outData;
                }

                outData.bodyBytes = bodyBytes; // Binärdaten speichern

                validatePost(outData.bodyBytes, outData);
                if (outData.responseCode != VALID_AND_COMPLETE) return outData;
            }

            outData.responseCode = VALID_AND_COMPLETE;
        } catch (UnsupportedEncodingException exc) {
            mout.println("Fehler: Nicht unterstützte URL Kodierung - ");
            exc.printStackTrace(mout);
            outData.responseCode = _400_BAD_REQUEST;
            return outData;
        } catch (IOException exc) {
            mout.println("Fehler beim Lesen des InputStreams. IO-Exception - ");
            exc.printStackTrace(mout);
            outData.responseCode = _400_BAD_REQUEST;
            return outData;
        }

        return outData;
    }


    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer,
     * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
     * Copyright Marco Scherzer, All rights reserved
     * todo UNGETESTET , erste ideen skizze
     */
    private void validatePost(byte[] bodyBytes, MHttpRequestData outData) {
        String ct = outData.getHeaders().get("Content-Type");

        // Falls kein Content-Type Header vorhanden ist → automatisch bestimmen
        if (ct == null) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bodyBytes)) {
                ct = URLConnection.guessContentTypeFromStream(bais);
            } catch (IOException e) {
                mout.println("Fehler: Content-Type konnte nicht bestimmt werden.");
                outData.responseCode = _415_UNSUPPORTED_MEDIA_TYPE;
                return;
            }
        }

        if (bodyBytes == null || bodyBytes.length == 0) {
            mout.println("Fehler: POST-Body fehlt oder leer.");
            outData.responseCode = _400_BAD_REQUEST;
            return;
        }

        // Behandlung nach Content-Type
        if (ct != null && ct.startsWith("application/x-www-form-urlencoded")) {
            String body = new String(bodyBytes, StandardCharsets.UTF_8);

            // synthetische URL bauen
            String syntheticUrl = outData.getResourcePath() + "/" + outData.getEndpointQuery() + "?" + body;

            // vorhandenen Parser nutzen
            urlParser.parseUrl(syntheticUrl, outData);

            if (outData.responseCode != VALID_AND_COMPLETE) {
                mout.println("Fehler beim Parsen des POST-Bodys.");
                return;
            }

            outData.responseCode = VALID_AND_COMPLETE;
            return;
        }

        // JSON oder Binärdaten aktuell nicht unterstützt → 415
        mout.println("Fehler: Unsupported Content-Type: " + ct);
        outData.responseCode = _415_UNSUPPORTED_MEDIA_TYPE;
    }



    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private void validateHeaders(MHttpVersion httpVersion, String[] lines, MHttpRequestData outData) {
        int headerSize = 0;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            mout.println("Checking header line: " + line);
            if (line.isEmpty()) {
                break; // Ende der Header-Sektion
            }

            headerSize += line.length();
            if (!checkHeaderSize(httpVersion, headerSize)) {
                outData.responseCode = _413_PAYLOAD_TOO_LARGE;
                return;
            }

            // Mehrzeilige Header berücksichtigen
            while (i < lines.length - 1 && (lines[i + 1].startsWith(" ") || lines[i + 1].startsWith("\t"))) {
                line += " " + lines[++i].trim();
                headerSize += lines[i].length();
                if (!checkHeaderSize(httpVersion, headerSize)) {
                    outData.responseCode = _413_PAYLOAD_TOO_LARGE;
                    return;
                }
            }

            // Validierung spezifischer Header
            mout.println("Checking specific header: " + line);
            for (MValidationPattern p : httpVersion.getValidationEntrySet()) {  //httpVersion.
                if (validateHeader(line, p)) {
                    String[] headerParts = line.split(":", 2);
                    if (headerParts.length == 2) {
                        outData.headers.put(headerParts[0].trim(), headerParts[1].trim());
                        break;
                    }
                } else outData.responseCode = _400_BAD_REQUEST;
            }
        }
        outData.responseCode = VALID_AND_COMPLETE;
        return;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private boolean checkHeaderSize(MHttpVersion httpVersion, int headerSize) {
        mout.println("Checking header size: " + headerSize);
        if (headerSize > MAX_HEADER_SIZE) {
            mout.println("Error: Header size exceeds the allowed limit. Checked header size: " + headerSize);
            return false;
        }
        return true;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final class MHttpRequestData {
        private final Map<String, String> headers = new HashMap<>();
        private byte[] bodyBytes;
        private MParameterMode mode;
        //private boolean validAndComplete;
        private final Map<String, String> resourceMethodParameters = new HashMap<>();
        private String requestMethod;
        private String resourcePath;
        private String resourceMethod;
        private String protocol;
        private MHttpResponseStatusCodes responseCode;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MParameterMode getMode() {
            return mode;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getRequestMethod() {
            return requestMethod;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getResourcePath() {
            return resourcePath;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getEndpointQuery() {
            return resourceMethod;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getProtocol() {
            return protocol;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public Map<String, String> getHeaders() {
            return headers;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public String getBodyBytes() {
            return bodyBytes;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public Map<String, String> getResourceMethodParameters() {
            return resourceMethodParameters;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MHttpResponseStatusCodes isValidAndCompleteOrErrorCode() {
            return responseCode;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Method: ").append(requestMethod).append("\n");
            sb.append("ResourcePath: ").append(resourcePath).append("\n");
            sb.append("EndpointQuery: ").append(resourceMethod).append("\n");
            sb.append("Protocol: ").append(protocol).append("\n");
            sb.append("Headers: ").append("\n");
            for (final Map.Entry<String, String> header : headers.entrySet()) {
                sb.append("  ").append(header.getKey()).append(": ").append(header.getValue()).append("\n");
            }
            for (final Map.Entry<String, String> resourceMethodParameter : resourceMethodParameters.entrySet()) {
                sb.append("  ").append(resourceMethodParameter.getKey()).append(": ").append(resourceMethodParameter.getValue()).append("\n");
            }
            sb.append("Internal Code or Error-ReponseCode: ").append(responseCode).append("\n");
            return sb.toString();
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static final class MUrlParser {

        private static final int MAX_PARAM_LENGTH = 128; // Maximale erlaubte Länge für Parameterwerte
        private static final Pattern PATH_PATTERN = Pattern.compile("^/[^=&\\s]*$"); // Kein '=', '&' oder Leerraum erlaubt
        private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
        private static final Pattern QUERY_PATTERN = Pattern.compile("^([a-zA-Z0-9_]+=[^&]*)(&[a-zA-Z0-9_]+=[^&]*)*$");

        private int maxUriLength = 1024;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public void setMaxUrlLength(int maxUrlLength) {
            this.maxUriLength = maxUrlLength;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public MHttpRequestData parseUrl(String url, MHttpRequestData data) {
            String path = "";

            // Überprüfe URL-Länge
            if (url.length() > maxUriLength) {
                mout.println("Fehler: URI Länge zu lang: " + url.length() + ", Maximal erlaubte Länge: " + maxUriLength);
                data.responseCode = MHttpResponseStatusCodes._414_URI_TOO_LONG;
                return data;
            }

            path = URLDecoder.decode(url, StandardCharsets.UTF_8);

            // Überprüfe doppelte Fragezeichen
            int firstQuestionMark = path.indexOf("?");
            int lastQuestionMark = path.lastIndexOf("?");

            if (firstQuestionMark != -1 && firstQuestionMark != lastQuestionMark) {
                mout.println("Fehler: Mehrere '?' in der URL.");
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }

            // **Überprüfung: Falls `=` oder `&` vor `?` auftaucht, ist die URL ungültig**
            if (firstQuestionMark != -1 && path.substring(0, firstQuestionMark).contains("=")) {
                mout.println("Fehler: '=' darf nur als Zuweisungsoperator nach '?' verwendet werden.");
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }
            if (firstQuestionMark != -1 && path.substring(0, firstQuestionMark).contains("&")) {
                mout.println("Fehler: '&' darf nur als Parametertrenner nach einem Methodenaufruf verwendet werden.");
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }

            String resourcePath = "";
            String methodName = "";
            String parameterPart = "";

            // Aufteilung in Resource und Methode
            if (firstQuestionMark != -1) {
                String[] splitParts = path.split("\\?");
                methodName = splitParts[0].substring(splitParts[0].lastIndexOf("/") + 1);
                resourcePath = splitParts[0].substring(0, splitParts[0].lastIndexOf("/"));
                parameterPart = splitParts.length > 1 ? splitParts[1] : "";

                // **Methoden ohne Ressourcen verhindern**
                if (resourcePath.isEmpty()) {
                    mout.println("Fehler: Methode kann nicht ohne eine Ressource existieren.");
                    data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                    return data;
                }

                // **Überprüfung des Methoden-Namens gemäß Java-Konventionen**
                if (!METHOD_NAME_PATTERN.matcher(methodName).matches() || methodName.length() > 64) {
                    mout.println("Fehler: Ungültiger Methodenname nach Java-Konventionen: " + methodName);
                    data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                    return data;
                }

                // **Methoden ohne Parameter sind erlaubt (`testresource/method?`)**
                if (!parameterPart.isEmpty()) {
                    // **Überprüfung der Query-Syntax**
                    if (!QUERY_PATTERN.matcher(parameterPart).matches()) {
                        mout.println("Fehler: Ungültige Query-Syntax oder nicht alle Parameter vorhanden: " + parameterPart);
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    // Query-Parameter speichern und doppelte Parameter verhindern
                    String[] params = parameterPart.split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=", 2);

                        if (keyValue.length < 2 || keyValue[1].isEmpty()) {
                            mout.println("Fehler: Alle Parameter müssen einen Wert haben: " + param);
                            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                            return data;
                        }

                        // **Prüfung: Nur genau ein `=` in jedem Parameter erlaubt**
                        if (param.chars().filter(ch -> ch == '=').count() > 1) {
                            mout.println("Fehler: Mehrfache '=' in einem Parameter sind nicht erlaubt: " + param);
                            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                            return data;
                        }

                        // **Prüfung der Parameterlänge**
                        if (keyValue[1].length() > MAX_PARAM_LENGTH) {
                            mout.println("Fehler: Parameterwert überschreitet die maximale Länge von " + MAX_PARAM_LENGTH + " Zeichen: " + keyValue[1]);
                            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                            return data;
                        }

                        // **Prüfung des Parameternamens gemäß Java-Konventionen**
                        if (!METHOD_NAME_PATTERN.matcher(keyValue[0]).matches()) {
                            mout.println("Fehler: Ungültiger Parametername nach Java-Konventionen: " + keyValue[0]);
                            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                            return data;
                        }

                        // **Einheitliche Fehlermeldung bei doppelten Parametern**
                        if (data.resourceMethodParameters.containsKey(keyValue[0])) {
                            mout.println("Fehler: Doppelter Parameter '" + keyValue[0] + "' erkannt. Bitte korrigieren.");
                            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                            return data;
                        }

                        data.resourceMethodParameters.put(keyValue[0], keyValue[1]);
                    }
                }
            } else {
                // Wenn kein `?` vorhanden ist, dann ist es nur eine Ressource
                resourcePath = path;

                if (!PATH_PATTERN.matcher(resourcePath).matches()) {
                    mout.println("Fehler: Ungültiger Ressourcenpfad: '=' und '&' sind nur in Query-Parametern erlaubt.");
                    data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                    return data;
                }
            }

            // Setze die extrahierten Werte ins `data`-Objekt
            data.resourcePath = resourcePath;
            data.resourceMethod = methodName;

            mout.println("Url-Syntax ist korrekt!");
            mout.println("Ressource: " + resourcePath);
            mout.println("resourceMethod: " + methodName);
            mout.println("Query-Parameter: " + parameterPart);
            return data;
        }
    }


}





