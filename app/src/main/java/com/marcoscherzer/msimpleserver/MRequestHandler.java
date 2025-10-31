package com.marcoscherzer.msimpleserver;

import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MRequestHandler {

    protected abstract MJob<?> createNewResponseJob(MSimpleObservableSocket socket, MInternalStatusCodes internalErrorCode);

}
