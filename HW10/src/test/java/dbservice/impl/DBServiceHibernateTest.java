package dbservice.impl;

import dbservice.DBService;
import jpausage.SqlDao;
import jpausage.impl.ConnectionHelper;
import jpausage.impl.SqlDaoImpl;
import model.AddressDataSet;
import model.UserDataSet;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author sergey
 *         created on 10.06.17.
 */
public class DBServiceHibernateTest {

    private void createUser() throws Exception {
        final String insertAddr = "insert into taddress(id, street, index) values (?, ?, ?)";
        final SqlDao sqlDao = new SqlDaoImpl(new ConnectionHelper().getConnection());
        final long addrId = 1;
        sqlDao.executeUpdate(insertAddr, ps -> {
            ps.setLong(1, addrId);
            ps.setString(2, "test street");
            ps.setInt(3, 444);
        });

        final String userName = "userName1" + System.currentTimeMillis();
        final Integer age = 10;
        final long userId = 1;
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

    private UserDataSet getUser() throws Exception {
        final SqlDao sqlDao = new SqlDaoImpl(new ConnectionHelper().getConnection());
        UserDataSet user = sqlDao.queryExecutor("select id, name, age from tuser", null, rs -> {
            UserDataSet result = new UserDataSet();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });

        AddressDataSet addressDataSet = sqlDao.queryExecutor("select id, street, index from taddress", null, rs -> {
            AddressDataSet result = new AddressDataSet();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setStreet(rs.getString("street"));
                result.setIndex(rs.getInt("index"));
            }
            return result;
        });
        user.setAddressDataSet(addressDataSet);
        sqlDao.close();
        return user;
    }

    @Before
    public void initTestTable() throws Exception {
        Connection connection = new ConnectionHelper().getConnection();
        SqlDaoImpl sqlDao = new SqlDaoImpl(connection);
        sqlDao.executeUpdate("delete from tuser", null);
        sqlDao.executeUpdate("delete from taddress", null);
        sqlDao.close();
    }

    @Test
    public void getLocalStatus() throws Exception {
        DBService dbService = new DBServiceHibernate();
        dbService.startup();
        String status = dbService.getLocalStatus();
        dbService.shutdown();

        System.out.println("status:" + status);
        assertEquals("userDataSet Age", "ACTIVE", status);

    }

    @Test
    public void save() throws Exception {
        final UserDataSet userDataSet = new UserDataSet();
        userDataSet.setName("userName");
        userDataSet.setAge(33);

        final AddressDataSet addressDataSet = new AddressDataSet();
        addressDataSet.setIndex(44);
        addressDataSet.setStreet("new Street");
        userDataSet.setAddressDataSet(addressDataSet);

        DBService dbService = new DBServiceHibernate();
        dbService.startup();
        dbService.save(userDataSet);
        dbService.shutdown();

        final UserDataSet userDataSetFact = getUser();

        assertEquals("userDataSet Name", userDataSet.getName(), userDataSetFact.getName());
        assertEquals("userDataSet Age", userDataSet.getAge(), userDataSetFact.getAge());

        assertEquals("addressDataSet Index", userDataSet.getAddressDataSet().getIndex(), userDataSetFact.getAddressDataSet().getIndex());
        assertEquals("addressDataSet Street", userDataSet.getAddressDataSet().getStreet(), userDataSetFact.getAddressDataSet().getStreet());
    }

    @Test
    public void read() throws Exception {
        createUser();
        UserDataSet user = getUser();

        DBService dbService = new DBServiceHibernate();
        dbService.startup();
        UserDataSet userFact = dbService.read(user.getId());
        dbService.shutdown();
        ///
        System.out.println("loaded userFact:" + userFact);
        assertEquals("userDataSet", user, userFact);
    }

    @Test
    public void readByName() throws Exception {
        createUser();
        UserDataSet user = getUser();

        DBService dbService = new DBServiceHibernate();
        dbService.startup();
        UserDataSet userFact = dbService.readByName(user.getName());
        dbService.shutdown();

        ///
        System.out.println("loaded userFact:" + userFact);
        assertEquals("userDataSet", user, userFact);
    }

    @Test
    public void readAll() throws Exception {
        final int size = 5;
        for (int idx = 0; idx < size; idx++) {
            createUser();
        }

        DBService dbService = new DBServiceHibernate();
        dbService.startup();
        List<UserDataSet> userList = dbService.readAll();
        dbService.shutdown();

        System.out.println("loaded userFact:" + userList);
        assertEquals("userList size", size, userList.size());
    }



}