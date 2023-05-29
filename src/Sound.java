import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {
    private String fileName;
    File file;
    AudioInputStream audioInputStream;
    Clip clip;

    public Sound(String fileName) {
        this.fileName = fileName;
        init();
    }

    public void init() {
        try {
            file = new File(fileName);
            audioInputStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception e) {
        }
    }

    public void stop() {
        clip.stop();
    }
    public void play(){
        this.clip.start();
    }
    public void reset(){
        this.clip.setMicrosecondPosition(0);
    }

}
