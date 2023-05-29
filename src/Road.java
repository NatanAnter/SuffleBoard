import javax.swing.*;
import java.awt.*;

public class Road extends JPanel {
    public static final double NORMAL_FRICTION_K = 4;
    public static final double ICE_FRICTION_K = 0.5;
    public static final int NUMBER_OF_DISKETTES_PER_PLAYER = 4;
    public static final int SPACE_BETWEEN_DISKETTES = Diskette.SIZE * 27 / 20;
    public static final Point STARTING_POINT = new Point(Window.WINDOW_WIDTH / 2 - Diskette.SIZE / 2, Window.WINDOW_HEIGHT * 6 / 7);
    public static final int SCORE_AREA_SIZE = 100;
    private double frictionK;
    private int count;
    private int[] xPoints;
    private int[] yPoints;
    private Diskette[] diskettes;
    private boolean isRedFirstPlayer;
    private Arrow arrow;

    public Diskette[] getDiskettes() {
        return this.diskettes;
    }

    public void iceRoad() {
        this.frictionK = ICE_FRICTION_K;
    }

    public void normalRoad() {
        this.frictionK = NORMAL_FRICTION_K;
        System.out.println("normal friction");
    }

    public Road(int[] xPoints, int[] yPoints, boolean isRedFirstPlayer) {
        normalRoad();
        this.isRedFirstPlayer = isRedFirstPlayer;
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        if (this.isRedFirstPlayer)
            this.count = 9;
        else
            this.count = 8;
        this.diskettes = new Diskette[NUMBER_OF_DISKETTES_PER_PLAYER * 2];
        for (int i = 0; i < NUMBER_OF_DISKETTES_PER_PLAYER * 2; i += 2) {
            this.diskettes[i] = new Diskette(Color.RED, xPoints[0] + ((i / 2 + 1) * SPACE_BETWEEN_DISKETTES - Diskette.SIZE), yPoints[0] - Diskette.SIZE * 2);
            this.diskettes[i].start();
        }
        for (int i = 1; i < NUMBER_OF_DISKETTES_PER_PLAYER * 2; i += 2) {
            this.diskettes[i] = new Diskette(Color.BLUE, xPoints[3] - ((i + 1) / 2 * SPACE_BETWEEN_DISKETTES), yPoints[0] - Diskette.SIZE * 2);
            this.diskettes[i].start();
        }
    }

    public void reSetGame() {
        this.isRedFirstPlayer = !this.isRedFirstPlayer;
        if (this.isRedFirstPlayer)
            this.count = 9;
        else
            this.count = 8;
        for (int i = 0; i < NUMBER_OF_DISKETTES_PER_PLAYER * 2; i += 2) {
            this.diskettes[i].setXY(xPoints[0] + ((i / 2 + 1) * SPACE_BETWEEN_DISKETTES - Diskette.SIZE), yPoints[0] - Diskette.SIZE * 2);
        }
        for (int i = 1; i < NUMBER_OF_DISKETTES_PER_PLAYER * 2; i += 2) {
            this.diskettes[i].setXY(xPoints[3] - ((i + 1) / 2 * SPACE_BETWEEN_DISKETTES), yPoints[0] - Diskette.SIZE * 2);
        }
    }

    public int calculateScore(int diskette) {
        double y = this.diskettes[diskette].getY();
        double x = this.diskettes[diskette].getX();
        int size = this.diskettes[diskette].getSize();
        int endBoardTop = GameScene.DEFAULT_Y_POINTS[1];
        int endBoardLeft = GameScene.DEFAULT_X_POINTS[0];
        int endBoardRight = GameScene.DEFAULT_X_POINTS[3];
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
            return (int) (diskettes[count].getEndTimeInSecond() * 1000);
        return 0;
    }

    public void moveCurrentDisketteXY(int x, int y) {
        this.diskettes[count].addX(x);
        this.diskettes[count].addY(y);
    }

    public int getCurrentDisketteX() {
        return (int) this.diskettes[count].getX();
    }

    public void moveArrowXY(int x, int y) {
        this.arrow.moveXY(x, y);
    }

    public void showArrow(Graphics graphics) {
        this.arrow.paint(graphics);
    }

    public void createArrow() {
        this.arrow = new Arrow(Window.WINDOW_WIDTH / 2, STARTING_POINT.getY() + Diskette.SIZE / 2, 100);
    }

    public int getCount() {
        return count;
    }

    public void changeAngle(boolean positiveAngle) {
        if (positiveAngle)
            this.arrow.addDegree();
        else this.arrow.takeDegree();
    }

    public void nextPlayer() {
        if (this.isRedFirstPlayer)
            count -= count % 2 * 3 + (count % 2 - 1);
        else
            count--;
        if (this.count >= 0 && this.count <= 7)
            this.diskettes[count].toStartMove(STARTING_POINT.getX(), STARTING_POINT.getY());
        else
            System.out.println("count out of bounds");
    }

    public boolean endGame() {
        if (this.isRedFirstPlayer && this.count != 1 || !this.isRedFirstPlayer && this.count != 0)
            return false;
        return allDiskettesNotMoving();
    }

