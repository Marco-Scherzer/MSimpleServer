package com.marcoscherzer.msimpleserver.http.response;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpReponseHeader {
    private final Map<String, String> headers = new HashMap<>();

    /**
     * @return Die Header-Map.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param key   Der Header-Name.
     * @param value Der Header-Wert.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Content-Type: "text/html; charset=UTF-8"
     * - Content-Length: "1234"
     * - Server: "Apache/2.4.1 (Unix)"
     * - Date: "Tue, 15 Nov 1994 08:12:31 GMT"
     * - Connection: "keep-alive"
     * - Cache-Control: "no-cache"
     * - Expires: "Thu, 01 Dec 1994 16:00:00 GMT"
     * - Location: "http://www.example.com/index.html"
     * - Set-Cookie: "sessionId=38afes7a8"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader addHeader(final String key, final String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * @param value Der Wert für den "Server"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Server: "Apache/2.4.1 (Unix)"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setServer(final String value) {
        return addHeader("Server", value);
    }

    /**
     * @param value Der Wert für den "Date"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Date: "Tue, 15 Nov 1994 08:12:31 GMT"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setDate(final String value) {
        return addHeader("Date", value);
    }

    /**
     * @param value Der Wert für den "Connection"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Connection: "keep-alive"
     * - Connection: "close"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setConnection(final String value) {
        return addHeader("Connection", value);
    }

    /**
     * @param value Der Wert für den "Cache-Control"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Cache-Control: "no-cache"
     * - Cache-Control: "no-store"
     * - Cache-Control: "max-age=3600"
     * - Cache-Control: "must-revalidate"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setCacheControl(final String value) {
        return addHeader("Cache-Control", value);
    }

    /**
     * @param value Der Wert für den "Expires"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Expires: "Thu, 01 Dec 1994 16:00:00 GMT"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setExpires(final String value) {
        return addHeader("Expires", value);
    }

    /**
     * @param value Der Wert für den "Location"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Location: "http://www.example.com/index.html"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setLocation(final String value) {
        return addHeader("Location", value);
    }

    /**
     * @param value Der Wert für den "Set-Cookie"-Header.
     * @return Die aktuelle Instanz von MHeader.
     * Mögliche HTTP-Response-Header:
     * - Set-Cookie: "sessionId=38afes7a8"
     * - Set-Cookie: "userId=abc123"
     * - Set-Cookie: "authToken=xyz789"
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader setSetCookie(final String value) {
        return addHeader("Set-Cookie", value);
    }
}


