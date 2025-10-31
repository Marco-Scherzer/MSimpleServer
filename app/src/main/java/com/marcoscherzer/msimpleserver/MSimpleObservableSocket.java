package com.marcoscherzer.msimpleserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MSimpleObservableSocket {

    private final Socket socket;

    // Listener-Interfaces
    // Listener-Listen
    private List<CloseListener> closeListeners;

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleObservableSocket(Socket socket) {
        this.socket = socket;
    }

    // Konstruktor, der ein Socket-Objekt erhält

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void addCloseListener(CloseListener listener) {
        if (closeListeners == null) {
            closeListeners = new ArrayList<>();
        }
        closeListeners.add(listener);
    }

    // Methoden zum Hinzufügen von Listenern

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void close() throws IOException {
        socket.close();
        if (closeListeners != null) {
            for (CloseListener listener : closeListeners) {
                listener.onClose();
            }
        }
    }

    // Delegierte close-Methode

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public Socket getSocket() {
        return socket;
    }

    // Getter-Methode für den internen Socket

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public abstract class CloseListener {
        protected Socket socket_ = MSimpleObservableSocket.this.socket;

        public abstract void onClose();
    }
}



