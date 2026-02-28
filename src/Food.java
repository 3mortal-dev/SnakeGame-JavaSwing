import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Food {
  private Point position = new Point();
  private Image img;

  public Food(ArrayList<Point> snakeBody) {
    URL imgURL = getClass().getResource("/date.png");
    if (imgURL != null) {
      img = new ImageIcon(imgURL).getImage();
    } else {
      System.err.println("date.png not found!");
    }
    this.respawn(snakeBody);
  }

  public void respawn(ArrayList<Point> snakeBody) {
    Random rand = new Random();
    int col;
    int row;
    do {
      col = rand.nextInt(GridRenderer.COLS);
      row = rand.nextInt(GridRenderer.ROWS);
    } while (snakeBody.contains(new Point(col, row)));
    this.position = new Point(col, row);
  }

  public Point getPosition() {
    return position;
  }

  public Image getImg() {
    return img;
  }
}
