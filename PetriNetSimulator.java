// initialisation file
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import simComponents.*;
import java.awt.event.*;

public class PetriNetSimulator {
  public static void main(String[] args) {
    System.out.println("Petri Net Simulator");

    // might be worth storing the transitions, places and arcs in a hash table so they can be found more quickly, but this may make rendering more awkward
    ArrayList<Transition> transitions = new ArrayList<Transition>();
    ArrayList<Place> places = new ArrayList<Place>();
    ArrayList<Arc> arcs = new ArrayList<Arc>();
  /* 
    canvas.addMouseListener(new MouseListener() {
      public void mousePressed(MouseEvent e) {
      }


      public void mouseClicked(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
        canvas.repaint();
      }

      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }



    });

*/



    JFrame simulator = new JFrame("Petri Net Simulator");
    DiagramCanvas canvas = new DiagramCanvas(simulator);

    simulator.setLayout(null);

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

    simulator.add(canvas);  
    

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

    // ** need action listeners for these

    // Add
    JMenu mbAdd = new JMenu("ADD");
    JMenuItem addArc = new JMenuItem("Add arc");
    JMenuItem addPlace = new JMenuItem("Add place");
    JMenuItem addTransition = new JMenuItem("Add transition");
    mbAdd.add(addArc);
    mbAdd.add(addPlace);
    mbAdd.add(addTransition);

    // addArc.addActionListener(e -> addNewArc(arcs)); // either pass in origin and destination at this point or add a default arc and add the origin and destination later on
    addPlace.addActionListener(e -> addNewPlace(places, canvas));
    addTransition.addActionListener(e -> addNewTransition(transitions, canvas));
    // addTokens.addActionListner(...

    // Edit
    JMenu mbEdit = new JMenu("EDIT");
    JMenuItem addTokens = new JMenuItem("Add tokens");
    JMenuItem changeLabel = new JMenuItem("Change label");
    JMenuItem addWeight = new JMenuItem("Add weight");
    mbEdit.add(addTokens);
    mbEdit.add(changeLabel);
    mbEdit.add(addWeight);

    changeLabel.addActionListener(e -> callPopupMenu("Change label", simulator, "Label:", "New label:", places));

    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    menuBar.add(mbEdit);

    //simulator.setJMenuBar(menuBar);
    simulator.setJMenuBar(menuBar);
    simulator.setVisible(true);
    
    addNewPlace(places, canvas);
    //addNewPlace(places, canvas);
    addNewTransition(transitions, canvas);
    addNewArc("P", 0, 0, places, transitions, arcs, canvas);
    simulator.repaint();
    
    
  }

  public static void callPopupMenu(String title, JFrame sim, String l1, String l2, ArrayList<Place> places) {
    JFrame popupMenu = new JFrame(title);
    popupMenu.setLayout(null);
    JTextField orig = new JTextField(20);
    JLabel label1 = new JLabel(l1);
    JTextField newVal = new JTextField(20);
    JLabel label2 = new JLabel(l2);

    JButton close = new JButton("Accept");

    popupMenu.add(orig);
    popupMenu.add(label1);
    popupMenu.add(newVal);
    popupMenu.add(label2);
    popupMenu.add(close);
    

    orig.setSize(200, 30);
    orig.setLocation(200, 50);

    label1.setSize(100, 30);
    label1.setLocation(100, 50);

    newVal.setSize(200, 30);
    newVal.setLocation(200, 100);

    label2.setSize(100, 30);
    label2.setLocation(100, 100);

    close.setSize(100, 20);
    close.setLocation(200, 200);
    
    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String origText = orig.getText();
        String newText = newVal.getText();
        //System.out.println("orig: " + origText);
        //System.out.println("new: " + newText);
        if (title == "Change label") {
          changePlaceLabel(newText, places, origText);
        }
        popupMenu.dispose();
        sim.repaint();
      }
    });

    popupMenu.setSize(500, 300);
    popupMenu.setVisible(true);
    popupMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


  }



  
  public static void addNewArc(String startObj, int transitionIndex, int placeIndex, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {  
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
    canvas.setArcs(arcs);
    canvas.repaint();
  }

  // ######### TODO #############
  // after clicking add new arc, the next place (or transition) to be clicked will be the origin, and then the next clicked transition (or place) will be the destination
  // do checks for whether clicking on the right one the second time
  // place and transition action listeners need to check whether adding an arc when clicked on

  // could possibly have the user select a place and transition and then click add arc, have a default direction and they have the option to flip it


  // handle source and sink transitions


  public static void addNewPlace(ArrayList<Place> places, DiagramCanvas canvas) { 
    Place placeToAdd = new Place(new String[] {"Place " + (places.size() + 1)}); // pass in a default label for the new place
    //placeToAdd.addActionListener(e -> placeClicked());
    places.add(placeToAdd);
    canvas.setPlaces(places);
    canvas.repaint();

  }

  public static void addNewTransition(ArrayList<Transition> transitions, DiagramCanvas canvas) {
    Transition transitionToAdd = new Transition(new String[] {"Transition " + (transitions.size() + 1)}, canvas); // pass in a default label for the new transition 
    //transitionToAdd.addActionListener(e -> transitionClicked(e));
    transitions.add(transitionToAdd);
    canvas.setTransitions(transitions);
    canvas.repaint();
  }

  public void transitionClicked(ActionEvent e) {
    System.out.println("Transiotn clicked");
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

  public void fireTransition(ArrayList<Arc> arcs, ArrayList<Transition> transitions, ArrayList<Place> places, String transitionID, DiagramCanvas canvas) {
    Transition t = new Transition(new String[] {"Placeholder"}, canvas);
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID() == transitionID) {
        t = transitions.get(i);
        break;
      }
    }

    ArrayList<String> incArcs = t.getIncomingArcsList();
    ArrayList<String> outArcs = t.getOutgoingArcsList();


    for (int i=0; i<incArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID() == incArcs.get(i)) {
          addTokensToPlace(-arcs.get(j).getWeight(), places, arcs.get(j).getOrigin()); // adding negative token value
          break;
        }
      }
    }


    for (int i=0; i<outArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID() == outArcs.get(i)) {
          addTokensToPlace(arcs.get(j).getWeight(), places, arcs.get(j).getEndpoint());
          break;
        }
      }
    }



  }

  // next tick
  // save
  // load
  // new file 


  
  public static void addTokensToPlace(int tokensToAdd, ArrayList<Place> places, String placeID) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getID() == placeID) {
        places.get(i).addTokens(tokensToAdd);
        break;
      }
    }
  }


  public static void changeArcWeight(int newWeight, ArrayList<Arc> arcs, String arcID) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getID() == arcID) {
        arcs.get(i).setWeight(newWeight);
        break;
      }
    }
  }

  public static void changeArcLabel(String newLabel, ArrayList<Arc> arcs, String arcID) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getID() == arcID) {
        arcs.get(i).setLabel(newLabel);
        break;
      }
    }
  }

  public static void changePlaceLabel(String newLabel, ArrayList<Place> places, String origLabel) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getLabel().equals(origLabel)) {
        places.get(i).setLabel(newLabel);
        break;
      }
    }
  }

  public static void changeTransitionLabel(String newLabel, ArrayList<Transition> transitions, String transitionID) {
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID() == transitionID) {
        transitions.get(i).setLabel(newLabel);
        break;
      }
    }
  }


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

