package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import java.awt.*;

import java.util.regex.*;

public class Place {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  int tokens = 0;
  String label;
  volatile int centreX;
  volatile int centreY;
  int radius = 50;

  // properties array format: [String label], String ID, int tokens, int centreX, int centreY, ArrayList<String> outArcs, ArrayList<String> incArcs
  public Place(String[] properties) { // constructor
    ID = "Place" + UUID.randomUUID().toString();
    label = properties[0];
    //tokens = Integer.parseInt(properties[0].replaceAll("\\D+", ""));
    centreX = (int) (Math.random() * 400);
    centreY = (int) (Math.random() * 400);
      
  }

  public Place(String l, String placeID, int t, int cX, int cY, ArrayList<String> outArcs, ArrayList<String> incArcs) {
    ID = placeID;
    outgoingArcs = outArcs;
    incomingArcs = incArcs;
    tokens = t;
    label = l;
    centreX = cX;
    centreY = cY;
  }

  public String getID() {
    return ID;
  }

  public void setX(int x) {
    centreX = x;
  }

  public int getX() {
    return centreX;
  }

  public void setY(int y) {
    centreY = y;
  }

  public int getY() {
    return centreY;
  }

  public int getRadius() {
    return radius;
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

  public void setTokens(int t) {
    tokens = t;
  }

  public ArrayList<String> getIncomingArcsList() {
    return incomingArcs;
  }

  public ArrayList<String> getOutgoingArcsList() {
    return outgoingArcs;
  }

  public boolean inBounds(int x, int y) {
    if (Math.pow((x - centreX), 2) + Math.pow((y - centreY), 2) <= Math.pow(radius, 2)) {
      return true;
    }
    return false;
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
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new BasicStroke(3));
		g2.drawArc((int)(centreX - radius),(int)(centreY - radius), (int)(radius*2), (int)(radius*2),0,360);
    g2.drawString(label, centreX, centreY - radius - 20);
    if (tokens < 5) {
      switch (tokens) {
        case 1:
          g2.fillOval(centreX, centreY, 10, 10);
          break;
        case 2:
          g2.fillOval(centreX - 10, centreY - 10, 10, 10);
          g2.fillOval(centreX + 10, centreY + 10, 10, 10);
          break;
        case 3:
          g2.fillOval(centreX - 10, centreY - 10, 10, 10);
          g2.fillOval(centreX + 10, centreY - 10, 10, 10);
          g2.fillOval(centreX + 10, centreY + 10, 10, 10);
          break;
        case 4:
          g2.fillOval(centreX - 10, centreY - 10, 10, 10);
          g2.fillOval(centreX + 10, centreY - 10, 10, 10);
          g2.fillOval(centreX + 10, centreY + 10, 10, 10);         
          g2.fillOval(centreX - 10, centreY + 10, 10, 10);
          break;
        default:
          break;
      }
          
    } else {
      g2.drawString("Tokens: " + tokens, centreX - 15, centreY);
    }

  }


}
