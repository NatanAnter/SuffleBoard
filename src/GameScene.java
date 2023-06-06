import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class GameScene extends JPanel implements KeyListener, ActionListener, ChangeListener {
    public static final int[] DEFAULT_X_POINTS = {Window.WINDOW_WIDTH * 143 / 384, Window.WINDOW_WIDTH * 143 / 384, Window.WINDOW_WIDTH * 241 / 384, Window.WINDOW_WIDTH * 241 / 384};
    public static final int[] DEFAULT_Y_POINTS = {Window.WINDOW_HEIGHT, Window.WINDOW_HEIGHT * 7 / 101, Window.WINDOW_HEIGHT * 7 / 101, Window.WINDOW_HEIGHT};
    public static final String PLAY_CLASSIC_NAME = "classicGame.png";
    public static final String PLAY_MAGIC_NAME = "newVersion.png";
    public static final String RETURN_TO_HOME_NAME = "home.png";
    public static final int PLAY_BUTTONS_WIDTH = 400;
    public static final int PLAY_BUTTONS_HEIGHT = 400;
    public static final int WINNER_WIDTH = 400;
    public static final int WINNER_HEIGHT = 400;
    public static final int SPACING = 20;
    public static final double FRICTION_K = 0;
    public static final int NUMBER_OF_DISKETTES = 15;
    public static final String PLAYER_RED_WON_LOCATION = "redWon.png";
    public static final String PLAYER_BLUE_WON_LOCATION = "blueWon.png";
    private final BufferedImage playerRedWon;
    private final BufferedImage playerBlueWon;
    private Puck[] pucks;
    private final JButton playButton;
    private final JButton playMagicButton;
    private final JButton returnToHomeButton;
    private STATUS status;
    public static final int DEGREES_MAX = 90;
    public static final int DEGREES_MIN = -90;
    public static final int DISKETTE_MAX_MOVEMENT_RIGHT = DEFAULT_X_POINTS[3] - Puck.SIZE;
    public static final int DISKETTE_MAX_MOVEMENT_LEFT = DEFAULT_X_POINTS[0];
    private boolean[] pressedKeys;
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private Board board;
    private Power power;
    private final ScoreArea scoreArea;
    private final JLabel winnerPlayerLabel;
    private final JLabel instructionsLabel;
    private boolean isRedFirstPlayer;
    public static boolean isGameWithMagic;
    private final IceCubeButton iceCubeButton;
    private final BombButton bombButton;
    private final Sounds sounds;
    private final MediaPlayer mediaPlayer;
    private boolean isRedWon;


    public void redPlayerWonStatus() {
        this.isRedWon = true;
        this.status = STATUS.playerWon;
        resetHome(Color.RED);

    }

    public void bluePlayerWonStatus() {
        this.isRedWon = false;
        this.status = STATUS.playerWon;
        resetHome(Color.BLUE);
    }


    public void resetHome(Color color) {
        isGameWithMagic = false;
        this.sounds.resetAndStopMusic();
        this.scoreArea.setScores(0, 0);
        if (this.status == STATUS.playerWon)
            this.sounds.playHomeWinnerMusic();
        else
            this.sounds.playHomeBackgroundMusic();
        this.pucks = new Puck[NUMBER_OF_DISKETTES];
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            this.pucks[i] = new Puck(100 * i, 150 * i, color);
            this.pucks[i].start();
            Utils.sleep(1);
        }
        this.iceCubeButton.setVisible(false);
        this.bombButton.setVisible(false);
        this.returnToHomeButton.setVisible(false);
        this.scoreArea.setVisible(false);
        this.mediaPlayer.setVisible(false);
        this.instructionsLabel.setVisible(false);
        this.playButton.setVisible(true);
        this.playMagicButton.setVisible(true);
    }

    public void paintRedWinner(Graphics graphics) {
        at(graphics, playerRedWon);
    }

    public void paintBlueWinner(Graphics graphics) {
        at(graphics, playerBlueWon);
    }

    private void at(Graphics graphics, BufferedImage playerBlueWon) {
        AffineTransform at = AffineTransform.getTranslateInstance(Window.WINDOW_WIDTH / 2.0 - WINNER_WIDTH / 2.0, Window.WINDOW_HEIGHT / 10.0);
        at.scale((WINNER_WIDTH + 0.0) / playerBlueWon.getWidth(), (WINNER_HEIGHT + 0.0) / playerBlueWon.getHeight());
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(playerBlueWon, at, null);
    }


    public GameScene() {
        this.playerRedWon = Utils.loadImage(PLAYER_RED_WON_LOCATION);
        this.playerBlueWon = Utils.loadImage(PLAYER_BLUE_WON_LOCATION);
        this.setBackground(Color.LIGHT_GRAY);
        this.scoreArea = new ScoreArea();
        this.mediaPlayer = new MediaPlayer();
        this.sounds = new Sounds(mediaPlayer.getSliderMusicVolume().getValue(), mediaPlayer.getSliderSoundVolume().getValue());
        this.sounds.playHomeBackgroundMusic();
        this.isRedFirstPlayer = false;
        this.pucks = new Puck[NUMBER_OF_DISKETTES];
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            this.pucks[i] = new Puck(100 * i, 150 * i, Color.GREEN);
            this.pucks[i].start();
            Utils.sleep(1);
        }

        //buttons:
        this.playButton = new JButton(Utils.loadButtonImage(PLAY_CLASSIC_NAME, PLAY_BUTTONS_WIDTH, PLAY_BUTTONS_HEIGHT));
        this.playButton.setBounds(Window.WINDOW_WIDTH / 2 - PLAY_BUTTONS_WIDTH - SPACING / 2, Window.WINDOW_HEIGHT / 2 - PLAY_BUTTONS_HEIGHT / 2, PLAY_BUTTONS_WIDTH, PLAY_BUTTONS_HEIGHT);
        this.playButton.addActionListener(this);
        this.playButton.setFocusable(false);
        this.playButton.setOpaque(false);
        this.playButton.setContentAreaFilled(false);
        this.playButton.setBorderPainted(false);
        this.add(playButton);

        this.playMagicButton = new JButton(Utils.loadButtonImage(PLAY_MAGIC_NAME, PLAY_BUTTONS_WIDTH, PLAY_BUTTONS_HEIGHT));
        this.playMagicButton.setBounds(Window.WINDOW_WIDTH / 2 + SPACING / 2, Window.WINDOW_HEIGHT / 2 - PLAY_BUTTONS_HEIGHT / 2, PLAY_BUTTONS_WIDTH, PLAY_BUTTONS_HEIGHT);
        this.playMagicButton.addActionListener(this);
        this.playMagicButton.setVisible(true);
        this.playMagicButton.setFocusable(false);
        this.playMagicButton.setOpaque(false);
        this.playMagicButton.setContentAreaFilled(false);
        this.playMagicButton.setBorderPainted(false);
        this.add(playMagicButton);

        this.returnToHomeButton = new JButton(Utils.loadButtonImage(RETURN_TO_HOME_NAME, MediaPlayer.WIDTH, MediaPlayer.HEIGHT));
        this.returnToHomeButton.setBounds(MediaPlayer.SPACE, MediaPlayer.Y + MediaPlayer.SPACE * 4 + MediaPlayer.HEIGHT * 2 + MediaPlayer.VOLUME_HEIGHT * 2, MediaPlayer.WIDTH, MediaPlayer.HEIGHT);
        this.returnToHomeButton.addActionListener(this);
        this.returnToHomeButton.setFocusable(false);
        this.returnToHomeButton.setVisible(false);
        this.add(returnToHomeButton);

        this.iceCubeButton = new IceCubeButton();
        this.iceCubeButton.getButton().addActionListener(this);
        this.iceCubeButton.getButton().setFocusable(false);
        this.iceCubeButton.getButton().setVisible(false);
        this.add(iceCubeButton.getButton());

        this.bombButton = new BombButton();
        this.bombButton.getButton().addActionListener(this);
        this.bombButton.getButton().setFocusable(false);
        this.bombButton.getButton().setVisible(false);
        this.add(bombButton.getButton());

        this.setFocusable(true);
        this.requestFocus();
        this.status = STATUS.homePage;
        this.addKeyListener(this);
        this.setLayout(null);

        this.instructionsLabel = new JLabel();
        this.instructionsLabel.setBounds(MediaPlayer.SPACE, MediaPlayer.Y + MediaPlayer.SPACE * 3 + MediaPlayer.HEIGHT + MediaPlayer.VOLUME_HEIGHT * 2, 320, MediaPlayer.HEIGHT);
        this.instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        this.add(this.instructionsLabel);
        this.add(scoreArea.getRedScoreLabel());
        this.add(scoreArea.getBlueScoreLabel());
        this.add(scoreArea.getRedSemiScoreLabel());
        this.add(scoreArea.getBlueSemiScoreLabel());
        this.winnerPlayerLabel = new JLabel();
        this.winnerPlayerLabel.setBounds(600, 70, 600, 60);
        this.winnerPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(winnerPlayerLabel);
        mainGameLoop();
    }

    public void showBoard() {
        this.status = STATUS.setUp;
        this.sounds.resetAndStopMusic();
        this.sounds.playGameFirstMusic();
        this.playMagicButton.setVisible(false);
        this.playButton.setVisible(false);
        this.winnerPlayerLabel.setVisible(false);
        this.returnToHomeButton.setVisible(true);
        this.pressedKeys = new boolean[4];
        this.setBackground(Color.LIGHT_GRAY);
        this.board = new Board(DEFAULT_X_POINTS, DEFAULT_Y_POINTS, this.isRedFirstPlayer);
        this.isRedFirstPlayer = !this.isRedFirstPlayer;
        this.board.createArrow();
        this.instructionsLabel.setVisible(true);
        this.scoreArea.setVisible(true);
        this.scoreArea.setScores(0, 0);
        Utils.sleep(10);
        this.pucks = null;
        this.board.initBoard();
        if (isGameWithMagic) {
            this.board.initBoardMagic();
            this.iceCubeButton.reset();
            this.bombButton.reset();
        }
        this.mediaPlayer.getButtonNext().addActionListener(this);
        this.mediaPlayer.getButtonNext().setFocusable(false);
        this.add(mediaPlayer.getButtonNext());
        this.mediaPlayer.getButtonPrevious().addActionListener(this);
        this.mediaPlayer.getButtonPrevious().setFocusable(false);
        this.add(mediaPlayer.getButtonPrevious());
        this.mediaPlayer.getButtonPlayPause().addActionListener(this);
        this.mediaPlayer.getButtonPlayPause().setFocusable(false);
        this.add(mediaPlayer.getButtonPlayPause());
        this.mediaPlayer.getButtonMusic().addActionListener(this);
        this.mediaPlayer.getButtonMusic().setFocusable(false);
        this.add(mediaPlayer.getButtonMusic());
        this.mediaPlayer.getSliderMusicVolume().setFocusable(false);
        this.mediaPlayer.getSliderMusicVolume().addChangeListener(this);
        this.add(mediaPlayer.getSliderMusicVolume());
        this.mediaPlayer.getButtonSound().addActionListener(this);
        this.mediaPlayer.getButtonSound().setFocusable(false);
        this.add(mediaPlayer.getButtonSound());
        this.mediaPlayer.getSliderSoundVolume().setFocusable(false);
        this.mediaPlayer.getSliderSoundVolume().addChangeListener(this);
        this.add(mediaPlayer.getSliderSoundVolume());
        this.mediaPlayer.setVisible(true);
        this.status = STATUS.playing;
    }

    public void paintGame(Graphics graphics) {
        this.board.paint(graphics);
        this.scoreArea.paintScores(graphics);

    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        switch (status) {
            case homePage -> {
                for (Puck puck : this.pucks) {
                    puck.paint(graphics);

                }

            }
            case playerWon -> {
                if (pucks != null) {
                    for (Puck puck : this.pucks) {
                        puck.paint(graphics);
                    }
                }
                if (isRedWon)
                    paintRedWinner(graphics);
                else
                    paintBlueWinner(graphics);
            }
            case disketteMovingToStart, playing, endGame -> paintGame(graphics);
            case choosePosition -> {
                paintGame(graphics);
                this.instructionsLabel.setText("<html>Press Right and Left arrows to move" +
                        "<br>Press Up and Down arrows to change direction " +
                        "<br>Press Enter or Space to choose speed.</html>");
                this.board.showArrow(graphics);
            }
            case prepareToShoot -> {
                paintGame(graphics);
                this.instructionsLabel.setText("<html>Press Enter or Space to shoot" +
                        "<br>Press BackSpace to return back.</html>");

                this.board.showArrow(graphics);
                this.power.paint(graphics);
            }
            case shooting -> {
                paintGame(graphics);
                this.power.paint(graphics);
            }

        }
//        label.setText(this.road.getInformation());

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            isGameWithMagic = false;
            showBoard();
        } else if (e.getSource() == playMagicButton) {
            isGameWithMagic = true;
            showBoard();
        } else if (e.getSource() == iceCubeButton.getButton()) {
            this.sounds.playFreezeSound();
            if (this.board.isRedNowPlaying())
                this.iceCubeButton.redClicked();
            else
                this.iceCubeButton.blueClicked();
            this.board.iceRoad();
        } else if (e.getSource() == bombButton.getButton()) {
            this.sounds.playBombSound();
            if (this.board.isRedNowPlaying())
                this.bombButton.redClicked();
            else
                this.bombButton.blueClicked();
            this.board.bomb();
        } else if (e.getSource() == mediaPlayer.getButtonNext()) {
            sounds.playNextMusic();
            this.mediaPlayer.playClicked();
        } else if (e.getSource() == mediaPlayer.getButtonPlayPause()) {
            mediaPlayer.playPausedClicked();
            sounds.playPauseMusic();
        } else if (e.getSource() == mediaPlayer.getButtonPrevious()) {
            sounds.playPreviousMusic();
            this.mediaPlayer.playClicked();
        }
        if (e.getSource() == mediaPlayer.getButtonMusic()) {
            mediaPlayer.buttonMusicClicked();
        }
        if (e.getSource() == mediaPlayer.getButtonSound()) {
            mediaPlayer.buttonSoundClicked();
        }
        if (e.getSource() == this.returnToHomeButton) {
            int answer = JOptionPane.showConfirmDialog(null, "Are you sure you want to return to home page? ", "natan the king", JOptionPane.YES_NO_OPTION);
            if (answer == 0) {
                resetHome(Color.GREEN);
                this.status = STATUS.homePage;
            }
        }
    }

    public void mainGameLoop() {
        new Thread(() -> {
            while (true) {
                switch (status) {
                    case homePage, playerWon -> handleHomeCollisions();
                    case disketteMovingToStart, prepareToShoot, shooting -> {
                        this.scoreArea.setSemiScores(board.calculateScores()[0], board.calculateScores()[1]);
                        handleCollisionsAndSounds();
                        if (isGameWithMagic) {
                            checkButtonsVisibility();
                        }
                    }
                    case choosePosition -> {
                        if (isGameWithMagic) {
                            checkButtonsVisibility();
                        }
                        int dx = 0;
                        if (this.pressedKeys[DOWN] && this.board.getArrowAngel() > DEGREES_MIN)
                            this.board.changeAngle(false);
                        if (this.pressedKeys[UP] && this.board.getArrowAngel() < DEGREES_MAX)
                            this.board.changeAngle(true);
                        if (this.pressedKeys[LEFT] && this.board.getCurrentPuckX() > DISKETTE_MAX_MOVEMENT_LEFT)
                            dx--;
                        if (this.pressedKeys[RIGHT] && this.board.getCurrentPuckX() < DISKETTE_MAX_MOVEMENT_RIGHT)
                            dx++;
                        this.board.moveCurrentPuckXY(dx, 0);
                        this.board.moveArrowXY(dx, 0);
                        handleCollisionsAndSounds();
                    }
                    case playing -> {
                        handleCollisionsAndSounds();
                        if (isGameWithMagic) {
                            checkButtonsVisibility();
                        }
                        if (this.board.endGame()) {
                            this.scoreArea.addScore(this.board.calculateScores()[0], this.board.calculateScores()[1]);
                            this.scoreArea.setSemiScores(board.calculateScores()[0], board.calculateScores()[1]);
                            Utils.sleep(3000);
                            if (this.scoreArea.getRedScore() >= 10 && this.scoreArea.getRedScore() > this.scoreArea.getBlueScore()) {
                                redPlayerWonStatus();
                            } else if (this.scoreArea.getBlueScore() >= 10 && this.scoreArea.getBlueScore() > this.scoreArea.getRedScore()) {
                                bluePlayerWonStatus();
                            } else
                                reSetGame();
                        }
                        if (this.board.boardInRest()) {
                            if (this.board.getCount() != 0 && isRedFirstPlayer || this.board.getCount() != 1 && !isRedFirstPlayer) {
                                this.board.nextPlayer();
                                disketteMovingToStartStatus();
                                this.board.createArrow();
                            }
                        }
                    }
                }
                Utils.sleep(10);
                this.sounds.playNextIfNeeded();
                this.repaint();
            }
        }).start();
    }

    public void checkButtonsVisibility() {
        if (this.status != STATUS.choosePosition && this.status != STATUS.prepareToShoot)
            iceCubeButton.setVisible(false);
        else if (this.board.isRedNowPlaying() && !this.iceCubeButton.isRedClicked())
            iceCubeButton.setVisible(true);
        else if (!this.board.isRedNowPlaying() && !this.iceCubeButton.isBlueClicked())
            iceCubeButton.setVisible(true);
        else
            iceCubeButton.getButton().setVisible(false);
        if (this.status != STATUS.choosePosition && this.status != STATUS.prepareToShoot)
            bombButton.setVisible(false);
        else if (this.board.isRedNowPlaying() && !this.bombButton.isRedClicked())
            bombButton.setVisible(true);
        else bombButton.setVisible(!this.board.isRedNowPlaying() && !this.bombButton.isBlueClicked());
    }

    public void reSetGame() {
        if (this.scoreArea.getRedScore() + this.scoreArea.getBlueScore() >= 20) {
            this.sounds.resetAndStopMusic();
            this.sounds.playGameFinalMusic();
        }
        this.isRedFirstPlayer = !isRedFirstPlayer;
        this.board.reSetGame();
    }

    public void handleHomeCollisions() {
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            for (int j = i + 1; j < NUMBER_OF_DISKETTES; j++) {
                if (Utils.checkCollisionBetweenCircles(pucks[i].getXCenter(), pucks[i].getYCenter(), pucks[i].getSize() / 2.0, pucks[j].getXCenter(), pucks[j].getYCenter(), pucks[j].getSize() / 2.0)) {
                    Puck.StopCollision(pucks[i], pucks[j]);
                    Puck.calculateCollision(pucks[i], pucks[j], FRICTION_K);
                    this.sounds.playHitNormalSounds();
                }
            }
        }
    }

    public void handleCollisionsAndSounds() {
        if (this.board.isNeedToPlayFallingSound()) {
            if (isGameWithMagic)
                this.sounds.playFallingMagicSounds();
            else
                this.sounds.playFallingNormalSounds();
        }
        for (int i = 0; i < 2 * Board.NUMBER_OF_PUCKS_PER_PLAYER; i++) {
            if (this.board.getPucks()[i].isPlaying()) {
                for (int j = i + 1; j < 2 * Board.NUMBER_OF_PUCKS_PER_PLAYER; j++) {
                    if (this.board.getPucks()[j].isPlaying() && this.board.thereIsCollision(i, j)) {
                        board.StopCollision(i, j);
                        board.calculateCollision(i, j);
                        if (isGameWithMagic && !board.isBomb())
                            this.sounds.playHitMagicSounds();
                        else if (isGameWithMagic && board.isBomb())
                            this.sounds.playBombCollisionSound();
                        else
                            this.sounds.playHitNormalSounds();

                    }
                }
            }
        }
    }

    public void shootingStatus() {
        this.status = STATUS.shooting;

        this.board.shoot(this.power.calculateSpeed(), 180 - this.board.getArrowAngel());
        this.status = STATUS.playing;
        this.power = null;
    }


    public void disketteMovingToStartStatus() {
        new Thread(() -> {
            int sleep = this.board.timeToWaite();
            Utils.sleep(sleep);
            if (status == STATUS.disketteMovingToStart) {
                this.status = STATUS.choosePosition;
                this.board.normalBoard();
                this.board.notBomb();
            }
        }).start();
        this.status = STATUS.disketteMovingToStart;
    }

    public void prepareToShootStatus() {
        this.status = STATUS.prepareToShoot;
        this.power = new Power();
        this.power.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_N) {
            this.sounds.playNextMusic();
            this.mediaPlayer.playClicked();

        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            this.sounds.playPauseMusic();
            this.mediaPlayer.playPausedClicked();

        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            this.sounds.playPreviousMusic();
            this.mediaPlayer.playClicked();

        }
        switch (status) {
            case homePage -> {

            }
            case choosePosition -> {
                Integer toPress = null;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    toPress = RIGHT;
                else if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    toPress = LEFT;
                else if (e.getKeyCode() == KeyEvent.VK_UP)
                    toPress = UP;
                else if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    toPress = DOWN;
                if (toPress != null) {
                    this.pressedKeys[toPress] = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    prepareToShootStatus();
                }
            }
            case prepareToShoot -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shootingStatus();
                    if (isGameWithMagic)
                        sounds.playShootingMagicSound();
                    else
                        sounds.playShootingNormalSound();
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    this.status = STATUS.choosePosition;
            }

        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        Integer toRelease = null;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            toRelease = RIGHT;
        else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            toRelease = LEFT;
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            toRelease = UP;
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            toRelease = DOWN;
        if (toRelease != null)
            this.pressedKeys[toRelease] = false;

    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == mediaPlayer.getSliderMusicVolume()) {
            sounds.setMusicVolume(mediaPlayer.getMusicVolume());
            mediaPlayer.musicVolumeChanged();
        }
        if (e.getSource() == mediaPlayer.getSliderSoundVolume()) {
            sounds.setSoundVolume(mediaPlayer.getSoundVolume());
            mediaPlayer.soundVolumeChanged();
        }
    }

    enum STATUS {
        setUp,
        homePage,
        disketteMovingToStart,
        choosePosition,
        prepareToShoot,
        shooting,
        playing,
        endGame,
        playerWon
    }
}