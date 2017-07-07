package dbservice.impl;

import dbservice.DBService;
import jpausage.impl.ConnectionHelper;
import jpausage.impl.SqlDaoImpl;
import model.UserDataSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sergey
 *         created on 10.06.17.
 */
public class DBServiceJdbc implements DBService {

    private SqlDaoImpl sqlHelper;

    @Override
    public void startup() throws SQLException {
        Connection connection = new ConnectionHelper().getConnection();
        sqlHelper = new SqlDaoImpl(connection);
    }

    @Override
    public String getLocalStatus() throws SQLException {
        return sqlHelper.getStatus();
    }

    @Override
    public void save(UserDataSet user) throws Exception {
        sqlHelper.save(user);
    }

    @Override
    public UserDataSet read(long id) throws Exception {
        UserDataSet user = new UserDataSet();
        final Map<String, Object> ids = new HashMap<>();
        ids.put("id", id);
        sqlHelper.load(user, ids);
        return user;
    }

    @Override
    public void shutdown() throws SQLException {
        sqlHelper.close();
    }

    @Override
    public UserDataSet readByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException();
    }
}
