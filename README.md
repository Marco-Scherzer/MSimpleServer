# This is the Page of MSimpleServer and my Simple Mini Server Architecture.

#### Development started in early 2025 and had reached 3 to 4 weeks of progress when it started to work for my own simple scenario purposes. 
#### This early version focuses on HTTP and HTTPS handler implementations and currently supports only simple HttpServer and HttpsServer features with a focus on security through minimalism.

#### Features after 4 weeks:
- **Parallel accept loops for redundancy**
- **General SSL redirection** of unencrypted browser HTTP requests
- **Webpage serving**
- **Custom REST API** with own endpoint declarations

#### MSimpleServer is a single-developer project implemented entirely in Java. External dependencies: 0.

#### Since October 31, 2025, I decided to additionally develop my project on GitHub.

#### It currently runs on both desktop and Android environments via console interface.

#### While the current focus is on Android GUI integration ( com.marcoscherzer.mminigui ), the core package ( com.marcoscherzer.msimpleserver ) remains platform-independent and compatible with desktop environments.

## Legal Notice
This software is proprietary and protected by copyright law.  
Idea, Author, and Copyright: Marco Scherzer  
All rights reserved.
This repository is to be treated as **private**.  
It is not intended for public collaboration or external contributions.  
Access is restricted, and any interaction with the repository is strictly forbidden.
Strictly prohibited:  
Forking, copying, reverse engineering, decompiling, modifying, redistributing, or any unauthorized
use of this software.

#### Common About-Me (to prevent wrong imaginations):

