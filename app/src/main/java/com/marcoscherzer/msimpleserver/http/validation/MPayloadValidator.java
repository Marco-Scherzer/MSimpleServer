package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._400_BAD_REQUEST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._415_UNSUPPORTED_MEDIA_TYPE;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.http.constants.MHttpContentType;
import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * Handler für application/x-www-form-urlencoded
 */
final class MPayloadValidator {

private final Map<MHttpContentType, MContentTypeHandler > handlers = new HashMap<>();
    private MUrlApiValidator urlParser;
    MPayloadValidator(MUrlApiValidator urlParser){
        this.urlParser = urlParser;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Handler für application/x-www-form-urlencoded
     */
    final MContentTypeHandler FormUrlEncodedHandler = new MContentTypeHandler() {
        @Override
        public MHttpResponseStatusCodes handle(byte[] bodyBytes, Charset charset, MHttpRequestData outData) {
            String body = new String(bodyBytes, charset);
            String syntheticUrl = outData.getResourcePath() + "/" + outData.getEndpointQuery() + "?" + body;
            urlParser.parseUrl(syntheticUrl, outData);
            if (outData.getResponseCode() != VALID_AND_COMPLETE) {
                mout.println("Fehler beim Parsen des POST-Bodys.");
                return outData.getResponseCode();
            }
            outData.setResponseCode(VALID_AND_COMPLETE);
            return outData.getResponseCode();
        }
    };

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Handler für application/json
     */
    static final MContentTypeHandler JsonHandler = new MContentTypeHandler() {
        @Override
        public MHttpResponseStatusCodes handle(byte[] bodyBytes, Charset charset, MHttpRequestData outData) {
            String body = new String(bodyBytes, charset);
            mout.println("Hinweis: JSON-Body empfangen. Übergabe an Parser.");
            // TODO: später optionalen Handler einsetzen
            outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE); // derweil unsupported
            return outData.getResponseCode();
        }
    };

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Handler für application/octet-stream (Binärdaten)
     */
    static final MContentTypeHandler OctetStreamHandler = new MContentTypeHandler() {
        @Override
        public MHttpResponseStatusCodes handle(byte[] bodyBytes, Charset charset, MHttpRequestData outData) {
            mout.println("Hinweis: Binärdaten-Body empfangen.");
            // TODO: später optionalen Handler einsetzen
            outData.setResponseCode(_415_UNSUPPORTED_MEDIA_TYPE); // derweil unsupported
            return outData.getResponseCode();
        }
    };
/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer,
 * Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer,
 * Copyright Marco Scherzer, All rights reserved
 * todo UNGETESTET , erste ideen skizze
 */
final void validatePost(byte[] bodyBytes, MHttpRequestData outData) {
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


/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * Fügt einen Content-Type-Handler zur internen Map hinzu.
 * @param type der Content-Type als Enum (z.B. MHttpContentType.APPLICATION_JSON)
 * @param handler die Handler-Instanz, die diesen Content-Type verarbeitet
 */
final void addHandler(MHttpContentType type, MContentTypeHandler handler) {
    if (type == null || handler == null) {
        throw new IllegalArgumentException("Content-Type und Handler dürfen nicht null sein.");
    }
    handlers.put(type, handler);
    mout.println("Handler für Content-Type '" + type.getValue() + "' registriert.");
}

}
