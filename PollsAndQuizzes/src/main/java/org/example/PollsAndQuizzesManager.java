package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class PollsAndQuizzesManager {
    private LinkedList<Quiz> quizzes;
    private List<Poll> polls;
    private Connection dbConnection;

    // Constructor
    public PollsAndQuizzesManager(Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.quizzes = new LinkedList<>();
        polls = new ArrayList<>();
        // In a real implementation: load quizzes from DB
    }

    // Create new quiz
    public Quiz createQuiz(StudyGroup group, String title, List<QuizQuestion> questions) throws SQLException {
        Quiz quiz = new Quiz(title, questions);
        quizzes.add(quiz);

        // Simulate DB save
        System.out.println("Quiz saved to DB for group: " + group.getName());
        return quiz;
    }

    public int attemptQuiz(int quizIndex, List<Integer> answers) {
        Quiz quiz = quizzes.get(quizIndex);
        return quiz.evaluate(answers);
    }

    // Submit response to a quiz
    public void submitQuizResponse(Quiz quiz, User user, String[] answers) throws SQLException {
        try {
            Response response = new Response(user, answers);
            quiz.addResponse(response);
            System.out.println("Response submitted for quiz: " + quiz.getTitle());
        } catch (Exception e) {
            throw new SQLException("Failed to submit response: " + e.getMessage());
        }
    }

    // Retrieve quiz results
    public LinkedList<Response> getQuizResults(Quiz quiz) {
        return quiz.getResults();
    }

    public Quiz getQuiz(int index) {
        return quizzes.get(index);
    }

    public List<Quiz> getAllQuizzes() {
        return quizzes;
    }

    // --------------------------------POLLS---------------------------------------

    public void createPoll(String question, List<String> options) {
        polls.add(new Poll(question, options));
    }

    public List<Poll> getAllPolls() {
        return polls;
    }

    public Poll getPoll(int index) {
        return polls.get(index);
    }

    public void vote(int pollIndex, int optionIndex, String userId) {
        Poll poll = polls.get(pollIndex);
        poll.vote(optionIndex, userId);
    }

}
