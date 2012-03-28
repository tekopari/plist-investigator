package org.tbrt.plist;

import org.tbrt.plist.*;
import com.dd.plist.*;

import java.awt.Component;
import java.awt.Dialog.ModalityType;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.BorderLayout;

import javax.swing.JDialog;
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
 		//-------------------------------------------------------------------
		// Check the usage:  
		// - The INSTALL_PATH identifies the path the tool was installed in.
		//-------------------------------------------------------------------
		if(args.length != 1) {
			System.err.println("USAGE: java org.tbrt.plist.PlistNavigator <INSTALL_PATH>");
			System.exit(1);
		}
		
		String installPath = args[0];
		if(installPath.equals("")) {
			System.err.println("USAGE: java org.tbrt.plist.PlistNavigator <INSTALL_PATH>");
			System.exit(1);	
		}
		
		//-------------------------------------------------------------------
		// The INSTALL PATH must be a directory
		//-------------------------------------------------------------------
		try {
 			File installDir = new File(installPath); 
 			if(!(installDir.isDirectory())) {
 				System.err.println("Error: Specified INSTALL_DIR is not an existing directory.");
 				System.exit(1);		
 			}
		} 
		catch (Exception e) {
			System.err.println("Error: " + e.toString());
 			System.exit(1);		
		}
		
		//-------------------------------------------------------------------
        // Initialize the application	
		//-------------------------------------------------------------------
		Configuration.initConfiguration(installPath);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					//-------------------------------------------------------------------
			        // Start the plist navigator	
					//-------------------------------------------------------------------
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
		
		JMenu mnInvestigation = new JMenu("Investigation");
		menuBar.add(mnInvestigation);
			
		JMenuItem mntmNewProject = new JMenuItem("New Investigation");
		mnInvestigation.add(mntmNewProject);

		// Add New investigation folder
        mntmNewProject.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Component frame = null;
        		String s = (String)JOptionPane.showInputDialog(
        				frame,
        				"Type the investigation folder name:\n");
        		//If a string was returned, say so.
        		if ((s != null) && (s.length() > 0)) {
        			InvestigationNode node = new InvestigationNode("Investigation", s);
        			InvestigationNode notes = new InvestigationNode("Notes", "Notes");		
        			DefaultMutableTreeNode p  = investigationTree.addObject(null, node);
        			investigationTree.addObject(p, notes);
        		}
        	}
        });

        //Temporary Menu Item
		JMenuItem mntmTempPList = new JMenuItem("Add PList (Temporary Item)");
		mnInvestigation.add(mntmTempPList);
		
///////// SECTION BELOW SHOULD BE MOVED TO FILE InvestigationTree.java //////////
		
        // TBRT addition
        mntmTempPList.addActionListener(new ActionListener() {
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
        			DefaultMutableTreeNode t  = investigationTree.addObject(evid);
        			investigationTree.addObject(t, notes);
        			
        			//investigationTree.addObject(s);	
        			
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
        
///////// SECTION ABOVE SHOULD BE MOVED TO FILE InvestigationTree.java //////////
        
		
		JMenuItem mntmConfig = new JMenuItem("Settings");
		mntmConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ConfigurationDialog dialog = new ConfigurationDialog();
					dialog.setModalityType(ModalityType.APPLICATION_MODAL);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		mnInvestigation.add(mntmConfig);
        
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnInvestigation.add(mntmExit);
		
		// Exit the application
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Component frame = null;
				int n = JOptionPane.showConfirmDialog (
						frame,
						"Are you sure?",
						"Exit",
						JOptionPane.YES_NO_OPTION);
				if (n == 0) {
					System.exit(0);
				}
			}
		});
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		// Display version
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Component frame = null;
				JOptionPane.showMessageDialog (
						frame,
						"TBRT Plist Investigator (version 1.0)");
			}
		});
		
		frmPlistNavigator.getContentPane().setLayout(new BorderLayout(0, 0));

	
		//===================================================================
		// Create the status bar at the bottom of the GUI
		//===================================================================
		//TC JToolBar statusBar = new JToolBar();
		//TC frmPlistNavigator.getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		//TC JLabel lblMessages = new JLabel("Messages.....");
		//TC statusBar.add(lblMessages);
		
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
		//TC populateInvesTree();
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
	
	//TC private void populateTree()
	//TC	{
	//TC		InvestigationNode node = new InvestigationNode("Investigation", "MyInvestigation");		
	//TC		InvestigationNode node1 = new InvestigationNode("EvidenceItems", "Evidence");
	//TC		InvestigationNode node2 = new InvestigationNode("Notes", "Notes");
		
	//TC		InvestigationNode evid1 = new InvestigationNode("EvidenceItem", "Plist1");
	//TC		InvestigationNode note1 = new InvestigationNode("Note", "Finding#1");
		
	//TC		DefaultMutableTreeNode p  = investigationTree.addObject(null, node);
		
	//TC		DefaultMutableTreeNode p1 = investigationTree.addObject(p, node1);
	//TC		investigationTree.addObject(p1, evid1);
		
	//TC		DefaultMutableTreeNode p2 = investigationTree.addObject(p, node2);
	//TC		investigationTree.addObject(p2, note1);
		
	//TC		return;
	//TC	}
}
