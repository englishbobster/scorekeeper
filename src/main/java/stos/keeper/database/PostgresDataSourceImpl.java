package stos.keeper.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class PostgresDataSourceImpl implements DataSource {

    public static final String DB_URL = "jdbc:postgresql://localhost/scorekeeper";
    public static final String USER = "postgres";
    public static final String PWD = "";
    public static final String SSL_ENABLED = "false";
    public static final String USER_PROP = "user";
    public static final String PWD_PROP = "password";
    public static final String SSL_PROP = "ssl";
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
    public boolean closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
