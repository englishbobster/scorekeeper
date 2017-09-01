package stos.keeper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresDataSourceImpl implements DataSource {

    private static final String DB_URL = "jdbc:postgresql://localhost/scorekeeper";
    private static final String USER_PROP = "user";
    private static final String USER = "postgres";
    private static final String PWD_PROP = "password";
    private static final String PWD = "postgres";
    private static final String SSL_PROP = "ssl";
    private static final String SSL_ENABLED = "false";
    private Connection conn = null;

    @Override
    public Connection getConnection() {
        Properties dbConnectorProperties = new Properties();
        dbConnectorProperties.setProperty(USER_PROP, USER);
        dbConnectorProperties.setProperty(PWD_PROP, PWD);
        dbConnectorProperties.setProperty(SSL_PROP, SSL_ENABLED);

        try {
            return DriverManager.getConnection(DB_URL, dbConnectorProperties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Override
    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
