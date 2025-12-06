package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * @version 0.0.1 preAlpha unready intermediate state,
 * RESTRICTIVE NON STANDARD REST API, URL COMPATIBLE
 * @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
final class MUrlParser {

    private static final int MAX_PARAM_LENGTH = 128; // Maximale erlaubte Länge für Parameterwerte
    private static final Pattern PATH_PATTERN = Pattern.compile("^/[^=&\\s]*$"); // Kein '=', '&' oder Leerraum erlaubt
    private static final Pattern METHOD_NAME_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    private static final Pattern QUERY_PATTERN = Pattern.compile("^([a-zA-Z0-9_]+=[^&]*)(&[a-zA-Z0-9_]+=[^&]*)*$");

    private int maxUriLength = 1024;

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public void setMaxUrlLength(int maxUrlLength) {
        this.maxUriLength = maxUrlLength;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public int getMaxUrlLength() {
        return maxUriLength;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestData parseUrl(String url, MHttpRequestData data) {
        String path = "";

        // Überprüfe URL-Länge
        if (url.length() > maxUriLength) {
            mout.println("Fehler: URI Länge zu lang: " + url.length() + ", Maximal erlaubte Länge: " + maxUriLength);
            data.responseCode = MHttpResponseStatusCodes._414_URI_TOO_LONG;
            return data;
        }

        path = URLDecoder.decode(url, StandardCharsets.UTF_8);

        // Überprüfe doppelte Fragezeichen
        int firstQuestionMark = path.indexOf("?");
        int lastQuestionMark = path.lastIndexOf("?");

        if (firstQuestionMark != -1 && firstQuestionMark != lastQuestionMark) {
            mout.println("Fehler: Mehrere '?' in der URL.");
            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
            return data;
        }

        // **Überprüfung: Falls `=` oder `&` vor `?` auftaucht, ist die URL ungültig**
        if (firstQuestionMark != -1 && path.substring(0, firstQuestionMark).contains("=")) {
            mout.println("Fehler: '=' darf nur als Zuweisungsoperator nach '?' verwendet werden.");
            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
            return data;
        }
        if (firstQuestionMark != -1 && path.substring(0, firstQuestionMark).contains("&")) {
            mout.println("Fehler: '&' darf nur als Parametertrenner nach einem Methodenaufruf verwendet werden.");
            data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
            return data;
        }

        String resourcePath = "";
        String methodName = "";
        String parameterPart = "";

        // Aufteilung in Resource und Methode
        if (firstQuestionMark != -1) {
            String[] splitParts = path.split("\\?");
            methodName = splitParts[0].substring(splitParts[0].lastIndexOf("/") + 1);
            resourcePath = splitParts[0].substring(0, splitParts[0].lastIndexOf("/"));
            parameterPart = splitParts.length > 1 ? splitParts[1] : "";

            // **Methoden ohne Ressourcen verhindern**
            if (resourcePath.isEmpty()) {
                mout.println("Fehler: Methode kann nicht ohne eine Ressource existieren.");
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }

            // **Überprüfung des Methoden-Namens gemäß Java-Konventionen**
            if (!METHOD_NAME_PATTERN.matcher(methodName).matches() || methodName.length() > 64) {
                mout.println("Fehler: Ungültiger Methodenname nach Java-Konventionen: " + methodName);
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }

            // **Methoden ohne Parameter sind erlaubt (`testresource/method?`)**
            if (!parameterPart.isEmpty()) {
                // **Überprüfung der Query-Syntax**
                if (!QUERY_PATTERN.matcher(parameterPart).matches()) {
                    mout.println("Fehler: Ungültige Query-Syntax oder nicht alle Parameter vorhanden: " + parameterPart);
                    data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                    return data;
                }

                // Query-Parameter speichern und doppelte Parameter verhindern
                String[] params = parameterPart.split("&");
                for (String param : params) {
                    String[] keyValue = param.split("=", 2);

                    if (keyValue.length < 2 || keyValue[1].isEmpty()) {
                        mout.println("Fehler: Alle Parameter müssen einen Wert haben: " + param);
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    // **Prüfung: Nur genau ein `=` in jedem Parameter erlaubt**
                    if (param.chars().filter(ch -> ch == '=').count() > 1) {
                        mout.println("Fehler: Mehrfache '=' in einem Parameter sind nicht erlaubt: " + param);
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    // **Prüfung der Parameterlänge**
                    if (keyValue[1].length() > MAX_PARAM_LENGTH) {
                        mout.println("Fehler: Parameterwert überschreitet die maximale Länge von " + MAX_PARAM_LENGTH + " Zeichen: " + keyValue[1]);
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    // **Prüfung des Parameternamens gemäß Java-Konventionen**
                    if (!METHOD_NAME_PATTERN.matcher(keyValue[0]).matches()) {
                        mout.println("Fehler: Ungültiger Parametername nach Java-Konventionen: " + keyValue[0]);
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    // **Einheitliche Fehlermeldung bei doppelten Parametern**
                    if (data.resourceMethodParameters.containsKey(keyValue[0])) {
                        mout.println("Fehler: Doppelter Parameter '" + keyValue[0] + "' erkannt. Bitte korrigieren.");
                        data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                        return data;
                    }

                    data.resourceMethodParameters.put(keyValue[0], keyValue[1]);
                }
            }
        } else {
            // Wenn kein `?` vorhanden ist, dann ist es nur eine Ressource
            resourcePath = path;

            if (!PATH_PATTERN.matcher(resourcePath).matches()) {
                mout.println("Fehler: Ungültiger Ressourcenpfad: '=' und '&' sind nur in Query-Parametern erlaubt.");
                data.responseCode = MHttpResponseStatusCodes._400_BAD_REQUEST;
                return data;
            }
        }

        // Setze die extrahierten Werte ins `data`-Objekt
        data.resourcePath = resourcePath;
        data.resourceMethod = methodName;

        mout.println("Url-Syntax ist korrekt!");
        mout.println("Ressource: " + resourcePath);
        mout.println("resourceMethod: " + methodName);
        mout.println("Query-Parameter: " + parameterPart);
        return data;
    }
}
