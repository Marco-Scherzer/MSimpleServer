package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._415_UNSUPPORTED_MEDIA_TYPE;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;

import java.nio.charset.Charset;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * Sammlung von Standard-Content-Type-Handlern.
 */
final class MContentTypeHandlers {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Handler für application/x-www-form-urlencoded
     */
    static final MContentTypeHandler FormUrlEncodedHandler = new MContentTypeHandler() {
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
}

