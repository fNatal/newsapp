package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;

public class RecommendationEngine {
    private DataMessenger dataMessenger;
    private ModelMessenger modelMessenger;
    private static final String USER_DB_URL = "jdbc:sqlite:database/user_data.db";
    private static final String ARTICLES_DB_URL = "jdbc:sqlite:database/news_articles.db";

    public RecommendationEngine() {
        this.dataMessenger = new DataMessenger();
        this.modelMessenger = new ModelMessenger();
    }

    public void fetchRecommendations(String username) throws Exception {
        String userQuery = "SELECT preferences FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(USER_DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(userQuery)) {
            pstmt.setString(1, username);
            ResultSet userResult = pstmt.executeQuery();

            if (!userResult.next()) {
                System.out.println("No preferences found for user: " + username);
                return;
            }

            String preferences = userResult.getString("preferences");
            System.out.println("Retrieved preferences for user: " + preferences);

            String[] recommendations = modelMessenger.getRecommendations(preferences);
            if (recommendations.length == 0) {
                System.out.println("No recommendations available.");
                return;
            }

            String articleQuery = "SELECT * FROM articles WHERE category IN (%s) COLLATE NOCASE";
            String placeholders = String.join(",", Collections.nCopies(recommendations.length, "?"));
            articleQuery = String.format(articleQuery, placeholders);

            try (PreparedStatement articlePstmt = conn.prepareStatement(articleQuery)) {
                for (int i = 0; i < recommendations.length; i++) {
                    articlePstmt.setString(i + 1, recommendations[i].trim());
                }
                ResultSet articles = articlePstmt.executeQuery();

                boolean articlesFound = false;
                while (articles.next()) {
                    articlesFound = true;
                    System.out.println("ID: " + articles.getInt("id") +
                            ", Article: " + articles.getString("article_name") +
                            ", Author: " + articles.getString("author_name"));
                }

                if (!articlesFound) {
                    System.out.println("No articles found for recommended categories.");
                }
            }
        }
    }


}
