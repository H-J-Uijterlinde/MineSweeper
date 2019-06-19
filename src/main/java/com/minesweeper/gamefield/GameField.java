package com.minesweeper.gamefield;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.Tile.Tile;
import java.util.List;

import static com.minesweeper.gamefield.CreateGameFieldFunctions.createGameTiles;

/*
The GameField class represents an abstraction of the game field. The state of a GameField object contains a list of all
the tiles, and a DifficultyLevel. The object also contains a static reference to itself, which is no longer used.
I created this class with the idea of implementing the singleton pattern, because there should only exist one GameField
object throughout the entire application.

The singleton pattern describes that a GameField object can only be created through a factory method, which first checks
if the static reference variable already points to an existing GameField object, if so, it will just return a reference
to that object. The factory method will only return a new object when the static reference variable is set to null.

However the singleton pattern turned out to be a suboptimal design choice because it prevented me from starting a new
game, The getGame method would just return the exact same GameField object. So I had to alter the class which no longer
makes it a singleton. It is still immutable.
 */

public class GameField {

    private static GameField currentGame;
    private List<Tile> gameFieldTiles;
    private DifficultyLevel difficulty;

    private GameField(DifficultyLevel difficultyLevel) {
        this.gameFieldTiles = setGameFieldTiles(difficultyLevel);
        this.difficulty = difficultyLevel;
    }

    /*
    The getGame method was originally introduced to ensure that only one GameField object could exist. Its a static
    factory method. I left it in for illustration purposes.
     */

    public static GameField getGame(DifficultyLevel difficultyLevel) {
        if (currentGame == null) {
            currentGame = new GameField(difficultyLevel);
            return currentGame;
        }
        else return currentGame;
    }

    public static GameField restartGame(DifficultyLevel difficultyLevel) {
        currentGame = new GameField(difficultyLevel);
        return currentGame;
    }

    /*
    The setGameFieldTiles returns the list of tiles which make a game field. It calls upon a static method of the
    CreateGameFieldFunctions class, which does the actual work. In hindsight the setGameFieldTiles method here is
    probably redundant. I left it in for illustration purposes, mainly to be better able to discuss design choices.
     */
    private List<Tile> setGameFieldTiles(DifficultyLevel difficultyLevel) {
        return createGameTiles(difficultyLevel);
    }

    public List<Tile> getGameFieldTiles() {
        return gameFieldTiles;
    }

    public DifficultyLevel getDifficulty() {
        return difficulty;
    }

    /*
    The to string method is overridden mainly for debugging purposes. It prints an abstraction of the game field to the
    console.
     */

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
