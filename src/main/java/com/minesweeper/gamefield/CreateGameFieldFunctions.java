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

    /*
    The createGameTiles method generates a list of Tiles representing an abstraction of the actual game field.
    Based on the difficulty level it first obtains a set of integers from the randomBombIDGenerator. It then determines
    the total number of tiles to be added, and adds that number of tiles to a new list. If the index where a tile will
    be added corresponds to an index in the set of bombIDs, at that index a new BombTile will be added.
     */
    static List<Tile> createGameTiles(DifficultyLevel difficultyLevel) {

        Set<Integer> bombIDs = randomBombIDGenerator(difficultyLevel);
        int numberOfTiles = difficultyLevel.getNumberOfTiles();
        List<Tile> gameTiles = new ArrayList<>();

        for (int index = 0; index < numberOfTiles; index++) {
            if (bombIDs.contains(index)) gameTiles.add(new BombTile(index));
            else gameTiles.add(new NormalTile(index));
        }
        System.out.println(gameTiles.size());
        return gameTiles;
    }

    /*
    The randomBombIDGenerator generates a set of unique IDs (a set cannot contain duplicate values). It first determines
    the total number of bombs from the give difficulty level. It also needs the total number of tiles, so it knows in
    what range to generate random numbers. It then tries to add random numbers to the set. Math.random generates a
    random double between 0 and 1, so we need to multiply this with the number of possible IDs and cast it to an int.
    If the random ID is already in the set, the add method just returns false, and the loop wil run again.
     */
    private static Set<Integer> randomBombIDGenerator(DifficultyLevel difficultyLevel) {
        int numberOfBombs = difficultyLevel.getNumberOfBombs();
        int totalTiles = difficultyLevel.getNumberOfTiles();
        Set<Integer> bombIDs = new TreeSet<>();
        do {
            bombIDs.add((int) (Math.random() * (totalTiles)));
        } while (bombIDs.size() < numberOfBombs);
        return bombIDs;
    }

    /*
    The getAdjacentBombs method loops through the list with  adjacent tiles obtained from the getAdjacentTiles method,
    and for each Tile in the list, determines if it is a bomb tile. If so, it increments the bomb count. finally it
    returns to total bomb count.
     */
    public static int getAdjacentBombs(GameField gamefield, int tileID) {
        int totalAdjacentBombs = 0;
        for (Tile adjacentTile : getAdjacentTiles(gamefield, tileID)) {
            if (adjacentTile.isBomb()) totalAdjacentBombs++;
        }
        return totalAdjacentBombs;
    }

    /*
    The getAdjacentTiles method loops through the array with adjacent tileIDs and first determines if the tileID is on
    the game field. It then adds tiles from the list with all the tiles to a list containing only adjacent tiles.
     */
    private static List<Tile> getAdjacentTiles(GameField gamefield, int tileID) {
        List<Tile> alltiles = gamefield.getGameFieldTiles();
        List<Tile> adjacentTiles = new ArrayList<>();
        int totalTiles = alltiles.size();
        for (int adjacentTileID : getAdjacentTileIDs(gamefield, tileID)) {
            if (adjacentTileID >= 0 && adjacentTileID < totalTiles) {
                adjacentTiles.add(alltiles.get(adjacentTileID));
            }
        }
        return adjacentTiles;
    }

    /*
    The getAdjacentTileIDs method returns an array containing the IDs of the tiles adjacent to the Tile with the given
    tileID. It needs the gameField as argument to determine the difficulty. Then it determines the IDs based on a
    position vector. Adding the tiles per row to the current tileID returns the Tile right below the current tile.
    The other vectors are based on this fact. If the current tile is on the first or last column it will have only 5
    adjacent tiles instead of 8. So this must be checked.
     */
    public static int[] getAdjacentTileIDs(GameField gameField, int tileID) {
        int tilesPerRow = gameField.getDifficulty().getFieldLengthInTiles();
        int[] adjacentTileIds;
        if (isFirstColumn(gameField, tileID)) {
            adjacentTileIds = new int[]{tileID + 1, tileID - tilesPerRow, tileID - tilesPerRow + 1,
                    tileID + tilesPerRow, tileID + tilesPerRow + 1};
        } else if (isLastColumn(gameField, tileID)) {
            adjacentTileIds = new int[]{tileID - 1, tileID - tilesPerRow - 1,
                    tileID - tilesPerRow, tileID + tilesPerRow,
                    tileID + tilesPerRow - 1};
        } else {
            adjacentTileIds = new int[]{tileID - 1, tileID + 1, tileID - tilesPerRow - 1,
                    tileID - tilesPerRow, tileID - tilesPerRow + 1, tileID + tilesPerRow,
                    tileID + tilesPerRow - 1, tileID + tilesPerRow + 1};
        }
        return adjacentTileIds;
    }

    /*
    isFirstColumn determines if the given tileID is on the first column
     */
    private static boolean isFirstColumn(GameField gameField, int tileID) {
        int tilesPerRow = gameField.getDifficulty().getFieldLengthInTiles();
        return tileID % tilesPerRow == 0;
    }

    /*
    isLastColumn determines if the given tileID is on the last column
     */
    private static boolean isLastColumn(GameField gameField, int tileID) {
        int tilesPerRow = gameField.getDifficulty().getFieldLengthInTiles();
        return (tileID + 1) % tilesPerRow == 0;
    }
}
