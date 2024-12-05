package models;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import java.util.*;

public class MLModel {
    private RealMatrix userPreferencesMatrix;
    private Map<String, Integer> categoryIndex;
    private List<String> categories;

    public MLModel() {

        // Define the categories
        this.categories = Arrays.asList("Technology", "Health", "Travel", "Science", "Education",
                "Entertainment", "Fashion", "Movies", "Crime", "Books");
        this.categoryIndex = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryIndex.put(categories.get(i), i);
        }

        // Initialize the user preferences matrix (10 users x 10 categories)
        this.userPreferencesMatrix = new Array2DRowRealMatrix(10, categories.size());
        initializeMatrix();
    }

    /**
     * Initializes the user preferences matrix with some dummy values.
     */
    private void initializeMatrix() {
        // Simulate some initial preference scores (rows: users, columns: categories)
        userPreferencesMatrix.setRow(0, new double[]{5, 3, 0, 0, 2, 4, 0, 0, 0, 1}); // User 0
        userPreferencesMatrix.setRow(1, new double[]{4, 0, 0, 3, 5, 1, 0, 0, 2, 0}); // User 1
        userPreferencesMatrix.setRow(2, new double[]{1, 0, 5, 0, 0, 0, 4, 3, 0, 0}); // User 2
        // Add more rows as needed
    }


    /**
     * Predicts the top categories for a user based on their preferences.
     *
     * @param preferences Comma-separated string of user preferences
     * @return Array of top recommended categories
     */
    public String[] predict(String preferences) {
        String[] preferenceArray = preferences.split(",");
        RealMatrix userVector = new Array2DRowRealMatrix(1, categories.size());

        // Populate user vector based on the input preferences
        for (String pref : preferenceArray) {
            pref = pref.trim();
            if (categoryIndex.containsKey(pref)) {
                int index = categoryIndex.get(pref);
                userVector.setEntry(0, index, 1.0); // Set a preference weight
            }
        }

        // Calculate similarity scores
        double[] similarityScores = calculateSimilarity(userVector);

        // Sort categories by similarity scores and return the top ones
        Map<String, Double> categoryScores = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            categoryScores.put(categories.get(i), similarityScores[i]);
        }
        return categoryScores.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(3) // Top 3 recommendations
                .map(Map.Entry::getKey)
                .toArray(String[]::new);
    }

    /**
     * Learns from user behavior and updates the model.
     *
     * @param behavior Comma-separated string of categories the user interacted with
     */
    public void learn(String behavior) {
        String[] behaviorArray = behavior.split(",");
        for (String action : behaviorArray) {
            action = action.trim();
            if (categoryIndex.containsKey(action)) {
                int index = categoryIndex.get(action);
                // Update preferences (for simplicity, increment the score)
                userPreferencesMatrix.addToEntry(0, index, 1.0);
            }
        }
        System.out.println("Model updated with behavior: " + behavior);
    }

    /**
     * Calculates similarity scores between the user vector and the user preferences matrix.
     *
     * @param userVector A 1xN vector representing the user's current preferences
     * @return An array of similarity scores for each category
     */
    private double[] calculateSimilarity(RealMatrix userVector) {
        RealMatrix scores = userPreferencesMatrix.multiply(userVector.transpose());
        return scores.getColumn(0); // Extract the similarity scores for all categories
    }
}
