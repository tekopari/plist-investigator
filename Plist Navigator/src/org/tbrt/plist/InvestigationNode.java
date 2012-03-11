package org.tbrt.plist;

public class InvestigationNode {
	
	private String nodeType;
	private String nodeValue;
	
	private InvestigationNode() {
	}
	
	public InvestigationNode(String ntype, String nvalue) {
		nodeType = ntype;
		nodeValue = nvalue;
	}
	
	public String toString() {
		return nodeValue;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	
	public String getNodeValue() {
		return nodeValue;
	}

}
