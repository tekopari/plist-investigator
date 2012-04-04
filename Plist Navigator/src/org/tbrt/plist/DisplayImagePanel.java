package org.tbrt.plist;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class DisplayImagePanel extends JPanel{

    private BufferedImage displayImage;

    public DisplayImagePanel() {
       try {                
          displayImage = ImageIO.read(new File("c:\\tmp\\TBRT.png"));
       } catch (IOException ex) {
          System.out.println("Display File not found!");
       }
    }

    public void paintComponent(Graphics dg) {
        dg.drawImage(displayImage, 0, 0, null);

    }
}
