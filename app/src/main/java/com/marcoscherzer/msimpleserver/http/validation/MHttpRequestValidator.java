package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._302_FOUND;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._400_BAD_REQUEST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._405_METHOD_NOT_ALLOWED;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._413_PAYLOAD_TOO_LARGE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._415_UNSUPPORTED_MEDIA_TYPE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._505_HTTP_VERSION_NOT_SUPPORTED;
import static com.marcoscherzer.msimpleserver.http.validation.MParameterMode.POST;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.MRequestValidator;
import com.marcoscherzer.msimpleserver.http.constants.MHttpContentType;
import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;
import com.marcoscherzer.msimpleserver.http.request.MHttpContentMap;
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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.net.ssl.SSLSocket;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator(MParameterMode mode, MHttpVersion... supportedProtocols) {
        super(supportedProtocols);
        this.mode = mode;
    }
//private static final Pattern HEADER_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+:\\s.*$");

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static String sanitizeLine(String line) {
        return line.replaceAll(INVALID_CHARACTERS.toString(), ""); // Entfernt nicht druckbare Zeichen
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isInvalidCharacter(char ch) {
        return INVALID_CHARACTERS.matcher(Character.toString(ch)).find();
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setMaxHeaderSize(int maxHeaderSize) {
        MAX_HEADER_SIZE = maxHeaderSize;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setMaxUrlLength(int maxUrlLength) {
        urlParser.setMaxUrlLength(maxUrlLength);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator setUpgradeUnencrypted(boolean upgradeUnencrypted) {
        this.upgradeUnencrypted = upgradeUnencrypted;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer,
     * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
     * Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public MHttpRequestData isValidRequest(Socket socket) {
        MHttpRequestData outData = new MHttpRequestData();
        outData.setMode(mode);
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
                    outData.setResponseCode(_400_BAD_REQUEST);
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
                outData.setResponseCode(_400_BAD_REQUEST);
                return outData;
            }

            String[] parts = lines[0].split(" ");
            if (parts.length < 3) {
                mout.println("Fehler: Das Format der Request-Line ist falsch. Überprüfte Teile: " + Arrays.toString(parts));
                outData.setResponseCode(_400_BAD_REQUEST);
                return outData;
            }

            String method = parts[0];
            String protocol = parts[2];

            MHttpVersion version = null;
            for (int i = 0; i < getSupportedProtocols().size(); i++) {
                version = getSupportedProtocols().get(i);
                if (version.getVersion() == outData.getProtocol()) break;
            }

            // Überprüfe Protokoll
            if (!this.getSupportedProtocolsPattern().matcher(protocol).matches()) {
                mout.println("Fehler: Ungültiges Protokoll: " + protocol);
                outData.setResponseCode(_505_HTTP_VERSION_NOT_SUPPORTED);
                return outData;
            }
            //(Protokoll ist vorhanden und supported)

            // Überprüfe Methode
            if (!version.getSupportedMethods().matcher(method).matches()) {
                mout.println("Fehler: Ungültige Methode: " + method);
                outData.setResponseCode(_405_METHOD_NOT_ALLOWED);
                return outData;
            }

            //Überprüfe Url und setze data-attribute für url meth name und query-parameters oder return errorResponseCode
            outData = urlParser.parseUrl(parts[1], outData);
            if (outData.getResponseCode()!= null) return outData;

            outData.setRequestMethod(method);
            outData.setProtocol(protocol);

            // Upgrade unsicherer Verbindungen
            if (!(socket instanceof SSLSocket) && upgradeUnencrypted) {
                mout.println("Fehler: Request kam über unsicheres http");
                outData.setResponseCode(_302_FOUND);
                return outData;
            }

            //----------------------------------------- HTTP-Version > 0.9 -------------------------------------------
            if(!version.getVersion().equals("0.9")) {
                // Überprüfe Header
                validateHeaders(version, lines, outData);
                if (outData.getResponseCode() != VALID_AND_COMPLETE) return outData;

                /*
                 * benutzungskonventionsdefinition: POST wirkt überschreibend bzgl kompletter URL-Parameter
                 * diese werden neu gesetzt
                 */

                //toDo
                if (mode == POST) { //fester api-mode auf server definierbar(klarer und sicherer), möglichkeiten via supported wäre wegen kombination get post komplexer
                    // Content-Length aus Header holen
                    String cl = outData.getHeaders().get("Content-Length");
                    if (cl == null) {
                        mout.println("Fehler: Content-Length fehlt.");
                        outData.setResponseCode(_400_BAD_REQUEST);
                        return outData;
                    }
                    int contentLength;
                    try {
                        contentLength = Integer.parseInt(cl.trim());
                    } catch (NumberFormatException e) {
                        mout.println("Fehler: Ungültiger Content-Length.");
                        outData.setResponseCode(_400_BAD_REQUEST);
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
                        outData.setResponseCode(_400_BAD_REQUEST);
                        return outData;
                    }

                    validatePost(bodyBytes, outData);
                    if (outData.getResponseCode() != VALID_AND_COMPLETE) return outData;
                }

                outData.setResponseCode(VALID_AND_COMPLETE);
            }
        } catch (UnsupportedEncodingException exc) {
            mout.println("Fehler: Nicht unterstützte URL Kodierung - ");
            exc.printStackTrace(mout);
            outData.setResponseCode(_400_BAD_REQUEST);
            return outData;
        } catch (IOException exc) {
            mout.println("Fehler beim Lesen des InputStreams. IO-Exception - ");
            exc.printStackTrace(mout);
            outData.setResponseCode(_400_BAD_REQUEST);
            return outData;
        }

        return outData;
    }


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer,
     * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
     * Copyright Marco Scherzer, All rights reserved
     * todo UNGETESTET , erste ideen skizze
     */
    private void validatePost(byte[] bodyBytes, MHttpRequestData outData) {
        String contentType = outData.getHeaders().get("Content-Type");

        if (contentType == null) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(bodyBytes)) {
                contentType = URLConnection.guessContentTypeFromStream(bais);
            } catch (IOException e) {
                mout.println("Fehler: Content-Type konnte nicht bestimmt werden.");
                outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE);
                return;
            }
        }

        if (bodyBytes == null || bodyBytes.length == 0) {
            mout.println("Fehler: POST-Body fehlt oder leer.");
            outData.setResponseCode(_400_BAD_REQUEST);
            return;
        }

        // Charset-Parameter entfernen, falls vorhanden
        String baseCt = contentType.split(";")[0].trim().toLowerCase();
        String charsetName = "UTF-8"; // Default
        if (contentType.toLowerCase().contains("charset=")) {
            String[] parts = contentType.split("charset=");
            if (parts.length > 1) {
                charsetName = parts[1].trim();
            }
        }

        Charset charset;
        try {
            charset = Charset.forName(charsetName);
        } catch (Exception e) {
            mout.println("Fehler: Ungültiges Charset im Content-Type: " + charsetName);
            outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE);
            return;
        }

        // Enum aus Header-String bestimmen
        MHttpContentType type = null;
        for (MHttpContentType ctEnum : MHttpContentType.values()) {
            if (ctEnum.getValue().equalsIgnoreCase(baseCt)) {
                type = ctEnum;
                break;
            }
        }

        if (type != null) {
            MContentTypeHandler handler = handlers.get(type);
            if (handler != null) {
                MHttpResponseStatusCodes responseCode = handler.handle(bodyBytes, charset, outData);
                //outDataCheck

            } else {
                mout.println("Fehler: Kein Handler für Content-Type: " + type.getValue());
                outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE);
            }
        } else {
            mout.println("Fehler: Unsupported Content-Type: " + contentType);
            outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE);
        }
    }


    private final Map<MHttpContentType, MContentTypeHandler > handlers = new HashMap<>();

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Fügt einen Content-Type-Handler zur internen Map hinzu.
     * @param type der Content-Type als Enum (z.B. MHttpContentType.APPLICATION_JSON)
     * @param handler die Handler-Instanz, die diesen Content-Type verarbeitet
     */
    public void addHandler(MHttpContentType type, MContentTypeHandler handler) {
        if (type == null || handler == null) {
            throw new IllegalArgumentException("Content-Type und Handler dürfen nicht null sein.");
        }
        handlers.put(type, handler);
        mout.println("Handler für Content-Type '" + type.getValue() + "' registriert.");
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
                outData.setResponseCode(_413_PAYLOAD_TOO_LARGE);
                return;
            }

            // Mehrzeilige Header berücksichtigen
            while (i < lines.length - 1 && (lines[i + 1].startsWith(" ") || lines[i + 1].startsWith("\t"))) {
                line += " " + lines[++i].trim();
                headerSize += lines[i].length();
                if (!checkHeaderSize(httpVersion, headerSize)) {
                    outData.setResponseCode(_413_PAYLOAD_TOO_LARGE);
                    return;
                }
            }

            // Validierung spezifischer Header
            mout.println("Checking specific header: " + line);
            for (MValidationPattern p : httpVersion.getValidationEntrySet()) {  //httpVersion.
                if (validateHeader(line, p)) {
                    String[] headerParts = line.split(":", 2);
                    if (headerParts.length == 2) {
                        outData.getHeaders().put(headerParts[0].trim(), headerParts[1].trim());
                        break;
                    }
                } else outData.setResponseCode(_400_BAD_REQUEST);
            }
        }
        outData.setResponseCode(VALID_AND_COMPLETE);
        return;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private boolean checkHeaderSize(MHttpVersion httpVersion, int headerSize) {
        mout.println("Checking header size: " + headerSize);
        if (headerSize > MAX_HEADER_SIZE) {
            mout.println("Error: Header size exceeds the allowed limit. Checked header size: " + headerSize);
            return false;
        }
        return true;
    }
}