    public boolean allDiskettesNotMoving() {
        if (diskettes[count].isMoving())
            return false;
        for (int i = 0; i < diskettes.length; i++) {
            if (diskettes[i].isMoving()) {
                System.out.println("the diskette that moves is:" + i);
                return false;
            }
        }
        return true;
    }

    public boolean fieldInRest() {
        if (this.count > NUMBER_OF_DISKETTES_PER_PLAYER * 2 - 1)
            return true;
        if (diskettes[count].isMovingInGame())
            return false;
        for (int i = 0; i < diskettes.length; i++) {
            if (diskettes[i].isMovingInGame()) {
                return false;
            }
        }
        return true;
    }

    public int[] calculateScores() {
        int[] scores = new int[2];
        for (int i = 0; i < diskettes.length; i++) {
            if (diskettes[i].getColor() == Color.RED)
                scores[0] += calculateScore(i);
            else
                scores[1] += calculateScore(i);
        }
        return scores;
    }
     public void bomb(){
        this.diskettes[count].bomb();
     }
    public void notBomb(){
        for (Diskette d:diskettes) {
            d.cancelBomb();
        }
    }

    public String getInformation() {
        String str = "<html>";
        try {
            for (int i = 0; i < 8; i++) {
                str+=diskettes[i].getMass() + ", ";
//            str += "<br>" + i + ":   ";
//            str += " speed: " + Utils.calculateCurrentSpeed(this.diskettes[i].getSpeedPixelsPerSecond(), this.diskettes[i].getTime(), this.diskettes[i].getAcceleration());
//            str += "<br> angle: " + this.diskettes[i].getAngleInDegrees();
//            str += "<br> time: " + this.diskettes[i].getTime();
//            str += "<br> x: " + this.diskettes[i].getX();
//            str += "<br> y: " + this.diskettes[i].getY();
//            str += "<br> status: " + this.diskettes[i].getStatus();
//            str += "<br>";
            }
        }
        catch (Exception e){

        }

        return str + "</html>";
    }

    public void shoot(double speed, double angelInDegrees) {
        this.diskettes[count].inGameMoveSpeedAndAngle(speed, angelInDegrees, frictionK);
    }

    public double getArrowAngel() {
        return this.arrow.getDegrees();
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.fillPolygon(new Polygon(xPoints, yPoints, this.xPoints.length));
        graphics.setColor(Color.BLACK);
        graphics.fillRect(GameScene.DEFAULT_X_POINTS[1], GameScene.DEFAULT_Y_POINTS[1] + SCORE_AREA_SIZE, GameScene.DEFAULT_X_POINTS[3] - GameScene.DEFAULT_X_POINTS[1], 3);
        graphics.fillRect(GameScene.DEFAULT_X_POINTS[1], GameScene.DEFAULT_Y_POINTS[1] + SCORE_AREA_SIZE * 2, GameScene.DEFAULT_X_POINTS[3] - GameScene.DEFAULT_X_POINTS[1], 3);
        graphics.fillRect(GameScene.DEFAULT_X_POINTS[1], GameScene.DEFAULT_Y_POINTS[1] + SCORE_AREA_SIZE * 4, GameScene.DEFAULT_X_POINTS[3] - GameScene.DEFAULT_X_POINTS[1], 3);
        for (int i = 0; i < NUMBER_OF_DISKETTES_PER_PLAYER * 2; i++) {
            this.diskettes[i].paint(graphics);
        }
    }

    public void StopCollision(int d1, int d2) {
        diskettes[d1].rest();
        diskettes[d2].rest();
        double distance = Math.sqrt(Math.pow(diskettes[d2].getXCenter() - diskettes[d1].getXCenter(), 2) + Math.pow(diskettes[d2].getYCenter() - diskettes[d1].getYCenter(), 2));
        double overLapping = (diskettes[d1].getSize() / 2.0 + diskettes[d2].getSize() / 2.0 - distance) / 2.0;
        diskettes[d1].addX(overLapping / 2 * (diskettes[d1].getXCenter() - diskettes[d2].getXCenter()) / distance);
        diskettes[d1].addY(overLapping / 2 * (diskettes[d1].getYCenter() - diskettes[d2].getYCenter()) / distance);
        diskettes[d2].addX(overLapping / 2 * (diskettes[d2].getXCenter() - diskettes[d1].getXCenter()) / distance);
        diskettes[d2].addY(overLapping / 2 * (diskettes[d2].getYCenter() - diskettes[d1].getYCenter()) / distance);
//        diskettes[d1].addX(diskettes[d1].getX()-diskettes[d2].getX());
//        diskettes[d1].addY(diskettes[d1].getY()-diskettes[d2].getY());
//        diskettes[d2].addX(diskettes[d2].getX()-diskettes[d1].getX());
//        diskettes[d2].addY(diskettes[d2].getY()-diskettes[d1].getY());
    }

    public boolean thereIsCollision(int d1, int d2) {
        return Diskette.checkCollisionBetweenCircles(this.diskettes[d1], this.diskettes[d2]);
    }

    public boolean isRedNowPlaying() {
        return this.count % 2 == 1;
    }

    public void calculateCollision(int d1, int d2) {
        Diskette.calculateCollision(this.diskettes[d1], this.diskettes[d2], frictionK);
    }
}
