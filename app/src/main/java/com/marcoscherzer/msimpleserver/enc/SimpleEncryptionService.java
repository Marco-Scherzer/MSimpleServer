package com.marcoscherzer.msimpleserver.enc;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 UNFERTIG UND UNGETESTET
Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
*/

public class SimpleEncryptionService {
    /* UNFERTIG UND UNGETESTET
     Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
      Erzeugt zufälligen AES-Schlüssel */
    public static SecretKey generateKey() {
        byte[] keyBytes = new byte[32]; // 256-Bit Schlüssel
        new SecureRandom().nextBytes(keyBytes);
        return new SecretKeySpec(keyBytes, "AES");
    }
    /*
    UNFERTIG UND UNGETESTET
    Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
    URL-sichere Base64-Dekodierung
    */

    private static String base64UrlDecode(String input) {
        String adjustedInput = input.replace("-", "+").replace("_", "/");
        while (adjustedInput.length() % 4 != 0) {
            adjustedInput += "="; // Falls Padding fehlt, wieder hinzufügen
        }
        return new String(Base64.getDecoder().decode(adjustedInput), StandardCharsets.UTF_8);
    }

    /*
        UNFERTIG UND UNGETESTET
        Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
       Verschlüsselt Daten mit AES-GCM
        */
    public static String encryptData(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[12]; // 12 Bytes IV für GCM
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /*
    UNFERTIG UND UNGETESTET
    Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
     Entschlüsselt Daten mit AES-GCM
    */
    public static String decryptData(String encryptedData, SecretKey key) throws Exception {
        String[] parts = encryptedData.split(":");
        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedBytes = Base64.getDecoder().decode(parts[1]);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /*
    UNFERTIG UND UNGETESTET
    Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
    Rück-Mapping der entschlüsselten Parameter

     */
    public static Map<String, String> mapDecryptedParameters(Map<String, String> encryptedParams, SecretKey key) throws Exception {
        Map<String, String> mappedParams = new HashMap<>();

        for (Map.Entry<String, String> entry : encryptedParams.entrySet()) {
            String paramName = entry.getKey();
            String encryptedValue = base64UrlDecode(entry.getValue()); // URL-safe Base64 zurückwandeln
            String decryptedValue = decryptData(encryptedValue, key); // Entschlüsselung

            mappedParams.put(paramName, decryptedValue);
        }

        return mappedParams;
    }

    /*
      UNFERTIG UND UNGETESTET
      Author Marco Scherzer , Copyright Marco Scherzer, All rights reserved
          // **TESTAUFRUF**
     */
    public static void main(String[] args) throws Exception {
        SecretKey key = generateKey();

        // Beispiel-Verschlüsselung
        Map<String, String> encryptedParams = new HashMap<>();
        encryptedParams.put("username", encryptData("JohnDoe", key));
        encryptedParams.put("age", encryptData("30", key));

        mout.println("Verschlüsselte Parameter: " + encryptedParams);

        // Rück-Mapping nach Entschlüsselung
        Map<String, String> decryptedParams = mapDecryptedParameters(encryptedParams, key);
        mout.println("Entschlüsselte Parameter: " + decryptedParams);
    }
}

