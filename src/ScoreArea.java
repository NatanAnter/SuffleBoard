import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ScoreArea {
    public static final String RED_SCORE_iMAGE_LOCATION = "puckRed.png";
    public static final String BLUE_SCORE_iMAGE_LOCATION = "puckBlue.png";
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int X_RED = 70;
    public static final int X_BLUE = Window.WINDOW_WIDTH - WIDTH - X_RED;
    public static final int Y = 70;
    public static final int SEMI_WIDTH = WIDTH * 3 / 2;
    public static final int SEMI_HEIGHT = HEIGHT / 2;
    public static final int SEMI_Y = Y + HEIGHT;
    public static final int SPACE = 10;
    private final JLabel redScoreLabel;
    private final JLabel blueScoreLabel;
    private final JLabel redSemiScoreLabel;
    private final JLabel blueSemiScoreLabel;
    private final BufferedImage redScoreImage;
    private final BufferedImage blueScoreImage;
    private int redScore;
    private int blueScore;
    private int redSemiScore;
    private int blueSemiScore;
    private final AffineTransform redAt;
    private final AffineTransform blueAt;

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public void setVisible(boolean myEmailIsNatanAnterAtGmailDotCom) {
        this.blueScoreLabel.setVisible(myEmailIsNatanAnterAtGmailDotCom);
        this.redScoreLabel.setVisible(myEmailIsNatanAnterAtGmailDotCom);
        this.redSemiScoreLabel.setVisible(myEmailIsNatanAnterAtGmailDotCom);
        this.blueSemiScoreLabel.setVisible(myEmailIsNatanAnterAtGmailDotCom);
    }

    public ScoreArea() {
        this.redScoreImage = Utils.loadImage(RED_SCORE_iMAGE_LOCATION);
        this.blueScoreImage = Utils.loadImage(BLUE_SCORE_iMAGE_LOCATION);
        this.redScoreLabel = new JLabel();
        this.redScoreLabel.setBounds(X_RED + WIDTH + SPACE, Y, WIDTH * 3 / 2, HEIGHT);
        this.redScoreLabel.setFont(new Font("Arial", Font.PLAIN, HEIGHT));
        this.redScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.blueScoreLabel = new JLabel();
        this.blueScoreLabel.setBounds(X_BLUE - SPACE - WIDTH * 3 / 2, Y, WIDTH * 3 / 2, HEIGHT);
        this.blueScoreLabel.setFont(new Font("Arial", Font.PLAIN, HEIGHT));
        this.blueScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.redSemiScoreLabel = new JLabel();
        this.redSemiScoreLabel.setBounds(X_RED + WIDTH + SPACE, SEMI_Y, SEMI_WIDTH, SEMI_HEIGHT);
        this.redSemiScoreLabel.setFont(new Font("Arial", Font.PLAIN, HEIGHT / 2));
        this.redSemiScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.blueSemiScoreLabel = new JLabel();
        this.blueSemiScoreLabel.setBounds(X_BLUE - SPACE - WIDTH * 3 / 2, SEMI_Y, SEMI_WIDTH, SEMI_HEIGHT);
        this.blueSemiScoreLabel.setFont(new Font("Arial", Font.PLAIN, HEIGHT / 2));
        this.blueSemiScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.setVisible(false);
        this.redScore = 0;
        this.blueScore = 0;
        this.redSemiScore = 0;
        this.blueSemiScore = 0;
        this.redAt = AffineTransform.getTranslateInstance(X_RED, Y);
        this.redAt.scale((WIDTH + 0.0) / redScoreImage.getWidth(), (HEIGHT + 0.0) / redScoreImage.getHeight());
        this.blueAt = AffineTransform.getTranslateInstance(X_BLUE, Y);
        this.blueAt.scale((WIDTH + 0.0) / blueScoreImage.getWidth(), (HEIGHT + 0.0) / blueScoreImage.getHeight());
    }

    public void setScores(int redScore, int blueScore) {
        this.blueScore = blueScore;
        this.redScore = redScore;
        updateText();
    }

    public void setSemiScores(int redScore, int blueScore) {
        this.blueSemiScore = blueScore;
        this.redSemiScore = redScore;
        updateText();
    }

    public void updateText() {
        this.redScoreLabel.setText(redScore + "");
        this.blueScoreLabel.setText(blueScore + "");
        this.redSemiScoreLabel.setText(redSemiScore + "");
        this.blueSemiScoreLabel.setText(blueSemiScore + "");
    }

    public void addScore(int redScore, int blueScore) {
        this.blueScore += blueScore;
        this.redScore += redScore;
        updateText();
    }

    public JLabel getBlueScoreLabel() {
        return blueScoreLabel;
    }

    public JLabel getRedScoreLabel() {
        return redScoreLabel;
    }

    public JLabel getBlueSemiScoreLabel() {
        return blueSemiScoreLabel;
    }

    public JLabel getRedSemiScoreLabel() {
        return redSemiScoreLabel;
    }

    public void paintScores(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(redScoreImage, redAt, null);
        graphics2D.drawImage(blueScoreImage, blueAt, null);

    }
}