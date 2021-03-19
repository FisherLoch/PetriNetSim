// initialisation file
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import simComponents.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;


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
    simulator.setSize(2000, 1500);
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
    //JMenuItem saveAs = new JMenuItem("Save as");
    JMenuItem open = new JMenuItem("Open");
    JMenuItem newFile = new JMenuItem("New");
    mbFile.add(newFile);
    mbFile.add(open);
    mbFile.add(save);
    //mbFile.add(saveAs);

    open.addActionListener(e -> callFileMenu(places, transitions, arcs, "Open petri net", "Open", simulator, canvas));
    save.addActionListener(e -> callFileMenu(places, transitions, arcs, "Save petri net", "Save", simulator, canvas));
    newFile.addActionListener(e -> newPetriNet(places, transitions, arcs, canvas, simulator));

    // Add
    JMenu mbAdd = new JMenu("ADD");
    JMenuItem addArc = new JMenuItem("Add arc");
    JMenuItem addPlace = new JMenuItem("Add place");
    JMenuItem addTransition = new JMenuItem("Add transition");
    mbAdd.add(addArc);
    mbAdd.add(addPlace);
    mbAdd.add(addTransition);

    addArc.addActionListener(e -> callPopupMenu("Add new arc", simulator, "Label of Origin:", "Label of endpoint:", places, transitions, arcs, canvas)); 
    addPlace.addActionListener(e -> addNewPlace(places, canvas));
    addTransition.addActionListener(e -> addNewTransition(transitions, canvas));

    // Edit
    JMenu mbEdit = new JMenu("EDIT");
    JMenuItem changeLabel = new JMenuItem("Change label");
    JMenuItem setWeight = new JMenuItem("Set arc weight");
    JMenuItem setTokens = new JMenuItem("Set tokens");
    mbEdit.add(setTokens);
    mbEdit.add(changeLabel);
    mbEdit.add(setWeight);

    changeLabel.addActionListener(e -> callPopupMenu("Change label", simulator, "Label:", "New label:", places, transitions, arcs, canvas));
    setWeight.addActionListener(e -> callPopupMenu("Set new arc weight", simulator, "Arc Label:", "New weight:", places, transitions, arcs, canvas));
    setTokens.addActionListener(e -> callPopupMenu("Set new tokens in Place", simulator, "Place Label:", "New tokens:", places, transitions, arcs, canvas));
    

    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    menuBar.add(mbEdit);

    simulator.setJMenuBar(menuBar);
    simulator.setVisible(true);
    
    addNewPlace(places, canvas);
    addNewPlace(places, canvas);
    addNewTransition(transitions, canvas);
    addNewTransition(transitions, canvas);
    //addNewArc("P", "Transition 1", "Place 1", places, transitions, arcs, canvas);
    simulator.repaint();
    
    
  }


  public static void callFileMenu(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, String title, String buttonText, JFrame sim, DiagramCanvas canvas) {
    
    JFrame saveMenu = new JFrame(title);
    saveMenu.setLayout(null);
    JButton close = new JButton(buttonText);
    JTextField file = new JTextField(30);
    JLabel fileLabel = new JLabel("File name:");

    saveMenu.add(close);
    saveMenu.add(file);
    saveMenu.add(fileLabel);
    saveMenu.setSize(500, 400);

    fileLabel.setSize(100, 50);
    fileLabel.setLocation(50, 100);

    file.setSize(250, 50);
    file.setLocation(150, 100);

    close.setSize(200, 50);
    close.setLocation(150, 200);

    saveMenu.setVisible(true);
    saveMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        // check for valid file name here

        if (title.equals("Save petri net")) {
          saveFile(places, transitions, arcs, file.getText(), 0);
        } else if (title.equals("Open petri net")) {
          openFile(places, transitions, arcs, file.getText(), sim, canvas);
        }

        saveMenu.dispose();

      }

    });


  }


  public static void saveFile(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, String fileName, int depth) {
    // save in location ./fileName
    System.out.println("Save file: " + fileName);

    if (createFile(fileName)) {
      // new file created, write data to it
      try {
        FileWriter writer = new FileWriter(fileName);
        String writeData;
        Place p;
        ArrayList<String> list1;
        ArrayList<String> list2;

        // save place data
        for (int i=0; i<places.size(); i++) {
          writer.write("New place\n");
          p = places.get(i);
          writer.write(p.getID() + "\n");
          writeData = "";
          writeData = writeData + p.getLabel() + "-" + p.getTokens() + "-" + p.getX() + "-" + p.getY() + "\n";
          writer.write(writeData);
          list1 = p.getIncomingArcsList();
          list2 = p.getOutgoingArcsList();
          writer.write("incArcs\n");
          for (int j=0; j<list1.size(); j++) {
            writer.write(list1.get(j) + "\n");
          }
          writer.write("outArcs\n");
          for (int j=0; j<list2.size(); j++) {
            writer.write(list2.get(j) + "\n");
          }
        } 
    
        Transition t;
        // save transition data
        for (int i=0; i<transitions.size(); i++) {
          writer.write("New transition\n");
          t = transitions.get(i);
          writer.write(t.getID() + "\n");
          writeData = "";
          writeData = writeData + t.getLabel() + "-" + t.getX() + "-" + t.getY() + "\n";
          writer.write(writeData);
          list1 = t.getIncomingArcsList();
          list2 = t.getOutgoingArcsList();
          writer.write("incArcs\n");
          for (int j=0; j<list1.size(); j++) {
            writer.write(list1.get(j) + "\n");
          }
          writer.write("outArcs\n");
          for (int j=0; j<list2.size(); j++) {
            writer.write(list2.get(j) + "\n");
          }
        } 


        // save arc data
        Arc a;

        for (int i=0; i<arcs.size(); i++) {
          writer.write("New arc\n");
          a = arcs.get(i);
          writer.write(a.getID() + "\n");
          writeData = "";
          writeData = writeData + a.getLabel() + "-" + a.getWeight() + "\n" + a.getOrigin() + "\n" + a.getEndpoint() + "\n";
          writer.write(writeData);
        } 




        writer.close();
        System.out.println("Writing to file finished");

      } catch (IOException e) {
        System.out.println("Error writing file");
      }

      
    } else {
      // file already exists, or not created
      // check if exists, will need to remove the data and rewrite it
      if (depth < 3) {
        File delFile = new File(fileName);
        if (delFile.delete()) {
          saveFile(places, transitions, arcs, fileName, depth + 1);
        } else {
          System.out.println("File could not be deleted");
        } 
      } else {
        //err msg saying file could not be saved
        System.out.println("Depth 3 reached in save file");
      }
    }







  }

  public static boolean createFile(String fileName) { 
    try {
      File newFile = new File(fileName);
      if (newFile.createNewFile()) {
        return true;
      } else {
        // error box
        // decide whether to overwrite?
        //callErrorBox("File name already exists");
        return false;
      }
    } catch (IOException e) {
      System.out.println("Error occurred in createFile");
      return false;
    }
  }

  public static void openFile(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, String fileName, JFrame sim, DiagramCanvas canvas) {
    System.out.println("Open file: " + fileName);
    newPetriNet(places, transitions, arcs, canvas, sim);

    try {

      File f = new File(fileName);
      Scanner fileReader = new Scanner(f);
      String data = fileReader.nextLine();
      int c = 0;

      while (fileReader.hasNextLine()) {
        System.out.println("Start of if, data = " + data);
        if (data.equals("New place")) {
          String placeID = fileReader.nextLine();
          String[] placeData = fileReader.nextLine().split("-");
          System.out.println("should be incArcs: " + fileReader.nextLine()); // need to keep this next line part in some way to allow while loop to work as intended
          ArrayList<String> incArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          while (!data.equals("outArcs")) {
            incArcs.add(data);
            data = fileReader.nextLine();
          } // after this, current line should be outArcs

          ArrayList<String> outArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          while (!data.substring(0, 3).equals("New")) {
            outArcs.add(data);
            data = fileReader.nextLine();
            //System.out.println("Checking for equals New: " + outArc.substring(0, 2));
          }

          // after this, either met new transition, or new place

          int tokens = Integer.parseInt(placeData[1]);
          int centreX = Integer.parseInt(placeData[2]);
          int centreY = Integer.parseInt(placeData[3]);
          Place p = new Place(placeData[0], placeID, tokens, centreX, centreY, outArcs, incArcs);
          places.add(p);
          // exit if here, next line should be "New place" or "New transition"

        } else if (data.equals("New transition")) {

          String transID = fileReader.nextLine();
          String[] transData = fileReader.nextLine().split("-");

          ArrayList<String> incArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          System.out.println("Data inc: " + data);
          while (!data.equals("outArcs")) {
            incArcs.add(data);
            data = fileReader.nextLine();
          }

          ArrayList<String> outArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          System.out.println("Data out: " + data);
          while (!data.substring(0, 3).equals("New")) {
            outArcs.add(data);
            data = fileReader.nextLine();
          }

          int originX = Integer.parseInt(transData[1]);
          int originY = Integer.parseInt(transData[2]);
          Transition t = new Transition(transData[0], transID, originX, originY, canvas);
          transitions.add(t);

          // exit if here, next line should be "New transition" or "New arc"


        } else if (data.equals("New arc")) {
          System.out.println("At arcs");
          String arcID = fileReader.nextLine();
          String[] arcData = fileReader.nextLine().split("-");
          String origin = fileReader.nextLine();
          String endpoint = fileReader.nextLine();
          int weight = Integer.parseInt(arcData[1]);
          Arc a = new Arc(arcID, arcData[0], origin, endpoint, weight);

          arcs.add(a);

          if (fileReader.hasNextLine()) {
            System.out.println("Data = " + fileReader.nextLine());
          }

        } else {
          System.out.println("Unknown line: " + data + " encountered");
        }

    

      }

      fileReader.close();
      // all data stored in arrays, add to canvas data, repaint sim, check that arrays have data

      for (int i=0; i<places.size(); i++) {
        // print place data
        places.get(i).printData();
      }

      for (int i=0; i<transitions.size(); i++) {
        transitions.get(i).printData();
      }

      for (int i=0; i<arcs.size(); i++) {
        arcs.get(i).printData();
      }

      canvas.setPlaces(places);
      canvas.setTransitions(transitions);
      canvas.setArcs(arcs);
      canvas.repaint();
      sim.repaint();


    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
      
  }

  public static void callPopupMenu(String title, JFrame sim, String l1, String l2, ArrayList<Place> places, ArrayList<Transition>  transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {
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


        // remove arc, place, transition (removeItemWithLabel?)
        // add arc, place label to transition or opposite check

        // check inputs here
        if (title.equals("Change label")) {
          changeLabel(origText, newText, places, transitions, arcs);
        } else if (title.equals("Set new arc weight")) {
          if (!doesArcExist(arcs, origText)) {
            callErrorBox("Label does not match any arc");
          } else if (!isNumeric(newText)) {
            callErrorBox("           Not a number");
          } else if (Integer.parseInt(newText) < 1) {
            callErrorBox("Number cannot be less than 1");
          } else {
            setArcWeight(Integer.parseInt(newText), arcs, origText);
          }
        } else if (title.equals("Set new tokens in Place")) {
           if (!doesPlaceExist(places, origText)) {
              callErrorBox("Label does not match any place");
           } else if (!isNumeric(newText)) {
              callErrorBox("         Not a number");
           } else if (Integer.parseInt(newText) < 0) {
              callErrorBox("Number cannot be less than 0");
           } else {
              setTokensInPlace(Integer.parseInt(newText), places, origText);
           }
        } else if (title.equals("Add new arc")) {
          // check if orig label matches place
          // if it does, check that new matches a transition
          // add arc with default weight/label, might make a new menu specifially for adding arc with more options
          
          // if not, check if it matches a transition
          // if it does, check it matches a place
          // add arc with default weight/label


          if (doesPlaceExist(places, origText)) {
            if (doesTransitionExist(transitions, newText)) {
              addNewArc("P", newText, origText, places, transitions, arcs, canvas);
            } else {
              callErrorBox("Label 2 does not match a transition");
            }

          } else if (doesTransitionExist(transitions, origText)) {
              if (doesPlaceExist(places, newText)) {
                addNewArc("T", origText, newText, places, transitions, arcs, canvas); 
              } else {
                callErrorBox("Label 2 does not match a place");
              }
            
          } else {
            callErrorBox("Label 1 does not match any item");
          }
        }

        popupMenu.dispose();
        sim.repaint();
      }
    });

    popupMenu.setSize(500, 300);
    popupMenu.setVisible(true);
    popupMenu.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


  }

  public static void callErrorBox(String eMsg) {
    JFrame eBox = new JFrame("Error");
    eBox.setLayout(null);
    JButton close = new JButton("Close");
    JLabel errMessage = new JLabel(eMsg);

    eBox.add(close);
    eBox.add(errMessage);
    eBox.setSize(300, 300);

    close.setSize(100, 50);
    close.setLocation(100, 120);
    errMessage.setSize(200, 50);
    errMessage.setLocation(50, 70);
    eBox.setVisible(true);
    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        eBox.dispose();
      }
    });
    eBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }

  public static boolean isNumeric(String s) {
    if (s == null) {
      return false;
    }
    return s.matches("\\d+");
  }



  
  public static void addNewArc(String startObj, String transitionLabel, String placeLabel, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {  
    Arc arcToAdd = new Arc(new String[] {"Arc " + (arcs.size() + 1)});

    //System.out.println("tL: " + transitionLabel + " pL: " + placeLabel);
    
    if (startObj.equals("T")) { // arc originates from transition and is going into a place

     // change to be based on labels for adding 
    // System.out.println("startObj: " + startObj);

      for (int i=0; i<transitions.size(); i++) {
        if (transitions.get(i).getLabel().equals(transitionLabel)) {
        //  System.out.println("Transition with label: " + transitionLabel + " found");
          transitions.get(i).addOutgoingArc(arcToAdd.getID());
          arcToAdd.setOrigin(transitions.get(i).getID());
          break;
        }
      }
      for (int i=0; i<places.size(); i++) {
        if (places.get(i).getLabel().equals(placeLabel)) {
        //  System.out.println("Place with label: " + placeLabel + " found");
          places.get(i).addIncomingArc(arcToAdd.getID());
          arcToAdd.setEndpoint(places.get(i).getID());
          break;
        }
      }
      arcs.add(arcToAdd);
      
    } else {
   //  System.out.println("startObj: " + startObj);
      
      for (int i=0; i<places.size(); i++) {
       // System.out.println("Place label: " + i + ": " + places.get(i).getLabel() + ", " + (places.get(i).getLabel() == placeLabel));
        if (places.get(i).getLabel().equals(placeLabel)) {
        //  System.out.println("Place with label: " + placeLabel + " found");
          places.get(i).addOutgoingArc(arcToAdd.getID());
          arcToAdd.setOrigin(places.get(i).getID());
          break;
        }
      }
      for (int i=0; i<transitions.size(); i++) {
     //   System.out.println("Transition label: " + i + ": " + transitions.get(i).getLabel());
        if (transitions.get(i).getLabel().equals(transitionLabel)) {
         // System.out.println("Transition with label: " + transitionLabel + " found");
          transitions.get(i).addIncomingArc(arcToAdd.getID());
          arcToAdd.setEndpoint(transitions.get(i).getID());
          break;
        }
      }
/*
      places.get(placeIndex).addOutgoingArc(arcToAdd.getID());
      arcToAdd.setOrigin(places.get(placeIndex).getID());
      transitions.get(transitionIndex).addIncomingArc(arcToAdd.getID());
      arcToAdd.setEndpoint(transitions.get(transitionIndex).getID());
*/
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
    transitions.add(transitionToAdd);
    canvas.setTransitions(transitions);
    canvas.repaint();
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

  public static void newPetriNet(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim) {
    // reset all of places, transitions and arcs, repaint and it should be fine?
    // need to reset arrays in diagram canvas as well


    // maybe have a popup box to confirm?
    // can also save the file before making a new one in case it needs to be recovered?

    for (int i=(places.size() - 1); i>=0; i--) {
      places.remove(i);
    }
    for (int i=(transitions.size() - 1); i>=0; i--) {
      transitions.remove(i);
    }
    for (int i=(arcs.size() - 1); i>=0; i--) {
      arcs.remove(i);
    }
    canvas.setPlaces(places);
    canvas.setTransitions(transitions);
    canvas.setArcs(arcs);
    sim.repaint();

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


  public static boolean doesArcExist(ArrayList<Arc> arcs, String arcLabel) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getLabel().equals(arcLabel)) {
        return true;
      }
    }
    return false;
  }

  public static boolean doesPlaceExist(ArrayList<Place> places, String placeLabel) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getLabel().equals(placeLabel)) {
        return true;
      }
    }
    return false;
  }

  public static boolean doesTransitionExist(ArrayList<Transition> transitions, String transitionLabel) {
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getLabel().equals(transitionLabel)) {
        return true;
      }
    }
    return false;
  }

  
  public static void setTokensInPlace(int tokens, ArrayList<Place> places, String placeLabel) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getLabel().equals(placeLabel)) {
        places.get(i).setTokens(tokens);
        return;
      }
    }
  }


  public static void addTokensToPlace(int tokensToAdd, ArrayList<Place> places, String placeID) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getID().equals(placeID)) {
        places.get(i).addTokens(tokensToAdd);
        return;
      }
    }
  }


  public static void setArcWeight(int newWeight, ArrayList<Arc> arcs, String arcLabel) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getLabel().equals(arcLabel)) {
        arcs.get(i).setWeight(newWeight);
        return;
      }
    }
  }

  public static void changeLabel(String origLabel, String newLabel, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getLabel().equals(origLabel)) {
        places.get(i).setLabel(newLabel);
        return;
      }
    }
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getLabel().equals(origLabel)) {
        transitions.get(i).setLabel(newLabel);
        return;
      }
    }
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getLabel().equals(origLabel)) {
        arcs.get(i).setLabel(newLabel);
        return;
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

