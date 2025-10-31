package com.marcoscherzer.msimpleserver.http.request;

import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestValidator.MHttpRequestData;
import com.marcoscherzer.msimpleserver.util.logging.MNullPrintStringWriter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Author Marco Scherzer (Description, Definition, writing) with Microsoft Copilot (writing),
 * PostAuthor, Ideas & Architectures Marco Scherzer
 * Copyright Marco Scherzer, All rights reserved
 * history: msimplehttpserver.request.MRequest
 * Evtl später doch nur entweder MHttpRequestValidator.MHttpRequestData oder MHttpRequest
 */
public final class MHttpRequest {
    private final String requestMethod;
    private final String resourcePath;
    private final String endpointQuery;
    private final String protocol;
    private final MHttpRequestHeader headers;
    private final String body;
    public PrintWriter out = new MNullPrintStringWriter();
    private Map<String, String> resourceMethodParameters = new HashMap<>();

    /**
     @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
      *  Evtl später doch nur entweder MHttpRequestData oder MHttpRequestValidator.MHttpRequestData
      * @param inputStream Der Eingabestream.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     */
    /**
     * @param requestData Die HTTP-Request-Daten.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequest(final MHttpRequestData requestData) {
        this.requestMethod = requestData.getRequestMethod();
        this.resourcePath = requestData.getResourcePath();
        this.endpointQuery = requestData.getEndpointQuery();
        this.protocol = requestData.getProtocol();
        this.headers = new MHttpRequestHeader(requestData.getHeaders());
        this.resourceMethodParameters = requestData.getResourceMethodParameters();
        this.body = "";
    }

    /**
     * @return Die HTTP-Methode.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * @return Die HTTP-Methode.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Map<String, String> getResourceMethodParameters() {
        return resourceMethodParameters;
    }

    /**
     * @return Der Pfad.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * @return Der Pfad.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getResourceMethod() {
        return endpointQuery;
    }

    /**
     * @return Das Protokoll.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return Die Header der Anfrage.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestHeader getHeaders() {
        return headers;
    }

    /**
     * @return Der Body der Anfrage.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getBody() {
        return body;
    }

    /**
     * @return Eine String-Darstellung der Anfrage.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
        for (final Map.Entry<String, String> resourceMethodParameter : resourceMethodParameters.entrySet()) {
            result.append("  ").append(resourceMethodParameter.getKey()).append(": ").append(resourceMethodParameter.getValue()).append("\n");
        }
        if (body != null && !body.isEmpty()) {
            result.append("Body: ").append("\n").append(body);
        }
        return result.toString();
    }

}


