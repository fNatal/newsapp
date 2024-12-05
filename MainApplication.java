package main;

import models.*;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        // Initialize components
        RecommendationEngine recommendationEngine = new RecommendationEngine();
        ArticleManager articleManager = new ArticleManager();
        BehaviorTracker behaviorTracker = new BehaviorTracker();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Article Recommendation System!");

        // Step 1: Login or Create Account
        String username = handleLoginOrCreateAccount(scanner);

        // Step 2: Main Application Loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // View Recommended Articles
                    try {
                        System.out.println("\n--- Recommended Articles ---");
                        recommendationEngine.fetchRecommendations(username);
                    } catch (Exception e) {
                        System.out.println("Error fetching recommendations: " + e.getMessage());
                    }
                    break;

                case 2: // Search Articles
                    System.out.print("\nEnter a category to search for articles: ");
                    String category = scanner.nextLine();
                    try {
                        System.out.println("\n--- Search Results ---");
                        articleManager.displayArticles(category);
                        behaviorTracker.logSearch(username, category);
                    } catch (Exception e) {
                        System.out.println("Error searching articles: " + e.getMessage());
                    }
                    break;

                case 3: // Change Preferences
                    System.out.print("\nEnter your new preferences (comma-separated categories): ");
                    String newPreferences = scanner.nextLine();
                    try {
                        updateUserPreferences(username, newPreferences);
                        System.out.println("Preferences updated successfully!");
                    } catch (Exception e) {
                        System.out.println("Error updating preferences: " + e.getMessage());
                    }
                    break;

                case 4: // Rate an Article
                    System.out.print("\nEnter the article ID you want to rate: ");
                    int articleId = scanner.nextInt();
                    System.out.print("Enter your rating (1-5): ");
                    double rating = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    try {
                        articleManager.rateArticle(articleId, rating);
                        behaviorTracker.logRating(username, String.valueOf(articleId), rating);
                        System.out.println("Thank you for your feedback!");
                    } catch (Exception e) {
                        System.out.println("Error rating the article: " + e.getMessage());
                    }
                    break;

                case 5: // Exit
                    running = false;
                    System.out.println("Thank you for using the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    /**
     * Handles user login or account creation.
     */
    private static String handleLoginOrCreateAccount(Scanner scanner) {
        System.out.println("Do you have an account? (yes/no)");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("yes")) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            if (validateUser(username, password)) {
                System.out.println("Login successful!");
                return username;
            } else {
                System.out.println("Invalid credentials. Exiting.");
                System.exit(1);
            }
        }

        // Create a new account
        return createNewAccount(scanner);
    }

    /**
     * Validates user credentials by checking the database.
     */
    private static boolean validateUser(String username, String password) {
        DataMessenger dataMessenger = new DataMessenger();
        String query = "SELECT * FROM users WHERE username = '" + username + "' AND password = '" + password + "'";
        try {
            return dataMessenger.fetchData("jdbc:sqlite:database/user_data.db", query).next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new user account and saves their information in the database.
     */
    private static String createNewAccount(Scanner scanner) {
        MLModel model = new MLModel(); // Initialize MLModel for category display

        System.out.println("Creating a new account...");

        // Input username
        String username;
        while (true) {
            try {
                System.out.print("Enter your username: ");
                username = scanner.nextLine();
                if (username.isEmpty()) {
                    throw new IllegalArgumentException("Username cannot be empty.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Input password
        String password;
        while (true) {
            try {
                System.out.print("Enter a password: ");
                password = scanner.nextLine();
                if (password.isEmpty()) {
                    throw new IllegalArgumentException("Password cannot be empty.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Input name
        String name;
        while (true) {
            try {
                System.out.print("Enter your name: ");
                name = scanner.nextLine();
                if (name.isEmpty()) {
                    throw new IllegalArgumentException("Name cannot be empty.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Input age
        int age;
        while (true) {
            try {
                System.out.print("Enter your age: ");
                age = scanner.nextInt();
                if (age <= 0) {
                    throw new IllegalArgumentException("Age must be a positive number.");
                }
                scanner.nextLine(); // Consume newline
                break;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number for age.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        // Input gender
        String gender;
        while (true) {
            try {
                System.out.print("Enter your gender: ");
                gender = scanner.nextLine();
                if (gender.isEmpty()) {
                    throw new IllegalArgumentException("Gender cannot be empty.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Display available categories and input preferences
        String preferences;
        while (true) {
            try {
                System.out.println("\nAvailable Categories for Preferences:");
                System.out.println("1. Technology");
                System.out.println("2. Health");
                System.out.println("3. Travel");
                System.out.println("4. Science");
                System.out.println("5. Education");
                System.out.println("6. Entertainment");
                System.out.println("7. Fashion");
                System.out.println("8. Movies");
                System.out.println("9. Crime");
                System.out.println("10. Books");

                System.out.print("Enter your preferences (comma-separated from the above list): ");
                preferences = scanner.nextLine();
                if (preferences.isEmpty()) {
                    throw new IllegalArgumentException("Preferences cannot be empty.");
                }
                break;
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
            }
        }

        // Save the new user to the database
        DataMessenger dataMessenger = new DataMessenger();
        String query = String.format("INSERT INTO users (username, password, name, age, gender, preferences) VALUES " +
                "('%s', '%s', '%s', %d, '%s', '%s')", username, password, name, age, gender, preferences);
        try {
            dataMessenger.saveData("jdbc:sqlite:database/user_data.db", query);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error saving new account.");
        }

        return username;
    }

    /**
     * Updates user preferences in the database.
     */
    private static void updateUserPreferences(String username, String newPreferences) {
        DataMessenger dataMessenger = new DataMessenger();
        String query = "UPDATE users SET preferences = '" + newPreferences + "' WHERE username = '" + username + "'";
        try {
            dataMessenger.saveData("jdbc:sqlite:database/user_data.db", query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main menu options.
     */
    private static void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. View Recommended Articles");
        System.out.println("2. Search Articles");
        System.out.println("3. Change Preferences");
        System.out.println("4. Rate an Article");
        System.out.println("5. Exit");
    }
}

