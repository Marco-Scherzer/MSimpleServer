package com.marcoscherzer.msimpleserver;


import java.util.Collection;
import java.util.HashMap;

/*
 @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
History: httpvalidation.MHttpVersion (Abstraction of MHttpVersion),httpvalidation.ProtocolVersion(namefailure),httpvalidation.MProtocolVersion
*/
public abstract class MProtocolVersion<ValidationPatternT extends Enum> {
    protected final HashMap<String, ValidationPatternT> headerMap = new HashMap<>();
    private final String version;

    /*
 @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
*/
    protected MProtocolVersion(String version) {
        this.version = version;
    }

    /*
 @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
*/
    public final String getVersion() {
        return version;
    }

    /*
@version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
*/
    @Override
    public final String toString() {
        return version;
    }

    /*
@version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
*/
    public final ValidationPatternT getValidationPatternByName(String name) {
        return headerMap.get(name);
    }

    /*
@version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
*/
    public final Collection<ValidationPatternT> getValidationEntrySet() {
        return headerMap.values();
    }


}