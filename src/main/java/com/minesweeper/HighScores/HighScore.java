package com.minesweeper.HighScores;

import com.minesweeper.GameUtils.DifficultyLevel;

/*
The HighScore class is used to keep track of the players score. A high score consists of the time elapsed, the players
name. And the difficulty level of the played game.
 */

public abstract class HighScore implements Comparable<HighScore> {

    private DifficultyLevel difficultyLevel;
    private long timeElapsed;
    private String playerName;

    HighScore(DifficultyLevel difficultyLevel, long timeElapsed, String playerName) {
        this.difficultyLevel = difficultyLevel;
        this.timeElapsed = timeElapsed;
        this.playerName = playerName;
    }

    @Override
    public int compareTo(HighScore otherHighScore) {
        return (int) (this.getTimeElapsed() - otherHighScore.getTimeElapsed());
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public String getPlayerName() {
        return playerName;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    static class beginnerHighScore extends HighScore {

        beginnerHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.BEGINNER, timeElapsed, playerName);
        }
    }

    static class intermediateHighScore extends HighScore {

        intermediateHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.INTERMEDIATE, timeElapsed, playerName);
        }
    }

    static class expertHighScore extends HighScore {

        expertHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.EXPERT, timeElapsed, playerName);
        }
    }
}
