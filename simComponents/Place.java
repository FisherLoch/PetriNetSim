package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import java.awt.*;

public class Place {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  int tokens = 0;
  String label;
  float centreX;
  float centreY;
  int radius = 100;

  // properties array format: [label]
  public Place(String[] properties) { // constructor
    ID = UUID.randomUUID().toString();
    label = properties[0];
    centreX = 300;
    centreY = 300;
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

  public int getTokens() { 
    return tokens;
  }

  public ArrayList<String> getIncomingArcsList() {
    return incomingArcs;
  }

  public ArrayList<String> getOutgoingArcsList() {
    return outgoingArcs;
  }

  public boolean containsOutgoingArc(String arcID) {
    for (int i=0; i<outgoingArcs.size(); i++) {
      if (outgoingArcs.get(i) == arcID) {
        return true;
      }
    }
    return false;
  }

  public boolean containsIncomingArc(String arcID) {
    for (int i=0; i<incomingArcs.size(); i++) {
      if (incomingArcs.get(i) == arcID) {
        return true;
      }
    }
    return false;
  }

  public void addIncomingArc(String arcID) {
    incomingArcs.add(arcID); // change to create arc and set arcID as location with content arc
  }

  public void removeIncomingArc(String arcID) {
  //public void removeIncomingArc(int arcIndex) {
    //incomingArcs.remove(arcIndex);
    for (int i=0; i<incomingArcs.size(); i++) {
      if (incomingArcs.get(i) == arcID) {
        incomingArcs.remove(i);
        break;
      }
    }
  }

  public void addOutgoingArc(String arcID) {
    outgoingArcs.add(arcID);
  }

  public void removeOutgoingArc(String arcID) {
  //public void removeOutgoingArc(int arcIndex) {
    //outgoingArcs.remove(arcIndex);
    for (int i=0; i<outgoingArcs.size(); i++) {
      if (outgoingArcs.get(i) == arcID) {
        outgoingArcs.remove(i);
        break;
      }
    }
  }


  public void addTokens(int tokensToAdd) { // can use this to add and remove with negative values but might add superfluous removeTokens function for clarity
    tokens = tokens + tokensToAdd;
  }


  public void render(Graphics g) {
    g.setColor(Color.BLACK);
		g.drawArc((int)(centreX - radius),(int)(centreY - radius), (int)(radius*2), (int)(radius*2),0,360);
  }


}
