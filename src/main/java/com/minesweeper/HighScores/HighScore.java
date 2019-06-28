package com.minesweeper.HighScores;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minesweeper.GameUtils.DifficultyLevel;

/*
The HighScore class is used to keep track of the players score. A high score consists of the time elapsed, the players
name. And the difficulty level of the played game.

The @JsonIgnoreProperties annotation tells your application to ignore any unknown properties during deserialization.
This means that when a JSON object is returned from our REST api, any properties that the JSON object holds that cannot
be set through this java class, are simply ignored.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class HighScore implements Comparable<HighScore> {

    private DifficultyLevel difficultyLevel;
    private long timeElapsed;
    private String playerName;

    public HighScore() {

    }

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

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    static class beginnerHighScore extends HighScore {

        public beginnerHighScore() {
        }

        beginnerHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.BEGINNER, timeElapsed, playerName);
        }
    }

    static class intermediateHighScore extends HighScore {

        public intermediateHighScore() {

        }

        intermediateHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.INTERMEDIATE, timeElapsed, playerName);
        }
    }

    static class expertHighScore extends HighScore {

        public expertHighScore() {}

        expertHighScore(long timeElapsed, String playerName) {
            super(DifficultyLevel.EXPERT, timeElapsed, playerName);
        }
    }
}
