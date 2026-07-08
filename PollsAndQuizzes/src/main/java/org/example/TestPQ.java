package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class TestPQ {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // -------------------------------
            // 1️⃣ Database connection setup
            // -------------------------------
            String dbPath = "C:/Users/DELL/Documents/DSA/StudyPlatform.accdb";
            String dbURL = "jdbc:ucanaccess://C:/Users/DELL/Documents/DSA/StudyPlatform.accdb";

            connection = DriverManager.getConnection(dbURL);
            System.out.println("✅ Database connected successfully!");

            // -------------------------------
            // 2️⃣ Create sample users and study group
            // -------------------------------
            User alice = new User("Alice", "alice123", "alice@gmail.com");
            User bob = new User("Bob", "bob123", "bob@gmail.com");
            StudyGroup javaGroup = new StudyGroup("Java Learners", "Learn Java together", alice);

            // -------------------------------
            // 3️⃣ Initialize QuizManager
            // -------------------------------
            PollsAndQuizzesManager pollsAndQuizzesManager = new PollsAndQuizzesManager(connection);

            // -------------------------------
            // 4️⃣ Create a sample quiz
            // -------------------------------
            List<QuizQuestion> questions = new ArrayList<>();
            questions.add(new QuizQuestion("What is Java?", new String[] { "Language", "Coffee", "Island" }, 0));
            questions.add(new QuizQuestion("Explain OOP concepts.",
                    new String[] { "Object Oriented", "Procedural", "Functional" }, 0));
            questions.add(new QuizQuestion("What is JVM?",
                    new String[] { "Java Virtual Machine", "Java Visual Machine", "Just Virtual Machine" }, 0));

            Quiz quiz = pollsAndQuizzesManager.createQuiz(javaGroup, "Java Basics Quiz", questions);

            // -------------------------------
            // 5️⃣ Submit responses
            // -------------------------------
            String[] aliceAnswers = { "Programming language", "Encapsulation, Inheritance, Polymorphism, Abstraction",
                    "Java Virtual Machine" };
            String[] bobAnswers = { "Language", "OOP concepts", "JVM" };

            pollsAndQuizzesManager.submitQuizResponse(quiz, alice, aliceAnswers);
            pollsAndQuizzesManager.submitQuizResponse(quiz, bob, bobAnswers);

            // -------------------------------
            // 6️⃣ Retrieve and display quiz results
            // -------------------------------
            LinkedList<Response> results = pollsAndQuizzesManager.getQuizResults(quiz);
            System.out.println("\nQuiz Results for: " + quiz.getTitle());
            for (Response r : results) {
                System.out.println("User: " + r.getUser().getUsername());
                String[] ans = r.getAnswers();
                for (int i = 0; i < ans.length; i++) {
                    System.out.println("Q" + (i + 1) + ": " + ans[i]);
                }
                System.out.println("-------------------");
            }

        } catch (SQLException e) {
            System.err.println("❌ Database connection or SQL error: " + e.getMessage());
        } finally {
            // -------------------------------
            // 7️⃣ Close connection
            // -------------------------------
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("\n🔒 Database connection closed.");
                } catch (SQLException e) {
                    System.err.println("❌ Error closing database: " + e.getMessage());
                }
            }
        }
    }
}
