package jpausage;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author sergey
 *         created on 04.06.17.
 */
public interface ResultHandler<T> {

    T handle(ResultSet rs) throws SQLException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
