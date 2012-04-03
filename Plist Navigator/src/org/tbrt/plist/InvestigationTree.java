package org.tbrt.plist;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
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

import org.tbrt.plist.MyEditor.SimpleEditor;

public class InvestigationTree extends JPanel {
    protected DefaultMutableTreeNode rootNode;
    protected DefaultTreeModel treeModel;
    protected JTree tree;
    
    protected JPopupMenu investigationsPopup;
    protected JPopupMenu investigationPopup;
    protected JPopupMenu evidenceItemPopup;
    protected JPopupMenu notesPopup;
    protected JPopupMenu unknownPopup;

    protected MyEditor MyEditor;
    protected PdfCreate PdfCreate;
    
    private String nameNotesFile = "Notes";
    
    public InvestigationTree() {
        super(new GridLayout(1,0));
        rootNode = new DefaultMutableTreeNode(new InvestigationNode("Investigations", "My Investigations"));
        treeModel = new DefaultTreeModel(rootNode);
	    treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        //TC tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        
        // TODO - rework the popup menus and add listeners
        investigationsPopup = new JPopupMenu();
        investigationPopup = new JPopupMenu();
        evidenceItemPopup = new JPopupMenu();
        notesPopup = new JPopupMenu();
        unknownPopup = new JPopupMenu();
        
        JMenuItem mnInvestigations = investigationsPopup.add(new JMenuItem("Add New Investigation"));
        mnInvestigations.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type the investigation name:\n");
        		if ((s != null) && (s.length() > 0)) {
        			//Create the investigation directory
        			try {
        				//Create directory
        				String rootDir = Configuration.getConfiguration().getWorkspace();
        				String strDirectory = rootDir + "/" + s;
        				
        			    boolean success = (new File(strDirectory)).mkdir();
        			    if (success) {
        			    	//Create notes file
        			    	String strFile = strDirectory + "/" + nameNotesFile;
        			    	File f = new File(strFile);
        			    	if (!f.exists()) {
                                f.createNewFile();
        			    	}
        			    	
        			    	// Add node to JTree
        	        	    InvestigationNode node = new InvestigationNode("Investigation", s);
        	        		InvestigationNode notes = new InvestigationNode("Notes", nameNotesFile);		
        	        		DefaultMutableTreeNode p  = addObject(node);
        	        	    addObject(p, notes);
       		            }  
        			    else {
        			    	String m = "The same investigation name exists.\nPlease use a different name.";
        			    	JOptionPane.showMessageDialog(frame, m);
        			    }
        		    } catch (Exception e) {
        		    	String m = "Add Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
        		    }
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
        			try {
        			    String rootDir = Configuration.getConfiguration().getWorkspace();       			
        			    File f = new File(rootDir + "/" + getNodeName());
        		    	File f2 = new File(rootDir + "/" + s);
        			
        			    boolean success = f.renameTo(f2);
        			    if (success) {
        	  			    renameSelectedNode(s);
        			    }
        			    else {
       			        	String m = "The same investigation name exists.\nPlease use a different name.";
    			    	    JOptionPane.showMessageDialog(frame, m);
        			    }
        			} catch (Exception e) {
        				String m = "Rename Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
        			}
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
						"Delete Investigation with All Data",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					try {
					    String rootDir = Configuration.getConfiguration().getWorkspace();					
					    File f = new File(rootDir + "/" + getNodeName());
					    
					    boolean success = deleteDir(f);
					    if (success) {
					    	removeSelectedNode();
					    }
					    else {
	        				String m = "Operation Failed. \nPlease try again...";
	        		    	JOptionPane.showMessageDialog(frame, m);
					    }
					} catch (Exception  e) {
        				String m = "Delete Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
					}
				}
        	}
        });
        
        JMenuItem mnPlist = investigationPopup.add(new JMenuItem("Add PList"));
        mnPlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type a PList name for the investigation:\n");
        		if ((s != null) && (s.length() > 0)) {
        			//Create the PList directory
        			try {
        				//Create directory
        				String rootDir = Configuration.getConfiguration().getWorkspace();
        				String plistName = "PList";
        				String strDirectory = rootDir + "/" + getNodeName() + "/" + plistName;
        				
        			    boolean success = (new File(strDirectory)).mkdir();
        			    if (success) {
     			    	    //Choose PList target file name
     			    	    String plistFile = "plistfile.plist";
     			    	    File toFile = new File(strDirectory + "/" + plistFile); 
     			    	   
                			//Choose PList source file name and perform copy
            	            JFileChooser chooser = new JFileChooser();
            	            chooser.setDialogTitle("Choose a PList File to Import");
            	    		
            	            int c = chooser.showOpenDialog(frame);
            	            if (c == JFileChooser.APPROVE_OPTION){
                	            File fromFile = chooser.getSelectedFile();

            			    	//Copy PList file
            			    	copyFile(fromFile, toFile);

        			            //Create notes file
        			    	    File f = new File(strDirectory + "/" + nameNotesFile);
        			    	    if (!f.exists()) {
                                    f.createNewFile();
        			    	    }
        			    	
        			    	    // Add node to JTree
                		        InvestigationNode evid = new InvestigationNode("EvidenceItem", plistName);
                			    InvestigationNode notes = new InvestigationNode("Notes", nameNotesFile);
                			    DefaultMutableTreeNode t  = addObject(evid);
                			    addObject(t, notes);
                			    
                			    PlistTreeTable p = new PlistTreeTable(s);   
            	            }
            	            else {
            			    	String m = "Operation Failed. \nPlease try again...";
            			    	JOptionPane.showMessageDialog(frame, m);
            	            }
       		            }  
        			    else {
        			    	String m = "The same plist name exists.\nPlease use a different name.";
        			    	JOptionPane.showMessageDialog(frame, m);
        			    }
        		    } catch (Exception e) {
        		    	String m = "Add Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
        		    }
        		}                 
        	}
        });       
        
        investigationPopup.add(new JMenuItem("Search Text String"));
        investigationPopup.add(new JMenuItem("Save Investigatoin as PDF File"));
        
        JMenuItem mnDelPlist = evidenceItemPopup.add(new JMenuItem("Delete PList"));
        mnDelPlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
				Component frame = null;
				int n = JOptionPane.showConfirmDialog (
						frame,
						"Are you sure?",
						"Delete PList and Notes",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					try {
					    String rootDir = Configuration.getConfiguration().getWorkspace();
					    File f = new File(rootDir + "/" + getNodeName());
					    
					    boolean success = deleteDir(f);
					    if (success) {
					    	removeSelectedNode();
					    }
					    else {
	        				String m = "Operation Failed. \nPlease try again...";
	        		    	JOptionPane.showMessageDialog(frame, m);
					    }
					} catch (Exception  e) {
        				String m = "Delete Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
					}
				}
        	}
        });
        
        evidenceItemPopup.add(new JMenuItem("Search Text String"));
        
        JMenuItem mnPlist2pdf = evidenceItemPopup.add(new JMenuItem("Save PList as PDF File"));
        mnPlist2pdf.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String rootDir = Configuration.getConfiguration().getWorkspace();
        		String plistFile = "plistfile.plist";
        		//String f = rootDir + "/" + getNodeParentName() + "/" + plistFile;
        		String f = "C:/tmp/plistfile.plist";
        		
        		System.out.println("TC:InvestigationTree:"+f);
        	    PdfCreate pdfH = new PdfCreate(f);
        	}
        });
        
        JMenuItem mnEdit = notesPopup.add(new JMenuItem("Edit Notes"));
        mnEdit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String rootDir = Configuration.getConfiguration().getWorkspace();
        		String parentNode = getNodeParentName();
        		
        		System.out.println("TC:notes_edit:"+parentNode);
        		
        	    MyEditor editor = new MyEditor();
        	    editor.doEdit(
        	    		"Notes for " + parentNode, 
        	    		rootDir + "/" + parentNode + "/" + nameNotesFile);
        	}
        });
        
        unknownPopup.add(new JMenuItem("unknown option"));
        
        tree.addMouseListener(new MyTreeMouseListener());

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    //=======================================================================
    // Delete directory including all files and sub-directories
    //=======================================================================
    public void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    //=======================================================================
    // Delete directory including all files and sub-directories
    //=======================================================================
    public static boolean deleteDir(File dir) {
    	if (dir.isDirectory()) {
    		String[] children = dir.list();
    		for (int i = 0; i < children.length; i++) {
    			boolean success = deleteDir(new File(dir, children[i]));
    			if (!success) {
    				return(false);
    			}
    		}
    	}
    	// The directory is now empty to be deleted
    	boolean s = dir.delete();
    	return(s);
    }
    
    //=======================================================================
    // Return Node Parent Name
    //======================================================================= 
    public String getNodeParentName() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            String s = "";
            while (s != nameNotesFile) {
                DefaultMutableTreeNode parent = currentNode.getPreviousNode();
                InvestigationNode iNode = (InvestigationNode)(currentNode.getUserObject());
                s = iNode.getNodeValue();
                currentNode = parent;
        	}
            InvestigationNode iNode = (InvestigationNode)(currentNode.getUserObject());
            String p = iNode.getNodeValue();
            return(p);
        }
        return("");
    }

  //=======================================================================
    // Return Node Name
    //======================================================================= 
    public String getNodeName() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            InvestigationNode iNode = (InvestigationNode)(currentNode.getUserObject());
            String s = iNode.getNodeValue();
            return(s);
        }
        return("");
    }
    
    //=======================================================================
    // Rename Selected Node
    //======================================================================= 
    public void renameSelectedNode(String s) {
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
               nodeType.equals("EvidenceItem") ||
               nodeType.equals("Notes")) {           
            	return;
            }
            
            //---------------------------------------------------------------
            // Do not allow the root node to be renamed
            //---------------------------------------------------------------
            MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
            if (parent != null) {
            	iNode.setNodeValue(s);
                // TODO handle the rename of the files
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
    	    	if (nodeName.equals("EvidenceItem")) {
    	    		evidenceItemPopup.show((Component) e.getSource(), e.getX(), e.getY());
    	    	}
    	    	else if (nodeName.equals("Notes")) {
    	    		notesPopup.show((Component) e.getSource(), e.getX(), e.getY());     	    		
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
    	    	else if (nodeName.equals("EvidenceItem")) {
    	    		evidenceItemPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
    	    	}
    	    	else if (nodeName.equals("Notes")) {
    	    		notesPopup.show((Component) e.getSource(), e.getX(), e.getY()); 
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
