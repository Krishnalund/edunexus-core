package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class PrivateChatManager {
    private LinkedList<PrivateChat> activeChats; // List of active private chats
    private Connection dbConnection;

    // Constructor: initializes LinkedList and loads active chats
    public PrivateChatManager(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.activeChats = new LinkedList<>();
        loadActiveChats(); // Load existing chats (helper)
    }

    // Start a new private chat
    public PrivateChat startPrivateChat(User user1, User user2) throws DuplicateChatException {
        // Check if a chat already exists between the two users
        for (PrivateChat chat : activeChats) {
            boolean samePair = (chat.getUser1().equals(user1) && chat.getUser2().equals(user2)) ||
                    (chat.getUser1().equals(user2) && chat.getUser2().equals(user1));

            if (samePair) {
                throw new DuplicateChatException("Chat already exists between these users!");
            }
        }

        PrivateChat newChat = new PrivateChat(user1, user2);
        activeChats.add(newChat); // LinkedList add
        return newChat;
    }

    // Send a message in a private chat
    public void sendPrivateMessage(PrivateChat chat, User sender, String content)
            throws SQLException, IllegalSenderException {
        chat.sendMessage(sender, content); // Uses Queue/LinkedList inside chat
        savePrivateMessageToDB(chat, sender, content);
    }

    // View private chat history
    public LinkedList<Message> viewPrivateChatHistory(PrivateChat chat) {
        return chat.getHistory(); // LinkedList retrieval
    }

    // ---------------- Helper methods ----------------

    // Load active chats from database
    private void loadActiveChats() throws SQLException {
        // In a real implementation, would load chat pairs from database
        // For now, chats are created on-demand via startPrivateChat()
        System.out.println("PrivateChatManager initialized");
    }

    // Get messages for a specific chat between two users
    public LinkedList<Message> getMessagesForChat(User user1, User user2) throws SQLException {
        LinkedList<Message> messages = new LinkedList<>();
        String sql = "SELECT sender_id, receiver_id, content, timestamp FROM private_messages " +
                "WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) " +
                "ORDER BY timestamp ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setString(1, user1.getUserId());
            stmt.setString(2, user2.getUserId());
            stmt.setString(3, user2.getUserId());
            stmt.setString(4, user1.getUserId());

            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String senderId = rs.getString("sender_id");
                String content = rs.getString("content");

                // Determine sender
                User sender = senderId.equals(user1.getUserId()) ? user1 : user2;
                Message message = new Message(sender, content);
                messages.add(message);
            }
        } catch (SQLException e) {
            // Table might not exist, return empty list
            System.out.println("Note: Could not load private messages (table may not exist yet)");
        }

        return messages;
    }

    // Get or create chat between two users
    public PrivateChat getChatBetweenUsers(User user1, User user2) {
        // Check if chat already exists
        for (PrivateChat chat : activeChats) {
            boolean samePair = (chat.getUser1().equals(user1) && chat.getUser2().equals(user2)) ||
                    (chat.getUser1().equals(user2) && chat.getUser2().equals(user1));

            if (samePair) {
                return chat;
            }
        }

        // Create new chat if doesn't exist
        PrivateChat newChat = new PrivateChat(user1, user2);
        activeChats.add(newChat);
        return newChat;
    }

    // Save private message to DB
    private void savePrivateMessageToDB(PrivateChat chat, User sender, String content) throws SQLException {
        String sql = "INSERT INTO private_messages (sender_id, receiver_id, content, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            String receiverId = (chat.getUser1().equals(sender))
                    ? chat.getUser2().getUserId()
                    : chat.getUser1().getUserId();

            stmt.setString(1, sender.getUserId());
            stmt.setString(2, receiverId);
            stmt.setString(3, content);
            stmt.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        }
    }
}

class DuplicateChatException extends Exception {
    public DuplicateChatException(String message) {
        super(message);
    }
}
