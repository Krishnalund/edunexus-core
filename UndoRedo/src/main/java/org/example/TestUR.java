package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EmptyStackException;

public class TestUR {
    public static void main(String[] args) {
        Connection connection = null;

        try {
            // -------------------------------
            // 1️⃣ Database connection setup
            // -------------------------------
            String dbPath = "C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb";
            String dbURL = "jdbc:ucanaccess://C:/Users/EURO COMPUTERS/Documents/StudyPlatform.accdb";

            connection = DriverManager.getConnection(dbURL);
            System.out.println("✅ Database connected successfully!");

            // -------------------------------
            // 2️⃣ Initialize UndoRedoManager
            // -------------------------------
            UndoRedoManager undoRedoManager = new UndoRedoManager();

            // -------------------------------
            // 3️⃣ Simulate some actions
            // -------------------------------
            Action addPost = new Action("addPost", "Post1");
            Action deletePost = new Action("deletePost", "Post2");
            Action sendMessage = new Action("sendMessage", "Hello Bob!");

            undoRedoManager.performAction(addPost);
            undoRedoManager.performAction(deletePost);
            undoRedoManager.performAction(sendMessage);

            // -------------------------------
            // 4️⃣ Undo last 2 actions
            // -------------------------------
            System.out.println("\n⏪ Undoing last 2 actions...");
            undoRedoManager.undoLastAction(); // undo sendMessage
            undoRedoManager.undoLastAction(); // undo deletePost

            // -------------------------------
            // 5️⃣ Redo last undone action
            // -------------------------------
            System.out.println("\n⏩ Redoing last action...");
            undoRedoManager.redoAction(); // redo deletePost

        } catch (SQLException e) {
            System.err.println("❌ Database connection or SQL error: " + e.getMessage());
        } catch (EmptyStackException e) {
            System.err.println("❌ No actions left to undo/redo.");
        } finally {
            // -------------------------------
            // 6️⃣ Close connection
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
