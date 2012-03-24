package org.tbrt.plist;

import javax.swing.JTable;
import javax.swing.JTree;
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
    }

    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;  
    }

    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

	    protected int visibleRow;
   
	    public TreeTableCellRenderer(TreeModel model) { 
	        super(model);
	    }

	    public void setBounds(int x, int y, int w, int h) {
	        super.setBounds(x, 0, w, JTreeTable.this.getHeight());
	        return;
	    }

	    public void paint(Graphics g) {
	        g.translate(0, -visibleRow * getRowHeight());
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

