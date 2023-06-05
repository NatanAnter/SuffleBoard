import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Board extends JPanel {
    public static final String BOARD_NAME = "board.png";
    public static final String BOARD_MAGIC_NAME = "boardMagic1.png";
    public static final String BOARD_FROZEN_NAME = "boardFrozen.png";
    public static final double NORMAL_FRICTION_K = 4;
    public static final double ICE_FRICTION_K = 0.5;
    public static final int NUMBER_OF_PUCKS_PER_PLAYER = 4;
    public static final int SPACE_BETWEEN_PUCKS = Puck.SIZE * 27 / 20;
    public static final Point STARTING_POINT = new Point(Window.WINDOW_WIDTH / 2 - Puck.SIZE / 2, Window.WINDOW_HEIGHT * 10 / 11);
    public static final int SCORE_AREA_SIZE = 100;
    private double frictionK;
    private int count;
    private final int[] xPoints;
    private final int[] yPoints;
    private final Puck[] pucks;
    private boolean isRedFirstPlayer;
    private Arrow arrow;
    private BufferedImage img;

    public Puck[] getPucks() {
        return this.pucks;
    }

    public void iceRoad() {
        this.frictionK = ICE_FRICTION_K;
        initBoardFrozen();
    }

    public void normalBoard() {
        this.frictionK = NORMAL_FRICTION_K;
        if (GameScene.isGameWithMagic)
            initBoardMagic();
        else
            initBoard();

    }

    public Board(int[] xPoints, int[] yPoints, boolean isRedFirstPlayer) {
        normalBoard();
        this.isRedFirstPlayer = isRedFirstPlayer;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        if (this.isRedFirstPlayer)
            this.count = NUMBER_OF_PUCKS_PER_PLAYER * 2 + 1;
        else
            this.count = NUMBER_OF_PUCKS_PER_PLAYER * 2;
        this.pucks = new Puck[NUMBER_OF_PUCKS_PER_PLAYER * 2];
        for (int i = 0; i < NUMBER_OF_PUCKS_PER_PLAYER * 2; i += 2) {
            this.pucks[i] = new Puck(Color.RED, xPoints[0] - SPACE_BETWEEN_PUCKS, yPoints[0] - ((i + 3) * SPACE_BETWEEN_PUCKS / 2));
            this.pucks[i].start();
        }
        for (int i = 1; i < NUMBER_OF_PUCKS_PER_PLAYER * 2; i += 2) {
            this.pucks[i] = new Puck(Color.BLUE, xPoints[3] + SPACE_BETWEEN_PUCKS - Puck.SIZE, yPoints[0] - ((i + 2) * SPACE_BETWEEN_PUCKS / 2));

            this.pucks[i].start();
        }
        initBoard();
    }

    public void reSetGame() {
        this.isRedFirstPlayer = !this.isRedFirstPlayer;
        if (this.isRedFirstPlayer)
            this.count = NUMBER_OF_PUCKS_PER_PLAYER * 2 + 1;
        else
            this.count = NUMBER_OF_PUCKS_PER_PLAYER * 2;
        for (int i = 0; i < NUMBER_OF_PUCKS_PER_PLAYER * 2; i += 2) {
            this.pucks[i].setXY(xPoints[0] - SPACE_BETWEEN_PUCKS, yPoints[0] - ((i + 3) * SPACE_BETWEEN_PUCKS / 2));
        }
        for (int i = 1; i < NUMBER_OF_PUCKS_PER_PLAYER * 2; i += 2) {
            this.pucks[i].setXY(xPoints[3] + SPACE_BETWEEN_PUCKS - Puck.SIZE, yPoints[0] - ((i + 2) * SPACE_BETWEEN_PUCKS / 2));
        }
    }

    public int calculateScore(int diskette) {
        if(pucks[diskette].getStatus()!= Puck.STATUS.restingInGame)
            return 0;
        double y = this.pucks[diskette].getY();
        double x = this.pucks[diskette].getX();
        int size = this.pucks[diskette].getSize();
        int endBoardTop = yPoints[1];
        int endBoardLeft = xPoints[0];
        int endBoardRight = xPoints[3];
        if (y < endBoardTop - size || x < endBoardLeft - size || x > endBoardRight) {
            return 0;
        }
        if (y <= endBoardTop || x > endBoardRight - size || x < endBoardLeft)
            return 4;
        if (y + size < endBoardTop + SCORE_AREA_SIZE)
            return 3;
        if (y + size < endBoardTop + SCORE_AREA_SIZE * 2)
            return 2;
        if (y + size < endBoardTop + SCORE_AREA_SIZE * 4)
            return 1;
        return 0;
    }

    public int timeToWaite() {
        if (this.count >= 0)
            return (int) (pucks[count].getEndTimeInSecond() * 1000);
        return 0;
    }

    public void moveCurrentPuckXY(int x, int y) {
        this.pucks[count].addX(x);
        this.pucks[count].addY(y);
    }

    public int getCurrentPuckX() {
        return (int) this.pucks[count].getX();
    }

    public void moveArrowXY(int x, int y) {
        this.arrow.moveXY(x, y);
    }

    public void showArrow(Graphics graphics) {
        this.arrow.paint(graphics);
    }

    public void createArrow() {
        this.arrow = new Arrow(Window.WINDOW_WIDTH / 2, STARTING_POINT.y() + Puck.SIZE / 2, 100);
    }

    public int getCount() {
        return count;
    }

    public void changeAngle(boolean positiveAngle) {
        if (positiveAngle)
            this.arrow.addDegree();
        else this.arrow.takeDegree();
    }

    public boolean isNeedToPlayFallingSound() {
        for (Puck puck : pucks) {
            if (puck.isNeedToPlayFallingSound()) {
                puck.noNeedToPlayFallingSound();
                return true;
            }
        }
        return false;
    }


    public void nextPlayer() {
        if (this.isRedFirstPlayer)
            count -= count % 2 * 3 + (count % 2 - 1);
        else
            count--;
        if (this.count >= 0 && this.count <= NUMBER_OF_PUCKS_PER_PLAYER * 2 - 1)
            this.pucks[count].toStartMove(STARTING_POINT.x(), STARTING_POINT.y());
        else
            System.out.println("count out of bounds");
    }

    public boolean endGame() {
        if (this.isRedFirstPlayer && this.count != 1 || !this.isRedFirstPlayer && this.count != 0)
            return false;
        return allPucksNotMoving();
    }

    public boolean allPucksNotMoving() {
        if (pucks[count].isMoving())
            return false;
        for (Puck puck : pucks) {
            if (puck.isMoving()) {
                return false;
            }
        }
        return true;
    }

    public boolean boardInRest() {
        if (this.count > NUMBER_OF_PUCKS_PER_PLAYER * 2 - 1)
            return true;
        if (pucks[count].isMovingInGame())
            return false;
        for (Puck puck : pucks) {
            if (puck.isMovingInGame()) {
                return false;
            }
        }
        return true;
    }

    public int[] calculateScores() {
        int[] scores = new int[2];
        for (int i = 0; i < pucks.length; i++) {
            if (pucks[i].getColor() == Color.RED)
                scores[0] += calculateScore(i);
            else
                scores[1] += calculateScore(i);
        }
        return scores;
    }

    public void bomb() {
        this.pucks[count].bomb();
    }

    public boolean isBomb() {
        return this.pucks[count].isBomb();
    }

    public void notBomb() {
        for (Puck d : pucks) {
            d.cancelBomb();
        }
    }

    public void shoot(double speed, double angelInDegrees) {
        this.pucks[count].inGameMoveSpeedAndAngle(speed, angelInDegrees, frictionK);
    }

    public double getArrowAngel() {
        return this.arrow.getDegrees();
    }

    public void paint(Graphics graphics) {
        paintBoard(graphics);
        for (int i = 0; i < NUMBER_OF_PUCKS_PER_PLAYER * 2; i++) {
            this.pucks[i].paint(graphics);
        }
    }

    public void paintBoard(Graphics graphics) {
        AffineTransform at = AffineTransform.getTranslateInstance(xPoints[1], yPoints[1]);
        double multiplierX = (xPoints[3] - xPoints[0] + 0.0) / img.getWidth();
        double multiplierY = (yPoints[3] - yPoints[2] + 0.0) / img.getHeight();
        at.scale(multiplierX, multiplierY);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(img, at, null);

    }

    public void initBoard() {
        this.img = Utils.loadImage(BOARD_NAME);
    }

    public void initBoardMagic() {
        this.img = Utils.loadImage(BOARD_MAGIC_NAME);
    }

    public void initBoardFrozen() {
        this.img = Utils.loadImage(BOARD_FROZEN_NAME);
    }

    public void StopCollision(int d1, int d2) {
        pucks[d1].rest();
        pucks[d2].rest();
        double distance = Math.sqrt(Math.pow(pucks[d2].getXCenter() - pucks[d1].getXCenter(), 2) + Math.pow(pucks[d2].getYCenter() - pucks[d1].getYCenter(), 2));
        double overLapping = (pucks[d1].getSize() / 2.0 + pucks[d2].getSize() / 2.0 - distance) / 2.0;
        pucks[d1].addX(overLapping / 2 * (pucks[d1].getXCenter() - pucks[d2].getXCenter()) / distance);
        pucks[d1].addY(overLapping / 2 * (pucks[d1].getYCenter() - pucks[d2].getYCenter()) / distance);
        pucks[d2].addX(overLapping / 2 * (pucks[d2].getXCenter() - pucks[d1].getXCenter()) / distance);
        pucks[d2].addY(overLapping / 2 * (pucks[d2].getYCenter() - pucks[d1].getYCenter()) / distance);
    }

    public boolean thereIsCollision(int d1, int d2) {
        return Puck.checkCollisionBetweenCircles(this.pucks[d1], this.pucks[d2]);
    }

    public boolean isRedNowPlaying() {
        return this.count % 2 == 1;
    }

    public void calculateCollision(int d1, int d2) {
        Puck.calculateCollision(this.pucks[d1], this.pucks[d2], frictionK);
    }
}
