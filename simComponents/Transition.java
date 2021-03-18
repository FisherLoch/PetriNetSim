package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import simComponents.*;

public class Transition {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  String label;
  volatile int originX;
  volatile int originY;
  
  int width = 20;
  int height = 80;
  volatile int mouseX = 0;
  volatile int mouseY = 0;

  DiagramCanvas canvas;


  // properties array format: [label]
  public Transition(String[] properties, DiagramCanvas c) { // constructor
    ID = "Trans" + UUID.randomUUID().toString();
    label = properties[0];

    originX = (int) (Math.random() * 500);
    originY = (int) (Math.random() * 500);   

    canvas = c;

  }

  public Transition(String l, String i, int x, int y, DiagramCanvas c) {
    ID = i;
    label = l;
    originX = x;
    originY = y;
    canvas = c;
  }

  public String getID() {
    return ID;
  }

  public void setX(int x) {
    originX = x;
  }

  public void setY(int y) {
    originY = y;
  }
  
  public int getX() {
    return originX;
  }


  public int getY() {
    return originY;
  }


  public boolean inBounds(int x, int y) {
    if ((x >= originX) && (x <= originX + width)) {
      if ((y >= originY) && (y <= originY + height)) {
        return true;
      }
    }
    return false;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public String getLabel() {
    return label;
  }
  
  public void setLabel(String newLabel) {
    label = newLabel;
  }

  public ArrayList<String> getIncomingArcsList() {
    return incomingArcs;
  }

  public ArrayList<String> getOutgoingArcsList() {
    return outgoingArcs;
  }

  public void addIncomingArc(String arcID) {
    incomingArcs.add(arcID); // change to create arc and set arcID as location with content arc
    // check whether arc is already in?
  }

  public void removeIncomingArc(String arcID) {
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
    for (int i=0; i<outgoingArcs.size(); i++) {
      if (outgoingArcs.get(i) == arcID) {
        outgoingArcs.remove(i);
        break;
      }
    }
  }



// might not be needed yet
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

  public void render(Graphics g) {
    // draw and fill rectangle, can use a rotation value to determine the orientation
    g.setColor(Color.BLACK);
    g.fillRect(originX, originY, width, height);
    g.drawString(label, originX - 20, originY - 10);
  }



}
