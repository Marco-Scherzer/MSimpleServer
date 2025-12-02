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
public final class MHttp_3_0 extends MHttpVersion {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttp_3_0() {
        super("HTTP/3.0", GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, CONNECT, PATCH);
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
        // HTTP/2.0 und HTTP/3.0 spezifische Header
        this.headerMap.put(":authority", MHttpVersion.MValidationPattern.__AUTHORITY_HEADER);
        this.headerMap.put(":method", MHttpVersion.MValidationPattern.__METHOD_HEADER);
        this.headerMap.put(":path", MHttpVersion.MValidationPattern.__PATH_HEADER);
        this.headerMap.put(":scheme", MHttpVersion.MValidationPattern.__SCHEME_HEADER);
        this.headerMap.put(":status", MHttpVersion.MValidationPattern.__STATUS_HEADER);
    }
}