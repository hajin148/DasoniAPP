package com.example.dasoniapp;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreBoard {
    private Map<String, Integer> topRhythmScores = new HashMap<>();
    private Map<String, Integer> topNoteScores = new HashMap<>();

    public ScoreBoard() {
        // Initialize with default values or from a persistent storage
    }

    // Method to update rhythm score
    public void updateRhythmScore(String userName, int newRhythmScore) {
        updateScoreBoard(userName, newRhythmScore, topRhythmScores);
    }

    // Method to update note score
    public void updateNoteScore(String userName, int newNoteScore) {
        updateScoreBoard(userName, newNoteScore, topNoteScores);
    }

    private void updateScoreBoard(String userName, int newScore, Map<String, Integer> scoreBoard) {
        // Update the score for the user
        Integer currentScore = scoreBoard.get(userName);
        if (currentScore == null || newScore > currentScore) {
            scoreBoard.put(userName, newScore);
        }

        // Sort and limit the size of the scoreboard to top 10 entries
        // Note: This is a simplified way to keep the top 10 scores.
        // Depending on your use case, you might need a more efficient way.
        scoreBoard = scoreBoard.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        HashMap::new));
    }

    // Getters for topRhythmScores and topNoteScores
    public Map<String, Integer> getTopRhythmScores() {
        return topRhythmScores;
    }

    public Map<String, Integer> getTopNoteScores() {
        return topNoteScores;
    }

}
