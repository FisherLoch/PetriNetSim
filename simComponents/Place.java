package simComponents;

import java.util.ArrayList;

public class Place {
  int ID;
  ArrayList<Arc> outgoingArcs = new ArrayList<Arc>();
  ArrayList<Arc> incomingArcs = new ArrayList<Arc>();
  int tokens = 0;

  public Place() { // constructor
    
  }

  void addIncomingArc(String[] properties) {
    incomingArcs.add(new Arc(properties)); // change to create arc and set arcID as location with content arc
  }

  void removeIncomingArc(String arcID) {
    incomingArcs.remove(arcID);
  }

  void addOutgoingArc(String[] properties) {
    outgoingArcs.add(new Arc(properties));
  }

  void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  void addTokens(int tokensToAdd) { // can use this to add and remove with negative values but might add superfluous removeTokens function for clarity
    tokens = tokens + tokensToAdd;
  }


}
