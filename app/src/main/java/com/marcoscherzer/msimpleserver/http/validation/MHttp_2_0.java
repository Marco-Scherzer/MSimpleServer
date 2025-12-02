package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.CONNECT;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.DELETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.GET;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.HEAD;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.OPTIONS;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.PATCH;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.POST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.PUT;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.TRACE;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttp_2_0 extends MHttpVersion {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttp_2_0(String version) {
        super("HTTP/2.0", GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, CONNECT, PATCH);
        this.headerMap.put("Host: ", MHttpVersion.MValidationPattern.HOST_HEADER);
        this.headerMap.put("User-Agent: ", MHttpVersion.MValidationPattern.USER_AGENT_HEADER);
        this.headerMap.put("Accept: ", MHttpVersion.MValidationPattern.ACCEPT_HEADER);
        this.headerMap.put("Accept-Language: ", MHttpVersion.MValidationPattern.ACCEPT_LANGUAGE_HEADER);
        this.headerMap.put("Accept-Encoding: ", MHttpVersion.MValidationPattern.ACCEPT_ENCODING_HEADER);
        this.headerMap.put("Content-Length: ", MHttpVersion.MValidationPattern.CONTENT_LENGTH_HEADER);
        this.headerMap.put("Content-Type: ", MHttpVersion.MValidationPattern.CONTENT_TYPE_HEADER);
        this.headerMap.put("Referer: ", MHttpVersion.MValidationPattern.REFERER_HEADER);
        this.headerMap.put("Authorization: ", MHttpVersion.MValidationPattern.AUTHORIZATION_HEADER);
        this.headerMap.put("Cache-Control: ", MHttpVersion.MValidationPattern.CACHE_CONTROL_HEADER);
        this.headerMap.put("Connection: ", MHttpVersion.MValidationPattern.CONNECTION_HEADER);
        this.headerMap.put("Cookie: ", MHttpVersion.MValidationPattern.COOKIE_HEADER);
        this.headerMap.put("DNT: ", MHttpVersion.MValidationPattern.DNT_HEADER);
        this.headerMap.put("If-Modified-Since: ", MHttpVersion.MValidationPattern.IF_MODIFIED_SINCE_HEADER);
        this.headerMap.put("If-None-Match: ", MHttpVersion.MValidationPattern.IF_NONE_MATCH_HEADER);
        this.headerMap.put("Range: ", MHttpVersion.MValidationPattern.RANGE_HEADER);
        this.headerMap.put("TE: ", MHttpVersion.MValidationPattern.TE_HEADER);
        this.headerMap.put("Accept-Ranges: ", MHttpVersion.MValidationPattern.TE_HEADER); // Füllen Sie hier den richtigen Wert ein.
        this.headerMap.put("Age: ", MHttpVersion.MValidationPattern.AGE_HEADER);
        this.headerMap.put("Allow: ", MHttpVersion.MValidationPattern.ALLOW_HEADER);
        this.headerMap.put("Content-Encoding: ", MHttpVersion.MValidationPattern.CONTENT_ENCODING_HEADER);
        this.headerMap.put("Content-Language: ", MHttpVersion.MValidationPattern.CONTENT_LANGUAGE_HEADER);
        this.headerMap.put("Content-Location: ", MHttpVersion.MValidationPattern.CONTENT_LOCATION_HEADER);
        this.headerMap.put("Content-MD5: ", MHttpVersion.MValidationPattern.CONTENT_MD5_HEADER);
        this.headerMap.put("Content-Range: ", MHttpVersion.MValidationPattern.CONTENT_RANGE_HEADER);
        this.headerMap.put("ETag: ", MHttpVersion.MValidationPattern.ETAG_HEADER);
        this.headerMap.put("Expect: ", MHttpVersion.MValidationPattern.EXPECT_HEADER);
        this.headerMap.put("If-Match: ", MHttpVersion.MValidationPattern.IF_MATCH_HEADER);
        this.headerMap.put("If-Range: ", MHttpVersion.MValidationPattern.IF_RANGE_HEADER);
        this.headerMap.put("If-Unmodified-Since: ", MHttpVersion.MValidationPattern.IF_UNMODIFIED_SINCE_HEADER);
        this.headerMap.put("Last-Modified: ", MHttpVersion.MValidationPattern.LAST_MODIFIED_HEADER);
        this.headerMap.put("Link: ", MHttpVersion.MValidationPattern.LINK_HEADER);
        this.headerMap.put("Location: ", MHttpVersion.MValidationPattern.LOCATION_HEADER);
        this.headerMap.put("Proxy-Authenticate: ", MHttpVersion.MValidationPattern.PROXY_AUTHENTICATE_HEADER);
        this.headerMap.put("Proxy-Authorization: ", MHttpVersion.MValidationPattern.PROXY_AUTHORIZATION_HEADER);
        this.headerMap.put("Retry-After: ", MHttpVersion.MValidationPattern.RETRY_AFTER_HEADER);
        this.headerMap.put("Trailer: ", MHttpVersion.MValidationPattern.TRAILER_HEADER);
        this.headerMap.put("Transfer-Encoding: ", MHttpVersion.MValidationPattern.TRANSFER_ENCODING_HEADER);
        this.headerMap.put("Upgrade: ", MHttpVersion.MValidationPattern.UPGRADE_HEADER);
        this.headerMap.put("Vary: ", MHttpVersion.MValidationPattern.VARY_HEADER);
        this.headerMap.put("Via: ", MHttpVersion.MValidationPattern.VIA_HEADER);
        this.headerMap.put("Warning: ", MHttpVersion.MValidationPattern.WARNING_HEADER);
        // HTTP/2.0 spezifische Header
        this.headerMap.put(":authority", MHttpVersion.MValidationPattern.__AUTHORITY_HEADER);
        this.headerMap.put(":method", MHttpVersion.MValidationPattern.__METHOD_HEADER);
        this.headerMap.put(":path", MHttpVersion.MValidationPattern.__PATH_HEADER);
        this.headerMap.put(":scheme", MHttpVersion.MValidationPattern.__SCHEME_HEADER);
        this.headerMap.put(":status", MHttpVersion.MValidationPattern.__STATUS_HEADER);
    }
}