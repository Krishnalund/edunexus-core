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
        //title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setFont(UIConstants.TITLE_FONT);
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
        btnGroups.setIcon(UIHelper.createDotIcon(UIConstants.PRIMARY));
        btnGroups.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnGroups.setIconTextGap(12);
        contentPanel.add(btnGroups, gbc);

        gbc.gridx = 1;
        btnDiscussion = UIHelper.createDashboardButton("Discussion Posts");
        btnDiscussion.setIcon(UIHelper.createDotIcon(UIConstants.PRIMARY));
        btnDiscussion.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDiscussion.setIconTextGap(12);
        contentPanel.add(btnDiscussion, gbc);

        gbc.gridx = 2;
        btnResources = UIHelper.createDashboardButton("Resources");
        btnResources.setIcon(UIHelper.createDotIcon(UIConstants.PRIMARY));
        btnResources.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnResources.setIconTextGap(12);
        contentPanel.add(btnResources, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        btnQuizzes = UIHelper.createDashboardButton("Polls & Quizzes");
        btnQuizzes.setIcon(UIHelper.createDotIcon(UIConstants.ACCENT));
        btnQuizzes.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnQuizzes.setIconTextGap(12);
        contentPanel.add(btnQuizzes, gbc);

        gbc.gridx = 1;
        btnChallenges = UIHelper.createDashboardButton("Daily Challenges");
        btnChallenges.setIcon(UIHelper.createDotIcon(UIConstants.ACCENT));
        btnChallenges.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnChallenges.setIconTextGap(12);
        contentPanel.add(btnChallenges, gbc);

        gbc.gridx = 2;
        btnLeaderboard = UIHelper.createDashboardButton("Leaderboard");
        btnLeaderboard.setIcon(UIHelper.createDotIcon(UIConstants.ACCENT));
        btnLeaderboard.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnLeaderboard.setIconTextGap(12);
        contentPanel.add(btnLeaderboard, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        btnNotifications = UIHelper.createDashboardButton("Notifications");
        btnNotifications.setIcon(UIHelper.createDotIcon(UIConstants.WARNING));
        btnNotifications.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnNotifications.setIconTextGap(12);
        contentPanel.add(btnNotifications, gbc);

        gbc.gridx = 1;
        btnSearch = UIHelper.createDashboardButton("Search");
        btnSearch.setIcon(UIHelper.createDotIcon(UIConstants.WARNING));
        btnSearch.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnSearch.setIconTextGap(12);
        contentPanel.add(btnSearch, gbc);

        gbc.gridx = 2;
        btnRecommendations = UIHelper.createDashboardButton("Recommendations");
        btnRecommendations.setIcon(UIHelper.createDotIcon(UIConstants.WARNING));
        btnRecommendations.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnRecommendations.setIconTextGap(12);
        contentPanel.add(btnRecommendations, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        btnProfile = UIHelper.createDashboardButton("Profile / Settings");
        btnProfile.setIcon(UIHelper.createDotIcon(UIConstants.TEXT_SECONDARY));
        btnProfile.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnProfile.setIconTextGap(12);
        contentPanel.add(btnProfile, gbc);
        gbc.gridwidth = 1;

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        // Logout — separated footer, visually distinct
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UIConstants.BG_PRIMARY);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        btnLogout = UIHelper.createDashboardButton("Logout");
        btnLogout.setBackground(new Color(255, 235, 235));
        btnLogout.setForeground(UIConstants.ERROR);
        footerPanel.add(btnLogout);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
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