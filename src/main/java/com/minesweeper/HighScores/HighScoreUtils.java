package com.minesweeper.HighScores;

import com.minesweeper.GameUtils.DifficultyLevel;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.minesweeper.HighScores.HighScore.*;

/*
The HighScoreUtils class was introduced to keep track of the established high scores. It makes use of the singleton
pattern, because only one set of high scores should exist. The created HighScoreUtils object contains a list of high
scores for each of the difficulty levels.
 */

public class HighScoreUtils {

    private static final HighScoreUtils highScores;
    private List<beginnerHighScore> beginnerHighScores;
    private List<intermediateHighScore> intermediateHighScores;
    private List<expertHighScore> expertHighScores;
    private RestTemplate restTemplate;
    private String beginnerUrl = "https://todolist-back-end-sema.herokuapp.com/api/beginnerHighScore";
    private String intermediateUrl = "https://todolist-back-end-sema.herokuapp.com/api/intermediateHighScore";
    private String expertUrl = "https://todolist-back-end-sema.herokuapp.com/api/expertHighScore";

    static {
        highScores = new HighScoreUtils();
    }

    private HighScoreUtils() {
        beginnerHighScores = new ArrayList<>();
        intermediateHighScores = new ArrayList<>();
        expertHighScores = new ArrayList<>();
        restTemplate = new RestTemplate();
    }

    public static HighScoreUtils createHighScores() {
        if (highScores == null) return new HighScoreUtils();
        else return highScores;
    }

    /*
    The following methods use a restTemplate object, from Spring Web, to perform get requests. The getForObject
    method requires the url of the corresponding endpoint. See HighScoreResponse class for the logic behind obtaining
    the actual list of high scores.
     */

    public List<beginnerHighScore> getBeginnerHighScores() {
        HighScoreResponse<beginnerHighScore> beginnerHighScoreResponse =
                restTemplate.getForObject("https://todolist-back-end-sema.herokuapp.com/api/beginnerHighScore", HighScoreResponse.class);
        if (beginnerHighScoreResponse != null) {
            beginnerHighScores = beginnerHighScoreResponse.get_Embedded().get("beginnerHighScores");
        }
        return beginnerHighScores;
    }

    public List<intermediateHighScore> getIntermediateHighScores() {
        HighScoreResponse<intermediateHighScore> intermediateHighScore =
                restTemplate.getForObject(intermediateUrl, HighScoreResponse.class);
        if (intermediateHighScore != null) {
            intermediateHighScores = intermediateHighScore.get_Embedded().get("intermediateHighScores");
        }
        return intermediateHighScores;
    }

    public List<expertHighScore> getExpertHighScores() {
        HighScoreResponse<expertHighScore> expertHighScore =
                restTemplate.getForObject(expertUrl, HighScoreResponse.class);
        if (expertHighScore != null) {
            expertHighScores = expertHighScore.get_Embedded().get("expertHighScores");
        }
        return expertHighScores;
    }

    /*
    The addHighScore method makes use of a switch statement to add high scores to the corresponding repository endpoints.
    It uses the same restTemplate object as used for the get methods.
     */

    public void addHighScore(DifficultyLevel difficultyLevel, long timeElapsed, String playerName) {
        switch (difficultyLevel) {
            case BEGINNER: {
                restTemplate.postForObject(beginnerUrl,
                        new beginnerHighScore(timeElapsed, playerName), beginnerHighScore.class);
                break;
            }
            case INTERMEDIATE: {
                restTemplate.postForObject(intermediateUrl
                        ,new intermediateHighScore(timeElapsed, playerName), intermediateHighScore.class);
                break;
            }
            case EXPERT: {
                restTemplate.postForObject(expertUrl,
                        new expertHighScore(timeElapsed, playerName), expertHighScore.class);
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
        switch (difficultyLevel) {
            case BEGINNER: {
                if (beginnerHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(beginnerHighScores, timeElapsed);
                break;
            }
            case INTERMEDIATE: {
                if (intermediateHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(intermediateHighScores, timeElapsed);
                break;
            }
            case EXPERT: {
                if (expertHighScores.size() < 10) isHighScore = true;
                else isHighScore = checkIfHighScore(expertHighScores, timeElapsed);
                break;
            }
        }
        return isHighScore;
    }

    private boolean checkIfHighScore(List<? extends HighScore> HighScores, long timeElapsed) {
        Collections.sort(HighScores);
        long lowestHighScoreTime = HighScores.get(9).getTimeElapsed();
        return timeElapsed < lowestHighScoreTime;
    }
}
