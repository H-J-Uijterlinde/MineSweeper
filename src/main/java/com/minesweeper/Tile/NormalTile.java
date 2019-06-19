package com.minesweeper.Tile;

/*
NormalTile also extends Tile, see BombTile for the reasoning behind this design decision. It provides no new
functionality.
 */

public class NormalTile extends Tile {

    public NormalTile(int tileID) {
        super(tileID, false);
    }
}
