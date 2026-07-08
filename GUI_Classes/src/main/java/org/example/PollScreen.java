package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class PollScreen extends JFrame {

    private AppContext context;



    // UI Components
    private JTextField questionField;
    private JTextField optionField;
    private DefaultListModel<String> optionsModel;
    private JList<String> optionsList;

    private JComboBox<String> pollDropdown;
    private JComboBox<String> voteOptionsDropdown;

    private JTextArea resultArea;

    public PollScreen(AppContext context) {
        this.context = context;

        setTitle("Polls");
        setSize(UIConstants.WINDOW_SIZE_MEDIUM);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("Create Poll", createPollPanel());
        tabs.add("Vote Poll", votePollPanel());
        tabs.add("Results", resultPanel());

        add(tabs, BorderLayout.CENTER);

        // Add back button
        JButton btnBack = UIHelper.createSecondaryButton("← Back");
        btnBack.addActionListener(e -> dispose());
        add(btnBack, BorderLayout.SOUTH);

        refreshPollDropdowns();
    }


    // ----------------------------------------------------
    // CREATE POLL TAB
    // ----------------------------------------------------
    private JPanel createPollPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new GridLayout(3, 1));

        questionField = new JTextField();
        top.add(labeled("Poll Question:", questionField));

        optionField = new JTextField();
        JButton addOptionBtn = new JButton("Add Option");
        addOptionBtn.addActionListener(e -> {
            if (!optionField.getText().isEmpty()) {
                optionsModel.addElement(optionField.getText());
                optionField.setText("");
            }
        });

        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.add(optionField, BorderLayout.CENTER);
        optionPanel.add(addOptionBtn, BorderLayout.EAST);

        top.add(optionPanel);

        optionsModel = new DefaultListModel<>();
        optionsList = new JList<>(optionsModel);
        JScrollPane scroll = new JScrollPane(optionsList);

        JButton createBtn = new JButton("Create Poll");
        createBtn.addActionListener(this::handleCreatePoll);

        p.add(top, BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        p.add(createBtn, BorderLayout.SOUTH);

        return p;
    }

    private void handleCreatePoll(ActionEvent e) {
        if (questionField.getText().isEmpty() || optionsModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter question and at least one option.");
            return;
        }

        List<String> opts = new ArrayList<>();
        for (int i = 0; i < optionsModel.size(); i++) opts.add(optionsModel.get(i));

        context.pollsAndQuizzesManager.createPoll(questionField.getText(), opts);

        JOptionPane.showMessageDialog(this, "Poll Created!");

        questionField.setText("");
        optionsModel.clear();
        refreshPollDropdowns();
    }

    // ----------------------------------------------------
    // VOTE TAB
    // ----------------------------------------------------
    private JPanel votePollPanel() {
        JPanel p = new JPanel(new GridLayout(4, 1));

        pollDropdown = new JComboBox<>();
        pollDropdown.addActionListener(e -> refreshVoteOptions());

        voteOptionsDropdown = new JComboBox<>();

        JButton voteBtn = new JButton("Vote");
        voteBtn.addActionListener(this::handleVote);

        p.add(labeled("Choose Poll:", pollDropdown));
        p.add(labeled("Options:", voteOptionsDropdown));
        p.add(voteBtn);

        return p;
    }

    private void handleVote(ActionEvent e) {
        int pollIndex = pollDropdown.getSelectedIndex();
        int optionIndex = voteOptionsDropdown.getSelectedIndex();

        if (pollIndex < 0 || optionIndex < 0) {
            JOptionPane.showMessageDialog(this, "Please select a poll and option.");
            return;
        }

        boolean ok = context.pollsAndQuizzesManager.getPoll(pollIndex).vote(optionIndex, context.currentUser.getUserId());

        if (!ok) {
            JOptionPane.showMessageDialog(this, "You already voted!");
        } else {
            JOptionPane.showMessageDialog(this, "Vote Recorded!");
        }
    }

    // ----------------------------------------------------
    // RESULTS TAB
    // ----------------------------------------------------
    private JPanel resultPanel() {
        JPanel p = new JPanel(new BorderLayout());
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        JButton refreshBtn = new JButton("Refresh Results");
        refreshBtn.addActionListener(e -> loadResults());

        p.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        p.add(refreshBtn, BorderLayout.SOUTH);

        return p;
    }

    private void loadResults() {
        StringBuilder sb = new StringBuilder();

        List<Poll> polls = context.pollsAndQuizzesManager.getAllPolls();
        for (int i = 0; i < polls.size(); i++) {
            Poll poll = polls.get(i);
            sb.append("Poll ").append(i + 1).append(": ").append(poll.getQuestion()).append("\n");

            int[] votes = poll.getVotes();
            for (int j = 0; j < votes.length; j++) {
                sb.append("   ").append(poll.getOptions().get(j))
                        .append(" — ").append(votes[j]).append(" votes\n");
            }
            sb.append("\n");
        }

        resultArea.setText(sb.toString());
    }

    // ----------------------------------------------------
    // HELPERS
    // ----------------------------------------------------
    private JPanel labeled(String text, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(text), BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private void refreshPollDropdowns() {
        pollDropdown.removeAllItems();
        for (Poll p : context.pollsAndQuizzesManager.getAllPolls()) {
            pollDropdown.addItem(p.getQuestion());
        }
        refreshVoteOptions();
    }

    private void refreshVoteOptions() {
        voteOptionsDropdown.removeAllItems();
        int idx = pollDropdown.getSelectedIndex();
        if (idx >= 0) {
            Poll poll = context.pollsAndQuizzesManager.getPoll(idx);
            for (String opt : poll.getOptions()) {
                voteOptionsDropdown.addItem(opt);
            }
        }
    }
}
