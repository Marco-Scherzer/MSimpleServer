package com.marcoscherzer.msimpleserver.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MPrintStringWriter extends PrintWriter {

    public MPrintStringWriter() {
        super(new StringWriter());
    }

    public final StringWriter getStringWriter() {
        return (StringWriter) this.out;
    }


}
