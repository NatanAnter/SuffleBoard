import javax.swing.*;
public class MediaPlayer {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int VOLUME_WIDTH = 50;
    public static final int VOLUME_HEIGHT = 50;
    public static final int SPACE = 10;
    public static final int X = SPACE;
    public static final int Y = GameScene.DEFAULT_Y_POINTS[1] + ScoreArea.Y + ScoreArea.HEIGHT;
    public static final String NEXT_NAME = "nextSong.png";
    public static final String PREVIOUS_NAME = "previousSong.png";
    public static final String PAUSE_NAME = "pause.png";
    public static final String PLAY_NAME = "play.png";
    public static final String MUSIC_ON_NAME = "musicOn.png";
    public static final String MUSIC_OFF_NAME = "musicOFF.png";
    public static final String SOUND_ON_NAME = "soundOn.png";
    public static final String SOUND_OFF_NAME = "soundOFF.png";
    public static final float NO_SOUND = -80;
    private final JButton buttonNext;
    private final JButton buttonPlayPause;
    private final JButton buttonPrevious;
    private final JButton buttonMusic;
    private final JButton buttonSound;
    private final JSlider sliderMusicVolume;
    private final JSlider sliderSoundVolume;
    private boolean musicOn;
    private boolean soundOn;
    private boolean pause;
    private int lastMusicValue;
    private int lastSoundValue;

    public MediaPlayer() {

        this.buttonPrevious = new JButton(Utils.loadButtonImage(PREVIOUS_NAME, WIDTH, HEIGHT));
        this.buttonPrevious.setBounds(X, Y, WIDTH, HEIGHT);
        this.buttonPlayPause = new JButton(Utils.loadButtonImage(PAUSE_NAME, WIDTH, HEIGHT));
        this.buttonPlayPause.setBounds(X + SPACE + WIDTH, Y, WIDTH, HEIGHT);
        this.buttonNext = new JButton(Utils.loadButtonImage(NEXT_NAME, WIDTH, HEIGHT));
        this.buttonNext.setBounds(X + (SPACE + WIDTH) * 2, Y, WIDTH, HEIGHT);
        this.buttonMusic = new JButton();
        this.buttonMusic.setBounds(X, Y + HEIGHT + SPACE, VOLUME_WIDTH, VOLUME_HEIGHT);
        this.buttonSound = new JButton();
        this.buttonSound.setBounds(X, Y + HEIGHT + SPACE + VOLUME_HEIGHT + SPACE, VOLUME_WIDTH, VOLUME_HEIGHT);
        this.pause = false;
        this.sliderMusicVolume = new JSlider(-20, 6);
        this.sliderMusicVolume.setBounds(X + VOLUME_WIDTH + SPACE, Y + HEIGHT + SPACE, WIDTH * 3 + SPACE * 2 - SPACE - VOLUME_WIDTH, VOLUME_HEIGHT);
        this.sliderSoundVolume = new JSlider(-20, 6);
        this.sliderSoundVolume.setBounds(X + VOLUME_WIDTH + SPACE, Y + HEIGHT + SPACE + VOLUME_HEIGHT + SPACE, WIDTH * 3 + SPACE * 2 - SPACE - VOLUME_WIDTH, VOLUME_HEIGHT);
        this.setVisible(false);
        this.musicOn = true;
        this.soundOn = true;
        this.lastMusicValue = sliderMusicVolume.getValue();
        this.lastSoundValue = sliderSoundVolume.getValue();
        buttonMusicClicked();
        buttonSoundClicked();

    }

    public float getMusicVolume() {
        if (sliderMusicVolume.getValue() == sliderMusicVolume.getMinimum())
            return NO_SOUND;
        return sliderMusicVolume.getValue();
    }

    public float getSoundVolume() {
        if (sliderSoundVolume.getValue() == sliderSoundVolume.getMinimum())
            return NO_SOUND;
        return sliderSoundVolume.getValue();
    }

    public void musicVolumeChanged() {
        if (musicOn)
            changeMusicImage();
    }

    public void soundVolumeChanged() {
        if (soundOn)
            changeSoundImage();
    }

    public JButton getButtonMusic() {
        return buttonMusic;
    }

    public JButton getButtonSound() {
        return buttonSound;
    }

    public JSlider getSliderSoundVolume() {
        return sliderSoundVolume;
    }

    public void buttonSoundClicked() {
        changeSoundImage();
        changeSoundVolume();
        soundOn = !soundOn;
    }

    public void changeSoundImage() {
        if (soundOn) {
            buttonSound.setIcon(Utils.loadButtonImage(SOUND_ON_NAME, VOLUME_WIDTH, VOLUME_HEIGHT));
        } else {
            buttonSound.setIcon(Utils.loadButtonImage(SOUND_OFF_NAME, VOLUME_WIDTH, VOLUME_HEIGHT));
        }
    }

    public void changeSoundVolume() {
        if (soundOn) {
            sliderSoundVolume.setValue(lastSoundValue);
        } else {
            this.lastSoundValue = sliderSoundVolume.getValue();
            sliderSoundVolume.setValue((int) NO_SOUND);
        }

    }

    public void buttonMusicClicked() {
        changeMusicImage();
        changeMusicVolume();
        musicOn = !musicOn;
    }

    public void changeMusicImage() {
        if (musicOn) {
            buttonMusic.setIcon(Utils.loadButtonImage(MUSIC_ON_NAME, VOLUME_WIDTH, VOLUME_HEIGHT));
        } else {
            buttonMusic.setIcon(Utils.loadButtonImage(MUSIC_OFF_NAME, VOLUME_WIDTH, VOLUME_HEIGHT));
        }
    }

    public void changeMusicVolume() {
        if (musicOn) {
            sliderMusicVolume.setValue(lastMusicValue);
        } else {
            this.lastMusicValue = sliderMusicVolume.getValue();
            sliderMusicVolume.setValue((int) NO_SOUND);
        }
    }


    public JSlider getSliderMusicVolume() {
        return sliderMusicVolume;
    }

    public void playPausedClicked() {
        if (this.pause)
            playClicked();
        else
            pauseClicked();
    }

    public void playClicked() {
        this.buttonPlayPause.setIcon(Utils.loadButtonImage(PAUSE_NAME, WIDTH, HEIGHT));
        this.pause = false;
    }

    public void pauseClicked() {
        this.buttonPlayPause.setIcon(Utils.loadButtonImage(PLAY_NAME, WIDTH, HEIGHT));
        this.pause = true;
    }

    public JButton getButtonNext() {
        return buttonNext;
    }

    public JButton getButtonPlayPause() {
        return buttonPlayPause;
    }

    public JButton getButtonPrevious() {
        return buttonPrevious;
    }

    public void setVisible(boolean hiItsMeNatan) {
        this.buttonNext.setVisible(hiItsMeNatan);
        this.buttonPrevious.setVisible(hiItsMeNatan);
        this.buttonPlayPause.setVisible(hiItsMeNatan);
        this.buttonMusic.setVisible(hiItsMeNatan);
        this.buttonSound.setVisible(hiItsMeNatan);
        this.sliderSoundVolume.setVisible(hiItsMeNatan);
        this.sliderMusicVolume.setVisible(hiItsMeNatan);
    }
}