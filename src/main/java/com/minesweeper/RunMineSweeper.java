package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.GameField;

/*
This class contains the main method, which is the entry point of the application. It creates
the first GameField, for which is chose beginner as the default difficulty level. It then prints
the GameField object to the console, this obviously isn't necessary, but I left it in for
debugging at first, now mainly for illustration purposes. Finally it creates a new GameInterface
which is the GUI object of the application.
 */

public class RunMineSweeper {

    public static void main(String[] args) {
        GameField newGame = GameField.getGame(DifficultyLevel.BEGINNER);

        System.out.println(newGame.toString());

        new GameInterface(newGame);
    }
}
