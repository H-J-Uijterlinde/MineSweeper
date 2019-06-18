package com.minesweeper.Tile;

public abstract class Tile {

    private boolean isBomb;
    private int tileID;

    Tile(int tileID, boolean isBomb) {
        this.tileID = tileID;
        this.isBomb = isBomb;
    }

    public int getTileID() {
        return tileID;
    }

    public boolean isBomb() {
        return this.isBomb;
    }

    @Override
    public String toString() {
        return "- ";
    }
}
