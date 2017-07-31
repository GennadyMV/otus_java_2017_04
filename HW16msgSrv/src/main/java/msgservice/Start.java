package msgservice;

import messageSystem.Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class Start {
    public final static int PORT = 5050;
    private final static int DB_SERVICE_NUMBER = 2;
    private final static int DB_SERVICE_PORT_FIRST = 5060;
    private static final String DB_SERVICE_START_COMMAND = "../HW16dbSrv/target/dbService.jar";

    private static final int START_DELAY_SEC = 3;

    public static void main(String[] args) throws Exception {
        System.out.println("Message service is starting...");
        new Start().activateService(PORT);
    }

    private void activateService(int port) throws Exception {
        final List<Integer> dbServicePorts = new ArrayList<>();
        for(int idx = 0; idx < DB_SERVICE_NUMBER; idx++) {
            dbServicePorts.add(DB_SERVICE_PORT_FIRST + idx);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(DB_SERVICE_NUMBER);
        for (int dbPort: dbServicePorts) {
            startClient(executorService, dbPort);
            Thread.sleep(TimeUnit.SECONDS.toMillis(START_DELAY_SEC));
        }

        RequestManager requestManager = new RequestManager(port);
        requestManager.addService(Services.DBservice, dbServicePorts);
        requestManager.start();
    }

    private void startClient(ExecutorService executorService, int dbPort) {
        executorService.submit(() -> {
            try {
               new ProcessRunnerImpl().start(DB_SERVICE_START_COMMAND, String.valueOf(dbPort));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
