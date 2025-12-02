package com.marcoscherzer.msimpleserver.util.compression;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MCompression {

    /**
     * @param data Die komprimierten Daten.
     * @return Die dekomprimierten Daten.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static byte[] decompress(byte[] data) throws IOException {
        if (isGzip(data)) {
            return gzipDecompress(data);
        } else if (isIdentity(data)) {
            return data; // Unkomprimierte Daten direkt zurückgeben
        } else if (isDeflate(data)) {
            return deflateDecompress(data);
        } else if (isBrotli(data)) {
            throw new UnsupportedOperationException("Brotli compression not supported yet");
        } else {
            throw new IllegalArgumentException("Unrecognized compression format");
        }
    }

    /**
     * @param compressionTyp Der Komprimierungstyp.
     * @param data           Die zu komprimierenden Daten.
     * @return Die komprimierten Daten.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static byte[] compress(String compressionTyp, byte[] data) throws IOException {
        return compress(MSupportedCompressionType.valueOf(compressionTyp), data);
    }

    /**
     * @param compressionTyp Der Komprimierungstyp.
     * @param data           Die zu komprimierenden Daten.
     * @return Die komprimierten Daten.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static byte[] compress(MSupportedCompressionType compressionTyp, byte[] data) throws IOException {
        byte[] out = data;
        switch (compressionTyp) {
            case IDENTITY:
                out = data;
                break;
            case GZIP:
                out = gzip(data);
                break;
            case DEFLATE:
                out = deflate(data);
                break;
            // case BROTL I: //not supported yet
            default:
                mout.println("unsupported compression type. Falling back to IDENTITY");
        }
        return out;
    }

    /**
     * @param data Die zu komprimierenden Daten.
     * @return Die komprimierten Daten im GZIP-Format.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static byte[] gzip(byte[] data) throws IOException {
        mout.print("\ngzipping byte[] with length " + data.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        try {
            gzipOutputStream.write(data);
        } finally {
            gzipOutputStream.close();
        }
        byte[] out = byteArrayOutputStream.toByteArray();
        mout.println(", gzipped length " + out.length);
        return out;
    }

    /**
     * @param data Die zu komprimierenden Daten.
     * @return Die komprimierten Daten im Deflate-Format.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static byte[] deflate(byte[] data) throws IOException {
        mout.print("\ndeflating byte[] with length " + data.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream);
        try {
            deflaterOutputStream.write(data);
        } finally {
            deflaterOutputStream.close();
        }
        byte[] out = byteArrayOutputStream.toByteArray();
        mout.println(", deflated length " + out.length);
        return out;
    }

    /**
     * @param data Die zu dekomprimierenden Daten im GZIP-Format.
     * @return Die dekomprimierten Daten.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static byte[] gzipDecompress(byte[] data) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        GZIPInputStream gzipStream = new GZIPInputStream(byteStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = gzipStream.read(buffer)) != -1) {
            try {
                outStream.write(buffer, 0, len);
            } finally {
                outStream.close();
            }
        }
        return outStream.toByteArray();
    }

    /**
     * @param data Die zu dekomprimierenden Daten im Deflate-Format.
     * @return Die dekomprimierten Daten.
     * @throws IOException Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static byte[] deflateDecompress(byte[] data) throws IOException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        InflaterInputStream inflaterStream = new InflaterInputStream(byteStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inflaterStream.read(buffer)) != -1) {
            try {
                outStream.write(buffer, 0, len);
            } finally {
                outStream.close();
            }
        }
        return outStream.toByteArray();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isGzip(byte[] data) {
        return (data[0] == (byte) 0x1F) && (data[1] == (byte) 0x8B);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isIdentity(byte[] data) {
        //evtl noch ändern
        return true;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isDeflate(byte[] data) {
        try {
            return (data[0] & 0x0F) == 0x08 && (data[1] & 0x80) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean isBrotli(byte[] data) {
        return (data.length > 2 && data[0] == (byte) 0xCE && data[1] == (byte) 0xB2);
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public enum MSupportedCompressionType {
        UNDEFINED("*/*"),
        GZIP("GZIP"), //häufigstes
        DEFLATE("DEFLATE"),
        IDENTITY("IDENTITY"),
        BROTLI("BR");
        private final String value;

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MSupportedCompressionType(String value) {
            this.value = value;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        public final String getValue() {
            return value;
        }
    }
}
