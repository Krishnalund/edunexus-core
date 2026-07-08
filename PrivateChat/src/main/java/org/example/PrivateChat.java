package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class PrivateChat {
    private User user1;
    private User user2;
    private Queue<Message> pendingMessages;   // For unsent or pending messages
    private LinkedList<Message> chatHistory;  // For complete chat record

    // Constructor: Initializes Queue and LinkedList
    public PrivateChat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.pendingMessages = new LinkedList<>(); // Queue initialization
        this.chatHistory = new LinkedList<>();     // LinkedList initialization
    }

    // Send a message in this private chat
    public void sendMessage(User sender, String content) throws IllegalSenderException {
        if (!sender.equals(user1) && !sender.equals(user2)) {
            throw new IllegalSenderException("Sender not part of this private chat!");
        }
        Message message = new Message(sender, content);
        pendingMessages.offer(message);  // Queue offer
        chatHistory.add(message);        // LinkedList add
    }

    // Retrieve chat history
    public LinkedList<Message> getHistory() {
        return chatHistory;
    }

    // Optional helper: retrieve pending messages
    public Queue<Message> getPendingMessages() {
        return pendingMessages;
    }

    // Getters for users
    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }
}
class IllegalSenderException extends Exception {
    public IllegalSenderException(String message) {
        super(message);
    }
}
