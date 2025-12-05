package com.marcoscherzer.msimpleserver.http.request;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.util.MValue2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MChoosableFieldsEvaluator {
    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    //get("Accept"), resource.getAcceptableReadContentTypes()
    public static final <T> T chooseBestField(String acceptables, T[] availables, T defaultValue) {
        T choice = defaultValue;
        if (acceptables != null) {
            mout.println("acceptables=\"" + acceptables + "\"" + ", availables=\"" + Arrays.toString(availables) + "\"");
            choice = MChoosableFieldsEvaluator.chooseBestField(acceptables, availables);
            if (choice == null || choice == "*/*") {
                choice = defaultValue;
                mout.println("Error: The clients accept-possibilies are either not compatible or */* (=everything) is accepted.");

                mout.println("Falling back to \"" + choice + "\".");
            }
        }
        mout.println("chosen accept-compatible value=" + choice);
        return choice;
    }


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static final <T> T chooseBestField(String acceptFields, T... supportedFields) {
        mout.println("supportedFields=" + Arrays.toString(supportedFields));
        ArrayList<MValue2D<Float, String>> l = new ArrayList();
        String[] tokens = acceptFields.replaceAll(" ", "").split(",");
        //mout.println("tokens="+Arrays.toString(tokens));
        for (String token : tokens) {
            String[] parts = token.split(";q=");
            if (parts.length == 1) {
                mout.println("1.0f,\"" + parts[0] + "\"");
                l.add(new MValue2D(1.0f, parts[0]));
            } else {

                mout.println(parts[1] + ",\"" + parts[0] + "\"");
                l.add(new MValue2D(Float.parseFloat(parts[1]), parts[0]));
            }
        }

        l.sort(new Comparator<MValue2D<Float, String>>() {
            @Override
            public int compare(MValue2D<Float, String> o1, MValue2D<Float, String> o2) {
                return -1 * o1.get1().compareTo(o2.get1());
            }
        });
        mout.println(Arrays.toString(l.toArray()));
        for (MValue2D<Float, String> e : l) {//1.0,....
            for (T s : supportedFields) {
                boolean found = e.get2().equalsIgnoreCase(s.toString());
                mout.println(e.get2() + " equals " + s + "= " + found);
                if (found) return s;
            }
        }

        return null;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static void main_(String[] args) {
        // Datensätze mit Expected-Werten
        String[][] testValues = {
                {"gzip, deflate, br;q=0.8", "gzip"},
                {"br;q=1.0, gzip;q=0.5, deflate;q=0.3", "br"},
                {"deflate;q=0.7, gzip;q=0.9", "gzip"},
                {"gzip;q=0.6, br;q=0.4, deflate;q=0.8", "deflate"},
                {"br;q=0.8", "br"},
                {"gzip, deflate", "gzip"},
                {"deflate", "deflate"},
                {"br, gzip;q=0.5", "br"},
                {"*/*;q=0.7, gzip;q=0.8", "gzip"},
                {"gzip;q=0.5, */*;q=0.9", "*/*"},
                {"de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7", "de-DE"},
                {"en-US,en;q=0.9,de;q=0.8", "en-US"},
                {"de-DE,de;q=0.8,en-US;q=0.5,*/*;q=0.9", "de-DE"},
                {"gzip;q=0.9,deflate;q=0.8,identity;q=0.7,br;q=0.6", "gzip"},
                {"deflate;q=0.6,gzip;q=0.5,br;q=0.7,identity;q=0.8", "identity"},
                {"*/*,gzip;q=0.8", "*/*"},
                {"*/*", "*/*"},
                {"gzip,deflate,*/*", "gzip"},
                {"identity;q=1.0,br;q=0.9", "identity"},
                {"de-DE,de;q=0.9,*/*;q=0.8,en-US;q=0.7", "de-DE"},
                {"br;q=0.6,gzip;q=0.5,deflate;q=0.4", "br"},
                {"gzip;q=0.9,deflate;q=0.1", "gzip"},
                {"gzip,deflate;q=0.9,br", "gzip"},
                {"gzip;q=1.0,deflate;q=0.5,br;q=0.9", "gzip"},
                {"gzip;q=0.5,br;q=0.5,deflate;q=0.5", "gzip"},
                {"identity", "identity"},
                {"br;q=1.0", "br"},
                {"deflate;q=0.3,gzip;q=0.7", "gzip"},
                {"*/*;q=0.5,identity;q=0.9,gzip;q=0.1", "identity"},
                {"gzip;q=0.1,*/*;q=0.5,br;q=0.9", "br"}
        };

        mout.println("MiniTests:");
        for (String[] testValue : testValues) {
            mout.println("\n\n");
            String acceptEncoding = testValue[0];
            String expected = testValue[1];
            String[] tokens = {"gzip", "deflate", "identity", "br", "*/*", "de-DE", "de", "en-US", "en"};
            String bestEncoding = MChoosableFieldsEvaluator.chooseBestField(acceptEncoding, tokens);
            String result = (bestEncoding != null ? bestEncoding : "none");

            if (result.equals(expected)) {
                mout.println("Accept: " + acceptEncoding + " -> Best: " + result + " (Expected: " + expected + ") - PASSED");
            } else {
                mout.println("Accept: " + acceptEncoding + " -> Best: " + result + " (Expected: " + expected + ") - FAILED");
                System.err.println("Test failed for Accept: " + acceptEncoding + ". Expected: " + expected + ", but got: " + result);
            }
        }
    }
}








