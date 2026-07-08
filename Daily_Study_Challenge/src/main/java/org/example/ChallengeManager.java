package org.example;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChallengeManager {
    private CircularQueue<Challenge> challenges;
    private Connection dbConnection;
    private List<Challenge> challengePool;

    public ChallengeManager(int capacity, Connection dbConnection) throws SQLException {
        this.dbConnection = dbConnection;
        this.challenges = new CircularQueue<>(capacity);
        this.challengePool = new ArrayList<>();
    }

    public void initializeChallengePool() {
        challengePool.clear();

        // Easy Challenges
        challengePool.add(new Challenge(
                "Reverse a String\n\nWrite a function that reverses a string. For example, 'hello' becomes 'olleh'.",
                5, "Easy", "Strings"));
        challengePool.add(new Challenge(
                "Find Maximum in Array\n\nWrite a function to find the maximum element in an array of integers.",
                5, "Easy", "Arrays"));
        challengePool.add(new Challenge(
                "Check Palindrome\n\nWrite a function to check if a given string is a palindrome (reads same forwards and backwards).",
                5, "Easy", "Strings"));
        challengePool.add(new Challenge(
                "Sum of Array Elements\n\nWrite a function that returns the sum of all elements in an integer array.",
                5, "Easy", "Arrays"));
        challengePool.add(new Challenge(
                "Count Vowels\n\nWrite a function to count the number of vowels (a, e, i, o, u) in a given string.",
                5, "Easy", "Strings"));

        // Medium Challenges
        challengePool.add(new Challenge(
                "Two Sum Problem\n\nGiven an array of integers and a target sum, find two numbers that add up to the target. Return their indices.",
                10, "Medium", "Algorithms"));
        challengePool.add(new Challenge(
                "Binary Search Implementation\n\nImplement binary search algorithm to find an element in a sorted array. Return the index or -1 if not found.",
                10, "Medium", "Algorithms"));
        challengePool.add(new Challenge(
                "Remove Duplicates from Sorted Array\n\nGiven a sorted array, remove duplicates in-place and return the new length.",
                10, "Medium", "Arrays"));
        challengePool.add(new Challenge(
                "Valid Parentheses\n\nGiven a string containing '(', ')', '{', '}', '[' and ']', determine if the input string is valid (all brackets properly closed).",
                10, "Medium", "Data Structures"));
        challengePool.add(new Challenge(
                "Merge Two Sorted Lists\n\nMerge two sorted linked lists into one sorted linked list.",
                10, "Medium", "Data Structures"));
        challengePool.add(new Challenge(
                "Find First Non-Repeating Character\n\nGiven a string, find the first non-repeating character and return its index. Return -1 if none exists.",
                10, "Medium", "Strings"));
        challengePool.add(new Challenge(
                "Rotate Array\n\nRotate an array to the right by k steps. For example, [1,2,3,4,5] rotated by 2 becomes [4,5,1,2,3].",
                10, "Medium", "Arrays"));

        // Hard Challenges
        challengePool.add(new Challenge(
                "Longest Substring Without Repeating Characters\n\nFind the length of the longest substring without repeating characters.",
                15, "Hard", "Algorithms"));
        challengePool.add(new Challenge(
                "Median of Two Sorted Arrays\n\nFind the median of two sorted arrays. The overall run time complexity should be O(log(m+n)).",
                15, "Hard", "Algorithms"));
        challengePool.add(new Challenge(
                "Trapping Rain Water\n\nGiven n non-negative integers representing elevation map, compute how much water it can trap after raining.",
                15, "Hard", "Algorithms"));
        challengePool.add(new Challenge(
                "Serialize and Deserialize Binary Tree\n\nDesign an algorithm to serialize and deserialize a binary tree.",
                15, "Hard", "Data Structures"));
        challengePool.add(new Challenge(
                "Word Ladder\n\nGiven two words and a dictionary, find the length of shortest transformation sequence from start to end word.",
                15, "Hard", "Algorithms"));

        // Additional Medium Challenges
        challengePool.add(new Challenge(
                "Group Anagrams\n\nGiven an array of strings, group anagrams together. For example: ['eat','tea','tan','ate','nat','bat'] -> [['eat','tea','ate'],['tan','nat'],['bat']]",
                10, "Medium", "Strings"));
        challengePool.add(new Challenge(
                "Implement Queue using Stacks\n\nImplement a queue data structure using only two stacks.",
                10, "Medium", "Data Structures"));
        challengePool.add(new Challenge(
                "Find Peak Element\n\nA peak element is greater than its neighbors. Find a peak element and return its index.",
                10, "Medium", "Arrays"));
        challengePool.add(new Challenge(
                "Longest Palindromic Substring\n\nGiven a string, find the longest palindromic substring.",
                10, "Medium", "Strings"));
        challengePool.add(new Challenge(
                "Kth Largest Element\n\nFind the kth largest element in an unsorted array.",
                10, "Medium", "Algorithms"));
    }

    public void addChallenge(String task, int points) throws SQLException, QueueEmptyException {
        Challenge challenge = new Challenge(task, points);
        try {
            challenges.enqueue(challenge);
        } catch (QueueFullException e) {
            challenges.dequeue();
            try {
                challenges.enqueue(challenge);
            } catch (QueueFullException ignored) {
            }
        }
    }

    public Challenge getDailyChallenge() {
        // Use date-based random selection to ensure same challenge for entire day
        if (challengePool.isEmpty()) {
            initializeChallengePool();
        }

        if (challengePool.isEmpty()) {
            // Fallback to old method if pool is still empty
            try {
                Challenge today = challenges.dequeue();
                try {
                    challenges.enqueue(today);
                } catch (QueueFullException ignored) {
                }
                return today;
            } catch (QueueEmptyException e) {
                return null;
            }
        }

        // Use current date as seed for random selection
        LocalDate today = LocalDate.now();
        long seed = today.toEpochDay();
        Random random = new Random(seed);

        int index = random.nextInt(challengePool.size());
        return challengePool.get(index);
    }

    public void completeChallenge(User user, Challenge challenge) throws SQLException {
        int earned = challenge.getRewardPoints();
        Contributor contributor = new Contributor(user, 0);
        contributor.addPoints(earned);
    }
}
