package simComponents;

import java.util.UUID;

public class TransitionData {
  int originX;
  int originY;
  int width;
  int height;

  
  public TransitionData(int x, int y, int w, int h) { // constructor
    originY = y;
    originX = x;
    width = w;
    height = h;
  }

  public int getX() {
    return originX;
  }

  public int getY() {
    return originY;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
 
}
