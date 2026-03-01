import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

  private GridRenderer gridRenderer = new GridRenderer();
  private Snake snake = new Snake();
  private Food food = new Food(snake.getBeads());
  private Timer timer;

  public enum GameState {MENU, PLAYING, GAME_OVER}

  private GameState state = GameState.MENU;
  private int score = 0;

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
          case KeyEvent.VK_R -> {
            if (state == GameState.GAME_OVER) GamePanel.this.resetGame();
          }
          case KeyEvent.VK_SPACE -> {
            if (state == GameState.MENU) state = GameState.PLAYING;
          }
        }
      }
    });

    timer = new Timer(150, null);
    timer.addActionListener(_ -> {
      if (state != GameState.PLAYING) return;

      if (snake.getHead().equals(food.getPosition())) {
        snake.grow();
        food.respawn(snake.getBeads());
        score++;
      } else {
        snake.move();
      }
      if (snake.isCollidingWithSelf() || snake.isOutOfBounds()) {
        state = GameState.GAME_OVER;
        timer.stop();
      }
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

    if (state == GameState.MENU) {
      drawMenu(g2d);                                    // only menu
    } else {
      gridRenderer.draw(g2d, getWidth(), getHeight(), snake, food); // grid + snake
      drawScore(g2d);                                   // score
      if (state == GameState.GAME_OVER) drawGameOver(g2d); // overlay
    }
  }

  private void drawGameOver(Graphics2D g2d) {
    g2d.setColor(new Color(0, 0, 0, 150));
    g2d.fillRect(0, 0, getWidth(), getHeight());

    FontMetrics fm;

    // Game Over
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 48));
    fm = g2d.getFontMetrics();
    String gameOver = "Game Over";
    g2d.drawString(gameOver, (getWidth() - fm.stringWidth(gameOver)) / 2, getHeight() / 2 - 40);

    // Score
    g2d.setFont(new Font("Arial", Font.BOLD, 24));
    fm = g2d.getFontMetrics();
    String scoreText = "Dates Collected: " + score;
    g2d.drawString(scoreText, (getWidth() - fm.stringWidth(scoreText)) / 2, getHeight() / 2 + 10);

    // Restart hint
    g2d.setFont(new Font("Arial", Font.PLAIN, 18));
    fm = g2d.getFontMetrics();
    String restart = "Press R to Restart";
    g2d.setColor(new Color(200, 200, 200));
    g2d.drawString(restart, (getWidth() - fm.stringWidth(restart)) / 2, getHeight() / 2 + 50);
  }

  private void resetGame() {
    snake = new Snake();
    food = new Food(snake.getBeads());
    score = 0;
    state = GameState.PLAYING;
    timer.start();
  }

  private void drawMenu(Graphics2D g2d) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    FontMetrics fm;

    // Title — "Ramadan Snake"
    g2d.setFont(new Font("Arial", Font.BOLD, 52));
    g2d.setColor(new Color(240, 200, 80)); // golden color
    fm = g2d.getFontMetrics();
    String title = "Ramadan Snake";
    g2d.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 80);

    // Subtitle — crescent moon and lantern emojis
    g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
    fm = g2d.getFontMetrics();
    String emoji = "🌙 🏮 🌙";
    g2d.drawString(emoji, (getWidth() - fm.stringWidth(emoji)) / 2, getHeight() / 2 - 20);

    // Start hint
    g2d.setFont(new Font("Arial", Font.PLAIN, 20));
    g2d.setColor(new Color(200, 200, 200));
    fm = g2d.getFontMetrics();
    String start = "Press SPACE to Start";
    g2d.drawString(start, (getWidth() - fm.stringWidth(start)) / 2, getHeight() / 2 + 40);

    // Ramadan Kareem
    g2d.setFont(new Font("Arial", Font.ITALIC, 16));
    g2d.setColor(new Color(150, 150, 150));
    fm = g2d.getFontMetrics();
    String sub = "Ramadan Kareem ✨";
    g2d.drawString(sub, (getWidth() - fm.stringWidth(sub)) / 2, getHeight() / 2 + 80);
  }

  private void drawScore(Graphics2D g2d) {
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    g2d.drawString("Dates Collected: " + score, 315, 30);
  }
}