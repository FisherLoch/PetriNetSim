package simComponents;

import java.util.ArrayList;
import java.util.UUID;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Transition extends JComponent implements ActionListener {
  String ID;
  ArrayList<String> outgoingArcs = new ArrayList<String>();
  ArrayList<String> incomingArcs = new ArrayList<String>();
  String label;
  volatile int myX = 700;
  volatile int myY = 700;
  int width = 20;
  int height = 80;
  volatile int mouseX = 0;
  volatile int mouseY = 0;



  // properties array format: [label]
  public Transition(String[] properties) { // constructor
    ID = "Trans" + UUID.randomUUID().toString();
    label = properties[0];

    //originX = (int) (Math.random() * 500);
    //originY = (int) (Math.random() * 500);
    myX = (int) (Math.random() * 500);
    myY = (int) (Math.random() * 500);   
  
    setBorder(new LineBorder(Color.BLUE, 10));
    setBackground(Color.RED);
    setBounds(myX, myY, width, height);
    setOpaque(false);


    addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        mouseX = e.getXOnScreen();
        mouseY = e.getYOnScreen();

        myX = getX();
        myY = getY();
        System.out.println("Mouse pressed: T");
      }


      public void mouseClicked(MouseEvent e) {
        System.out.println("MouseClicked: T");
      }
      public void mouseReleased(MouseEvent e) {
        System.out.println("MouseReleased: T");
      }
      public void mouseEntered(MouseEvent e) {
        System.out.println("MouseEntered: T");
      }
      public void mouseExited(MouseEvent e) {}



    });

    addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {
        int diffX = e.getXOnScreen() - mouseX;
        int diffY = e.getYOnScreen() - mouseY;

        setLocation(myX + diffX, myY + diffY);
      }

      public void mouseMoved(MouseEvent e) {}
    });
  }

  public String getID() {
    return ID;
  }
/*
  public void setX(int x) {
    originX = x;
  }

  public int getX() {
    return originX;
  }

  public void setY(int y) {
    originY = y;
  }

  public int getY() {
    return originY;
  }
*/
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
    //g.fillRect(originX, originY, width, height);
    //g.drawString(label, originX - 20, originY - 10);
  }


  public void actionPerformed(ActionEvent e) {
    System.out.println("Transition action: " + e);
  }

}
