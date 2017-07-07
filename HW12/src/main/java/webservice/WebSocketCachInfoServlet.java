package webservice;

import dbservice.impl.DBServiceHibernate;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * @author sergey
 *         created on 07.07.17.
 */
public class WebSocketCachInfoServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private final DBServiceHibernate dbService;

    public WebSocketCachInfoServlet(DBServiceHibernate dbService) {
        this.dbService = dbService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new CachInfoWebSocketCreator(dbService));
    }

}
