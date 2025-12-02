package com.marcoscherzer.msimpleserver.http.response;

import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.IDENTITY;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.util.compression.MCompression;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * Mögliche HTTP-Response-Header:
 * - Content-Type: "text/html; charset=UTF-8"
 * - Content-Encoding: "gzip"
 * - Content-Length: "1234"
 * - Content-Language: "en"
 * - Content-Disposition: "attachment; filename=\"example.pdf\""
 * - Content-Range: "bytes 200-1000/67589"
 * - Content-MD5: "Q2hlY2sgSW50ZWdyaXR5IQ=="
 * - Content-Location: "/documents/resume.pdf"
 * - Content-Security-Policy: "default-src 'self'"
 * - Content-Script-Type: "application/javascript"
 * - Content-Style-Type: "text/css"
 */
public final class MHttpResponseBody {

    private final Map<String, String> headers = new HashMap<>();
    private String contentEncoding = IDENTITY.getValue();
    private Charset contentCharset;
    private byte[] body = new byte[0];

    /**
     * @param key   Der Header-Name.
     * @param value Der Header-Wert.
     * @return Die aktuelle Instanz von MContentBody.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody addHeader(final String key, final String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * @param body            Der Inhalt.
     * @param contentCharset  Das Charset des Inhalts.
     * @param contentType     Der MIME-Typ des Inhalts.
     * @param contentEncoding Die Komprimierungsmethode des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     *                     Mögliche HTTP-Response-Header:
     *                     - Content-Type: "text/html; charset=UTF-8"
     *                     - Content-Encoding: "gzip"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContent(final byte[] body, Charset contentCharset, String contentType, String contentEncoding) throws IOException {
        this.body = body;
        headers.put("Content-Type", contentType);
        headers.put("Content-Encoding", contentEncoding);
        this.contentEncoding = contentEncoding;
        this.contentCharset = contentCharset;
        return this;
    }

    /**
     * @param contentLanguage Die Sprache des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Language: "en"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentLanguage(String contentLanguage) {
        headers.put("Content-Language", contentLanguage);
        return this;
    }

    /**
     * @param contentDisposition Die Disposition des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Disposition: "attachment; filename=\"example.pdf\""
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentDisposition(String contentDisposition) {
        headers.put("Content-Disposition", contentDisposition);
        return this;
    }

    /**
     * @param contentRange Der Inhaltsbereich.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Range: "bytes 200-1000/67589"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentRange(String contentRange) {
        headers.put("Content-Range", contentRange);
        return this;
    }

    /**
     * @param contentMD5 Der MD5-Hash des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-MD5: "Q2hlY2sgSW50ZWdyaXR5IQ=="
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentMD5(String contentMD5) {
        headers.put("Content-MD5", contentMD5);
        return this;
    }

    /**
     * @param contentLocation Die alternative URL des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Location: "/documents/resume.pdf"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentLocation(String contentLocation) {
        headers.put("Content-Location", contentLocation);
        return this;
    }

    /**
     * @param contentSecurityPolicy Die Sicherheitsrichtlinie des Inhalts.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Security-Policy: "default-src 'self'"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentSecurityPolicy(String contentSecurityPolicy) {
        headers.put("Content-Security-Policy", contentSecurityPolicy);
        return this;
    }

    /**
     * @param contentScriptType Der MIME-Typ der Skripte.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Script-Type: "application/javascript"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentScriptType(String contentScriptType) {
        headers.put("Content-Script-Type", contentScriptType);
        return this;
    }

    /**
     * @param contentStyleType Der MIME-Typ der Stile.
     * @return Die aktuelle Instanz von MContentBody.
     * Mögliche HTTP-Response-Header:
     * - Content-Style-Type: "text/css"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody setContentStyleType(String contentStyleType) {
        headers.put("Content-Style-Type", contentStyleType);
        return this;
    }

    /**
     * @return Das komprimierte byte[] des Inhalts.
     * unencodedBody falls body==null oder contentEncoding==null
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    byte[] createEncodedBody() throws IOException {
        byte[] compressedResourceBytes = body;
        if (body != null && body.length != 0) { //=content set
            try {
                mout.println("Trying to compress resource with contentEncoding=" + contentEncoding);
                compressedResourceBytes = MCompression.compress(contentEncoding, body);
            } catch (IOException exc) {
                mout.println("Error while compressing resource");
                exc.printStackTrace(mout);
                throw new IOException("Error while compressing resource");
            }
        }
        return compressedResourceBytes;
    }

    /**
     * @return Das unkodierte byte[] des Inhalts.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    byte[] getUnencodedBody() {
        return body;
    }

    /**
     * @return Die Header-Map.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @return Das Charset des Inhalts.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    Charset getContentCharset() {
        return this.contentCharset;
    }
}


