package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        // Step 1: Connect to database
        Connection connection = null;
        try {
            String url = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/Desktop/StudyPlatform.accdb";
            connection = DriverManager.getConnection(url);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed!");
            System.exit(1);
        }

        // Step 2: Launch LoginRegisterScreen
        Connection finalConnection = connection;
        SwingUtilities.invokeLater(() -> {
            LoginRegisterScreen loginScreen = new LoginRegisterScreen(finalConnection);
            loginScreen.setVisible(true);
        });
    }
}
