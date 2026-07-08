package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestDB {
    public static void main(String[] args) {
        // 1️⃣ Path to your Access database
        String dbPath = "C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb"; // <-- change this
        String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb" ;

        try {
            // 2️⃣ Connect to the database
            Connection connection = DriverManager.getConnection(dbURL);
            System.out.println(" Database connected successfully!");

            // 3️⃣ Create UserManager instance
            UserManager userManager = new UserManager(connection);

            // 4️⃣ Test registration
            boolean registered = userManager.registerUser("sneha", "myPassword456", "snehaa@example.com");
            System.out.println("User registration: " + (registered ? "Success" : "Already exists"));

            // 5️⃣ Test login
            User user = userManager.loginUser("sneha", "myPassword456");
            if (user != null) {
                System.out.println("User logged in successfully: " + user.getUsername());
            } else {
                System.out.println("Login failed");
            }
            // 6️⃣ Test logout
            if (user != null) {
                userManager.logoutUser(user);
                System.out.println("User logged out successfully");
            }
            // 7️⃣ Close connection
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Database connection failed!");
                }
            }
        }

