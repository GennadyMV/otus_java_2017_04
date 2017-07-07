package webservice;

import com.google.gson.Gson;
import dbservice.impl.DBServiceHibernate;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;


/**
 * @author sergey
 *         created on 07.07.17.
 */

@WebSocket
public class CachInfoSocket {
    private final DBServiceHibernate dbService;

    public CachInfoSocket(DBServiceHibernate dbService) {
        this.dbService = dbService;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        System.out.println("gettig message:" + data);
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        sendData(session);
        System.out.println("onOpen");
    }

    private void sendData(Session session) {
        while(true) {
            try {
                final CachInfo cachInfo = new CachInfo(dbService.getHitCount(), dbService.getMissCount());
                final String data = new Gson().toJson(cachInfo);
                session.getRemote().sendString(data);
                System.out.println("Sending message:" + data);
                Thread.sleep(1000);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("onClose");
    }

    class CachInfo {
        private int hitCount;
        private int missCount;

        public CachInfo() {
        }

        public CachInfo(int hitCount, int missCount) {
            this.hitCount = hitCount;
            this.missCount = missCount;
        }

        public int getHitCount() {
            return hitCount;
        }

        public void setHitCount(int hitCount) {
            this.hitCount = hitCount;
        }

        public int getMissCount() {
            return missCount;
        }

        public void setMissCount(int missCount) {
            this.missCount = missCount;
        }
    }
}
