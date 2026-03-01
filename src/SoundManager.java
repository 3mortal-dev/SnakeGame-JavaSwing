import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {

  public static void play(String filename) {
    new Thread(() -> { // run on separate thread!
      try {
        URL url = SoundManager.class.getResource("/sounds/" + filename);
        if (url == null) return;
        AudioInputStream audio = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        clip.start();
        // close after done
        clip.addLineListener(e -> {
          if (e.getType() == LineEvent.Type.STOP) clip.close();
        });
      } catch (Exception e) {
        System.err.println("Sound error: " + e.getMessage());
      }
    }).start();
  }
}