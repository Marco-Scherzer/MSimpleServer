package com.marcoscherzer.msimpleserver.http.request;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public interface MResourceMethod<T> {
    byte[] call(T params);
    //HashMap<String,String> mapParamsIfStrucutured(String bodyOrUrlParams);

    T mapParamsIfStructured(String bodyOrUrlParams);
}
