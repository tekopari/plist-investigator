package org.tbrt.plist;

import java.io.File;


// http://pdfbox.apache.org/download.html
// http://pdfbox.apache.org/userguide/cookbook.html
// http://pdfbox.apache.org/userguide/cookbook/creation.html#HelloWorld

public class Directory {

	//bew, initial code to create directory per plist file.
	// first thing is to remove the .plist extension.
	// TODO: need to figure out how to remove the C:\dirname, so that we can
	//   just use the filename, and then add our own path name.  I.e. We will 
	//   not usually be creating the directoy in same location as where plist
	//   file may be.
	// TODO: Add additional error check, ie. what if dir exists, or invalid name.
	// TODO: Use the final code for getting filename, to also use when displaying
	//       on the GUI.
	
	public static void CreateDirectory(String s){
		s = s.substring(0, s.lastIndexOf('.'));
		//File f = new File("C:" + File.separator + "temp" + File.separator + s);
		File f = new File(s);
		try{
			f.mkdir();
			} catch (Exception e) {
				e.printStackTrace(); //TODO: Add code to handle displaying correct error codes
				}
		}
}
