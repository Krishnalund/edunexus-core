package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DailyChallengeScreen extends JFrame {
    private AppContext context;

    public DailyChallengeScreen(AppContext context) {
        this.context = context;

        setTitle("Daily Challenge");
        setSize(700, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background panel
        JPanel background = new JPanel();
        background.setBackground(new Color(245, 245, 250));
        background.setLayout(new BorderLayout(15, 15));
        background.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setContentPane(background);

        // Title
        JLabel title = new JLabel("Daily Coding Challenge", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(47, 79, 79));
        background.add(title, BorderLayout.NORTH);

        // Load challenge
        Challenge c = context.challengeManager.getDailyChallenge();

        // Center panel with challenge details
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 250));

        if (c != null) {
            // Info panel with difficulty, category, and points
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
            infoPanel.setBackground(new Color(245, 245, 250));
            
            JLabel difficultyLabel = new JLabel("Difficulty: " + c.getDifficulty());
            difficultyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            Color diffColor = c.getDifficulty().equals("Easy") ? new Color(34, 139, 34) :
                             c.getDifficulty().equals("Medium") ? new Color(255, 140, 0) :
                             new Color(220, 20, 60);
            difficultyLabel.setForeground(diffColor);
            
            JLabel categoryLabel = new JLabel("Category: " + c.getCategory());
            categoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            categoryLabel.setForeground(new Color(70, 130, 180));
            
            JLabel pointsLabel = new JLabel("Reward: " + c.getRewardPoints() + " points");
            pointsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            pointsLabel.setForeground(new Color(218, 165, 32));
            
            infoPanel.add(difficultyLabel);
            infoPanel.add(new JLabel("|"));
            infoPanel.add(categoryLabel);
            infoPanel.add(new JLabel("|"));
            infoPanel.add(pointsLabel);
            
            centerPanel.add(infoPanel, BorderLayout.NORTH);
            
            // Challenge text area
            JTextArea challengeArea = new JTextArea();
            challengeArea.setText(c.getTaskDescription());
            challengeArea.setEditable(false);
            challengeArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            challengeArea.setLineWrap(true);
            challengeArea.setWrapStyleWord(true);
            challengeArea.setBackground(Color.WHITE);
            challengeArea.setForeground(new Color(47, 79, 79));
            challengeArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JScrollPane scrollPane = new JScrollPane(challengeArea);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            centerPanel.add(scrollPane, BorderLayout.CENTER);
        } else {
            JLabel noChallenge = new JLabel("No challenge available today!", SwingConstants.CENTER);
            noChallenge.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            noChallenge.setForeground(new Color(150, 150, 150));
            centerPanel.add(noChallenge, BorderLayout.CENTER);
        }

        background.add(centerPanel, BorderLayout.CENTER);

        // Complete button
        JButton btnComplete = new JButton("Mark as Complete");
        btnComplete.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnComplete.setBackground(new Color(60, 179, 113));
        btnComplete.setForeground(Color.WHITE);
        btnComplete.setFocusPainted(false);
        btnComplete.setPreferredSize(new Dimension(200, 50));
        btnComplete.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        btnComplete.addActionListener(e -> {
            if (c != null) {
                try {
                    context.challengeManager.completeChallenge(context.currentUser, c);
                    JOptionPane.showMessageDialog(this, 
                        "Challenge completed! You earned " + c.getRewardPoints() + " points!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                        "Error completing challenge: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No challenge to complete!");
            }
        });

        background.add(btnComplete, BorderLayout.SOUTH);

        setVisible(true);
    }
}