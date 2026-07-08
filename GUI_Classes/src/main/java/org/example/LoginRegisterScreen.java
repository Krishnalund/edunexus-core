package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginRegisterScreen extends JFrame {
    private UserManager userManager;
    private Connection dbConnection;
    
    // Login components
    private JTextField txtLoginUsername;
    private JPasswordField txtLoginPassword;
    
    // Register components
    private JTextField txtRegUsername;
    private JTextField txtRegEmail;
    private JPasswordField txtRegPassword;
    private JPasswordField txtRegConfirmPassword;
    
    private JTabbedPane tabbedPane;

    public LoginRegisterScreen(Connection conn) {
        this.dbConnection = conn;
        userManager = new UserManager(conn);

        setTitle("Study Platform - Login / Register");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        setContentPane(mainPanel);

        // Title
        JLabel title = UIHelper.createTitleLabel("Welcome!");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(title, BorderLayout.NORTH);

        // Tabbed pane for Login/Register
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(UIConstants.HEADER_FONT);
        tabbedPane.addTab("Login", createLoginPanel());
        tabbedPane.addTab("Register", createRegisterPanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = UIHelper.createCardPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(UIHelper.createLabel("Username"), gbc);

        gbc.gridy = 1;
        txtLoginUsername = UIHelper.createTextField();
        txtLoginUsername.setPreferredSize(new Dimension(400, 40));
        panel.add(txtLoginUsername, gbc);

        // Password
        gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Password"), gbc);

        gbc.gridy = 3;
        txtLoginPassword = UIHelper.createPasswordField();
        txtLoginPassword.setPreferredSize(new Dimension(400, 40));
        panel.add(txtLoginPassword, gbc);

        // Status label
        gbc.gridy = 4;
        JLabel lblLoginStatus = new JLabel("");
        lblLoginStatus.setFont(UIConstants.BODY_FONT);
        lblLoginStatus.setForeground(UIConstants.ERROR);
        lblLoginStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblLoginStatus, gbc);

        // Login button
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton btnLogin = UIHelper.createPrimaryButton("Login");
        btnLogin.setPreferredSize(new Dimension(400, UIConstants.BUTTON_HEIGHT_LARGE));
        panel.add(btnLogin, gbc);

        // Login action
        btnLogin.addActionListener(e -> {
            String username = txtLoginUsername.getText().trim();
            String password = new String(txtLoginPassword.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                lblLoginStatus.setText("Please fill in all fields!");
                return;
            }

            User user = userManager.loginUser(username, password);
            if (user != null) {
                AppContext context = new AppContext(user, dbConnection);
                context.initializeManagers();
                DashboardScreen dashboard = new DashboardScreen(context);
                dashboard.setVisible(true);
                dispose();
            } else {
                lblLoginStatus.setText("Invalid credentials!");
            }
        });
        
        txtLoginPassword.addActionListener(e -> btnLogin.doClick());

        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = UIHelper.createCardPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(UIHelper.createLabel("Username"), gbc);

        gbc.gridy = 1;
        txtRegUsername = UIHelper.createTextField();
        txtRegUsername.setPreferredSize(new Dimension(400, 40));
        panel.add(txtRegUsername, gbc);

        // Email
        gbc.gridy = 2;
        panel.add(UIHelper.createLabel("Email"), gbc);

        gbc.gridy = 3;
        txtRegEmail = UIHelper.createTextField();
        txtRegEmail.setPreferredSize(new Dimension(400, 40));
        panel.add(txtRegEmail, gbc);

        // Password
        gbc.gridy = 4;
        panel.add(UIHelper.createLabel("Password (min 6 characters)"), gbc);

        gbc.gridy = 5;
        txtRegPassword = UIHelper.createPasswordField();
        txtRegPassword.setPreferredSize(new Dimension(400, 40));
        panel.add(txtRegPassword, gbc);

        // Confirm Password
        gbc.gridy = 6;
        panel.add(UIHelper.createLabel("Confirm Password"), gbc);

        gbc.gridy = 7;
        txtRegConfirmPassword = UIHelper.createPasswordField();
        txtRegConfirmPassword.setPreferredSize(new Dimension(400, 40));
        panel.add(txtRegConfirmPassword, gbc);

        // Status label
        gbc.gridy = 8;
        JLabel lblRegStatus = new JLabel("");
        lblRegStatus.setFont(UIConstants.BODY_FONT);
        lblRegStatus.setForeground(UIConstants.ERROR);
        lblRegStatus.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblRegStatus, gbc);

        // Register button
        gbc.gridy = 9;
        gbc.insets = new Insets(15, 10, 10, 10);
        JButton btnRegister = UIHelper.createSuccessButton("Register");
        btnRegister.setPreferredSize(new Dimension(400, UIConstants.BUTTON_HEIGHT_LARGE));
        panel.add(btnRegister, gbc);

        // Register action
        btnRegister.addActionListener(e -> handleRegistration(lblRegStatus));
        txtRegConfirmPassword.addActionListener(e -> btnRegister.doClick());

        return panel;
    }
    
    private void handleRegistration(JLabel statusLabel) {
        String username = txtRegUsername.getText().trim();
        String email = txtRegEmail.getText().trim();
        String password = new String(txtRegPassword.getPassword());
        String confirmPassword = new String(txtRegConfirmPassword.getPassword());
        
        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields!");
            return;
        }
        
        if (username.length() < 3 || username.length() > 20) {
            statusLabel.setText("Username must be 3-20 characters!");
            return;
        }
        
        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Please enter a valid email!");
            return;
        }
        
        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters!");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match!");
            return;
        }
        
        // Attempt registration
        try {
            boolean success = userManager.registerUser(username, password, email);
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Registration successful! Please login with your credentials.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear registration fields
                txtRegUsername.setText("");
                txtRegEmail.setText("");
                txtRegPassword.setText("");
                txtRegConfirmPassword.setText("");
                statusLabel.setText("");
                
                // Switch to login tab
                tabbedPane.setSelectedIndex(0);
                txtLoginUsername.setText(username);
                txtLoginUsername.requestFocus();
            } else {
                statusLabel.setText("Username already exists!");
            }
        } catch (SQLException ex) {
            statusLabel.setText("Registration failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
