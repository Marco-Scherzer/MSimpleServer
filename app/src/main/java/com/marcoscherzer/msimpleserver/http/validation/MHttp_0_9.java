package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.GET;

/**
 * Author Marco Scherzer: Descriptions, Definitions, Architectures, Authoring
 * Microsoft Copilot: Regex on Request/Filling out the Map in SubClasses on Request
 * Copyright Marco Scherzer, All rights reserved
 * history: httpvalidation.MHttp_0_9
 */
public final class MHttp_0_9 extends MHttpVersion {

    public MHttp_0_9() {
        super("HTTP/0.9", GET);
        this.headerMap.put("Host: ", MValidationPattern.HOST_HEADER);
        this.headerMap.put("User-Agent: ", MValidationPattern.USER_AGENT_HEADER);
        this.headerMap.put("Accept: ", MValidationPattern.ACCEPT_HEADER);
        // Weitere Header für HTTP/0.9 ergänzen, falls nötig
    }
}
