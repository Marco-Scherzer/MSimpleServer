package com.marcoscherzer.msimpleserver.http.validation;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._302_FOUND;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._400_BAD_REQUEST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._405_METHOD_NOT_ALLOWED;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._505_HTTP_VERSION_NOT_SUPPORTED;
import static com.marcoscherzer.msimpleserver.http.validation.MParameterMode.POST;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;
import com.marcoscherzer.msimpleserver.MRequestValidator;
import com.marcoscherzer.msimpleserver.http.constants.MHttpContentType;
import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;
import com.marcoscherzer.msimpleserver.http.request.MHttpContentMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.net.ssl.SSLSocket;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpRequestValidator extends MRequestValidator<MHttpRequestData, MHttpVersion> {
    //private static Pattern INVALID_CHARACTERS = Pattern.compile("[^\\x20-\\x7E]");
    private static final Pattern INVALID_CHARACTERS = Pattern.compile("[^\\x20-\\x7E\\r\\n]");
    private MHttpContentMap contentMap;
    private boolean upgradeUnencrypted;
    private MHttpResponseStatusCodes[] errorsToSendPagesForInsteadOfPlain;
    private final MParameterMode mode;

    private final MUrlApiValidator urlParser = new MUrlApiValidator();
    private final MHttpHeaderValidator headerVal = new MHttpHeaderValidator();
    private final MPayloadValidator postVal = new MPayloadValidator(urlParser);

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestValidator(MParameterMode mode, MHttpVersion... supportedProtocols) {
        super(supportedProtocols);
        this.mode = mode;
    }


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void addHandler(MHttpContentType type, MContentTypeHandler handler){
        this.postVal.addHandler(type, handler);
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
    public final MHttpRequestValidator setMaxHeaderSize(int maxHeaderSize) {
        headerVal.setMaxHeaderSize(maxHeaderSize);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MHttpRequestValidator setMaxUrlLength(int maxUrlLength) {
        urlParser.setMaxUrlLength(maxUrlLength);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MHttpRequestValidator setUpgradeUnencrypted(boolean upgradeUnencrypted) {
        this.upgradeUnencrypted = upgradeUnencrypted;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer,
     * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
     * Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public final MHttpRequestData isValidRequest(Socket socket) {
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
                headerVal.validateHeaders(version, lines, outData);
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

                    postVal.validatePost(bodyBytes, outData);
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

}





