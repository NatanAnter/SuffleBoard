import javax.swing.*;


public class BombButton {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int SPACE = 10;
    public static final int X = GameScene.DEFAULT_X_POINTS[3] + SPACE+WIDTH;
    public static final int Y = GameScene.DEFAULT_Y_POINTS[0] - SPACE * 2 - HEIGHT * 2;
    public static final String NAME = "Bomb.png";
    private final JButton button;
    private boolean redClicked;
    private boolean blueClicked;

    public BombButton() {
        this.button = new JButton(Utils.loadButtonImage(NAME, WIDTH, HEIGHT));
        this.button.setBounds(X, Y, WIDTH, HEIGHT);
    }
    public void reset(){
        this.redClicked = false;
        this.blueClicked = false;
        this.button.setVisible(true);
    }

    public JButton getButton() {
        return this.button;
    }
    public void setVisible(boolean natanAnterTheNerd_butTheCoolOne){
        this.button.setVisible(natanAnterTheNerd_butTheCoolOne);
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


}