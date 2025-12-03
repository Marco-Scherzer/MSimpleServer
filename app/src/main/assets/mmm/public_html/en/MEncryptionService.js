/**
 * UNFERTIG UND UNGETESTET
 *
 * Author: Marco Scherzer with Microsoft Copilot
 * Code on Goal-Description
 * Copyright Marco Scherzer, All rights reserved
 */

"use strict";

export class MClientEncryptionService {
    /**
     * @param {string} serverUrl - Basis-URL des Servers für Schlüsselabruf
     */
    constructor(serverUrl) {
        this.serverUrl = serverUrl;
    }

    /**
     * Holt den Schlüssel dynamisch vom Server
     * @returns {Promise<Uint8Array>} - Der empfangene AES-Schlüssel
     */
    async getEncryptionKey() {
        const response = await fetch(`${this.serverUrl}/getEncryptionKey`);
        return Uint8Array.from(atob(await response.text()), c => c.charCodeAt(0));
    }

    /**
     * URL-sichere Base64-Kodierung
     * @param {string} input - Zeichenkette zum Kodieren
     * @returns {string} - URL-sichere Base64-Kodierung
     */
    static base64UrlEncode(input) {
        return btoa(input)
            .replace(/\+/g, "-")
            .replace(/\//g, "_")
            .replace(/=+$/, "");
    }

    /**
     * URL-sichere Base64-Dekodierung
     * @param {string} input - URL-sicher codierte Zeichenkette
     * @returns {string} - Dekodierte Zeichenkette
     */
    static base64UrlDecode(input) {
        let adjustedInput = input.replace(/-/g, "+").replace(/_/g, "/");
        while (adjustedInput.length % 4) {
            adjustedInput += "=";
        }
        return atob(adjustedInput);
    }

    /**
     * AES-GCM-Verschlüsselung der Daten ohne Speicherung des Schlüssels
     * @param {string} data - Klartextdaten zur Verschlüsselung
     * @returns {Promise<{encrypted: string, iv: string}>} - Verschlüsselte Daten & Initialisierungsvektor
     */
    async encryptData(data) {
        const key = await this.getEncryptionKey();
        const encoder = new TextEncoder();
        const encodedData = encoder.encode(data);

        const keyBuffer = await crypto.subtle.importKey(
            "raw",
            key,
            { name: "AES-GCM", length: 256 },
            false,
            ["encrypt"]
        );

        const iv = crypto.getRandomValues(new Uint8Array(12));
        const encrypted = await crypto.subtle.encrypt({ name: "AES-GCM", iv }, keyBuffer, encodedData);

        return {
            encrypted: MClientEncryptionService.base64UrlEncode(
                String.fromCharCode(...new Uint8Array(encrypted))
            ),
            iv: MClientEncryptionService.base64UrlEncode(
                String.fromCharCode(...iv)
            )
        };
    }
}


