package com.marcoscherzer.minigui.lib.util;

/**
 * @param <T>
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Nomenclatures(1A = 1 Attribute), Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MRunnable1A<T> {
    protected final T attribute1;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MRunnable1A(T attribute1) {
        this.attribute1 = attribute1;
    }

    public abstract void run();

}
