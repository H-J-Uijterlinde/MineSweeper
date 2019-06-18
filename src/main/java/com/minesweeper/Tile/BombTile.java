package com.minesweeper.Tile;

public class BombTile extends Tile {

    public BombTile(int tileID) {
        super(tileID, true);
    }

    @Override
    public String toString() {
        return "B ";
    }
}
