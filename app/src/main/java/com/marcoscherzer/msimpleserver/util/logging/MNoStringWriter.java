package com.marcoscherzer.msimpleserver.util.logging;

import java.io.StringWriter;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MNoStringWriter extends StringWriter {

    @Override
    public void write(int c) {
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
    }

    @Override
    public void write(String str, int off, int len) {
    }

    @Override
    public void write(String str) {
    }

    @Override
    public StringWriter append(CharSequence csq) {
        return this;
    }

    @Override
    public StringWriter append(CharSequence csq, int start, int end) {
        return this;
    }

    @Override
    public StringWriter append(char c) {
        return this;
    }

}
