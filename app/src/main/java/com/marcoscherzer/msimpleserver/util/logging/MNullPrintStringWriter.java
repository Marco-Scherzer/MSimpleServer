package com.marcoscherzer.msimpleserver.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer
 */
public class MNullPrintStringWriter extends PrintWriter {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MNullPrintStringWriter() {
        super(new MNoStringWriter());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final StringWriter getStringWriter() {
        return (StringWriter) this.out;
    }
}
