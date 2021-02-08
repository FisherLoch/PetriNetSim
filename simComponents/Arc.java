package simComponents;

public class Arc {
  int ID;
  int weight = 1;
  String origin;
  String endpoint;
  
  public Arc(String[] properties) { // constructor

  }

  void addWeight(int weightToAdd) {
    weight = weight + weightToAdd;
  }

  void setOrigin(String val) {
    origin = val;
  }

  void setEndpoint(String val) {
    endpoint = val;
  }

}
