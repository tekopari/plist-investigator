package org.tbrt.plist;

import java.io.File;

public class PlistMetaData {
    private String evidenceId;
    private String evidenceName;
    private String plistFilename;
    private File plistFile;
    
	public PlistMetaData() {
	    evidenceId = "";
	    evidenceName = "";
	    plistFilename = "";
	    plistFile = null;
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
}
