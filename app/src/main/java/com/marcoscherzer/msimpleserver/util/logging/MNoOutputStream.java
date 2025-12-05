package com.marcoscherzer.msimpleserver.util.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
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
