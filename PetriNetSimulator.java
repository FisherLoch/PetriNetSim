// initialisation file
public class PetriNetSim {
  public static void main(String[] args) {
    System.out.println("Petri Net Simulator");
    
    
  }
  
  public static void initArrays() {
    
  }
  

}



public class Place {
  int ID;
  ArrayList<Integer> outgoingArcs = new ArrayList<Integer>();
  ArrayList<Integer> incomingArcs = new ArrayList<Integer>();
  int tokens = 0;

  void addIncomingArc(String properties) {
    incomingArcs.add(new Arc(properties)); // change to create arc and set arcID as location with content arc
  }

  void removeIncomingArc(String arcID) {
    incomingArcs.remove(arcID);
  }

  void addOutgoingArc(String properties) {
    outgoingArcs.add(new Arc(properties));
  }

  void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  void addTokens(int tokensToAdd) { // can use this to add and remove with negative values but might add superfluous removeTokens function for clarity
    tokens = tokens + tokensToAdd;
  }


}

public class Transition {
  int ID;
  ArrayList<Integer> outgoingArcs = new ArrayList<Integer>();
  ArrayList<Integer> incomingArcs = new ArrayList<Integer>();

  void addIncomingArc(String properties) {
    incomingArcs.add(new Arc(properties)); // change to create arc and set arcID as location with content arc
  }

  void removeIncomingArc(String arcID) {
    incomingArcs.remove(arcID);
  }

  void addOutgoingArc(String properties) {
    outgoingArcs.add(new Arc(properties));
  }

  void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  boolean isEnabled() {
    // check that for every incoming arc that the place it originates from has tokens >= the weight of the incoming arc
  }

}

public class Arc {
  int ID;
  int weight = 1;
  String origin;
  String endpoint;

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
