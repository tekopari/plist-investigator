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

import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;

public class PlistSearch {

	public static boolean SearchMulti = false;
	boolean Searchdone = false;
	String searchStr = "";
	String Title = "";
	OutputBox oBox;
	
	public void PlistSearchReset()  {
		SearchMulti = false;
		searchStr = "";
	}
	
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
			
			SearchPlist (file, plistName, notesName, invName);
		} catch (Exception e) {
			System.err.println("Problem in PlistSearch Constructor");
		}
	}
	
	
	private  class OutputBox extends JFrame  {
		  JTextArea aTextArea = new JTextArea(Title);

		  
		  public OutputBox() {
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
				   "Type the Exact Search String:", "");
		   System.out.println("Search string typed by the user: " + searchStr);
	}
	
    private void NSObjectSearch (PlistModel MyModel, NSObject MyObj)  {

    	try {
    		
    		if (MyObj == null)  {
    			System.out.println("MyObj is null");
    			return;
    		} 
    		
    		//---------------------------
    		// Process current node here
    		//---------------------------
 			   // 0 for key name, 1 for Key Type Name and 2 for Key Value
    		   String KeyName = (String) MyModel.getValueAt(MyObj, 0);
    		   String KeyTypeName = (String) MyModel.getValueAt(MyObj, 1);
    		   String KeyValue = (String) MyModel.getValueAt(MyObj, 2);	   

    		   String Wstr = "";
    		   // Good for debug - System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    		   if ( (KeyTypeName == "Dictionary") ||(KeyTypeName == "Array"))  {
    			   // For these types, don't print anything for the very first object.  For some reason, it is empty.
    				   System.out.println(KeyName + ":  " + KeyValue);
    				   Wstr = KeyName + ":  " + KeyValue;
    				   // oBox.write(Wstr  + "\n");
    				   if (Wstr.contains(searchStr))  {
    					   // Search string is in this line.  Print it.
    					   oBox.write(Wstr  + "\n");
    				   }
    		   }  else {
    			   // System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    			   System.out.println(KeyName + " = " + KeyValue);
    			   Wstr = KeyName + " = " + KeyValue;
    			   // oBox.write(Wstr  + "\n");
				   if (Wstr.contains(searchStr))  {
					   // Search string is in this line.  Print it.
					   oBox.write(Wstr  + "\n");
				   }
    		   }
    		   

    		   
			int ChildCount = MyModel.getChildCount(MyObj);
			// System.out.println("getChildCount(): is " + ChildCount);
			
			for ( int i = 0; i < ChildCount; i++)  {
				// System.out.println("calling nPModel with ChildCount " + i);
				NSObject ChildObj = (NSObject) MyModel.getChild(MyObj, i);
				if (ChildObj == null)  {
					System.out.println("***** Child Model is null: " + i);
					// It looks like we never reach here by design.
					continue;
				}
				
				NSObjectSearch (MyModel, ChildObj);
			}

    	}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
    }

	
	private  void SearchInvestigation (String plistName, String notesName, String invName)  {
		
		System.out.println("in SearchInvestigation\n" );
		GetSearchString();		   
		if (searchStr.length() == 0)  {
			   return;  
		}
		
		Title = "Investigation: " + invName + " search resuts for the pattern - " + "\"" + searchStr + "\"";
		oBox = new OutputBox();
		
		try  {
		}  catch(Exception ex) {
		  ex.printStackTrace();
	    }
	}
		 

	private  void SearchPlist (File PlistFile, String plistName, String notesName, String evidenceName) throws IOException  {
		
		// i.e. if it is called for a single plist file, go get the search string.
		if (SearchMulti == false)  { 	
			GetSearchString();	
		}
				
		if (searchStr.length() == 0)  {
		   return;  
		}
			
		Title = " INDIVIDUAL Plist Search: " + evidenceName +  " search resuts for the pattern - " + "\"" + searchStr + "\"";
 		oBox = new OutputBox();		
		
 		/**** test
		for (int i=0; i < 100; i++)  {
			oBox.write("Outputting to TEXT AREA===========================================================>\n");
		}
		 * **** test*/
	
				
		try  {
			NSDictionary LocRootDict = null;

			PlistModel nPModel = new PlistModel(LocRootDict);
			LocRootDict = (NSDictionary)PropertyListParser.parse(PlistFile);
			if (LocRootDict == null)  {
				System.out.println("SearchPlist:  LocRootDict is null\n");
			}
			NSObjectSearch (nPModel, LocRootDict);
		}  catch(Exception ex) {
		  ex.printStackTrace();
	    }

	}
	
}
