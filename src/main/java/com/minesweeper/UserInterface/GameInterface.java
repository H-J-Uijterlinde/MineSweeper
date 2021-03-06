package com.minesweeper.UserInterface;

import com.minesweeper.GameUtils.DifficultyLevel;
import com.minesweeper.HighScores.HighScore;
import com.minesweeper.HighScores.HighScoreUtils;
import com.minesweeper.gamefield.CreateGameFieldFunctions;
import com.minesweeper.gamefield.GameField;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
The GameInterface Class is where the GUI will be created, based on an abstraction of a GameField as created by our
game engine. The GameInterface class will contain private inner classes for all the GUI components that are needed.
Using inner classes is convenient because they can directly access GameInterface instance variables,
which they will often need.
 */

public class GameInterface {

    private DifficultyLevel difficultyLevel;
    private int NUMBER_OF_ROWS;
    private int NUMBER_OF_COLUMNS;
    private int NUMBER_OF_TILES;
    private int NUMBER_OF_BOMBS;
    private GameField gameField;
    private List<TileUserInterface> gameFieldTiles;
    private JLabel bombImage;
    private boolean gameOver;
    private boolean firstClick;
    private JFrame gameFieldUserInterface;
    private TileContainer tileContainer;
    private int totalClickedtiles;
    private displayScorePanel scorePanel;
    private HighScoreUtils highscores;
    private long timeElapsed;

    public GameInterface(DifficultyLevel difficultyLevel) {
        createGameSettings(difficultyLevel);
        createGameUserInterface();
        bombImage = setBombImage();
        gameOver = false;
        firstClick = true;
        highscores = HighScoreUtils.createHighScores();

    }

    /*
    The createGameUserInterface method creates a new JFrame top-level container, which will hold the other GUI elements
    we want in our game. The JFrame constructor sets the title. Next, a display icon, the layout, dimensions, position
    on the screen, en default close operations are set.
     */

    private void createGameUserInterface() {
        gameFieldUserInterface = new JFrame("Mine Sweeper Project");
        gameFieldUserInterface.setIconImage((imageLoader("images/boom.png")));
        gameFieldUserInterface.setLayout(new BorderLayout());
        Dimension GAME_FIELD_DIMENSION = new Dimension(800, 800);
        gameFieldUserInterface.setSize(GAME_FIELD_DIMENSION);
        tileContainer = new TileContainer();
        scorePanel = new displayScorePanel();
        gameFieldUserInterface.add(scorePanel, BorderLayout.SOUTH);
        gameFieldUserInterface.add(tileContainer, BorderLayout.CENTER);
        gameFieldUserInterface.setLocationRelativeTo(null);
        gameFieldUserInterface.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFieldUserInterface.add(createMenu(), BorderLayout.NORTH);
        gameFieldUserInterface.setVisible(true);
    }

    /*
    The createGameSettings method determines the number of rows, columns and total tiles based on the DifficultyLevel
    selected by the player.
     */

    private void createGameSettings(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        NUMBER_OF_ROWS = difficultyLevel.getFieldWidthInTiles();
        NUMBER_OF_COLUMNS = difficultyLevel.getFieldLengthInTiles();
        NUMBER_OF_TILES = difficultyLevel.getNumberOfTiles();
        NUMBER_OF_BOMBS = difficultyLevel.getNumberOfBombs();
    }

