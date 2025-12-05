package com.marcoscherzer.minigui;


import com.marcoscherzer.msimpleserver.util.fileloader.MMultiPlatformFileLoader;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSSLConfig1 {

    public static SSLContext create(MMultiPlatformFileLoader configuredFileLoader) throws Exception {

        char[] password = "mypassword".toCharArray();

        KeyStore serverKS = KeyStore.getInstance("BKS");
        serverKS.load(configuredFileLoader.loadByteInputStream("server-keystore.bks"), password);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
        kmf.init(serverKS, password);

        KeyStore trustKS = KeyStore.getInstance("BKS");
        trustKS.load(configuredFileLoader.loadByteInputStream("truststore.bks"), password);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(trustKS);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }
}


