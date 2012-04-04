package org.tbrt.plist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Properties;

public class PlistMetaData {
	private String investigationDir;

	private String evidenceId;
    private String evidenceName;
    private String plistFilename;
    private File plistFile;
    
	public PlistMetaData() {
		investigationDir = "";
	    evidenceId = "";
	    evidenceName = "";
	    plistFilename = "";
	    plistFile = null;
	}
	
	public String getInvestigationDir() {
		return investigationDir;
	}

	public void setInvestigationDir(String investigationDir) {
		this.investigationDir = investigationDir;
	}
	
	private String removeBackSlashes(String str) {
		int len = str.length();
		char[] tmp = str.toCharArray();
		for(int i = 0; i < len; i++) {
			if(tmp[i] == '\\') {
				tmp[i] = '/';
		    }	
		}
		return new String(tmp);
	}
	public String getEvidenceId() {
		return evidenceId;
	}
	public void setEvidenceId(String evidenceId) {
		this.evidenceId = evidenceId;
	}
	public String getEvidenceName() {
		return evidenceName;
	}
	public void setEvidenceName(String evidenceName) {
		this.evidenceName = evidenceName;
	}
	public String getPlistFilename() {
		return plistFilename;
	}
	public void setPlistFilename(String plistFilename) {
		this.plistFilename = removeBackSlashes(plistFilename);
	}
	public File getFile() {
		return plistFile;
	}
	public void setFile(File file) {
		this.plistFile = file;
	}
	
	private void save(String filename) {
		try {
			PrintWriter out = new PrintWriter(new FileWriter(filename));
			out.println("EVIDENCE.ID=" + this.getEvidenceId());
			out.println("EVIDENCE.NAME=" + this.getEvidenceName());
			out.println("INVESTIGATION.DIR=" + this.getInvestigationDir());
			out.println("PLIST.FILE=" + this.getPlistFilename());
			out.close();							
		}
		catch (Exception e) {
			System.err.println("ERROR: Failed to write file [" + filename + "] :");
			System.err.println(e);
		}
		return;
	}
	
	private void load(String filename) {
		Properties p = null;
        FileInputStream propFile = null;
		try {
			propFile = new FileInputStream(filename);
			p = new Properties();
            p.load(propFile);
            this.setEvidenceId(p.getProperty("EVIDENCE.ID", ""));
            this.setEvidenceName(p.getProperty("EVIDENCE.NAME", ""));
            this.setInvestigationDir(p.getProperty("INVESTIGATION.DIR", ""));
            this.setPlistFilename(p.getProperty("PLIST.FILE", ""));
            this.setFile(null);
		} catch (Exception e) {
			System.err.println("ERROR: Failed to read file [" + filename + "] :");
			System.err.println(e);
		}
        return;
	}
	
}
