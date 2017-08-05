package msgservice;

import messageSystem.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class RequestManager {
    private final int port;
    private final Map<Services,List<Integer>> portMapping = new HashMap<>();
    private final Map<Services,Integer> nextPortMapping = new HashMap<>();
    private final static int THREAD_COUNT = 10;
    private final Executor executor = Executors.newFixedThreadPool(THREAD_COUNT);

    public RequestManager(int port) {
        this.port = port;
    }

    public void addService(Services service, List<Integer> ports) {
        portMapping.putIfAbsent(service, ports);
        nextPortMapping.putIfAbsent(service, ports.get(0));
    }

    public void start() throws IOException {
        System.out.println("Server started on port: " + port);
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket client = serverSocket.accept();
                executor.execute(() -> processRequest(client));
            }
        }
    }

    private void processRequest(Socket client) {
        System.out.println(Thread.currentThread().getName());
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            String inputLine = in.readLine();
            System.out.println("Message received: " + inputLine);
            for (Services serv : Services.values()) {
                if (serv.name().equals(inputLine)) {
                    int port = getServicePortRoundRobin(serv);
                    System.out.println("sending port:" + port);
                    out.println(port);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getServicePortRoundRobin(Services service) {
        int nextPort = nextPortMapping.get(service);
        int lastPort = portMapping.get(service).get(portMapping.get(service).size() - 1);

        if (nextPort + 1 > lastPort ) {
            nextPortMapping.put(service, portMapping.get(service).get(0));
        } else {
            nextPortMapping.put(service, nextPort + 1);
        }

        System.out.println("next port:" + nextPort);
        return nextPort;
    }

}
