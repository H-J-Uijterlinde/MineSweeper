package com.minesweeper.UserInterface;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.gamefield.CreateGameFieldFunctions;
import com.minesweeper.gamefield.GameField;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
The GameInterface Class is where the GUI will be created, based on an abstraction of our gamefield created by our
game engine. The GameInterface class will contain private innerclasses for all the GUI components that are wanted.
Using inner classes is convenient because they can access GameInterface instance variables, which they will often need.
 */

//Todo display number of tiles left to click

public class GameInterface {

    private int NUMBER_OF_ROWS;
    private int NUMBER_OF_COLUMNS;
    private int NUMBER_OF_TILES;
    private int NUMBER_OF_BOMBS;
    private GameField gameField;
    private List<TileUserInterface> gameFieldTiles;
    private JLabel bombImage;
    private boolean gameOver;
    private JFrame gameFieldUserInterface;
    private TileContainer tileContainer;
    private int totalClickedtiles;

    public GameInterface(GameField gameField) {
        createGameSettings(gameField);
        createGameUserInterface();
        bombImage = setBombImage();
        gameOver = false;
    }

    /*
    The createGameUserInterface method creates a new JFrame top-level container, which will hold the other GUI elements
    we want in our game. The JFrame constructor sets the title. Next, a display icon, the layout, dimensions, position
    on the screen, en default close operations are set.
     */

    private void createGameUserInterface() {
        gameFieldUserInterface = new JFrame("Mine Sweeper Project");
        gameFieldUserInterface.setIconImage(new ImageIcon("assets/images/bombjpg.jpg").getImage());
        gameFieldUserInterface.setLayout(new BorderLayout());
        Dimension GAME_FIELD_DIMENSION = new Dimension(800, 800);
        gameFieldUserInterface.setSize(GAME_FIELD_DIMENSION);
        tileContainer = new TileContainer();
        gameFieldUserInterface.add(tileContainer, BorderLayout.CENTER);
        gameFieldUserInterface.setLocationRelativeTo(null);
        gameFieldUserInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFieldUserInterface.add(createMenu(), BorderLayout.NORTH);
        gameFieldUserInterface.setVisible(true);
    }

    /*
    The createGameSettings method determines the number of rows, columns and total tiles based on the abstract
    GameField created by the game engine. The engine is the nog GUI part of the application, where a gameboard is
    created based on the selected difficulty level.
     */

    private void createGameSettings(GameField gameField) {
        this.gameField = gameField;
        NUMBER_OF_ROWS = gameField.getDifficulty().getFieldWidthInTiles();
        NUMBER_OF_COLUMNS = gameField.getDifficulty().getFieldLengthInTiles();
        NUMBER_OF_TILES = gameField.getDifficulty().getNumberOfTiles();
        NUMBER_OF_BOMBS = gameField.getDifficulty().getNumberOfBombs();
    }

