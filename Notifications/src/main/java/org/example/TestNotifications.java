package org.example;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;

public class TestNotifications {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // -------------------------------
            // 1️⃣ Database connection setup
            // -------------------------------
            String dbPath = "C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb";
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";

            connection = DriverManager.getConnection(dbURL);
            System.out.println("✅ Database connected successfully!");

            // -------------------------------
            // 2️⃣ Create sample users
            // -------------------------------
            User alice = new User("Alice", "alice123", "alice@gmail.com");
            User bob = new User("Bob", "bob123", "bob@gmail.com");

            // -------------------------------
            // 3️⃣ Initialize NotificationManager
            // -------------------------------
            NotificationManager notificationManager = new NotificationManager(connection);

            // -------------------------------
            // 4️⃣ Add notifications for users
            // -------------------------------
            notificationManager.addNotification(alice, "Welcome Alice! You have a new message.");
            notificationManager.addNotification(bob, "Hi Bob! Check your new group invitation.");
            notificationManager.addNotification(alice, "Reminder: Complete your profile.");

            System.out.println("💬 Notifications added successfully!");

            // -------------------------------
            // 5️⃣ Retrieve and display notifications
            // -------------------------------
            Queue<Notification> aliceNotifications = notificationManager.getNotifications(alice);
            System.out.println("\nNotifications for Alice:");
            for (Notification n : aliceNotifications) {
                System.out.println("[" + (n.isRead() ? "Read" : "Unread") + "] " + n.getMessage());
            }

            Queue<Notification> bobNotifications = notificationManager.getNotifications(bob);
            System.out.println("\nNotifications for Bob:");
            for (Notification n : bobNotifications) {
                System.out.println("[" + (n.isRead() ? "Read" : "Unread") + "] " + n.getMessage());
            }

        } catch (SQLException e) {
            System.err.println("❌ Database connection or SQL error: " + e.getMessage());
        } catch (NoSuchUserException e) {
            System.err.println("❌ " + e.getMessage());
        } finally {
            // -------------------------------
            // 6️⃣ Close connection
            // -------------------------------
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\n🔒 Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("❌ Error closing database: " + e.getMessage());
                }
            }
        }
    }
}
