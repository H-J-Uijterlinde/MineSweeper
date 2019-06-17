package com.minesweeper.gamefield;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.Tile.BombTile;
import com.minesweeper.Tile.NormalTile;
import com.minesweeper.Tile.Tile;

import java.util.*;

public class CreateGameFieldFunctions {

    CreateGameFieldFunctions() {
        throw new RuntimeException("Should never be instantiated");
    }

    public static Set<Integer> randomBombIDGenerator(DifficultyLevel difficultyLevel) {
        int numberOfBombs = difficultyLevel.getNumberOfBombs();
        Set<Integer> bombIDs = new TreeSet<Integer>();
        do {
            bombIDs.add((int)(Math.random() * 64));
        }while (bombIDs.size() < numberOfBombs);
        return bombIDs;
    }

    public static List<Tile> createGameTiles(DifficultyLevel difficultyLevel) {

        Set<Integer> bombIDs = randomBombIDGenerator(difficultyLevel);
        int numberOfTiles = difficultyLevel.getNumberOfTiles();
        List<Tile> gameTiles = new ArrayList<>(numberOfTiles);

        for (int index = 0; index < numberOfTiles; index++) {
            if (bombIDs.contains(index)) gameTiles.add(new BombTile(index));
            else gameTiles.add(new NormalTile(index));
        }
        return gameTiles;
    }
}
