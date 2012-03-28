//===========================================================================
// File:    Configuration.java
//
// Purpose: The configuration class hold the user configurable settings for
//          the plist tool
//===========================================================================

package org.tbrt.plist;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class Configuration {
	
	//---------------------------------------------------
	// The name of the file hold the configuration values
	//---------------------------------------------------
	private String m_filename = "";
	
	//------------------------------------------------------
	// The configuration values and system properties values
	//------------------------------------------------------
	private Properties p = null;
	
	public Configuration(String path) throws Exception {

		//-------------------------------------------
		// Set the pathname to the configuration file
		//-------------------------------------------
		m_filename = path 
				   + System.getProperty("file.separator")
				   + "properties.txt";
		
		//-----------------------------
        // Set up new properties object
		//-----------------------------
        FileInputStream propFile = new FileInputStream(m_filename);
        p = new Properties(System.getProperties());
        p.load(propFile);

        //--------------------------
        // Set the system properties
        //--------------------------   
        System.setProperties(p);
        propFile.close();
	}
	
	//---------------------------------------------------------------------------
	// Save the configuration value to the config file
	//---------------------------------------------------------------------------
	private void saveProperties() {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(m_filename));
			out.println("PLIST.INVESTIGATOR.NAME=" + System.getProperty("PLIST.INVESTIGATOR.NAME"));
			out.println("PLIST.INVESTIGATOR.PHONE=" + System.getProperty("PLIST.INVESTIGATOR.PHONE"));
			out.println("PLIST.INVESTIGATOR.EMAIL=" + System.getProperty("PLIST.INVESTIGATOR.EMAIL"));
			out.println("PLIST.VERSION=" + System.getProperty("PLIST.VERSION"));
			out.println("PLIST.HOME_DIR=" + System.getProperty("PLIST.HOME_DIR"));
			out.close();							
		}
		catch (Exception e) {
			System.err.println("ERROR: Failed to write file [" + m_filename + "] :");
			System.err.println(e);
		}
		return;
	}
	
	//---------------------------------------------------------------------------
	// Get the investigator's name
	//---------------------------------------------------------------------------
	public synchronized String getInvestigatorName() {
		return System.getProperty("PLIST.INVESTIGATOR.NAME");
	}
	
	//---------------------------------------------------------------------------
	// Get the investigator's phone number
	//---------------------------------------------------------------------------
	public synchronized String getInvestigatorPhone() {
		return System.getProperty("PLIST.INVESTIGATOR.PHONE");
	}
	
	//---------------------------------------------------------------------------
	// Get the investigator's email address
	//---------------------------------------------------------------------------
	public synchronized String getInvestigatorEmail() {
		return System.getProperty("PLIST.INVESTIGATOR.EMAIL");
	}
	
	//---------------------------------------------------------------------------
	// Get the version number
	//---------------------------------------------------------------------------
	public synchronized String getVersion() {
		return System.getProperty("PLIST.VERSION");
	}
	
	//---------------------------------------------------------------------------
	// Get the plist application's home directory
	//---------------------------------------------------------------------------
	public synchronized String getHomeDir() {
		return System.getProperty("PLIST.HOME_DIR");
	}
	
	public synchronized void setInvestigatorName(String str) {
		p.setProperty("PLIST.INVESTIGATOR.NAME", str.trim());
		saveProperties();
		return;
	}
	
	public synchronized void setInvestigatorPhone(String str) {
		
		p.setProperty("PLIST.INVESTIGATOR.PHONE", str.trim());
		saveProperties();
		return;
	}
	
	public synchronized void setInvestigatorEmail(String str) {
		p.setProperty("PLIST.INVESTIGATOR.EMAIL", str.trim());
		saveProperties();
		return;
	}
	
	public synchronized void setVersion(String str) {
		p.setProperty("PLIST.VERSION", str.trim());
		saveProperties();
		return;
	}
	
	public synchronized void setHomeDir(String str) {
		p.setProperty("PLIST.HOME_DIR", str.trim());
		saveProperties();
		return;
	}
}
