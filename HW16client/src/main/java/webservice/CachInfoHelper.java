package webservice;

import cache.CachInfo;
import com.google.gson.Gson;
import dbservice.DbServiceMessageSupport;
import dbservice.MsgGetCachInfo;
import messageSystem.Address;
import messageSystem.Services;

import java.io.IOException;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class CachInfoHelper {
    private final int messageServicePort;

    public CachInfoHelper(int messageServicePort) {
        this.messageServicePort = messageServicePort;
    }


    private int getDBservicePort() throws IOException {
        SocketClient socketClient = null;
        try {
            socketClient = new SocketClient("localhost", messageServicePort);
            final String portDbService = socketClient.doRequest(Services.DBservice.name());
            System.out.println("got portDbService:" + portDbService);
            return Integer.parseInt(portDbService);
        }
        finally {
            if (socketClient != null) {
                socketClient.close();
            }
        }
    }

    public CachInfo get(Address adress) throws IOException {
        final int portDbService = getDBservicePort();
        SocketClient socketClientDb = null;
        DbServiceMessageSupport msg = new MsgGetCachInfo();
        msg.setFrom(adress);
        try {
            System.out.println("connect to port:" + portDbService);
            socketClientDb = new SocketClient("localhost", portDbService);
            System.out.println("connect to port:" + portDbService + " DONE");
            final String responseFromDb = socketClientDb.doRequest(new Gson().toJson(msg));
            System.out.println("got replay:" + responseFromDb);
            return new com.google.gson.Gson().fromJson(responseFromDb, CachInfo.class);
        } finally {
            if (socketClientDb != null) {
                socketClientDb.close();
            }
        }
    }
}
