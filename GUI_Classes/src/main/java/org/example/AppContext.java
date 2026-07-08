package org.example;

import java.sql.Connection;

public class AppContext {
    public User currentUser;
    public Connection dbConnection;

    public UserManager userManager;
    public GroupManager groupManager;
    public DiscussionManager discussionManager;
    public TaggingManager taggingManager;
    public CommentManager commentManager;
    public PollsAndQuizzesManager pollsAndQuizzesManager;
    public PrivateChatManager privateChatManager;
    public GroupChatManager groupChatManager;
    public NotificationManager notificationManager;
    public ResourceManager resourceManager;
    public LeaderboardManager leaderboardManager;
    public RecommendationEngine recommendationEngine;
    public ChallengeManager challengeManager;
    public UndoRedoManager undoRedoManager;

    public AppContext(User user, Connection conn) {
        this.currentUser = user;
        this.dbConnection = conn;
    }

    public AppContext() {

    }

    public void initializeManagers() {
        try {
            this.userManager = new UserManager(dbConnection);
            this.groupManager = new GroupManager(dbConnection);
            this.discussionManager = new DiscussionManager(dbConnection);
            this.taggingManager = new TaggingManager(dbConnection);
            this.commentManager = new CommentManager(dbConnection);
            this.pollsAndQuizzesManager = new PollsAndQuizzesManager(dbConnection);
            this.privateChatManager = new PrivateChatManager(dbConnection);
            this.groupChatManager = new GroupChatManager(dbConnection);
            this.notificationManager = new NotificationManager(dbConnection);
            this.resourceManager = new ResourceManager(dbConnection);
            this.leaderboardManager = new LeaderboardManager(dbConnection);
            this.recommendationEngine = new RecommendationEngine(dbConnection);
            this.challengeManager = new ChallengeManager(3, dbConnection);
            this.undoRedoManager = new UndoRedoManager();
            
            // Initialize sample data
            initializeSampleData();
        } catch (Exception e) {
            System.err.println("Error initializing managers: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initializeSampleData() {
        try {
            // Initialize challenge pool
            challengeManager.initializeChallengePool();
            
            // Add sample polls
            if (pollsAndQuizzesManager.getAllPolls().isEmpty()) {
                java.util.List<String> options1 = new java.util.ArrayList<>();
                options1.add("Java");
                options1.add("Python");
                options1.add("C++");
                options1.add("JavaScript");
                pollsAndQuizzesManager.createPoll("What's your favorite programming language?", options1);

                java.util.List<String> options2 = new java.util.ArrayList<>();
                options2.add("Morning (6-10 AM)");
                options2.add("Afternoon (12-4 PM)");
                options2.add("Evening (6-10 PM)");
                options2.add("Night (10 PM-2 AM)");
                pollsAndQuizzesManager.createPoll("When do you prefer to study?", options2);

                java.util.List<String> options3 = new java.util.ArrayList<>();
                options3.add("Videos");
                options3.add("Books");
                options3.add("Interactive Tutorials");
                options3.add("Practice Problems");
                pollsAndQuizzesManager.createPoll("What's your preferred learning method?", options3);
            }
            
            // Add sample quizzes
            if (pollsAndQuizzesManager.getAllQuizzes().isEmpty()) {
                // Sample Quiz 1: Java Basics
                java.util.List<QuizQuestion> javaQuestions = new java.util.ArrayList<>();
                javaQuestions.add(new QuizQuestion(
                    "What is Java?",
                    new String[]{"A programming language", "A type of coffee", "An island"},
                    0
                ));
                javaQuestions.add(new QuizQuestion(
                    "What does JVM stand for?",
                    new String[]{"Java Virtual Machine", "Java Visual Machine", "Just Virtual Machine"},
                    0
                ));
                javaQuestions.add(new QuizQuestion(
                    "Which keyword is used for inheritance in Java?",
                    new String[]{"extends", "implements", "inherits"},
                    0
                ));
                pollsAndQuizzesManager.createQuiz(
                    new StudyGroup("Java Group", "Java learners", currentUser),
                    "Java Basics Quiz",
                    javaQuestions
                );

                // Sample Quiz 2: Data Structures
                java.util.List<QuizQuestion> dsQuestions = new java.util.ArrayList<>();
                dsQuestions.add(new QuizQuestion(
                    "What is the time complexity of binary search?",
                    new String[]{"O(log n)", "O(n)", "O(n²)"},
                    0
                ));
                dsQuestions.add(new QuizQuestion(
                    "Which data structure uses LIFO?",
                    new String[]{"Stack", "Queue", "Array"},
                    0
                ));
                dsQuestions.add(new QuizQuestion(
                    "What is a complete binary tree?",
                    new String[]{"All levels filled except possibly last", "All nodes have 2 children", "Tree with only left children"},
                    0
                ));
                pollsAndQuizzesManager.createQuiz(
                    new StudyGroup("DSA Group", "Data Structures learners", currentUser),
                    "Data Structures Quiz",
                    dsQuestions
                );
            }
        } catch (Exception e) {
            System.err.println("Error initializing sample data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

