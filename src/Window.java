import javax.swing.*;

public class Window extends JFrame {
    public static final int WINDOW_WIDTH = 1920;
    public static final int WINDOW_HEIGHT = 1080;
    public static String PROTOCOL;
    public Window() {
        PROTOCOL = this.getClass().getResource(this.getClass().getName()+".class").getProtocol();
        this.setResizable(false);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        GameScene gameScene = new GameScene();
        this.add(gameScene);
    }

    public void showWindow() {
        this.setVisible(true);
    }
}