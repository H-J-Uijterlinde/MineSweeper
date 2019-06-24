package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.GameField;

/*
This class contains the main method, which is the entry point of the application. It creates
the first GameInterface, for which I chose beginner as the default difficulty level.
 */

public class RunMineSweeper {

    public static void main(String[] args) {

        new GameInterface(DifficultyLevel.BEGINNER);
    }
}
