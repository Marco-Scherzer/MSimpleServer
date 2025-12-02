package com.marcoscherzer.msimpleserver.util.fileloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MDesktopFileLoader implements MMultiPlatformFileLoader {

    private String desktopPath;
    private String path;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MDesktopFileLoader initForDesktop(String basePath) {
        basePath = basePath.trim();
        if (!basePath.startsWith("/")) basePath = "/" + basePath;
        if (!basePath.endsWith("/")) basePath += "/";
        File f = new File(basePath);
        if (!f.exists() || !f.isDirectory()) {
            throw new IllegalArgumentException("Error in initForDesktop: base directory \"" + f + "\" does not exist or is not a directory.");
        }

        desktopPath = basePath;
        path = desktopPath;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public String getBaseBath() {
        return path;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public byte[] loadByteArray(Path relativePath) throws IOException {
        return loadByteArray(relativePath.toString());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public byte[] loadByteArray(String relativePath) throws IOException {
        return Files.readAllBytes(Paths.get(path, relativePath));
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public InputStream loadByteInputStream(String relativePath) throws IOException {
        return Files.newInputStream(Paths.get(path, relativePath));
    }

}
