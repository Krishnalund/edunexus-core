package org.example;
import java.sql.Connection;
import java.sql.DriverManager;

public class TestSG {
    public static void main(String[] args) {
        Connection connection = null;

        // Path to your Access database
        String dbPath = "C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";
        String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb" ;

        try {
            // 1. Connect to database
            connection = DriverManager.getConnection(dbURL);
            System.out.println("Connected to database successfully!");

            // 2. Create GroupManager instance
            GroupManager groupManager = new GroupManager(connection);

            // 3. Display all existing groups
            System.out.println("\nExisting Groups:");
            StudyGroup[] loadedGroups = groupManager.getAllGroups();
            if (loadedGroups.length == 0) {
                System.out.println("No groups found in database.");
            } else {
                for (StudyGroup g : loadedGroups) {
                    g.displayGroupInfo();
                    System.out.println("----------------------");
                }
            }

            // 4. Create a new user (dummy)
            User user1 = new User("0", "alice", "pass123", "alice@mail.com", false, null);

            // 5. Create a new group
            StudyGroup newGroup = groupManager.createGroup("Java Learners", "Group for Java enthusiasts", user1);
            System.out.println("\nCreated New Group:");
            newGroup.displayGroupInfo();

            // 6. Add a member
            User user2 = new User("0", "bob", "pass456", "bob@mail.com", false, null);
            if (groupManager.joinGroup(newGroup.getgroupID(), user2)) {
                System.out.println("\nUser 'bob' joined the group successfully!");
            }

            // 7. Display updated group info
            System.out.println("\nUpdated Group Info:");
            newGroup.displayGroupInfo();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8. Close connection safely
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("\nDatabase connection closed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
