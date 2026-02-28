// Main.java
// └── GameWindow.java (JFrame)
//      ├── MenuPanel.java    → start screen
//      ├── GamePanel.java    → actual game
//      │     ├── Snake.java       → snake logic
//      │     ├── Food.java        → food spawning
//      │     └── GameRenderer.java → drawing
//      └── GameOverPanel.java → game over screen
public class Main {
  public static void main(String[] args) {
    new GameWindow();
  }
}