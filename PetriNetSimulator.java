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

    menuBar.add(mbFile);
    menuBar.add(mbAdd);
    simulator.getContentPane().add(BorderLayout.NORTH, menuBar);
    simulator.setVisible(true);
    
  }
  
 
  

}

