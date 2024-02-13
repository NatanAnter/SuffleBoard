import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class Sounds {
    public static final String[] MUSIC_FOLDERS_NAMES = {"gameBackgroundMusic", "gameBoosMusic", "homeBackgroundMusic", "homeWinnerMusic"};
    public static final String[] SOUND_EFFECTS_FOLDERS_NAMES = {"collision", "falling", "shooting"};
    public static final String[] SOUNDS_EFFECTS_MAGIC_FOLDERS_NAMES = {"bombCollision", "bombSpell", "collision", "falling", "freezeSpell", "shooting"};
    public static final String RESOURCES = "resources\\";
    public static final String MUSIC_LOCATION = "music";
    private final List<List<Sound>> MUSIC;
    private final List<Integer> MUSIC_NUMBER;
    private int currentPlay;
    public static final String SOUND_EFFECTS_LOCATION = "soundEffects";
    private final List<List<Sound>> SOUND_EFFECTS;
    private final List<Integer> SOUND_EFFECTS_NUMBER;
    public static final String SOUND_EFFECTS_MAGIC_LOCATION = "soundEffectsMagic";
    private final List<List<Sound>> SOUND_EFFECTS_MAGIC;
    private final List<Integer> SOUND_EFFECTS_MAGIC_NUMBER;
    private boolean paused;
    private float musicVolume;
    private float soundVolume;

    public Sounds(float musicVolume, float soundVolume) {
        this.MUSIC = loadSounds(MUSIC_LOCATION, MUSIC_FOLDERS_NAMES);
        this.MUSIC_NUMBER = setNumbers(MUSIC);
        this.SOUND_EFFECTS = loadSounds(SOUND_EFFECTS_LOCATION, SOUND_EFFECTS_FOLDERS_NAMES);
        this.SOUND_EFFECTS_NUMBER = setNumbers(SOUND_EFFECTS);
        this.SOUND_EFFECTS_MAGIC = loadSounds(SOUND_EFFECTS_MAGIC_LOCATION, SOUNDS_EFFECTS_MAGIC_FOLDERS_NAMES);
        this.SOUND_EFFECTS_MAGIC_NUMBER = setNumbers(SOUND_EFFECTS_MAGIC);
        this.paused = false;
        this.musicVolume = musicVolume;
        this.soundVolume = soundVolume;

    }

    public List<List<Sound>> loadSounds(String folderPath, String[] foldersNames) {
        if(Window.PROTOCOL.equals("jar")){
            List<List<Sound>> allMusic = new ArrayList<>();
            ClassLoader classLoader = getClass().getClassLoader();
            for (String innerFolderName : foldersNames) {
                try {
                    Enumeration<URL> resources = classLoader.getResources(folderPath + "/" + innerFolderName);
                    List<Sound> musicLevel = new ArrayList<>();
                    while (resources.hasMoreElements()) {
                        URL url = resources.nextElement();
                        URI uri = url.toURI();
                        String scheme = uri.getScheme();
                        if (scheme.equals("jar")) {
                            try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap())) {
                                Path jarPath = fileSystem.getPath("/"+folderPath+"/" + innerFolderName);
                                Files.walk(jarPath, 1).forEach(path -> {
                                    if (!Files.isDirectory(path)) {
                                        musicLevel.add(new Sound(path.toString()));
                                    }
                                });
                            }
                        } else {
                            Files.list(Paths.get(uri)).forEach(path -> {
                                if (Files.isRegularFile(path)) {
                                    musicLevel.add(new Sound(path.toString()));
                                }
                            });
                        }
                    }
                    allMusic.add(musicLevel);
                } catch (URISyntaxException | IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("allMusic: " + allMusic);
            return allMusic;
        }
        List<List<Sound>> allMusic = new ArrayList<>();
        File folder = new File(RESOURCES + folderPath);
        for (String innerFolderName : foldersNames) {
            File innerFolder = new File(folder.getPath() + "\\" + innerFolderName);
            if (innerFolder.isDirectory()) {
                List<Sound> musicLevel = new ArrayList<>();
                for (File musicFile : Objects.requireNonNull(innerFolder.listFiles())) {
                    if (musicFile.isFile()) {
                        musicLevel.add(new Sound(musicFile.getPath()));
                    }
                }
                allMusic.add(musicLevel);
            }
        }
        return allMusic;
    }


    public List<Integer> setNumbers(List<List<Sound>> sounds) {
        Random random = new Random();
        List<Integer> returnList = new ArrayList<>();
        for (List<Sound> list : sounds) {
            if (list.size() > 0)
                returnList.add(random.nextInt(0, list.size()));
        }
        return returnList;
    }

    public void playMusic(int musicState) {
        this.currentPlay = musicState;
        MUSIC_NUMBER.set(this.currentPlay, (MUSIC_NUMBER.get(this.currentPlay) + 1) % MUSIC.get(this.currentPlay).size());
        this.MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(this.currentPlay)).setVolume(musicVolume);
        this.MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(this.currentPlay)).play();
        this.paused = false;
    }

    public void resetAndStopMusic() {
        this.MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(this.currentPlay)).resetAndStop();
        resetAndStopAll();
    }
    public  void resetAndStopAll(){
        for (List<Sound> list:MUSIC) {
            for(Sound sound:list){
                sound.resetAndStop();
            }
        }
    }

    public void playPauseMusic() {
        if (!paused) {
            this.MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(this.currentPlay)).pause();
            this.paused = true;

        } else {
            this.MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(this.currentPlay)).play();
            this.paused = false;
        }

    }

    public void playPreviousMusic() {
        resetAndStopMusic();
        MUSIC_NUMBER.set(currentPlay, MUSIC_NUMBER.get(currentPlay) + 2 * MUSIC.get(currentPlay).size() - 2);
        playMusic(this.currentPlay);
    }

    public void playNextMusic() {
        this.paused = true;
        resetAndStopMusic();
        playMusic(this.currentPlay);
    }

    public void setMusicVolume(float volume) {
        MUSIC.get(currentPlay).get(MUSIC_NUMBER.get(currentPlay)).setVolume(volume);
        this.musicVolume = volume;
    }
    public void setSoundVolume(float volume) {
        this.soundVolume = volume;
    }

    public void playNextIfNeeded() {
        if (!paused && !MUSIC.get(this.currentPlay).get(MUSIC_NUMBER.get(currentPlay)).isActive()) {
            playNextMusic();
        }
    }

    public void playHomeBackgroundMusic() {
        this.currentPlay = 2;
        if (!paused)
            playMusic(this.currentPlay);
    }

    public void playGameFirstMusic() {
        this.currentPlay = 0;
        if (!paused)
            playMusic(this.currentPlay);
    }

    public void playGameFinalMusic() {
        this.currentPlay = 1;
        if (!paused)
            playMusic(this.currentPlay);
    }

    public void playHomeWinnerMusic() {
        this.currentPlay = 3;
        if (!paused)
            playMusic(this.currentPlay);
    }

    public void playSound(int soundState) {
        SOUND_EFFECTS_NUMBER.set(soundState, (SOUND_EFFECTS_NUMBER.get(soundState) + 1) % SOUND_EFFECTS.get(soundState).size());
        this.SOUND_EFFECTS.get(soundState).get(SOUND_EFFECTS_NUMBER.get(soundState)).setVolume(soundVolume);
        this.SOUND_EFFECTS.get(soundState).get(SOUND_EFFECTS_NUMBER.get(soundState)).rePlay();
    }

    public void playSoundMagic(int soundState) {
        SOUND_EFFECTS_MAGIC_NUMBER.set(soundState, (SOUND_EFFECTS_MAGIC_NUMBER.get(soundState) + 1) % SOUND_EFFECTS_MAGIC.get(soundState).size());
        this.SOUND_EFFECTS_MAGIC.get(soundState).get(SOUND_EFFECTS_MAGIC_NUMBER.get(soundState)).setVolume(soundVolume);
        this.SOUND_EFFECTS_MAGIC.get(soundState).get(SOUND_EFFECTS_MAGIC_NUMBER.get(soundState)).rePlay();
    }

    public void playHitNormalSounds() {
        playSound(0);
    }

    public void playFallingNormalSounds() {
        playSound(1);
    }

    public void playShootingNormalSound() {
        playSound(2);
    }

    public void playFallingMagicSounds() {
        playSoundMagic(3);

    }

    public void playHitMagicSounds() {
        playSoundMagic(2);
    }

    public void playFreezeSound() {
        playSoundMagic(4);
    }

    public void playBombSound() {
        playSoundMagic(1);
    }

    public void playBombCollisionSound() {
        playSoundMagic(0);
    }

    public void playShootingMagicSound() {
        playSoundMagic(5);
    }
}