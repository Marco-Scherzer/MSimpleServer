package com.marcoscherzer.msimpleserver;

import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public abstract class MRequestValidator<OutDataObjectT, ProtocolT extends MProtocolVersion> {

    private final ArrayList<ProtocolT> supportedProtocols = new ArrayList(5);
    private Pattern PROTOCOL_PATTERN = Pattern.compile("^(HTTP/1.0|HTTP/1.1|HTTP/2.0|HTTP/2.1|HTTP/3.0|HTTP/3.1)$");

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected MRequestValidator(ProtocolT... supportedProtocols) {
        this.setSupportedProtocols(supportedProtocols);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public abstract OutDataObjectT isValidRequest(Socket socket);

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected ArrayList<ProtocolT> getSupportedProtocols() {
        return supportedProtocols;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected final void setSupportedProtocols(ProtocolT... supportedProtocols) {
        StringBuffer protocolString = new StringBuffer();
        for (ProtocolT protocol : supportedProtocols) {
            protocolString.append(protocol).append("|");
            this.supportedProtocols.add(protocol);
        }
        PROTOCOL_PATTERN = Pattern.compile("^(" + protocolString + ")$");
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    protected Pattern getSupportedProtocolsPattern() {
        return PROTOCOL_PATTERN;
    }


}
