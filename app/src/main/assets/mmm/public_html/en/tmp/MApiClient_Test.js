/*
 * //UNFERTIG UND UNGETESTET
 * 
 * Author Marco Scherzer with Microsoft Copilot: Code on Goal-Description
 * Copyright Marco Scherzer, All rights reserved
 */
import { ApiClient } from "./module.js";

"use strict";

(async () => {
    const apiClient = new ApiClient("/api/resource");

    const methodName = "validateTestForm1";
    const params = { name: "JohnDoe", age: "30" };

    // Anfrage über URL-Parameter
    const urlResponse = await apiClient.sendRequestWithUrlParams(methodName, params);
    console.log("Antwort (URL-Params):", urlResponse);

    // Anfrage über Header-Parameter
    const headerResponse = await apiClient.sendRequestWithHeaderParams(methodName, params);
    console.log("Antwort (Header-Params):", headerResponse);
})();
