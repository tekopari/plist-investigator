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

/*
 * %W% %E%
 *
 * Copyright 1997, 1998 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer. 
 *   
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution. 
 *   
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.  
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE 
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,   
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER  
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF 
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS 
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package org.tbrt.plist;

import javax.swing.JTree;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;

public class TreeTableModelAdapter extends AbstractTableModel
{
     TreeTableModel m_model;
     JTree m_tree;

    public TreeTableModelAdapter(TreeTableModel model, JTree tree) {
        this.m_tree = tree;
        this.m_model = model;

	    tree.addTreeExpansionListener(
	        new TreeExpansionListener() {
	            public void treeExpanded(TreeExpansionEvent e) {  
	                fireTableDataChanged(); 
	            }
                public void treeCollapsed(TreeExpansionEvent e) {  
	                fireTableDataChanged(); 
	            }
	        }
	    );
    }

    public int getColumnCount() {
	    return m_model.getColumnCount();
    }
    
    public Object getValueAt(int rowNum, int columnNum) {
    	String rc = (String) (m_model.getValueAt(nodeForRow(rowNum), columnNum));
    	//System.out.println("GetValueAt[" + rowNum + "][" + columnNum + "]=[" + rc + "]");
    	return rc;
	    //return m_model.getValueAt(nodeForRow(rowNum), columnNum);
    }
    
    public String getColumnName(int columnNum) {
	    return m_model.getColumnName(columnNum);
    }

    public Class getColumnClass(int columnNum) {
	    return m_model.getColumnClass(columnNum);
    }

    public int getRowCount() {
	    return m_tree.getRowCount();
    }

    protected Object nodeForRow(int rowNum) {
	    TreePath treePath = m_tree.getPathForRow(rowNum);
	    return treePath.getLastPathComponent();         
    }

    public void setValueAt(Object obj, int rowNum, int columnNum) {
    	m_model.setValueAt(obj, nodeForRow(rowNum), columnNum);
    	return;
    }

    public boolean isCellEditable(int rowNum, int columnNum) {
        return m_model.isCellEditable(nodeForRow(rowNum), columnNum); 
    }
}

