package org.example;

import javax.swing.*;
import java.awt.*;

public class SearchScreen extends JFrame {
    private AppContext context;
    private JTextField txtSearch;
    private JTextArea resultArea;

    public SearchScreen(AppContext context) {
        this.context = context;

        setTitle("Search");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("🔍 Search");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBackground(UIConstants.BG_PRIMARY);

        txtSearch = UIHelper.createTextField();
        txtSearch.setPreferredSize(new Dimension(300, 40));

        JButton btnSearch = UIHelper.createPrimaryButton("Search");
        btnSearch.setPreferredSize(new Dimension(100, 40));
        btnSearch.addActionListener(e -> performSearch());

        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Results area
        resultArea = UIHelper.createTextArea();
        resultArea.setEditable(false);

        JScrollPane scrollPane = UIHelper.createScrollPane(resultArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void performSearch() {
        String query = txtSearch.getText().trim();
        resultArea.setText("");

        if (query.isEmpty()) {
            resultArea.append("Please enter a search term.");
            return;
        }

        // Search users
        User[] users = context.userManager.searchUsers(query);

        if (users.length == 0) {
            resultArea.append("No users found.");
        } else {
            for (User u : users) {
                resultArea.append("User: " + u.getUsername() + "\n");
            }
        }
    }
}
