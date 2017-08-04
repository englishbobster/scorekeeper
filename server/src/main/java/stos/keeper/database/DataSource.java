package stos.keeper.database;

import java.sql.Connection;

public interface DataSource {

    Connection getConnection();

    boolean closeConnection();
}
