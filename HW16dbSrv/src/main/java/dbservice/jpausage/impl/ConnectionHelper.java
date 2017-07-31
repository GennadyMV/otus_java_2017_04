package dbservice.jpausage.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author sergey
 *         created on 04.06.17.
 */
public class ConnectionHelper {

    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/testdb", "userdb", "userdb");
        connection.setAutoCommit(false);
        return connection;
    }
}
