import java.awt.*;
import java.util.ArrayList;

public class GridRenderer {
  static int ROWS = 10;
  static int COLS = 10;
  static int CELL_SIZE = 50;

  public void draw(Graphics g, int width, int height, Snake snake, Food food) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int X = (width - COLS * CELL_SIZE) / 2;
    int Y = (height - ROWS * CELL_SIZE) / 2;

    g2d.setColor(new Color(255, 255, 255, 40)); // Grid Color

    // horizontal lines
    for (int row = 0; row <= ROWS; row++) {
      int y = Y + row * CELL_SIZE;
      g2d.drawLine(X, y, X + COLS * CELL_SIZE, y);
    }

    // vertical lines
    for (int col = 0; col <= COLS; col++) {
      int x = X + col * CELL_SIZE;
      g2d.drawLine(x, Y, x, Y + ROWS * CELL_SIZE);
    }
    for (int i = 0; i < snake.getBeads().size(); i++) {
      int beadX = X + getX(snake, i) * CELL_SIZE;
      int beadY = Y + getY(snake, i) * CELL_SIZE;
      if (i == 0) {
        drawHead(g2d, beadX, beadY);
      } else {
        drawBead(g2d, beadX, beadY);
      }
    }
    drawFood(g2d, food, X, Y);
  }

  private static int getX(Snake snake, int i) {
    return snake.getBeads().get(i).x;
  }

  private static int getY(Snake snake, int i) {
    return snake.getBeads().get(i).y;
  }

  private void drawBead(Graphics2D g2d, int x, int y) {
    // shadow
    g2d.setColor(new Color(0, 0, 0, 80));
    g2d.fillOval(x + 7, y + 7, CELL_SIZE - 10, CELL_SIZE - 10);

    // base bead color — warm golden
    g2d.setPaint(new GradientPaint(
            x, y, new Color(210, 170, 100, 80),          // light gold
            x + CELL_SIZE, y + CELL_SIZE, new Color(150, 100, 50)  // dark brown
    ));
    g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);

    // shine highlight
    g2d.setColor(new Color(255, 255, 255, 80));
    g2d.fillOval(x + 10, y + 8, 10, 7);
  }

  private void drawHead(Graphics2D g2d, int x, int y) {
    // same as bead but brighter
    g2d.setPaint(new GradientPaint(
            x, y, new Color(240, 200, 80, 95),
            x + CELL_SIZE, y + CELL_SIZE, new Color(180, 120, 40)
    ));
    g2d.fillOval(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10);

    // shine
    g2d.setColor(new Color(255, 255, 255, 100));
    g2d.fillOval(x + 10, y + 8, 10, 7);
  }

  private void drawFood(Graphics2D g2d, Food food, int startX, int startY) {
    int x = startX + food.getPosition().x * CELL_SIZE;
    int y = startY + food.getPosition().y * CELL_SIZE;
    g2d.drawImage(food.getImg(), x, y, CELL_SIZE, CELL_SIZE, null);
  }
}
