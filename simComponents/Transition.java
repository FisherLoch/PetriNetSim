package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import simComponents.Arc;

public class Transition {
  String ID;
  ArrayList<Arc> outgoingArcs = new ArrayList<Arc>();
  ArrayList<Arc> incomingArcs = new ArrayList<Arc>();
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


  public void addIncomingArc(String[] properties) {
    incomingArcs.add(new Arc(properties)); // change to create arc and set arcID as location with content arc
  }

  public void removeIncomingArc(String arcID) {
    incomingArcs.remove(arcID);
  }

  public void addOutgoingArc(String[] properties) {
    outgoingArcs.add(new Arc(properties));
  }

  public void removeOutgoingArc(String arcID) {
    outgoingArcs.remove(arcID);
  }

  public boolean isEnabled() {
    // check that for every incoming arc that the place it originates from has tokens >= the weight of the incoming arc
    return false;
  }

}
