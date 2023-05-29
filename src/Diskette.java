import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Diskette extends Thread {
    public static final int SIZE = 50;
    public static final double SET_UP_SPEED_PX_PER_SECOND = 300;
    public static final Double SLEEP_IN_SECOND = 0.002;
    public static final int SLEEP_IN_MILLI_SECOND = (int) (SLEEP_IN_SECOND * 1000);
    private static final double MASS = 0.5;
    public static final double G = 9.8;
    private int size;
    private double mass;
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
    private double time;
    private STATUS status;
    private double endTimeInSecond;
    private double acceleration;
    private Timer timer;
    private TimerTask task;

    public STATUS getStatus() {
        return this.status;
    }

    public int getSize() {
        return size;
    }

    public double getAngleInDegrees() {
        return angleInDegrees;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getSpeedPixelsPerSecond() {
        return speedPixelsPerSecond;
    }

    public double getTime() {
        return time;
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

    public Color getColor() {
        return color;
    }

    public Diskette(int x, int y, Color color) {
        Random random = new Random();
        this.timer = new Timer();
        this.size = random.nextInt(10, 80);
        this.speedPixelsPerSecond = random.nextDouble(50, 400);
        this.angleInDegrees = random.nextInt(0, 360);
        this.x = x;
        this.y = y;
        this.xStartingPosition = x;
        this.yStartingPosition = y;
        this.color = color;
        this.mass = size * 2;
        this.acceleration = 0;
        this.status = STATUS.homePage;
        startTimer();
    }

    public Diskette(Color color, int x, int y) {
        this.timer = new Timer();
        this.size = SIZE;
        this.mass = MASS;
        this.status = STATUS.setUp;
        this.x = x;
        this.y = y;
        this.color = color;


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
        return x+this.size/2.0;
    }
    public double getYCenter() {
        return y+this.size/2.0;
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

    public void startTimer() {
        try {
            this.task = new TimerTask() {
                @Override
                public void run() {
                    time += SLEEP_IN_SECOND;
                }
            };
            this.time = 0;
            this.timer.scheduleAtFixedRate(this.task, 0, (int) (SLEEP_IN_SECOND * 1000));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void restartTimer() {
        try {
            this.task.cancel();

        }
        catch (Exception e)
        {}
        startTimer();
    }

    public void toStartMove(int xTarget, int yTarget) {
        startTimer();
        this.xStartingPosition = this.x;
        this.yStartingPosition = this.y;
        this.xTarget = xTarget;
        this.yTarget = yTarget;
        this.distance = Math.sqrt((Math.pow(this.xStartingPosition - this.xTarget, 2) + Math.pow(this.yStartingPosition - this.yTarget, 2)));
        this.status = STATUS.moveToStart;
        this.endTimeInSecond = distance / SET_UP_SPEED_PX_PER_SECOND;
    }

    public void inGameMoveSpeedAndAngle(double speed, double angle, double frictionK) {
        restartTimer();
//        restartTimer();
        this.acceleration = calculateAcceleration(frictionK);
        this.speedPixelsPerSecond = speed;
        this.angleInDegrees = angle;
        this.xStartingPosition = this.x;
        this.yStartingPosition = this.y;
        this.endTimeInSecond = Math.abs(speed) / (frictionK * G);
        if (this.status != STATUS.homePage)
            this.status = STATUS.movingInGame;
    }
    public static void StopCollision(Diskette d1, Diskette d2) {
//        d1.rest();
//        d2.rest();
        double distance = Math.sqrt(Math.pow(d2.getXCenter() - d1.getXCenter(), 2) + Math.pow(d2.getYCenter() - d2.getYCenter(), 2));
        double overLapping = (d1.getSize() / 2.0 + d2.getSize() / 2.0 - distance)/2.0;
        d1.addX(overLapping/2*(d1.getXCenter() - d2.getXCenter())/distance);
        d1.addY(overLapping/2*(d1.getYCenter() - d2.getYCenter())/distance);
        d2.addX(overLapping/2*(d2.getXCenter() - d1.getXCenter())/distance);
        d2.addY(overLapping/2*(d2.getYCenter() - d1.getYCenter())/distance);
    }

    public void rest() {
        this.status = STATUS.restingInGame;
    }

    public void run() {
        while (true) {
            switch (this.status) {
                case moveToStart -> {
                    if (this.time < this.endTimeInSecond) {
                        this.x = Utils.calculatePosition(xStartingPosition, (xTarget - xStartingPosition) / (this.distance) * SET_UP_SPEED_PX_PER_SECOND, this.time, 0);
                        this.y = Utils.calculatePosition(yStartingPosition, (yTarget - yStartingPosition) / (this.distance) * SET_UP_SPEED_PX_PER_SECOND, this.time, 0);
                    } else {
                        this.x = xTarget;
                        this.y = yTarget;
                        this.task.cancel();
                        this.status = STATUS.shoot;
                        this.time = this.endTimeInSecond;
                    }
                }
                case movingInGame -> {

                    if (this.time < this.endTimeInSecond) {
                        this.x = Utils.calculatePosition(this.xStartingPosition, this.speedPixelsPerSecond * Math.sin(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.sin(Math.toRadians(this.angleInDegrees)));
                        this.y = Utils.calculatePosition(this.yStartingPosition, this.speedPixelsPerSecond * Math.cos(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.cos(Math.toRadians(this.angleInDegrees)));
                    } else {
                        this.task.cancel();
                        this.status = STATUS.restingInGame;
                        this.time = this.endTimeInSecond;
                    }
                    checkIfOutOfFiledAndEliminate();

                }
                case restingOutOfFiled -> {
                    this.xStartingPosition += 0.5;
                    this.yStartingPosition += 0.5;
                    this.x = Utils.calculatePosition(this.xStartingPosition, this.speedPixelsPerSecond * Math.sin(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.sin(Math.toRadians(this.angleInDegrees)));
                    this.y = Utils.calculatePosition(this.yStartingPosition, this.speedPixelsPerSecond * Math.cos(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.cos(Math.toRadians(this.angleInDegrees)));
                    this.size--;
                    Utils.sleep(20);
                    if (this.size == 0) {
                        this.status = STATUS.eliminated;
                        this.task.cancel();
                    }
                }
                case homePage -> {
                    this.x = Utils.calculatePosition(this.xStartingPosition, this.speedPixelsPerSecond * Math.sin(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.sin(Math.toRadians(this.angleInDegrees)));
                    this.y = Utils.calculatePosition(this.yStartingPosition, this.speedPixelsPerSecond * Math.cos(Math.toRadians(this.angleInDegrees)), this.time, -1 * this.acceleration * Math.cos(Math.toRadians(this.angleInDegrees)));
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

    public void checkIfOutOfFiledAndEliminate() {
        if (this.x + this.size < GameScene.DEFAULT_X_POINTS[0] || this.x > GameScene.DEFAULT_X_POINTS[3] || this.y + this.size < GameScene.DEFAULT_Y_POINTS[1])
            this.status = STATUS.restingOutOfFiled;
    }

    public void paint(Graphics graphics) {
        graphics.setColor(this.color);
        graphics.fillOval((int) x, (int) y, size, size);
    }
//    public static Boolean checkCollisionBetweenCircles(double x1, double y1, double r1, double x2, double y2, double r2) {
//        double DoubledDistance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
//        return DoubledDistance <= Math.pow(r1 + r2, 2);
//    }
    public static Boolean checkCollisionBetweenCircles(Diskette d1, Diskette d2) {
        double x1 = d1.getXCenter();
        double y1 = d1.getYCenter();
        double r1 = d1.size/2.0;
        double x2 = d2.getXCenter();
        double y2 = d2.getYCenter();
        double r2 = d2.size/2.0;
        return Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) <= Math.pow(r1 + r2, 2);
    }

    public static void calculateCollision(Diskette d1, Diskette d2, double frictionK) {
        double collisionAngleInDegrees = 270 - Utils.calculateCollisionDegrees(d1.getXCenter(), d1.getYCenter(), d2.getXCenter(), d2.getYCenter());
        double v1 = Utils.calculateCurrentSpeed(d1.speedPixelsPerSecond, d1.time, d1.getAcceleration());
        double m1 = d1.mass;
        double angle1 = d1.angleInDegrees;
        double v2 = Utils.calculateCurrentSpeed(d2.speedPixelsPerSecond, d2.time, d2.acceleration);
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
        d1.inGameMoveSpeedAndAngle(speed1, newAngle1, frictionK);
        d2.inGameMoveSpeedAndAngle(speed2, newAngle2, frictionK);
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
