package org.tbrt.plist;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

public class InvestigationTree extends JPanel {
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    
    protected JPopupMenu investigationsPopup;
    protected JPopupMenu investigationPopup;
    protected JPopupMenu evidenceItemsPopup;
    protected JPopupMenu evidenceItemPopup;
    protected JPopupMenu notesPopup;
    protected JPopupMenu notePopup;
    protected JPopupMenu unknownPopup;

    public InvestigationTree() {
        super(new GridLayout(1,0));
        rootNode = new DefaultMutableTreeNode(new InvestigationNode("Investigations", "Investigations"));
        treeModel = new DefaultTreeModel(rootNode);
	    treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        
        // TODO - rework the popup menus and add listeners
        investigationsPopup = new JPopupMenu();
        investigationPopup = new JPopupMenu();
        evidenceItemsPopup = new JPopupMenu();
        evidenceItemPopup = new JPopupMenu();
        notesPopup = new JPopupMenu();
        notePopup = new JPopupMenu();
        unknownPopup = new JPopupMenu();
        
        investigationsPopup.add(new JMenuItem("investigations option#1"));
        investigationsPopup.add(new JMenuItem("investigations option#2"));
        investigationsPopup.add(new JMenuItem("investigations option#3"));
        
        investigationPopup.add(new JMenuItem("investigation option#1"));
        investigationPopup.add(new JMenuItem("investigation option#2"));
        investigationPopup.add(new JMenuItem("investigation option#3"));
        
        evidenceItemsPopup.add(new JMenuItem("evidence items option#1"));
        evidenceItemsPopup.add(new JMenuItem("evidence items option#2"));
        
        evidenceItemPopup.add(new JMenuItem("evidence item option#1"));
        evidenceItemPopup.add(new JMenuItem("evidence item option#2"));
        
        notesPopup.add(new JMenuItem("notes option#1"));
        notesPopup.add(new JMenuItem("notes option#2"));
        
        notePopup.add(new JMenuItem("note option#1"));
        notePopup.add(new JMenuItem("note option#2"));
        
        unknownPopup.add(new JMenuItem("unknown option"));
        
        tree.addMouseListener(new MyTreeMouseListener());

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }
    
    //=======================================================================
    // Remove Selected Node
    //======================================================================= 
    public void removeSelectedNode() {
        TreePath currentSelection = tree.getSelectionPath();
        
        //-------------------------------------------------------------------
        // Verify the user selected a node
        //-------------------------------------------------------------------
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            
            InvestigationNode iNode = (InvestigationNode)(currentNode.getUserObject());
            String nodeType = iNode.getNodeType();
            
            //---------------------------------------------------------------
            // Do not allow the root node or structures we create
            //---------------------------------------------------------------
            if(nodeType.equals("Investigations") ||
               nodeType.equals("EvidenceItems") ||
               nodeType.equals("Notes")) {
            	return;
            }
            
            //---------------------------------------------------------------
            // Do not allow the root node to be removed
            //---------------------------------------------------------------
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                // TODO handle the deletion of the files
                return;
            }
        }
        else {
        	//---------------------------------------------------------------
        	// No selection has been made
        	//---------------------------------------------------------------
        	// TODO - set message informing user they did not select anything
        }
    }
    
    //=======================================================================
    // Remove All Nodes
    //=======================================================================   
    public void removeAllNodes() {
    	// TODO 
        rootNode.removeAllChildren();
        treeModel.reload();
        return;
    }

    //=======================================================================
    // Add a child node to the selected node
    //======================================================================= 
    public DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = tree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)
                         (parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child) {
        return addObject(parent, child, false);
    }

    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
                                            Object child, 
                                            boolean shouldBeVisible) {
    	// TODO - objects we add should be of type InvestigationNode
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        if (parent == null) {
            parent = rootNode;
        }
	
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

        if (shouldBeVisible) {
            tree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }
    
    class MyTreeMouseListener implements MouseListener {
    	public void mousePressed(MouseEvent e) {
    		// TODO Auto-generated method stub
    		
            String nodeName = "";
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selPath == null) {
                return;
            }
            else {
            	Object x = selPath.getLastPathComponent();
            	if(x == null) {
            		nodeName = "UNKNOWN";
            	}
            	else {
            		nodeName = ((InvestigationNode)((DefaultMutableTreeNode)x).getUserObject()).getNodeType();
            	}
                tree.setSelectionPath(selPath);
            }	
    		
    	    if (e.isPopupTrigger()) {
    	    	if (nodeName.equals("EvidenceItems")) {
    	    		evidenceItemsPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("EvidenceItem")) {
    	    		evidenceItemPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Notes")) {
    	    		notesPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Note")) {
    	    		notePopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Investigation")) {
    	    		investigationPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Investigations")) {
    	    		investigationsPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    }
    	    return;
    	}
    	
    	public void mouseReleased(MouseEvent e) {
    		// TODO Auto-generated method stub
            
            String nodeName = "";
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selPath == null){
                return;
            }else{
            	Object x = selPath.getLastPathComponent();
            	if(x == null) {
            		nodeName = "UNKNOWN";
            	}
            	else {
            		nodeName = ((InvestigationNode)((DefaultMutableTreeNode)x).getUserObject()).getNodeType();
            	}
                tree.setSelectionPath(selPath);
            }
            
    	    if (e.isPopupTrigger()) {
    	    	if(nodeName.equals("UNKNOWN")) {
    	            unknownPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("EvidenceItems")) {
    	    		evidenceItemsPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("EvidenceItem")) {
    	    		evidenceItemPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Notes")) {
    	    		notesPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Note")) {
    	    		notePopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Investigation")) {
    	    		investigationPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	} 
    	    	else if (nodeName.equals("Investigations")) {
    	    		investigationsPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    }
    	    return;
    	}
    	
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}

		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
		}
    }

    class MyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
            DefaultMutableTreeNode node;
            node = (DefaultMutableTreeNode)(e.getTreePath().getLastPathComponent());

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */

                int index = e.getChildIndices()[0];
                node = (DefaultMutableTreeNode)(node.getChildAt(index));

            System.out.println("The user has finished editing the node.");
            System.out.println("New value: " + node.getUserObject());
        }
        
        public void treeNodesInserted(TreeModelEvent e) {
        	System.out.println("treeNodesInserted:");
        	System.out.println("    TreePath:" + e.getTreePath().toString());
        }
        
        public void treeNodesRemoved(TreeModelEvent e) {
        	System.out.println("treeNodesRemoved:");
        	System.out.println("    TreePath:" + e.getTreePath().toString());
        }
        
        public void treeStructureChanged(TreeModelEvent e) {
        	System.out.println("treeStructureChanged:" + e.getTreePath().toString());
        }
    }
}
