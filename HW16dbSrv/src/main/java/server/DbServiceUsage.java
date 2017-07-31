package server;

import dbservice.DBService;
import dbservice.DBServiceHibernate;
import dbservice.UserCacheMessageSupport;
import dbservice.jpausage.SqlDao;
import dbservice.jpausage.impl.ConnectionHelper;
import dbservice.jpausage.impl.SqlDaoImpl;

import java.sql.Connection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author sergey
 *         created on 06.07.17.
 */

public class DbServiceUsage {

    private final DBService dbService;

    public DbServiceUsage(UserCacheMessageSupport cache) {
        dbService = new DBServiceHibernate(cache);
        System.out.println("db activity starting...");
        this.start();
        System.out.println("db activity started");
    }

    private void initTestTable() throws Exception {
        Connection connection = new ConnectionHelper().getConnection();
        SqlDaoImpl sqlDao = new SqlDaoImpl(connection);
        sqlDao.executeUpdate("delete from tphone", null);
        sqlDao.executeUpdate("delete from tuser", null);
        sqlDao.executeUpdate("delete from taddress", null);
        sqlDao.close();
    }


    private void createUser(long id) throws Exception {
        final String insertAddr = "insert into taddress(id, street, index) values (?, ?, ?)";
        final SqlDao sqlDao = new SqlDaoImpl(new ConnectionHelper().getConnection());
        final long addrId = id;
        sqlDao.executeUpdate(insertAddr, ps -> {
            ps.setLong(1, addrId);
            ps.setString(2, "test street");
            ps.setInt(3, 444);
        });

        final String userName = "userName1" + System.currentTimeMillis();
        final Integer age = 10;
        final long userId = id;
        final String insert = "insert into tuser(id, name, age, address_id) values (?, ?, ?, ?)";
        sqlDao.executeUpdate(insert, ps -> {
            ps.setLong(1, userId);
            ps.setString(2, userName);
            ps.setInt(3, age);
            ps.setLong(4, addrId);
        });

        final String insertPhones = "insert into tphone(userId, code, number) values (?, ?, ?)";

        sqlDao.executeUpdate(insertPhones, ps -> {
            ps.setLong(1, userId);
            ps.setInt(2, 1);
            ps.setString(3, "1-1-1");
        });

        sqlDao.executeUpdate(insertPhones, ps -> {
            ps.setLong(1, userId);
            ps.setInt(2, 2);
            ps.setString(3, "2-2-2");
        });

        sqlDao.close();
    }

    private void dbAction() throws Exception {
        dbService.startup();
        for(int iteration = 0; iteration < Integer.MAX_VALUE; iteration++) {
            createUser(iteration);
            long userId = ThreadLocalRandom.current().nextInt(0, iteration + 1);
            if (iteration % 2 == 0) {
                userId++;
            }
            dbService.read(userId);
            sleep();
        }
        dbService.shutdown();
    }

    private void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        (new Thread(() -> {
            try {
                initTestTable();
                dbAction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        })).start();
    }

    public DBService getDbService() {
        return dbService;
    }
}
