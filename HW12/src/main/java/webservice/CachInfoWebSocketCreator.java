package webservice;

import dbservice.impl.DBServiceHibernate;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * @author sergey
 *         created on 07.07.17.
 */
public class CachInfoWebSocketCreator implements WebSocketCreator {
    private final DBServiceHibernate dbService;

    public CachInfoWebSocketCreator(DBServiceHibernate dbService) {
        this.dbService = dbService;
        System.out.println("WebSocketCreator created");
    }


    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        CachInfoSocket socket = new CachInfoSocket(dbService);
        System.out.println("Socket created");
        return socket;
    }

}
