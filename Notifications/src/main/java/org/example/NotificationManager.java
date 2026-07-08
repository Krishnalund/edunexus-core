package org.example;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
public class NotificationManager {
    private Map<User , Queue<Notification>>userNotifications;
    private Connection dbConnection;

    // Constructor
    public NotificationManager(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.userNotifications = new HashMap<>();
        // In a real system: load user notifications from DB
    }

    // Add new notification for a user
    public void addNotification(User user, String message) throws SQLException {
        Notification notification = new Notification(user, message);

        userNotifications.putIfAbsent(user, new LinkedList<>());
        Queue<Notification> queue = userNotifications.get(user);
        queue.offer(notification);

        // Simulate DB save
        System.out.println("Notification saved to DB for user: " + user.getUsername());
    }

    // Get all notifications for a user
    public Queue<Notification> getNotifications(User user) throws NoSuchUserException {
        Queue<Notification> queue = userNotifications.get(user);
        if (queue == null) {
            throw new NoSuchUserException("User has no notification queue: " + user.getUsername());
        }
        return queue;
    }

    // Clear all notifications for a user
    public void clearNotifications(User user) {
        Queue<Notification> queue = userNotifications.get(user);
        if (queue != null) {
            queue.clear();
            System.out.println("Notifications cleared for user: " + user.getUsername());
        }
    }
}
class NoSuchUserException extends Exception {
    public NoSuchUserException(String message) {
        super(message);
    }
}
