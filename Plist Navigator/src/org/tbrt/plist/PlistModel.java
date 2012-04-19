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

package org.tbrt.plist;

import com.dd.plist.*;

public class PlistModel extends AbstractTreeTableModel implements TreeTableModel {

    //-----------------------------------------------------------------------
	// The column names used as the header in the Tree Table
    //-----------------------------------------------------------------------
    static protected String[] columnNames = {
    	"Key", 
    	"Type", 
    	"Value"};

    //-----------------------------------------------------------------------
    // The class types of the each of the three columns
    //-----------------------------------------------------------------------
    static protected Class[] columnDataTypes = {
    	TreeTableModel.class, 
    	String.class, 
    	String.class};

    //-----------------------------------------------------------------------
    // The PlistModel constructor
    //-----------------------------------------------------------------------
    public PlistModel(NSObject root) { 
	    super(root); // TODO
    }

    //-----------------------------------------------------------------------
    // The getColumnName method returns the name of the specified column
    //-----------------------------------------------------------------------
    public String getColumnName(int column) {
	    return columnNames[column];
    }
    
    //-----------------------------------------------------------------------
    // The getColumnCount method returns the number of columns
    //-----------------------------------------------------------------------
    public int getColumnCount() {
	    return columnNames.length;
    }

    //-----------------------------------------------------------------------
    // The getColumnClass method returns the name of the specified column
    //-----------------------------------------------------------------------
    public Class getColumnClass(int column) {
	    return columnDataTypes[column];
    } 

    //-----------------------------------------------------------------------
    // Container classes have children others do not
    //-----------------------------------------------------------------------
    public int getChildCount(Object node) {
    	if(node == null) {
    		//System.out.println("getChildCount(): node is null" );
    		return 0;
    	}
    	
    	int rc = 0;
    	String name = node.getClass().getCanonicalName();
    	if(name.equals("com.dd.plist.NSArray")) {
    		rc = ((NSArray)node).count();
    	}
    	else if(name.equals("com.dd.plist.NSDictionary")){
    		rc = ((NSDictionary)node).count();
    	}
    	else if(name.equals("com.dd.plist.NSSet")) {
    	    rc = ((NSSet)node).count();
    	}
    	else {
    		rc = 0;	
    	}
    	//System.out.println("getChildCount(): ClassName is " + name + " count is " + rc );
    	return rc;
    }

    //-----------------------------------------------------------------------
    // Container classes are not leafs
    //-----------------------------------------------------------------------
    public boolean isLeaf(Object node) {
    	String name = node.getClass().getCanonicalName();
    	//System.out.println("isLeaf(): TYPE[" + name + "]" );
    	if(name.equals("com.dd.plist.NSArray") ||
    	   name.equals("com.dd.plist.NSDictionary") ||
    	   name.equals("com.dd.plist.NSSet")) {
    		return false;
    	}
    	else if(name.equals("com.dd.plist.NSData") ||
    			name.equals("com.dd.plist.NSDate") ||
    			name.equals("com.dd.plist.NSNumber") ||
    			name.equals("com.dd.plist.NSString")) {
    	    return true;
    	}
    	return true;
    }

    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    protected Object[] getChildren(Object node) {
    	String name = node.getClass().getCanonicalName();
    	if(name.equals("com.dd.plist.NSArray")) {
    		return ((NSArray)node).getArray();
    	}
    	else if(name.equals("com.dd.plist.NSDictionary")) {
    		return ((NSDictionary)node).getAllObjects();
    	}
    	else if(name.equals("com.dd.plist.NSSet")) {
    		return ((NSSet)node).allObjects();
    	}
    	return null; 
    }
    
