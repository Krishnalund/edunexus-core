package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class PrivateChatScreen extends JPanel {
    private AppContext context;
    private User recipientUser;
    private JTextArea chatArea;
    private JTextField messageField;

    public PrivateChatScreen(AppContext context, User recipient) {
        this.context = context;
        this.recipientUser = recipient;

        setLayout(new BorderLayout());
        setBackground(UIConstants.BG_PRIMARY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        initHeader();
        initChatArea();
        initMessageBar();
        loadMessages();
    }

    private void initHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIConstants.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel nameLabel = new JLabel(recipientUser.getUsername());
        nameLabel.setFont(UIConstants.SUBTITLE_FONT);
        nameLabel.setForeground(Color.WHITE);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        statusPanel.setOpaque(false);
        
        JPanel statusDot = new JPanel();
        statusDot.setBackground(UIConstants.SUCCESS);
        statusDot.setPreferredSize(new Dimension(12, 12));
        statusDot.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        statusPanel.add(statusDot);
        statusPanel.add(nameLabel);
        header.add(statusPanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);
    }

    private void initChatArea() {
        chatArea = UIHelper.createTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(UIConstants.BODY_FONT);

        JScrollPane scrollPane = UIHelper.createScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initMessageBar() {
        JPanel messagePanel = new JPanel(new BorderLayout(10, 0));
        messagePanel.setBackground(UIConstants.BG_PRIMARY);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        messageField = UIHelper.createTextField();
        messageField.addActionListener(e -> handleSend());

        JButton sendButton = UIHelper.createPrimaryButton("Send");
        sendButton.addActionListener(e -> handleSend());

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        add(messagePanel, BorderLayout.SOUTH);
    }

    private void loadMessages() {
        try {
            LinkedList<Message> messages = context.privateChatManager.getMessagesForChat(
                context.currentUser, recipientUser);
            
            chatArea.setText(""); // Clear existing
            
            if (messages.isEmpty()) {
                chatArea.append("No messages yet. Start the conversation!\n\n");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                for (Message msg : messages) {
                    String sender = msg.getSender().getUsername();
                    String content = msg.getContent();
                    String time = sdf.format(msg.getTimestamp());
                    
                    // Show "YOU" for current user's messages
                    String displayName = msg.getSender().equals(context.currentUser) ? "YOU" : sender;
                    chatArea.append(String.format("[%s] %s: %s\n", time, displayName, content));
                }
            }
            
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        } catch (SQLException e) {
            chatArea.append("Error loading messages: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void handleSend() {
        String content = messageField.getText().trim();
        
        if (content.isEmpty()) {
            return;
        }
        
        try {
            // Get or create chat
            PrivateChat chat = context.privateChatManager.getChatBetweenUsers(
                context.currentUser, recipientUser);
            
            // Send message through manager
            context.privateChatManager.sendPrivateMessage(chat, context.currentUser, content);
            
            // Clear input
            messageField.setText("");
            
            // Reload messages to show new message
            loadMessages();
            
        } catch (SQLException | IllegalSenderException e) {
            JOptionPane.showMessageDialog(this, 
                "Error sending message: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}