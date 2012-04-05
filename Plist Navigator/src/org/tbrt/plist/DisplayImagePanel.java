package org.tbrt.plist;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class DisplayImagePanel extends JPanel{

    private BufferedImage displayImage;
    private String path = "images/TBRT.png";

    public DisplayImagePanel() {
       try {                
          URL imageURL = DisplayImagePanel.class.getResource(path); 
          if (imageURL != null)
          {
        	  displayImage = ImageIO.read(imageURL);
          }
          else
          {
         	  System.err.println("Couldn't find file: " + path);  
          }
       } catch (IOException ex) {
          System.out.println("Display File not found!");
       }
    }

    public void paintComponent(Graphics dg) {
        dg.drawImage(displayImage, 0, 0, null);

    }
}
