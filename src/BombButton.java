import javax.swing.*;
import java.awt.*;

public class BombButton {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int SPACE = 10;
    public static final int X = GameScene.DEFAULT_X_POINTS[3] + SPACE;
    public static final int Y = GameScene.DEFAULT_Y_POINTS[0] - SPACE * 2 - HEIGHT * 2;
    public static final String NAME = "resources\\images\\Bomb.png";
    private JButton button;
    private boolean redClicked;
    private boolean blueClicked;

    public BombButton() {
        this.button = new JButton(Utils.loadButtonImage(NAME, WIDTH, HEIGHT));
        this.button.setBounds(X, Y, WIDTH, HEIGHT);
        this.redClicked = false;
        this.blueClicked = false;
    }

    public JButton getButton() {
        return this.button;
    }

    public boolean isRedClicked() {
        return redClicked;
    }

    public boolean isBlueClicked() {
        return blueClicked;
    }

    public void redClicked() {
        this.redClicked = true;
    }

    public void blueClicked() {
        this.blueClicked = true;
    }

    public void reset() {
        this.redClicked = false;
        this.blueClicked = false;
    }
}
