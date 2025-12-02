package com.marcoscherzer.msimpleserver.util;

import com.marcoscherzer.msimpleserver.util.logging.MPrintStringWriter;

import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSocketInfo {
    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static String toString(Socket socket) {
        StringBuilder sb = new StringBuilder();
        MPrintStringWriter errorStream = new MPrintStringWriter();
        sb.append("Socket Information:\n");
        sb.append("SupportedOptions: ").append(Arrays.toString(socket.supportedOptions().toArray())).append("\n");
        sb.append("InetAddress: ").append(socket.getInetAddress()).append("\n");
        sb.append("LocalAddress: ").append(socket.getLocalAddress()).append("\n");
        sb.append("Port: ").append(socket.getPort()).append("\n");
        sb.append("LocalPort: ").append(socket.getLocalPort()).append("\n");
        sb.append("RemoteSocketAddress: ").append(socket.getRemoteSocketAddress()).append("\n");
        sb.append("LocalSocketAddress: ").append(socket.getLocalSocketAddress()).append("\n");
        sb.append("Closed: ").append(socket.isClosed()).append("\n");
        sb.append("Connected: ").append(socket.isConnected()).append("\n");

        try {
            sb.append("KeepAlive: ").append(socket.getKeepAlive()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting KeepAlive:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("ReceiveBufferSize: ").append(socket.getReceiveBufferSize()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting ReceiveBufferSize:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("SendBufferSize: ").append(socket.getSendBufferSize()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting SendBufferSize:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("SoLinger: ").append(socket.getSoLinger()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting SoLinger:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("SoTimeout: ").append(socket.getSoTimeout()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting SoTimeout:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("TcpNoDelay: ").append(socket.getTcpNoDelay()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting TcpNoDelay:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("TrafficClass: ").append(socket.getTrafficClass()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting TrafficClass:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }

        try {
            sb.append("ReuseAddress: ").append(socket.getReuseAddress()).append("\n");
        } catch (SocketException exc) {
            errorStream.println("Error while getting ReuseAddress:");
            exc.printStackTrace(errorStream);
            sb.append("\n");
        }
        sb.append("\n");
        if (socket instanceof SSLSocket) {
            SSLSocket sslSocket = (SSLSocket) socket;
            sb.append("SSL Enabled Protocols: ").append(Arrays.toString(sslSocket.getEnabledProtocols())).append("\n");
            sb.append("SSL Enabled Cipher Suites: ").append(Arrays.toString(sslSocket.getEnabledCipherSuites())).append("\n");
            sb.append("SSL Supported Protocols: ").append(Arrays.toString(sslSocket.getSupportedProtocols())).append("\n");
            sb.append("SSL Supported Cipher Suites: ").append(Arrays.toString(sslSocket.getSupportedCipherSuites())).append("\n");
            sb.append("SSL Need Client Auth: ").append(sslSocket.getNeedClientAuth()).append("\n");
            sb.append("SSL Want Client Auth: ").append(sslSocket.getWantClientAuth()).append("\n");
            sb.append("SSL Use Client Mode: ").append(sslSocket.getUseClientMode()).append("\n");
            sb.append("SSL Handshake Application Protocol: ").append(sslSocket.getHandshakeApplicationProtocol()).append("\n");
            sb.append("SSL Enable Session Creation: ").append(sslSocket.getEnableSessionCreation()).append("\n");
            sb.append("\n");
            sb.append("SSL Handshake Session Details:\n");
            logHandshakeSessionDetails(sb, sslSocket.getHandshakeSession(), "\t", errorStream);
            sb.append("\n");
            sb.append("SSL Session Details:\n");
            logSessionDetails(sb, sslSocket.getSession(), "\t", errorStream);
            sb.append("\n");
            sb.append("SSLParameters Details:\n");
            logSSLParameters(sb, sslSocket.getSSLParameters(), "\t");
            sb.append("\n");
            try {
                sb.append("SSL Session ID: ").append(Arrays.toString(sslSocket.getSession().getId())).append("\n");
            } catch (Exception exc) {
                errorStream.println("Error while getting SSL Session ID:");
                exc.printStackTrace(errorStream);
                sb.append("\n");
            }

            try {
                sb.append("SSL Protocol: ").append(sslSocket.getSession().getProtocol()).append("\n");
            } catch (Exception exc) {
                errorStream.println("Error while getting SSL Protocol:");
                exc.printStackTrace(errorStream);
                sb.append("\n");
            }

            try {
                sb.append("SSL Cipher Suite: ").append(sslSocket.getSession().getCipherSuite()).append("\n");
            } catch (Exception exc) {
                errorStream.println("Error while getting SSL Cipher Suite:");
                exc.printStackTrace(errorStream);
                sb.append("\n");
            }
        }

        return sb.append("\n").append(errorStream.getStringWriter().toString()).toString();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void logHandshakeSessionDetails(StringBuilder sb, SSLSession session, String indent, MPrintStringWriter errorStream) {
        if (session != null) {
            sb.append(indent).append("Handshake Session ID: ").append(Arrays.toString(session.getId())).append("\n");
            sb.append(indent).append("Handshake Cipher Suite: ").append(session.getCipherSuite()).append("\n");
            sb.append(indent).append("Handshake Protocol: ").append(session.getProtocol()).append("\n");
            sb.append(indent).append("Handshake Peer Host: ").append(session.getPeerHost()).append("\n");
            sb.append(indent).append("Handshake Peer Port: ").append(session.getPeerPort()).append("\n");
            sb.append(indent).append("Handshake Packet Buffer Size: ").append(session.getPacketBufferSize()).append("\n");
            sb.append(indent).append("Handshake Application Buffer Size: ").append(session.getApplicationBufferSize()).append("\n");

            try {
                sb.append(indent).append("Handshake Peer Principal: ").append(session.getPeerPrincipal()).append("\n");
            } catch (SSLPeerUnverifiedException e) {
                errorStream.println("Error while getting Handshake Peer Principal:");
                e.printStackTrace(errorStream);
                sb.append("\n");
            }

            try {
                sb.append(indent).append("Handshake Peer Certificates: ").append(Arrays.toString(session.getPeerCertificates())).append("\n");
            } catch (SSLPeerUnverifiedException e) {
                errorStream.println("Error while getting Handshake Peer Certificates:");
                e.printStackTrace(errorStream);
                sb.append("\n");
            }
        } else {
            sb.append(indent).append("Handshake Session: null").append("\n");
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void logSessionDetails(StringBuilder sb, SSLSession session, String indent, MPrintStringWriter errorStream) {
        if (session != null) {
            sb.append(indent).append("Session ID: ").append(Arrays.toString(session.getId())).append("\n");
            sb.append(indent).append("Cipher Suite: ").append(session.getCipherSuite()).append("\n");
            sb.append(indent).append("Protocol: ").append(session.getProtocol()).append("\n");
            sb.append(indent).append("Peer Host: ").append(session.getPeerHost()).append("\n");
            sb.append(indent).append("Peer Port: ").append(session.getPeerPort()).append("\n");
            sb.append(indent).append("Packet Buffer Size: ").append(session.getPacketBufferSize()).append("\n");
            sb.append(indent).append("Application Buffer Size: ").append(session.getApplicationBufferSize()).append("\n");

            try {
                sb.append(indent).append("Peer Principal: ").append(session.getPeerPrincipal()).append("\n");
            } catch (SSLPeerUnverifiedException e) {
                errorStream.println("Error while getting Peer Principal:");
                e.printStackTrace(errorStream);
                sb.append("\n");
            }

            try {
                sb.append(indent).append("Peer Certificates: ").append(Arrays.toString(session.getPeerCertificates())).append("\n");
            } catch (SSLPeerUnverifiedException e) {
                errorStream.println("Error while getting Peer Certificates:");
                e.printStackTrace(errorStream);
                sb.append("\n");
            }
        } else {
            sb.append(indent).append("Session= null").append("\n");
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static void logSSLParameters(StringBuilder sb, SSLParameters sslParameters, String indent) {
        sb.append(indent).append("Algorithm Constraints: ").append(sslParameters.getAlgorithmConstraints()).append("\n");
        sb.append(indent).append("Application Protocols: ").append(Arrays.toString(sslParameters.getApplicationProtocols())).append("\n");
        sb.append(indent).append("Cipher Suites: ").append(Arrays.toString(sslParameters.getCipherSuites())).append("\n");
        //sb.append(indent).append("Enable Retransmissions: ").append(sslParameters.getEnableRetransmissions()).append("\n");//JDK17, JDK > ?
        sb.append(indent).append("Endpoint Identification Algorithm: ").append(sslParameters.getEndpointIdentificationAlgorithm()).append("\n");
        //sb.append(indent).append("Maximum Packet Size: ").append(sslParameters.getMaximumPacketSize()).append("\n");//JDK17, JDK > ?
        sb.append(indent).append("Need Client Auth: ").append(sslParameters.getNeedClientAuth()).append("\n");
        sb.append(indent).append("Protocols: ").append(Arrays.toString(sslParameters.getProtocols())).append("\n");
        sb.append(indent).append("SNI Matchers: ").append(sslParameters.getSNIMatchers()).append("\n");
        sb.append(indent).append("Server Names: ").append(sslParameters.getServerNames()).append("\n");
        sb.append(indent).append("Use Cipher Suites Order: ").append(sslParameters.getUseCipherSuitesOrder()).append("\n");
        sb.append(indent).append("Want Client Auth: ").append(sslParameters.getWantClientAuth()).append("\n");
    }
}




