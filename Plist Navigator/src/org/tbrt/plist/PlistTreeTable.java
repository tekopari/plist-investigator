package org.tbrt.plist;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JScrollPane;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

public class PlistTreeTable
{
	private int rc = 0;
	
    public static void main(String args[])
    {
    	String filename = "C:\\Documents and Settings\\paritj\\My Documents\\NetworkInterfaces.xml";
    	//String filename = "C:\\Documents and Settings\\paritj\\My Documents\\PlistOne.xml";
    	PlistTreeTable p = new PlistTreeTable(filename, 1);
    }
    
    public PlistTreeTable(String filename, int mode) {
    	
    	//-------------------------------------------------------------------
    	// Parse the plist file
    	//-------------------------------------------------------------------
    	NSDictionary rootDict = null;
        try {
            File file = new File(filename);
            rootDict = (NSDictionary)PropertyListParser.parse(file);
            // ravi: Why can't we also use rootDict to create PDF?
            rootDict.setKey(file.getName());
        }
        catch (Exception e)
        {
          System.err.println("Cannot use the PropertyListParser");
          rc = 1;
          return;
        }
    	
        if (mode == 0) {
        	return;
        }
        
        //-------------------------------------------------------------------
        // The frame will have the same name as the file
        //-------------------------------------------------------------------
	    final JFrame frame = new JFrame(filename);
	    frame.addWindowListener(
	        new WindowAdapter() {
	            public void windowClosing(WindowEvent we) {
	            	frame.dispose();
	            }
	        }
	    );
	    
		ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
		frame.setIconImage(img.getImage());
		frame.setBounds(200, 100, 500, 400);
		
	    //-------------------------------------------------------------------
	    // Create the panel to display the plist in
	    //-------------------------------------------------------------------
	    JTreeTable treeTable = new JTreeTable(new PlistModel(rootDict));
	    //MyTreeTable treeTable = new MyTreeTable(new PlistModel(rootDict));
	    frame.getContentPane().add(new JScrollPane(treeTable));
	    frame.pack();
	    frame.show();
    }
    
    public int status () {
    	return(rc);
    }
}
