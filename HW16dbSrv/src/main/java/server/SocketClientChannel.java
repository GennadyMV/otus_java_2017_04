package server;

import com.google.gson.Gson;
import dbservice.DbServiceMessageSupport;
import messageSystem.MessageResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class SocketClientChannel implements MsgChannel {
    private static final Logger logger = Logger.getLogger(SocketClientChannel.class.getName());
    private static final int WORKERS_COUNT = 4;

    private final BlockingQueue<MessageResponse> outputQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<DbServiceMessageSupport> inputQueue = new LinkedBlockingQueue<>();

    private final ExecutorService executor;

    private final Socket client;

    public SocketClientChannel(Socket client) {
        this.client = client;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    public void init() {
        executor.execute(this::sendMessage);
        executor.execute(this::receiveMessage);
    }

    private void receiveMessage() {
        if (!client.isConnected()) {
           this.close();
           return;
        }
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String inputLine = in.readLine();
            System.out.println("Message received: " + inputLine);
            DbServiceMessageSupport msg = getMsgFromJSON(inputLine);
            inputQueue.add(msg);
        } catch (IOException | ClassNotFoundException | ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            this.close();
        }
    }

    private DbServiceMessageSupport getMsgFromJSON(String json) throws ClassNotFoundException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(json);
        String className = (String) jsonObject.get(DbServiceMessageSupport.CLASS_NAME_VARIABLE);
        Class<?> msgClass = Class.forName(className);
        return (DbServiceMessageSupport) new Gson().fromJson(json, msgClass);
    }

    private void sendMessage() {
        if (!client.isConnected()) {
            this.close();
            return;
        }
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            MessageResponse msg = outputQueue.take();
            Object value = msg.getValue();
            if (value != null) {
                String json = new Gson().toJson(value);
                out.println(json);
                System.out.println("sent replay:" + json);
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            this.close();
        }
    }

    @Override
    public void replay(MessageResponse response) {
        outputQueue.add(response);
    }

    @Override
    public DbServiceMessageSupport pool() {
        return inputQueue.poll();
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }

    @Override
    public boolean isClosed() {
        return executor.isShutdown();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocketClientChannel that = (SocketClientChannel) o;

        if (outputQueue != null ? !outputQueue.equals(that.outputQueue) : that.outputQueue != null) return false;
        if (inputQueue != null ? !inputQueue.equals(that.inputQueue) : that.inputQueue != null) return false;
        if (executor != null ? !executor.equals(that.executor) : that.executor != null) return false;
        return client != null ? client.equals(that.client) : that.client == null;
    }

    @Override
    public int hashCode() {
        int result = outputQueue != null ? outputQueue.hashCode() : 0;
        result = 31 * result + (inputQueue != null ? inputQueue.hashCode() : 0);
        result = 31 * result + (executor != null ? executor.hashCode() : 0);
        result = 31 * result + (client != null ? client.hashCode() : 0);
        return result;
    }
}
