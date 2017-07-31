package server;

import dbservice.DbServiceMessageSupport;
import dbservice.UserCacheMessageSupport;
import messageSystem.MessageResponse;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class DbServer implements DbServerMBean {
    private static final Logger logger = Logger.getLogger(DbServer.class.getName());

    private static final int THREADS_NUMBER = 2;
    private final int port;

    private final ExecutorService executor;
    private final Map<MsgChannel, Long> channels = new ConcurrentHashMap<>();
    private final UserCacheMessageSupport cache;

    public DbServer(int port, UserCacheMessageSupport cache) {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);
        this.cache = cache;
        this.port = port;
    }

    public void start() throws Exception {
        executor.submit(this::processSocketMessages);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port: " + serverSocket.getLocalPort());
            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept(); //blocks
                SocketClientChannel channel = new SocketClientChannel(client);
                channel.init();
                channels.put(channel, System.currentTimeMillis());
            }
        } finally {
            System.out.println("Server stopped");
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private Object processSocketMessages() throws InterruptedException {
        while (true) {
            try {
                for (Iterator<MsgChannel> iterator = channels.keySet().iterator(); iterator.hasNext();) {
                    MsgChannel channel = iterator.next();
                    if (channel.isClosed()) {
                        System.out.println("removing chanel, time live:" + (System.currentTimeMillis() - channels.get(channel)));
                        iterator.remove();
                    } else {
                        DbServiceMessageSupport msg = channel.pool();
                        if (msg != null) {
                            msg.setCacheEngine(cache);
                            MessageResponse response = msg.exec();
                            channel.replay(response);
                        }
                    }
                }
                Thread.sleep(200);
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }
}
