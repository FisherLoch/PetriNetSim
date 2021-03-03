package simComponents;

import java.util.UUID;

public class PlaceData {
  int originX;
  int originY;
  int radius;

  
  public PlaceData(int x, int y, int rad) { // constructor
    originY = y;
    originX = x;
    radius = rad;
  }

  public int getX() {
    return originX;
  }

  public int getY() {
    return originY;
  }
  public int getRadius() {
    return radius;
  } 
}
