import java.awt.*;
import java.util.ArrayList;

public class Snake {
  private ArrayList<Point> snakeList = new ArrayList<>();

  public enum Direction {
    UP, DOWN, LEFT, RIGHT;
  }

  private Direction currentDirection = Direction.DOWN;
  private Direction buffer = Direction.DOWN; // Prevents delays for opposite direction movments
  private boolean directionChanged = false; // lock flag

  private int startCol = GridRenderer.COLS / 2;
  private int startRow = GridRenderer.ROWS / 2;

  public Snake() {
    this.snakeList.add(new Point(startCol, startRow));
    this.snakeList.add(new Point(startCol, startRow - 1));
    this.snakeList.add(new Point(startCol, startRow - 2));

  }

  public ArrayList<Point> getBeads() {
    return this.snakeList;
  }

  public void changeDirection(Direction d) {
    // Ignore if I'm in the same direction
    if (this.directionChanged) return;
    if (this.buffer == d) {
      return;
    }

    // If I'm up I can't go down
    else if ((this.buffer == Direction.UP || this.buffer == Direction.DOWN) && (d == Direction.UP || d == Direction.DOWN)) {
      return;
    }
    // If I'm left I can't go right
    else if ((this.buffer == Direction.LEFT || this.buffer == Direction.RIGHT) && (d == Direction.LEFT || d == Direction.RIGHT)) {
      return;
    }
    this.buffer = d;
    this.directionChanged = true;
  }

  public void move() {
    this.currentDirection = this.buffer;
    this.directionChanged = false;
    Point next = getNextPoint();
    this.snakeList.addFirst(next);
    this.snakeList.removeLast();
  }

  private Point getNextPoint() {
    Point head = this.snakeList.getFirst();
    switch (this.currentDirection) {
      case UP -> {
        return new Point(head.x, head.y - 1);
      }
      case DOWN -> {
        return new Point(head.x, head.y + 1);
      }
      case RIGHT -> {
        return new Point(head.x + 1, head.y);
      }
      case LEFT -> {
        return new Point(head.x - 1, head.y);
      }
    }
    return head;
  }

}
