
/*
 * //UNFERTIG UND UNGETESTET
 * 
 * Author Marco Scherzer with Microsoft Copilot: Code on Goal-Description
 * Copyright Marco Scherzer, All rights reserved
 */

import { EncryptionService } from "./EncryptionService.js";

"use strict";

export class MSecureUrlGenerator {
    /**
     * @param {string} baseUrl Basis-URL der API
     */
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
        this.encryptionService = new EncryptionService("/security");
    }

    /**
     * Erstellt eine verschlüsselte URL mit sicheren Parametern
     * @param {Object.<string, string>} params Schlüssel-Wert-Paare als Parameter
     * @returns {Promise<string>} Die sicher generierte URL
     */
    async generateSecureUrl(params) {
        let secureParams = [];

        for (const [keyName, value] of Object.entries(params)) {
            const encryptedData = await this.encryptionService.encryptData(value);
            secureParams.push(`${keyName}=${encodeURIComponent(encryptedData.encrypted)}&iv=${encodeURIComponent(encryptedData.iv)}`);
        }

        return `${this.baseUrl}?${secureParams.join("&")}`;
    }
}

