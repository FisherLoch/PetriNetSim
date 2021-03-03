package simComponents;

import java.util.UUID;

public class TransitionData {
  int originX;
  int originY;

  
  public TransitionData(int x, int y) { // constructor
    originY = y;
    originX = x;
  }

  public int getX() {
    return originX;
  }

  public int getY() {
    return originY;
  }
 
}
