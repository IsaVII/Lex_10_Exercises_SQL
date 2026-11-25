package se.lexicon.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    
    private static final String HOST = "localhost";
    private static final int PORT = 5433;
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/";
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private static String databaseName;
    
    public static void setDatabaseName(String databaseName) {
        DatabaseConnection.databaseName = databaseName;
    }

    public static Connection getConnection() throws SQLException {
        
        return DriverManager.getConnection(URL + databaseName, USER, PASSWORD);
    }
    
}
