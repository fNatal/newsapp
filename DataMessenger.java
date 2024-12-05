package models;

import java.sql.*;

public class DataMessenger {
    private static final String USER_DB_URL = "jdbc:sqlite:/absolute/path/to/database/user_data.db";


    public DataMessenger() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public ResultSet fetchData(String dbUrl, String query) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            return stmt.executeQuery(query);
        }
    }

    public void saveData(String dbUrl, String query) throws SQLException {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }
}
