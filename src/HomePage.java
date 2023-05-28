//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class HomePage extends JPanel implements ActionListener {
//
////    public static final int BUTTON_WIDTH = 200;
////    public static final int BUTTON_HEIGHT = 50;
////    public static final double FRICTION_K = 0;
////    public static final int NUMBER_OF_DISKETTES = 20;
////    private GameScene gameScene;
////    private Diskette[] diskettes;
////    private JButton button;
////    private STATUS status;
//    public HomePage() {
////        this.diskettes = new Diskette[NUMBER_OF_DISKETTES];
////        for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
////            this.diskettes[i] = new Diskette(100 * i, 150 * i);
////            this.diskettes[i].start();
////            Utils.sleep(1);
////        }
////        button = new JButton();
////        button.setBounds(Window.WINDOW_WIDTH/2 - BUTTON_WIDTH/2, Window.WINDOW_HEIGHT/2 - BUTTON_HEIGHT/2, BUTTON_WIDTH, BUTTON_HEIGHT);
////        button.addActionListener(this);
////        button.setText("play");
////        button.setFont(new Font("Arial", Font.BOLD, 50));
////        button.setFocusable(false);
////        this.add(button);
////        this.status = STATUS.homePage;
////
////        mainLoop();
//    }
//
////    public void mainLoop() {
////        new Thread(() -> {
////            while (true) {
////                for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
////                    for (int j = i + 1; j < NUMBER_OF_DISKETTES; j++) {
////                        if (Utils.checkCollisionBetweenCircles(diskettes[i].getX() + diskettes[i].getSize() / 2.0, diskettes[i].getY() + diskettes[i].getSize() / 2.0, diskettes[i].getSize() / 2.0, diskettes[j].getX() + diskettes[j].getSize() / 2.0, diskettes[j].getY() + diskettes[j].getSize() / 2.0, +diskettes[j].getSize() / 2.0)) {
////                            calculateCollision(i, j, FRICTION_K);
////                            Utils.sleep(1);
////                        }
////                    }
////                }
////                repaint();
////            }
////        }).start();
////    }
//
////    public void calculateCollision(int d1, int d2, double frictionK) {
////        Diskette.calculateCollision(this.diskettes[d1],this.diskettes[d2], frictionK);
////    }
//
////    public void paintComponent(Graphics graphics) {
////        super.paintComponent(graphics);
////        switch (this.status){
////            case homePage -> {
////                for (int i = 0; i < NUMBER_OF_DISKETTES; i++) {
////                    this.diskettes[i].paint(graphics);
////                }
////            }
////            case buttonClicked -> {
////                this.gameScene.paintComponent(graphics);
////            }
////        }
////
////    }
//
////    public void actionPerformed(ActionEvent e) {
////        if (e.getSource() == button) {
////            this.status = STATUS.buttonClicked;
////            System.out.println("openGame");
////            gameScene = new GameScene();
////            this.add(gameScene);
////        }
////    }
//
//
//    public enum STATUS{
//        homePage,
//        buttonClicked,
//        playerWon
//    }
//}
