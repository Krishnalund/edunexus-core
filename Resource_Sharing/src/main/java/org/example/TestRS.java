package org.example;
import java.sql.*;
import java.util.LinkedList;

public class TestRS {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 1️⃣ Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // 2️⃣ Connect to Access database
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb";
            conn = DriverManager.getConnection(dbURL);
            System.out.println("✅ Connected to Access database!");

            // 3️⃣ Create dummy users
            User alice = new User("Alice", "hash1234", "alice@mail.com");
            User bob = new User("Bob", "hash5678", "bob@mail.com");
            alice.setUserId("1");
            bob.setUserId("2");

            PreparedStatement pstUser = conn.prepareStatement(
                    "INSERT INTO users (UserID, Username, PasswordHash, Email, IsLoggedIn) VALUES (?, ?, ?, ?, ?)"
            );
            pstUser.setString(1, alice.getUserId());
            pstUser.setString(2, alice.getUsername());
            pstUser.setString(3, alice.getPasswordHash());
            pstUser.setString(4, alice.getEmail());
            pstUser.setBoolean(5, alice.isLoggedIn());
            pstUser.executeUpdate();

            pstUser.setString(1, bob.getUserId());
            pstUser.setString(2, bob.getUsername());
            pstUser.setString(3, bob.getPasswordHash());
            pstUser.setString(4, bob.getEmail());
            pstUser.setBoolean(5, bob.isLoggedIn());
            pstUser.executeUpdate();

            System.out.println("✅ Dummy users inserted!");

            // 4️⃣ Create dummy study group
            StudyGroup group1 = new StudyGroup("Java Learners", "Learn Java together", alice);
            group1.setGroupID(1);

            PreparedStatement pstGroup = conn.prepareStatement(
                    "INSERT INTO Groups (GroupID, GroupName, Description, AdminID, MaxCapacity) VALUES (?, ?, ?, ?, ?)"
            );
            pstGroup.setInt(1, group1.getgroupID());
            pstGroup.setString(2, group1.getName());
            pstGroup.setString(3, group1.getDescription());
            pstGroup.setString(4, group1.getAdmin().getUserId());
            pstGroup.setInt(5, 50);
            pstGroup.executeUpdate();

            System.out.println("✅ Dummy study group inserted!");

            // 5️⃣ Initialize ResourceManager
            ResourceManager rm = new ResourceManager(conn);

            // 6️⃣ Upload dummy resources
            Resource res1 = rm.uploadResource(group1,alice, "Java Basics","C:/Files/JavaBasics.pdf", "Java Basics Tutorial");
            Resource res2 = rm.uploadResource(group1,bob,"Java OOP","C:/Files/JavaOOP.pdf", "Java OOP Concepts");

            System.out.println("✅ Dummy resources uploaded!");

            // 7️⃣ View resources for group
            LinkedList<Resource> resources = rm.viewResources(group1);
            System.out.println("\nResources for group: " + group1.getName());
            for (Resource r : resources) {
                System.out.println("➡ " + r.getFilePath() + " by " + r.getUploader().getUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                System.out.println("\n✅ Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}