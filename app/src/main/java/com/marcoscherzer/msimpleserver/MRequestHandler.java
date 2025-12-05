package com.marcoscherzer.msimpleserver;

import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MRequestHandler {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected abstract MJob<?> createNewResponseJob(MSimpleObservableSocket socket, MInternalStatusCodes internalErrorCode);

}
