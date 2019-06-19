package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.GameField;

public class RunMineSweeper {

    public static void main(String[] args) {
        GameField newGame = GameField.startGame(DifficultyLevel.BEGINNER);

        System.out.println(newGame.toString());

        GameInterface startGame = new GameInterface(newGame);
    }
}
