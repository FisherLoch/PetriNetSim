package simComponents;

import java.util.UUID;

public class Arc {
  String ID;
  int weight = 1;
  String origin;
  String endpoint;
  String label;
  
  public Arc() { // constructor
    ID = UUID.randomUUID().toString();
    label = "1";
  }

  public String getID() {
    return ID;
  }

  public String getLabel() {
    return label;
  }

  public void addWeight(int weightToAdd) {
    weight = weight + weightToAdd;
    label = Integer.toString(weight);
  }

  public int getWeight() {
    return weight;
  }

  public void setOrigin(String val) {
    origin = val;
  }

  public String getOrigin() {
    return origin;
  }

  public void setEndpoint(String val) {
    endpoint = val;
  }

  public String getEndpoint() {
    return endpoint;
  }

}
