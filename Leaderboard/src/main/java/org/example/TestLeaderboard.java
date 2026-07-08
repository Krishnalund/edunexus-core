package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestLeaderboard {
    public static void main(String[] args) {

        // 1️⃣ Load UCanAccess driver
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ UCanAccess driver not found! Make sure the JAR is in your classpath.");
            e.printStackTrace();
            return;
        }

        Connection conn = null;
        String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";

        try {
            // 2️⃣ Connect to database
            conn = DriverManager.getConnection(dbURL);
            System.out.println("✅ Database connected successfully!");

            // 3️⃣ Create LeaderboardManager
            LeaderboardManager leaderboard = new LeaderboardManager(conn);

            // 4️⃣ Create users
            User user1 = new User("Alice", "alicePass123", "alice@example.com");
            User user2 = new User("Bob", "bobPass123", "bob@example.com");
            User user3 = new User("Charlie", "charliePass123", "charlie@example.com");

            // 5️⃣ Create study group
            StudyGroup group = new StudyGroup("Math Enthusiasts", "All about algebra and geometry", user1);

            // 6️⃣ Update contributions
            leaderboard.updateContribution(user1, 50);
            leaderboard.updateContribution(user2, 30);
            leaderboard.updateContribution(user3, 70);

            // 7️⃣ Get top contributors
            Contributor[] topContributors = leaderboard.getLeaderboard(group, 3);
            System.out.println("\n🏆 Leaderboard for " + group.getName() + ":");
            for (Contributor c : topContributors) {
                System.out.println(c.getUser().getUsername() + " - " + c.getPoints() + " points");
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("\n🔒 Database connection closed.");
                }
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
