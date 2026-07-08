package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class RecommendationsScreen extends JFrame {
    private AppContext context;
    private JPanel recListPanel;

    public RecommendationsScreen(AppContext context) {
        this.context = context;

        setTitle("Recommendations");
        setSize(UIConstants.WINDOW_SIZE_LARGE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("💡 Recommendations");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Recommendation list panel
        recListPanel = new JPanel();
        recListPanel.setLayout(new BoxLayout(recListPanel, BoxLayout.Y_AXIS));
        recListPanel.setBackground(UIConstants.BG_PRIMARY);

        JScrollPane scrollPane = UIHelper.createScrollPane(recListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadRecommendations();
        setVisible(true);
    }

    private void loadRecommendations() {
        recListPanel.removeAll();

        LinkedList<User> buddies = context.recommendationEngine.recommendBuddies(context.currentUser);
        if (!buddies.isEmpty()) {
            recListPanel.add(createSectionLabel("Recommended Buddies:"));
            for (User u : buddies) recListPanel.add(createCard(u.getUsername()));
        }

        LinkedList<StudyGroup> groups = context.recommendationEngine.recommendGroups(context.currentUser);
        if (!groups.isEmpty()) {
            recListPanel.add(Box.createVerticalStrut(20));
            recListPanel.add(createSectionLabel("Recommended Study Groups:"));
            for (StudyGroup g : groups) recListPanel.add(createCard(g.getName()));
        }

        recListPanel.revalidate();
        recListPanel.repaint();
    }

    private JLabel createSectionLabel(String text) {
        JLabel lbl = UIHelper.createSubtitleLabel(text);
        lbl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return lbl;
    }

    private JPanel createCard(String text) {
        JPanel card = UIHelper.createCardPanel();
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        JLabel lbl = new JLabel(text);
        lbl.setFont(UIConstants.BODY_LARGE_FONT);
        card.add(lbl);
        return card;
    }
}