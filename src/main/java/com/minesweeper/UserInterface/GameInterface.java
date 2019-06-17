package com.minesweeper.UserInterface;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.gamefield.GameField;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameInterface {

    private final int NUMBER_OF_ROWS;
    private final int NUMBER_OF_COLUMNS;
    private final int NUMBER_OF_TILES;

    public GameInterface(GameField gameField) {
        NUMBER_OF_ROWS = gameField.getDifficulty().getFieldWidthInTiles();
        NUMBER_OF_COLUMNS = gameField.getDifficulty().getFieldLengthInTiles();
        NUMBER_OF_TILES = gameField.getDifficulty().getNumberOfTiles();
        JFrame gameFieldUserInterface = new JFrame("Mine Sweeper Project");
        gameFieldUserInterface.setIconImage(new ImageIcon("assets/images/bombjpg.jpg").getImage());
        gameFieldUserInterface.setLayout(new BorderLayout());
        Dimension GAME_FIELD_DIMENSION = new Dimension(800, 800);
        gameFieldUserInterface.setSize(GAME_FIELD_DIMENSION);
        gameFieldUserInterface.add(new TileContainer(), BorderLayout.CENTER);
        gameFieldUserInterface.setLocationRelativeTo(null);
        gameFieldUserInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFieldUserInterface.setVisible(true);
    }

    private class TileContainer extends JPanel {

        List<TileUserInterface> gameFieldTiles;

        TileContainer() {
            super(new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS));
            gameFieldTiles = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_TILES; i++) {
                TileUserInterface tile = new TileUserInterface(this, i);
                gameFieldTiles.add(tile);
                add(tile);
            }
            setPreferredSize(new Dimension(600, 600));
            validate();

        }
    }

    private static class TileUserInterface extends JPanel {
        private int tileID;

        TileUserInterface(TileContainer tileContainer, int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            setPreferredSize(new Dimension(5,5));
            setBackground(Color.darkGray);
            setBorder(BorderFactory.createRaisedBevelBorder());
        }

    }
}
