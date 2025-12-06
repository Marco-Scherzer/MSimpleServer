package com.marcoscherzer.msimpleserver.http.request;

import java.util.HashMap;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
    public final class MParameterMap extends HashMap<String,Object> {

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         * Typisierter Zugriff auf Werte.
         * @param key Schlüssel in der Map
         * @param <T> generischer Typ
         * @return Wert als T oder null
         * @throws ClassCastException wenn der Wert nicht vom erwarteten Typ ist
         */
        @SuppressWarnings("unchecked")
        public final <T> T get(String key) throws ClassCastException{
            Object value = super.get(key);
            if (value == null) return null;
            return (T) value;
        }
}
