package models;

public class BehaviorTracker {
    private DataMessenger dataMessenger;
    private static final String USER_DB_URL = "jdbc:sqlite:database/user_data.db";

    public BehaviorTracker() {
        this.dataMessenger = new DataMessenger();
    }

    public void logSearch(String username, String searchQuery) throws Exception {
        String query = "UPDATE users SET search_history = search_history || ', " + searchQuery + "' WHERE username = '" + username + "'";
        dataMessenger.saveData(USER_DB_URL, query);
    }

    public void logRating(String username, String article, double rating) throws Exception {
        String query = "UPDATE users SET ratings = ratings || ', " + rating + "' WHERE username = '" + username + "'";
        dataMessenger.saveData(USER_DB_URL, query);
    }
}
