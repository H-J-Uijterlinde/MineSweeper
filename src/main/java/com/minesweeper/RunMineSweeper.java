package com.minesweeper;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.Tile.Tile;
import com.minesweeper.UserInterface.GameInterface;
import com.minesweeper.gamefield.CreateGameFieldFunctions;
import com.minesweeper.gamefield.GameField;

import java.util.Set;

public class RunMineSweeper {

    public static void main(String[] args) {
        // This wil be the entry point of the minesweeper game.
        //TODO the main method wil have to start the gameframe.
        System.out.println("Start Game");
        GameField newGame = GameField.startGame(DifficultyLevel.BEGINNER);
        System.out.println(newGame.toString());

        GameInterface startGame = new GameInterface(newGame);
    }
}
