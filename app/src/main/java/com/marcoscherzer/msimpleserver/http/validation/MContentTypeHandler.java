package com.marcoscherzer.msimpleserver.http.validation;

import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;

import java.nio.charset.Charset;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public interface MContentTypeHandler { MHttpResponseStatusCodes handle(byte[] bodyBytes, Charset charset); }


