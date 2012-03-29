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
    	System.out.println("GetValueAt[" + rowNum + "][" + columnNum + "]=[" + rc + "]");
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

