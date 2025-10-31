@echo off 
REM ============================================================================
REM Erzeugt Android-kompatible Root-CA, signiertes Server-Zertifikat und getrennte BKS-Keystores
REM Kompatibel mit:
REM   KeyStore serverKS = BKS (server-keystore.bks)
REM   KeyStore trustKS  = BKS (truststore.bks)
REM ============================================================================
REM Autor: Marco Scherzer, Copyright Marco Scherzer, All rights reserved
REM ============================================================================

REM === Parameter definieren ===
set KEYTOOL="C:\Program Files\Android\Android Studio\jbr\bin\keytool.exe"
set STOREPASS=mypassword
set CA_ALIAS=androidca
set SERVER_ALIAS=server
set KEYALG=RSA
set KEYSIZE=2048
set VALIDITY_CA=1825
set VALIDITY_SERVER=365
set BCPROV="Z:\MarcoScherzer-Projects\libs\bcprov-jdk18on-1.82.jar"

REM === Dateinamen ===
set CA_KEYSTORE=ca-keystore.bks
set CA_CERT=android-ca.crt
set CA_SRL=android-ca.srl
set SERVER_KEYSTORE=server-keystore.bks
set TRUSTSTORE=truststore.bks
set SERVER_CSR=server.csr
set SERVER_CERT=server.crt

REM === Distinguished Names ===
set CA_DNAME="CN=Marco CA, OU=Dev, O=Marco Scherzer, L=Bad Urach, ST=Baden-Wuerttemberg, C=DE"
set SERVER_DNAME="CN=127.0.0.1, OU=Dev, O=Marco Scherzer, L=Bad Urach, ST=Baden-Wuerttemberg, C=DE"
set SAN_EXT="SAN=dns:localhost,ip:127.0.0.1"

echo.
echo === Schritt 1: Root-CA erzeugen (BKS) ===
%KEYTOOL% -genkeypair -alias %CA_ALIAS% -keyalg %KEYALG% -keysize %KEYSIZE% -validity %VALIDITY_CA% ^
 -keystore %CA_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% ^
 -dname %CA_DNAME% -ext bc=ca:true -ext ku=keyCertSign,cRLSign

echo.
echo === Schritt 2: CA-Zertifikat exportieren (PEM für Android) ===
%KEYTOOL% -exportcert -alias %CA_ALIAS% -keystore %CA_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% -rfc -file %CA_CERT%

echo.
echo === Schritt 3: Server-Schlüsselpaar erzeugen (BKS) ===
%KEYTOOL% -genkeypair -alias %SERVER_ALIAS% -keyalg %KEYALG% -keysize %KEYSIZE% -validity %VALIDITY_SERVER% ^
 -keystore %SERVER_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% ^
 -dname %SERVER_DNAME% -ext %SAN_EXT%

echo.
echo === Schritt 4: CSR für Server-Zertifikat erzeugen ===
%KEYTOOL% -certreq -alias %SERVER_ALIAS% -keystore %SERVER_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% -file %SERVER_CSR%

echo.
echo === Schritt 5: Server-Zertifikat mit Root-CA signieren ===
%KEYTOOL% -gencert -infile %SERVER_CSR% -outfile %SERVER_CERT% -alias %CA_ALIAS% ^
 -keystore %CA_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% ^
 -validity %VALIDITY_SERVER% -rfc -ext ku=digitalSignature,keyEncipherment -ext %SAN_EXT%

echo.
echo === Schritt 6: Signiertes Server-Zertifikat in Server-Keystore importieren ===
%KEYTOOL% -importcert -alias %CA_ALIAS% -keystore %SERVER_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% -file %CA_CERT% -noprompt

%KEYTOOL% -importcert -alias %SERVER_ALIAS% -keystore %SERVER_KEYSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% -file %SERVER_CERT% -noprompt

echo.
echo === Schritt 7: Truststore erzeugen mit CA-Zertifikat ===
%KEYTOOL% -importcert -alias %CA_ALIAS% -keystore %TRUSTSTORE% -storetype BKS ^
 -providerclass org.bouncycastle.jce.provider.BouncyCastleProvider ^
 -providerpath %BCPROV% -storepass %STOREPASS% -file %CA_CERT% -noprompt

echo.
echo ============================================================================
echo Fertig. Folgende Dateien wurden erzeugt:
echo - %CA_KEYSTORE%       (Root-CA mit privatem Schlüssel)
echo - %CA_CERT%           (Android-kompatibles PEM-Zertifikat zur Installation)
echo - %SERVER_KEYSTORE%   (Server-Zertifikat + privater Schlüssel)
echo - %SERVER_CERT%       (Signiertes Server-Zertifikat)
echo - %TRUSTSTORE%        (Nur Root-CA, für TrustManagerFactory)
echo ============================================================================
cmd /k
pause

