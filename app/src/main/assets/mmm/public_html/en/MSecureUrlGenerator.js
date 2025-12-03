<!DOCTYPE html>
<html lang="de">
<head>
  <meta charset="UTF-8">
  <title>MSecureUrlGenerator Test</title>
</head>
<body>
  <h1>Test MSecureUrlGenerator</h1>

  <script type="module">
    /**
     * UNFERTIG UND UNGETESTET
     *
     * Author: Marco Scherzer with Microsoft Copilot
     * Code on Goal-Description
     * Copyright Marco Scherzer, All rights reserved
     */

    import { EncryptionService } from "./EncryptionService.js";

    "use strict";

    export class MSecureUrlGenerator {
      /**
       * @param {string} baseUrl - Basis-URL der API
       */
      constructor(baseUrl) {
        this.baseUrl = baseUrl;
        this.encryptionService = new EncryptionService("/security");
      }

      /**
       * Erstellt eine verschlüsselte URL mit sicheren Parametern
       * @param {Object.<string, string>} params - Schlüssel-Wert-Paare als Parameter
       * @returns {Promise<string>} - Die sicher generierte URL
       */
      async generateSecureUrl(params) {
        const secureParams = [];

        for (const [keyName, value] of Object.entries(params)) {
          const encryptedData = await this.encryptionService.encryptData(value);
          secureParams.push(
            `${keyName}=${encodeURIComponent(encryptedData.encrypted)}&iv=${encodeURIComponent(encryptedData.iv)}`
          );
        }

        return `${this.baseUrl}?${secureParams.join("&")}`;
      }
    }

    // Beispiel-Nutzung:
    (async () => {
      const generator = new MSecureUrlGenerator("https://api.example.com/data");
      const url = await generator.generateSecureUrl({ user: "Marco", token: "12345" });
      console.log("Generierte sichere URL:", url);
    })();
  </script>
</body>
</html>

