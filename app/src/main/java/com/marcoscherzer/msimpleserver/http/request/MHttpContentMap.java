package com.marcoscherzer.msimpleserver.http.request;

import com.marcoscherzer.msimpleserver.http.constants.MHttpContentType;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * History: msimplehttpserver.MContentMap
 */
public final class MHttpContentMap {

    private final HashMap<String, MHttpResource> url2Resource = new HashMap<>();

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public HashMap<String, MHttpResource> getMap() {
        return url2Resource;
    }

    /**
     * @param url      Die URL.
     * @param fileName Der Dateiname.
     * @param language Die Sprachpfade.
     * @return Die aktuelle Instanz von MHttpContentMap.
     * @throws Exception Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpContentMap addContent(String url, Locale language, String fileName, boolean cacheAtClient) throws Exception {
        url2Resource.put(url, new MHttpResource(language, fileName));
        return this;
    }

    /**
     * @param url Die URL.
     * @return Die aktuelle Instanz von MHttpContentMap.
     * @throws Exception Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpContentMap addContent(String url, MHttpResource resource, boolean cacheAtClient) throws Exception {
        url2Resource.put(url, resource);
        return this;
    }

    /**
     * @param persitencePath        Der Pfad.
     * @param languageOfFilesInPath Die Sprache der Dateien im Pfad.
     * @return Die aktuelle Instanz von MHttpContentMap.
     * @throws Exception Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpContentMap addAllContent(String persitencePath, Locale languageOfFilesInPath, MHttpContentType... fileTypesToCacheAtClient) throws Exception {
        File f = Paths.get(persitencePath).toFile();
        if (!f.exists())
            throw new Exception("Error in addContent: path \"" + f + "\" does not exist.");
        throw new UnsupportedOperationException("not supported yet");
        // return this;
    }


}
 
