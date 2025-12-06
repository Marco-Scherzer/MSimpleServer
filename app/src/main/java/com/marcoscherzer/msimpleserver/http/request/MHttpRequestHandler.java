package com.marcoscherzer.msimpleserver.http.request;

import static com.marcoscherzer.msimpleserver.MInternalStatusCodes.VALID;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes.VALID_AND_COMPLETE;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._200_OK;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._301_MOVED_PERMANENTLY;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._404_NOT_FOUND;
import static com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes._406_NOT_ACCEPTABLE;
import static com.marcoscherzer.msimpleserver.util.compression.MCompression.MSupportedCompressionType.IDENTITY;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.THREADNAME;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.TIMEFIELD;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.MInternalStatusCodes;
import com.marcoscherzer.msimpleserver.MRequestHandler;
import com.marcoscherzer.msimpleserver.MSimpleMiniServer.Mode;
import com.marcoscherzer.msimpleserver.MSimpleObservableSocket;
import com.marcoscherzer.msimpleserver.http.constants.MHttpResponseStatusCodes;
import com.marcoscherzer.msimpleserver.http.response.MHttpResponse;
import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestData;
import com.marcoscherzer.msimpleserver.http.validation.MHttpRequestValidator;
import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;
import com.marcoscherzer.msimpleserver.util.MValue3D;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeader;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.net.ssl.SSLSocket;

/**
 * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 */
