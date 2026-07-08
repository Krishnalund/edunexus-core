package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Queue;

public class NotificationsScreen extends JFrame {
    private AppContext context;

    public NotificationsScreen(AppContext context) throws NoSuchUserException {
        this.context = context;

        setTitle("Notifications");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("🔔 Notifications");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Notifications list
        JPanel notifPanel = new JPanel();
        notifPanel.setLayout(new BoxLayout(notifPanel, BoxLayout.Y_AXIS));
        notifPanel.setBackground(UIConstants.BG_PRIMARY);

        Queue<Notification> notifications = context.notificationManager.getNotifications(context.currentUser);
        for (Notification n : notifications) {
            notifPanel.add(createNotificationCard(n));
            notifPanel.add(Box.createVerticalStrut(10));
        }

        JScrollPane scrollPane = UIHelper.createScrollPane(notifPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createNotificationCard(Notification n) {
        JPanel card = UIHelper.createCardPanel();
        card.setLayout(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setBackground(n.isRead() ? UIConstants.BG_CARD : UIConstants.PRIMARY_LIGHT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel message = new JLabel(n.getMessage());
        message.setFont(UIConstants.BODY_LARGE_FONT);
        card.add(message, BorderLayout.CENTER);

        // Make notifications clickable
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                n.markAsRead();
                card.setBackground(UIConstants.BG_CARD);

                // Open DailyChallengeScreen if this is a daily challenge notification
                if (n.getMessage().toLowerCase().contains("daily challenge")) {
                    new DailyChallengeScreen(context).setVisible(true);
                    dispose();
                }
            }
        });

        return card;
    }
}