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

import java.util.EventObject;
import javax.swing.CellEditor;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;

public class AbstractCellEditor implements CellEditor {

    protected EventListenerList listenerList = new EventListenerList();

    public Object getCellEditorValue() {
    	return null;
    }
    
    public boolean isCellEditable(EventObject e) {
    	return true; 
    }
    
    public boolean shouldSelectCell(EventObject e) {
    	return false;
    }
    
    public boolean stopCellEditing() {
    	return true; 
    }
    
    public void cancelCellEditing() {
    	return;
    }

    public void addCellEditorListener(CellEditorListener l) {
	    listenerList.add(CellEditorListener.class, l);
        return;
    }

    public void removeCellEditorListener(CellEditorListener l) {
	    listenerList.remove(CellEditorListener.class, l);
        return;
    }

    protected void fireEditingStopped() {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	        if (listeners[i] == CellEditorListener.class) {
		        ((CellEditorListener)listeners[i+1]).editingStopped(new ChangeEvent(this));
	        }	       
	    }
	    return;
    }

    protected void fireEditingCanceled() {
	    Object[] listeners = listenerList.getListenerList();
	    for (int i = listeners.length-2; i>=0; i-=2) {
	        if (listeners[i] == CellEditorListener.class) {
		        ((CellEditorListener)listeners[i+1]).editingCanceled(new ChangeEvent(this));
	        }	       
	    }
	    return;
    }
    
    
}