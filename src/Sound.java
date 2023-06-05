import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Sound {
    private final String fileName;
    private Clip clip;
    private boolean exist;

    public Sound(String fileName) {
        this.fileName = fileName;
        loadSound();
    }

    public void loadSound() {
        this.exist = true;
        try {
            File file = new File(fileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
            System.out.println("Error: " + e);
            this.exist = false;
        }
    }

    public void setVolume(float volume) {
        if (this.exist) {
            try {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(volume);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public boolean isActive() {
        return this.exist && this.clip.isActive();
    }

    public void resetAndStop() {
        if (this.exist) {
            reset();
            this.clip.stop();
        }
    }

    public void pause() {
        if (this.exist)
            clip.stop();
    }

    public void play() {
        if (this.exist) {
            this.clip.start();
        }
    }

    public void rePlay() {
        if (this.exist) {
            reset();
            this.clip.start();
        }
    }

    public void reset() {
        if (this.exist) {
            this.clip.setMicrosecondPosition(0);
        }
    }

}
