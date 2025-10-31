
/*
 * //UNFERTIG UND UNGETESTET
 * 
 * Author Marco Scherzer with Microsoft Copilot: Code on Goal-Description
 * Copyright Marco Scherzer, All rights reserved
 */
import { SecureUrlGenerator } from "./SecureUrlGenerator.js";

"use strict";

(async () => {
    const secureUrlGen = new SecureUrlGenerator("/security/decrypt");

    const params = { name: "JohnDoe", age: "30" };
    const secureUrl = await secureUrlGen.generateSecureUrl(params);

    console.log("Gesicherte URL:", secureUrl);
})();

