package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.GET;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttp_0_9 extends MHttpVersion {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttp_0_9() {
        super("HTTP/0.9", GET);
        this.headerMap.put("Host: ", MValidationPattern.HOST_HEADER);
        this.headerMap.put("User-Agent: ", MValidationPattern.USER_AGENT_HEADER);
        this.headerMap.put("Accept: ", MValidationPattern.ACCEPT_HEADER);
        // Weitere Header für HTTP/0.9 ergänzen, falls nötig
    }
}
