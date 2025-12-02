package com.marcoscherzer.msimpleserver.http.response;

import java.io.IOException;
import java.util.Map;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpResponse {
    private final MHttpReponseHeader header = new MHttpReponseHeader();
    private final MHttpResponseBody contentHeaderAndBody = new MHttpResponseBody();
    private String protocol = "HTTP/1.1";
    private String statusCode = "200 OK";

    /**
     * @return Die Header der Antwort.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpReponseHeader getHeader() {
        return header;
    }

    /**
     * @return Der Body der Antwort.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponseBody getBody() {
        return contentHeaderAndBody;
    }

    /**
     * @param protocol Das Protokoll.
     * @return Die aktuelle Instanz von MHttpResponse.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponse setProtocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * @param statusCode Der Statuscode.
     * @return Die aktuelle Instanz von MHttpResponse.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResponse setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    /**
     * @return Das HTTP/HTTPS-Antwortbytearray.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public byte[] create() throws IOException {
        byte[] body = contentHeaderAndBody.createEncodedBody();

        StringBuilder response = new StringBuilder();
        response.append(protocol).append(" ").append(statusCode).append("\r\n");
        for (Map.Entry<String, String> header : header.getHeaders().entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        for (Map.Entry<String, String> header : contentHeaderAndBody.getHeaders().entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        response.append("\r\n");
        byte[] headerBytes = response.toString().getBytes();
        byte[] fullResponse = new byte[headerBytes.length + body.length];

        System.arraycopy(headerBytes, 0, fullResponse, 0, headerBytes.length);
        System.arraycopy(body, 0, fullResponse, headerBytes.length, body.length);

        return fullResponse;
    }

    /**
     * @return Eine String-Darstellung der Antwort.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();

        if (protocol != null && statusCode != null) {
            response.append(protocol).append(" ").append(statusCode).append("\r\n");
        }

        if (header != null && header.getHeaders() != null) {
            for (Map.Entry<String, String> headerEntry : header.getHeaders().entrySet()) {
                if (headerEntry.getKey() != null && headerEntry.getValue() != null) {
                    response.append(headerEntry.getKey()).append(": ").append(headerEntry.getValue()).append("\r\n");
                }
            }
        }

        if (contentHeaderAndBody != null && contentHeaderAndBody.getHeaders() != null) {
            for (Map.Entry<String, String> headerEntry : contentHeaderAndBody.getHeaders().entrySet()) {
                if (headerEntry.getKey() != null && headerEntry.getValue() != null) {
                    response.append(headerEntry.getKey()).append(": ").append(headerEntry.getValue()).append("\r\n");
                }
            }
        }

        response.append("\r\n");

        if (contentHeaderAndBody != null && contentHeaderAndBody.getUnencodedBody() != null && contentHeaderAndBody.getContentCharset() != null) {
            response.append(new String(contentHeaderAndBody.getUnencodedBody(), contentHeaderAndBody.getContentCharset()));
        }

        return response.toString();
    }


    /**
     @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
      * @param args Die Befehlszeilenargumente.
     */
    /*public static void main(String[] args) {
        try {
            mout.println("---------------------------------------------------------");
            MHttpResponse response = new MHttpResponse()
                    .setProtocol("HTTP/1.1")
                    .setStatusCode("200 OK");
            response.getBody().setContent("Hello, World!".getBytes(), StandardCharsets.UTF_8, "text/html", IDENTITY.getValue());
            
            mout.println("\"" + new String(response.create()) + "\"");
            
            mout.println("---------------------------------------------------------");
            response = new MHttpResponse()
                    .setProtocol("HTTP/1.1")
                    .setStatusCode("200 OK");
            response.getBody().setContent("Hello, World!".getBytes(), StandardCharsets.UTF_8, "text/html", GZIP.getValue());
            mout.println("\"" + new String(response.create()) + "\"");
            
            mout.println("---------------------------------------------------------");
            response = new MHttpResponse()
                    .setProtocol("HTTP/1.1")
                    .setStatusCode("200 OK");
            response.getBody().setContent("Hello, World!".getBytes(), StandardCharsets.UTF_8, "text/html", DEFLATE.getValue());
            mout.println("\"" + new String(response.create()) + "\"");
            
            mout.println("---------------------------------------------------------");
            mout.println(new String(response.toString()));
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }*/
}

