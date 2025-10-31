package com.marcoscherzer.msimpleserver.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer
 */
public class MNullPrintStringWriter extends PrintWriter {

    public MNullPrintStringWriter() {
        super(new MNoStringWriter());
    }

    public final StringWriter getStringWriter() {
        return (StringWriter) this.out;
    }
}
