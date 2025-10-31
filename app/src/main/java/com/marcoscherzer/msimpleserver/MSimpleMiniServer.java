package com.marcoscherzer.msimpleserver;

import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.THREADNAME;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeaderFieldType.TIMEFIELD;
import static com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.mout;

import com.marcoscherzer.msimpleserver.mpool.MJobSupplier;
import com.marcoscherzer.msimpleserver.mpool.MSimplePool;
import com.marcoscherzer.msimpleserver.mpool.MSimplePool.MJob;
import com.marcoscherzer.msimpleserver.util.MSocketInfo;
import com.marcoscherzer.msimpleserver.util.MValue2D;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream;
import com.marcoscherzer.msimpleserver.util.logging.MThreadLocalPrintStream.MLogHeader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocket;

/**
 * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
 * BasisPfad bis 02.02.25 13:08: E:\User\Marco
 * Scherzer\Documents\NetBeansProjects; History vor diesem Zeitpunt über
 * BasisPfad wiederherstellbar
 */
public class MSimpleMiniServer {

    private static final HashMap<Integer, HashMap<Integer, MRunLoopRunnable>> servers = new HashMap();
    private final ExecutorService serverPool = Executors.newCachedThreadPool();//mehrfachethreads mit start startbar in welchen serversocket.accept auf verb. wartet(workoff muss nicht abgewartet werden)//Executors.newSingleThreadExecutor();
    private final MSimplePool jobPool = new MSimplePool();
    private final HashMap<InetAddress, Long> adress2PreConnectionTime = new HashMap();
    private final HashMap<InetAddress, Integer> adress2ConnectionCnt = new HashMap();
    private int requestBufferMaxCnt = 37;
    private double perAdressMaxConnectionsPerMilliSecond;


    /**
     * @param port       Der Port.
     * @param sslContext Der SSL-Kontext.
     *                   History: MSimpleHttpServer
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public MSimpleMiniServer() {
    }

    /**
     * @param port       Der Port.
     * @param sslContext Der SSL-Kontext.
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void start(int port, MServerSocketConfig socketConfig, MRequestHandler requestHandler, int parallelAcceptLoopsMax, int connectionBufferMax) {
        try {
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                mout.println("Uncaught exception in thread: " + thread.getName());
                throwable.printStackTrace(mout);
                //shutdown();
            });
            mout.println("creating Server @port=" + port);
            ServerSocket serverSocket = socketConfig.createSocket(port, connectionBufferMax);
            setRequestWorkoffBufferMax(connectionBufferMax);
//ServerSocket serverSocket = new MDummyServerSocket(port,"GET /test2.html HTTP/1.1\r\nHost: example.com\r\nUser-Agent: Mozilla/5.0\r\n\r\n");//dbg
            mout.flushBufferToTargetStream();
            MRunLoopRunnable serverRunnable = new MRunLoopRunnable(requestHandler);
            HashMap<Integer, MRunLoopRunnable> server = servers.getOrDefault(port, new HashMap<Integer, MRunLoopRunnable>());

            for (int j = 0; j < parallelAcceptLoopsMax; j++) {
                serverRunnable.setServer(this);
                serverRunnable.setServerSocket(serverSocket);
                serverPool.submit(serverRunnable);
                server.put(j, serverRunnable);
            }
            servers.putIfAbsent(port, server);

            mout.flushBufferToTargetStream();
        } catch (Exception e) {
            mout.println("Exception while starting Server @port=" + port);
            serverPool.shutdownNow();
            e.printStackTrace(mout);
            mout.flushBufferToTargetStream();
        }
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setRequestWorkoffBufferMax(int requestWorkoffBufferMax) {
        this.requestBufferMaxCnt = requestWorkoffBufferMax;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean isShuttingDownAllJobs() {
        return jobPool.isShutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final boolean isShuttingDownAllServers() {
        return serverPool.isShutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void shutdownAllServers() {
        mout.println("Shutting down servers...");
        cancelAllServerRunLoops();
        serverPool.shutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    private final void shutdownAllJobs() {
        mout.println("Shutting down jobs...");
        jobPool.shutdown();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final MRunLoopRunnable cancelServerRunLoop(Integer port, int loopnr) {
        mout.println("cancelServerRunLoop: server=" + port + ", loopNr=" + loopnr);
        HashMap<Integer, MRunLoopRunnable> loops = servers.get(port);
        MRunLoopRunnable r = null;
        if (loops != null) {
            r = loops.get(loopnr);
            if (r != null) r.cancel();
        }
        mout.println("Error: Server " + port + " not found.");
        return r;
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void cancelServerRunLoops(Integer port) {
        mout.println("cancelServerRunLoops: server=" + port);
        HashMap<Integer, MRunLoopRunnable> loops = servers.get(port);
        if (loops != null) for (MRunLoopRunnable r : loops.values()) r.cancel();
        else mout.println("Error: Server " + port + " not found.");
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void cancelAllServerRunLoops() {
        mout.println("cancelAllServerRunLoops");
        for (HashMap<Integer, MRunLoopRunnable> loops : servers.values())
            for (MRunLoopRunnable r : loops.values()) r.cancel();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void restartServerRunLoop(Integer port, int loopnr) {
        MRunLoopRunnable r = servers.get(port).get(loopnr);
        r.cancel();
        r.cloneMe();
    }

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     */
    public final void setPerAdressMaxConnectionsPerMilliSecond(int perAdressMaxConnectionsPerMilliSecond) {
        this.perAdressMaxConnectionsPerMilliSecond = perAdressMaxConnectionsPerMilliSecond;
    }
    public enum Mode {UNENCRYPTED, SECURE}
    //------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
     * History: msimplehttpserver.MHttpRunLoopRunnable
     */
    private final class MRunLoopRunnable implements Runnable {
        private final MRequestHandler requestHandler;
        private Mode mode;
        private ServerSocket serverSocket;
        private MSimpleMiniServer server;
        private boolean canceled;

