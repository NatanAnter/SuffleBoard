import java.awt.*;
import java.util.Random;

public class Power extends Thread {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 800;
    public static final int SPACING = 5;
    public static final int X = 550;
    public static final int Y = Window.WINDOW_HEIGHT - HEIGHT - 50;
    public static final Color BORDER_COLOR = Color.WHITE;
    public static final Color INTERIOR_COLOR = Color.MAGENTA;
    public static final double MAX_SPEED = 400;/////////////////////////////////////////////////////////////////////////600
    public static final double MIN_SPEED = 50;
    private int height;
    private boolean inMove;

    public Power() {
    }

    public void paint(Graphics graphics) {
        graphics.setColor(BORDER_COLOR);
        graphics.drawRect(X, Y, WIDTH, HEIGHT);
        graphics.setColor(INTERIOR_COLOR);
        graphics.fillRect(X + SPACING, Y + HEIGHT - SPACING - this.height, WIDTH - SPACING * 2, this.height);
    }


    public double calculateSpeed() {
        return this.height / (HEIGHT - SPACING * 2.0) * (MAX_SPEED - MIN_SPEED);
    }

    public void run() {
        Random random = new Random();
        this.height = random.nextInt(0, HEIGHT - SPACING * 2);
        this.inMove = true;
        boolean goingUp = random.nextBoolean();
        while (true) {
            if(inMove) {
                if (goingUp)
                    this.height += 3;
                else this.height -= 3;
                if (this.height > HEIGHT - SPACING * 2 || this.height < 0)
                    goingUp = !goingUp;
                Utils.sleep(4);
            }
        }
    }

}
