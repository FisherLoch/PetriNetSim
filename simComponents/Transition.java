package simComponents;

import java.util.ArrayList;
import java.util.UUID;

public class Transition {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  String label;


  // properties array format: [label]
  public Transition(String[] properties) { // constructor
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
    // check whether arc is already in?
  }

  public void removeIncomingArc(String arcID) {
    // wont work right now, search for arc, remove index of
    incomingArcs.remove(arcID);
  }

  public void addOutgoingArc(String arcID) {
    outgoingArcs.add(arcID);
  }

  public void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  public boolean isEnabled() {
    // check that for every incoming arc that the place it originates from has tokens >= the weight of the incoming arc
    return false;
  }

}
