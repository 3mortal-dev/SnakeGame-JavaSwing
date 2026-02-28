import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel {

  private GridRenderer gridRenderer = new GridRenderer();
  private Snake snake = new Snake();
  private Food food = new Food(snake.getBeads());

  public GamePanel() {
    this.setFocusable(true);
    this.requestFocusInWindow();
    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP, KeyEvent.VK_W -> snake.changeDirection(Snake.Direction.UP);
          case KeyEvent.VK_DOWN, KeyEvent.VK_S -> snake.changeDirection(Snake.Direction.DOWN);
          case KeyEvent.VK_LEFT, KeyEvent.VK_A -> snake.changeDirection(Snake.Direction.LEFT);
          case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> snake.changeDirection(Snake.Direction.RIGHT);
        }
      }

    });
    Timer timer = new Timer(150, _ -> {
      snake.move();
      repaint();
    });
    timer.start();
  }

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
    gridRenderer.draw(g2d, getWidth(), getHeight(), snake, food);
  }
}