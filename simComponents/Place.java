package simComponents;

import java.util.ArrayList;
import java.util.UUID;

public class Place {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  int tokens = 0;
  String label;

  // properties array format: [label]
  public Place(String[] properties) { // constructor
    ID = UUID.randomUUID().toString();
    label = properties[0];
  }

  public String getID() {
    return ID;
  }

  public String getLabel() {
    return label;
  }
  
  public void setLabel(String newLabel) {
    label = newLabel;
  }

  public void addIncomingArc(String arcID) {
    incomingArcs.add(arcID); // change to create arc and set arcID as location with content arc
  }

  public void removeIncomingArc(String arcID) {
    incomingArcs.remove(arcID);
  }

  public void addOutgoingArc(String arcID) {
    outgoingArcs.add(arcID);
  }

  public void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  public void addTokens(int tokensToAdd) { // can use this to add and remove with negative values but might add superfluous removeTokens function for clarity
    tokens = tokens + tokensToAdd;
  }


}
