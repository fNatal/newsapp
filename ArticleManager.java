package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ArticleManager {
    private DataMessenger dataMessenger;
    private static final String ARTICLES_DB_URL = "jdbc:sqlite:database/news_articles.db";

    public ArticleManager() {
        this.dataMessenger = new DataMessenger();
    }

    public void displayArticles(String category) throws Exception {
        String query = "SELECT * FROM article_name WHERE category = ? COLLATE NOCASE";
        try (Connection conn = DriverManager.getConnection(ARTICLES_DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, category.trim());
            ResultSet resultSet = pstmt.executeQuery();

            boolean articlesFound = false;
            while (resultSet.next()) {
                articlesFound = true;
                System.out.println("ID: " + resultSet.getInt("id") +
                        ", Name: " + resultSet.getString("article_name") +
                        ", Author: " + resultSet.getString("author_name"));
            }

            if (!articlesFound) {
                System.out.println("No articles available in this category.");
            }
        }
    }


    public void rateArticle(int articleId, double rating) throws Exception {
        String updateQuery = "UPDATE article_name SET ratings = " + rating + " WHERE id = " + articleId;
        dataMessenger.saveData(ARTICLES_DB_URL, updateQuery);
    }
}
