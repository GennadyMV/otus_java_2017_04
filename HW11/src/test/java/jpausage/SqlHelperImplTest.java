package jpausage;

import jpausage.impl.ConnectionHelper;
import jpausage.impl.SqlDaoImpl;
import model.UserDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/**
 * @author sergey
 *         created on 10.06.17.
 */
public class SqlHelperImplTest {
    private SqlDao sqlDao;

    @Before
    public void initTestTable() throws Exception {
        Connection connection = new ConnectionHelper().getConnection();
        sqlDao = new SqlDaoImpl(connection);
        sqlDao.executeUpdate("delete from tuser", null);
    }

    @After
    public void close() throws SQLException {
        sqlDao.close();
    }

    @Test
    public void queryExecutor() throws Exception {
        final String userName = "userName1";
        final Integer age = 10;

        final String insert = "insert into tuser(name, age) values (?, ?)";

        sqlDao.executeUpdate(insert, ps -> {
            ps.setString(1, userName);
            ps.setInt(2, age);
        });

        final String select = "select id, name, age from tuser";
        UserDataSet userDataSet = sqlDao.queryExecutor(select, null, rs -> {
            UserDataSet result = new UserDataSet();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });

        assertEquals("userDataSet Name", userName, userDataSet.getName());
        assertEquals("userDataSet Age", age, userDataSet.getAge());
    }


    @Test
    public void save() throws Exception {
        final UserDataSet userDataSet = new UserDataSet();
        userDataSet.setName("userName");
        userDataSet.setAge(33);

        sqlDao.save(userDataSet);

        final String select = "select id, name, age from tuser";
        final UserDataSet userDataSetFact = sqlDao.queryExecutor(select, null, rs -> {
            UserDataSet result = new UserDataSet();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });

        assertEquals("userDataSet Name", userDataSet.getName(), userDataSetFact.getName());
        assertEquals("userDataSet Age", userDataSet.getAge(), userDataSetFact.getAge());
    }

    @Test
    public void load() throws Exception {
        final String userName = "userName1";
        final Integer age = 10;
        final String insert = "insert into tuser(name, age) values (?, ?)";

        sqlDao.executeUpdate(insert, ps -> {
            ps.setString(1, userName);
            ps.setInt(2, age);
        });

        final String select = "select id, name, age from tuser";
        final UserDataSet userDataSet = sqlDao.queryExecutor(select, null, rs -> {
            UserDataSet result = new UserDataSet();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });


        final UserDataSet userDataSetFact = new UserDataSet();
        final Map<String, Object> ids = new HashMap<>();
        ids.put("id", userDataSet.getId());
        sqlDao.load(userDataSetFact, ids);

        System.out.println("loaded userDataSet:" + userDataSetFact);
        assertEquals("userDataSet", userDataSet, userDataSetFact);
    }
}