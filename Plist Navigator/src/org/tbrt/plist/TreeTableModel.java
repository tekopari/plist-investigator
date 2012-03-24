package org.tbrt.plist;

import javax.swing.tree.TreeModel;

public interface TreeTableModel extends TreeModel
{
    public int getColumnCount();
    public Object getValueAt(Object node, int column);
    public String getColumnName(int column);
    public Class getColumnClass(int column);
    public void setValueAt(Object value, Object node, int column);
    public boolean isCellEditable(Object node, int column);
}

