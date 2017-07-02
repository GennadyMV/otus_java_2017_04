package jpausage;

import java.sql.PreparedStatement;

/**
 * @author sergey
 *         created on 04.06.17.
 */
public interface ParamsHandler {

    void setParams(PreparedStatement ps) throws Exception;
}
