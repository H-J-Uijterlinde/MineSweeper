package com.minesweeper.GameUtils;

public enum DifficultyLevel {
    BEGINNER(8, 8, 10),
    INTERMEDIATE(16, 16, 40),
    EXPERT(24, 16, 99);

    private int numberOfTiles;
    private int numberOfBombs;
    private int fieldLengthInTiles;

    DifficultyLevel(int fieldLengthInTiles, int fieldWidthInTiles, int numberOfBombs) {
        this.fieldLengthInTiles = fieldLengthInTiles;
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
}
