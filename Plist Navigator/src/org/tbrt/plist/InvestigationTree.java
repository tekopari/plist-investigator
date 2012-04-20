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

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
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
    protected PlistSearch PlistSearch;
    protected PlistFileDialog PlistFileDialog;
    
    private String nameMyInvestigations = "My Investigations";
    private String nameNotesFile = "Notes";
    //TC private String namePlistFile = "PListFile";
    private String nameExtORG = ".org";
    
    private void init(){
        
        //-------------------------------------------------------------------
        // Read the workspace and only return a list of directory names
        //-------------------------------------------------------------------
        String workspace = Configuration.getConfiguration().getWorkspace();
        File workspaceDir = new File(workspace);
        FileFilter dirOnlyFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        
        //-------------------------------------------------------------------
        // Read the contents of each investigation and create nodes
        //-------------------------------------------------------------------
        File[] investigations = workspaceDir.listFiles(dirOnlyFilter);
        for(int i = 0; i < investigations.length; i++) {
              //---------------------------------------------------------------
              // Create the investigation node
              //---------------------------------------------------------------
              InvestigationNode node = new InvestigationNode(
                                                   "Investigation", 
                                                   investigations[i].getName());
              
              //---------------------------------------------------------------
              // ADD THE NODE TO THE ROOT NODE
              //---------------------------------------------------------------               
              DefaultMutableTreeNode p  = addObject(rootNode, node, true);
              
              //---------------------------------------------------------------
              // Open the investigation directory and read all the plists 
              // directories
              //---------------------------------------------------------------
              String investigationFile = workspace + "/" + investigations[i].getName();
              File investigation = new File(investigationFile);
              File[] investigationfiles = investigation.listFiles(dirOnlyFilter);
              for(int j = 0; j < investigationfiles.length; j++) {
                    //-----------------------------------------------------------
                    // Add each plist to the tree
                    //-----------------------------------------------------------           	  
                    String filename = investigationfiles[j].getName();
    		        InvestigationNode evid = new InvestigationNode("EvidenceItem", filename);
    			    DefaultMutableTreeNode t  = addObject(p, evid, false);
              }
        }
        return;
      }

    
    public InvestigationTree() {
        super(new GridLayout(1,0));
        rootNode = new DefaultMutableTreeNode(new InvestigationNode("Investigations", nameMyInvestigations));
        treeModel = new DefaultTreeModel(rootNode);
	    treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        //TC tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        init();
        
        // TODO - rework the popup menus and add listeners
        investigationsPopup = new JPopupMenu();
        investigationPopup = new JPopupMenu();
        evidenceItemPopup = new JPopupMenu();
        notesPopup = new JPopupMenu();
        unknownPopup = new JPopupMenu();
        
 		for (int i = 0; i < tree.getRowCount(); i++) {
	         tree.expandRow(i);
		}
	
        JMenuItem mnInvestigations = investigationsPopup.add(new JMenuItem("Add New Investigation"));
        mnInvestigations.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		createNewInvestigation();
        	}
        });
        
        JMenuItem mnInvNotes = investigationPopup.add(new JMenuItem("Show/Edit Investigation Notes"));
        mnInvNotes.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String dirFile = getDirPath() + "/" + getNodeName() + "/" + nameNotesFile;
        		
        	    MyEditor editor = new MyEditor();
        	    editor.doEdit(dirFile, dirFile);
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
        				String r = getDirPath();
        			    File f = new File(r + "/" + getNodeName());
        		    	File f2 = new File(r + "/" + s);
        			
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
					    File f = new File(getDirPath() + "/" + getNodeName());
					    
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
        		try {			
        			PlistFileDialog dia = new PlistFileDialog("C:/");
        			PlistMetaData metadata = dia.importPlist("c:/");
        			
        			//TC String eviId = metadata.getEvidenceId();
        			String eviName = metadata.getEvidenceName();
        			String eviFile = metadata.getPlistFilename();
        			
        			if ((eviName.length() > 0) && (eviFile.length() > 0)) {
        				File fromFile = new File(eviFile);
        				if (fromFile.exists()) {
        					//Valid the PList file
        					PlistTreeTable p = new PlistTreeTable(eviFile, 0);
     		                if (p.status() != 0) {
     		            	    String m = "File \"" + eviFile + "\"\nThis file neither a binary nor a XML property list.";
     		            	    JOptionPane.showMessageDialog(frame, m);
     		                }
     		                else {
     		               
        				        //Create directory
        				        String strDirectory = getDirPath() + "/" + getNodeName() + "/" + eviName;        				
        			            boolean success = (new File(strDirectory)).mkdir();
        			            if (success) {
                			        //Choose PList source file name and perform copy
        			    	        String f = fromFile.getName();      			    		
    			    		
        			    	        //Copy PList file
    			    		        String fileName = strDirectory + "/" + f;   			    		 
    			    		        File toFile = new File(fileName);
        			    	        copyFile(fromFile, toFile);
        			    	        toFile = new File(fileName + nameExtORG);
        			    	        copyFile(fromFile, toFile);
        			    	
        			    	    
        			    	        //Copy PList file to a fixed name           			    	
        			    	        //TC int pos = f.lastIndexOf('.');
        			    	        //TC String ext = f.substring(pos + 1);
        			    	        //TC fileName = strDirectory + "/" + namePlistFile;            			    
        			    	        //TC if (ext.length() > 0) {
        			    	        //TC     fileName = fileName + "." + ext;
        			    	        //TC }
        			    	        //TC
        			    	        //TC toFile = new File(fileName);
        			    	        //TC copyFile(fromFile, toFile);

    			                    //Create notes file
    			    	            File n = new File(strDirectory + "/" + nameNotesFile);
    			    	            if (!n.exists()) {
                                        n.createNewFile();
    			    	            }
    			    	
    			    	            // Add node to JTree
            		                InvestigationNode evid = new InvestigationNode("EvidenceItem", eviName);
            			            //TC InvestigationNode notes = new InvestigationNode("Notes", "PList "+nameNotesFile);
            			            DefaultMutableTreeNode t  = addObject(evid);
            			            //TC addObject(t, notes);
        			            }
        			            else {
        			    	        String m = "The same evidence name exists.\nPlease use a different name.";
        			    	        JOptionPane.showMessageDialog(frame, m);
        			            }
     		                }
        			    }
        				else {
        			        String m = "The evidence file does not exist.";
     			    	    JOptionPane.showMessageDialog(frame, m);
        				}
        			}
        			
        		} catch (Exception e) {
        			e.printStackTrace();
    		    	String m = "Add Operation Failed. \nPlease try again...";
    		    	JOptionPane.showMessageDialog(frame, m);
        		}
        	}       		                
        });    
        
        
        JMenuItem invSearch = investigationPopup.add(new JMenuItem("Search Text String"));
        invSearch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                	Component frame = null;
                		
    	            String[] nodeName = new String[2001];
    	            String[] dirPath = new String[2001];
    	      
    	            int max = travelDirPath(frame, nodeName, dirPath, 2000);
    	            int flag = 0;
    	            PlistSearch pSi = null;
    	            
    	            for (int i = 0; i < max; i++) {
    	            	String notesName = dirPath[i] + "/" + nameNotesFile;   	            	
    	            	String invName = nodeName[i];
    	            	String plistName = "";
    	            	
    	            	if (i > 0) {
   	    	                File folder = new File(dirPath[i]);
    	        		    File[] listOfFiles = folder.listFiles();
    	        		    
    	        		    for (int j = 0; j < listOfFiles.length; j++) {
    	        		        if (listOfFiles[j].isFile()) {
    	        		            String f = listOfFiles[j].getName();
    	        		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
    	        		               plistName = dirPath[i] + "/" + f;
    	        		               //TBRT: calling for all plist files under an investigation 
    	        		               PlistSearch pS = new PlistSearch (invName, notesName, plistName);
    	        		            }
    	        		        }
    	        		    }
    	            	}
    	            	else {
    	            		//TBRT: calling at the investigation line where plist file name is blank 
    	            		pSi = new PlistSearch(invName, notesName, plistName);
    	            	}
    	           }
	               if (pSi != null) {
	            	    PlistSearch.showResults();
	            		pSi.PlistSearchReset();
	               }
                }
        });
        
        JMenuItem mnInv2pdf = investigationPopup.add(new JMenuItem("Save Investigation as PDF File"));
        mnInv2pdf.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		
   			    //Choose a PDF file name
        		File fileName = doFileChooser(frame, "Choose a PDF File Name");
	            if (fileName != null) {
    	            String pdfName = fileName.getPath();
    	            
    	            if (!pdfName.endsWith(".pdf")) {
    	            	pdfName = pdfName + ".pdf";
    	            }
    	            
    	            String[] nodeName = new String[2001];
    	            String[] dirPath = new String[2001];
    	      
    	            int max = travelDirPath(frame, nodeName, dirPath, 2000);
    	            int flag = 0;   	            
    	            PdfCreate hh = null;
    	            
    	            for (int i = 0; i < max; i++) {
    	            	String notesName = dirPath[i] + "/" + nameNotesFile;   	            	
    	            	String invName = nodeName[i];
    	            	String plistName = "";
    	            	
    	            	if (i > 0) {
   	    	                File folder = new File(dirPath[i]);
    	        		    File[] listOfFiles = folder.listFiles();
    	        		    
    	        		    for (int j = 0; j < listOfFiles.length; j++) {
    	        		        if (listOfFiles[j].isFile()) {
    	        		            String f = listOfFiles[j].getName();
    	        		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
    	        		               plistName = dirPath[i] + "/" + f;
    	        		               //TBRT: calling for all plist files under an investigation 
    	        		               PdfCreate h = new PdfCreate(invName, notesName, plistName, pdfName);
    	        		               if (!h.pdfStatus()) {
    	        		            	   flag = 1;
    	        		            	   j = listOfFiles.length;
    	        		            	   i = max;
    	        		               }
    	        		            }
    	        		        }
    	        		    }
    	            	}
    	            	else {
    	            		//TBRT: calling at the investigation line where plist file name is blank 
    	            		hh = new PdfCreate(invName, notesName, plistName, pdfName);
    	            		if (! hh.pdfStatus()) {
    	            			flag = 1;
     		            	    i = max;
    	            		}
    	            	}
    	            }
    	            
	            	if (hh != null) {
	            		hh.pdfReset();
	            	}
	            	
    	            String m = "PDF file \"" + pdfName + "\" is ready.";
		            if (flag != 0) {
		                m = "Failed to generate the PDF file \"" + pdfName + "\".";
		            }
		            JOptionPane.showMessageDialog(frame, m);
        		}
        	}
        });
 
        JMenuItem mnPlistNotes = evidenceItemPopup.add(new JMenuItem("Show/Edit PList Notes"));
        mnPlistNotes.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		String dirFile = getDirPath() + "/" + getNodeName() + "/" + nameNotesFile;
        		
        	    MyEditor editor = new MyEditor();
        	    editor.doEdit(dirFile, dirFile);
        	}
        });
        
        JMenuItem mnShowPlist = evidenceItemPopup.add(new JMenuItem("Show PList"));
        mnShowPlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String nodeName = getNodeName();
	            
	            String path = getDirPath() + "/" + nodeName;

	            File folder = new File(path);
    		    File[] listOfFiles = folder.listFiles();
    		    
    		    for (int i = 0; i < listOfFiles.length; i++) {
    		        if (listOfFiles[i].isFile()) {
    		            String f = listOfFiles[i].getName();
    		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
    		               String plistName = path + "/" + f;
    		               PlistTreeTable p = new PlistTreeTable(plistName, 1);
    		               if (p.status() != 0) {
    		            	   String m = "File \"" + plistName + "\"\nThis file neither a binary nor a XML property list.";
    		            	   JOptionPane.showMessageDialog(frame, m);
    		               }
    		            }
    		        }
    		    }
        	}
        });
        
        JMenuItem mnRePlist = evidenceItemPopup.add(new JMenuItem("Rename PList"));
        mnRePlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type the new PList name for the investigation:\n");
        		if ((s != null) && (s.length() > 0)) {
        			try {
        				String r = getDirPath();
        			    File f = new File(r + "/" + getNodeName());
        		    	File f2 = new File(r + "/" + s);
        			
        			    boolean success = f.renameTo(f2);
        			    if (success) {
        	  			    renameSelectedNode(s);
        			    }
        			    else {
       			        	String m = "The same PList name exists.\nPlease use a different name.";
    			    	    JOptionPane.showMessageDialog(frame, m);
        			    }
        			} catch (Exception e) {
        				String m = "Rename Operation Failed. \nPlease try again...";
        		    	JOptionPane.showMessageDialog(frame, m);
        			}
        		}
        	}
        });
        
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
					    String dirName = getDirPath() + "/" + getNodeName();
					    File f = new File(dirName);
					    
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
        
        JMenuItem searchPlist = evidenceItemPopup.add(new JMenuItem("Search Text String"));
        searchPlist.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
    	            String nodeName = getNodeName();
    	            String path = getDirPath() + "/" + nodeName;
    	            String notesName = path + "/" + nameNotesFile;
    	            
    	            File folder = new File(path);
        		    File[] listOfFiles = folder.listFiles();
        		    
        		    for (int i = 0; i < listOfFiles.length; i++) {
        		        if (listOfFiles[i].isFile()) {
        		            String f = listOfFiles[i].getName();
        		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
        		               String plistName = path + "/" + f;
        		               
        		               //TBRT: calling at the individual plist file
        		               PlistSearch h = new PlistSearch(nodeName, notesName, plistName);
        		            }
        		        }
        		    }
        		    PlistSearch.showResults();
        		}
        });
        
        JMenuItem mnPlist2pdf = evidenceItemPopup.add(new JMenuItem("Save PList as PDF File"));
        mnPlist2pdf.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		
   			    //Choose a PDF file name
        		File target = doFileChooser(frame, "Choose a PDF File Name");
	            if (target != null) {
	            	String nodeName = getNodeName();
    	            String pdfName = target.getPath();
    	            
    	            if (!pdfName.endsWith(".pdf")) {
    	            	pdfName = pdfName + ".pdf";
    	            }
    	            
    	            String path = getDirPath() + "/" + nodeName;
    	            String notesName = path + "/" + nameNotesFile;
    	            
    	            File folder = new File(path);
        		    File[] listOfFiles = folder.listFiles();
        		    
        		    for (int i = 0; i < listOfFiles.length; i++) {
        		        if (listOfFiles[i].isFile()) {
        		            String f = listOfFiles[i].getName();
        		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
        		               String plistName = path + "/" + f;
        		               
        		               //TBRT: calling at the individual plist file
        		               PdfCreate h = new PdfCreate(nodeName, notesName, plistName, pdfName);
        		               
        		               String m = "PDF file \"" + pdfName + "\" is ready.";
        		               if (!h.pdfStatus()) {
        		            	   m = "Failed to generate the PDF file \"" + pdfName + "\".";
        		               }
        	        		   JOptionPane.showMessageDialog(frame, m);
        		            }
        		        }
        		    }
        		}
        	}
        });
        
        JMenuItem mnEdit = notesPopup.add(new JMenuItem("Edit Notes"));
        mnEdit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		//String parentNode = getNodeParentName();
        		String dirFile = getDirPath() + "/" + nameNotesFile;
        		
        	    MyEditor editor = new MyEditor();
        	    editor.doEdit(dirFile, dirFile);
        	}
        });
        
        unknownPopup.add(new JMenuItem("unknown option"));
        
        tree.addMouseListener(new MyTreeMouseListener());

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }

    //=======================================================================
    // Handle File Chooser
    //=======================================================================
    public void createNewInvestigation() {
		Component frame = null;
		String s = (String)JOptionPane.showInputDialog(
				frame,
				"Type the investigation name:\n");
		if ((s != null) && (s.length() > 0)) {
			//Create the investigation directory
			try {
				//Create directory				
				String rootPath = Configuration.getConfiguration().getWorkspace();
				String strDirectory = rootPath + "/" + s;
				
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
	        		//TC InvestigationNode notes = new InvestigationNode("Notes", "Investigation "+nameNotesFile);		
	        		DefaultMutableTreeNode p  = addObject(null, node);
	        	    //TC addObject(p, notes);
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
    
    //=======================================================================
    // Handle File Chooser
    //=======================================================================
    public File doFileChooser(Component frame, String title) {
        JFileChooser chooser = new JFileChooser() {
            protected JDialog createDialog( Component parent ) throws HeadlessException {
                JDialog dialog = super.createDialog( parent );            	                   
                ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
    		    dialog.setIconImage(img.getImage());
                return dialog;
            }
        };
        
        chooser.setDialogTitle(title);
        
        int c = chooser.showOpenDialog(frame);
        if (c == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            return f;
        }
        return null;
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
    // Return directory path to current node
    //=======================================================================
    public String getDirPath() {
    	String rootPath = Configuration.getConfiguration().getWorkspace();
    	String dirPath = "";
    	
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            while (currentNode != null) {
                DefaultMutableTreeNode sibling = currentNode.getPreviousSibling();
                while (sibling != null) {
            	    currentNode = sibling;
            	    sibling = currentNode.getPreviousSibling();
                }
                
                DefaultMutableTreeNode parent = currentNode.getPreviousNode();
                
                if (parent != null) {
                    InvestigationNode iNode = (InvestigationNode)(parent.getUserObject());
                
                    String p = iNode.getNodeValue();
                    if (p != nameMyInvestigations) {
                        if (dirPath.length() == 0) {
                		    dirPath = p;
                	    }
                	    else {
                            dirPath = p + "/" + dirPath;
                	    }
                    }
                }

                currentNode = parent;
        	}
        }
        
        if (dirPath.length() > 0) {
            dirPath = rootPath + "/" + dirPath;
        }
        else {
        	dirPath = rootPath;
        }

    	return(dirPath);
    }

    //=======================================================================
    // Return set of directory paths under a current node
    //=======================================================================
    public int travelDirPath(Component frame, String[] n, String[] p, int max) {
    	String rootPath = Configuration.getConfiguration().getWorkspace();;
    	int i = 0;
    	
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            InvestigationNode iNode = (InvestigationNode)(currentNode.getUserObject());
            String invName = iNode.getNodeValue();

            rootPath = rootPath + "/" + invName;
            n[i] = invName;
            p[i++] = rootPath;
            
            DefaultMutableTreeNode sibling = currentNode.getNextNode();
            while (sibling != null) {
                InvestigationNode iNode2 = (InvestigationNode)(sibling.getUserObject());
                	
                String s = iNode2.getNodeValue();
                if (!s.contains(nameNotesFile)) {  //skip the first child which is the Notes file
                	n[i] = s;
                    p[i++] = rootPath + "/" + s;
                }	
                sibling = sibling.getNextSibling();
                
                if (i >= max) {
                	int c = max - 1;
    				String m = "Sorry, not able to handle more than" + c + "PList files.";
    		    	JOptionPane.showMessageDialog(frame, m);
                }
            }
        }
        return(i);
    }
    
    //=======================================================================
    // Return Node Parent Name
    //======================================================================= 
    public String getNodeParentName() {
        TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                         (currentSelection.getLastPathComponent());
            DefaultMutableTreeNode sibling = currentNode.getPreviousSibling();
            while (sibling != null) {
            	currentNode = sibling;
            	sibling = currentNode.getPreviousSibling();
            }

            DefaultMutableTreeNode parent = currentNode.getPreviousNode();
            InvestigationNode iNode = (InvestigationNode)(parent.getUserObject());
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

            String nodeName = "";
            if(e.getClickCount() < 2) {
            	return;
            }
            	
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

 	    	if (nodeName.equals("EvidenceItem")) {
         		String nodename = getNodeName();
 	            
 	            String path = getDirPath() + "/" + nodename;

 	            File folder = new File(path);
     		    File[] listOfFiles = folder.listFiles();
     		    
     		    for (int i = 0; i < listOfFiles.length; i++) {
     		        if (listOfFiles[i].isFile()) {
     		            String f = listOfFiles[i].getName();
     		            if (!f.endsWith(nameExtORG) && !f.contains(nameNotesFile)) {
     		               String plistName = path + "/" + f;
     		               PlistTreeTable p = new PlistTreeTable(plistName, 1);
     		            }
     		        }
     		    }
    	    }
 	    	
    	    return;
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

            //System.out.println("The user has finished editing the node.");
            //System.out.println("New value: " + node.getUserObject());
        }
        
        public void treeNodesInserted(TreeModelEvent e) {
        	//System.out.println("treeNodesInserted:");
        	//System.out.println("    TreePath:" + e.getTreePath().toString());
        }
        
        public void treeNodesRemoved(TreeModelEvent e) {
        	//System.out.println("treeNodesRemoved:");
        	//System.out.println("    TreePath:" + e.getTreePath().toString());
        }
        
        public void treeStructureChanged(TreeModelEvent e) {
        	//System.out.println("treeStructureChanged:" + e.getTreePath().toString());
        }
    }
}
