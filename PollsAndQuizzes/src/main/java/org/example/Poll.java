package org.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Poll {

    private String question;
    private List<String> options;
    private int[] votes;
    private HashSet<String> votedUsers;  // prevent double voting

    public Poll(String question, List<String> options) {
        this.question = question;
        this.options = options;
        this.votes = new int[options.size()];
        this.votedUsers = new HashSet<>();
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int[] getVotes() {
        return votes;
    }

    public boolean vote(int optionIndex, String userId) {
        if (votedUsers.contains(userId)) {
            return false; // already voted
        }
        votes[optionIndex]++;
        votedUsers.add(userId);
        return true;
    }
}
