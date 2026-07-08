package org.example;

import javax.swing.*;
import java.awt.*;

public class LeaderboardScreen extends JFrame {
    private AppContext context;

    public LeaderboardScreen(AppContext context) {
        this.context = context;

        setTitle("Leaderboard");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("🏆 Leaderboard");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Leaderboard area
        JTextArea leaderboardArea = UIHelper.createTextArea();
        leaderboardArea.setEditable(false);

        // Load leaderboard content
        StudyGroup[] groups = context.groupManager.getAllGroups();
        for (StudyGroup g : groups) {
            Contributor[] top = context.leaderboardManager.getLeaderboard(g, 5);
            leaderboardArea.append("Group: " + g.getName() + "\n");
            for (Contributor c : top) {
                leaderboardArea.append("  " + c.getUser().getUsername() + " - " + c.getPoints() + " pts\n");
            }
            leaderboardArea.append("\n");
        }

        JScrollPane scrollPane = UIHelper.createScrollPane(leaderboardArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}