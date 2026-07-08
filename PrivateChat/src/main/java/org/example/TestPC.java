package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class TestPC {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // -------------------------------
            // 1️⃣ Database connection setup
            // -------------------------------
            String dbPath = "C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb";
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb" ;

            connection = DriverManager.getConnection(dbURL);
            System.out.println("✅ Database connected successfully!");

            // -------------------------------
            // 2️⃣ Create sample users
            // -------------------------------
            User alice = new User("Alice", "alice123", "alice@gmail.com");
            User bob = new User("Bob", "bob123", "bob@gmail.com");

            // Assign IDs manually for DB simulation
            alice.setUserId("1");
            bob.setUserId("2");

            // -------------------------------
            // 3️⃣ Initialize PrivateChatManager
            // -------------------------------
            PrivateChatManager chatManager = new PrivateChatManager(connection);

            // -------------------------------
            // 4️⃣ Start a private chat between users
            // -------------------------------
            PrivateChat chat = chatManager.startPrivateChat(alice, bob);
            System.out.println("Private chat started between " + alice.getUsername() + " and " + bob.getUsername());

            // -------------------------------
            // 5️⃣ Send messages
            // -------------------------------
            chatManager.sendPrivateMessage(chat, alice, "Hi Bob! How are you?");
            chatManager.sendPrivateMessage(chat, bob, "Hey Alice! I'm good, thanks. How about you?");
            chatManager.sendPrivateMessage(chat, alice, "I'm doing great, ready to study!");

            System.out.println("💬 Messages sent successfully!");

            // -------------------------------
            // 6️⃣ View chat history
            // -------------------------------
            LinkedList<Message> history = chatManager.viewPrivateChatHistory(chat);
            System.out.println("\nPrivate Chat History:");
            for (Message msg : history) {
                System.out.println(msg.getSender().getUsername() + ": " + msg.getContent() + " [" + msg.getTimestamp() + "]");
            }

            // -------------------------------
            // 7️⃣ View pending messages (optional)
            // -------------------------------
            Queue<Message> pending = chat.getPendingMessages();
            System.out.println("\nPending Messages Queue Size: " + pending.size());

        } catch (SQLException e) {
            System.err.println("❌ Database connection or SQL error: " + e.getMessage());
        } catch (DuplicateChatException | IllegalSenderException e) {
            System.err.println("❌ " + e.getMessage());
        } finally {
            // -------------------------------
            // 8️⃣ Close connection
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
