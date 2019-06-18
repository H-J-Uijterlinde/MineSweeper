package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.Tile.Tile;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.CreateGameFieldFunctions;
import com.minesweeper.gamefield.GameField;

import java.util.Set;

public class RunMineSweeper {

    public static void main(String[] args) {
        GameField newGame = GameField.startGame(DifficultyLevel.EXPERT);

        System.out.println(newGame.toString());

        GameInterface startGame = new GameInterface(newGame);
    }
}
