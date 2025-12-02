package com.marcoscherzer.msimpleserver.http.validation;

import com.marcoscherzer.msimpleserver.MProtocolVersion;
import com.marcoscherzer.msimpleserver.http.constants.MHttpMethod;
import com.marcoscherzer.msimpleserver.http.validation.MHttpVersion.MValidationPattern;

import java.util.regex.Pattern;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MHttpVersion extends MProtocolVersion<MValidationPattern> {


    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private Pattern METHOD_PATTERN = Pattern.compile("^(GET|POST|PUT|DELETE|OPTIONS|HEAD|PATCH|CONNECT|TRACE)$");

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected MHttpVersion(String version, MHttpMethod... methods) {
        super(version);
        setSupportedMethods(methods);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final Pattern getSupportedMethods() {
        return METHOD_PATTERN;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MHttpVersion setSupportedMethods(MHttpMethod... supportedMethods) {
        StringBuffer methodsString = new StringBuffer();
        for (MHttpMethod method : supportedMethods) {
            methodsString.append(method).append("|");
        }
        METHOD_PATTERN = Pattern.compile("^(" + methodsString + ")$");
        return this;
    }


    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public enum MValidationPattern {
        HOST_HEADER("Host: ", "^Host: [a-zA-Z0-9.-]+(:[0-9]+)?$"),
        USER_AGENT_HEADER("User-Agent: ", "^User-Agent: [a-zA-Z0-9\\-\\s/\\.]+$"),
        ACCEPT_HEADER("Accept: ", "^(Accept: )?((\\*|\\*/\\*|[a-zA-Z0-9!#$%&'*+.^_`|~-]+/[a-zA-Z0-9!#$%&'*+.^_`|~-]+)(;[a-zA-Z0-9!#$%&'*+.^_`|~-]+=[a-zA-Z0-9!#$%&'*+.^_`|~-]+)*)(,\\s*(\\*|\\*/\\*|[a-zA-Z0-9!#$%&'*+.^_`|~-]+/[a-zA-Z0-9!#$%&'*+.^_`|~-]+)(;[a-zA-Z0-9!#$%&'*+.^_`|~-]+=[a-zA-Z0-9!#$%&'*+.^_`|~-]+)*)*$"),
        ACCEPT_LANGUAGE_HEADER("Accept-Language: ", "^Accept-Language: [a-zA-Z0-9,-;=\\s]+$"),
        ACCEPT_ENCODING_HEADER("Accept-Encoding: ", "^Accept-Encoding: [a-zA-Z0-9,;\\s]+$"),
        CONTENT_LENGTH_HEADER("Content-Length: ", "^Content-Length: [0-9]+$"),
        CONTENT_TYPE_HEADER("Content-Type: ", "^Content-Type: [a-zA-Z0-9/+-]+$"),
        REFERER_HEADER("Referer: ", "^Referer: https?://[a-zA-Z0-9.-]+(:[0-9]+)?(/.*)?$"),
        AUTHORIZATION_HEADER("Authorization: ", "^Authorization: [a-zA-Z0-9+/=\\s-]+$"),
        CACHE_CONTROL_HEADER("Cache-Control: ", "^Cache-Control: [a-zA-Z0-9,=\\s-]+$"),
        CONNECTION_HEADER("Connection: ", "^Connection: [a-zA-Z0-9,\\s-]+$"),
        COOKIE_HEADER("Cookie: ", "^Cookie: [a-zA-Z0-9=;\\s-]+$"),
        DNT_HEADER("DNT: ", "^DNT: [01]$"),
        IF_MODIFIED_SINCE_HEADER("If-Modified-Since: ", "^If-Modified-Since: .+$"),
        IF_NONE_MATCH_HEADER("If-None-Match: ", "^If-None-Match: .+$"),
        RANGE_HEADER("Range: ", "^Range: bytes=\\d*-\\d*(,\\d*-\\d*)*$"),
        TE_HEADER("TE: ", "^TE: [a-zA-Z0-9,;\\s]+$"),
        AGE_HEADER("Age: ", "^Age: [0-9]+$"),
        ALLOW_HEADER("Allow: ", "^Allow: ([A-Z]+)(, [A-Z]+)*$"),
        CONTENT_ENCODING_HEADER("Content-Encoding: ", "^Content-Encoding: [a-zA-Z0-9,\\s-]+$"),
        CONTENT_LANGUAGE_HEADER("Content-Language: ", "^Content-Language: [a-zA-Z0-9,\\s-]+$"),
        CONTENT_LOCATION_HEADER("Content-Location: ", "^Content-Location: .+$"),
        CONTENT_MD5_HEADER("Content-MD5: ", "^Content-MD5: [a-zA-Z0-9+/=]+$"),
        CONTENT_RANGE_HEADER("Content-Range: ", "^Content-Range: bytes \\d*-\\d*/\\d*$"),
        ETAG_HEADER("ETag: ", "^ETag: .+$"),
        EXPECT_HEADER("Expect: ", "^Expect: 100-continue$"),
        IF_MATCH_HEADER("If-Match: ", "^If-Match: .+$"),
        IF_RANGE_HEADER("If-Range: ", "^If-Range: .+$"),
        IF_UNMODIFIED_SINCE_HEADER("If-Unmodified-Since: ", "^If-Unmodified-Since: .+$"),
        LAST_MODIFIED_HEADER("Last-Modified: ", "^Last-Modified: .+$"),
        LINK_HEADER("Link: ", "^Link: .+$"),
        LOCATION_HEADER("Location: ", "^Location: .+$"),
        PROXY_AUTHENTICATE_HEADER("Proxy-Authenticate: ", "^Proxy-Authenticate: .+$"),
        PROXY_AUTHORIZATION_HEADER("Proxy-Authorization: ", "^Proxy-Authorization: .+$"),
        RETRY_AFTER_HEADER("Retry-After: ", "^Retry-After: .+$"),
        TRAILER_HEADER("Trailer: ", "^Trailer: .+$"),
        TRANSFER_ENCODING_HEADER("Transfer-Encoding: ", "^Transfer-Encoding: .+$"),
        UPGRADE_HEADER("Upgrade: ", "^Upgrade: .+$"),
        VARY_HEADER("Vary: ", "^Vary: .+$"),
        VIA_HEADER("Via: ", "^Via: .+$"),
        WARNING_HEADER("Warning: ", "^Warning: .+$"),
        __AUTHORITY_HEADER(":authority", "^:authority: .+$"),
        __METHOD_HEADER(":method", "^:method: .+$"),
        __PATH_HEADER(":path", "^:path: .+$"),
        __SCHEME_HEADER(":scheme", "^:scheme: .+$"),
        __STATUS_HEADER(":status", "^:status: .+$");

        private final String headerName;
        private final String pattern;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MValidationPattern(String headerName, String pattern) {
            this.headerName = headerName;
            this.pattern = pattern;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final String getHeaderName() {
            return headerName;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final String getPattern() {
            return pattern;
        }
    }


    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    /*public void addSupportedHeader(String headerNameList){}*/


}  
   



