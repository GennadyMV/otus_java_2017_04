package dbservice.jpausage;

import java.sql.SQLException;
import java.util.Map;

/**
 * @author sergey
 *         created on 06.06.17.
 */
public interface SqlDao {

    String getStatus() throws SQLException;

    <T> T queryExecutor(String sql, ParamsHandler params, ResultHandler<T> handler) throws Exception;

    void executeUpdate(String sql, ParamsHandler params) throws Exception;

    void close() throws SQLException;

    void save(Object object) throws Exception;

    void load(Object object, Map<String, Object> idValues) throws Exception;

}
