package com.marcoscherzer.msimpleserver.http.constants;


/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */

public enum MHttpContentType {
    // Text
    TEXT_PLAIN("text/plain"),
    TEXT_HTML("text/html"),
    TEXT_CSS("text/css"),
    TEXT_JAVASCRIPT("text/javascript"),
    TEXT_XML("text/xml"),

    // Application
    APPLICATION_JSON("application/json"),
    APPLICATION_XML("application/xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_PDF("application/pdf"),
    APPLICATION_MSWORD("application/msword"),
    APPLICATION_VND_MS_EXCEL("application/vnd.ms-excel"),
    APPLICATION_VND_MS_POWERPOINT("application/vnd.ms-powerpoint"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    APPLICATION_ZIP("application/zip"),
    APPLICATION_GZIP("application/gzip"),
    APPLICATION_RTF("application/rtf"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_WORDPROCESSINGML_DOCUMENT("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_SPREADSHEETML_SHEET("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    APPLICATION_VND_OPENXMLFORMATS_OFFICEDOCUMENT_PRESENTATIONML_PRESENTATION("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
    APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded"),
    APPLICATION_JAVASCRIPT("application/javascript"),
    APPLICATION_ECMASCRIPT("application/ecmascript"),
    APPLICATION_OGG("application/ogg"),
    APPLICATION_RSS_XML("application/rss+xml"),
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_EPUB_ZIP("application/epub+zip"),
    APPLICATION_PKCS10("application/pkcs10"),
    APPLICATION_PKCS7_MIME("application/pkcs7-mime"),
    APPLICATION_PKCS7_SIGNATURE("application/pkcs7-signature"),
    APPLICATION_PKCS12("application/pkcs12"),
    APPLICATION_PKCS8("application/pkcs8"),
    APPLICATION_SIGNED_EXCHANGE("application/signed-exchange"),

    // Image
    IMAGE_GIF("image/gif"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    IMAGE_TIFF("image/tiff"),
    IMAGE_VND_MICROSOFT_ICON("image/vnd.microsoft.icon"),
    IMAGE_SVG_XML("image/svg+xml"),
    IMAGE_WEBP("image/webp"),
    IMAGE_AVIF("image/avif"),
    IMAGE_APNG("image/apng"),

    // Audio
    AUDIO_MPEG("audio/mpeg"),
    AUDIO_OGG("audio/ogg"),
    AUDIO_WAV("audio/wav"),
    AUDIO_WEBM("audio/webm"),

    // Video
    VIDEO_MP4("video/mp4"),
    VIDEO_MPEG("video/mpeg"),
    VIDEO_OGG("video/ogg"),
    VIDEO_WEBM("video/webm"),

    // Any
    ANY("*/*");

    private final String val;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    MHttpContentType(String val) {
        this.val = val;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getValue() {
        return val;
    }
}
