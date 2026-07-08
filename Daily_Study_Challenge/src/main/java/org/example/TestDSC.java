package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestDSC {
    public static void main(String[] args) {
        // --- Step 1: Connect to Access Database ---
        Connection connection = null;
        String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";

        try {
            connection = DriverManager.getConnection(dbURL);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database.");
            e.printStackTrace();
            return;
        }

        try {
            // --- Step 2: Create ChallengeManager ---
            ChallengeManager manager = new ChallengeManager(3, connection);

            // --- Step 3: Add some challenges ---
            manager.addChallenge("Solve 5 math problems", 10);
            manager.addChallenge("Read a chapter of a book", 5);
            manager.addChallenge("Exercise for 30 minutes", 15);

            System.out.println("Challenges added successfully!");

            // --- Step 4: Create users ---
            User user1 = new User("Alice", "hash123", "alice@example.com");
            User user2 = new User("Bob", "hash456", "bob@example.com");

            // --- Step 5: Get daily challenge ---
            Challenge daily = manager.getDailyChallenge();
            if (daily != null) {
                System.out.println("Today's challenge: " + daily.getTaskDescription() +
                        " | Reward Points: " + daily.getRewardPoints());
            }

            // --- Step 6: Complete challenge for users ---
            manager.completeChallenge(user1, daily);
            manager.completeChallenge(user2, daily);

            System.out.println(user1.getUsername() + " and " + user2.getUsername() + " completed the challenge!");

        } catch (SQLException | QueueEmptyException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException ignored) {}
        }
    }
}
