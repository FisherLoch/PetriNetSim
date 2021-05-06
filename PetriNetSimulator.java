// initialisation file
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import simComponents.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.Scanner;
import java.lang.Thread;
import java.lang.InterruptedException;


public class PetriNetSimulator {


  static Thread t = new Thread();

  public static void main(String[] args) {
    System.out.println("Petri Net Simulator");

    // might be worth storing the transitions, places and arcs in a hash table so they can be found more quickly, but this may make rendering more awkward
    ArrayList<Transition> transitions = new ArrayList<Transition>();
    ArrayList<Place> places = new ArrayList<Place>();
    ArrayList<Arc> arcs = new ArrayList<Arc>();

    ArrayList<String> transitionsFired = new ArrayList<String>();

    JFrame simulator = new JFrame("Petri Net Simulator");
    DiagramCanvas canvas = new DiagramCanvas(simulator);

    simulator.setLayout(null);

    simulator.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // check whether this can be replaced with a function to save automatically
    simulator.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        simClosed(places, transitions, arcs, canvas, simulator);
      }
    });
    simulator.setSize(2000, 1500);
    JMenuBar menuBar = new JMenuBar();
    // Debugging
    /*
    JMenu mbDebug = new JMenu("DEBUG");
    JMenuItem dispPlaces = new JMenuItem("Display places");
    mbDebug.add(dispPlaces);
    dispPlaces.addActionListener(e -> displayPlacesArray(places));
    menuBar.add(mbDebug);
    JMenuItem dispTransitions = new JMenuItem("Display transitions");
    mbDebug.add(dispTransitions);
    dispTransitions.addActionListener(e -> displayTransitionArray(transitions));
*/
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

    addArc.addActionListener(e -> callPopupMenu("Add new arc", simulator, "Label of Origin:", "Label of endpoint:", places, transitions, arcs, canvas, transitionsFired)); 
    addPlace.addActionListener(e -> addNewPlace(places, canvas));
    addTransition.addActionListener(e -> addNewTransition(transitions, canvas));

    
    // Remove

    JMenu mbRemove = new JMenu("REMOVE");
    JMenuItem removeArc = new JMenuItem("Remove arc");
    JMenuItem removePlace = new JMenuItem("Remove place");
    JMenuItem removeTransition = new JMenuItem("Remove transition");

    mbRemove.add(removeArc);
    mbRemove.add(removePlace);
    mbRemove.add(removeTransition);

    removeArc.addActionListener(e -> callPopupMenuSingleBox("Remove arc", simulator, places, transitions, arcs, canvas, "Label:", transitionsFired));
    removePlace.addActionListener(e -> callPopupMenuSingleBox("Remove place", simulator, places, transitions, arcs, canvas, "Label:", transitionsFired));
    removeTransition.addActionListener(e -> callPopupMenuSingleBox("Remove transition", simulator, places, transitions, arcs, canvas, "Label:", transitionsFired));




    // Edit
    JMenu mbEdit = new JMenu("EDIT");
    JMenuItem changeLabel = new JMenuItem("Change label");
    JMenuItem setWeight = new JMenuItem("Set arc weight");
    JMenuItem setTokens = new JMenuItem("Set tokens");
    mbEdit.add(setTokens);
    mbEdit.add(changeLabel);
    mbEdit.add(setWeight);

    changeLabel.addActionListener(e -> callPopupMenu("Change label", simulator, "Label:", "New label:", places, transitions, arcs, canvas, transitionsFired));
    setWeight.addActionListener(e -> callPopupMenu("Set new arc weight", simulator, "Arc Label:", "New weight:", places, transitions, arcs, canvas, transitionsFired));
    setTokens.addActionListener(e -> callPopupMenu("Set new tokens in Place", simulator, "Place Label:", "New tokens:", places, transitions, arcs, canvas, transitionsFired));
 

    // Simulate
    JMenu mbSim = new JMenu("SIM");
    JMenuItem fireTrans = new JMenuItem("Fire transition");
    JMenuItem doNextTick = new JMenuItem("Next tick");
    JMenuItem doXTicks = new JMenuItem("Do x ticks");
    JMenuItem autoTick = new JMenuItem("Auto run");
    JMenuItem stopAutoSim = new JMenuItem("Stop auto run");
    JMenuItem undoTick = new JMenuItem("Undo tick");

    
    mbSim.add(fireTrans);
    mbSim.add(doNextTick);
    mbSim.add(doXTicks);
    mbSim.add(autoTick);
    mbSim.add(stopAutoSim);
    mbSim.add(undoTick);

    fireTrans.addActionListener(e -> callPopupMenuSingleBox("Fire transition", simulator, places, transitions, arcs, canvas, "Label:", transitionsFired));
    doNextTick.addActionListener(e -> nextTick(places, transitions, arcs, canvas, simulator, transitionsFired));
    doXTicks.addActionListener(e -> callPopupMenuSingleBox("Next X ticks", simulator, places, transitions, arcs, canvas, "Ticks to do:", transitionsFired));
    autoTick.addActionListener(e -> callPopupMenu("Auto run simulator", simulator, "Number of ticks:", "Ticks per sec:", places, transitions, arcs, canvas, transitionsFired));
    stopAutoSim.addActionListener(e -> stopSim());
    undoTick.addActionListener(e -> undoTick(places, transitions, arcs, canvas, simulator, transitionsFired));


    // Compute
    JMenu mbCompute = new JMenu("COMPUTE");
    JMenuItem reachGraph = new JMenuItem("Reachability graph");

    reachGraph.addActionListener(e -> callPopupMenuSingleBox("Reachability graph", simulator, places, transitions, arcs, canvas, "Depth:", transitionsFired));

    mbCompute.add(reachGraph);


    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    menuBar.add(mbRemove);
    menuBar.add(mbEdit);
    menuBar.add(mbSim);
    menuBar.add(mbCompute);

    simulator.setJMenuBar(menuBar);
    simulator.setVisible(true);

    simOpened(places, transitions, arcs, canvas, simulator);

   /* 
    addNewPlace(places, canvas);
    addNewPlace(places, canvas);
    addNewTransition(transitions, canvas);
    addNewTransition(transitions, canvas);
    //addNewArc("P", "Transition 1", "Place 1", places, transitions, arcs, canvas);
    simulator.repaint();
    */
    
  }


  public static void simOpened(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim) {
    openFile(places, transitions, arcs, "autoSaveFile", sim, canvas);

  }


  public static void simClosed(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim) { // auto save upon closing
    System.out.println("Sim closed: ");
    if (t.isAlive()) {
      t.stop();
    }
    // save here
    saveFile(places, transitions, arcs, "autoSaveFile", 0);
    sim.dispose();
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
        //System.out.println("Start of if, data = " + data);
        if (data.equals("New place")) {
          String placeID = fileReader.nextLine();
          String[] placeData = fileReader.nextLine().split("-");
          //System.out.println("should be incArcs: " + fileReader.nextLine()); // need to keep this next line part in some way to allow while loop to work as intended
          fileReader.nextLine();
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
          fileReader.nextLine(); 
          ArrayList<String> incArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          
          //System.out.println("Data inc: " + data);
          while (!data.equals("outArcs")) {
            incArcs.add(data);
            data = fileReader.nextLine();
          }

          ArrayList<String> outArcs = new ArrayList<String>();
          data = fileReader.nextLine();
          //System.out.println("Data out: " + data);
          while (!data.substring(0, 3).equals("New")) {
            outArcs.add(data);
            data = fileReader.nextLine();
          }
          

          int originX = Integer.parseInt(transData[1]);
          int originY = Integer.parseInt(transData[2]);
          Transition t = new Transition(transData[0], transID, originX, originY, incArcs, outArcs, canvas);
          transitions.add(t);

          // exit if here, next line should be "New transition" or "New arc"


        } else if (data.equals("New arc")) {
          //System.out.println("At arcs");
          String arcID = fileReader.nextLine();
          String[] arcData = fileReader.nextLine().split("-");
          String origin = fileReader.nextLine();
          String endpoint = fileReader.nextLine();
          int weight = Integer.parseInt(arcData[1]);
          Arc a = new Arc(arcID, arcData[0], origin, endpoint, weight);

          arcs.add(a);

          if (fileReader.hasNextLine()) {
            //System.out.println("Data = " + fileReader.nextLine());
            fileReader.nextLine();
          }

        } else {
          System.out.println("Unknown line: " + data + " encountered");
        }

    

      }

      fileReader.close();
      // all data stored in arrays, add to canvas data, repaint sim, check that arrays have data
     /* 
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
*/
      


      canvas.setPlaces(places);
      canvas.setTransitions(transitions);
      canvas.setArcs(arcs);
      canvas.repaint();
      sim.repaint();


    } catch (FileNotFoundException e) {
      System.out.println("File not found");
    }
      
  }

  public static void callPopupMenu(String title, JFrame sim, String l1, String l2, ArrayList<Place> places, ArrayList<Transition>  transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, ArrayList<String> transFired) {
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
              for (int i=transFired.size()-1; i>=0; i--) {
                transFired.remove(i);
              }
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
        } else if (title.equals("Auto run simulator")) {
          // check values are right in each box, limits
          if (!isNumeric(origText)) {
            callErrorBox("Enter an integer in ticks box");
          } else if ((Integer.parseInt(origText) < 1) || (Integer.parseInt(origText) > 100)) {
            callErrorBox("Number of ticks must be a value from 1-100");
          } else if (!isNumericDouble(newText)) {
            callErrorBox("Enter a number in ticks per second box");
          } else if ((Double.parseDouble(newText) <= 0) || (Double.parseDouble(newText) > 1000)) {
            callErrorBox("Number of ticks per second must be 0-1000 not including 0");
          } else {
            double tps = Double.parseDouble(newText);
            int ticks = Integer.parseInt(origText);

            autoRunSim(ticks, tps, places, transitions, arcs, canvas, sim, transFired);
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


  public static void autoRunSim(int ticks, double ticksPerSecond, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim, ArrayList<String> transFired) {

    t = new Thread() {
      public void run() {

        int sleepTime = (int) Math.round(1000/ticksPerSecond);
        
        for (int i=0; i<ticks; i++) { 
          System.out.println("Next tick");
          if (!nextTick(places, transitions, arcs, canvas, sim, transFired)) {
            System.out.println("No more enabled transitions");
            return;
          }
          try {
            Thread.sleep(sleepTime);
          } catch (InterruptedException e) {
            System.out.println("Error sleeping thread");
          } 
        }
      }
    };

    t.start();
  }

  public static void stopSim() {
    t.stop();
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

  public static boolean isNumericDouble(String s) {
    try {
      Double.parseDouble(s);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }


  public static void callPopupMenuSingleBox(String title, JFrame sim, ArrayList<Place> places, ArrayList<Transition>  transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, String l1, ArrayList<String> transFired) {
    JFrame popupMenu = new JFrame(title);
    popupMenu.setLayout(null);
    JTextField box1 = new JTextField(20);
    JLabel label1 = new JLabel(l1);
    JButton close = new JButton("Accept");

    popupMenu.add(box1);
    popupMenu.add(label1);
    popupMenu.add(close);
    

    box1.setSize(200, 30);
    box1.setLocation(200, 50);

    label1.setSize(100, 30);
    label1.setLocation(100, 50);

    close.setSize(100, 20);
    close.setLocation(200, 200);
    
    close.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String box1Text = box1.getText();



        if (title.equals("Fire transition")) {
          if (!doesTransitionExist(transitions, box1Text)) {
            callErrorBox("Label does not match any transition,\n or transition is not enabled");
          } else {
            String IDToPass = getTransIDFromLabel(box1Text, transitions);
            if (!isTransitionEnabled(IDToPass, places, transitions, arcs)) {
              callErrorBox("Transition is not enabled");
            } else {
              fireTransition(places, transitions, arcs, IDToPass, canvas, transFired);
            }
          }
        } else if (title.equals("Remove arc")) {
          if (!doesArcExist(arcs, box1Text)) {
            callErrorBox("Label does not match any arc");
          } else {
            String IDToPass = getArcIDFromLabel(box1Text, arcs);
            removeArc(IDToPass, places, transitions, arcs, canvas);
          }
        } else if (title.equals("Remove place")) {
          if (!doesPlaceExist(places, box1Text)) {
            callErrorBox("Label does not match any place");
          } else {
            String IDToPass = getPlaceIDFromLabel(box1Text, places);
            removePlace(IDToPass, places, transitions, arcs, canvas);
          } 
        } else if (title.equals("Remove transition")) {
          if (!doesTransitionExist(transitions, box1Text)) {
            callErrorBox("Label does not match any transition");
          } else {
            String IDToPass = getTransIDFromLabel(box1Text, transitions);
            removeTransition(IDToPass, places, transitions, arcs, canvas);
          }
        } else if (title.equals("Next X ticks")) {
          if (!isNumeric(box1Text)) {
            callErrorBox("         Not a valid number");
          } else if (Integer.parseInt(box1Text) < 0) {
            callErrorBox("Number cannot be less than 0");
          } else if (Integer.parseInt(box1Text) > 30) {
            callErrorBox("Number cannot be more than 30");
          } else {
            int ticks = Integer.parseInt(box1Text);
            nextXTicks(places, transitions, arcs, canvas, sim, ticks, transFired);
          }
          
        } else if (title.equals("Reachability graph")) {
          if (!isNumeric(box1Text)) {
            callErrorBox("         Not a valid number");
          } else if (Integer.parseInt(box1Text) < 0) {
            callErrorBox("Number cannot be less than 0");
          } else if (Integer.parseInt(box1Text) > 30) {
            callErrorBox("Number cannot be more than 30");
          } else { 
            int depth = Integer.parseInt(box1Text);
            reachabilityGraph(places, transitions, arcs, depth);
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

  public static void removeTransition(String transID, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {
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
      removeArc(outgoingArcsList.get(i), places, transitions, arcs, canvas);
    }
    for (int i=0; i<incomingArcsList.size(); i++) {
      removeArc(incomingArcsList.get(i), places, transitions, arcs, canvas);
    }
    transitions.remove(transIndex);

    canvas.setTransitions(transitions);
    canvas.setArcs(arcs);
  }

  public static void removePlace(String placeID, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {
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
      removeArc(incomingArcList.get(i), places, transitions, arcs, canvas);
    }
    for (int i=0; i<outgoingArcList.size(); i++) {
      removeArc(outgoingArcList.get(i), places, transitions, arcs, canvas);
    }
    places.remove(placeIndex);

    canvas.setPlaces(places);
    canvas.setArcs(arcs);
    
  }

  // check for possible function to make this more efficient
  public static void removeArc(String arcID, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas) {

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

    canvas.setTransitions(transitions);
    canvas.setPlaces(places);
    canvas.setArcs(arcs);

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


  public static String getTransIDFromLabel(String l, ArrayList<Transition> transitions) { // this might be a function elsewhere, if found, replace the call to this with that
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getLabel().equals(l)) {
        return transitions.get(i).getID();
      }
    }
    System.out.println("getTransID THIS LINE SHOULD NOT BE REACHED");
    return "Not found";
  }

  public static String getPlaceIDFromLabel(String l, ArrayList<Place> places) {
    for (int i=0; i<places.size(); i++) {
      if (places.get(i).getLabel().equals(l)) {
        return places.get(i).getID();
      }
    }
    System.out.println("getPlaceID THIS LINE SHOULD NOT BE REACHED");
    return "Not found";
  }

  public static String getArcIDFromLabel(String l, ArrayList<Arc> arcs) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getLabel().equals(l)) {
        return arcs.get(i).getID();
      }
    }
    System.out.println("getArcID THIS LINE SHOULD NOT BE REACHED");
    return "Not found";
  }

  public static boolean isTransitionEnabled(String transID, ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs) {
    Transition t = new Transition();
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID().equals(transID)) {
        t = transitions.get(i);
        break;
      }
    }
    
    ArrayList<String> arcsList = new ArrayList<String>();

    arcsList = t.getIncomingArcsList();
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

  public static int getArcWeight(ArrayList<Arc> arcs, String arcID) {
    for (int i=0; i<arcs.size(); i++) {
      if (arcs.get(i).getID().equals(arcID)) {
        return arcs.get(i).getWeight();
      }
    }

    System.out.println("getArcWeight: THIS LINE SHOULD NEVER BE REACHED");
    return -1;
  }


  // fire transition

  public static void fireTransition(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, String transitionID, DiagramCanvas canvas, ArrayList<String> transFired) {
    transFired.add(transitionID);

    Transition t = new Transition();
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID().equals(transitionID)) {
        t = transitions.get(i);
        break;
      }
    }
    
    setFiredColours(transitions, transFired, t);

    ArrayList<String> incArcs = t.getIncomingArcsList();
    ArrayList<String> outArcs = t.getOutgoingArcsList();

    for (int i=0; i<incArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID().equals(incArcs.get(i))) {
          //System.out.println("Remove " + arcs.get(j).getWeight() + " tokens from " + arcs.get(j).getOrigin());
          addTokensToPlace(-arcs.get(j).getWeight(), places, arcs.get(j).getOrigin()); // adding negative token value
          break;
        }
      }
    }

    for (int i=0; i<outArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID().equals(outArcs.get(i))) {
          addTokensToPlace(arcs.get(j).getWeight(), places, arcs.get(j).getEndpoint());
          break;
        }
      }
    }

    canvas.setPlaces(places);
  }

  public static void setFiredColours(ArrayList<Transition> transitions, ArrayList<String> transFired, Transition t) {
    if (transFired.size() < 2) {
      t.setFired();
    } else {
      String ID = transFired.get(transFired.size() - 2);
      for (int i=0; i<transitions.size(); i++) {
        if (transitions.get(i).getID().equals(ID)) {
          transitions.get(i).setNotFired();
          break;
        }
      }
      t.setFired();
    }
  }

  public static void setUnfiredColours(ArrayList<Transition> transitions, ArrayList<String> transFired, Transition t) {
    if (transFired.size() < 2) {
      t.setNotFired();
    } else {
      t.setNotFired();
      String ID = transFired.get(transFired.size() - 2);
      for (int i=0; i<transitions.size(); i++) {
        if (transitions.get(i).getID().equals(ID)) {
          transitions.get(i).setFired();
          break;
        }
      }
    }
  }

  public static void nextXTicks(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim, int ticksToPass, ArrayList<String> transFired) {
    for (int i=0; i<ticksToPass; i++) {
      nextTick(places, transitions, arcs, canvas, sim, transFired);
      /*
      try {
        Thread.sleep(1000);
        sim.repaint();
      } catch (InterruptedException e) {
        System.out.println("Error in thread sleep");
      }*/
    }
  }


  public static boolean nextTick(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim, ArrayList<String> transFired) {
    // get ids of enabled transitions
    ArrayList<String> enabledTrans = new ArrayList<String>();

    /*
    for (int i=0; i<transitions.size(); i++) {
      String currID = transitions.get(i).getID();
      if (isTransitionEnabled(currID, places, transitions, arcs)) {
        enabledTrans.add(currID);
      }
    }
    */

    getEnabledTransitions(places, transitions, arcs, enabledTrans);

    if (enabledTrans.size() == 0) {
      System.out.println("No transitions enabled");
      return false;
    }

    // pick a random id from these
    int randIndex = (int) Math.floor((Math.random() * enabledTrans.size())); 
    System.out.println("Random index chosen: " + randIndex);

    // fire this transition
    fireTransition(places, transitions, arcs, enabledTrans.get(randIndex), canvas, transFired);

    sim.repaint();
    return true;

  }


  public static void undoTick(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim, ArrayList<String> transFired) {
    // get size of array
    // val in size -1 is transition ID
    // unfire that transition
    // remove val in size -1
    int listSize = transFired.size();
    if (listSize == 0) {
      callErrorBox("No transitions to undo");
      return;
    }
    String idToUndo = transFired.get(listSize - 1);
    
    unfireTransition(places, transitions, arcs, canvas, sim, idToUndo, transFired);

    transFired.remove(listSize - 1);
  }

  public static void unfireTransition(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, DiagramCanvas canvas, JFrame sim, String transitionID, ArrayList<String> transFired) {
    // remove tokens based on arc weight from all outgoing
    // add tokens based on arc weight to all incoming
    // dont need to check if its enabled
    

    Transition t = new Transition();
    for (int i=0; i<transitions.size(); i++) {
      if (transitions.get(i).getID().equals(transitionID)) {
        t = transitions.get(i);
        break;
      }
    }

    setUnfiredColours(transitions, transFired, t);

    ArrayList<String> incArcs = t.getIncomingArcsList();
    ArrayList<String> outArcs = t.getOutgoingArcsList();

    for (int i=0; i<incArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID().equals(incArcs.get(i))) {
          addTokensToPlace(arcs.get(j).getWeight(), places, arcs.get(j).getOrigin()); 
          break;
        }
      }
    }


    for (int i=0; i<outArcs.size(); i++) {
      for (int j=0; j<arcs.size(); j++) {
        if (arcs.get(j).getID().equals(outArcs.get(i))) {
          addTokensToPlace(-arcs.get(j).getWeight(), places, arcs.get(j).getEndpoint());
          break;
        }
      }
    }

    canvas.setPlaces(places);

    sim.repaint();
  }


  public static void getEnabledTransitions(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, ArrayList<String> enabledTrans) {
    for (int i=0; i<transitions.size(); i++) {
      String currID = transitions.get(i).getID();
      if (isTransitionEnabled(currID, places, transitions, arcs)) {
        enabledTrans.add(currID);
      }
    }
  }


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



  // reachability

  public static String getStringFromModel(Place[] places, Transition[] transitions, Arc[] arcs, Hashtable<String, Place> placeTable, Hashtable<String, Transition> transTable, Hashtable<String, Arc> arcTable) {
    
    String modelData = "";
    // format:  P1-5-2*T1-1*T2:P2-3-4*T1:P3-2:P4-3/T1-2*P3-1*P4:T2-1*P4
    //        place-tokens-weight*transition-weight*transition-etc:place2-tokens-weight*transition-etc/
    //        transition-weight*place-weight*place-weight*place-etc:transition-weight*place-weight*place-etc

    // go through places, go through outgoing arcs, weight and transition label added for each,
    // go through transitions, go through outgoing arcs, weight and place label for each


    for (int i=0; i<places.length; i++) {
      // get outgoing arcs list, get arc from table, get weight and endpoint, add to string, do for all arcs, then do next place 
      modelData = modelData + places[i].getLabel() + "-" + places[i].getTokens() + "-";

      ArrayList<String> outArcs = places[i].getOutgoingArcsList();
      for (int j=0; j<outArcs.size(); j++) {
        Arc a = arcTable.get(outArcs.get(j));
        modelData = modelData + a.getWeight() + "*" + a.getEndpoint() + "-";
      }
      modelData = modelData + ":";
    }

    modelData = modelData + "/";



    for (int i=0; i<transitions.length; i++) {
      // get outoing arcs list, get arc from table, get weight and endpoint, add to string, do for all arcs, then do next transition
      modelData = modelData + transitions[i].getLabel() + "-";

      ArrayList<String> outArcs = transitions[i].getOutgoingArcsList();
      for (int j=0; j<outArcs.size(); j++) {
        Arc a = arcTable.get(outArcs.get(j));
        modelData = modelData + a.getWeight() + "*" + a.getEndpoint() + "-";
      }
      modelData = modelData + ":";
    }



    System.out.println("modelData: " + modelData);
    return modelData;
  }


  public static void reachabilityGraph(ArrayList<Place> places, ArrayList<Transition> transitions, ArrayList<Arc> arcs, int depthToReach) {
    // change arraylists into arrays, pass into getStringFromModel
    // create new arrayList with String for modelDatas, pass into getReachabilityGraph functiono

    

    Place placeArr[] = new Place[places.size()];
    Hashtable<String, Place> placeTable = new Hashtable<String, Place>(places.size());
    
    for (int i=0; i<places.size(); i++) {
      placeArr[i] = new Place(places.get(i));
      placeArr[i].setLabel("P" + i);
      placeTable.put(placeArr[i].getID(), placeArr[i]);
    }

    Transition transArr[] = new Transition[transitions.size()];
    Hashtable<String, Transition> transTable = new Hashtable<String, Transition>(transitions.size());
    for (int i=0; i<transitions.size(); i++) {
      transArr[i] = new Transition(transitions.get(i));
      transArr[i].setLabel("T" + i);
      transTable.put(transArr[i].getID(), transArr[i]);
    } 


    Arc arcArr[] = new Arc[arcs.size()];
    Hashtable<String, Arc> arcTable = new Hashtable<String, Arc>(arcs.size());
    for (int i=0; i<arcs.size(); i++) {
      arcArr[i] = new Arc(arcs.get(i));

      if (arcArr[i].getEndpoint().substring(0, 5).equals("Place")) {
        // endpoint is place
        arcArr[i].setEndpoint(placeTable.get(arcArr[i].getEndpoint()).getLabel());
        // so origin is transition
        arcArr[i].setOrigin(transTable.get(arcArr[i].getOrigin()).getLabel());
      } else { // endpoint is transition
        arcArr[i].setEndpoint(transTable.get(arcArr[i].getEndpoint()).getLabel());
        arcArr[i].setOrigin(placeTable.get(arcArr[i].getOrigin()).getLabel());
      }

      arcTable.put(arcArr[i].getID(), arcArr[i]);
    }
    
    String initModel = getStringFromModel(placeArr, transArr, arcArr, placeTable, transTable, arcTable);


    // arrayList when function is done should contain all models up to x depth

    ArrayList<String> reachableModels = new ArrayList<String>();
    Hashtable<String, String> foundModels = new Hashtable<String, String>(depthToReach * places.size() * transitions.size()); // arbitrary size for hash table, ideally slightly larger than the number of reachable models that will be found 
    reachableModels.add(initModel);
    foundModels.put(initModel, "Found");


    getReachabilityGraph(placeArr, transArr, arcArr, placeTable, transTable, arcTable, reachableModels, foundModels, depthToReach, 1);
  
    System.out.println("Reachable models:");
    for (int i=0; i<reachableModels.size(); i++) {
      String tuple = convertModelToTuple(reachableModels.get(i));
      System.out.println("Model " + (i+1) + ": " + reachableModels.get(i) + " -> " + tuple);
    }

  }

  public static String convertModelToTuple(String model) {
    String tuple = "";
    String[] initSplit = model.split("/");
    if (initSplit.length > 1) {
      String[] places = initSplit[0].split(":");
      for (int i=0; i<places.length; i++) {
        String[] place = places[i].split("-");
        tuple = tuple + place[0] + "(" + place[1] + "), ";
      }
    }
    return tuple;
  }

  public static void getReachabilityGraph(Place[] places, Transition[] transitions, Arc[] arcs, Hashtable<String, Place> placeTable, Hashtable<String, Transition> transTable, Hashtable<String, Arc> arcTable, ArrayList<String> reachableModels, Hashtable<String, String> foundModels, int depthToReach, int depth) {
  

    // get enabled transitions

    // for each one
      // fire
      // getStringFromModel, check if exists in hashTable
        // if exists, ignore
        // if not, add true entry to hashTable address


      // getReachabilityGraph for new model
        // once depth reached or no enabled transitions
        // return
      // unfire the same transition

    
    if (depth >= depthToReach) { // base case (net could potentially go infinitely so have a depth value set to limit it)
      return;
    } else {
      int[] enabledIndexes = getEnabledTransitionIndexes(places, transitions, arcs, placeTable, transTable, arcTable);
      /*
        System.out.println();

      for (int i=0; i<enabledIndexes.length; i++) {
        System.out.println("Transition " + (enabledIndexes[i] + 1) + " is enabled");

      }
        System.out.println();
        */
      if (enabledIndexes.length == 0) {
        return;
      } else {
        for (int i=0; i<enabledIndexes.length; i++) {
          fireTransitionArr(places, transitions, arcs, placeTable, transTable, arcTable, enabledIndexes[i]);
          String model = getStringFromModel(places, transitions, arcs, placeTable, transTable, arcTable);
          //System.out.println("Model: " + model);

          if (foundModels.get(model) != null) {
            // model already found so do nothing
            //System.out.println("Model already found");
          } else {
            //System.out.println("Model not found");
            foundModels.put(model, "Found");
            reachableModels.add(model);
          }
          getReachabilityGraph(places, transitions, arcs, placeTable, transTable, arcTable, reachableModels, foundModels, depthToReach, depth + 1);
          unfireTransitionArr(places, transitions, arcs, placeTable, transTable, arcTable, enabledIndexes[i]);
        }
      }
    }

  }


  public static int[] getEnabledTransitionIndexes(Place[] places, Transition[] transitions, Arc[] arcs, Hashtable<String, Place> placeTable, Hashtable<String, Transition> transTable, Hashtable<String, Arc> arcTable) {

    ArrayList<Integer> enabledIndexes = new ArrayList<Integer>();


    for (int i=0; i<transitions.length; i++) {
      boolean enabled = true;
      ArrayList<String> incArcs = transitions[i].getIncomingArcsList();
      for (int j=0; j<incArcs.size(); j++) {
        String ID = incArcs.get(j);
        String orgID = arcTable.get(ID).getOriginID();
        if (placeTable.get(orgID).getTokens() < arcTable.get(ID).getWeight()) {
          enabled = false;
          break; // if at least one place is not enabling the transition then stop the for loop to save time 
        }
      }

      if (enabled) {
        enabledIndexes.add(i);
      }
    }

    int[] transIndexes = new int[enabledIndexes.size()];

    for (int i=0; i<enabledIndexes.size(); i++) {
      transIndexes[i] = enabledIndexes.get(i);
    }

    return transIndexes;
  }
  
  // a lot faster version of the general fireTransition function
  public static void fireTransitionArr(Place[] places, Transition[] transitions, Arc[] arcs, Hashtable<String, Place> placeTable, Hashtable<String, Transition> transTable, Hashtable<String, Arc> arcTable, int transIndex) {

    ArrayList<String> incArcs = transitions[transIndex].getIncomingArcsList();
    ArrayList<String> outArcs = transitions[transIndex].getOutgoingArcsList();

    for (int i=0; i<incArcs.size(); i++) {
      // contains the string of the ID of the arc
      String ID = incArcs.get(i);
      String orgID = arcTable.get(ID).getOriginID();
      // remove tokens from place with ID in ID
      placeTable.get(orgID).addTokens(-arcTable.get(ID).getWeight());
    }


    for (int i=0; i<outArcs.size(); i++) {
      String ID = outArcs.get(i);
      String endID = arcTable.get(ID).getEndpointID();
      placeTable.get(endID).addTokens(arcTable.get(ID).getWeight());
    }

  }

  public static void unfireTransitionArr(Place[] places, Transition[] transitions, Arc[] arcs, Hashtable<String, Place> placeTable, Hashtable<String, Transition> transTable, Hashtable<String, Arc> arcTable, int transIndex) {

    ArrayList<String> incArcs = transitions[transIndex].getIncomingArcsList();
    ArrayList<String> outArcs = transitions[transIndex].getOutgoingArcsList();

    for (int i=0; i<incArcs.size(); i++) {
      // contains the string of the ID of the arc
      String ID = incArcs.get(i);
      String orgID = arcTable.get(ID).getOriginID();
      // add tokens from place with ID in ID
      placeTable.get(orgID).addTokens(arcTable.get(ID).getWeight());
    }


    for (int i=0; i<outArcs.size(); i++) {
      String ID = outArcs.get(i);
      String endID = arcTable.get(ID).getEndpointID();
      // removing tokens from the outgoing places from the transition
      placeTable.get(endID).addTokens(-arcTable.get(ID).getWeight());
    }
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

