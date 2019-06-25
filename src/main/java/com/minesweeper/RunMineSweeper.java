package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.HighScores.HighScoreUtils;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.GameField;

/*
This class contains the main method, which is the entry point of the application. It creates
the first GameInterface, for which I chose beginner as the default difficulty level.
 */

public class RunMineSweeper {

    public static void main(String[] args) {
        HighScoreUtils highscores = HighScoreUtils.createHighScores();
        highscores.addHighScore(DifficultyLevel.BEGINNER, 134, "HJ");
        highscores.addHighScore(DifficultyLevel.BEGINNER, 135, "Sema");
        highscores.addHighScore(DifficultyLevel.BEGINNER, 341, "Sema");
        highscores.addHighScore(DifficultyLevel.BEGINNER, 1324, "Sema");
        highscores.addHighScore(DifficultyLevel.BEGINNER, 231, "Sema");

        new GameInterface(DifficultyLevel.BEGINNER);
    }
}
