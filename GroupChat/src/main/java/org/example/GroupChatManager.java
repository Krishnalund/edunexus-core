package org.example;

import org.example.User;
import org.example.StudyGroup;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class GroupChatManager {
    private Queue<Message> messageQueue; // FIFO for pending messages
    private LinkedList<Message> history; // For archived messages
    private Connection dbConnection; // Database connection

    // Constructor
    public GroupChatManager(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.messageQueue = new LinkedList<>(); // Queue init
        this.history = new LinkedList<>();
        loadHistory(); // Loads chat history from DB
    }

    // Send group message
    public void sendGroupMessage(StudyGroup group, User sender, String content) throws SQLException {
        Message message = new Message(sender, content);
        messageQueue.offer(message); // Queue offer (enqueue)
        history.add(message); // Add to history
        saveMessageToDB(group, message); // Save in DB
        notifyGroupMembers(group, message); // Notify members
    }

    // Receive group messages
    public Queue<Message> receiveGroupMessages(StudyGroup group, User user) {
        Queue<Message> userMessages = new LinkedList<>();
        while (!messageQueue.isEmpty()) {
            Message msg = messageQueue.poll(); // Queue poll (dequeue)
            userMessages.offer(msg);
        }
        return userMessages;
    }

    // Clear group chat
    public void clearGroupChat(StudyGroup group) throws SQLException {
        archiveHistory(group); // Archive before clearing
        messageQueue.clear(); // Queue clear
    }

    // ---------------- Helper methods ----------------

    // Loads existing chat history from database
    private void loadHistory() throws SQLException {
        String sql = "SELECT sender_id, content, timestamp FROM group_messages ORDER BY timestamp ASC";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql);
                java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String senderId = rs.getString("sender_id");
                String content = rs.getString("content");
                java.sql.Timestamp timestamp = rs.getTimestamp("timestamp");

                // Create temporary user (in real app, would fetch from UserManager)
                User sender = new User(senderId, "dummy", senderId + "@mail.com");
                Message message = new Message(sender, content);
                history.add(message);
            }
        } catch (SQLException e) {
            // Table might not exist yet, that's okay
            System.out.println("Note: Could not load chat history (table may not exist yet)");
        }
    }

    // Get all messages for a specific group
    public LinkedList<Message> getMessagesForGroup(StudyGroup group) throws SQLException {
        LinkedList<Message> messages = new LinkedList<>();
        String sql = "SELECT sender_id, content, timestamp FROM group_messages WHERE group_id = ? ORDER BY timestamp ASC";

        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, group.getgroupID());
            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String senderId = rs.getString("sender_id");
                String content = rs.getString("content");

                // Create temporary user
                User sender = new User(senderId, "dummy", senderId + "@mail.com");
                Message message = new Message(sender, content);
                messages.add(message);
            }
        } catch (SQLException e) {
            // Table might not exist, return empty list
            System.out.println("Note: Could not load messages for group (table may not exist yet)");
        }

        return messages;
    }

    // Saves message to DB
    private void saveMessageToDB(StudyGroup group, Message message) throws SQLException {
        String sql = "INSERT INTO group_messages (group_id, sender_id, content, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
            stmt.setInt(1, group.getgroupID());
            stmt.setString(2, message.getSender().getUserId());
            stmt.setString(3, message.getContent());
            stmt.setTimestamp(4, new java.sql.Timestamp(message.getTimestamp().getTime()));
            stmt.executeUpdate();
        }
    }

    // Archives chat history (placeholder)
    private void archiveHistory(StudyGroup group) throws SQLException {
        history.clear(); // Simple local archive
    }

    // Notifies group members (placeholder)
    private void notifyGroupMembers(StudyGroup group, Message message) {
        System.out.println("New message in group " + group.getName() + " from " + message.getSender().getUsername());
    }
}
