package com.marcoscherzer.minigui.lib.util;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Nomenclatures(2P = 2 Parameters), Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
@FunctionalInterface
public interface MRunnable2P<T1, T2> {
    void run(T1 p, T2 p2);
}