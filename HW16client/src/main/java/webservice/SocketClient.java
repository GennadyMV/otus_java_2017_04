package webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sergey
 * created on 30.07.17.
 */
class SocketClient {
    private final int SOCKET_NUMBER = 2;

    private final Map<TcpConnectInfo, BlockingQueue<Socket>> socketPool = new ConcurrentHashMap<>();

    void initPool(String host, int port) throws IOException {
        final TcpConnectInfo connectInfo = new TcpConnectInfo(host, port);
        if (socketPool.get(connectInfo) != null) {
            return;
        }

        final BlockingQueue<Socket> sockets = new ArrayBlockingQueue<>(SOCKET_NUMBER);
        for(int idx = 0; idx < SOCKET_NUMBER; idx++) {
            sockets.add(openSocket(connectInfo));
        }
        socketPool.put(connectInfo, sockets);
        System.out.println("Created pool for:"  + connectInfo);
    }

    private Socket openSocket(TcpConnectInfo connectInfo) throws IOException {
        return new Socket(connectInfo.getHost(), connectInfo.getPort());
    }

    private Socket getSocketFromPool(TcpConnectInfo connectInfo) throws InterruptedException, IOException {
        final BlockingQueue<Socket> queue = socketPool.get(connectInfo);
        if (queue == null) {
            throw new IllegalArgumentException("Not found connections for:" + connectInfo);
        }
        System.out.printf("getting connect from pool:" + connectInfo);
        final Socket socket = queue.take();
        if (!socket.isClosed()) {
            return socket;
        }
        return openSocket(connectInfo);
    }

    private void returnToPool(TcpConnectInfo connectInfo, Socket socket) {
        socketPool.get(connectInfo).add(socket);
    }

    String doRequest(String host, int port, String request) throws IOException, InterruptedException {
        String result;
        final TcpConnectInfo connectInfo = new TcpConnectInfo(host, port);
        final Socket socket = getSocketFromPool(connectInfo);
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            out.println(request);
            result = in.readLine();
            System.out.println("Message received from host: " + result);
        } finally {
            if (socket != null) {
              returnToPool(connectInfo, socket);
            } else {
                System.err.println("Error. Unexpected case: socket is null");
            }
        }
        return result;
    }

    private class TcpConnectInfo {
        String host;
        int port;

        public TcpConnectInfo(String host, int port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TcpConnectInfo that = (TcpConnectInfo) o;

            if (port != that.port) return false;
            return host != null ? host.equals(that.host) : that.host == null;
        }

        @Override
        public int hashCode() {
            int result = host != null ? host.hashCode() : 0;
            result = 31 * result + port;
            return result;
        }

        @Override
        public String toString() {
            return "TcpConnectInfo{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }
}
