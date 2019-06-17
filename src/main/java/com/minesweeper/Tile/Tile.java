package com.minesweeper.Tile;

public abstract class Tile {

    boolean isBomb;
    int numBombsAtAdjacentTiles;
    private int tileID;

    protected Tile(int tileID) {
        this.tileID = tileID;
    }

    public int getTileID() {
        return tileID;
    }

    @Override
    public String toString() {
        return "- ";
    }
}
