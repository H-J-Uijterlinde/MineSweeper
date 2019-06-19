package com.minesweeper.GameUtils;

/*
The enum Difficulty level provides the constant values a player can choose to set the difficulty. The creation of the
gamefield, both abstract and visual, is dependent on the selected Difficulty level. The enum contains a constructor
which sets the number of bombs and tiles, and the fieldlength and fieldwith, based on the selected constant.
The enum also provides methods to obtain those values.
 */

public enum DifficultyLevel {
    BEGINNER(8, 8, 10),
    INTERMEDIATE(16, 16, 40),
    EXPERT(24, 16, 99);

    private int numberOfTiles;
    private int numberOfBombs;
    private int fieldLengthInTiles;
    private int fieldWidthInTiles;

    DifficultyLevel(int fieldLengthInTiles, int fieldWidthInTiles, int numberOfBombs) {
        this.fieldLengthInTiles = fieldLengthInTiles;
        this.fieldWidthInTiles= fieldWidthInTiles;
        this.numberOfBombs = numberOfBombs;
        this.numberOfTiles = fieldLengthInTiles * fieldWidthInTiles;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public int getFieldLengthInTiles() {
        return fieldLengthInTiles;
    }

    public int getFieldWidthInTiles() {
        return fieldWidthInTiles;
    }
}
