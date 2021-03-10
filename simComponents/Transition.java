package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import simComponents.*;

public class Transition extends JComponent {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  String label;
  volatile int myX;
  volatile int myY;
  
  int width = 20;
  int height = 80;
  volatile int mouseX = 0;
  volatile int mouseY = 0;


  DiagramCanvas canvas;


  // properties array format: [label]
  public Transition(String[] properties, DiagramCanvas c) { // constructor
    ID = "Trans" + UUID.randomUUID().toString();
    label = properties[0];

    myX = (int) (Math.random() * 500);
    myY = (int) (Math.random() * 500);   


    canvas = c;

  }

  public String getID() {
    return ID;
  }

  public void setX(int x) {
    myX = x;
  }

  public void setY(int y) {
    myY = y;
  }
  
  public int getX() {
    return myX;
  }


  public int getY() {
    return myY;
  }


  public boolean inBounds(int x, int y) {
    if ((x >= myX) && (x <= myX + width)) {
      if ((y >= myY) && (y <= myY + height)) {
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
    g.fillRect(myX, myY, width, height);
    g.drawString(label, myX - 20, myY - 10);
  }



}
