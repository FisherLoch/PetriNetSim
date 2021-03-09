package simComponents;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import simComponents.*;

public class DiagramCanvas extends JPanel {
  
  ArrayList<Arc> arcsRenderList = new ArrayList<Arc>();
  ArrayList<Place> placesRenderList = new ArrayList<Place>();
  ArrayList<Transition> transitionsRenderList = new ArrayList<Transition>();
  
  JFrame sim;

  volatile int mouseX;
  volatile int mouseY;


  public DiagramCanvas(JFrame s) {
    setBackground(Color.WHITE);
    setSize(500, 500);
    setBounds(0, 0, 500, 500);
    setOpaque(true);
    sim = s;
 
    addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        System.out.println("Mouse pressed: T");
      }


      public void mouseClicked(MouseEvent e) {
        System.out.println("MouseClicked");
       // repaint();
      }
      public void mouseReleased(MouseEvent e) {
        System.out.println("MouseReleased");
        int diffX = e.getX() - mouseX;
        int diffY = e.getY() - mouseY;

        moveComponent(mouseX, mouseY, diffX, diffY);
        //repaint();
      }

      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }



    });

    addMouseMotionListener(new MouseMotionListener() {
      public void mouseDragged(MouseEvent e) {

      }

      public void mouseMoved(MouseEvent e) {}
    });



  }


  public void moveComponent(int clickX, int clickY, int diffX, int diffY) {
    System.out.println("ClickX: " + clickX + " ClickY: " + clickY + " DiffX: " + diffX + " DiffY: " + diffY);
    for (int i=0; i<transitionsRenderList.size(); i++) {
      if (transitionsRenderList.get(i).inBounds(clickX, clickY)) {
        transitionsRenderList.get(i).setX(transitionsRenderList.get(i).getX() + diffX);
        transitionsRenderList.get(i).setY(transitionsRenderList.get(i).getY() + diffY);
        sim.repaint();
        break;
      }
    }
  }

  public static void main(String[] args) {
    
  }

  public void setArcs(ArrayList<Arc> arcs) {
    arcsRenderList = arcs;
  }

  public void setPlaces(ArrayList<Place> places) {
    placesRenderList = places;
  }

  public void setTransitions(ArrayList<Transition> transitions) {
    transitionsRenderList = transitions;
  }

  public void paint(Graphics g) {
    // for all objects to be rendered, render    
    for (int i=0; i<placesRenderList.size(); i++) {
      placesRenderList.get(i).render(g);
    }
    for (int i=0; i<transitionsRenderList.size(); i++) {
      transitionsRenderList.get(i).render(g);
    }
    for (int i=0; i<arcsRenderList.size(); i++) {
      if (arcsRenderList.get(i).getOrigin().contains("Place")) {
        PlaceData p = getPlaceData(arcsRenderList.get(i).getOrigin());
        TransitionData t = getTransitionData(arcsRenderList.get(i).getEndpoint());
        arcsRenderList.get(i).renderPlace(g, p.getX(), p.getY(), p.getRadius(), t.getX() + (t.getWidth()/2), t.getY() + (t.getHeight()/2)); // pass relevant rendering data
      } else {
        //arcsRenderList.get(i).renderTransition(g, placesRenderList, transitionsRenderList);
      }
    }

  }

  private PlaceData getPlaceData(String placeID) { 
    for (int i=0; i<placesRenderList.size(); i++) {
      if (placesRenderList.get(i).getID() == placeID) {
        return new PlaceData(placesRenderList.get(i).getX(), placesRenderList.get(i).getY(), placesRenderList.get(i).getRadius());
      }
    }
    return new PlaceData(0, 0, 0);
  }

  private TransitionData getTransitionData(String transID) {
    for (int i=0; i<transitionsRenderList.size(); i++) {
      if (transitionsRenderList.get(i).getID() == transID) {
        return new TransitionData(transitionsRenderList.get(i).getX(), transitionsRenderList.get(i).getY(), transitionsRenderList.get(i).getWidth(), transitionsRenderList.get(i).getHeight());
      }
    }
    return new TransitionData(0, 0, 0, 0);
  }

}
