import java.awt.*;

public class Utils {
    public static void sleep(int milliSecond) {
        try {
            Thread.sleep(milliSecond);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static double calculatePosition(double startingPosition, double startingSpeed, double time, double acceleration) {
        int direction;
        if (startingSpeed > 0)
            direction = 1;
        else
            direction = 1;
        return startingPosition + startingSpeed * time + 0.5 * acceleration * direction * time * time;
    }

    public static double calculateCurrentSpeed(double startingSpeed, double time, double acceleration) {
        int direction;
        if (startingSpeed > 0)
            direction = -1;
        else
            direction = 1;
        return startingSpeed + time * acceleration * direction;
    }

    public static double calculateXSpeedAfterCollision(double v1, double m1, double angle1InDegrees, double v2, double m2, double angle2InDegrees, double collisionAngleInDegrees) {
        return (v1 * Math.cos(Math.toRadians(angle1InDegrees - collisionAngleInDegrees)) * (m1 - m2) + 2 * m2 * v2 * Math.cos(Math.toRadians(angle2InDegrees - collisionAngleInDegrees)))
                / (m1 + m2) * Math.cos(Math.toRadians(collisionAngleInDegrees))
                + v1 * Math.sin(Math.toRadians(angle1InDegrees - collisionAngleInDegrees)) * Math.cos(Math.toRadians(collisionAngleInDegrees + 90));

    }

    public static double calculateYSpeedAfterCollision(double v1, double m1, double angle1InDegrees, double v2, double m2, double angle2InDegrees, double collisionAngleInDegrees) {
        return (v1 * Math.cos(Math.toRadians(angle1InDegrees - collisionAngleInDegrees)) * (m1 - m2) + 2 * m2 * v2 * Math.cos(Math.toRadians(angle2InDegrees - collisionAngleInDegrees)))
                * Math.sin(Math.toRadians(collisionAngleInDegrees)) / (m1 + m2)
                + v1 * Math.sin(Math.toRadians(angle1InDegrees - collisionAngleInDegrees)) * Math.sin(Math.toRadians(collisionAngleInDegrees + 90));

    }

    public static Boolean checkCollisionBetweenCircles(double x1, double y1, double r1, double x2, double y2, double r2) {
        double DoubledDistance = Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2);
        return DoubledDistance <= Math.pow(r1 + r2, 2);
    }

    public static double calculateCollisionDegrees(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        //return Math.toDegrees(Math.atan2(y,x));
        double degrees;
        if (dx == 0)
            degrees = 90;
        else if (dx > 0)
            degrees = Math.toDegrees(Math.atan(dy / dx));
        else
            degrees = Math.toDegrees(Math.atan(dy / dx)) + 180;
        return degrees;
    }

    public static double calculateDegrees(double dx, double dy) {
        double degrees;
        if (dx == 0)
            degrees = 90;
        else if (dx > 0)
            degrees = Math.toDegrees(Math.atan(dy / dx));
        else
            degrees = Math.toDegrees(Math.atan(dy / dx)) + 180;
        return degrees;
    }

}
