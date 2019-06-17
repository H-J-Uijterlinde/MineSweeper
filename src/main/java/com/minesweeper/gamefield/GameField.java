package com.minesweeper.gamefield;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.Tile.Tile;

import java.util.ArrayList;
import java.util.List;

import static com.minesweeper.gamefield.CreateGameFieldFunctions.*;

public class GameField {

    private static GameField currentGame;
    private List<Tile> gameFieldTiles;
    private DifficultyLevel difficulty;

    private GameField(DifficultyLevel difficultyLevel) {
        this.gameFieldTiles = getGameFieldTiles(difficultyLevel);
        this.difficulty = difficultyLevel;
    }

    public static GameField startGame(DifficultyLevel difficultyLevel) {
        if (currentGame == null) return new GameField(difficultyLevel);
        else return currentGame;
    }

    private List<Tile> getGameFieldTiles(DifficultyLevel difficultyLevel) {
        return createGameTiles(difficultyLevel);
    }

    public List<Tile> getGameFieldTiles() {
        return gameFieldTiles;
    }

    @Override
    public String toString() {
        StringBuilder gameFieldString = new StringBuilder();
        int numberOfTiles = gameFieldTiles.size();
        int tilesPerRow = difficulty.getFieldLengthInTiles();

        for (int i = 1; i <= numberOfTiles; i++) {
            gameFieldString.append(gameFieldTiles.get(i - 1).toString());
            if (i % (tilesPerRow) == 0) gameFieldString.append("\n");
        }
        return gameFieldString.toString();
    }
}
