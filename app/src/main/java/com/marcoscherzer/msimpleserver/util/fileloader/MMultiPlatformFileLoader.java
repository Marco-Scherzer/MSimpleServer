package com.marcoscherzer.msimpleserver.util.fileloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/*
 @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public interface MMultiPlatformFileLoader {
    String getBaseBath();

    byte[] loadByteArray(Path relativePath) throws IOException;

    byte[] loadByteArray(String relativePath) throws IOException;

    InputStream loadByteInputStream(String relativePath) throws IOException;

}