I’m a 43‑year‑old old‑school developer. I’m an all‑rounder, not a specialist. Not for networking, nor for HTTP things or Android. I’ve been developing since my very early childhood.
According to the calculation of Microsoft Copilot, 
I spent more than twice as many hours developing as someone who starts at 20 with a 40‑hours‑per‑week job, 
because I developed most of my life Monday till Sunday for more than 12 hours a day.
Even if I let only count it the last 25 serious to take years 
sadly this is already more than two times 25 years sitting in front of a computer solving problems.
Moreover, I have always been an extremely money‑poor person. 
This will finally be one of my last projects because I planed since longer time already to stop development forever,
despite of helpfull things like Microsoft Copilot that save me frist time in lofe time since I started using it in November 2024.
Happily any HTTP knowledge I needed for this Project to refresh has been available on demand through Microsoft Copilot.
I recently transitioned from multiplatform development to Android-specific projects.
My current work is intended solely for internal use within my own self‑employment, which means for
myself and my projects. Until now, all of my work has been closed-source. I have always worked independently and entirely on
my own. Everything I have built has been done only on my local computer, and everything I create is formally
protected by law. Only now, in my later years, I have published something here on GitHub for the first time.
Except for what I publish here on this GitHub page (https://github.com/Marco-Scherzer),
all of my software projects remain closed-source.

My source code and any compiled versions that may sometimes appear here,  
as well as any texts or other content on this page, are protected by copyright. 
All rights are reserved, which means that any kind of use, copying, linking or downloading and many
things more is not permitted. If I ever decide to write a license for the binary, so that other people may at least be allowed to
download the executable or installer, this text will also include the license and the exact location where the binaries can be downloaded.

### Example

```java
/**
 * @version 0.0.1 preAlpha
 * @author Marco Scherzer
 * Author, Ideas, APIs, Nomenclatures & Architectures: Marco Scherzer
 * Copyright Marco Scherzer, All Rights Reserved
 */
private static MSimpleMiniServer createAndStartServer(MHttpContentMap contentMap, MMultiPlatformFileLoader certFileLoader) throws Exception {
    mout.println("MSimpleServer (Unready Development Version, current project-time approx. 4 weeks).\n" +
                 "MSimpleServer Author/Copyright Marco Scherzer. All Rights Reserved.\n" +
                 "Program started.");

    MThreadLocalPrintStream.setLogHeader(
        new MLogHeader()
            .addField("", THREADNAME, "")
            .addField("@", TIMEFIELD, "|\t")
    );
    MThreadLocalPrintStream.setLogMode(MThreadLocalPrintStream.MGlobalLogMode.logOutToSetupedOut);

    mout.println("adding content...");

    MHttpVersion protocol = new MHttp_1_1().setSupportedMethods(GET);

    MHttpRequestValidator v = new MHttpRequestValidator(protocol)
            .setMaxHeaderSize(8192)
            .setUpgradeUnencrypted(true);

    MHttpRequestHandler content1RequestHandler = new MHttpRequestHandler(contentMap.getMap(), v)
            .setAdressAndPortForHttpsRedirectResponses("192.168.0.3", 7733)
            .setSendErrorPagesFor(_404_NOT_FOUND);

    MServerSocketConfig httpSocket1 = new MServerSocketConfig()
            .setAddress("192.168.0.3")
            .setBiggestAllowedRequestSize(8192);

    MServerSocketConfig httpsSocket1 = new MServerSocketConfig()
            .setAddress("192.168.0.3")
            .setSSLContext(MSSLConfig1.create(certFileLoader))
            .setBiggestAllowedRequestSize(8192);

    MSimpleMiniServer server = new MSimpleMiniServer();
    server.start(7777, httpSocket1, content1RequestHandler, 1, 65535);
    server.start(7733, httpsSocket1, content1RequestHandler, 1, 65535);

    return server;
} 

/**
 * @version 0.0.1 preAlpha
 * @author Marco Scherzer
 * Author, Ideas, APIs, Nomenclatures & Architectures: Marco Scherzer
 * Copyright Marco Scherzer, All Rights Reserved
 */
private static MHttpContentMap createAndAddContent(MMultiPlatformFileLoader resourceFileLoader) throws Exception {
    MHttpResource.setHttpResourceFileLoader(resourceFileLoader);

    MHttpResource root = new MHttpResource(Locale.ENGLISH, "/test2__.html")
        .addResourceMethod("validateTestForm1", new MResourceMethod() {
            @Override
            public byte[] call(Map<String, String> params) {
                String r = "MSimpleServer says: validateTestForm1(" + params + ") called";
                mout.println(r);
                return r.getBytes();
            }
        })
        .addResourceMethod("validateTestForm2", new MResourceMethod() {
            @Override
            public byte[] call(Map<String, String> params) {
                String r = "MSimpleServer says: validateTestForm2(" + params + ") called";
                mout.println(r);
                return r.getBytes();
            }
        });

    MHttpContentMap contentMap = new MHttpContentMap();
    contentMap.addContent("/", root, false)
              .addContent("/test2__", root, false)
              .addContent("/MApiClient.js", Locale.ENGLISH, "MApiClient.js", false);

    contentMap.addContent("_404_NOT_FOUND", Locale.ENGLISH, "notFound.html", false)
              .addContent("/test.pdf", Locale.ENGLISH, "test.pdf", false);

    return contentMap;
}

```
<br>


## Repository Sale Notice

This repository is offered non-exclusiv for sale in its current, up‑to‑date code state.
If you are interested, please contact me via my listed email address

**Important Notice:**
For security reasons, contracts are concluded exclusively after personal identification and
presentation of the buyer’s official ID document in the presence of my trusted notary in Karlsruhe,
Germany.
I always identify with ID-Card.
Since my childhood, I have always had exactly and only one banking account at a trusted local bank,
ensuring protocolized secure banking.
I never accept any transactions other than traditional, documented transactions processed directly
through my local bank.

Contact: fahrservice.1@gmail.com

# Declaration to Avoid Scamming, Theft of Intellectual Property, and Deception by Fraudsters

To prevent scamming, theft of intellectual property, and the deception of persons by fraudulent
actions, I hereby make the following statement once and for all, clearly and explicitly:

**Please note:** I never grant any permissions, not in the past, not now, and not in the future.

---

## 1. Abuse and Phishing

To protect against abuse and phishing, please note:
There are **no other websites, email addresses, or communication channels** associated with this software except the official contact listed here.

If you encounter the code or binaries anywhere other than:

- [https://github.com/Marco-Scherzer](https://github.com/Marco-Scherzer)
- The same repository archived in the Wayback Machine (pre‑publication archiving)

then it constitutes **abuse, a scam, and theft of law‑protected intellectual property**.

In such a case, please inform GitHub and email me.

---

## 2. False Claims of Involvement or Permission

Any false claim by any person to be in any way involved in my projects, or to have received any
permission from me – whether for usage, reproduction, replication, especially of APIs,
functionality, modularity, architecture, or for public display – is untrue and constitutes a *
*serious criminal offense**.

This includes in particular:

- Scamming and fraudulent deception
- Theft of intellectual property
- Always implicit defamation of the true author of a work and his business, since the truth about
  the origin of a work is reputation‑critical

I explicitly declare that I **never grant any licenses of any kind for an open source work and
especially not for its code – not in the past, not now, and not in the future.**

---

## 3. Reporting Criminal Acts

If you have information pointing to criminal acts as described under points 1–2, I request that you
immediately:

- Inform the **Economic Cybercrime Division of the German Police (Zentrale Ansprechstellen
  Cybercrime, ZAC)**
    - [Polizei.de – Zentrale Ansprechstellen Cybercrime](https://www.polizei.de/Polizei/DE/Einrichtungen/ZAC/zac_node.html)
    - [ZAC Contact List (Bund & Länder, PDF)](https://www.wirtschaftsschutz.info/DE/Themen/Cybercrime/Ansprechpartner/ZACErreichbarkeiten.pdf?__blob=publicationFile&v=3)

- Contact **GitHub** via its official abuse reporting email: **abuse@github.com**
    - [GitHub Docs – Reporting Abuse or Spam](https://docs.github.com/en/communities/maintaining-your-safety-on-github/reporting-abuse-or-spam)

**Your civil courage counts. Help prevent such crimes, make Open Source safer, and protect the
reputation of authors.**

---


<br>
<br>
<br>
<br>
<br>
<br>

## Legal Notice

This software is proprietary and protected by copyright law.  
Idea, Author, and Copyright: Marco Scherzer
All rights reserved.

Forking, copying, reverse engineering, decompiling, modifying, redistributing,  
or any unauthorized use of this software is strictly forbidden.

**Contact**: fahrservice.1@gmail.com


