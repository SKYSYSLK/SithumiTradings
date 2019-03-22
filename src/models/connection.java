package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {
    public static Connection getConnection()
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:src/resources/db/main.db");
        }
        catch(SQLException e) {
            System.err.println(e.getMessage());
        }
        return connection;

    }
}
