package com.minesweeper.HighScores;

import com.minesweeper.GameUtils.DifficultyLevel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.minesweeper.HighScores.HighScore.*;

/*
The HighScoreUtils class was introduced to keep track of the established high scores. It makes use of the singleton
pattern, because only one set of high scores should exist. The created HighScoreUtils object contains a list of high
scores for each of the difficulty levels.
 */

public class HighScoreUtils {

    private static final HighScoreUtils highScores;
    private List<HighScore> beginnerHighScores;
    private List<HighScore> intermediateHighScores;
    private List<HighScore> expertHighScores;

    static {highScores = new HighScoreUtils();}

    private HighScoreUtils() {
        beginnerHighScores = new ArrayList<>();
        intermediateHighScores = new ArrayList<>();
        expertHighScores = new ArrayList<>();
    }

    public static HighScoreUtils createHighScores() {
        if (highScores == null) return new HighScoreUtils();
        else return highScores;
    }

    static void fetchHighScores() {
        //Todo create methods to fetch highscores from database.
    }

    public List<HighScore> getBeginnerHighScores() {
        return beginnerHighScores;
    }

    public List<HighScore> getIntermediateHighScores() {
        return intermediateHighScores;
    }

    public List<HighScore> getExpertHighScores() {
        return expertHighScores;
    }

    /*
    The addHighScore method makes use of a switch statement to add high scores to the corresponding list, based on the
    difficulty level of the game played.
     */

    public void addHighScore(DifficultyLevel difficultyLevel, long  timeElapsed, String playerName) {
        switch (difficultyLevel){
            case BEGINNER: {
                beginnerHighScores.add(new beginnerHighScore(timeElapsed, playerName));
                break;
            }case INTERMEDIATE: {
                intermediateHighScores.add(new intermediateHighScore(timeElapsed, playerName));
                break;
            }case EXPERT: {
                expertHighScores.add(new expertHighScore(timeElapsed, playerName));
                break;
            }
        }
    }

    /*
    The determineIfHighScore method makes use of a switch statement to determine which list to check. It calls the
    checkIfHighScore method to determine if the score is higher then the lowest score in the list. Only the top 10
    scores will be displayed, so no more than 10 scores can be added to each list.
     */

    public boolean determineIfHighScore(DifficultyLevel difficultyLevel, long timeElapsed) {
        boolean isHighScore = false;
        switch (difficultyLevel){
            case BEGINNER: {
                if (beginnerHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(beginnerHighScores, timeElapsed);
                break;
            }case INTERMEDIATE: {
                if (intermediateHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(intermediateHighScores, timeElapsed);
                break;
            }case EXPERT: {
                if (expertHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(expertHighScores, timeElapsed);
                break;
            }
        }
        return isHighScore;
    }

    private boolean checkIfHighScore(List<HighScore> HighScores, long timeElapsed) {
        Collections.sort(HighScores);
        long lowestHighScoreTime = HighScores.get(9).getTimeElapsed();
        return timeElapsed < lowestHighScoreTime;
    }
}
