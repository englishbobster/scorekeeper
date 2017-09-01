package stos.keeper.database;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();

    void closeConnection();
}
