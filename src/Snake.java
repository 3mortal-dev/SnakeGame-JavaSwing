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

  public Point getHead() {
    return snakeList.getFirst();
  }

  public void grow() {
    this.currentDirection = this.buffer;
    this.directionChanged = false;
    Point next = getNextPoint();
    this.snakeList.addFirst(next);
  }

  public void move() {
    this.grow();
    this.snakeList.removeLast();
  }

  public boolean isOutOfBounds() {
    Point head = this.getHead();
    return head.x < 0 || head.x >= GridRenderer.COLS ||
            head.y < 0 || head.y >= GridRenderer.ROWS;
  }

  public boolean isCollidingWithSelf() {
    return this.snakeList.subList(1, snakeList.size()).contains(snakeList.getFirst());
  }
}