        /**
         * @throws IOException Falls eine E/A-Operation fehlschlägt.
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MRunLoopRunnable(MRequestHandler requestHandler) {
            this.requestHandler = requestHandler;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        MRunLoopRunnable cloneMe() {
            MRunLoopRunnable out = new MRunLoopRunnable(requestHandler);
            out.mode = mode;
            out.serverSocket = serverSocket;
            out.server = server;
            out.canceled = false;
            return out;
        }

        /**
         * @param socket Der ServerSocket.
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private void cancel() {
            canceled = true;

        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MRunLoopRunnable setServerSocket(ServerSocket serverSocket) throws SocketException {
            this.serverSocket = serverSocket;
            mode = serverSocket instanceof SSLServerSocket ? Mode.SECURE : Mode.UNENCRYPTED;
            return this;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MRunLoopRunnable setServer(MSimpleMiniServer server) {
            this.server = server;
            return this;
        }

        /**
         * @return Der Modus (HTTP oder HTTPS).
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private Mode getMode() {
            return mode;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        @Override
        public void run() {
            MThreadLocalPrintStream.setLogHeader(new MLogHeader().addField("", THREADNAME, "").addField("@", TIMEFIELD, "|\t"));
            Thread.currentThread().setUncaughtExceptionHandler((thread, throwable) -> {
                mout.println("Uncaught exception in thread: " + thread.getName());
                throwable.printStackTrace(mout);
            });
            MSimpleObservableSocket socket = null;
            try {
                mout.println("Start listening for incoming " + mode + " connections...");
                boolean error = false;
                while (!canceled) {
                    mout.println("listening");
                    error = false;
                    try {
                        synchronized (serverSocket) {
                            socket = new MSimpleObservableSocket(serverSocket.accept());
                        }

                    } catch (IOException exc) {
                        mout.println("IOException while socket accept" + MSocketInfo.toString(socket.getSocket()));
                        exc.printStackTrace(mout);
                    }

                    socket.addCloseListener(socket.new CloseListener() {
                        @Override
                        public void onClose() {
                            //updateRegisteredConnection(socket_.getInetAddress(),-1);
                            mout.println("****************************************************************************************************************************\n\n");
                            mout.flushBufferToTargetStream();
                        }
                    });
                         /*if(!error && !connectionLimitsOk(socket.getSocket().getInetAddress())){
                             submitSupplier(socket,TOO_MANY_REQUESTS);error=true;
                         }*/
                         
                       
                         /*if(!error && jobPool.getWaitingTaskCount()>requestBufferMaxCnt){
                             mout.println("jobPool.getWaitingTaskCount()="+jobPool.getWaitingTaskCount());
                             submitSupplier(socket,SERVICE_UNAVAILABLE);error=true;                                 
                         }*/

                    if (!error) {
                        submitSupplier(socket, MInternalStatusCodes.VALID);
                        mout.println("JobMetrics: Waiting=" + jobPool.getWaitingTaskCount() + ", Running=" + jobPool.getRunningTaskCount() + ",Done=" + jobPool.getDoneTaskCount());
                    }
                    //Thread.yield();
                    mout.flushBufferToTargetStream();
                }

            } catch (Exception exc) {
                mout.println("Some other Error appeared");
                exc.printStackTrace(mout);
            }
            mout.println("runLoop canceled");
            try {
                mout.println("Shutting down serverSocket...");
                socket.close();
            } catch (IOException exc) {
                exc.printStackTrace(mout);
            }
            mout.flushBufferToTargetStream();
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private boolean connectionLimitsOk(InetAddress adress) {
            MValue2D<Integer, Long> val = updateRegisteredConnection(adress, +1);
            //mout.println("hier****************************************************************************************************************************");
            return val.get1() < 30 && val.get2() <= perAdressMaxConnectionsPerMilliSecond;
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private MValue2D<Integer, Long> updateRegisteredConnection(InetAddress adress, int increment) {
            long t = System.currentTimeMillis();
            long dt = t - adress2PreConnectionTime.getOrDefault(adress, t);
            adress2PreConnectionTime.put(adress, t);
            int cnt = adress2ConnectionCnt.getOrDefault(adress, 0);
            cnt += increment;
            adress2ConnectionCnt.put(adress, cnt);//-- evtl über Referenz
            mout.println("socket.getInetAddress()=" + adress + " cnt=" + adress2ConnectionCnt.get(adress) + " , dt=" + dt + " ,perAdressMaxConnectionsPerMilliSecond=" + (perAdressMaxConnectionsPerMilliSecond));
            return new MValue2D(cnt, dt);
        }

        /**
         * @version 0.0.1 preAlpha, @author Marco Scherzer, Author, Ideas & Architectures Marco Scherzer, Copyright Marco Scherzer, All rights reserved
         */
        private void submitSupplier(MSimpleObservableSocket socket, MInternalStatusCodes internalErrorCode) {
            jobPool.submit(new MJobSupplier(socket, internalErrorCode) {
                @Override
                public MJob get() {
                    MJob mjob = requestHandler.createNewResponseJob((MSimpleObservableSocket) this.parameters[0], (MInternalStatusCodes) this.parameters[1]);
                    this.parameters = null;
                    return mjob;
                }
            });
        }

        private boolean isCanceled() {
            return canceled;
        }
    }


}





