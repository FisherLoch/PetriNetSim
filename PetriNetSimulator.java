// initialisation file
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import simComponents.*;

public class PetriNetSimulator {
  public static void main(String[] args) {
    System.out.println("Petri Net Simulator");

    // might be worth storing the transitions, places and arcs in a hash table so they can be found more quickly, but this may make rendering more awkward
    ArrayList<Transition> transitions = new ArrayList<Transition>();
    ArrayList<Place> places = new ArrayList<Place>();
    ArrayList<Arc> arcs = new ArrayList<Arc>();
    
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

    // addArc.addActionListener(e -> addNewArc(arcs)); // either pass in origin and destination at this point or add a default arc and add the origin and destination later on
    addPlace.addActionListener(e -> addNewPlace(places));
    addTransition.addActionListener(e -> addNewTransition(transitions));

    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    
    simulator.getContentPane().add(BorderLayout.NORTH, menuBar);
    simulator.setVisible(true);
    
  }
  
  public static void addNewArc(String startObj, int transitionIndex, int placeIndex, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs) {  
    Arc arcToAdd = new Arc();
    
    if (startObj == "T") { // arc originates from transition and is going into a place
      
      transitions.get(transitionIndex).addOutgoingArc(arcToAdd.getID());
      arcToAdd.setOrigin(transitions.get(transitionIndex).getID());
      places.get(placeIndex).addIncomingArc(arcToAdd.getID());
      arcToAdd.setEndpoint(places.get(placeIndex).getID());

      arcs.add(arcToAdd);
      
    } else {
      places.get(placeIndex).addOutgoingArc(arcToAdd.getID());
      arcToAdd.setOrigin(places.get(placeIndex).getID());
      transitions.get(transitionIndex).addIncomingArc(arcToAdd.getID());
      arcToAdd.setEndpoint(transitions.get(transitionIndex).getID());

      arcs.add(arcToAdd);
      
    }
  }

  // ######### TODO #############
  // after clicking add new arc, the next place (or transition) to be clicked will be the origin, and then the next clicked transition (or place) will be the destination
  // do checks for whether clicking on the right one the second time
  // place and transition action listeners need to check whether adding an arc when clicked on

  // could possibly have the user select a place and transition and then click add arc, have a default direction and they have the option to flip it


  // handle source and sink transitions


  public static void addNewPlace(ArrayList<Place> places) { 
    Place placeToAdd = new Place(new String[] {"Place " + (places.size() + 1)}); // pass in a default label for the new place
    //placeToAdd.addActionListener(e -> placeClicked());
    places.add(placeToAdd);
  }

  public static void addNewTransition(ArrayList<Transition> transitions) {
    Transition transitionToAdd = new Transition(new String[] {"Transition " + (transitions.size() + 1)}); // pass in a default label for the new transition 
    //transitionToAdd.addActionListener(e -> transitionClicked());
    transitions.add(transitionToAdd);
  }


  public static void removeTransition(String transID, ArrayList<Transition> transitions, ArrayList<Place> places, ArrayList<Arc> arcs) {
    // find transition with transition ID, then go through list of incoming and outgoing arcs and delete all of those arcs, finally, delete transition itself
    int transIndex = -1;
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID() == transID) {
        transIndex = i;
        break;
      }
    }
    
    ArrayList<String> outgoingArcsList = transitions.get(transIndex).getOutgoingArcsList();
    ArrayList<String> incomingArcsList = transitions.get(transIndex).getIncomingArcsList();

    for (int i=0; i<outgoingArcsList.size(); i++) {
      removeArc(outgoingArcsList.get(i), arcs, transitions, places);
    }
    for (int i=0; i<incomingArcsList.size(); i++) {
      removeArc(incomingArcsList.get(i), arcs, transitions, places);
    }
    transitions.remove(transIndex);
  }

  public static void removePlace(String placeID, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs) {
    // same process as removing a transition
    int placeIndex = -1;
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getID() == placeID) {
        placeIndex = i;
        break;
      }
    }

    ArrayList<String> incomingArcList = places.get(placeIndex).getIncomingArcsList();
    ArrayList<String> outgoingArcList = places.get(placeIndex).getOutgoingArcsList();

    for (int i=0; i<incomingArcList.size(); i++) {
      removeArc(incomingArcList.get(i), arcs, transitions, places);
    }
    for (int i=0; i<outgoingArcList.size(); i++) {
      removeArc(outgoingArcList.get(i), arcs, transitions, places);
    }
    places.remove(placeIndex);
    
  }

  // check for possible function to make this more efficient
  public static void removeArc(String arcID, ArrayList<Arc> arcs, ArrayList<Transition> transitions, ArrayList<Place> places) {

    // can possibly make these functions faster in general by returning when found and then breaking out of the loop, would require an extra couple of if statements though
 
    for (int i=0; i<transitions.size(); i++) {
      transitions.get(i).removeOutgoingArc(arcID);
      transitions.get(i).removeIncomingArc(arcID);
    }
    for (int i=0; i<places.size(); i++) {
      places.get(i).removeOutgoingArc(arcID);
      places.get(i).removeIncomingArc(arcID);
    }

    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getID() == arcID) {
        arcs.remove(i);
        break; // if found before last element, exit for loop to save time
      }
    }  
  }


  // will need to check for if multiple arcs come out of a single place and to add up the total weights before checking down the line (edge case)

  public boolean isTransitionEnabled(Transition t, ArrayList<Arc> arcs, ArrayList<Place> places) {
    ArrayList<String> arcsList = t.getIncomingArcsList();
    // check that for every incoming arc that the place it originates from has tokens >= the weight of the incoming arc
    if (arcsList.size() == 0) { // source transition
      return true;
    }

    ArrayList<PlaceIndexArcID> placeIndexes = new ArrayList<PlaceIndexArcID>();

    for (int i=0; i<arcsList.size(); i++) {
      for (int j=0; j<places.size(); j++) {
        if (places.get(j).containsOutgoingArc(arcsList.get(i))) { // if the place contains an outgoing arc with the current arcID in question
          placeIndexes.add(new PlaceIndexArcID(j, arcsList.get(i)));
        }
      }
    }

    for (int i=0; i<placeIndexes.size(); i++) {
      if (places.get(placeIndexes.get(i).getIndex()).getTokens() < getArcWeight(arcs, placeIndexes.get(i).getArcID())) {
        return false;
      }
    }

    return true;
  }

  public int getArcWeight(ArrayList<Arc> arcs, String arcID) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getID() == arcID) {
        return arcs.get(i).getWeight();
      }
    }

    System.out.println("getArcWeight: THIS LINE SHOULD NEVER BE REACHED");
    return -1;
  }


  // fire transition

  // add tokens button
  // change arc weight button
  // change label button
  // 

  // rendering








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

