/*
* TBRT - An open source application for plist display
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
/*Copyright © 2008, 2010 Oracle and/or its affiliates. All rights reserved. Use is subject to license terms.
*Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 

*    Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
*    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation *and/or other materials provided with the distribution.
*    Neither the name of Oracle Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without *specific prior written permission.

 
*THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE *IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE *FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR *SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, *OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
*You acknowledge that this software is not designed, licensed or intended for use in the design, construction, operation or maintenance of any nuclear *facility. 
*/

package org.tbrt.plist;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Graphics;

public class JTreeTable extends JTable {
	
    protected TreeTableCellRenderer tree;

    public JTreeTable(TreeTableModel model) {
	    super();
	    
	    tree = new TreeTableCellRenderer(model); 
	    super.setModel(new TreeTableModelAdapter(model, tree));
	    tree.setSelectionModel(
	        new DefaultTreeSelectionModel() { 
	            {
		           setSelectionModel(listSelectionModel); 
	            } 
	        }
	    ); 	    
	    tree.setRowHeight(getRowHeight());
	    setDefaultRenderer(TreeTableModel.class, tree); 
	    setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());  
	    setShowGrid(false);
	    setIntercellSpacing(new Dimension(0, 0));
	    
	    if(tree.getRowHeight() < 1) {
	    	tree.setRowHeight(18);
	    }
	  
    }

    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;  
    }

    public void updateUI() {
	    super.updateUI();
	    if(tree != null) {
	        tree.updateUI();
	    }
	    // Use the tree's default foreground and background colors in the
  	    // table. 
        LookAndFeel.installColorsAndFont(this, 
        		                         "Tree.background",
                                         "Tree.foreground", 
                                         "Tree.font");
    }
    
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

	    protected int visibleRow = 0;
   
	    public TreeTableCellRenderer(TreeModel model) { 
	        super(model);
	    }

	    public void setBounds(int x, int y, int w, int h) {
	        super.setBounds(x, 0, w, JTreeTable.this.getHeight());
	        return;
	    }

	    public void paint(Graphics g) {
	        g.translate(0, -visibleRow * getRowHeight() );
	        super.paint(g);
	        return;
	    }

	    public Component getTableCellRendererComponent(JTable table,
	    		Object value,
				boolean isSelected,
				boolean hasFocus,
				int row,
				int column) {
	        if(isSelected)
		        setBackground(table.getSelectionBackground());
	        else
		        setBackground(table.getBackground());
	        visibleRow = row;
	        return this;
	    }
    }

    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
	    public Component getTableCellEditorComponent(JTable table,
	        Object value,
            boolean isSelected,
            int rowNum, 
            int columnNum) {
	        return tree;
	    }
    }
}

