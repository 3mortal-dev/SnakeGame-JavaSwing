import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Color top = new Color(10, 15, 40);
    Color bottom = new Color(25, 50, 90);
    Graphics2D g2d = (Graphics2D) g;
    // Gradient from (0,0) -> (0,height) => top to bottom
    // Not (0,0) -> (width,height) => diagonal
    g2d.setPaint(new GradientPaint(0, 0, top, 0, this.getHeight(), bottom));
    g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
  }
}