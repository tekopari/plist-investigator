package org.tbrt.plist;

import org.tbrt.plist.*;
import com.dd.plist.*;

import java.awt.Component;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;

public class PlistNavigator {

	private JFrame frmPlistNavigator;
	private InvestigationTree investigationTree;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PlistNavigator window = new PlistNavigator();
					window.frmPlistNavigator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PlistNavigator() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPlistNavigator = new JFrame();
		frmPlistNavigator.setTitle("Plist Investigator");
		frmPlistNavigator.setBounds(50, 50, 600, 500);
		frmPlistNavigator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmPlistNavigator.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		
		JMenu mnSearch = new JMenu("Edit");
		menuBar.add(mnSearch);
		
		JMenuItem mntmSearchEvidence = new JMenuItem("Search File");
		mnSearch.add(mntmSearchEvidence);
		
		JMenuItem mntmSearchAll = new JMenuItem("Search All");
		mnSearch.add(mntmSearchAll);
		
		JMenu mnInvestigation = new JMenu("Investigation");
		menuBar.add(mnInvestigation);
			
		JMenuItem mntmNewProject = new JMenuItem("New");
		mnInvestigation.add(mntmNewProject);

        // TBR addition
        mntmNewProject.addActionListener(new ActionListener() {
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
                                            investigationTree.addObject(s);
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
                            					e.printStackTrace(); //TODO: Add code to handle displaying correct error codes.
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

        
		JMenuItem mntmOpen = new JMenuItem("Open");
		mnInvestigation.add(mntmOpen);		
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnInvestigation.add(mntmClose);
		
		JMenuItem mntmDelete = new JMenuItem("Delete");
		mnInvestigation.add(mntmDelete);
		
		JMenuItem mntmProperties = new JMenuItem("Properties");
		mnInvestigation.add(mntmProperties);		
		
		JMenu mnEdit = new JMenu("Evidence");
		menuBar.add(mnEdit);
		
		JMenuItem mntmAddEvidence = new JMenuItem("Add Evidence");
		mnEdit.add(mntmAddEvidence);
		
		JMenuItem mntmAddAllEvidence = new JMenuItem("Add All Evidence");
		mnEdit.add(mntmAddAllEvidence);
		
		JMenuItem mntmRemoveEvidence = new JMenuItem("Remove Evidence");
		mnEdit.add(mntmRemoveEvidence);
		
		JMenuItem mntmRemoveAllEvidence = new JMenuItem("Remove All Evidence");
		mnEdit.add(mntmRemoveAllEvidence);
		
		JMenuItem mntmVerifyEvidence = new JMenuItem("Verify Evidence");
		mnEdit.add(mntmVerifyEvidence);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		frmPlistNavigator.getContentPane().setLayout(new BorderLayout(0, 0));
		
		//===================================================================
		// Create the toolbar
		//===================================================================
		JToolBar toolBar = new JToolBar();
		frmPlistNavigator.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		//-------------------------------------------------------------------
		// Create the "Add" toolbar button
		//-------------------------------------------------------------------
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				investigationTree.addObject("NEW NODE");
			}
		});
		btnAdd.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toolBar.add(btnAdd);
		
		//-------------------------------------------------------------------
		// Create the "Remove" toolbar button
		//-------------------------------------------------------------------
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				investigationTree.removeSelectedNode();
			}
		});
		btnRemove.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toolBar.add(btnRemove);
		
		//-------------------------------------------------------------------
		// Create the "Clear" toolbar button
		//-------------------------------------------------------------------
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO
				investigationTree.removeAllNodes();
			}
		});
		btnClear.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		toolBar.add(btnClear);
		
		//-------------------------------------------------------------------
		// TODO - Add more buttons
		//-------------------------------------------------------------------
		
		//===================================================================
		// Create the status bar at the bottom of the GUI
		//===================================================================
		JToolBar statusBar = new JToolBar();
		frmPlistNavigator.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		JLabel lblMessages = new JLabel("Messages.....");
		statusBar.add(lblMessages);
		
		JPanel panel = new JPanel();
		frmPlistNavigator.getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		//===================================================================
		// Create tree
		//===================================================================
		investigationTree = new InvestigationTree();
		sl_panel.putConstraint(SpringLayout.NORTH, investigationTree, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, investigationTree, 10, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.SOUTH, investigationTree, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, investigationTree, -443, SpringLayout.EAST, panel);
		populateTree();
		panel.add(investigationTree);		
		
		//===================================================================
		// Tab panels
		//===================================================================
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		sl_panel.putConstraint(SpringLayout.NORTH, tabbedPane, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, tabbedPane, 6, SpringLayout.EAST, investigationTree);
		sl_panel.putConstraint(SpringLayout.SOUTH, tabbedPane, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, panel);
		panel.add(tabbedPane);
		
	}
	
	private void populateTree()
	{
		InvestigationNode node = new InvestigationNode("Investigation", "MyInvestigation");
		
		InvestigationNode node1 = new InvestigationNode("EvidenceItems", "Evidence");
		InvestigationNode node2 = new InvestigationNode("Notes", "Notes");
		
		InvestigationNode evid1 = new InvestigationNode("EvidenceItem", "Plist1");
		InvestigationNode note1 = new InvestigationNode("Note", "Finding#1");
		
		DefaultMutableTreeNode p  = investigationTree.addObject(null, node);
		
		DefaultMutableTreeNode p1 = investigationTree.addObject(p, node1);
		investigationTree.addObject(p1, evid1);
		
		DefaultMutableTreeNode p2 = investigationTree.addObject(p, node2);
		investigationTree.addObject(p2, note1);
		
		return;
	}
	
}
