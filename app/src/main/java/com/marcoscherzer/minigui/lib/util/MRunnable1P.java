package com.marcoscherzer.minigui.lib.util;

/**
 * @param <T>
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
@FunctionalInterface
public interface MRunnable1P<T> {
    void run(T p);
}
