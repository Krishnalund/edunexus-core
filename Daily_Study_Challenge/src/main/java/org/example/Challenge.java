package org.example;

public class Challenge {
    private String taskDescription;
    private int rewardPoints;
    private String difficulty; // Easy, Medium, Hard
    private String category; // Algorithms, Data Structures, etc.

    public Challenge(String taskDescription, int rewardPoints) {
        this(taskDescription, rewardPoints, "Medium", "General");
    }

    public Challenge(String taskDescription, int rewardPoints, String difficulty, String category) {
        this.taskDescription = taskDescription;
        this.rewardPoints = rewardPoints;
        this.difficulty = difficulty;
        this.category = category;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }
}
