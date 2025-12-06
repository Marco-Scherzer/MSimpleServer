package com.marcoscherzer.msimpleserver.http.validation;

import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpRequestData {
    private final Map<String, String> headers = new HashMap<>();
    private byte[] bodyBytes;
    private MParameterMode mode;
    //private boolean validAndComplete;
    private final Map<String, String> resourceMethodParameters = new HashMap<>();
    private String requestMethod;
    private String resourcePath;
    private String resourceMethod;
    private String protocol;
    private MHttpResponseStatusCodes responseCode;


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    void setResponseCode(MHttpResponseStatusCodes responseCode) { this.responseCode = responseCode; }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    void setRequestMethod(String method){ this.requestMethod = method; }
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    void setProtocol(String  protocol){ this.protocol = protocol; }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    void setMode(MParameterMode  protocol){ this.mode = protocol; }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MParameterMode getMode() {
        return mode;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getEndpointQuery() {
        return resourceMethod;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Map<String, String> getResourceMethodParameters() {
        return resourceMethodParameters;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseStatusCodes getResponseCode() {
        return responseCode;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Method: ").append(requestMethod).append("\n");
        sb.append("ResourcePath: ").append(resourcePath).append("\n");
        sb.append("EndpointQuery: ").append(resourceMethod).append("\n");
        sb.append("Protocol: ").append(protocol).append("\n");
        sb.append("Headers: ").append("\n");
        for (final Map.Entry<String, String> header : headers.entrySet()) {
            sb.append("  ").append(header.getKey()).append(": ").append(header.getValue()).append("\n");
        }
        for (final Map.Entry<String, String> resourceMethodParameter : resourceMethodParameters.entrySet()) {
            sb.append("  ").append(resourceMethodParameter.getKey()).append(": ").append(resourceMethodParameter.getValue()).append("\n");
        }
        sb.append("Internal Code or Error-ReponseCode: ").append(responseCode).append("\n");
        return sb.toString();
    }
}
