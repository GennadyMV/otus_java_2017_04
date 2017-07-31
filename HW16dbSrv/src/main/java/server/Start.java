package server;

import dbservice.UserCacheImpl;
import dbservice.UserCacheMessageSupport;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author sergey
 * created on 30.07.17.
 */
public class Start {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        System.out.println("Database service is starting..., port:" + port);
        new Start().activateService(port);
    }

    private void activateService(int port) throws Exception {
        UserCacheMessageSupport cache = new UserCacheImpl();
        new DbServiceUsage(cache);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Server");
        DbServer server = new DbServer(port, cache);
        mbs.registerMBean(server, name);
        server.start();
    }
}


