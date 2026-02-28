import java.awt.*;
import java.util.ArrayList;

public class Snake {
  private ArrayList<Point> snakeList = new ArrayList<>();

  public enum Direction {
    UP, DOWN, LEFT, RIGHT;
  }

  private Direction currentDirection = Direction.DOWN;
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
    Direction current = this.currentDirection;
    // Ignore if I'm in the same direction
    if (current == d) {
      return;
    }

    // If I'm up I can't go down
    else if ((current == Direction.UP || current == Direction.DOWN) && (d == Direction.UP || d == Direction.DOWN)) {
      return;
    }
    // If I'm left I can't go right
    else if ((current == Direction.LEFT || current == Direction.RIGHT) && (d == Direction.LEFT || d == Direction.RIGHT)) {
      return;
    }
    this.currentDirection = d;
  }

  public void move() {
    Point next = getNextPoint();
    this.snakeList.addFirst(next);
    this.snakeList.removeLast();
  }

  private Point getNextPoint() {
    Point head = this.snakeList.getFirst();
    switch (currentDirection) {
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