public class MHttpRequestHandler extends MRequestHandler {
    private final HashMap<String, MHttpResource> url2Resource;
    private final MHttpRequestValidator requestValidator;
    private MHttpResponseStatusCodes[] errorsToSendPagesForInsteadOfPlain;
    private int portForHttpsRedirectResponses;
    private String addressForHttpsRedirectResponses;


    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MHttpRequestHandler(HashMap<String, MHttpResource> url2Resource, MHttpRequestValidator requestValidator) {
        this.requestValidator = requestValidator;
        this.url2Resource = url2Resource;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    static final String createErrorContent(MHttpResponseStatusCodes errorCode, String message) {
        return "<html>" +
                "<head>" +
                "<title>" + errorCode + "</title>" +
                "</head>" +
                "<body>" +
                "<h1>" + errorCode + "</h1>" +
                "<p>" + message + "</p>" +
                "</body>" +
                "</html>";
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MHttpRequestHandler setSendErrorPagesFor(MHttpResponseStatusCodes... errorsToSendPagesForInsteadOfPlain) {
        this.errorsToSendPagesForInsteadOfPlain = errorsToSendPagesForInsteadOfPlain;
        return this;
    }

    /**
     * @param port Der https-Redirect-Port für https-Redirect-Responses.
     * @return Die aktuelle Instanz von MRequestHandler.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * Setzt den Client-Side Port für https-Redirect-Responses die ausgeführt werden wenn der unsichere http Port vom Client/Browser in Requests verwendet wird.
     * Damit spricht der Client im folgenden https Request dann den gesetzten Port an.
     * Dies kann z.B bei Client-Requests über Port-Weiterleitung in VirtualBox oder über einen Proxy/ReversProxys sinnvoll sein,
     * da der Client die https Requests an den Proxy stellt und die PortNummer unter umständen
     * von der https-Portnummer mit der der Server instanziert wurde (z.B 443) abweicht.
     */
    public final MHttpRequestHandler setAdressAndPortForHttpsRedirectResponses(String httpsAddressToTellClient, int portToTellClient) {
        this.addressForHttpsRedirectResponses = httpsAddressToTellClient;
        this.portForHttpsRedirectResponses = portToTellClient;
        return this;
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    @Override
    protected MJob<?> createNewResponseJob(MSimpleObservableSocket socket, MInternalStatusCodes internalErrorCode) {
        return new MResponseJob(socket, internalErrorCode);
    }

/*
     }catch (IllegalBlockingModeException exc) {
                                    submitSupplier(socket,INTERNAL_SERVER_ERROR);error=true;
                            }catch (SocketTimeoutException exc) {
                                    submitSupplier(socket,REQUEST_TIMEOUT);error=true;
                            }catch (SecurityException exc) {
                                    submitSupplier(socket,INTERNAL_SERVER_ERROR);error=true;
                            }
*/

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final MHttpResponse createErrorResponse(MHttpRequest request, MHttpResponseStatusCodes errorCode, String additionalMessage) throws Exception {
        return createResponse(request, errorCode, additionalMessage, createErrorContent(errorCode, additionalMessage).getBytes());
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final MHttpResponse createLowLevelErrorResponse(MHttpRequest request, MInternalStatusCodes errorCode, String additionalMessage) throws Exception {
        MHttpResponseStatusCodes httpErrorCode = null;
        switch (errorCode) {
            case INTERNAL_SERVER_ERROR:
                httpErrorCode = MHttpResponseStatusCodes._500_INTERNAL_SERVER_ERROR;
                break;
            case REQUEST_TIMEOUT:
                httpErrorCode = MHttpResponseStatusCodes._408_REQUEST_TIMEOUT;
                break;
            case TOO_MANY_REQUESTS:
                httpErrorCode = MHttpResponseStatusCodes._429_TOO_MANY_REQUESTS;
                break;
            case SERVICE_UNAVAILABLE:
                httpErrorCode = MHttpResponseStatusCodes._503_SERVICE_UNAVAILABLE;
                break;
        }
        return createResponse(request, httpErrorCode, additionalMessage, createErrorContent(httpErrorCode, additionalMessage).getBytes());
    }

    /**
     * @param request           Die HTTP-Anfrage.
     * @param responseCode      Der Fehlercode.
     * @param additionalMessage Die zusätzliche Nachricht.
     * @return Die HTTP-Fehlerantwort.
     * @throws Exception Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final MHttpResponse createResponse(MHttpRequest request, MHttpResponseStatusCodes responseCode, String additionalMessage, byte[] content) throws Exception {
        MHttpResponse response = new MHttpResponse();

        response.setStatusCode(responseCode.toString());
        String protocol = null;
        if (request != null) protocol = request.getProtocol();
        if (protocol != null) response.setProtocol(protocol);
        else response.setProtocol("HTTP/1.0");
        //optional
        String connection = null;
        if (request != null) connection = request.getHeaders().get("Connection");
        if (connection != null) {
            response.getHeader().setConnection(connection);
        }

        if (content != null)
            response.getBody().setContent(content, StandardCharsets.UTF_8, connection, IDENTITY.getValue());

        mout.println("Error-Response to send:\n" + response);

        return response;
    }

    /**
     * @param request Die HTTP-Anfrage.
     * @return Das Ergebnis der Ressourcenanfrage.
     * @throws Exception Falls eine E/A-Operation fehlschlägt.
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final MValue3D<MHttpResponseStatusCodes, String, MHttpResponse> createResourceResponse(MHttpRequest request) throws Exception {
        mout.println("Checking if resource \"" + request.getResourcePath() + "\" exists...");
        MHttpResource resource = url2Resource.get(request.getResourcePath());
        mout.println("Resource " + (resource == null ? "not " : "") + "found \"" + request.getResourcePath() + "\"");
        if (resource == null) return new MValue3D(_404_NOT_FOUND, "", null);

        //Trying to load resource from persistence...;
        String contentLanguage = request.getHeaders().getEvaluatedContentLanguage(resource);//defaultFallback en

        //falls gar kein acceptHeader dann ebenfalls _406_NOT_ACCEPTABLE
        String contentTypeAndCharset = request.getHeaders().getEvaluatedContentTypeAndCharset(resource);
        if (contentTypeAndCharset == null) return new MValue3D(_406_NOT_ACCEPTABLE, "", null);

        String contentEncoding = request.getHeaders().getEvaluatedContentEncoding(resource);//defaultFallback IDENTITY
        MHttpResponse response = new MHttpResponse();
        response.setProtocol(request.getProtocol());
        response.setStatusCode(_200_OK.toString());
        String connection = request.getHeaders().get("Connection");
        if (connection != null) {
            response.getHeader().setConnection(connection);
        }

        byte[] resourceBytes;
        if (request.getResourceMethod() != "") {
            MResourceMethod m = resource.getResourceMethod(request.getResourceMethod());

            //strukturierte parameter in map schreiben
            resourceBytes = m.call(request.getResourceMethodParameters());

        } else {
            resourceBytes = resource.loadResource(contentLanguage);
            //mout.println("Resource " + (resource == null ? "not " : "") + "found \""+request.getResourcePath()+"\"");
            if (resourceBytes == null) return new MValue3D(_404_NOT_FOUND, "", null);
        }

        response.getBody()
                .setContent(resourceBytes, resource.getCharset(), contentTypeAndCharset, contentEncoding)
                .setContentLanguage(contentLanguage);

        return new MValue3D(_200_OK, "", response);
    }

    /**
     * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    class MResponseJob extends MJob {

        private final MSimpleObservableSocket socket;
        private final Mode mode;
        private final MInternalStatusCodes internalErrorCode;


        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MResponseJob(MSimpleObservableSocket socket, MInternalStatusCodes internalErrorCode) {
            this.socket = socket;
            this.internalErrorCode = internalErrorCode;
            mode = socket.getSocket() instanceof SSLSocket ? Mode.SECURE : Mode.UNENCRYPTED;
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public final Void call() {
            MThreadLocalPrintStream.setLogHeader(new MLogHeader().addField("\t\t", THREADNAME, "").addField("@", TIMEFIELD, "|\t"));
            //serverSocket.setReceiveBufferSize(requestHandler.getRequestValidator().getBiggestAllowedHeaderSize());//protokoll kann v. request zu req theor. eine unterschiedliche http version haben->Max der protokolle
            Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
                mout.println("Uncaught exception in thread: " + thread.getName());
                throwable.printStackTrace(mout);
            });

            try {
                mout.println("new " + mode + " connection accepted:\n");
                MHttpResponse response = null;

                if (internalErrorCode != VALID) {
                    mout.println("low level error appeared");
                    response = createLowLevelErrorResponse(null, internalErrorCode, null);
                    writeResponse(response);
                    return null;
                }
                //try { mout.println(MSocketInfo.toString(getSocket()));} catch(Exception exc){ mout.println("Error while printing socket paramters");exc.printStackTrace(mout);}


                MHttpRequestData data = requestValidator.isValidRequest(socket.getSocket());
                mout.println("Request Data:\n" + data);
                if (data.getResponseCode() == VALID_AND_COMPLETE) {
                    MHttpRequest request = new MHttpRequest(data);
                    mout.println("Request:\n" + request + "\n");
                    mout.println("Creating response...");

                    MValue3D<MHttpResponseStatusCodes, String, MHttpResponse> r = createResourceResponse(request);
                    if (r.get1() != _200_OK) {
                        mout.println("FAILED...Creating error response.");
                        response = createErrorResponse(request, r.get1(), r.get2());
                        writeResponse(response);
                    } else {
                        mout.println("Sending response");
                        response = r.get3();
                        writeResponse(response);
                    }
                } else {
                    //mout.println(data.isValidAndCompleteOrErrorCode());
                    if (data.getResponseCode() == MHttpResponseStatusCodes._302_FOUND || data.getResponseCode() == _301_MOVED_PERMANENTLY) {
                        MHttpRequest request = new MHttpRequest(data);
                        mout.println("Request:\n" + request + "\n");
                        mout.println("...Creating redirect response.");
                        response = createRedirectResponse(request, data.getResponseCode(), "");
                    } else {
                        mout.println("...Creating error response.");
                        response = createErrorResponse(null, data.getResponseCode(), "");
                    }
                    writeResponse(response);
                }


            } catch (Exception exc) {
                mout.println("\nSome other Error appeared");
                exc.printStackTrace(mout);
            }
            try {
                socket.close();
            } catch (IOException exc) {
                mout.println("Error while closing socket.");
                exc.printStackTrace(mout);
            }
            mout.flushBufferToTargetStream();
            return null;
        }


        /**
         * @param response Die HTTP-Antwort.
         * @throws Exception Falls eine E/A-Operation fehlschlägt.
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private final void writeResponse(MHttpResponse response) { //response.create());//response.create() - contentHeaderAndBody.createEncodedBody()
            try {
                OutputStream os = socket.getSocket().getOutputStream();
                os.write(response.create());
                os.flush();
                mout.println("RESPONSE SENT !");
                try {
                    os.close();
                } catch (Exception exc) {
                    mout.println("Error while closing socket.");
                }
            } catch (IOException exc) {
                mout.println("Error while getting/writing to socket output stream. Cannot send anything back\n" + socket.getSocket() + "\n");
                exc.printStackTrace(mout);
            }
        }

        /**
         * @version 0.0.1 preAlpha unready intermediate state, @author Marco Scherzer, Author, Ideas, APIs, Nomenclatures & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MHttpResponse createRedirectResponse(MHttpRequest request, MHttpResponseStatusCodes errorCode, String additionalMessage) throws Exception {
            MHttpResponse response = createResponse(request, errorCode, additionalMessage, null);

            // Korrigieren des Pfads
            String originalPath = request.getResourcePath();
            //String serverIP = InetAddress.getLocalHost().getHostAddress();//bei Proxying/portweiterleitung in VirtualBox offenbar 127.0.1.1
            //ClientSide Port für Redirections(z.B bei Port-Weiterleitung in VirtualBox oder über Proxy/ReversProxy) die an den Browser gesendet werden um die https-Redirection über vom Browser anzusprechenden abweichenden (Proxy) Port möglich zu machen.
            String newPath = "https://" + addressForHttpsRedirectResponses + ":" + portForHttpsRedirectResponses + "/" + originalPath.replaceFirst("^/+", "").replaceFirst("^(http|https)://", "");

            mout.println("setting redirection to " + newPath);
            response.getHeader().addHeader("Location", newPath);
            response.getHeader().addHeader("Cache-Control", "no-cache");
            response.getHeader().addHeader("Expires", "-1");

            return response;
        }


    }//end MJobRunnable

}

