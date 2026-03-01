import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {

  private GridRenderer gridRenderer = new GridRenderer();
  private Snake snake = new Snake();
  private Food food = new Food(snake.getBeads());

  public enum GameState {MENU, PLAYING, GAME_OVER, WIN}

  private GameState state = GameState.MENU;
  private int score = 0;

  // Win animation fields
  private float winImageScale = 0.0f;
  private float glowAlpha = 0.3f;
  private boolean glowGrowing = true;
  private ArrayList<float[]> particles = new ArrayList<>();
  private boolean particlesReady = false;

  private Timer masterTimer;
  private int gameTickCounter = 0;
  private static final int GAME_TICK_RATE = 9; // 9 * 16ms ≈ 150ms
  private static final Random rand = new Random();

  private Image currentWinImage;
  private Image[] winImages = new Image[5];

  public GamePanel() {
    this.setFocusable(true);
    this.requestFocusInWindow();

    this.loadWinImages();

    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_UP, KeyEvent.VK_W -> snake.changeDirection(Snake.Direction.UP);
          case KeyEvent.VK_DOWN, KeyEvent.VK_S -> snake.changeDirection(Snake.Direction.DOWN);
          case KeyEvent.VK_LEFT, KeyEvent.VK_A -> snake.changeDirection(Snake.Direction.LEFT);
          case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> snake.changeDirection(Snake.Direction.RIGHT);
          case KeyEvent.VK_R -> {
            if (state == GameState.GAME_OVER || state == GameState.WIN) GamePanel.this.resetGame();
          }
          case KeyEvent.VK_SPACE -> {
            if (state == GameState.MENU) state = GameState.PLAYING;
          }
        }
      }
    });

    masterTimer = new Timer(16, _ -> {
      if (state == GameState.PLAYING) {
        gameTickCounter++;
        if (gameTickCounter >= GAME_TICK_RATE) {
          gameTickCounter = 0;
          updateGame();
        }
      }
      if (state == GameState.MENU || state == GameState.WIN) {
        updateWinAnimation();
      }
      repaint();
    });
    masterTimer.start();

  }

  private void updateGame() {
    if (snake.getHead().equals(food.getPosition())) {
      snake.grow();
      food.respawn(snake.getBeads());
      score++;
      SoundManager.play("eat.wav");
      if (score >= 15) {
        state = GameState.WIN;
        SoundManager.play("win.wav");
        startWinAnimation();
        return;
      }
    } else {
      snake.move();
    }
    if (snake.isCollidingWithSelf() || snake.isOutOfBounds()) {
      state = GameState.GAME_OVER;
      SoundManager.play("death.wav");
    }
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
      drawMenu(g2d);                                  // only menu
    } else if (state == GameState.GAME_OVER || state == GameState.PLAYING) {
      gridRenderer.draw(g2d, getWidth(), getHeight(), snake, food); // grid + snake
      drawScore(g2d);                                   // score
      if (state == GameState.GAME_OVER) drawGameOver(g2d); // overlay
    } else if (state == GameState.WIN) {
      drawWin(g2d);
    }
  }

  private void loadWinImages() {
    for (int i = 1; i <= 5; i++) {
      URL url = getClass().getResource("/t3b2a/win" + i + ".jpg");
      if (url != null) {
        winImages[i - 1] = new ImageIcon(url).getImage();
      } else {
        System.err.println("win" + i + ".png not found!");
      }
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
    gameTickCounter = 0;
    winImageScale = 0.0f;
    snake = new Snake();
    food = new Food(snake.getBeads());
    score = 0;
    state = GameState.PLAYING;
  }

  private void drawMenu(Graphics2D g2d) {
    if (!particlesReady) {
      generateParticles();
      particlesReady = true;
    }
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (float[] p : particles) {
      g2d.setColor(new Color(1f, 0.85f, 0.2f, Math.min(p[4], 1f)));
      g2d.fillOval((int) p[0], (int) p[1], (int) p[3], (int) p[3]);
    }

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
    String text = "Dates Collected: " + score;
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    FontMetrics fm = g2d.getFontMetrics();
    g2d.drawString(text, (getWidth() - fm.stringWidth(text)) / 2, 30);
  }

  // Win Animation functions
  private void generateParticles() {
    particles.clear();
    for (int i = 0; i < 60; i++) {
      float x = rand.nextFloat() * getWidth();
      float y = rand.nextFloat() * getHeight();
      float speed = 1.5f + rand.nextFloat() * 3f;
      float size = 4f + rand.nextFloat() * 8f;
      float alpha = 0.5f + rand.nextFloat() * 0.5f;
      particles.add(new float[]{x, y, speed, size, alpha});
    }
  }

  private void updateWinAnimation() {
    // zoom in
    if (winImageScale < 1.0f) winImageScale += 0.04f;
    else winImageScale = 1.0f;

    // glow pulse
    if (glowGrowing) glowAlpha += 0.02f;
    else glowAlpha -= 0.02f;
    if (glowAlpha >= 1.0f) glowGrowing = false;
    if (glowAlpha <= 0.3f) glowGrowing = true;

    // falling stars
    for (float[] p : particles) {
      p[1] += p[2]; // fall down
      if (p[1] > getHeight()) {
        p[1] = 0;
        p[0] = new Random().nextFloat() * getWidth();
      }
    }
  }

  private void startWinAnimation() {
    winImageScale = 0.0f;
    glowAlpha = 0.3f;
    currentWinImage = winImages[new Random().nextInt(winImages.length)];
    generateParticles();

  }

  private void drawWin(Graphics2D g2d) {
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // dark overlay
    g2d.setColor(new Color(0, 0, 0, 180));
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // falling stars
    for (float[] p : particles) {
      g2d.setColor(new Color(1f, 0.85f, 0.2f, Math.min(p[4], 1f)));
      g2d.fillOval((int) p[0], (int) p[1], (int) p[3], (int) p[3]);
    }

    int cx = getWidth() / 2;
    int cy = getHeight() / 2;
    int imgSize = (int) (200 * winImageScale);

    // golden glow behind image
    if (imgSize > 0) {
      int glowSize = imgSize + 60;
      g2d.setColor(new Color(1f, 0.85f, 0.1f, glowAlpha * 0.4f));
      g2d.fillOval(cx - glowSize / 2, cy - glowSize / 2, glowSize, glowSize);

      g2d.setColor(new Color(1f, 0.95f, 0.3f, glowAlpha * 0.2f));
      int outerGlow = imgSize + 100;
      g2d.fillOval(cx - outerGlow / 2, cy - outerGlow / 2, outerGlow, outerGlow);
    }

    // win image zooming in
    if (currentWinImage != null && imgSize > 0) {
      g2d.drawImage(currentWinImage,
              cx - imgSize / 2,
              cy - imgSize / 2 - 30,
              imgSize, imgSize, null);
    }

    // "You Win!" text
    g2d.setFont(new Font("Arial", Font.BOLD, 42));
    g2d.setColor(new Color(240, 200, 80));
    FontMetrics fm = g2d.getFontMetrics();
    String winText = "You Win! 🎉";
    g2d.drawString(winText, cx - fm.stringWidth(winText) / 2, cy + 140);

    // score
    g2d.setFont(new Font("Arial", Font.BOLD, 20));
    g2d.setColor(Color.WHITE);
    fm = g2d.getFontMetrics();
    String scoreText = "Dates Collected: " + score;
    g2d.drawString(scoreText, cx - fm.stringWidth(scoreText) / 2, cy + 175);

    // restart hint
    g2d.setFont(new Font("Arial", Font.PLAIN, 16));
    g2d.setColor(new Color(200, 200, 200));
    fm = g2d.getFontMetrics();
    String restart = "Press R to Play Again";
    g2d.drawString(restart, cx - fm.stringWidth(restart) / 2, cy + 210);
  }
}