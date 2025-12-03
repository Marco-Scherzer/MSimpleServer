/**
 * UNFERTIG UND UNGETESTET
 *
 * Author: Marco Scherzer with Microsoft Copilot
 * Code on Goal-Description
 * Copyright Marco Scherzer, All rights reserved
 */

"use strict";

/**
 * Definiert die erlaubten Parameter für API-Anfragen
 * @typedef {Object.<string, string>} RequestParams
 */

/**
 * API-Klasse für flexible Anfragen per URL- oder Header-Parameter
 */
export class MApiClient {
    /**
     * @param {string} baseUrl - Die Basis-URL der API
     */
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * GET-Anfrage mit URL-Parametern
     * @param {string} methodName - Name der Methode
     * @param {RequestParams} params - Schlüssel-Wert-Paare als Parameter
     * @returns {Promise<string>} - Serverantwort als Text
     */
    async sendRequestWithUrlParams(methodName, params) {
        // Validierung der Parameter
        validateParams(params);

        const paramString = new URLSearchParams(params).toString();
        const fullUrl = `${this.baseUrl}/${methodName}?${paramString}`;

        const response = await fetch(fullUrl, { method: "GET" });
        return response.text();
    }

    /**
     * GET-Anfrage mit Header-Parametern
     * @param {string} methodName - Name der Methode
     * @param {RequestParams} params - Schlüssel-Wert-Paare als Parameter
     * @returns {Promise<string>} - Serverantwort als Text
     */
    async sendRequestWithHeaderParams(methodName, params) {
        // Validierung der Parameter
        validateParams(params);

        const headers = new Headers();
        headers.append("X-Method-Name", methodName);
        headers.append("X-Params", new URLSearchParams(params).toString());

        const response = await fetch(this.baseUrl, { method: "GET", headers });
        return response.text();
    }
}

/**
 * Validiert die Parameter zur Laufzeit
 * @param {RequestParams} params - Schlüssel-Wert-Paare als Parameter
 * @throws {TypeError} - Falls ein Parameter nicht den Regeln entspricht
 */
function validateParams(params) {
    if (typeof params !== "object" || params === null) {
        throw new TypeError("params muss ein Objekt sein!");
    }
    Object.entries(params).forEach(([key, value]) => {
        if (typeof key !== "string" || typeof value !== "string") {
            throw new TypeError(`Ungültiger Parameter: ${key} = ${value}`);
        }
    });
}

