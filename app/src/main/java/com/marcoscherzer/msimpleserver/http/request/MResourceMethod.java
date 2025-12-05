package com.marcoscherzer.msimpleserver.http.request;

import java.util.Map;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
@FunctionalInterface
public interface MResourceMethod {
    byte[] call(Map<String,String> params);
}
