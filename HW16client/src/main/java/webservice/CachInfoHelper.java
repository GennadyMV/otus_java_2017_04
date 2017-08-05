package webservice;

import cache.CachInfo;
import com.google.gson.Gson;
import dbservice.DbServiceMessageSupport;
import dbservice.MsgGetCachInfo;
import messageSystem.Address;
import messageSystem.Services;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static msgservice.Start.PORT;

/**
 * @author sergey
 * created on 30.07.17.
 */

@Component
class CachInfoHelper {
    private final int messageServicePort = PORT;

    private final SocketClient socketClient  = new SocketClient();

    private int getDBservicePort() throws IOException {
        try {
            socketClient.initPool("localhost", messageServicePort);
            final String portDbService = socketClient.doRequest("localhost", messageServicePort, Services.DBservice.name());
            System.out.println("got portDbService:" + portDbService);
            return Integer.parseInt(portDbService);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error:" + e.getMessage());
        }
    }

    CachInfo get(Address adress) throws IOException {
        try {
            final int portDbService = getDBservicePort();
            final DbServiceMessageSupport msg = new MsgGetCachInfo();
            msg.setFrom(adress);
            System.out.println("connect to port:" + portDbService);
            socketClient.initPool("localhost", portDbService);
            final String responseFromDb = socketClient.doRequest("localhost", portDbService, new Gson().toJson(msg));
            System.out.println("got replay:" + responseFromDb);
            return new com.google.gson.Gson().fromJson(responseFromDb, CachInfo.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error:" + e.getMessage());
        }
    }
}
