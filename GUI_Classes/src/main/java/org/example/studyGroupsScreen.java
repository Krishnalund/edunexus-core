package org.example;

import javax.swing.*;
import java.awt.*;

public class studyGroupsScreen extends JFrame {
    private AppContext context;
    private JPanel groupListPanel;

    public studyGroupsScreen(AppContext context) {
        this.context = context;

        setTitle("Your Study Groups");
        setSize(UIConstants.WINDOW_SIZE_LARGE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("Your Study Groups");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Group list panel
        groupListPanel = new JPanel();
        groupListPanel.setLayout(new BoxLayout(groupListPanel, BoxLayout.Y_AXIS));
        groupListPanel.setBackground(UIConstants.BG_PRIMARY);

        JScrollPane scrollPane = UIHelper.createScrollPane(groupListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Load groups
        loadGroups();

        setVisible(true);
    }

    private void loadGroups() {
        groupListPanel.removeAll();

        StudyGroup[] groups = context.groupManager.getAllGroups();
        if (groups.length == 0) {
            JLabel emptyLabel = new JLabel("No study groups available.", SwingConstants.CENTER);
            emptyLabel.setFont(UIConstants.BODY_LARGE_FONT);
            emptyLabel.setForeground(UIConstants.TEXT_SECONDARY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            groupListPanel.add(Box.createVerticalGlue());
            groupListPanel.add(emptyLabel);
            groupListPanel.add(Box.createVerticalGlue());
        } else {
            for (StudyGroup g : groups) {
                JButton btn = UIHelper.createDashboardButton(
                    g.getName() + " (" + g.getMembers().size() + " members)"
                );
                btn.setPreferredSize(new Dimension(800, 70));
                btn.setMaximumSize(new Dimension(800, 70));
                btn.setAlignmentX(Component.CENTER_ALIGNMENT);
                btn.addActionListener(e -> openGroupChat(g));
                
                groupListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                groupListPanel.add(btn);
            }
        }

        groupListPanel.revalidate();
        groupListPanel.repaint();
    }

    private void openGroupChat(StudyGroup group) {
        JFrame chatFrame = new JFrame("Group Chat: " + group.getName());
        chatFrame.setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatFrame.setLocationRelativeTo(null);

        GroupChatScreen chatScreen = new GroupChatScreen(context, group);
        chatFrame.add(chatScreen);

        chatFrame.setVisible(true);
    }
}