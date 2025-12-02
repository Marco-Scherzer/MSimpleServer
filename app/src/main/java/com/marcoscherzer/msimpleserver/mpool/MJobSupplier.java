package com.marcoscherzer.msimpleserver.mpool;

import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;

import java.util.function.Supplier;


/**
 * Author: Marco Scherzer
 *
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures, Architectures & Copyright Marco Scherzer 2017, All rights reserved, base-principle reimplementation 2025. All Rights Reserved.
 */
public abstract class MJobSupplier<T extends MJob<?>> implements Supplier<T> {

    protected Object[] parameters;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MJobSupplier(Object... parameters) {
        this.parameters = parameters;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public abstract T get();
}