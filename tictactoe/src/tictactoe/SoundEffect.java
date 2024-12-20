package tictactoe;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public enum SoundEffect {
    EAT_FOOD("audio/discord-notification.wav"),
    EXPLODE("audio/spongebob-fail.wav"),
    DIE("audio/gedagedigeda-go.wav");

    public enum Volume {
        MUTE, LOW, MEDIUM, HIGH
    }

    public static Volume volume = Volume.LOW;
    private Clip clip;

    private SoundEffect(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            if (url == null) {
                throw new RuntimeException("Sound file not found: " + soundFileName);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (IOException | RuntimeException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.err.println("Error loading sound: " + soundFileName);
        }
    }

    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void pause() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void resume() {
        if (!clip.isRunning()) {
            clip.start();
        }
    }

    public static void initGame() {
        values();
    }

    public static void setVolume(Volume vol) {
        volume = vol;
    }
}
