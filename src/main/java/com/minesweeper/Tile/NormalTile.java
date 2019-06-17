package com.minesweeper.Tile;

public class NormalTile extends Tile {

    public NormalTile(int tileID) {
        super(tileID);
        this.isBomb = false;
    }
}
