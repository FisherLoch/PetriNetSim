package simComponents;

import java.util.UUID;

public class PlaceIndexArcID {
  String arcID;
  int index;
  
  public PlaceIndexArcID(int i, String ID) { // constructor
    index = i;
    arcID = ID;
  }

  public String getArcID() {
    return arcID;
  }

  public int getIndex() {
    return index;
  }
}
