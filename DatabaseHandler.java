package models;

import java.sql.*;

public class DatabaseHandler {
    private static final String USER_DB_URL = "jdbc:sqlite:database/user_data.db";
    private static final String ARTICLES_DB_URL = "jdbc:sqlite:database/news_articles.db";

    public void initializeUserDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(USER_DB_URL);
             Statement stmt = conn.createStatement()) {
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "password TEXT NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "age INTEGER NOT NULL, " +
                    "gender TEXT NOT NULL, " +
                    "preferences TEXT, " +
                    "view_history TEXT DEFAULT '', " +
                    "ratings TEXT DEFAULT '', " +
                    "search_history TEXT DEFAULT '')";
            stmt.execute(createUsersTable);
        }
    }

    public void initializeArticlesDB() throws SQLException {
        try (Connection conn = DriverManager.getConnection(ARTICLES_DB_URL);
             Statement stmt = conn.createStatement()) {
            String createArticlesTable = "CREATE TABLE IF NOT EXISTS articles (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "article_name TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "views INTEGER DEFAULT 0, " +
                    "ratings REAL DEFAULT 0.0, " +
                    "author_name TEXT, " +
                    "news_paper_name TEXT NOT NULL)";
            stmt.execute(createArticlesTable);
        }
    }
}
