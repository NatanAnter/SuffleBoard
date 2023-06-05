import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Puck extends Thread {
    public static final String PUCK_RED_NAME = "puckRed.png";
    public static final String PUCK_BLUE_NAME = "puckBlue.png";
    public static final String PUCK_DEFAULT_NAME = "puckDefault.png";
    public static final double EXPLODE_SPEED = Power.MAX_SPEED;
    public static final int SIZE = 50;
    public static final double SET_UP_SPEED_PX_PER_SECOND = 300;
    public static final Double SLEEP_IN_SECOND = 0.02;
    public static final int SLEEP_IN_MILLI_SECOND = (int) (SLEEP_IN_SECOND * 1000);
    private static final double MASS = 0.5;
    public static final double G = 9.8;
    private final String name;
    private int size;
    private final double mass;
    private Color color;
    private double x;
    private double y;
    private double xStartingPosition;
    private double yStartingPosition;
    private double xTarget;
    private double yTarget;
    private double speedPixelsPerSecond;
    private double angleInDegrees;
    private double distance;
    private STATUS status;
    private double endTimeInSecond;
    private double acceleration;
    private boolean isTimeRunning;
    private long startTime;
    private boolean isBomb;
    private boolean needToPlayFallingSound;
    private BufferedImage img;
    private double degrees;

    public STATUS getStatus() {
        return this.status;
    }

    public int getSize() {
        return size;
    }

    public double getAcceleration() {
        return acceleration;
    }


    public double getEndTimeInSecond() {
        return endTimeInSecond;
    }

    public boolean isPlaying() {
        return this.status == STATUS.movingInGame || this.status == STATUS.restingInGame;
    }

    public boolean isMoving() {
        return this.status == STATUS.movingInGame || this.status == STATUS.moveToStart || this.status == STATUS.setUp || this.status == STATUS.shoot || this.status == STATUS.restingOutOfFiled;
    }

    public boolean isMovingInGame() {
        return this.status == STATUS.movingInGame;
    }

    public Color getColor() {
        return color;
    }


    public Puck(int x, int y, Color color) {
        Random random = new Random();
        this.needToPlayFallingSound = false;
        this.isTimeRunning = true;
        this.size = random.nextInt(10, 80);
        this.speedPixelsPerSecond = random.nextDouble(50, 400);
        this.angleInDegrees = random.nextInt(0, 360);
        this.x = x;
        this.y = y;
        this.xStartingPosition = x;
        this.yStartingPosition = y;
        this.color = color;
        if (color == Color.GREEN) {
            int colorNumber = random.nextInt(3);
            if (colorNumber == 0)
                this.color = color;
            else if (colorNumber == 1) {
                this.color = Color.RED;
            } else {
                this.color = Color.BLUE;
            }
        }
        this.mass = size * 2;
        this.acceleration = 0;
        this.isBomb = false;
        this.status = STATUS.homePage;
        this.name = getFileName();
        init();
        reSetTime();
    }

    public void bomb() {
        this.isBomb = true;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void cancelBomb() {
        this.isBomb = false;
    }

    public String getFileName() {
        if (this.color == Color.RED)
            return PUCK_RED_NAME;
        if (this.color == Color.BLUE)
            return PUCK_BLUE_NAME;

        return PUCK_DEFAULT_NAME;

    }

    public Puck(Color color, int x, int y) {
        this.isTimeRunning = true;
        this.size = SIZE;
        this.mass = MASS;
        this.status = STATUS.setUp;
        this.x = x;
        this.y = y;
        this.color = color;
        this.isBomb = false;
        this.name = getFileName();
        init();
    }

    public void setXY(int x, int y) {
        this.status = STATUS.setUp;
        this.x = x;
        this.y = y;
        this.size = SIZE;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getXCenter() {
        return x + this.size / 2.0;
    }

    public double getYCenter() {
        return y + this.size / 2.0;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    public double calculateAcceleration(double friction) {
        return friction * G;
    }

    public double getTimeInSeconds() {
        if (isTimeRunning)
            return (System.currentTimeMillis() - this.startTime) / 1000.0;
        else
            return endTimeInSecond;
    }

    public void reSetTime() {
        this.startTime = System.currentTimeMillis();
        this.isTimeRunning = true;
    }

    public void toStartMove(int xTarget, int yTarget) {
        reSetTime();
        this.xStartingPosition = this.x;
        this.yStartingPosition = this.y;
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.distance = Math.sqrt((Math.pow(this.xStartingPosition - this.xTarget, 2) + Math.pow(this.yStartingPosition - this.yTarget, 2)));
        this.status = STATUS.moveToStart;
        this.endTimeInSecond = distance / SET_UP_SPEED_PX_PER_SECOND;
    }

    public void inGameMoveSpeedAndAngle(double speed, double angle, double frictionK) {
        reSetTime();
        this.acceleration = calculateAcceleration(frictionK);
        this.speedPixelsPerSecond = speed;
        this.angleInDegrees = angle;
        this.xStartingPosition = this.x;
        this.yStartingPosition = this.y;
        this.endTimeInSecond = Math.abs(speed) / (frictionK * G);
        if (this.status != STATUS.homePage)
            this.status = STATUS.movingInGame;
    }

    public static void StopCollision(Puck d1, Puck d2) {
        double distance = Math.sqrt(Math.pow(d2.getXCenter() - d1.getXCenter(), 2) + Math.pow(d2.getYCenter() - d1.getYCenter(), 2));
        double overLapping = (d1.getSize() / 2.0 + d2.getSize() / 2.0 - distance) / 2.0;
        d1.addX(overLapping / 2 * (d1.getXCenter() - d2.getXCenter()) / distance);
        d1.addY(overLapping / 2 * (d1.getYCenter() - d2.getYCenter()) / distance);
        d2.addX(overLapping / 2 * (d2.getXCenter() - d1.getXCenter()) / distance);
        d2.addY(overLapping / 2 * (d2.getYCenter() - d1.getYCenter()) / distance);
    }

    public void rest() {
        this.status = STATUS.restingInGame;
    }

    public void run() {

        while (true) {
            switch (this.status) {
                case moveToStart -> {
                    if (this.getTimeInSeconds() < this.endTimeInSecond) {
                        this.x = Utils.calculatePosition(xStartingPosition, (xTarget - xStartingPosition) / (this.distance) * SET_UP_SPEED_PX_PER_SECOND, this.getTimeInSeconds(), 0);
                        this.y = Utils.calculatePosition(yStartingPosition, (yTarget - yStartingPosition) / (this.distance) * SET_UP_SPEED_PX_PER_SECOND, this.getTimeInSeconds(), 0);
                    } else {
                        this.x = xTarget;
                        this.y = yTarget;
                        this.cancelTime();
                        this.status = STATUS.shoot;
                    }
                }
                case movingInGame -> {
                    this.degrees += 1;
                    if (this.getTimeInSeconds() < this.endTimeInSecond) {
                        calculateXY();
                    } else {
                        this.cancelTime();
                        this.status = STATUS.restingInGame;
                    }
                    checkIfOutOfFiledAndEliminate();
                }
                case restingOutOfFiled -> {
                    this.xStartingPosition += 0.5;
                    this.yStartingPosition += 0.5;
                    this.speedPixelsPerSecond *= 0.98;
                    this.x = Utils.calculatePosition(this.xStartingPosition, this.speedPixelsPerSecond * Math.sin(Math.toRadians(this.angleInDegrees)), this.getTimeInSeconds(), 0);
                    this.y = Utils.calculatePosition(this.yStartingPosition, this.speedPixelsPerSecond * Math.cos(Math.toRadians(this.angleInDegrees)), this.getTimeInSeconds(), 0);
                    this.size--;
                    Utils.sleep(20);
                    if (this.size == 0) {
                        this.status = STATUS.eliminated;
                    }
                }
                case homePage -> {
                    calculateXY();

                    if (this.x > Window.WINDOW_WIDTH) {
                        x = -size;
                        inGameMoveSpeedAndAngle(this.speedPixelsPerSecond, this.angleInDegrees, 0);
                    }
                    if (this.x < -size) {
                        x = Window.WINDOW_WIDTH;
                        inGameMoveSpeedAndAngle(this.speedPixelsPerSecond, this.angleInDegrees, 0);
                    }
                    if (this.y > Window.WINDOW_HEIGHT) {
                        y = -size;
                        inGameMoveSpeedAndAngle(this.speedPixelsPerSecond, this.angleInDegrees, 0);
                    }
                    if (this.y < -size) {
                        y = Window.WINDOW_HEIGHT;
                        inGameMoveSpeedAndAngle(this.speedPixelsPerSecond, this.angleInDegrees, 0);
                    }
                }
            }
            Utils.sleep(SLEEP_IN_MILLI_SECOND);

        }

    }

    private void calculateXY() {
        this.x = Utils.calculatePosition(this.xStartingPosition, this.speedPixelsPerSecond * Math.sin(Math.toRadians(this.angleInDegrees)), this.getTimeInSeconds(), -1 * this.acceleration * Math.sin(Math.toRadians(this.angleInDegrees)));
        this.y = Utils.calculatePosition(this.yStartingPosition, this.speedPixelsPerSecond * Math.cos(Math.toRadians(this.angleInDegrees)), this.getTimeInSeconds(), -1 * this.acceleration * Math.cos(Math.toRadians(this.angleInDegrees)));
    }

    public void cancelTime() {
        this.isTimeRunning = false;
    }

    public void checkIfOutOfFiledAndEliminate() {
        if (this.x + this.size < GameScene.DEFAULT_X_POINTS[0] || this.x > GameScene.DEFAULT_X_POINTS[3] || this.y + this.size < GameScene.DEFAULT_Y_POINTS[1]) {
            this.status = STATUS.restingOutOfFiled;
            this.reSetTime();
            this.xStartingPosition = this.x;
            this.yStartingPosition = this.y;
            this.needToPlayFallingSound = true;
        }
    }

    public boolean isNeedToPlayFallingSound() {
        return this.needToPlayFallingSound;
    }

    public void noNeedToPlayFallingSound() {
        needToPlayFallingSound = false;
    }

    public void init() {
        this.img = Utils.loadImage(name);
        this.degrees = 0;
    }

    public void paint(Graphics graphics) {
        
        AffineTransform at = AffineTransform.getTranslateInstance(x /*- img.getWidth() * 0.5 * multiplier*/, y /*- img.getHeight() * multiplier*/);
        double multiplier = (double) this.size / img.getHeight();
        at.rotate(Math.toRadians(this.degrees), img.getWidth() * 0.5 * multiplier, img.getHeight() * 0.5 * multiplier);
        at.scale(multiplier, multiplier);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(img, at, null);
    }

    public static Boolean checkCollisionBetweenCircles(Puck d1, Puck d2) {
        double x1 = d1.getXCenter();
        double y1 = d1.getYCenter();
        double r1 = d1.size / 2.0;
        double x2 = d2.getXCenter();
        double y2 = d2.getYCenter();
        double r2 = d2.size / 2.0;
        return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) <= Math.pow(r1 + r2, 2);
    }

    public static void calculateCollision(Puck d1, Puck d2, double frictionK) {
        double collisionAngleInDegrees = 270 - Utils.calculateCollisionDegrees(d1.getXCenter(), d1.getYCenter(), d2.getXCenter(), d2.getYCenter());
        double v1 = Utils.calculateCurrentSpeed(d1.speedPixelsPerSecond, d1.getTimeInSeconds(), d1.getAcceleration());
//        double v1 = Utils.calculateCurrentSpeed(d1.speedPixelsPerSecond, d1.time, d1.getAcceleration());
        double m1 = d1.mass;
        double angle1 = d1.angleInDegrees;
        double v2 = Utils.calculateCurrentSpeed(d2.speedPixelsPerSecond, d2.getTimeInSeconds(), d2.acceleration);
//        double v2 = Utils.calculateCurrentSpeed(d2.speedPixelsPerSecond, d2.time, d2.acceleration);
        double m2 = d2.mass;
        double angle2 = d2.angleInDegrees;

        double xSpeed1 = Utils.calculateXSpeedAfterCollision(v1, m1, angle1, v2, m2, angle2, collisionAngleInDegrees);
        double ySpeed1 = Utils.calculateYSpeedAfterCollision(v1, m1, angle1, v2, m2, angle2, collisionAngleInDegrees);
        double xSpeed2 = Utils.calculateXSpeedAfterCollision(v2, m2, angle2, v1, m1, angle1, collisionAngleInDegrees);
        double ySpeed2 = Utils.calculateYSpeedAfterCollision(v2, m2, angle2, v1, m1, angle1, collisionAngleInDegrees);


        double speed1 = Math.sqrt(xSpeed1 * xSpeed1 + ySpeed1 * ySpeed1);
        double speed2 = Math.sqrt(xSpeed2 * xSpeed2 + ySpeed2 * ySpeed2);
        double newAngle1 = Utils.calculateDegrees(xSpeed1, ySpeed1);
        double newAngle2 = Utils.calculateDegrees(xSpeed2, ySpeed2);
        if (d1.isBomb) {
            speed2 += EXPLODE_SPEED;
        } else if (d2.isBomb) {
            speed1 += EXPLODE_SPEED;
        }
        d1.inGameMoveSpeedAndAngle(speed1, newAngle1, frictionK);
        d2.inGameMoveSpeedAndAngle(speed2, newAngle2, frictionK);
        Utils.sleep(10);
    }

    public enum STATUS {
        setUp,
        moveToStart,
        shoot,
        movingInGame,
        restingInGame,
        restingOutOfFiled,
        eliminated,
        homePage
    }
}
