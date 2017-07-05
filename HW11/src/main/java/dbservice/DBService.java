package dbservice;

import model.UserDataSet;

import java.sql.SQLException;
import java.util.List;

/**
 * @author sergey
 *         created on 10.06.17.
 */
public interface DBService {

    void startup() throws SQLException;

    String getLocalStatus() throws SQLException;

    void save(UserDataSet user) throws Exception;

    UserDataSet read(long id) throws Exception;

    UserDataSet readByName(String name);

    List<UserDataSet> readAll();

    void shutdown() throws SQLException;

}
