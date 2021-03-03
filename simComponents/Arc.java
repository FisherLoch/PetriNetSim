package simComponents;

import java.util.UUID;
import java.awt.*;

public class Arc {
  String ID;
  int weight = 1;
  String origin;
  String endpoint;
  String label;
  
  public Arc() { // constructor
    ID = "Arc" + UUID.randomUUID().toString();
    label = "1";
  }

  public String getID() {
    return ID;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String newLabel) {
    label = newLabel;
  }

  public void addWeight(int weightToAdd) {
    weight = weight + weightToAdd;
    label = Integer.toString(weight);
  }

  public void setWeight(int newWeight) {
    weight = newWeight;
  }

  public int getWeight() {
    return weight;
  }

  public void setOrigin(String val) {
    origin = val;
  }

  public String getOrigin() {
    return origin;
  }

  public void setEndpoint(String val) {
    endpoint = val;
  }

  public String getEndpoint() {
    return endpoint;
  }


  public void renderPlace(Graphics g, int originX, int originY, int radius, int endpointX, int endpointY) {
    g.setColor(Color.BLACK);
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new BasicStroke(3));
    double angle = Math.atan((endpointY - originY)/(endpointX - originX));
    g2.drawLine((int) Math.round(originX + radius * Math.cos(angle)), (int) Math.round(originY + radius * Math.sin(angle)), endpointX, endpointY);

  }

}
