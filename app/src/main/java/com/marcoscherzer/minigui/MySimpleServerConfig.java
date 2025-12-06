
package com.marcoscherzer.minigui;

import static com.marcoscherzer.msimpleserver.http.constants.MHttpMethod.GET;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._404_NOT_FOUND;
import static com.marcoscherzer.msimpleserver.http.validation.MParameterMode.URL;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.THREADNAME;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.TIMEFIELD;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import android.content.Context;

import com.marcoscherzer.msimpleserver.MServerSocketConfig;
import com.marcoscherzer.msimpleserver.MSimpleMiniServer;
import com.marcoscherzer.msimpleserver.http.request.MHttpContentMap;
import com.marcoscherzer.msimpleserver.http.request.MHttpRequestHandler;
import com.marcoscherzer.msimpleserver.http.request.MHttpResource;
import com.marcoscherzer.msimpleserver.http.request.MResourceMethod;
import com.marcoscherzer.msimpleserver.http.request.MParameterMap;
import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestValidator;
import com.marcoscherzer.msimpleserver.http.validation.MHttpVersion;
import com.marcoscherzer.msimpleserver.http.validation.MHttp_1_1;
import com.marcoscherzer.msimpleserver.util.fileloader.MAndroidFileLoader;
import com.marcoscherzer.msimpleserver.util.fileloader.MDesktopFileLoader;
import com.marcoscherzer.msimpleserver.util.fileloader.MMultiPlatformFileLoader;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeader;

import java.util.Locale;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * aktuell muss nach schließen und reaktivieren von wlan der server neu gestartet werden
 */
public final class MySimpleServerConfig {

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static MSimpleMiniServer createAndStartServerOnDesktop() {
        MSimpleMiniServer server = null;
        try {

            MMultiPlatformFileLoader resourceFileLoader = new MDesktopFileLoader().initForDesktop(System.getProperty("user.dir"));
            MMultiPlatformFileLoader certFileLoader = new MDesktopFileLoader().initForDesktop(System.getProperty("user.dir"));
            mout.println(certFileLoader.getBaseBath() + "\n" + resourceFileLoader.getBaseBath());
            MHttpContentMap contentMap = createAndAddContent(resourceFileLoader);
            server = createAndStartServer(contentMap, certFileLoader);

        } catch (Exception exc) {
            exc.printStackTrace(mout);
            server.shutdownAllServers();
            mout.flushBufferToTargetStream();
        }
        return server;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public static MSimpleMiniServer createAndStartServerOnAndroid(Context context) {
        MSimpleMiniServer server = null;
        try {

            MMultiPlatformFileLoader resourceFileLoader = new MAndroidFileLoader().initForAndroidAssetsPath(context, "mmm/public_html");
            MMultiPlatformFileLoader certFileLoader = new MAndroidFileLoader().initForAndroidAssetsPath(context, "certificates");
            mout.println(certFileLoader.getBaseBath() + "\n" + resourceFileLoader.getBaseBath());
            MHttpContentMap contentMap = createAndAddContent(resourceFileLoader);
            server = createAndStartServer(contentMap, certFileLoader);

        } catch (Exception exc) {
            exc.printStackTrace(mout);
            server.shutdownAllServers();
            mout.flushBufferToTargetStream();
        }
        return server;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static MSimpleMiniServer createAndStartServer(MHttpContentMap contentMap, MMultiPlatformFileLoader certFileLoader) throws Exception {
        mout.println("MSimpleServer (Unready Development Version, current project-time appro. 4 weeks). \nMSimpleServer Author/Copyright Marco Scherzer. All Rights Reserved.\nProgram started.");
        MThreadLocalPrintStream.setLogHeader(new MLogHeader().addField("", THREADNAME, "").addField("@", TIMEFIELD, "|\t"));
        MThreadLocalPrintStream.setLogMode(MThreadLocalPrintStream.MGlobalLogMode.logOutToSetupedOut);
        mout.println("adding content...");

        MHttpVersion protocol = new MHttp_1_1().setRestrictSupportedMethods(GET);

        MHttpRequestValidator v = new MHttpRequestValidator(URL,protocol)
                .setMaxHeaderSize(8192)
                .setUpgradeUnencrypted(true);

        MHttpRequestHandler content1RequestHandler = new MHttpRequestHandler(contentMap.getMap(), v)
                .setAdressAndPortForHttpsRedirectResponses("192.168.0.3", 7733)
                .setSendErrorPagesFor(_404_NOT_FOUND);
        MHttpRequestHandler content1RequestHandler2 = new MHttpRequestHandler(contentMap.getMap(), v)
                .setAdressAndPortForHttpsRedirectResponses("192.168.0.3", 7733)
                .setSendErrorPagesFor(_404_NOT_FOUND);


        MServerSocketConfig httpSocket1 = new MServerSocketConfig()
                .setAddress("192.168.0.3")
                .setBiggestAllowedRequestSize(8192);
        //server.setRequestWorkoffBufferMax(10);
        //server.setPerAdressMaxConnectionsPerMilliSecond(1);//siteResourceCnt*3;

        MServerSocketConfig httpsSocket1 = new MServerSocketConfig()
                .setAddress("192.168.0.3")
                .setSSLContext(MSSLConfig1.create(certFileLoader))
                .setBiggestAllowedRequestSize(8192);

        MSimpleMiniServer server = new MSimpleMiniServer();
        server.start(7777, httpSocket1, content1RequestHandler, 1, 65535);//evtl localhost konektierungsmöglichkeiten erweitern(clientseitig)
        server.start(7733, httpsSocket1, content1RequestHandler2, 1, 65535);


        return server;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private static MHttpContentMap createAndAddContent(MMultiPlatformFileLoader resourceFileLoader) throws Exception {
        MHttpResource.setHttpResourceFileLoader(resourceFileLoader);
        MHttpResource root = new MHttpResource(Locale.ENGLISH, "/test2__.html")
                .addResourceMethod("validateTestForm1", new MResourceMethod() {
                    @Override
                    public byte[] call(MParameterMap p) {
                        String r = "MSimpleServer says: validateTestForm1(" + p + ") called";
                        String s= p.get("name");
                        mout.println(r);

                        return r.getBytes();
                    }
                })
                .addResourceMethod("validateTestForm2", new MResourceMethod() {
                    @Override
                    public byte[] call(MParameterMap p) {
                        String r = "MSimpleServer says: validateTestForm2(" + p + ") called";
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
}
