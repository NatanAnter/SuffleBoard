import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Arrow {
    public static final int STARTING_X = 0;
    public static final int STARTING_Y = 0;
    private int x;
    private int y;
    private double radios;
    private double degrees;
    private int i = 0;
    private double multiplier;

    public Arrow(int x, int y, double radios) {
        this.x = x;
        this.y = y;
        this.radios = radios;
        this.degrees = 0;
    }

    public double getDegrees() {
        return degrees;
    }

    public void addDegree() {
        this.degrees += 1;
    }

    public void takeDegree() {
        this.degrees -= 1;
    }

    public void moveXY(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public void paint(Graphics graphics) {
        BufferedImage img = loadImage("Arrow.png");
        this.multiplier = this.radios / img.getHeight();
        AffineTransform at = AffineTransform.getTranslateInstance(x - img.getWidth() * 0.5 * multiplier, y - img.getHeight() * multiplier);
        at.rotate(Math.toRadians(this.degrees), img.getWidth() * 0.5 * multiplier, img.getHeight() * multiplier);
        at.scale(multiplier, multiplier);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(img, at, null);
    }

    public BufferedImage loadImage(String fileName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileName));
        } catch (IOException ignored) {
            System.out.println("didnt find image");
        }
        return img;
    }
}
