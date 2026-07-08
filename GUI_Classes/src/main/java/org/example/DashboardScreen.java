package org.example;

import javax.swing.*;
import java.awt.*;

public class DashboardScreen extends JFrame {
    private AppContext context;
    private JButton btnGroups, btnDiscussion, btnResources, btnQuizzes, btnChallenges, btnLeaderboard, 
                    btnNotifications, btnSearch, btnRecommendations, btnProfile, btnLogout;

    public DashboardScreen(AppContext context) {
        this.context = context;
        
        setTitle("Dashboard - " + context.currentUser.getUsername());
        setSize(UIConstants.WINDOW_SIZE_DASHBOARD);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        setContentPane(mainPanel);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PRIMARY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Study Platform Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + context.currentUser.getUsername() + "!");
        welcomeLabel.setFont(UIConstants.SUBTITLE_FONT);
        welcomeLabel.setForeground(Color.WHITE);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with grid
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(UIConstants.BG_PRIMARY);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        btnGroups = UIHelper.createDashboardButton("Study Groups");
        contentPanel.add(btnGroups, gbc);

        gbc.gridx = 1;
        btnDiscussion = UIHelper.createDashboardButton("Discussion Posts");
        contentPanel.add(btnDiscussion, gbc);

        gbc.gridx = 2;
        btnResources = UIHelper.createDashboardButton("Resources");
        contentPanel.add(btnResources, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        btnQuizzes = UIHelper.createDashboardButton("Polls & Quizzes");
        contentPanel.add(btnQuizzes, gbc);

        gbc.gridx = 1;
        btnChallenges = UIHelper.createDashboardButton("Daily Challenges");
        contentPanel.add(btnChallenges, gbc);

        gbc.gridx = 2;
        btnLeaderboard = UIHelper.createDashboardButton("Leaderboard");
        contentPanel.add(btnLeaderboard, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        btnNotifications = UIHelper.createDashboardButton("Notifications");
        contentPanel.add(btnNotifications, gbc);

        gbc.gridx = 1;
        btnSearch = UIHelper.createDashboardButton("Search");
        contentPanel.add(btnSearch, gbc);

        gbc.gridx = 2;
        btnRecommendations = UIHelper.createDashboardButton("Recommendations");
        contentPanel.add(btnRecommendations, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3;
        btnProfile = UIHelper.createDashboardButton("Profile / Settings");
        contentPanel.add(btnProfile, gbc);

        gbc.gridx = 1;
        btnLogout = UIHelper.createDashboardButton("Logout");
        btnLogout.setForeground(UIConstants.ERROR);
        contentPanel.add(btnLogout, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // ALL ACTION LISTENERS - FUNCTIONALITY PRESERVED
        btnGroups.addActionListener(e -> {
            new studyGroupsScreen(context).setVisible(true);
        });

        btnDiscussion.addActionListener(e -> {
            new DiscussionPostsScreen(context).setVisible(true);
        });

        btnResources.addActionListener(e -> {
            new ResourceSharingScreen(context).setVisible(true);
        });

        btnQuizzes.addActionListener(e -> {
            new PollsAndQuizzesScreen(context).setVisible(true);
        });

        btnChallenges.addActionListener(e -> {
            new DailyChallengeScreen(context).setVisible(true);
        });

        btnLeaderboard.addActionListener(e -> {
            new LeaderboardScreen(context).setVisible(true);
        });

        btnNotifications.addActionListener(e -> {
            try {
                new NotificationsScreen(context).setVisible(true);
            } catch (NoSuchUserException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "User not found!");
            }
        });

        btnSearch.addActionListener(e -> {
            new SearchScreen(context).setVisible(true);
        });

        btnRecommendations.addActionListener(e -> {
            new RecommendationsScreen(context).setVisible(true);
        });

        btnProfile.addActionListener(e -> {
            new UserManagementScreen(context).setVisible(true);
        });

        btnLogout.addActionListener(e -> {
            context.userManager.logoutUser(context.currentUser);
            new LoginRegisterScreen(context.dbConnection).setVisible(true);
            dispose();
        });
    }
}