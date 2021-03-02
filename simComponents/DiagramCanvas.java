package simComponents;

import java.awt.*;
import java.util.ArrayList;
import simComponents.*;

public class DiagramCanvas extends Canvas {
  
  ArrayList<Arc> arcsRenderList = new ArrayList<Arc>();
  ArrayList<Place> placesRenderList = new ArrayList<Place>();
  ArrayList<Transition> transitionsRenderList = new ArrayList<Transition>();


  public DiagramCanvas() {
    setBackground(Color.WHITE);
    setSize(500, 500);
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

  }
}
