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
        int totalTiles = difficultyLevel.getNumberOfTiles();
        Set<Integer> bombIDs = new TreeSet<>();
        do {
            bombIDs.add((int) (Math.random() * totalTiles));
        } while (bombIDs.size() < numberOfBombs);
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


    public static int getAdjacentBombs(GameField gamefield, int tileID) {
        int totalAdjacentBombs = 0;
        for (Tile adjacentTile: getAdjacenttiles(gamefield, tileID)) {
            if (adjacentTile.isBomb()) totalAdjacentBombs++;
        }
        return totalAdjacentBombs;
    }

    private static List<Tile> getAdjacenttiles(GameField gamefield, int tileID) {
        List<Tile> alltiles = gamefield.getGameFieldTiles();
        List<Tile> adjacentTiles = new ArrayList<>();
        int totalTiles = alltiles.size();
        for (int adjacentTileID : gatAdjecentTileIDs(gamefield, tileID)){
            if (adjacentTileID >= 0 && adjacentTileID <totalTiles) {
                adjacentTiles.add(alltiles.get(adjacentTileID));
            }
        }
        return adjacentTiles;
    }

    //TODO determine exceptions, meaning tiles at the edges.
    private static int[] gatAdjecentTileIDs(GameField gameField, int tileID) {
        int tilesPerRow = gameField.getDifficulty().getFieldLengthInTiles();
        int[] adjacentTileIds = {tileID - 1, tileID + 1, tileID - tilesPerRow - 1,
                tileID - tilesPerRow, tileID - tilesPerRow + 1, tileID + tilesPerRow,
                tileID + tilesPerRow - 1, tileID + tilesPerRow + 1};
        return adjacentTileIds;
    }
}
