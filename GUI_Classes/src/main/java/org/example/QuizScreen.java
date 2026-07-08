package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuizScreen extends JFrame {

    private PollsAndQuizzesManager manager;
    private User currentUser;
    private DashboardScreen dashboard;

    // Create Quiz UI
    private JTextField questionField;
    private JTextField optionA;
    private JTextField optionB;
    private JTextField optionC;
    private JComboBox<String> correctAnswerDropdown;
    private DefaultListModel<String> quizQuestionsModel;

    // Attempt Quiz UI
    private JComboBox<String> quizDropdown;
    private JTextArea attemptArea;

    public QuizScreen(PollsAndQuizzesManager manager, User user, DashboardScreen dashboard) {
        this.manager = manager;
        this.currentUser = user;
        this.dashboard = dashboard;

        setTitle("Quizzes");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Create Quiz", createQuizPanel());
        tabs.add("Attempt Quiz", attemptQuizPanel());

        add(tabs, BorderLayout.CENTER);
        add(backButton(), BorderLayout.SOUTH);

        refreshQuizDropdown();
    }

    private JButton backButton() {
        JButton back = UIHelper.createSecondaryButton("← Back");
        back.addActionListener(e -> {
            if (dashboard != null) {
                dashboard.setVisible(true);
            }
            this.dispose();
        });
        return back;
    }

    // ----------------------------------------------------------
    // CREATE QUIZ TAB
    // ----------------------------------------------------------
    private JPanel createQuizPanel() {
        JPanel p = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5, 2));

        questionField = new JTextField();
        optionA = new JTextField();
        optionB = new JTextField();
        optionC = new JTextField();
        correctAnswerDropdown = new JComboBox<>(new String[]{"A", "B", "C"});

        form.add(new JLabel("Question:"));
        form.add(questionField);

        form.add(new JLabel("Option A:"));
        form.add(optionA);

        form.add(new JLabel("Option B:"));
        form.add(optionB);

        form.add(new JLabel("Option C:"));
        form.add(optionC);

        form.add(new JLabel("Correct Answer:"));
        form.add(correctAnswerDropdown);

        quizQuestionsModel = new DefaultListModel<>();
        JList<String> qList = new JList<>(quizQuestionsModel);

        JButton addQ = new JButton("Add Question");
        addQ.addActionListener(e -> addQuestion());

        JButton saveQuiz = new JButton("Save Quiz");
        saveQuiz.addActionListener(e -> saveQuiz());

        p.add(form, BorderLayout.NORTH);
        p.add(new JScrollPane(qList), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(addQ);
        bottom.add(saveQuiz);

        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private void addQuestion() {
        if (questionField.getText().isEmpty() ||
                optionA.getText().isEmpty() ||
                optionB.getText().isEmpty() ||
                optionC.getText().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Fill all fields.");
            return;
        }

        String q = questionField.getText();
        quizQuestionsModel.addElement(q);

        questionField.setText("");
        optionA.setText("");
        optionB.setText("");
        optionC.setText("");
    }

    private void saveQuiz() {
        List<QuizQuestion> questions = new ArrayList<>();

        for (int i = 0; i < quizQuestionsModel.size(); i++) {
            String q = quizQuestionsModel.get(i);

            String[] opts = {optionA.getText(), optionB.getText(), optionC.getText()};
            int correct = correctAnswerDropdown.getSelectedIndex();

            questions.add(new QuizQuestion(q, opts, correct));
        }

        try {
            manager.createQuiz(new StudyGroup("Dummy Group", "Dummy Description", new User("Dummy Admin", "dummy", "dummy@mail.com")), "New Quiz", questions);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving quiz: " + e.getMessage());
            return;
        }

        JOptionPane.showMessageDialog(this, "Quiz Saved!");
        quizQuestionsModel.clear();

        refreshQuizDropdown();
    }

    // ----------------------------------------------------------
    // ATTEMPT QUIZ TAB
    // ----------------------------------------------------------
    private JPanel attemptQuizPanel() {
        JPanel p = new JPanel(new BorderLayout());

        quizDropdown = new JComboBox<>();
        attemptArea = new JTextArea();
        attemptArea.setEditable(false);

        JButton loadQuizBtn = new JButton("Load Quiz");
        loadQuizBtn.addActionListener(e -> loadQuiz());

        JButton submitBtn = new JButton("Submit Answers");
        submitBtn.addActionListener(e -> submitQuiz());

        p.add(labeled("Choose quiz:", quizDropdown), BorderLayout.NORTH);
        p.add(new JScrollPane(attemptArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(loadQuizBtn);
        bottom.add(submitBtn);

        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    private void loadQuiz() {
        int idx = quizDropdown.getSelectedIndex();
        if (idx < 0) return;

        Quiz q = manager.getQuiz(idx);

        StringBuilder sb = new StringBuilder();
        sb.append("ANSWER FORMAT:\nEnter answers separated by commas (0=A, 1=B, 2=C)\n\n");
        for (int i = 0; i < q.getQuestions().size(); i++) {
            QuizQuestion qq = q.getQuestions().get(i);
            sb.append((i + 1)).append(". ").append(qq.getQuestion()).append("\n");
            sb.append("   A) ").append(qq.getOptions()[0]).append("\n");
            sb.append("   B) ").append(qq.getOptions()[1]).append("\n");
            sb.append("   C) ").append(qq.getOptions()[2]).append("\n\n");
        }

        attemptArea.setText(sb.toString());
    }

    private void submitQuiz() {
        String answerText = JOptionPane.showInputDialog("Enter answers (0,1,2):");

        if (answerText == null) return;

        try {
            String[] parts = answerText.split(",");
            List<Integer> answers = new ArrayList<>();
            for (String s : parts) answers.add(Integer.parseInt(s.trim()));

            int idx = quizDropdown.getSelectedIndex();
            int score = manager.attemptQuiz(idx, answers);

            JOptionPane.showMessageDialog(this,
                    "Quiz Completed!\nYour Score: " + score);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid answer format.");
        }
    }

    // ----------------------------------------------------------
    private JPanel labeled(String txt, JComponent c) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(txt), BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    private void refreshQuizDropdown() {
        quizDropdown.removeAllItems();

        List<Quiz> list = manager.getAllQuizzes();
        for (int i = 0; i < list.size(); i++) {
            quizDropdown.addItem("Quiz " + (i + 1));
        }
    }
}
