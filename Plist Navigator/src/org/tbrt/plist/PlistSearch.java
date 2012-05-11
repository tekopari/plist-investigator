/*
 * TBRT - An open source application for plist display
 * Copyright (C) 2012 Tom Pari, Blair Wolfinger, Todd Chu, Ravi Jagannathan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * The users must publish the derived work consistence with the Copyright mentioned.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.tbrt.plist;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//import org.tbrt.plist.NewPlistSearch.OutputBox;

import java.io.File;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.PropertyListParser;

public class PlistSearch {
		
	public static boolean SearchMulti = false;
	boolean Searchdone = false;
	public static OutputBox oBox;
	public static String searchStr = "";
	String Title = "";

	public void PlistSearchReset() {
		SearchMulti = false;
		searchStr = "";
		oBox = null;
	}

	public PlistSearch(String invName, String notesName, String plistName) {

		try {	
			
			// During an investigation, plist name is blank, evidenceName is
			// valid, if there is notes, output notes.
			if (plistName.length() == 0) {
				if (invName.length() != 0) {
					Searchdone = true;
					SearchMulti = true;
					SearchInvestigation(plistName, notesName, invName);
					return;
				}
			}

			File file = new File(plistName);

			// If plist file does not exist, nothing to do.
			if (!file.exists()) {
				Searchdone = false;
				return;
			}

			// If the file is empty, nothing to do say it is a success and
			// return.
			if (file.length() == 0) {
				Searchdone = false;
				return;
			}

			// Clear the error flag, assume everything is fine.
			Searchdone = true;
			SearchPlist(file, plistName, notesName, invName);	
			
		} catch (Exception e) {
			System.err.println("Problem in PlistSearch Constructor");
			System.err.println(e);		
		}
	}
	
	private class OutputBox extends JFrame {
		
        private int matches = 0;
	    private JEditorPane htmlPane    = null;
	    private JScrollPane scrollPane = null;
		private StringBuffer htmlbuffer = null;
		private ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));

		public OutputBox() {
			htmlbuffer = new StringBuffer();
			htmlbuffer.append("<html>\n<head></head>\n<body lang=EN-US>\n<div>\n");
			setSize(750, 400);
			setTitle(Title);
			setForeground(Color.blue);
			setIconImage(img.getImage());
            htmlPane = new JEditorPane();
            htmlPane.setContentType( "text/html" );
            htmlPane.setEditable(false);
            htmlPane.setText("<html>\n<head></head>\n<body></body>\n/</html>");
            scrollPane = new JScrollPane(htmlPane);
            add("Center", scrollPane);
			setVisible(true);
		}

		public void write(String aString) throws IOException {
			htmlbuffer.append(aString);
			return;
		}
		
		public void resetMatchCount() {
			matches = 0;
			return;
		}
		
		public void incrementCount() {
			matches++;
			return;
		}
		
		public int getMatchCount() {
			return matches;
		}
		
		public void showResults() {
			htmlbuffer.append("</div></body></html>\n");
			htmlPane.setText(htmlbuffer.toString());
			return;
		}
	}

	private void GetSearchString() {
		searchStr = JOptionPane.showInputDialog(null, "Type the Exact Search String:", "");
		return;
	}
	
    private String highlightMatch(String pattern, String data) {
	    String highlighted = data;
	    String replacement = "<font color=blue><b>" + pattern + "</b></font>";
	    return highlighted.replaceAll(pattern, replacement);
    }
    
	private void NSObjectSearch(PlistModel MyModel, NSObject MyObj) {

		try {

			if (MyObj == null) {
				System.out.println("MyObj is null");
				return;
			}

			// ---------------------------
			// Process current node here
			// ---------------------------
			// 0 for key name, 1 for Key Type Name and 2 for Key Value
			String KeyName     = (String) MyModel.getValueAt(MyObj, 0);
			String KeyTypeName = (String) MyModel.getValueAt(MyObj, 1);
			String KeyValue    = (String) MyModel.getValueAt(MyObj, 2);

			String Wstr = "";
			if ((KeyTypeName == "Dictionary") || (KeyTypeName == "Array")) {
				Wstr = KeyName + ":  " + KeyValue;
				if (Wstr.contains(searchStr)) {
					oBox.write("<tr><td><p>&nbsp;");
					oBox.write(highlightMatch(searchStr, KeyName));
					oBox.write("</p></td><td><p>");
					oBox.write(highlightMatch(searchStr, KeyValue));
					oBox.incrementCount();
				}
			} 
			else {
				Wstr = KeyName + " = " + KeyValue;
				if (Wstr.contains(searchStr)) {
					oBox.write("<tr><td><p>&nbsp;");
					oBox.write(highlightMatch(searchStr, KeyName));
					oBox.write("</p></td><td><p>&nbsp;");
					oBox.write(highlightMatch(searchStr, KeyValue));
					oBox.write("</p></td></tr>\n");
					oBox.incrementCount();
				}
			}

			int ChildCount = MyModel.getChildCount(MyObj);
			for (int i = 0; i < ChildCount; i++) {
				NSObject ChildObj = (NSObject) MyModel.getChild(MyObj, i);
				if (ChildObj == null) {
					System.out.println("***** Child Model is null: " + i);
					// It looks like we never reach here by design.
					continue;
				}

				NSObjectSearch(MyModel, ChildObj);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void notesSearch(String notesName) throws IOException {
		File file = new File(notesName);

		// If notes file does not exist, nothing to do.
		if (!file.exists()) {
			return;
		}

		// If the file is empty, nothing to do.
		if (file.length() == 0) {
			return;
		}

		FileInputStream fstream = new FileInputStream(notesName);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine = "";
		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			if (strLine.contains(searchStr)) {
				oBox.write("<tr><td><p>&nbsp;");
				oBox.write(highlightMatch(searchStr, strLine));
				oBox.write("</p></td></tr>\n");
				oBox.incrementCount();
			}
		}
		in.close();
        return;
	}

	private void SearchInvestigation(String plistName, String notesName, String invName) {

		//---------------------------------------------------------------
		// GET THE SEARCH STRING
		//---------------------------------------------------------------
		GetSearchString();
		if (searchStr.length() == 0) {
			Searchdone = false;
			return;
		}

		Title = "Investigation: " + invName
				+ " search results for the pattern - " + "\"" + searchStr
				+ "\"";
		oBox = new OutputBox();

		if (oBox == null) {
			// Can't even make a TextArea on a Jframe. Something wrong, just
			// return.
			return;
		}
	    
		try {
			// Search the notes file for investigations.
			oBox.write("       <p align=center><b><u>INVESTIGATION SEARCH RESULTS</u></b></p>\n");
			oBox.write("       <p>\n");
			oBox.write("          <b>Investigation:&nbsp;</b>");
			oBox.write(invName);
			oBox.write("<br><br>\n");
			oBox.write("          <b>Search Pattern:&nbsp;</b>");
			oBox.write(searchStr);
			oBox.write("\n");
			oBox.write("       </p><br>\n");
			oBox.write("       <b>Results From the Investigation Notes:</b><br>\n");
			oBox.write("       <table  border=1 cellspacing=0 cellpadding=0 width=700>\n");
			oBox.write("          <tr><td><p align=center><b>Lines Matched</b></p></td></tr>\n");
			oBox.resetMatchCount();
			notesSearch(notesName);
			oBox.write("          <tr><td><p><b>&nbsp;Number of matches:&nbsp;" + oBox.getMatchCount());
			oBox.write("</b></p></td></tr>\n</table>\n");			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void showResults() {
		if(oBox != null) {
		    oBox.showResults();
		}
		return;
	}

	private void SearchPlist(File PlistFile, String plistName, String notesName, 
			String evidenceName) throws IOException {

		// System.out.println("Entering SearchPlist\n");
		// i.e. if it is called for a single plist file, go get the search
		// string and create a TextArea to output.
		if (SearchMulti == false) {
			GetSearchString();
			Title = " INDIVIDUAL Plist Search: " + evidenceName
					+ " search results for the pattern - " + "\"" + searchStr
					+ "\"";

			oBox = new OutputBox();
		}

		if (searchStr.length() == 0) {
			return;
		}

		if (oBox == null) {
			// Can't even make a TextArea on a Jframe. Something wrong, just
			// return.
			return;
		}

		try {
			NSDictionary LocRootDict = null;

			PlistModel nPModel = new PlistModel(LocRootDict);
			LocRootDict = (NSDictionary) PropertyListParser.parse(PlistFile);
			if (LocRootDict == null) {
				System.out.println("SearchPlist:  LocRootDict is null\n");
			}
			
			//---------------------------------------------------------------
			// Search the plist file
			//---------------------------------------------------------------
			oBox.write("<p align=center><b><u>PLIST SEARCH RESULTS</u></b></p>\n");
			oBox.write("<p><b>Evidence Name:&nbsp;</b>");
			oBox.write(evidenceName);
			oBox.write("</p><br>\n");
			oBox.write("<b>Results from the Plist File:</b><br>\n");
			oBox.write("<table border=1 cellspacing=0 cellpadding=0 width=700>\n");
			oBox.write("          <tr><td><p align=center><b>Key</b></p></td><td><p align=center><b>Value</b></p></td></tr>\n");
			oBox.resetMatchCount();
			NSObjectSearch(nPModel, LocRootDict);
			oBox.write("          <tr><td><p><b>&nbsp;Number of matches:&nbsp;" + oBox.getMatchCount());
			oBox.write("</b></p></td><td><p>&nbsp;</p></td></tr>\n</table>\n<br>\n");			
			
			//---------------------------------------------------------------
			// Search the notes file too
			//---------------------------------------------------------------
			oBox.write("      <b>Results from the Plist Notes:</b><br>\n");
			oBox.write("      <table border=1 cellspacing=0 cellpadding=0 width=700>\n");
			oBox.write("        <tr><td><p align=center><b>Lines Matched</b></p></td></tr>\n");
			oBox.resetMatchCount();
			notesSearch(notesName);
			oBox.write("          <tr><td><p><b>&nbsp;Number of matches:&nbsp;" + oBox.getMatchCount());
			oBox.write("</b></p></td></tr>\n</table>\n<br>\n");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

