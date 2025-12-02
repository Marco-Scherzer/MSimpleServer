package com.marcoscherzer.msimpleserver.http.request;

import static com.marcoscherzer.msimpleserver.http.request.MChoosableFieldsEvaluator.chooseBestField;
import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.IDENTITY;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpRequestHeader {
    private final Map<String, String> headers;

    /**
     * @param headers Die Header-Map.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MHttpRequestHeader(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @param key Der Schlüssel des Headers.
     * @return Der Wert des Headers.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String get(String key) {
        return headers.get(key);
    }

    /**
     * @return Die Header-Entry-Set.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    Set<Entry<String, String>> entrySet() {
        return headers.entrySet();
    }

    /**
     * @param resource Die Ressource.
     * @return Der evaluierte Content-Type und das Charset.
     * Falls kein accept-Header existiert wird resource.getMimeType() einfach zurückgegeben
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getEvaluatedContentTypeAndCharset(MHttpResource resource) {
        mout.println("Checking request compatiblity possiblities (acceptable response parameters): Content-Type");
        //String contentType = chooseBestField(get("Accept"), new String[]{resource.getMimeType()}, "text/plain");
        //String fallback=resource.getAcceptableFallBackContentTypes()[0];
        if (get("Accept") == null) return resource.getRealContentType();
        String contentType = chooseBestField(get("Accept"), resource.getCompatibleContentTypes(), resource.getRealContentType());
        contentType = contentType != null ? contentType + "; charset=" + resource.getCharset() : null;
        mout.println("chosen accept-compatible contentEncoding=" + contentType);
        return contentType;
    }

    /**
     * @return Das evaluierte Content-Encoding.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getEvaluatedContentEncoding(MHttpResource resource) {
        mout.println("Checking request compatiblity possiblities (acceptable response parameters): Content-Encoding");
        String contentEncoding = chooseBestField(get("Accept-Encoding"), resource.getCompatibleCompressionTypes(), IDENTITY.toString());//festes Fallback
        mout.println("chosen accept-compatible contentEncoding=" + contentEncoding);
        return contentEncoding;
    }

    /**
     * @param resource Die Ressource.
     * @return Die evaluierte Content-Language.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getEvaluatedContentLanguage(MHttpResource resource) {
        mout.println("Checking request compatiblity possiblities (acceptable response parameters): Content-Language");
        String contentLanguage = chooseBestField(get("Accept-Language"), resource.getCompatibleLanguages(), Locale.ENGLISH.toString());//festes Fallback
        mout.println("chosen accept-compatible contentLanguage=" + contentLanguage);
        return contentLanguage;
    }
}

