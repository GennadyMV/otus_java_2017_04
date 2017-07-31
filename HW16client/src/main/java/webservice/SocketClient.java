package webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class SocketClient {
    private final Socket socket;

    public SocketClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public String doRequest(String request) throws IOException {
        String result = null;
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(request);
            result = in.readLine();
            System.out.println("Message received from host: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() throws IOException {
        socket.close();
    }
}
