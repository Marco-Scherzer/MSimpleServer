package com.marcoscherzer.msimpleserver.http.request;

import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestData;
import com.marcoscherzer.msimpleserver.http.validation.MParameterMode;
import com.marcoscherzer.msimpleserver.util.logging.MNullPrintStringWriter;

import java.io.PrintWriter;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpRequest {
    private final String requestMethod;
    private final String resourcePath;
    private final String endpointQuery;
    private final String protocol;
    private final MHttpRequestHeader headers;
    private String body;
    private final MParameterMode mode;

    public PrintWriter out = new MNullPrintStringWriter();
    private MParameterMap resourceMethodParameters = new MParameterMap();


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequest(final MHttpRequestData requestData) {
        this.requestMethod = requestData.getRequestMethod();
        this.resourcePath = requestData.getResourcePath();
        this.endpointQuery = requestData.getEndpointQuery();
        this.protocol = requestData.getProtocol();
        this.headers = new MHttpRequestHeader(requestData.getHeaders());
        this.resourceMethodParameters = requestData.getResourceMethodParameters();

        this.mode = requestData.getMode();
        //this.body = "";
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
    public MParameterMap getResourceMethodParameters() {
        return resourceMethodParameters;
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
    public String getResourceMethod() {
        return endpointQuery;
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
    public MHttpRequestHeader getHeaders() {
        return headers;
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
    public String getBody() {
        return body;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();
        result.append("Method: ").append(requestMethod).append("\n");
        result.append("Path: ").append(resourcePath).append("\n");
        result.append("Protocol: ").append(protocol).append("\n");
        result.append("Headers: ").append("\n");
        for (final Map.Entry<String, String> header : headers.entrySet()) {
            result.append("  ").append(header.getKey()).append(": ").append(header.getValue()).append("\n");
        }
        for (final Map.Entry<String, Object> resourceMethodParameter : resourceMethodParameters.entrySet()) {
            result.append("  ").append(resourceMethodParameter.getKey()).append(": ").append(resourceMethodParameter.getValue()).append("\n");
        }
        if (body != null && !body.isEmpty()) {
            result.append("Body: ").append("\n").append(body);
        }
        return result.toString();
    }

}


