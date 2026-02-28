import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class GameWindow extends JFrame {
  public GameWindow() {
    setTitle("Ramadan Snake");
    setSize(600, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setLocationRelativeTo(null);
    getContentPane().setBackground(Color.BLACK);

    // load icon
    URL iconURL = getClass().getResource("/Snake.png");
    if (iconURL != null) {
      Image img = new ImageIcon(iconURL).getImage();

      // Windows/Linux taskbar icon
      setIconImage(img);

      // Mac dock icon
      try {
        if (Taskbar.isTaskbarSupported()) {
          Taskbar taskbar = Taskbar.getTaskbar();
          if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            taskbar.setIconImage(img);
          }
        }
      } catch (Exception e) {
        System.out.println("Mac Taskbar not supported.");
      }

    } else {
      System.err.println("Icon not found!");
    }

    setVisible(true); // always last!
  }
}