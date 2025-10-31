package com.marcoscherzer.msimpleserver.http.constants;

/*
Author Marco Scherzer (Description,Definition) with Microsoft Copilot (adding up Constants),
PostAuthor, Ideas & Architectures Marco Scherzer
Copyright Marco Scherzer, All rights reserved
History: msimplehttpserver.MContentType 
*/
public enum MHttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    OPTIONS("OPTIONS"),
    HEAD("HEAD"),
    PATCH("PATCH"),
    CONNECT("CONNECT"),
    TRACE("TRACE");

    private final String methodString;

    MHttpMethod(String methodString) {
        this.methodString = methodString;
    }

    @Override
    public String toString() {
        return this.methodString;
    }

    public String getValue() {
        return this.methodString;
    }
}