    //-----------------------------------------------------------------------
    // Get the child of the node
    //-----------------------------------------------------------------------
    public Object getChild(Object node, int i) { 
	    return getChildren(node)[i]; 
    }
    
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    private String getKeyName(Object node) {
    	if(node == null) {
    		return "null";
    	}
    	
    	String rc = "";
    	rc = new String(((NSObject)node).getKey());
    	//System.out.println("GETKEY[" + rc + "]");
 // DEBUG--------------
    	/*
    	String name = node.getClass().getCanonicalName();
    	if(name.equals("com.dd.plist.NSArray")){
    		rc = ((NSArray)node).getKey();	
    	}
    	else if (name.equals("com.dd.plist.NSDictionary")) {	
    		NSDictionary d = (NSDictionary)node;
    		rc = d.getKey();  
    	}
    	else if (name.equals("com.dd.plist.NSSet")) {
    		rc = ((NSSet)node).getKey();	
    	}
    	else if (name.equals("com.dd.plist.NSData")) {
    		rc = ((NSData)node).getKey();			
    	}
    	else if (name.equals("com.dd.plist.NSDate")) {
    		rc = ((NSDate)node).getKey();	
    	}
    	else if (name.equals("com.dd.plist.NSNumber")) {
    		rc = ((NSNumber)node).getKey();	
    	}
    	else if (name.equals("com.dd.plist.NSString")) {
    		rc = ((NSString)node).getKey();			
    	}
    	else {
    		rc = "unknown";
    	}
    	*/
 // DEBUG--------------------
    	return rc;
    }
    
    //-----------------------------------------------------------------------
    // Get the type name of the object
    //-----------------------------------------------------------------------
    private String getTypeName(Object node) {
    	if(node == null) {
    		return "null";
    	}
    	
    	String rc = "";
    	String name = node.getClass().getCanonicalName();
    	if(name.equals("com.dd.plist.NSArray")) {
    		rc = "Array";
    	}
    	else if(name.equals("com.dd.plist.NSDictionary")){
    		rc = "Dictionary";
    	}
    	else if(name.equals("com.dd.plist.NSSet")) {
    		rc = "Set";
    	}
    	else if(name.equals("com.dd.plist.NSData")) {
    		rc = "Data";
    	}
    	else if(name.equals("com.dd.plist.NSDate")) {
    		rc = "Date";
    	}
    	else if(name.equals("com.dd.plist.NSNumber")) {
    		int numtype = ((NSNumber)node).type();
    		if(numtype == com.dd.plist.NSNumber.INTEGER) {
    			rc = "Integer";
    		}
    		else if(numtype == com.dd.plist.NSNumber.REAL) {
    			rc = "Real";
    		}
    		else if(numtype == com.dd.plist.NSNumber.BOOLEAN) {
    			rc = "Boolean";
    		}
    		else {
    			rc = "Number";
    		}
    	}
    	else if(name.equals("com.dd.plist.NSString")) {
    		rc = "String";	
    	}
    	else {
    		rc = "Unknown";
    	}
    	return rc;
    }
    
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    private String getDataValue(Object node) {
    	if(node == null) {
    		return "null";
    	}
    	
    	String rc = "";
    	String name = node.getClass().getCanonicalName();
    	if(name.equals("com.dd.plist.NSArray")) {
    		rc = "(" + Integer.toString(((NSArray)node).count()) + " items)";
    	}
    	else if(name.equals("com.dd.plist.NSDictionary")){
    		rc = "(" + Integer.toString(((NSDictionary)node).count()) + " items)";
    	}
    	else if(name.equals("com.dd.plist.NSSet")) {
    	    rc = "(" + Integer.toString(((NSSet)node).count()) + " items)";
    	}
    	else if(name.equals("com.dd.plist.NSData")) {
    		rc = ((NSData)node).getBase64EncodedData();
    	}
    	else if(name.equals("com.dd.plist.NSDate")) {
    		rc = ((NSDate)node).toStringValue();
    	}
    	else if(name.equals("com.dd.plist.NSNumber")) {
    		rc = ((NSNumber)node).toStringValue();
    	}
    	else if(name.equals("com.dd.plist.NSString")) {
    		rc = ((NSString)node).toStringValue();
    	}
    	else {
    		rc = "unknown";
    	}
    	return rc;
    }
    
    //-----------------------------------------------------------------------
    // Get the value at a location in the tree table
    //-----------------------------------------------------------------------
    public Object getValueAt(Object node, int column) {
	    try {
	        switch(column) {
	            case 0:  // KEY NAME
		            return getKeyName(node);
	            case 1:  // TYPE NAME
		            return getTypeName(node);
	            case 2:  // VALUE
		            return getDataValue(node);
	        }
	    }
	    catch  (Exception se) {
        }
        return null; 
    }
}


