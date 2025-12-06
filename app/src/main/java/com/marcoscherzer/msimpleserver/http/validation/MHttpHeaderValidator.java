package com.marcoscherzer.msimpleserver.http.validation;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._400_BAD_REQUEST;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._413_PAYLOAD_TOO_LARGE;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

final class MHttpHeaderValidator {
//private static final Pattern HEADER_PATTERN = Pattern.compile("^[a-zA-Z0-9-]+:\\s.*$");

    private int MAX_HEADER_SIZE = 8192; // Maximale Größe der Header private int MAX_HEADER_SIZE = 8192; // Maximale Größe der Header

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    final void setMaxHeaderSize(int maxHeaderSize) { MAX_HEADER_SIZE = maxHeaderSize; }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
     final void validateHeaders(MHttpVersion httpVersion, String[] lines, MHttpRequestData outData) {
        int headerSize = 0;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            mout.println("Checking header line: " + line);
            if (line.isEmpty()) {
                break; // Ende der Header-Sektion
            }

            headerSize += line.length();
            if (!checkHeaderSize(httpVersion, headerSize)) {
                outData.setResponseCode(_413_PAYLOAD_TOO_LARGE);
                return;
            }

            // Mehrzeilige Header berücksichtigen
            while (i < lines.length - 1 && (lines[i + 1].startsWith(" ") || lines[i + 1].startsWith("\t"))) {
                line += " " + lines[++i].trim();
                headerSize += lines[i].length();
                if (!checkHeaderSize(httpVersion, headerSize)) {
                    outData.setResponseCode(_413_PAYLOAD_TOO_LARGE);
                    return;
                }
            }

            // Validierung spezifischer Header
            mout.println("Checking specific header: " + line);
            for (MHttpVersion.MValidationPattern p : httpVersion.getValidationEntrySet()) {  //httpVersion.
                if (validateHeader(line, p)) {
                    String[] headerParts = line.split(":", 2);
                    if (headerParts.length == 2) {
                        outData.getHeaders().put(headerParts[0].trim(), headerParts[1].trim());
                        break;
                    }
                } else outData.setResponseCode(_400_BAD_REQUEST);
            }
        }
        outData.setResponseCode(VALID_AND_COMPLETE);
        return;
    }


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static boolean validateHeader(String line, MHttpVersion.MValidationPattern p) {
        mout.println("Checking header: " + line);
        if (line.startsWith(p.getHeaderName())) {
            if (!line.matches(p.getPattern())) {
                mout.println("Error: Invalid header. Checked header: " + line);
                return false;
            }
        }
        return true;
    }


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private  boolean checkHeaderSize(MHttpVersion httpVersion, int headerSize) {
        mout.println("Checking header size: " + headerSize);
        if (headerSize > MAX_HEADER_SIZE) {
            mout.println("Error: Header size exceeds the allowed limit. Checked header size: " + headerSize);
            return false;
        }
        return true;
    }

}
