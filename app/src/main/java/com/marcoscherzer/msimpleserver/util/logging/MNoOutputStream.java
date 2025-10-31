package com.marcoscherzer.msimpleserver.util.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * History: siehe MW.java
 */
public final class MNoOutputStream extends PrintStream {
    public MNoOutputStream() {
        super(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }
        });
    }
}
