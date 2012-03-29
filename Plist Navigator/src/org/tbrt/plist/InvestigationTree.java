package org.tbrt.plist;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
        rootNode = new DefaultMutableTreeNode(new InvestigationNode("Investigations", "My Investigations"));
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
        
        JMenuItem mnInvestigations = investigationsPopup.add(new JMenuItem("Add New Investigation"));
        mnInvestigations.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type the investigation folder name:\n");
        		if ((s != null) && (s.length() > 0)) {
        			InvestigationNode node = new InvestigationNode("Investigation", s);
        			InvestigationNode notes = new InvestigationNode("Notes", "Notes");		
        			DefaultMutableTreeNode p  = addObject(node);
        			addObject(p, notes);
        		}
        	}
        });
        
        JMenuItem mnRename = investigationPopup.add(new JMenuItem("Rename Investigation"));
        mnRename.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type the new investigation folder name:\n");
        		if ((s != null) && (s.length() > 0)) {
        			//TODO
        			System.out.print(s+"\n");
        		}
        	}
        });
        
        JMenuItem mnDelFolder = investigationPopup.add(new JMenuItem("Delete Investigation"));
        mnDelFolder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
				Component frame = null;
				int n = JOptionPane.showConfirmDialog (
						frame,
						"Are you sure?",
						"Delete Investigation Folder & All Data",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					removeSelectedNode();
				}
        	}
        });
        
        JMenuItem mnPlist = investigationPopup.add(new JMenuItem("Add PList"));        
        // TBRT addition
        mnPlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		// TODO
        		// Create a dialog box to get the name of the jtree node
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type plist file path:\n");
        		//If a string was returned, say so.
        		if ((s != null) && (s.length() > 0)) {
        			setLabel("Typed String... " + s + "!");
        			
        			InvestigationNode evid = new InvestigationNode("EvidenceItems", s);
        			InvestigationNode notes = new InvestigationNode("Notes", "Notes");
        			DefaultMutableTreeNode t  = addObject(evid);
        			addObject(t, notes);       			
        			
        			PlistTreeTable p = new PlistTreeTable(s);
        			
        			//bew, initial code to create directory per plist file.
        			// first thing is to remove the .plist extension.
        			// TODO: need to figure out how to remove the C:\dirname, so that we can
        			//   just use the filename, and then add our own path name.  I.e. We will 
        			//   not usually be creating the directoy in same location as where plist
        			//   file may be.
        			// TODO: Add additional error check, ie. what if dir exists, or invalid name.
        			// TODO: Use the final code for getting filename, to also use when displaying
        			//       on the GUI.
        			s = s.substring(0, s.lastIndexOf('.'));
        			//File f = new File("C:" + File.separator + "temp" + File.separator + s);
        			File f = new File(s);
        			try{
        				f.mkdir();
        				} catch (Exception e) {
        					e.printStackTrace(); //TODO: Add code to handle displaying correct error codes
        					}
        			//bew, end add code
        			}  else {
        				
        				//If you're here, the return value was null/empty.
        				setLabel("Come on, finish the sentence!");
        				}
        		
                                        
        	}

        	private void setLabel(String string) {
        		// TODO Auto-generated method stub                                       
        	}
        });       
        
        investigationPopup.add(new JMenuItem("Search Text String"));
        investigationPopup.add(new JMenuItem("Save Investigatoin as PDF File"));
        
        evidenceItemsPopup.add(new JMenuItem("Delete PList")); 
        evidenceItemsPopup.add(new JMenuItem("Search Text String"));
        evidenceItemsPopup.add(new JMenuItem("Save PList as PDF File"));
        
        evidenceItemPopup.add(new JMenuItem("Delete PList"));
        evidenceItemPopup.add(new JMenuItem("Search Text String"));
        evidenceItemPopup.add(new JMenuItem("Save PList as PDF file"));
        
        notesPopup.add(new JMenuItem("Edit Notes"));
        
        notePopup.add(new JMenuItem("Edit Notes"));
        
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
