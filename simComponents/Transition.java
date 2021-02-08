package simComponents;

import java.util.ArrayList;
import simComponents.Arc;

public class Transition {
  int ID;
  ArrayList<Arc> outgoingArcs = new ArrayList<Arc>();
  ArrayList<Arc> incomingArcs = new ArrayList<Arc>();

  public Transition(String[] properties) { // constructor

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

  boolean isEnabled() {
    // check that for every incoming arc that the place it originates from has tokens >= the weight of the incoming arc
    return false;
  }

}
