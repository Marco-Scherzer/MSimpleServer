package com.marcoscherzer.msimpleserver.http.validation;


import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.GET;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.HEAD;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.POST;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttp_1_0 extends MHttpVersion {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttp_1_0() {
        super("HTTP/1.0", GET, POST, HEAD);
        this.headerMap.put("Host: ", MValidationPattern.HOST_HEADER);
        this.headerMap.put("User-Agent: ", MValidationPattern.USER_AGENT_HEADER);
        this.headerMap.put("Accept: ", MValidationPattern.ACCEPT_HEADER);
        //
        this.headerMap.put("Accept-Language: ", MValidationPattern.ACCEPT_LANGUAGE_HEADER);
        this.headerMap.put("Accept-Encoding: ", MValidationPattern.ACCEPT_ENCODING_HEADER);
        this.headerMap.put("Content-Length: ", MValidationPattern.CONTENT_LENGTH_HEADER);
        this.headerMap.put("Content-Type: ", MValidationPattern.CONTENT_TYPE_HEADER);
        this.headerMap.put("Referer: ", MValidationPattern.REFERER_HEADER);
        this.headerMap.put("Authorization: ", MValidationPattern.AUTHORIZATION_HEADER);
        this.headerMap.put("Cache-Control: ", MValidationPattern.CACHE_CONTROL_HEADER);
        this.headerMap.put("Connection: ", MValidationPattern.CONNECTION_HEADER);
        this.headerMap.put("Cookie: ", MValidationPattern.COOKIE_HEADER);
        this.headerMap.put("DNT: ", MValidationPattern.DNT_HEADER);
        this.headerMap.put("If-Modified-Since: ", MValidationPattern.IF_MODIFIED_SINCE_HEADER);
        this.headerMap.put("If-None-Match: ", MValidationPattern.IF_NONE_MATCH_HEADER);
        this.headerMap.put("Range: ", MValidationPattern.RANGE_HEADER);
        this.headerMap.put("TE: ", MValidationPattern.TE_HEADER);
    }
}
     
