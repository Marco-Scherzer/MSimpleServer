package com.marcoscherzer.msimpleserver;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public final class MServerSocketConfig {

    private SSLContext ssl = null;
    private Boolean reuseAdress = false;
    private int listeningPerConnectionTimeoutMillis = -1;
    private int connectionTimeout = -1;
    private int latency = -1;
    private int bandwidth = -1;

    private int MAX_SIZE = -1;

    private String ipaddress;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    ServerSocket createSocket(int port, int backlog) throws IOException {
        SSLContext sslContext = ssl;
        ServerSocket serverSocket = null;
        InetAddress address = null;
        mout.println("ipaddress " + ipaddress);
        if (ipaddress == null) {
            address = InetAddress.getLoopbackAddress();
            mout.println("No address set. Setting \"" + address + " \"");
        } else address = InetAddress.getByName(ipaddress);

        if (sslContext == null) {
            serverSocket = new ServerSocket(port, backlog, address);
            //serverSocket=new ServerSocket(port,backlog);
            mout.println("UNSECURE ServerSocket started @ " + serverSocket.getInetAddress() + " @ port " + serverSocket.getLocalPort());
        } else {
            SSLServerSocket sslsocket = ((SSLServerSocket) sslContext.getServerSocketFactory().createServerSocket(port, backlog, address));
            //SSLServerSocket sslsocket=((SSLServerSocket)sslContext.getServerSocketFactory().createServerSocket(port,backlog));
            mout.println("SECURE ServerSocket started @ " + sslsocket.getInetAddress() + " @ port " + sslsocket.getLocalPort());
            //sslsocket.setUseClientMode(true);später evtl interessant
            serverSocket = sslsocket;
        }

        if (MAX_SIZE >= 0) serverSocket.setReceiveBufferSize(MAX_SIZE);
        if (reuseAdress != null)
            serverSocket.setReuseAddress(reuseAdress);//neubindning während timeoutState der letzten connection
        if (connectionTimeout > 0 && bandwidth > 0)
            serverSocket.setPerformancePreferences(connectionTimeout, latency, bandwidth);
        if (listeningPerConnectionTimeoutMillis > 0)
            serverSocket.setSoTimeout(listeningPerConnectionTimeoutMillis);
        return serverSocket;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setBiggestAllowedRequestSize(int maxSize) {
        this.MAX_SIZE = maxSize;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setAddress(String ipString) throws UnknownHostException {
        ipaddress = ipString;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setReuseAddressWithinConnectionClosedTimeWait(boolean reuseAdress) {
        this.reuseAdress = reuseAdress;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setSocketListeningTimeout(int listeningTimeoutMillis) {
        this.listeningPerConnectionTimeoutMillis = listeningTimeoutMillis;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setPerformancePreferences(int connectionTimeout, int latency, int bandwidth) {
        this.connectionTimeout = connectionTimeout;
        this.latency = latency;
        this.bandwidth = bandwidth;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    SSLContext getSSLContext() {
        return ssl;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MServerSocketConfig setSSLContext(SSLContext ssl) {
        this.ssl = ssl;
        return this;
    }


}