import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Arrow {
    private int x;
    private int y;
    private final double radios;
    private double degrees;
    private double multiplier;
    private BufferedImage img;

    public Arrow(int x, int y, double radios) {
        this.x = x;
        this.y = y;
        this.radios = radios;
        this.degrees = 0;
        init();
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
    public void init(){
        this.img = Utils.loadImage("Arrow.png");
        this.multiplier = this.radios / img.getHeight();
    }
    public void paint(Graphics graphics) {
        AffineTransform at = AffineTransform.getTranslateInstance(x - img.getWidth() * 0.5 * multiplier, y - img.getHeight() * multiplier);
        at.rotate(Math.toRadians(this.degrees), img.getWidth() * 0.5 * multiplier, img.getHeight() * multiplier);
        at.scale(multiplier, multiplier);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(img, at, null);
    }
}