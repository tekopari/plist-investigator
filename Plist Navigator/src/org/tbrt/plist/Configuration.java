/*
* tbrt - An open source application for plist display
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
 	
    private static boolean initConfiguration(String path) {
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
    
    private static boolean initWorkspace() { 
    	String workspace = Configuration.getConfiguration().getWorkspace();
    	
    	//-------------------------------------------------------------------
    	// If the workspace is not set you the default workspace
    	// If they do not like it the can change it in the configuration
    	// GUI.
    	//-------------------------------------------------------------------
    	if(workspace == null || workspace.equals("") || workspace.equals("<Your Workspace>")) {
    		int len = System.getProperty("user.home").length();
    		char[] tmp = System.getProperty("user.home").toCharArray();
    		for(int i = 0; i < len; i++) {
    			if(tmp[i] == '\\') {
    				tmp[i] = '/';
    		    }	
    		}
    		workspace = new String(tmp) + "/My Investigations";
    		
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
	
	private static String removeBackSlashes(String str) {
		int len = str.length();
		char[] tmp = str.toCharArray();
		for(int i = 0; i < len; i++) {
			if(tmp[i] == '\\') {
				tmp[i] = '/';
		    }	
		}
		return new String(tmp);
	}
	
	private static String getInstallPath() {
        String rc = "";
        Class myclass = Configuration.class;
        String path = myclass.getClassLoader().getResource(
            myclass.getName().replace('.', '/') + ".class").toString();
        
        if(path == null) {
        	rc = "";
        }
        else if (path.startsWith("jar:file:")) {
        	rc = path.substring(10, path.lastIndexOf(".jar"));
        	rc = rc.substring(0, rc.lastIndexOf("/"));
        }
        else if (path.endsWith(".class")) {
        	// Running from eclipse
        	for(int count = 0; count < 5; count++) {
            	path = path.substring(0, path.lastIndexOf("/"));
        	}
            rc = path.substring(6);
        }
        else {
        	rc = "/";
        }
        rc = rc.replaceAll("%20", " ");

        return rc;
	}
	
	public static void init() {
 		//-------------------------------------------------------------------
		// Set the install path
		//-------------------------------------------------------------------
		String installPath = Configuration.getInstallPath();		
		Configuration.setInstallPath(installPath);
		
		//-------------------------------------------------------------------
		// The INSTALL PATH must be a directory
		//-------------------------------------------------------------------
		try {
 			File installDir = new File(installPath); 
 			if(!(installDir.isDirectory())) {
 				System.err.println("Error: Specified INSTALL_DIR[" + installPath + "] is not an existing directory.");
 				System.exit(1);		
 			}
		} 
		catch (Exception e) {
			System.err.println("Error: " + e.toString());
 			System.exit(1);		
		}
	
		//---------------------------------------------------------------
        // Initialize the application	
		//---------------------------------------------------------------
		Configuration.initConfiguration(installPath);
		
		//---------------------------------------------------------------
		// Check for the workspace directory in the users home directory
		//---------------------------------------------------------------
		Configuration.initWorkspace();
		
		return;
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
		p.setProperty("PLIST.HOME_DIR", removeBackSlashes(str.trim()));
		saveProperties();
		return;
	}
	
	public synchronized void setWorkspace(String str) {
		p.setProperty("PLIST.WORKSPACE", removeBackSlashes(str.trim()));
		saveProperties();
		return;
	}
	
	public static void setInstallPath(String str) {
		installPath = removeBackSlashes(str.trim());
		return;
	}
}
