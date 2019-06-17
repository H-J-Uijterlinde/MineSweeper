package com.minesweeper.Tile;

public class BombTile extends Tile {

    public BombTile(int tileID) {
        super(tileID);
        isBomb = true;
    }

    @Override
    public String toString() {
        return "B";
    }
}