    /*
    In this next section the menu bar wil be created. The first step is creating a JMenuBar object, which can be added
    to the the top-level container. The JMenuBar can hold JMenu objects, which represent the different options the
    player sees in the menu bar.
     */

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(createOptionsMenu());
        return menu;
    }

    /*
    The createOptionsMenu method generates the first JMenu object to be added to the menu bar. The options menu will
    contain options for restarting the game, selecting difficulty and exiting the game. The JMenu is populated with
    JMenuItems. Each option is either a JMenuItem, or a JMenu on its own, in which case a submenu is created.
    To increase readability the creation of the select difficulty submenu is done in its own method. Listening for
    events like the player selection an option is explained in the create DifficultySubMenu method.
     */

    private JMenu createOptionsMenu() {
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem restartGameOption = new JMenuItem("Restart");
        restartGameOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK));
        JMenuItem exitOption = new JMenuItem("Exit");
        restartGameOption.addActionListener((e) -> tileContainer.resetGame());
        exitOption.addActionListener((e) -> System.exit(0));
        optionsMenu.add(restartGameOption);
        optionsMenu.addSeparator();
        optionsMenu.add(createDifficultySubMenu());
        optionsMenu.addSeparator();
        optionsMenu.add(exitOption);
        return optionsMenu;
    }

    /*
    The createDifficultySubmenu method is similar to the createOptionsMenu method in that it adds JMenu items to a
    JMenu. For each JMenu item it adds an actionListener. The addActionListener method of a JMenuItem takes an
    ActionListener as its argument. ActionListener is a functional interface, meaning it has only one abstract method.
    Functional interfaces can be implemented creating anonymous inner classes, with lamba expressions, or in some cases
    method references. The functional method of the ActionListener interface is actionPerformed, in this case it is
    implemented using lambda expressions.
     */

    private JMenu createDifficultySubMenu() {
        JMenu selectDifficultyMenu = new JMenu("Select difficulty");
        ButtonGroup difficultyButtonGroup = new ButtonGroup();
        JMenuItem beginnerButton = new JRadioButtonMenuItem("Beginner");
        beginnerButton.setSelected(true);
        difficultyButtonGroup.add(beginnerButton);
        JMenuItem intermediateButton = new JRadioButtonMenuItem("Intermediate");
        JMenuItem expertButton = new JRadioButtonMenuItem("Expert");
        beginnerButton.addActionListener((e) -> tileContainer.resetGame(DifficultyLevel.BEGINNER));
        intermediateButton.addActionListener((e) -> tileContainer.resetGame(DifficultyLevel.INTERMEDIATE));
        expertButton.addActionListener((e) -> tileContainer.resetGame(DifficultyLevel.EXPERT));
        difficultyButtonGroup.add(intermediateButton);
        difficultyButtonGroup.add(expertButton);
        selectDifficultyMenu.add(beginnerButton);
        selectDifficultyMenu.add(intermediateButton);
        selectDifficultyMenu.add(expertButton);
        return selectDifficultyMenu;
    }

    /*
    The Method setBombImage is added here, because caching the bomb image on starting the game gives great performance
    benefits. Otherwise the gameOverSequence method in the TileUserFace innerclass below, would have to perform I/O-
    operations for every bomb tile. I/O is relatively slow.
     */

    private JLabel setBombImage() {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("assets/images/bomb.png"));
            Image scaleImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaleImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setGameOver() {
        gameOver = true;
    }

    private void resetGameOver() {
        gameOver = false;
    }

    /*
    The main component of the GUI will be the playing field, this is represented by the TileContainer class. As the
    name implies the TileContainer class acts as a container holding the actual tiles. The tiles are spread out in a
    GridLayout, where the number of rows and columns are determined by the difficulty.
     */

    private class TileContainer extends JPanel {

        TileContainer() {
            super(new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS));
            addTiles();
            setPreferredSize(new Dimension(600, 600));
            validate();

        }

        /*
        The addTiles method adds tiles to the actual visible game field. It adds the number of tiles, specified earlier
        by the difficulty. It also adds the tiles to a list game tiles for future reference.
         */

        private void addTiles() {
            gameFieldTiles = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_TILES; i++) {
                TileUserInterface tile = new TileUserInterface(this, i);
                gameFieldTiles.add(tile);
                add(tile);
            }
        }

        /*
        The resetGame method removes all tiles from the tileContainer, it then starts a new game, generating a new
        random list of tiles. It also cals the addTiles method again, adding new TilesUserInterfaces to the container.
        There is also an overloaded version of resetGame which takes a DifficultyLevel as an argument. This makes it
        easier to implement an option with which a player can choose the desired difficulty.
         */

        void resetGame() {
            removeAll();
            DifficultyLevel currentDifficulty = gameField.getDifficulty();
            gameField = GameField.startGame(currentDifficulty);
            addTiles();
            validate();
            resetGameOver();
            totalClickedtiles = 0;
            System.out.println(gameField);
        }

        void resetGame(DifficultyLevel difficulty) {
            createGameSettings(GameField.startGame(difficulty));
            tileContainer.setLayout(new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS));
            removeAll();
            addTiles();
            validate();
            resetGameOver();
            totalClickedtiles = 0;
            System.out.println(gameField);
        }
    }

    /*
    The TileUserInterface class represents the actual visible tiles, the player can click on. It is called
    TileUserInterface to clearly distinguish between the GUI component and the abstraction which is just called Tile.
     */

    private class TileUserInterface extends JPanel {
        private int tileID;
        private boolean isClicked;
        private TileContainer container;

        TileUserInterface(TileContainer tileContainer, int tileID) {
            super(new GridBagLayout());
            this.tileID = tileID;
            this.isClicked = false;
            this.container = tileContainer;
            setTileAttributes(tileID);
        }

        private void setTileAttributes(int tileID) {
            setPreferredSize(new Dimension(5, 5));
            setBackground(Color.darkGray);
            setBorder(BorderFactory.createRaisedBevelBorder());
            determineMouseActions(tileID);
        }

        /*
        The determineMouseActions method determines what happens when the player clicks a mouse button, either left or
        right. addMouseListener takes a MouseListener object as its argument. In this case an anonymous inner class is
        created, which acts as a class implementing MouseListener, this allows us to implement the methods for the
        different mouse events.
         */

        private void determineMouseActions(int tileID) {
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    // determine if the user clicked right or left using static methods from SwingUtilities.

                    if (SwingUtilities.isRightMouseButton(e)) {
                        removeAll();
                        if (!isClicked() && !gameOver) setFlagIcon();
                    } else if (SwingUtilities.isLeftMouseButton(e)) {
                        if (!isClicked() && !gameOver) {
                            removeAll();
                            setClicked();
                            if (gameField.getGameFieldTiles().get(tileID).isBomb()) {
                                setBombIcon();
                                gameOverSequence(tileID);

                            } else {
                                setNumberOfBombsIcon(gameField, tileID);
                                determineVictory();
                            }
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        /*
        The setFlagIcon method is called when the player right clicks on a Tile. We could seperate this method like
        we did in the gameOverSequence method, but this method can not be called twice before first registering player
        input, which is also slow. So the I/O- operation here will have no noticable performance effects.
         */

        private void setFlagIcon() {
            try {
                BufferedImage image = ImageIO.read(new File("assets/images/flag.png"));
                Image scaleImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel jimage = new JLabel(new ImageIcon(scaleImage));
                add(jimage);
                validate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        The setNumberOfBombsIcon method determines which number icon to display after left clicking on a tile.
        It first calls on a function in the CreateGameFieldFunctions utility class to get the number of bombs in the
        eight adjacent tiles. If there is at least one bomb in the adjacent tielse, it obtains an image through
         an I/O operation were the url of the path is determined by the number of adjacent bombs.
         It also changes the border from raised to lowered for visual effect.

        If there are no bombs in the adjacent tiles, the method enters a recursive sequence. It will get a list
        of adjacent tiles that have not yet been clicked, and invoke itself on those tiles. This process repeats itself
        until an area is uncovered for which the border tiles all contain number icons.
         */

        private void setNumberOfBombsIcon(GameField gamefield, int tileID) {
            int numberOfAdjacentBombs = CreateGameFieldFunctions.getAdjacentBombs(gamefield, tileID);
            if (numberOfAdjacentBombs > 0) {
                addNumberIcon(numberOfAdjacentBombs);
            } else {
                setBorder(BorderFactory.createLoweredBevelBorder());
                List<TileUserInterface> adjacentTiles = getUnclickedAdjacentTiles(gamefield, tileID);

                for (TileUserInterface tile : adjacentTiles) {
                    tile.setNumberOfBombsIcon(gamefield, tile.tileID);
                }
            }
        }

        /*
        The getUnclickedAdjacentTiles method generates a list of actual tiles, adjacent to the current tile, with the
        unclicked status. It first obtains an array of IDs of adjacent tiles. Then it loops through the array,
        checking if the id is valid, if so the method checks if the corresponding tile is unclicked. If so, it adds
        the tile to the list, and changes the status of that tile to clicked.

        This last part is very important, if we dont change the clicked status, adjacent tiles with zero adjacent bombs
        would keep adding each other to their lists of adjacent tiles, resulting in an infinite number of recursive
        calls in the setNumberOfBombsIcon method, ending in a StackOverflow error.
         */

        private synchronized List<TileUserInterface> getUnclickedAdjacentTiles(GameField gamefield, int tileID) {
            int[] adjacentTileIDs = CreateGameFieldFunctions.getAdjacentTileIDs(gameField, tileID);
            int numTiles = gamefield.getDifficulty().getNumberOfTiles();
            List<TileUserInterface> adjacentUnclickedTiles = new ArrayList<>();
            for (int i : adjacentTileIDs) {
                if (i >= 0 && i < numTiles) {
                    TileUserInterface tile = gameFieldTiles.get(i);
                    if (!tile.isClicked()) {
                        tile.setClicked();
                        adjacentUnclickedTiles.add(tile);
                    }
                }
            }
            return adjacentUnclickedTiles;
        }

        // The addNumberIcon method is identical to the addFlag method.
        private void addNumberIcon(int numberOfAdjacentBombs) {
            try {
                BufferedImage image = ImageIO.read(new File("assets/images/" + numberOfAdjacentBombs + ".png"));
                Image scaleImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel jimage = new JLabel(new ImageIcon(scaleImage));
                add(jimage);
                setBorder(BorderFactory.createLoweredBevelBorder());
                validate();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        The determine if a player has successfully completed the game whe have to keep track of how many tiles the
        player has clicked on (only left clicks increase the counter), and compare that to the number of tiles that are
        not bombs. If the player successfully completed the game, the displayEndMessage method is called.
         */

        private void determineVictory() {
            int tilesToClick = NUMBER_OF_TILES - NUMBER_OF_BOMBS;
            if (tilesToClick - totalClickedtiles == 0) {
                setGameOver();
                displayEndMessage("assets/images/victory.png",
                        "Would you like to play again?",
                        "Victorious");
            }
        }

        /*
        The gameOverSequence method is called when the player clicks a bomb. It goes through all the tiles on the
        gameField and prints a bomb icon on the Tiles containing a bomb. This is a CPU heavy process. I tested the
        stream method against;

                        for (TileUserInterface tile : gameFieldTiles) {
                        int id = tile.getTileID();
                        if (clickedTileID != id && gameField.getGameFieldTiles().get(id).isBomb()) {
                            tile.setBombIcon();
                        }

         The stream was the clear winner. A new Thread is called, so the main Thread can maintain gamecontrol.
         The Thread.sleep is just for the visual effect.
         */

        private void gameOverSequence(int clickedTileID) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    gameFieldTiles.stream()
                            .filter(t -> gameField.getGameFieldTiles().get(t.getTileID()).isBomb())
                            .filter(t -> t.getTileID() != clickedTileID)
                            .forEach((t) -> {
                                t.removeAll();
                                t.setBombIcon();
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            });
            new Thread(() -> displayEndMessage("assets/images/boom.png",
                    "Would you like to play again?",
                    "Game over!")).start();
            t.start();
            setGameOver();
        }

        /*
        The displayEndMessage first creates an icon based on the url of path provided. It then creates a Yes/ No
        question dialog. The message displayed and the title of the dialog can be provided as parameters when calling
        this method.
         */

        private void displayEndMessage(String url, String message, String title) {
            BufferedImage image;
            try {
                image = ImageIO.read(new File(url));
                Image scaleImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon gameOverIcon = new ImageIcon(scaleImage);
                int n = JOptionPane.showConfirmDialog(gameFieldUserInterface,
                        message,
                        title,
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        gameOverIcon);
                if (n == 0) {
                    container.resetGame();
                } else if (n == 1) {
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void setBombIcon() {
            setClicked();
            add(bombImage);
            setBorder(BorderFactory.createLoweredBevelBorder());
            validate();
        }

        private int getTileID() {
            return tileID;
        }

        private boolean isClicked() {
            return isClicked;
        }

        void setClicked() {
            isClicked = true;
            totalClickedtiles++;
        }
    }
}
