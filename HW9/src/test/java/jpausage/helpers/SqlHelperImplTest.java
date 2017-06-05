package jpausage.helpers;

import jpausage.User;
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
 *         created on 04.06.17.
 */
public class SqlHelperImplTest {

    private SqlHelper sqlHelper;

    @Before
    public void initTestTable() throws Exception {
        Connection connection = new ConnectionHelper().getConnection();
        sqlHelper = new SqlHelperImpl(connection);
        sqlHelper.executeUpdate("delete from tuser", null);
    }

    @After
    public void close() throws SQLException {
        sqlHelper.close();
    }

    @Test
    public void queryExecutor() throws Exception {
        final String userName = "userName1";
        final Integer age = 10;

        final String insert = "insert into tuser(name, age) values (?, ?)";

        sqlHelper.executeUpdate(insert, ps -> {
            ps.setString(1, userName);
            ps.setInt(2, age);
        });

        final String select = "select id, name, age from tuser";
        User user = sqlHelper.queryExecutor(select, null, rs -> {
            User result = new User();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });

        assertEquals("user Name", userName, user.getName());
        assertEquals("user Age", age, user.getAge());
    }


    @Test
    public void save() throws Exception {
        final User user = new User();
        user.setName("userName");
        user.setAge(33);

        sqlHelper.save(user);

        final String select = "select id, name, age from tuser";
        final User userFact = sqlHelper.queryExecutor(select, null, rs -> {
            User result = new User();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });

        assertEquals("user Name", user.getName(), userFact.getName());
        assertEquals("user Age", user.getAge(), userFact.getAge());
    }

    @Test
    public void load() throws Exception {
        final String userName = "userName1";
        final Integer age = 10;
        final String insert = "insert into tuser(name, age) values (?, ?)";

        sqlHelper.executeUpdate(insert, ps -> {
            ps.setString(1, userName);
            ps.setInt(2, age);
        });

        final String select = "select id, name, age from tuser";
        final User user = sqlHelper.queryExecutor(select, null, rs -> {
            User result = new User();
            while (rs.next()) {
                result.setId(rs.getLong("id"));
                result.setName(rs.getString("name"));
                result.setAge(rs.getInt("age"));
            }
            return result;
        });


        final User userFact = new User();
        final Map<String, Object> ids = new HashMap<>();
        ids.put("id", user.getId());
        sqlHelper.load(userFact, ids);

        System.out.println("loaded user:" + userFact);
        assertEquals("user", user, userFact);
    }
}