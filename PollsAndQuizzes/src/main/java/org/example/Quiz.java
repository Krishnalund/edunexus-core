package org.example;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Quiz {
    private static int idCounter = 1;
    private int quizId;
    private String title;
    private List<QuizQuestion> questions;
    private LinkedList<Response> responses;

    // Constructor
    public Quiz(String title, List<QuizQuestion> questions) {
        if (questions == null || questions.isEmpty()) {
            throw new IllegalArgumentException("Quiz must have at least one question.");
        }
        this.quizId = idCounter++;
        this.title = title;
        this.questions = new LinkedList<>(questions);
        this.responses = new LinkedList<>();
    }

    // Add a response to quiz
    public void addResponse(Response response) throws InvalidResponseException {
        if (response.getAnswers().length != questions.size()) {
            throw new InvalidResponseException("Answer count doesn't match question count.");
        }
        responses.add(response);
        System.out.println("Response added for quiz: " + title);
    }

    // Get all responses (results)
    public LinkedList<Response> getResults() {
        return responses;
    }

    // Getters
    public int getQuizId() {
        return quizId;
    }

    public String getTitle() {
        return title;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public int evaluate(List<Integer> answers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (answers.get(i) == questions.get(i).getCorrectIndex()) {
                score++;
            }
        }
        return score;
    }
}

class InvalidResponseException extends Exception {
    public InvalidResponseException(String message) {
        super(message);
    }
}
