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

import javax.swing.ImageIcon;
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

	public static String getInstallPath(Class myclass) {
        String rc = "";
        String path = myclass.getClassLoader().getResource(
            myclass.getName().replace('.', '/') + ".class").toString();

        if(path == null) {
        	rc = "";
        }
        else if (path.endsWith(".jar")) {
        	// Running from jar file
        	rc = path.substring(6, path.lastIndexOf("/"));
        }
        else if (path.endsWith(".class")) {
        	// Running from eclipse
        	for(int count = 0; count < 5; count++) {
            	path = path.substring(0, path.lastIndexOf("/"));
        	}
            rc = path.substring(6);
        }
        else {
        	rc = "/";
        }
        rc = rc.replaceAll("%20", " ");
        return rc;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		String pwd = System.getProperties().getProperty("user.home");
		System.out.println("TC:"+pwd);
		
 		//-------------------------------------------------------------------
		// Set the install path
		//-------------------------------------------------------------------
		String installPath = getInstallPath(PlistNavigator.class);		
		Configuration.setInstallPath(installPath);
		
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
		ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
		frmPlistNavigator = new JFrame();
		frmPlistNavigator.setIconImage(img.getImage());
		frmPlistNavigator.setTitle("TBRT Plist Investigator");
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
        				"Type the investigation name:\n");
        		if ((s != null) && (s.length() > 0)) {
        			InvestigationNode node = new InvestigationNode("Investigation", s);
        			InvestigationNode notes = new InvestigationNode("Notes", "Notes");		
        			DefaultMutableTreeNode p  = investigationTree.addObject(null, node);
        			investigationTree.addObject(p, notes);
        		}
        	}
        });

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
}
