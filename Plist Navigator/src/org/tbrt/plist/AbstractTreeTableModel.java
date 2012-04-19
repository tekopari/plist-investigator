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

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSSet;

public abstract class AbstractTreeTableModel implements TreeTableModel {
    protected Object root;     
    protected EventListenerList listenerList = new EventListenerList();
  
    //-----------------------------------------------------------------------
    // Constructor
    //-----------------------------------------------------------------------
    public AbstractTreeTableModel(Object root) {
        this.root = root; 
    }

    //-----------------------------------------------------------------------
    // Return the root of the tree
    //-----------------------------------------------------------------------
    public Object getRoot() {
        return root;
    }

    //-----------------------------------------------------------------------
    // If the node has no children its a leaf
    //-----------------------------------------------------------------------
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0; 
    }

    //-----------------------------------------------------------------------
    // Changes are not allowed so this method does nothing
    //-----------------------------------------------------------------------
    public void valueForPathChanged(TreePath path, Object value) {
    	return;
    }
 
    public int getIndexOfChild(Object parent, Object child) {
        for (int i = 0; i < getChildCount(parent); i++) {
	        if (getChild(parent, i).equals(child)) { 
	            return i; 
	        }
        }
	    return -1; 
    }

    //-----------------------------------------------------------------------
    // Add a tree model listener via its base class
    //-----------------------------------------------------------------------
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
        return;
    }

    //-----------------------------------------------------------------------
    // Remove a tree model listener via its base class
    //-----------------------------------------------------------------------
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
        return;
    }

    //-----------------------------------------------------------------------
    // Notify the listeners that the event occurred
    //-----------------------------------------------------------------------
    private void fireTreeModelEvent(Object source, 
                                    Object[] path, 
                                    int[] childIndices, 
                                    Object[] children,
                                    char eventtype) {
    	//-------------------------------------------------------------------
        // Get the array of listeners -- should alo
    	//-------------------------------------------------------------------
    	if(listenerList == null) {
    		return;
    	}
        Object[] listeners = listenerList.getListenerList();
        
        //-------------------------------------------------------------------
        // Create the event from the input parameters
        //-------------------------------------------------------------------
        TreeModelEvent e = new TreeModelEvent(source, 
        		                              path, 
                                              childIndices, 
                                              children);
        
        //-------------------------------------------------------------------
        // Notify each listener of the event
        //-------------------------------------------------------------------
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==TreeModelListener.class) {
            	switch(eventtype) {
            	    case 'i':
            	    	((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
            		    break;
            	    case 'r':
            	    	((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);
            	        break;
            	    case 'c':
            	        ((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
            	        break;
            	    case 's':
            	    	((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
            		    break;
            		default:
            			System.err.println("Invalid event type");
            	}        
            }          
        }
        return;
    }

    //-----------------------------------------------------------------------
    // Notify the listeners that nodes have been inserted
    //-----------------------------------------------------------------------
    protected void fireTreeNodesInserted(Object source, 
                                         Object[] path, 
                                         int[] childIndices, 
                                         Object[] children) {
    	fireTreeModelEvent(source, path, childIndices, children, 'i');
    	return;
    }

    //-----------------------------------------------------------------------
    // Notify the listeners that nodes have been removed
    //-----------------------------------------------------------------------
    protected void fireTreeNodesRemoved(Object source, 
    		                            Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
    	fireTreeModelEvent(source, path, childIndices, children, 'r');
    	return;
    }
    
    //-----------------------------------------------------------------------
    // Notify the listeners that nodes have been changed
    //-----------------------------------------------------------------------
    protected void fireTreeNodesChanged(Object source, 
    		                            Object[] path, 
                                        int[] childIndices, 
                                        Object[] children) {
    	fireTreeModelEvent(source, path, childIndices, children, 'c');
    	return;
    }

    //-----------------------------------------------------------------------
    // Notify the listeners that nodes structure has changed
    //-----------------------------------------------------------------------
    protected void fireTreeStructureChanged(Object source, 
    		                                Object[] path, 
                                            int[] childIndices, 
                                            Object[] children) {
    	fireTreeModelEvent(source, path, childIndices, children, 's');
    	return;
    }

    //-----------------------------------------------------------------------
    // Return the base java Object class
    //-----------------------------------------------------------------------
    public Class getColumnClass(int column) { 
    	return Object.class; 
    }

    //----------------------------------------------------------------------- 
    // Only the first column of the table is editable.  This causes the
    // JTable events to be sent to the JTree implementation which enables
    // the expansion/contraction of the tree nodes
    //-----------------------------------------------------------------------
    public boolean isCellEditable(Object node, int column) { 
    	if(column != 0) {
    		return false;
    	}
    	
    	String name = node.getClass().getName();
    	
    	if(name.equals("com.dd.plist.NSArray") ||
    	   name.equals("com.dd.plist.NSDictionary")|| 
    	   name.equals("com.dd.plist.NSSet")) {
    		//System.out.println("IsEditable NAME=" + name);
    		return true;
    	}
    	return false;	
        
        //return getColumnClass(column) == TreeTableModel.class;
    }

    //-----------------------------------------------------------------------
    // Do not allow changes to the values
    //-----------------------------------------------------------------------
    public void setValueAt(Object value, Object node, int column) {
    	return;
    }
}

