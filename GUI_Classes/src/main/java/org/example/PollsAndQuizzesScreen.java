package org.example;

import javax.swing.*;
import java.awt.*;

public class PollsAndQuizzesScreen extends JFrame {
    private AppContext context;

    public PollsAndQuizzesScreen(AppContext context) {
        this.context = context;
        
        setTitle("Polls & Quizzes");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("Polls & Quizzes");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(UIConstants.BG_PRIMARY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.gridx = 0;

        // Polls button
        gbc.gridy = 0;
        JButton btnPolls = UIHelper.createPrimaryButton("📊 Manage Polls");
        btnPolls.setPreferredSize(new Dimension(300, 80));
        btnPolls.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnPolls.addActionListener(e -> {
            new PollScreen(context).setVisible(true);
        });
        centerPanel.add(btnPolls, gbc);

        // Quizzes button
        gbc.gridy = 1;
        JButton btnQuizzes = UIHelper.createPrimaryButton("📝 Manage Quizzes");
        btnQuizzes.setPreferredSize(new Dimension(300, 80));
        btnQuizzes.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnQuizzes.addActionListener(e -> {
            new QuizScreen(context.pollsAndQuizzesManager, context.currentUser, null).setVisible(true);
        });
        centerPanel.add(btnQuizzes, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Back button
        JButton btnBack = UIHelper.createSecondaryButton("← Back to Dashboard");
        btnBack.addActionListener(e -> dispose());
        mainPanel.add(btnBack, BorderLayout.SOUTH);
    }
}
