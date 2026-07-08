package org.example;

import java.sql.*;
import java.util.LinkedList;

public class TestRecommendations {

    public static void main(String[] args) {
        Connection conn = null;

        try {
            // 1️⃣ Load UCanAccess driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // 2️⃣ Connect to Access database
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";
            conn = DriverManager.getConnection(dbURL);
            System.out.println("✅ Connected to Access database!");

            RecommendationEngine engine = new RecommendationEngine(conn);

            // 3️⃣ Insert dummy users (let Access auto-generate UserID)
            User alice = new User("Alice", "hash1234", "alice@mail.com");
            User bob = new User("Bob", "hash5678", "bob@mail.com");

            PreparedStatement pstUser = conn.prepareStatement(
                    "INSERT INTO [users] (Username, PasswordHash, Email, IsLoggedIn) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            // Alice
            pstUser.setString(1, alice.getUsername());
            pstUser.setString(2, alice.getPasswordHash());
            pstUser.setString(3, alice.getEmail());
            pstUser.setBoolean(4, alice.isLoggedIn());
            pstUser.executeUpdate();
            ResultSet rs = pstUser.getGeneratedKeys();
            if (rs.next()) alice.setUserId(rs.getString("1"));

            // Bob
            pstUser.setString(1, bob.getUsername());
            pstUser.setString(2, bob.getPasswordHash());
            pstUser.setString(3, bob.getEmail());
            pstUser.setBoolean(4, bob.isLoggedIn());
            pstUser.executeUpdate();
            rs = pstUser.getGeneratedKeys();
            if (rs.next()) bob.setUserId(rs.getString("1"));

            System.out.println("✅ Dummy users inserted!");

            // 4️⃣ Insert dummy interests (Access auto-generates InterestID)
            InterestGraph java = new InterestGraph(0, "Java");
            InterestGraph sql = new InterestGraph(0, "SQL");

            PreparedStatement pstInterest = conn.prepareStatement(
                    "INSERT INTO [InterestGraph] (InterestName) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            pstInterest.setString(1, java.getInterestName());
            pstInterest.executeUpdate();
            rs = pstInterest.getGeneratedKeys();
            if (rs.next()) java.setInterestID(rs.getInt(1));

            pstInterest.setString(1, sql.getInterestName());
            pstInterest.executeUpdate();
            rs = pstInterest.getGeneratedKeys();
            if (rs.next()) sql.setInterestID(rs.getInt(1));

            System.out.println("✅ Dummy interests inserted!");

            // 5️⃣ Link users to interests
            PreparedStatement pstUserInterest = conn.prepareStatement(
                    "INSERT INTO [UserInterests] (UserID, InterestID) VALUES (?, ?)"
            );

            pstUserInterest.setString(1, alice.getUserId());
            pstUserInterest.setInt(2, java.getInterestID());
            pstUserInterest.executeUpdate();

            pstUserInterest.setString(1, bob.getUserId());
            pstUserInterest.setInt(2, sql.getInterestID());
            pstUserInterest.executeUpdate();

            System.out.println("✅ Users linked to interests!");

            // 6️⃣ Insert dummy study groups (Access auto-generates GroupID)
            StudyGroup group1 = new StudyGroup("Java Learners", "Learn Java together", alice);
            StudyGroup group2 = new StudyGroup("SQL Masters", "Master SQL concepts", bob);

            PreparedStatement pstGroup = conn.prepareStatement(
                    "INSERT INTO [Groups] (GroupName, Description, AdminID, MaxCapacity) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            // Group 1
            pstGroup.setString(1, group1.getName());
            pstGroup.setString(2, group1.getDescription());
            pstGroup.setString(3, group1.getAdmin().getUserId());
            pstGroup.setInt(4, 50);
            pstGroup.executeUpdate();
            rs = pstGroup.getGeneratedKeys();
            if (rs.next()) group1.setGroupID(rs.getInt(1));

            // Group 2
            pstGroup.setString(1, group2.getName());
            pstGroup.setString(2, group2.getDescription());
            pstGroup.setString(3, group2.getAdmin().getUserId());
            pstGroup.setInt(4, 50);
            pstGroup.executeUpdate();
            rs = pstGroup.getGeneratedKeys();
            if (rs.next()) group2.setGroupID(rs.getInt(1));

            System.out.println("✅ StudyGroups inserted!");

            // 7️⃣ Add users and interests to engine memory
            engine.userInterests.put(alice, java);
            engine.userInterests.put(bob, sql);

            // 8️⃣ Test recommendations
            System.out.println("\nRecommended buddies for Alice:");
            LinkedList<User> buddies = engine.recommendBuddies(alice);
            if (buddies.isEmpty()) System.out.println("No buddies found.");
            else buddies.forEach(u -> System.out.println("➡ " + u.getUsername()));

            System.out.println("\nRecommended study groups for Alice:");
            LinkedList<StudyGroup> groups = engine.recommendGroups(alice);
            if (groups.isEmpty()) System.out.println("No groups found.");
            else groups.forEach(g -> System.out.println("➡ " + g.getName()));

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