package com.marcoscherzer.msimpleserver.testdummyclasses;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MDummySocket extends Socket {
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final boolean bound = true;
    private boolean closed = false;
    private boolean connected = true;
    private boolean inputShutdown = false;
    private boolean outputShutdown = false;

    public MDummySocket(String inputData) {
        this.inputStream = new ByteArrayInputStream(inputData.getBytes());
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        connected = false;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public boolean isBound() {
        return bound;
    }

    @Override
    public boolean isInputShutdown() {
        return inputShutdown;
    }

    @Override
    public boolean isOutputShutdown() {
        return outputShutdown;
    }

    @Override
    public void shutdownInput() throws IOException {
        inputShutdown = true;
    }

    @Override
    public void shutdownOutput() throws IOException {
        outputShutdown = true;
    }

}
