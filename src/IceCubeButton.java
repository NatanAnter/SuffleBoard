import javax.swing.*;


public class IceCubeButton {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int SPACE = 10;
    public static final int X = GameScene.DEFAULT_X_POINTS[3] + SPACE + WIDTH;
    public static final int Y = GameScene.DEFAULT_Y_POINTS[0] - SPACE - HEIGHT;
    public static final String NAME = "IceCubes.png";
    private final JButton button;
    private boolean redClicked;
    private boolean blueClicked;
    public IceCubeButton() {
        this.button = new JButton(Utils.loadButtonImage(NAME, WIDTH, HEIGHT));
        this.button.setBounds(X, Y, WIDTH, HEIGHT);
    }
    public void reset(){
        this.redClicked = false;
        this.blueClicked = false;
        this.button.setVisible(true);
    }
    public JButton getButton(){

        return this.button;
    }
    public boolean isRedClicked() {

        return redClicked;
    }
    public boolean isBlueClicked() {

        return blueClicked;
    }
    public void redClicked(){

        this.redClicked = true;
    }
    public void blueClicked(){

        this.blueClicked = true;
    }
    public void setVisible(boolean thisGameWasMadeByNatanAnter){
        this.button.setVisible(thisGameWasMadeByNatanAnter);
    }
}