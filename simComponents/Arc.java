package simComponents;

import java.util.UUID;
import java.awt.*;

public class Arc {
  String ID;
  int weight = 1;
  String origin;
  String endpoint;
  String label;
  

  // [label, weight]
  public Arc(String[] initValues) { // constructor
    ID = "Arc" + UUID.randomUUID().toString();
    label = initValues[0];
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

   
    double sideX = originY;
    double sideY = Math.sqrt(Math.pow(endpointX - originX, 2) + Math.pow(endpointY - originY, 2));
    //System.out.println("Side Y: " + sideY);
    double sideZ = Math.sqrt(Math.pow(endpointY, 2) + Math.pow(originX - endpointX, 2));
    if ((originY - endpointY) > 0) {
      sideZ = Math.sqrt(Math.pow(originY - endpointY, 2) + Math.pow(originX - endpointX, 2));
    }
    //System.out.println("Side Z: " + sideZ);

    double angle = Math.acos((Math.pow(sideX, 2) + Math.pow(sideY, 2) - Math.pow(sideZ, 2))/(2 * sideX * sideY));
   
    if (originX >= endpointX) { // triangle faces left
      // leave angle as it is
    } else { // triangle faces right
      angle = 2*Math.PI - angle;
    }

    //System.out.println("Angle: " + angle);


    //double angle = Math.atan((endpointY - originY)/(endpointX - originX));
    // replace with rotation matrix calculation
    //int arcLength = -(int) Math.round(Math.sqrt(Math.pow(endpointX - originX, 2) + Math.pow(endpointY - originY, 2)));
    // coords are now (0, arcLength), might need to invert line as canvas coords have inverted y axis

    int endXOffset = (int) Math.round(-radius * Math.sin(angle));
    int endYOffset = (int) Math.round(-radius * Math.cos(angle));

    g2.drawLine(endXOffset + originX, endYOffset + originY, endpointX, endpointY);

    int midX = Math.round((originX + endXOffset + endpointX)/2);
    int midY = Math.round((originY + endYOffset + endpointY)/2);

    g2.drawString(label, midX, midY - 20);
    g2.drawString(Integer.toString(weight), midX, midY + 20);

  }


  //public void renderTransition(...)

}
