package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class GroupChatScreen extends JFrame {
    private AppContext context;
    private StudyGroup group;
    private JTextArea chatArea;
    private JTextField messageField;

    public GroupChatScreen(AppContext context, StudyGroup group) {
        this.context = context;
        this.group = group;

        setTitle("Group Chat - " + group.getName());
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblGroupName = new JLabel(group.getName());
        lblGroupName.setFont(UIConstants.SUBTITLE_FONT);
        lblGroupName.setForeground(Color.WHITE);

        JLabel lblMemberCount = new JLabel(group.getMembers().size() + " members");
        lblMemberCount.setFont(UIConstants.BODY_FONT);
        lblMemberCount.setForeground(Color.WHITE);

        headerPanel.add(lblGroupName, BorderLayout.WEST);
        headerPanel.add(lblMemberCount, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Chat area
        chatArea = UIHelper.createTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(UIConstants.BODY_FONT);

        JScrollPane scrollPane = UIHelper.createScrollPane(chatArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Message input panel
        JPanel messagePanel = new JPanel(new BorderLayout(10, 0));
        messagePanel.setBackground(UIConstants.BG_PRIMARY);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        messageField = UIHelper.createTextField();
        messageField.addActionListener(e -> sendMessage());

        JButton sendButton = UIHelper.createPrimaryButton("Send");
        sendButton.addActionListener(e -> sendMessage());

        messagePanel.add(messageField, BorderLayout.CENTER);
        messagePanel.add(sendButton, BorderLayout.EAST);

        mainPanel.add(messagePanel, BorderLayout.SOUTH);

        // Load existing messages
        loadMessages();

        setVisible(true);
    }

    private void loadMessages() {
        try {
            LinkedList<Message> messages = context.groupChatManager.getMessagesForGroup(group);
            chatArea.setText(""); // Clear existing
            
            if (messages.isEmpty()) {
                chatArea.append("No messages yet. Start the conversation!\n\n");
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                for (Message msg : messages) {
                    String sender = msg.getSender().getUsername();
                    String content = msg.getContent();
                    String time = sdf.format(msg.getTimestamp());
                    chatArea.append(String.format("[%s] %s: %s\n", time, sender, content));
                }
            }
            
            // Scroll to bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        } catch (SQLException e) {
            chatArea.append("Error loading messages: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String content = messageField.getText().trim();
        
        if (content.isEmpty()) {
            return;
        }
        
        try {
            // Send message through manager
            context.groupChatManager.sendGroupMessage(group, context.currentUser, content);
            
            // Clear input
            messageField.setText("");
            
            // Reload messages to show new message
            loadMessages();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error sending message: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
