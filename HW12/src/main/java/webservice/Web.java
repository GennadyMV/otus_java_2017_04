package webservice;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * @author sergey
 *         created on 06.07.17.
 */
public class Web {
    private final static int PORT = 8091;
    private final static String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        DbServiceUsage dbServiceUsage = new DbServiceUsage();
        dbServiceUsage.start();

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(LoginServlet.class, "/login");
        context.addServlet(new ServletHolder(new AdminServlet(dbServiceUsage.getDbService().getUserCache())), "/admin");
        context.addServlet(new ServletHolder(new WebSocketCachInfoServlet(dbServiceUsage.getDbService().getUserCache())), "/getCachInfo");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();
    }
}
