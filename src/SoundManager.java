import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class SoundManager {

  public static void play(String filename) {
    new Thread(() -> {
      try {
        InputStream is = SoundManager.class.getResourceAsStream("/sounds/" + filename);
        if (is == null) {
          System.err.println(filename + " not found!");
          return;
        }
        AudioInputStream audio = AudioSystem.getAudioInputStream(
                new BufferedInputStream(is)
        );
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        clip.addLineListener(e -> {
          if (e.getType() == LineEvent.Type.STOP) clip.close();
        });
      } catch (Exception e) {
        System.err.println("Sound error: " + e.getMessage());
      }
    }).start();
  }
}