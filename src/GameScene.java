import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameScene extends JPanel implements KeyListener, ActionListener {
    public static final int[] DEFAULT_X_POINTS = {Window.WINDOW_WIDTH / 3, Window.WINDOW_WIDTH / 3, Window.WINDOW_WIDTH * 2 / 3, Window.WINDOW_WIDTH * 2 / 3};
    public static final int[] DEFAULT_Y_POINTS = {Window.WINDOW_HEIGHT, Window.WINDOW_HEIGHT / 8, Window.WINDOW_HEIGHT / 8, Window.WINDOW_HEIGHT};
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 50;
    public static final double FRICTION_K = 0;
    public static final int NUMBER_OF_DISKETTES = 30;
    private int blueScore;
    private int redScore;
    private Diskette[] diskettes;
    private JButton button;
    private STATUS status;
    public static final int DEGREES_MAX = 90;
    public static final int DEGREES_MIN = -90;
    public static final int DISKETTE_MAX_MOVEMENT_RIGHT = DEFAULT_X_POINTS[3] - Diskette.SIZE;
    public static final int DISKETTE_MAX_MOVEMENT_LEFT = DEFAULT_X_POINTS[0];
    private boolean[] pressedKeys;
    public static final int DOWN = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private Road road;
    private Power power;
    private JLabel winnerPlayerLabel;
    private JLabel redScoreLabel;
    private JLabel blueScoreLabel;
    private  JLabel instructionsLabel;
    private boolean isRedFirstPlayer;

    public void redPlayerWonStatus() {
        this.winnerPlayerLabel.setText("RED player Won!");
        this.winnerPlayerLabel.setVisible(true);
        resetHome(Color.red);

    }

    public void bluePlayerWonStatus() {
        this.winnerPlayerLabel.setText("BLUE player Won!");
        this.winnerPlayerLabel.setVisible(true);
        resetHome(Color.BLUE);
    }

    public void resetHome(Color color) {
        this.redScore = 0;
        this.blueScore = 0;
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            this.diskettes[i] = new Diskette(100 * i, 150 * i, color);
            this.diskettes[i].start();
            Utils.sleep(1);
        }
        Utils.sleep(10000);
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            this.diskettes[i] = new Diskette(100 * i, 150 * i, color);
        }
        this.instructionsLabel.setVisible(false);
        this.blueScoreLabel.setVisible(false);
        this.redScoreLabel.setVisible(false);
        this.button.setVisible(true);
        this.status = STATUS.playerWon;
    }

    public GameScene() {
        this.redScore = 0;
        this.blueScore = 0;
        this.isRedFirstPlayer = false;
        this.diskettes = new Diskette[NUMBER_OF_DISKETTES];
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            this.diskettes[i] = new Diskette(100 * i, 150 * i, Color.GREEN);
            this.diskettes[i].start();
            Utils.sleep(1);
        }


        this.button = new JButton();
        this.button.setBounds(Window.WINDOW_WIDTH / 2 - BUTTON_WIDTH / 2, Window.WINDOW_HEIGHT / 2 - BUTTON_HEIGHT / 2, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.button.addActionListener(this);
        this.button.setText("play");
        this.button.setFont(new Font("Arial", Font.BOLD, 50));
        this.button.setFocusable(false);
        this.add(button);
        this.setFocusable(true);
        this.requestFocus();
        this.status = STATUS.homePage;
        this.addKeyListener(this);
        mainGameLoop();
        this.setLayout(null);

        this.instructionsLabel = new JLabel();
        this.instructionsLabel.setBounds(DEFAULT_X_POINTS[0],DEFAULT_Y_POINTS[1]-100,DEFAULT_X_POINTS[3]-DEFAULT_X_POINTS[0],80);
        this.instructionsLabel.setFont(new Font("Arial",Font.PLAIN,20));
        this.add(this.instructionsLabel);
        this.redScoreLabel = new JLabel();
        this.redScoreLabel.setBounds(100, 70, 600, 60);
        this.redScoreLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(redScoreLabel);
        this.blueScoreLabel = new JLabel();
        this.blueScoreLabel.setBounds(1400, 70, 600, 60);
        this.blueScoreLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(blueScoreLabel);
        this.winnerPlayerLabel = new JLabel();
        this.winnerPlayerLabel.setBounds(600, 70, 600, 60);
        this.winnerPlayerLabel.setFont(new Font("Arial", Font.PLAIN, 40));
        this.add(winnerPlayerLabel);
    }

    public void showRoad() {
//        this.diskettes = null;

        this.button.setVisible(false);
        this.winnerPlayerLabel.setVisible(false);
        this.pressedKeys = new boolean[4];
        this.setBackground(Color.LIGHT_GRAY);
        this.road = new Road(DEFAULT_X_POINTS, DEFAULT_Y_POINTS, this.isRedFirstPlayer);
        this.isRedFirstPlayer = !this.isRedFirstPlayer;
        this.road.createArrow();
        this.instructionsLabel.setVisible(true);
        this.redScoreLabel.setVisible(true);
        this.blueScoreLabel.setVisible(true);
        this.status = STATUS.playing;

    }

    public void paintGame(Graphics graphics) {
        this.road.paint(graphics);
        this.redScoreLabel.setText("Red Score: " + redScore);
        this.blueScoreLabel.setText("Blue Score: " + blueScore);

    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        switch (status) {
            case homePage, playerWon -> {
                for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
                    this.diskettes[i].paint(graphics);
                }
                this.instructionsLabel.setText("");
            }
            case disketteMovingToStart, playing, endGame -> {
                paintGame(graphics);
            }
            case choosePosition -> {
                paintGame(graphics);
                this.instructionsLabel.setText("<html>Press Right and Left arrows to move"+
                        "<br>Press Up and Down arrows to change direction "+
                        "<br>Press Enter or Space to choose speed.</html>");
                this.road.showArrow(graphics);
            }
            case prepareToShoot -> {
                paintGame(graphics);
                this.instructionsLabel.setText("<html>Press Enter or Space to shoot"+
                        "<br>Press BackSpace to return back.</html>");

                this.road.showArrow(graphics);
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
        if (e.getSource() == button) {
            showRoad();
        }
    }

    public void mainGameLoop() {
        new Thread(() -> {
            while (true) {
                switch (status) {
                    case homePage -> {
                        handleHomeCollisions();
//                        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
//                            for (int j = i + 1; j < NUMBER_OF_DISKETTES; j++) {
//                                if (Utils.checkCollisionBetweenCircles(diskettes[i].getX() + diskettes[i].getSize() / 2.0, diskettes[i].getY() + diskettes[i].getSize() / 2.0, diskettes[i].getSize() / 2.0, diskettes[j].getX() + diskettes[j].getSize() / 2.0, diskettes[j].getY() + diskettes[j].getSize() / 2.0, +diskettes[j].getSize() / 2.0)) {
//                                    Diskette.calculateCollision(diskettes[i], diskettes[j], FRICTION_K);
//                                }
//                            }
//                        }
                    }
                    case disketteMovingToStart -> {
                        handleCollisions();
                    }
                    case choosePosition -> {
                        int dx = 0;
                        if (this.pressedKeys[DOWN] && this.road.getArrowAngel() > DEGREES_MIN)
                            this.road.changeAngle(false);
                        if (this.pressedKeys[UP] && this.road.getArrowAngel() < DEGREES_MAX)
                            this.road.changeAngle(true);
                        if (this.pressedKeys[LEFT] && this.road.getCurrentDisketteX() > DISKETTE_MAX_MOVEMENT_LEFT)
                            dx--;
                        if (this.pressedKeys[RIGHT] && this.road.getCurrentDisketteX() < DISKETTE_MAX_MOVEMENT_RIGHT)
                            dx++;
                        this.road.moveCurrentDisketteXY(dx, 0);
                        this.road.moveArrowXY(dx, 0);
                        Utils.sleep(10);
                        handleCollisions();
                    }
                    case prepareToShoot,shooting-> {
                        handleCollisions();
                    }
                    case playing -> {
                        handleCollisions();

                        //////////////////////////////////////////////////////////////////////////////////////////////////////

                        //////////////////////////////////////////////////////////////////////////////////////////////////////
                        if (this.road.endGame()) {
                            System.out.println("engGame");
                            this.redScore += this.road.calculateScores()[0];
                            this.blueScore += this.road.calculateScores()[1];
                            Utils.sleep(3000);
                            if (this.redScore >= 10 && this.redScore > this.blueScore) {
                                redPlayerWonStatus();
                            } else if (this.blueScore >= 10 && this.blueScore > this.redScore) {
                                bluePlayerWonStatus();
                            } else
                                reSetGame();
                        }
                        if (this.road.getCount() != 0 && isRedFirstPlayer ||this.road.getCount() !=1 &&!isRedFirstPlayer){
                            this.road.nextPlayer();
                            disketteMovingToStartStatus();
                            this.road.createArrow();
                            System.out.println(this.road.getCount()+"  "+isRedFirstPlayer);
                        }


                    }
                }
                this.repaint();

            }
        }).start();
    }
    public void reSetGame(){
        this.isRedFirstPlayer = !isRedFirstPlayer;
        this.road.reSetGame();
    }
    public void handleHomeCollisions(){
        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
            for (int j = i + 1; j < NUMBER_OF_DISKETTES; j++) {
                if (Utils.checkCollisionBetweenCircles(diskettes[i].getXCenter(), diskettes[i].getYCenter(), diskettes[i].getSize() / 2.0, diskettes[j].getXCenter() , diskettes[j].getYCenter() , +diskettes[j].getSize() / 2.0)) {
                    Diskette.StopCollision(diskettes[i], diskettes[j]);
                    Diskette.calculateCollision(diskettes[i], diskettes[j], FRICTION_K);

                }
            }
        }
    }
    public void handleCollisions(){
        for (int i = 0; i < 2 * Road.NUMBER_OF_DISKETTES_PER_PLAYER; i++) {
            if(this.road.getDiskettes()[i].isPlaying()) {
                for (int j = i + 1; j < 2 * Road.NUMBER_OF_DISKETTES_PER_PLAYER; j++) {
                    if (this.road.getDiskettes()[j].isPlaying() && this.road.thereIsCollision(i,j)) {
                        road.StopCollision(i, j);
                        road.calculateCollision(i, j);
                        System.out.printf("collision: %d, %d%n", i, j);
                    }
                }
            }
        }
    }

    public void shootingStatus() {
        this.status = STATUS.shooting;
        this.power.showPower();
        this.road.shoot(this.power.calculateSpeed(), 180 - this.road.getArrowAngel());
        this.status = STATUS.playing;
    }


    public void disketteMovingToStartStatus() {
        new Thread(() -> {
            int sleep = this.road.timeToWaite();
            Utils.sleep(sleep);
            this.status = STATUS.choosePosition;
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
        switch (status) {
            case homePage -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    showRoad();
                }
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)
                    prepareToShootStatus();
            }
            case prepareToShoot -> {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    shootingStatus();
                }
                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                    this.status = STATUS.choosePosition;
            }
            case playing -> {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
//                    this.road.nextPlayer();
//                    disketteMovingToStartStatus();
//                    this.road.createArrow();
//                }
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

    enum STATUS {
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
