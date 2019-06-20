package com.minesweeper.Tile;

/*
The Tile class is an abstract class providing common behaviour for the different types of Tiles. In this case BombTile
and Normal Tile. The state of a Tile object contains an ID which corresponds with the ID of the GUI Tile component,
which is called TileUserInterface. It also contains a boolean value determining if the tile is a Bomb or not.
 */

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
