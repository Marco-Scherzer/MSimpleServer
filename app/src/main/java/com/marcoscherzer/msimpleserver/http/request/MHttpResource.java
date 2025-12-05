package com.marcoscherzer.msimpleserver.http.request;

import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.DEFLATE;
import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.GZIP;
import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.IDENTITY;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.http.constants.MHttpContentType;
import com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType;
import com.marcoscherzer.msimpleserver.util.fileloader.MMultiPlatformFileLoader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MHttpResource {

    private static final String sep = System.getProperty("file.separator");
    private static MMultiPlatformFileLoader httpFileLoader;
    //private final String persistenceBasePath;
    private final ArrayList<Locale> compatibleLanguages = new ArrayList<>();
    private final String realMimeType;
    private final Charset charset;
    private final String fileName;
    private final ArrayList<String> compatibleContentTypes = new ArrayList();
    private final ArrayList<MSupportedCompressionType> compatibleCompressionTypes = new ArrayList();
    private final HashMap<String, MResourceMethod> resourceMethods = new HashMap(0);
    private byte[] resource;
    private boolean keepInMemory;
    private boolean cacheAtClient;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResource(Locale language, String fileName) throws Exception {
        //this.persistenceBasePath = persistenceBasePath;
        this.fileName = fileName;
        String path = sep + language.toString() + sep + fileName;
        Path p = Paths.get(path);

        realMimeType = Files.probeContentType(p); // für alle Sprachen
        mout.println("creating resource " + p + ", mimeType=" + realMimeType);
        charset = detectCharset(realMimeType); // für alle Sprachen
        compatibleContentTypes.add(this.realMimeType);
        compatibleLanguages.add(language);
        compatibleCompressionTypes.add(GZIP);
        compatibleCompressionTypes.add(IDENTITY);
        compatibleCompressionTypes.add(DEFLATE);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void setHttpResourceFileLoader(MMultiPlatformFileLoader configuredFileLoader) {
        httpFileLoader = configuredFileLoader;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static Charset detectCharset(String mimeType) throws IOException {
        if (mimeType != null && mimeType.contains("charset=")) {
            String charset = mimeType.substring(mimeType.indexOf("charset=") + 8);
            try {
                return Charset.forName(charset);
            } catch (UnsupportedCharsetException e) {
                System.err.println("Unsupported charset: " + charset);
            }
        }
        return StandardCharsets.UTF_8;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResource addResourceMethod(String resourceMethodName, MResourceMethod resourceMethod) {
        resourceMethods.put(resourceMethodName, resourceMethod);
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResource addCompatibleTypes(MHttpContentType... fallbackTypes) {
        for (MHttpContentType c : fallbackTypes) this.compatibleContentTypes.add(c.toString());
        return this;
    }

    /**
     * @param languages Die hinzuzufügenden Sprachen.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void addCompatibleLanguages(Locale... languages) {
        Collections.addAll(this.compatibleLanguages, languages);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void addCompatibleCompressionTypes(MSupportedCompressionType... compatibleCompressionTypes) {
        Collections.addAll(this.compatibleCompressionTypes, compatibleCompressionTypes);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResource setCacheAtClient(boolean cacheAtClient) {
        this.cacheAtClient = cacheAtClient;
        return this;
    }

    /**
     * @param language Die Sprache.
     * @return Die geladenen Ressourcendaten.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public byte[] loadResource(String language) {
        byte[] b = this.resource;
        Path p = null;
        mout.print("Trying to load resource from persistence");
        try {
            if (b == null) {
                p = Paths.get(language + sep + fileName);
                mout.print("\nloadResource " + p);
                b = httpFileLoader.loadByteArray(p);
                mout.println(", length " + b.length);
            }
        } catch (IOException exc) {
            mout.println("Error while loading resource \"" + p + "\" from persistence.");
            exc.printStackTrace(mout);
        }
        if (keepInMemory) this.resource = b;
        return b;
    }

    /**
     * @return Der MIME-Typ.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getRealContentType() {
        return realMimeType;
    }

    /**
     * @return Das Charset.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @return Ob die Ressource im Speicher gehalten wird.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public boolean getKeepInMemory() {
        return this.keepInMemory;
    }

    /**
     * @param keepInMemory Ob die Ressource im Speicher gehalten werden soll.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpResource setKeepInMemory(boolean keepInMemory) {
        this.keepInMemory = keepInMemory;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public boolean getCacheAtClient(boolean cacheAtClient) {
        return this.cacheAtClient;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MResourceMethod getResourceMethod(String resourceMethodName) {
        return resourceMethods.get(resourceMethodName);
    }

    /**
     * @return Die Sprachen der Ressource.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String[] getCompatibleLanguages() {
        String[] out = new String[compatibleLanguages.size() + 1];
        int i = 0;
        for (Locale language : compatibleLanguages) out[i++] = language.toString();
        out[out.length - 1] = "*/*";
        return out;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String[] getCompatibleContentTypes() {
        String[] out = new String[compatibleContentTypes.size() + 1];
        int i = 0;
        for (String mimetype : compatibleContentTypes) out[i++] = mimetype;
        out[out.length - 1] = "*/*";
        return out;
    }

    public String[] getCompatibleCompressionTypes() {
        String[] out = new String[compatibleCompressionTypes.size() + 1];
        int i = 0;
        for (MSupportedCompressionType language : compatibleCompressionTypes)
            out[i++] = language.toString();
        out[out.length - 1] = "*/*";
        return out;
    }


    /**
     @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
      * @return Der Basis-Pfad für die Persistenz.
     */
   /* public final String getPersistenceBasePath() {
        return this.persistenceBasePath;
    }*/

    /**
     * @return Der Dateiname.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public String getName() {
        return this.fileName;
    }


}
