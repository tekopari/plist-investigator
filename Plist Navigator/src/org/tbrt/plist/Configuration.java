//===========================================================================
// File:    Configuration.java
//
// Purpose: The configuration class hold the user configurable settings for
//          the plist tool
//===========================================================================


/* TODO:
 * 
 * SAMPLE USAGE

    String path = Configuration.getConfiguration.getHomeDir()
    	    			    + System.getProperty("file.separator")
    	    			    + "SOMEDIR";
 
 */

package org.tbrt.plist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class Configuration {
	
    //------------------------------------------------------------
    // The following method initialize the configuration data
    //------------------------------------------------------------
    private static Configuration config = null;
 	private static String installPath = ""; 
 	
    public static boolean initConfiguration(String path) {
    	try {
    	    if(config == null){
    		    config = new Configuration(path);
    		    config.setHomeDir(path);
    	    }
    	}
    	catch (Exception e) {
    		System.err.println("Error: Failed to initialize configuration");
    		System.err.println("Reason: " + e);		
    		return false;
    	}
    	return true;
    }
    
    public static boolean initWorkspace() {   	
    	String workspace = Configuration.getConfiguration().getWorkspace();
    	
    	//-------------------------------------------------------------------
    	// If the workspace is not set you the default workspace
    	// If they do not like it the can change it in the configuration
    	// GUI.
    	//-------------------------------------------------------------------
    	if(workspace == null || workspace.equals("") || workspace.equals("<Your Workspace>")) {
    		workspace = System.getProperties().getProperty("user.home") 
			         + System.getProperty("file.separator")
			         + "My Investigations";
    		Configuration.getConfiguration().setWorkspace(workspace);
    	}

		File workspaceDir = null;
		try {
			workspaceDir = new File(workspace);
				
		    if(!workspaceDir.exists()) {
			    workspaceDir.mkdirs();
		    }
		}
    	catch (Exception e) {
    		System.err.println("Error: Failed to initialize workspace");
    		System.err.println("Reason: " + e);	
    		return false;
    	}
    	return true;
    }
    
    public static Configuration getConfiguration() {
    	try {
    	    if(config == null){
    	    	// TODO: DECIDE ON DEFAULT INSTALLATION DIRECTORY
    		    String path = installPath;
    		    config = new Configuration(path);
    	    }
    	}
    	catch (Exception e) {
    		return null;
    	}
    	return config;
    }
	
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
			out.println("PLIST.WORKSPACE=" + System.getProperty("PLIST.WORKSPACE"));
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
	
	//---------------------------------------------------------------------------
	// Get the workspace
	//---------------------------------------------------------------------------
	public synchronized String getWorkspace() {
		return System.getProperty("PLIST.WORKSPACE");
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
	
	public synchronized void setWorkspace(String str) {
		p.setProperty("PLIST.WORKSPACE", str.trim());
		saveProperties();
		return;
	}
	
	public static void setInstallPath(String str) {
		installPath = str;
		return;
	}
}
