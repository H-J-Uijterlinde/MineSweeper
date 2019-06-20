package com.minesweeper.Tile;

/*
The BombTile class extends Tile. The benefit of this design choice is that it makes it clearer when a BombTile is
being created versus when a NormalTile is created, so it enhances the readability of the code. Also it allows us to
override the toString method. This comes in handy during debugging.
 */

public class BombTile extends Tile {

    public BombTile(int tileID) {
        super(tileID, true);
    }

    @Override
    public String toString() {
        return "B ";
    }
}
