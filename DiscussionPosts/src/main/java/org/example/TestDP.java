package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class TestDP {
    public static void main(String[] args) {
        String dbPath = "C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";
        String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/documents/StudyPlatform.accdb";
        try (Connection connection = DriverManager.getConnection(dbURL)) {
            System.out.println("✅ Connected to database successfully!");

            // Managers
            DiscussionManager discussionManager = new DiscussionManager(connection);
            TaggingManager taggingManager = new TaggingManager(connection);
            CommentManager commentManager = new CommentManager(connection);
            // Dummy User & Group
            User author = new User("alice", "pass123", "alice@mail.com");
            StudyGroup group = new StudyGroup(1, "Java Learners", "Group for Java Enthusiasts", author);

            // 1️⃣ Add new post
            System.out.println("\n📘 Adding a new post...");
            Post newPost = discussionManager.addPost(group, author, "This is our first discussion post!");
            System.out.println("✅ New post added with ID: " + newPost.getPostId());

            // 2️⃣ Add tags to post
            System.out.println("\n🏷️ Adding tags to post...");
            try {
                taggingManager.addTagToPost(newPost, "Java");
            } catch (SQLException e) {
                System.out.println("❌ Failed to add tag 'Java': " + e.getMessage());
            }
            try {
                taggingManager.addTagToPost(newPost, "Learning");
            } catch (SQLException e) {
                System.out.println("❌ Failed to add tag 'Learning': " + e.getMessage());
            }
            // 3️⃣ Add comments to the post
            System.out.println("\n💬 Adding comments...");
            Comment comment1 = null;
            Comment comment2 = null;
            Comment comment3 = null;

            try {
                comment1 = commentManager.addComment(newPost, author, "Great post!");
            } catch (SQLException e) {
                System.out.println("❌ Failed to add comment1: " + e.getMessage());
            }

            try {
                comment2 = commentManager.addComment(newPost, author, "Thanks for sharing.", comment1);
            } catch (SQLException e) {
                System.out.println("❌ Failed to add comment2: " + e.getMessage());
            }

            try {
                comment3 = commentManager.addComment(newPost, author, "I have a question.", null);
            } catch (SQLException e) {
                System.out.println("❌ Failed to add comment3: " + e.getMessage());
            }
            // 4️⃣ View all posts
            System.out.println("\n🗂️ All Posts:");
            for (Post p : discussionManager.getAllPosts()) {
                System.out.println("----------------------------");
                System.out.println("Post ID: " + p.getPostId());
                System.out.println("Author: " + p.getAuthor().getUsername());
                System.out.println("Content: " + p.getContent());
                System.out.println("Timestamp: " + p.getTimestamp());
                // Display comments for this post
                LinkedList<Comment> comments = commentManager.getCommentsForPost(p);
                for (Comment c : comments) {
                    displayComment(c, 0);  // helper method to show threaded replies
                }

            }

            // 5️⃣ Get posts by tag
            System.out.println("\n🔎 Posts with tag 'Java':");
            LinkedList<Post> javaPosts = taggingManager.getPostsByTag("Java", group);
            for (Post p : javaPosts) {
                System.out.println("- " + p.getContent() + " (by " + p.getAuthor().getUsername() + ")");
            }

        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Recursive display of comments
    private static void displayComment(Comment comment, int level) {
        String indent = "  ".repeat(level); // indentation for replies
        System.out.println(indent + "- " + comment.getAuthor().getUsername() + ": " + comment.getContent() + " (Replies: " + comment.getReplies().size() + ")");
        for (Comment reply : comment.getReplies()) {
            displayComment(reply, level + 1);
        }
    }
}

