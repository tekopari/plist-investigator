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

