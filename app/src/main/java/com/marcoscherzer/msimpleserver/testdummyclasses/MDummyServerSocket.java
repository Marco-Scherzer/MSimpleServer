package com.marcoscherzer.msimpleserver.testdummyclasses;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MDummyServerSocket extends ServerSocket {
    private final String inputData;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MDummyServerSocket(int port, String inputData) throws IOException {
        super(port);
        this.inputData = inputData;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main_(String[] args) throws IOException {
        String httpRequest = "GET /test2.html HTTP/1.1\r\nHost: example.com\r\nUser-Agent: Mozilla/5.0\r\n\r\n";
        MDummyServerSocket serverSocket = new MDummyServerSocket(80, httpRequest);

        // Akzeptiere einen DummySocket vom ServerSocket
        MDummySocket dummySocket = (MDummySocket) serverSocket.accept();

        // Lese Daten vom DummySocket
        byte[] buffer = new byte[1024];
        int bytesRead = dummySocket.getInputStream().read(buffer);
        String receivedData = new String(buffer, 0, bytesRead);
        mout.println("Empfangene Daten: " + receivedData);

        // Schreibe Daten zum DummySocket
        String sendData = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\n\r\nAntwortdaten";
        dummySocket.getOutputStream().write(sendData.getBytes());
        dummySocket.getOutputStream().flush();
        mout.println("Gesendete Daten: " + dummySocket.getOutputStream().toString());
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    public Socket accept() throws IOException {
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        return new MDummySocket(inputData);
    }

}