    /*
    In this next section the menu bar wil be created. The first step is creating a JMenuBar object, which can be added
    to the the top-level container. The JMenuBar can hold JMenu objects, which represent the different options the
    player sees in the menu bar.
     */

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        menu.add(createOptionsMenu());
        menu.add(createHighScoreMenu());
        menu.add(createHelpMenu());
        return menu;
    }

    /*
    The createHelpMenu adds a single JMenuItem to the Help menu. The how to play option contains an actionlistener,
    which will check if it can create a Desktop Object from the players system, and if so, it will call the browse
    action on the Desktop object to open a browser, and direct the player the the MineSweeper wiki page.
     */

    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        JMenuItem howToPlay = new JMenuItem("How to play?");
        howToPlay.addActionListener(e -> {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Minesweeper_(video_game)"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        helpMenu.add(howToPlay);
        return helpMenu;
    }

    /*
    The createOptionsMenu method generates the first JMenu object to be added to the menu bar. The options menu will
    contain options for restarting the game, selecting difficulty and exiting the game. The JMenu is populated with
    JMenuItems. Each option is either a JMenuItem, or a JMenu on its own, in which case a submenu is created.
    To increase readability the creation of the select difficulty submenu is done in its own method. Listening for
    events like the player selecting an option is explained in the create DifficultySubMenu method.
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
    Functional interfaces can be implemented either by creating anonymous inner classes, or with lamba expressions,
    or in some cases method references. The functional method of the ActionListener interface is actionPerformed, in
    this case it is implemented using a lambda expression.
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
    In the next section, the high score menu will be create. In the createHighScoreMenu method, JMenuItems for
    beginner, intermediate and expert high scores are added to a JMenu. The action listeners call the getHighScore
    method.
     */

    private JMenu createHighScoreMenu() {
        JMenu highScoreMenu = new JMenu("High Scores");
        JMenuItem beginnerHighScores = new JMenuItem("Beginner");
        beginnerHighScores.addActionListener(e -> getHighScores(DifficultyLevel.BEGINNER));
        JMenuItem intermediateHighScores = new JMenuItem("Intermediate");
        intermediateHighScores.addActionListener(e -> getHighScores(DifficultyLevel.INTERMEDIATE));
        JMenuItem expertHighScores = new JMenuItem("Expert");
        expertHighScores.addActionListener(e -> getHighScores(DifficultyLevel.EXPERT));
        highScoreMenu.add(beginnerHighScores);
        highScoreMenu.addSeparator();
        highScoreMenu.add(intermediateHighScores);
        highScoreMenu.addSeparator();
        highScoreMenu.add(expertHighScores);
        return highScoreMenu;
    }

    /*
    The getHighScores method consists of a switch statement, determining which list of high scores to display
     */

    private void getHighScores(DifficultyLevel difficultyLevel) {
        switch (difficultyLevel) {
            case BEGINNER: {
                displayHighScores(highscores.getBeginnerHighScores());
                break;
            }
            case INTERMEDIATE: {
                displayHighScores(highscores.getIntermediateHighScores());
                break;
            }
            case EXPERT: {
                displayHighScores(highscores.getExpertHighScores());
                break;
            }
        }
    }

    /*
    The displayHighScores method creates a JDialog. It adds a JPanel to the Dialog, and for each HighScore in the list
    of HighScores, it adds a JLabel containing the position on the leader board, the name of the player, and the time
    elapsed.
     */

    private void displayHighScores(List<? extends HighScore> highScores) {
        Collections.sort(highScores);
        JDialog highScoreDialog = new JDialog(gameFieldUserInterface, "Highscores");
        highScoreDialog.setSize(200, 325);
        highScoreDialog.setLocationRelativeTo(gameFieldUserInterface);
        JPanel highscorePanel = new JPanel();
        highscorePanel.setLayout(new BoxLayout(highscorePanel, BoxLayout.PAGE_AXIS));
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        createSingleHighScorePanel(highScores, highscorePanel);
        highScoreDialog.add(highscorePanel);
        highScoreDialog.validate();
        highScoreDialog.setVisible(true);
    }

    private void createSingleHighScorePanel(List<? extends HighScore> highScores, JPanel highscorePanel) {
        int position = 1;
        for (HighScore highScore : highScores) {
            JPanel singleScorePanel = new JPanel(new BorderLayout());
            singleScorePanel.setSize(175, 5);
            singleScorePanel
                    .add(new JLabel(" " + position + ". " + highScore.getPlayerName()), BorderLayout.WEST);
            singleScorePanel
                    .add(new JLabel(scorePanel.formatTimeElapsed(highScore.getTimeElapsed()) + " "), BorderLayout.EAST);
            singleScorePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            highscorePanel.add(singleScorePanel, BorderLayout.NORTH);
            position++;
        }
    }

    /*
    The Method setBombImage is added here, because caching the bomb image on starting the game gives great performance
    benefits. Otherwise the gameOverSequence method in the TileUserFace innerclass below, would have to perform I/O-
    operations for every bomb tile. I/O is relatively slow.

    I changed to way to get images to enable packaging the application into a single executable jar. I originally used
    the following method:

            try {
                BufferedImage image = ImageIO.read(new File("src/main/resources/images/bomb.png"));
                Image scaleImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                JLabel jimage = new JLabel(new ImageIcon(scaleImage));
                add(jimage);
                validate();
            } catch (IOException e) {
                e.printStackTrace();
            }

     However this requires changing the path before packaging, because the maven packaging will put the images in the
     target class. And the src directory is no longer on the classpath, so when running the jar the application will not
     be able to find the images.
     */

    private JLabel setBombImage() {
        Image image = imageLoader("images/bomb.png")
                .getScaledInstance(40, 40, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(image));
    }

    /*
    The imageLoader method was added later to facilitate packaging. It first obtains the context Classloader of the
    currently executing thread. A ClassLoader is either a java class, or an instance written in system native code,
    which is responsible for dynamically loading classes into your application when they are needed.

    The getResource method of a ClassLoader can take a path relative to the classpath. It recursively passes the method
    up the ClassLoader hierarchy until one of the ClassLoaders is able to find the specified path. It then returns a
    URL object which can be used for reading the resource. This methods determines the URL needed at runtime, meaning it
    will always find the requested resource.
     */

    private Image imageLoader(String pathAndFileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(pathAndFileName);
        try {
            if (url != null) {
                return ImageIO.read(url);
            }
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

    private void resetFirstClick() {
        firstClick = true;
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
        by the difficulty. It also adds the tiles to a list of game tiles for future reference. I used ArrayList
        because ArrayList maintains an order, based on the order in which items are put in. This ensures that the index
        we give the TileUserInterface corresponds to the index of the abstract Tile generated by the game engine.
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
        random list of tiles. It also calls the addTiles method again, adding new TileUserInterfaces to the container.
        There is also an overloaded version of resetGame which takes a DifficultyLevel as an argument. This makes it
        easier to implement an option with which a player can choose the desired difficulty.
         */

        void resetGame() {
            removeAll();
            addTiles();
            validate();
            resetGameOver();
            resetFirstClick();
            totalClickedtiles = 0;
            resetScorePanel();
        }

        void resetGame(DifficultyLevel difficultyLevel) {
            createGameSettings(difficultyLevel);
            tileContainer.setLayout(new GridLayout(NUMBER_OF_ROWS, NUMBER_OF_COLUMNS));
            removeAll();
            addTiles();
            validate();
            resetGameOver();
            resetFirstClick();
            totalClickedtiles = 0;
            resetScorePanel();
        }
    }

    private void resetScorePanel() {
        gameFieldUserInterface.remove(scorePanel);
        scorePanel = new displayScorePanel();
        gameFieldUserInterface.add(scorePanel, BorderLayout.SOUTH);
        gameFieldUserInterface.validate();
        scorePanel.displayScore();
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
                    determineMouseClickEvents(e, tileID);
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
         The determineMouseClickEvents method determines if the user clicked right or left using static methods from
         SwingUtilities. If the player right clicked on a tile that has not yet been clicked on, the setFlagIcon method
         is called. If the player left clicked on a tile, it first removes possible icons set earlier, then the clicked
         status is set to true, and the leftMouseClickEvents method is called.
         */

        private void determineMouseClickEvents(MouseEvent e, int tileID) {
            if (SwingUtilities.isRightMouseButton(e)) {
                removeAll();
                if (!isClicked() && !gameOver) setFlagIcon();
            } else if (SwingUtilities.isLeftMouseButton(e)) {
                determineFirstClick(tileID);
            }
        }

        /*
        The determineFirstClick method is very important. Because I chose to make it impossible to click a bomb on the
        first click, so as not to be game over because of just bad luck, the abstraction of the game field is set only
        after the first click. The first click of a player results in a call to the restartGame method of the GameField
        class. This method takes the difficulty level and the clicked tile's ID as parameters. The game engine, the not
        GUI part of the code, returns a GameField in which the clicked tile, and its adjacent tiles, cannot contain
        a bomb.
         */

        private void determineFirstClick(int tileID) {
            if (firstClick) {
                firstClick = false;
                gameField = GameField.restartGame(difficultyLevel, tileID);
            }
            if (!isClicked() && !gameOver) {
                removeAll();
                setClicked();
                leftMouseClickEvents(tileID);
            }
        }

        /*
        The leftMouseClickEvents method determines if the player clicked on a bombTile or not, and calls the
        corresponding methods.
         */

        private void leftMouseClickEvents(int tileID) {
            if (gameField.getGameFieldTiles().get(tileID).isBomb()) {
                setBombIcon();
                gameOverSequence(tileID);

            } else {
                setNumberOfBombsIcon(gameField, tileID);
                determineVictory();
            }
        }

        /*
        The setFlagIcon method is called when the player right clicks on a Tile. We could separate this method like
        we did in the gameOverSequence method, but this method can not be called twice before first registering player
        input, which is also slow. So the I/O- operation here will have no noticeable performance effects.
         */

        private void setFlagIcon() {
            Image image = imageLoader("images/flag.png")
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel jimage = new JLabel(new ImageIcon(image));
            add(jimage);
            validate();
        }

        /*
        The setNumberOfBombsIcon method determines which number icon to display after left clicking on a tile.
        It first calls on a function in the CreateGameFieldFunctions utility class to get the number of bombs in the
        eight adjacent tiles. If there is at least one bomb in the adjacent tiles, it obtains an image through
        an I/O operation were the url of the path is determined by the number of adjacent bombs.
        It also changes the border from raised to lowered for visual effect.

        If there are no bombs in the adjacent tiles, the method enters a recursive sequence. It will get a list
        of adjacent tiles that have not yet been clicked, and the setNumberOfBombsIcon method invokes itself on those
        tiles. This process repeats itself until an area is uncovered for which the border tiles all contain number
        icons.
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
        The getUnclickedAdjacentTiles method generates a list of actual tiles, adjacent to the current tile, that have
        not yet been clicked on. It first obtains an array of IDs of adjacent tiles. Then it loops through the array,
        checking if the id is valid, if so the method checks if the corresponding tile is unclicked. If so, it adds
        the tile to the list, and changes the status of that tile to clicked.

        This last part is very important, if we don't change the clicked status, adjacent tiles with zero adjacent bombs
        would keep adding each other to their list of adjacent tiles, resulting in an infinite number of recursive
        calls in the setNumberOfBombsIcon method, ending in a StackOverflow error.
         */

        private synchronized List<TileUserInterface> getUnclickedAdjacentTiles(GameField gamefield, int tileID) {
            List<Integer> adjacentTileIDs = CreateGameFieldFunctions.
                    getAdjacentTileIDs(gamefield.getDifficulty(), tileID);
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
            Image image = imageLoader("images/" + numberOfAdjacentBombs + ".png")
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel jimage = new JLabel(new ImageIcon(image));
            add(jimage);
            setBorder(BorderFactory.createLoweredBevelBorder());
            validate();
        }

        /*
        To determine if a player has successfully completed the game whe have to keep track of how many tiles the
        player has clicked on (only left clicks increase the counter), and compare that to the number of tiles that are
        not bombs. If the player successfully completed the game, the displayEndMessage method is called.
         */

        private void determineVictory() {
            int tilesToClick = NUMBER_OF_TILES - NUMBER_OF_BOMBS;
            if (tilesToClick - totalClickedtiles == 0) {
                setGameOver();
                displayEnterHigScore();
                displayEndMessage("images/victory.png",
                        "Would you like to play again?",
                        "Victorious");
            }
        }

        /*
        The displayEnterHighScore method first determines if the score is good enough to be added to the list of
        HighScores. If true, it creates a JDialog asking the player to enter his/ her name. If a name is entered,
        a new HighScore is added to the list.
         */
        private void displayEnterHigScore() {
            boolean isHighScore = highscores.determineIfHighScore(difficultyLevel, scorePanel.calculateTimeElapsed());
            if (isHighScore) {
                String playerName = (String) JOptionPane.showInputDialog(
                        gameFieldUserInterface,
                        "       You got a new highscore!\n"
                                + "        please enter your name: ",
                        "New HighScore!",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        null);

                if ((playerName != null) && (playerName.length() > 0)) {
                    highscores.addHighScore(difficultyLevel, timeElapsed, playerName);
                }
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

         The stream was the clear winner. A new Thread is called, so the main Thread can maintain game-control.
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
            new Thread(() -> displayEndMessage("images/boom.png",
                    "Would you like to play again?",
                    "Game over!")).start();
            t.start();
            setGameOver();
        }

        /*
        The displayEndMessage method first creates an icon based on the url of the path provided.
        It then creates a Yes/ No question dialog. The message displayed and the title of the dialog can be provided as
        parameters when calling this method.
         */

        private void displayEndMessage(String url, String message, String title) {
            Image image = imageLoader(url)
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon gameOverIcon = new ImageIcon(image);
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

    /*
    The displayScorePanel is a JPanel that holds another JPanel which displays the number of tiles the player still
    has to click on to win the game.
     */

    private class displayScorePanel extends JPanel {
        LocalTime startingTime;

        displayScorePanel() {
            super(new BorderLayout());
            setPreferredSize(new Dimension(200, 25));
            displayScore();
        }

        /*
        In order to keep track of the time that has elapsed since the start of the game, the DisplayScore method runs
        in a separate thread, and refreshes the ScoreLabel with every pass through the do/ while loop. In order to save
        stack space, the thread sleeps for 1 second on every run through the loop.
         */
        private void displayScore() {
            startingTime = LocalTime.now();
            Thread t = new Thread(() -> {
                do {
                    refreshScoreLabels();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!gameOver);
            });
            t.start();
        }

        private void refreshScoreLabels() {
            int tilesLeft = NUMBER_OF_TILES - NUMBER_OF_BOMBS - totalClickedtiles;
            removeAll();
            add(new JLabel("   Tiles left: " + tilesLeft), BorderLayout.WEST);
            add(new JLabel("Time elapsed: " + formatTimeElapsed(calculateTimeElapsed()) + "  "), BorderLayout.EAST);
            validate();
        }

        private long calculateTimeElapsed() {
            LocalTime currentTime = LocalTime.now();
            timeElapsed = Duration.between(startingTime, currentTime).getSeconds();
            return timeElapsed;
        }

        /*
        The formatTimeElapsed method formats the time elapsed into a more aesthetically pleasing String format.
        It distinguishes between times over or under 1 hour.
         */
        private String formatTimeElapsed(long secondsElapsed) {
            if (secondsElapsed >= 3600) {
                return String.format("%d:%02d:%02d", secondsElapsed / 3600,
                        (secondsElapsed % 3600) / 60, (secondsElapsed % 60));
            } else {
                return String.format("%02d:%02d", (secondsElapsed % 3600) / 60, (secondsElapsed % 60));
            }
        }
    }
}
