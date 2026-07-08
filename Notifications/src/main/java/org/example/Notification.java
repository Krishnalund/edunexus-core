package org.example;


public class Notification {
    private static int idCounter = 1;
    private int notificationId;
    private User recipient;
    private String message;
    private boolean isRead;

    // Constructor
    public Notification(User recipient, String message) {
        this.notificationId = idCounter++;
        this.recipient = recipient;
        this.message = message;
        this.isRead = false; // default: unread
    }

    // Mark notification as read
    public void markAsRead() {
        this.isRead = true;
    }

    // Getters
    public int getNotificationId() {
        return notificationId;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return isRead;
    }
}
