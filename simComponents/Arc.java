package simComponents;

import java.util.UUID;

public class Arc {
  String ID;
  int weight = 1;
  String origin;
  String endpoint;
  
  public Arc(String[] properties) { // constructor
    ID = UUID.randomUUID().toString();
  }

  public String getID() {
    return ID;
  }

  void addWeight(int weightToAdd) {
    weight = weight + weightToAdd;
  }

  void setOrigin(String val) {
    origin = val;
  }

  public String getOrigin() {
    return origin;
  }

  void setEndpoint(String val) {
    endpoint = val;
  }

  public String setEndpoint() {
    return endpoint;
  }

}
