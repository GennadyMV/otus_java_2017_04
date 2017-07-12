package webservice;

import cache.CacheEngine;
import model.UserDataSet;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * @author sergey
 *         created on 07.07.17.
 */
public class CachInfoWebSocketCreator implements WebSocketCreator {
    private final CacheEngine<Long, UserDataSet> userCache;

    public CachInfoWebSocketCreator(CacheEngine<Long, UserDataSet> userCache) {
        this.userCache = userCache;
        System.out.println("WebSocketCreator created");
    }


    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        CachInfoSocket socket = new CachInfoSocket(userCache);
        System.out.println("Socket created");
        return socket;
    }

}
