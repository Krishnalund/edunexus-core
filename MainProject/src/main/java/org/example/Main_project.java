package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class Main_project {

    public static void main(String[] args) {

        // -------------------------------
        // 1️⃣ Load UCanAccess Driver
        // -------------------------------
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("UCanAccess driver not found. Ensure the JAR is in classpath.");
            e.printStackTrace();
            return;
        }

        // -------------------------------
        // 2️⃣ Database Connection Setup
        // -------------------------------
        String dbURL = "jdbc:ucanaccess://C:/Users/DELL/Documents/DSA/StudyPlatform.accdb";
        try (Connection conn = DriverManager.getConnection(dbURL)) {
            System.out.println("✅ Connected to Access database successfully!");

            // -------------------------------
            // 3️⃣ Initialize Managers
            // -------------------------------
            UserManager userManager = new UserManager(conn);
            GroupManager groupManager = new GroupManager(conn);
            DiscussionManager discussionManager = new DiscussionManager(conn);
            TaggingManager taggingManager = new TaggingManager(conn);
            CommentManager commentManager = new CommentManager(conn);
            PollsAndQuizzesManager pollsAndQuizzesManager = new PollsAndQuizzesManager(conn);
            PrivateChatManager privateChatManager = new PrivateChatManager(conn);
            GroupChatManager groupChatManager = new GroupChatManager(conn);
            NotificationManager notificationManager = new NotificationManager(conn);
            ResourceManager resourceManager = new ResourceManager(conn);
            LeaderboardManager leaderboardManager = new LeaderboardManager(conn);
            RecommendationEngine recommendationEngine = new RecommendationEngine(conn);
            ChallengeManager challengeManager = new ChallengeManager(3, conn);
            UndoRedoManager undoRedoManager = new UndoRedoManager();

            // -------------------------------
            // 4️⃣ Create Users
            // -------------------------------
            User alice = new User("Alice", "alice123", "alice@mail.com");
            User bob = new User("Bob", "bob123", "bob@mail.com");
            User charlie = new User("Charlie", "charlie123", "charlie@mail.com");

            // -------------------------------
            // 5️⃣ Create Study Groups
            // -------------------------------
            StudyGroup javaGroup = groupManager.createGroup("C sharp learners", "Group for Java enthusiasts", alice);
            StudyGroup sqlGroup = groupManager.createGroup("SQL Masters", "Learn SQL effectively", bob);

            // Add members to groups
            groupManager.joinGroup(javaGroup.getgroupID(), bob);
            groupManager.joinGroup(sqlGroup.getgroupID(), charlie);

            // -------------------------------
            // 6️⃣ Add Posts, Comments & Tags
            // -------------------------------
            Post post1 = discussionManager.addPost(javaGroup, alice, "Welcome to the Java group!");
            taggingManager.addTagToPost(post1, "Java");
            taggingManager.addTagToPost(post1, "Learning");

            Comment comment1 = commentManager.addComment(post1, bob, "Excited to learn Java!");
            commentManager.addComment(post1, charlie, "Thanks for creating this group!", comment1);

            // -------------------------------
            // 7️⃣ Send Private & Group Messages
            // -------------------------------
            PrivateChat privateChat = privateChatManager.startPrivateChat(alice, bob);
            privateChatManager.sendPrivateMessage(privateChat, alice, "Hi Bob, ready to study?");
            privateChatManager.sendPrivateMessage(privateChat, bob, "Yes, Alice! Let's start.");

            groupChatManager.sendGroupMessage(javaGroup, alice, "Reminder: Daily challenge is up!");
            groupChatManager.sendGroupMessage(javaGroup, bob, "Got it, thanks!");

            // -------------------------------
            // 8️⃣ Upload Resources
            // -------------------------------
            Resource resource1 = resourceManager.uploadResource(javaGroup, alice, "Java Basics",
                    "C:/Files/JavaBasics.pdf", "Introduction to Java");
            Resource resource2 = resourceManager.uploadResource(javaGroup, bob, "OOP Concepts", "C:/Files/JavaOOP.pdf",
                    "Java OOP Tutorial");

            // -------------------------------
            // 9️⃣ Daily Challenges
            // -------------------------------
            challengeManager.addChallenge("Solve 5 coding problems", 10);
            Challenge dailyChallenge = challengeManager.getDailyChallenge();
            challengeManager.completeChallenge(alice, dailyChallenge);
            challengeManager.completeChallenge(bob, dailyChallenge);

            // -------------------------------
            // 🔟 Quiz Management
            // -------------------------------
            // -------------------------------
            // 🔟 Quiz Management
            // -------------------------------
            List<QuizQuestion> questions = new ArrayList<>();
            questions.add(new QuizQuestion("What is Java?", new String[] { "Language", "Coffee", "Island" }, 0));
            questions.add(new QuizQuestion("Explain OOP concepts.",
                    new String[] { "Object Oriented", "Procedural", "Functional" }, 0));
            questions.add(new QuizQuestion("What is JVM?",
                    new String[] { "Java Virtual Machine", "Java Visual Machine", "Just Virtual Machine" }, 0));

            Quiz javaQuiz = pollsAndQuizzesManager.createQuiz(javaGroup, "Java Basics Quiz", questions);
            String[] aliceAnswers = { "Programming language", "Encapsulation, Inheritance, Polymorphism, Abstraction",
                    "Java Virtual Machine" };
            String[] bobAnswers = { "Language", "OOP concepts", "JVM" };
            pollsAndQuizzesManager.submitQuizResponse(javaQuiz, alice, aliceAnswers);
            pollsAndQuizzesManager.submitQuizResponse(javaQuiz, bob, bobAnswers);

            // -------------------------------
            // 1️⃣1️⃣ Leaderboard & Contributions
            // -------------------------------
            leaderboardManager.updateContribution(alice, 50);
            leaderboardManager.updateContribution(bob, 30);
            leaderboardManager.updateContribution(charlie, 70);

            Contributor[] topContributors = leaderboardManager.getLeaderboard(javaGroup, 3);
            System.out.println("\n🏆 Leaderboard for " + javaGroup.getName() + ":");
            for (Contributor c : topContributors) {
                System.out.println(c.getUser().getUsername() + " - " + c.getPoints() + " points");
            }

            // -------------------------------
            // 1️⃣2️⃣ Recommendations
            // -------------------------------
            recommendationEngine.userInterests.put(alice, new InterestGraph(0, "Java"));
            recommendationEngine.userInterests.put(bob, new InterestGraph(0, "SQL"));

            System.out.println("\nRecommended buddies for Alice:");
            LinkedList<User> buddies = recommendationEngine.recommendBuddies(alice);
            if (buddies.isEmpty())
                System.out.println("No buddies found.");
            else
                buddies.forEach(u -> System.out.println("➡ " + u.getUsername()));

            System.out.println("\nRecommended study groups for Alice:");
            LinkedList<StudyGroup> groups = recommendationEngine.recommendGroups(alice);
            if (groups.isEmpty())
                System.out.println("No groups found.");
            else
                groups.forEach(g -> System.out.println("➡ " + g.getName()));

            // -------------------------------
            // 1️⃣3️⃣ Notifications
            // -------------------------------
            notificationManager.addNotification(alice, "Complete your profile.");
            notificationManager.addNotification(bob, "Join a new study group.");

            Queue<Notification> aliceNotifications = notificationManager.getNotifications(alice);
            System.out.println("\nNotifications for Alice:");
            for (Notification n : aliceNotifications) {
                System.out.println("[" + (n.isRead() ? "Read" : "Unread") + "] " + n.getMessage());
            }

            // -------------------------------
            // 1️⃣4️⃣ Undo/Redo Simulation
            // -------------------------------
            undoRedoManager.performAction(new Action("addPost", "Post1"));
            undoRedoManager.undoLastAction();
            undoRedoManager.redoAction();

            System.out.println("\n✅ All modules executed successfully!");

        } catch (SQLException e) {
            System.err.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}