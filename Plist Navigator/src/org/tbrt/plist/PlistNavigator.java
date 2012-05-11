/*
* TBRT - An open source application for plist display
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
	//private TabbedPane tabbedPane;
	private DisplayImagePanel imagePanel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
 		//-------------------------------------------------------------------
		// Initialize the configuration
		//-------------------------------------------------------------------
		Configuration.init();

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
        		investigationTree.createNewInvestigation();
        	}
        });

		JMenuItem mntmConfig = new JMenuItem("Settings");
		mntmConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ConfigurationDialog dialog = new ConfigurationDialog();
					
					dialog.setModalityType(ModalityType.APPLICATION_MODAL);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					
					ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
					dialog.setIconImage(img.getImage());
					
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
		sl_panel.putConstraint(SpringLayout.EAST, investigationTree, -343, SpringLayout.EAST, panel);
		panel.add(investigationTree);		
	
		//===================================================================
		// Tab panels
		//===================================================================
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPane = new TabbedPane();
//		sl_panel.putConstraint(SpringLayout.NORTH, tabbedPane, 10, SpringLayout.NORTH, panel);
//		sl_panel.putConstraint(SpringLayout.WEST, tabbedPane, 6, SpringLayout.EAST, investigationTree);
//		sl_panel.putConstraint(SpringLayout.SOUTH, tabbedPane, -10, SpringLayout.SOUTH, panel);
//		sl_panel.putConstraint(SpringLayout.EAST, tabbedPane, -10, SpringLayout.EAST, panel);
//		panel.add(tabbedPane);
		
		//===================================================================
		// Image Panel
		//===================================================================
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		//BEW:  Replace above with below.  Just adding some color for now.  We need to decide if this panel
		//   will be used for just displaying, or also for displaying what is clicked-on in the left panel?
		
		imagePanel = new DisplayImagePanel();
		sl_panel.putConstraint(SpringLayout.NORTH, imagePanel, 10, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, imagePanel, 6, SpringLayout.EAST, investigationTree);
		sl_panel.putConstraint(SpringLayout.SOUTH, imagePanel, -10, SpringLayout.SOUTH, panel);
		sl_panel.putConstraint(SpringLayout.EAST, imagePanel, -10, SpringLayout.EAST, panel);
		panel.add(imagePanel);
	}
}
