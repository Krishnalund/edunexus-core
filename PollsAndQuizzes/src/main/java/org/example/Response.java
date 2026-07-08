package org.example;

public class Response {
    private User user;
    private String[] answers;

    // Constructor
    public Response(User user, String[] answers) {
        if (answers == null || answers.length == 0) {
            throw new IllegalArgumentException("Answers array cannot be empty.");
        }
        this.user = user;
        this.answers = answers.clone(); // Defensive copy for immutability
    }

    // Getters
    public User getUser() {
        return user;
    }

    public String[] getAnswers() {
        return answers;
    }
}
