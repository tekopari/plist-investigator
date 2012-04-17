package org.tbrt.plist;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.List;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;





import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import javax.swing.*;

public class PlistSearch {

	private boolean SearchMulti = false;
	private boolean Searchdone = false;
	private String searchStr = "";
	private  String Title = "";
	
	public PlistSearch(String invName, String notesName, String plistName) {
		
		try {
			
			// During an investigation, plist name is blank, evidenceName is valid, if there is notes, output notes.
			if (plistName.length() == 0)  {
				if (invName.length() != 0)  {
					Searchdone = true;
					SearchMulti = true;
					SearchInvestigation (plistName, notesName, invName);
					return;
				}
				
			}
			File file = new File(plistName);
			
			// If plist file does not exist, nothing to do.
			if (! file.exists())  {
				Searchdone = false;
				return;
			}
			
			// If the file is empty, nothing to do say it is a success and return.
			if ( file.length() == 0)  {
				Searchdone = false;
				return;				
			}
			
			// Clear the error flag, assume everything is fine.
			Searchdone = true;
			
			GetSearchOutput (file, plistName, notesName, invName);
		} catch (Exception e) {
			System.err.println("Problem in PlistSearch Constructor");
		}
	}
	
	
	private  class sendOutput extends JFrame  {
		  JTextArea aTextArea = new JTextArea(Title);

		  
		  public sendOutput() {
		    setSize(800, 200);
		    setTitle(Title);
		    add("Center", new JScrollPane(aTextArea));
		    setVisible(true);
		  }

		  public void write(String aString) throws IOException {
		    aTextArea.append(aString);
		  }

	}

		
	private  void GetSearchString()  {
		   searchStr = JOptionPane.showInputDialog(null,
				   "Type the Search String:",
				   JOptionPane.WANTS_INPUT_PROPERTY);
		   System.out.println("Search string typed by the user: " + searchStr);
		
		
	}
	
	private  void SearchInvestigation (String plistName, String notesName, String invName)  {

		GetSearchString();
		
	}
	
	 

		 

	private  void GetSearchOutput (File file, String plistName, String notesName, String evidenceName) throws IOException  {
		 //Create a simple GUI window
		if (!SearchMulti)  {
			GetSearchString();
			// SearchWindow(evidenceName +" Plist:" + " Search");
			Title = evidenceName +" Plist:" + " Search";
			// Why can't I call it like this?  		
			sendOutput sS = new sendOutput();		
			
			for (int i=0; i < 100; i++)  {
				sS.write("Outputting to TEXT AREA===========================================================>\n");
			}
		}

	}
	
}
