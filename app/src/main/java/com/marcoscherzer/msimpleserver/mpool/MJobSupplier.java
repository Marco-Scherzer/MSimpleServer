package com.marcoscherzer.msimpleserver.mpool;

import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;

import java.util.function.Supplier;


/**
 * Author: Marco Scherzer
 *
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
 */
public abstract class MJobSupplier<T extends MJob<?>> implements Supplier<T> {

    protected Object[] parameters;

    public MJobSupplier(Object... parameters) {
        this.parameters = parameters;
    }

    @Override
    public abstract T get();
}