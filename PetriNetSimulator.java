// initialisation file
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import simComponents.*;

public class PetriNetSimulator {
  public static void main(String[] args) {
    System.out.println("Petri Net Simulator");

    ArrayList<Transition> transitions = new ArrayList<Transition>();
    ArrayList<Place> places = new ArrayList<Place>();
    
    JFrame simulator = new JFrame("Petri Net Simulator");
    simulator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // check whether this can be replaced with a function to save automatically
    simulator.setSize(1000, 1000);
    JMenuBar menuBar = new JMenuBar();
    // Debugging
    JMenu mbDebug = new JMenu("DEBUG");
    JMenuItem dispPlaces = new JMenuItem("Display places");
    mbDebug.add(dispPlaces);
    dispPlaces.addActionListener(e -> displayPlacesArray(places));
    menuBar.add(mbDebug);
    JMenuItem dispTransitions = new JMenuItem("Display transitions");
    mbDebug.add(dispTransitions);
    dispTransitions.addActionListener(e -> displayTransitionArray(transitions));
    

    // File
    JMenu mbFile = new JMenu("FILE");
    JMenuItem save = new JMenuItem("Save");
    JMenuItem saveAs = new JMenuItem("Save as");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem newFile = new JMenuItem("New");
    mbFile.add(newFile);
    mbFile.add(open);
    mbFile.add(save);
    mbFile.add(saveAs);
    // Add
    JMenu mbAdd = new JMenu("ADD");
    JMenuItem addArc = new JMenuItem("Add arc");
    JMenuItem addPlace = new JMenuItem("Add place");
    JMenuItem addTransition = new JMenuItem("Add transition");
    mbAdd.add(addArc);
    mbAdd.add(addPlace);
    mbAdd.add(addTransition);
    addArc.addActionListener(e -> addNewArc());
    addPlace.addActionListener(e -> addNewPlace(places));
    addTransition.addActionListener(e -> addNewTransition(transitions));

    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    
    simulator.getContentPane().add(BorderLayout.NORTH, menuBar);
    simulator.setVisible(true);
    
  }
  
  public static void addNewArc() {
    System.out.println("AddnewArc");
  }
  // ######### TODO #############
  // after clicking add new arc, the next place (or transition) to be clicked will be the origin, and then the next clicked transition (or place) will be the destination
  // do checks for whether clicking on the right one the second time
  // place and transition action listeners need to check whether adding an arc when clicked on

  // could possibly have the user select a place and transition and then click add arc, have a default direction and they have the option to flip it


  public static void addNewPlace(ArrayList<Place> places) { 
    Place placeToAdd = new Place(new String[] {"Place " + (places.size() + 1)}); // pass in a default label for the new place
    placeToAdd.addActionListener(e -> placeClicked());
    places.add(placeToAdd);
  }

  public static void addNewTransition(ArrayList<Transition> transitions) {
    Transition transitionToAdd = new Transition(new String[] {"Transition " + (transitions.size() + 1)}); // pass in a default label for the new transition 
    transitionToAdd.addActionListener(e -> transitionClicked());
    transitions.add(transitionToAdd);
  }






  // debugging functions

  public static void displayPlacesArray(ArrayList<Place> places) {
    System.out.println("Items in places array: ");
    for (int i=0; i<places.size(); i++) {
      System.out.print(i + ": " + places.get(i).getLabel() + ", ");
    }
    System.out.println("\nEnd of Places array");
  }


  public static void displayTransitionArray(ArrayList<Transition> transitions) {
    System.out.println("Items in transitions array: ");
    for (int i=0; i<transitions.size(); i++) {
      System.out.print(i + ": " + transitions.get(i).getLabel() + ", ");
    }
    System.out.println("\nEnd of transitions array");
  }




 }

