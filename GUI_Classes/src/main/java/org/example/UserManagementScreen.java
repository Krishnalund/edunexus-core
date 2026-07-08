package org.example;

import javax.swing.*;
import java.awt.*;

public class UserManagementScreen extends JFrame {
    private AppContext context;

    public UserManagementScreen(AppContext context) {
        this.context = context;

        setTitle("User Management");
        setSize(UIConstants.WINDOW_SIZE_SMALL);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("👤 Profile");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Profile info panel
        JPanel infoPanel = UIHelper.createCardPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel lblUsername = new JLabel("Username: " + context.currentUser.getUsername());
        lblUsername.setFont(UIConstants.HEADER_FONT);
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEmail = new JLabel("Email: " + context.currentUser.getEmail());
        lblEmail.setFont(UIConstants.BODY_LARGE_FONT);
        lblEmail.setForeground(UIConstants.TEXT_SECONDARY);
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(lblUsername);
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(lblEmail);
        infoPanel.add(Box.createVerticalStrut(20));

        JButton btnChangePassword = UIHelper.createPrimaryButton("Change Password");
        btnChangePassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnChangePassword.addActionListener(e -> changePassword());
        infoPanel.add(btnChangePassword);

        infoPanel.add(Box.createVerticalStrut(10));

        JButton btnLogout = UIHelper.createSecondaryButton("Logout");
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setForeground(UIConstants.ERROR);
        btnLogout.addActionListener(e -> {
            context.userManager.logoutUser(context.currentUser);
            new LoginRegisterScreen(context.dbConnection).setVisible(true);
            dispose();
        });
        infoPanel.add(btnLogout);

        mainPanel.add(infoPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void changePassword() {
        JPasswordField pf = UIHelper.createPasswordField();
        int option = JOptionPane.showConfirmDialog(this, pf, "Enter new password", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            context.currentUser.setPassword(new String(pf.getPassword()));
            JOptionPane.showMessageDialog(this, "Password changed!");
        }
    }
}