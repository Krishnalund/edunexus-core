package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestGC {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // ------------------------------------------------
            // 1️⃣ Database connection setup (MySQL example)
            // ------------------------------------------------
            String dbPath = "C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb"; // <-- change this
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb" ;
            connection = DriverManager.getConnection(dbURL);// Replace with your password
            System.out.println("✅ Database connected successfully!");

            // ------------------------------------------------
            // 2️⃣ Create sample users and study group
            // ------------------------------------------------
            User alice = new User("Alice", "alice123", "alice@gmail.com");
            User bob = new User("Bob", "bob123", "bob@gmail.com");
            StudyGroup javaGroup = new StudyGroup("Java Learners","Learn Java together", alice);

            // ------------------------------------------------
            // 3️⃣ Initialize GroupChatManager
            // ------------------------------------------------
            GroupChatManager chatManager = new GroupChatManager(connection);

            // ------------------------------------------------
            // 4️⃣ Send a group message
            // ------------------------------------------------
            chatManager.sendGroupMessage(javaGroup, alice, "Hey everyone, welcome to the Java study group!");
            chatManager.sendGroupMessage(javaGroup, bob, "Thanks Alice! Excited to learn together.");

            System.out.println("💬 Messages sent and saved to database successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Database connection or SQL error: " + e.getMessage());
        } finally {
            // ------------------------------------------------
            // 5️⃣ Close connection
            // ------------------------------------------------
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("🔒 Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("❌ Error closing database: " + e.getMessage());
                }
            }
        }
    }
}
