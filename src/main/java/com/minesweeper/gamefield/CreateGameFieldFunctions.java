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
    Dependent on the difficulty level, it first obtains a set of integers from the randomBombIDGenerator.
    It then determines the total number of tiles to be added, and adds that number of tiles to a new list.
    If the index where a tile will be added corresponds to an index in the set of bombIDs, at that index a new
    BombTile will be added.
     */

    static List<Tile> createGameTiles(DifficultyLevel difficultyLevel, int tileID) {

        Set<Integer> bombIDs = randomBombIDGenerator(difficultyLevel, tileID);
        int numberOfTiles = difficultyLevel.getNumberOfTiles();
        List<Tile> gameTiles = new ArrayList<>();

        for (int index = 0; index < numberOfTiles; index++) {
            if (bombIDs.contains(index)) gameTiles.add(new BombTile(index));
            else gameTiles.add(new NormalTile(index));
        }
        return gameTiles;
    }

    /*
    The randomBombIDGenerator generates a set of unique IDs (a set cannot contain duplicate values). It first determines
    the total number of bombs from the given difficulty level. It also needs the total number of tiles, so it knows in
    what range to generate random numbers. It then tries to add random numbers to the set. Math.random generates a
    random double between 0 and 1, so we need to multiply this with the number of possible IDs and cast it to an int.
    If the random ID is already in the set, the add method just returns false, and the loop wil run again.
     */
    private static Set<Integer> randomBombIDGenerator(DifficultyLevel difficultyLevel, int tileID) {
        int numberOfBombs = difficultyLevel.getNumberOfBombs();
        int totalTiles = difficultyLevel.getNumberOfTiles();
        List<Integer> excludedIDs = getAdjacentTileIDs(difficultyLevel, tileID);
        return fillRandomBombIDSet(numberOfBombs, totalTiles, excludedIDs, tileID);
    }

    /*
    This method was later adjusted because I changed the logic so that this method is called after the player first
    clicks on a tile. This way, the clicked Tile, and the adjacent tiles can be excluded from the randomBombID set.
     */
    private static Set<Integer> fillRandomBombIDSet(int numberOfBombs, int totalTiles,
                                                    List<Integer> excludedIDs, int tileID) {
        Set<Integer> bombIDs = new TreeSet<>();
        do {
            int randomID = (int) (Math.random() * (totalTiles));
            if (randomID != tileID && !excludedIDs.contains(randomID)) bombIDs.add(randomID);
        } while (bombIDs.size() < numberOfBombs);
        return bombIDs;
    }

    /*
    The getAdjacentBombs method loops through the list with adjacent tiles, obtained from the getAdjacentTiles method,
    and for each Tile in the list, determines if it is a bomb tile. If so, it increments the bomb count. finally it
    returns the total bomb count.
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
        List<Tile> allTiles = gamefield.getGameFieldTiles();
        List<Tile> adjacentTiles = new ArrayList<>();
        int totalTiles = allTiles.size();
        for (int adjacentTileID : getAdjacentTileIDs(gamefield.getDifficulty(), tileID)) {
            if (adjacentTileID >= 0 && adjacentTileID < totalTiles) {
                adjacentTiles.add(allTiles.get(adjacentTileID));
            }
        }
        return adjacentTiles;
    }

    /*
    The getAdjacentTileIDs method returns a list containing the IDs of the tiles adjacent to the Tile with the given
    tileID. It needs the gameField as argument to determine the difficulty. Then it determines the IDs based on a
    position vector. Adding the number of tiles per row to the current tileID returns the tileID of the Tile right below
    the current tile. The other vectors are based on the same logic. If the current tile is on the first or last column
    it will have only 5 adjacent tiles instead of 8. So this must be checked.
     */
    public static List<Integer> getAdjacentTileIDs(DifficultyLevel difficultyLevel, int tileID) {
        int tilesPerRow = difficultyLevel.getFieldLengthInTiles();
        List<Integer> adjacentTileIds;
        if (isFirstColumn(difficultyLevel, tileID)) {
            adjacentTileIds = Arrays.asList(tileID + 1, tileID - tilesPerRow, tileID - tilesPerRow + 1,
                    tileID + tilesPerRow, tileID + tilesPerRow + 1);
        } else if (isLastColumn(difficultyLevel, tileID)) {
            adjacentTileIds = Arrays.asList(tileID - 1, tileID - tilesPerRow - 1,
                    tileID - tilesPerRow, tileID + tilesPerRow,
                    tileID + tilesPerRow - 1);
        } else {
            adjacentTileIds = Arrays.asList(tileID - 1, tileID + 1, tileID - tilesPerRow - 1,
                    tileID - tilesPerRow, tileID - tilesPerRow + 1, tileID + tilesPerRow,
                    tileID + tilesPerRow - 1, tileID + tilesPerRow + 1);
        }
        return adjacentTileIds;
    }

    /*
    isFirstColumn determines if the given tileID is on the first column
     */
    private static boolean isFirstColumn(DifficultyLevel difficultyLevel, int tileID) {
        int tilesPerRow = difficultyLevel.getFieldLengthInTiles();
        return tileID % tilesPerRow == 0;
    }

    /*
    isLastColumn determines if the given tileID is on the last column
     */
    private static boolean isLastColumn(DifficultyLevel difficultyLevel, int tileID) {
        int tilesPerRow = difficultyLevel.getFieldLengthInTiles();
        return (tileID + 1) % tilesPerRow == 0;
    }
}
