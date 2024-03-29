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

//===========================================================================
// File:    ConfigurationDialog.java
//
// Purpose: The Configuration Dialog enabled the users to update the 
//          configuration setting through a graphical interface.  
//
// Author:  Thomas Pari
//===========================================================================

package org.tbrt.plist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ConfigurationDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textPhone;
	private JTextField textEmail;
	private JTextField textHomedir;
	private JTextField textVersion;
	private JTextField textWorkspace;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
 		//-------------------------------------------------------------------
		// Initialize the configuration
		//-------------------------------------------------------------------
		Configuration.init();
		try {			
			//---------------------------------------------------------------
			// Display GUI
			//---------------------------------------------------------------
			ConfigurationDialog dialog = new ConfigurationDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ConfigurationDialog() {
		setTitle("Plist Navigator Configuration");
		setBounds(100, 100, 550, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
		    JLabel lblName= new JLabel("Investigator Name:");
		    lblName.setBounds(12, 14, 140, 16);
		    contentPanel.add(lblName);
		}
		{
			JLabel lblPhone = new JLabel("Investigator Phone:");
			lblPhone.setBounds(12, 43, 140, 16);
			contentPanel.add(lblPhone);
		}
		{
			JLabel lblEmail = new JLabel("Investigator Email:");
			lblEmail.setBounds(12, 72, 140, 16);
			contentPanel.add(lblEmail);
		}
		{
			JLabel lblHomeDir = new JLabel("Investigation Homedir:");
			lblHomeDir.setBounds(12, 101, 140, 16);
			contentPanel.add(lblHomeDir);
		}
		{
			JLabel lblHomeDir = new JLabel("Investigation Workspace:");
			lblHomeDir.setBounds(12, 130, 140, 16);
			contentPanel.add(lblHomeDir);
		}
		{
			textName = new JTextField();
			textName.setColumns(10);
			String tmp = Configuration.getConfiguration().getInvestigatorName();
			if(tmp == null) {
				tmp = "<Your Name>";
			}
			textName.setText(tmp);		
			textName.setBounds(156, 14, 274, 16);
			contentPanel.add(textName);
		}
		{
			textPhone = new JTextField();
			textPhone.setBounds(156, 43, 274, 16);
			String tmp = Configuration.getConfiguration().getInvestigatorPhone();
			if(tmp == null) {
				tmp = "###-###-####";
			}
			textPhone.setText(tmp);
			contentPanel.add(textPhone);
			textPhone.setColumns(10);
		}
		{
			textEmail = new JTextField();
			textEmail.setColumns(10);
			textEmail.setBounds(156, 72, 374, 16);
			String tmp = Configuration.getConfiguration().getInvestigatorEmail();
			if(tmp == null) {
				tmp = "<Your Email Address>";
			}
			textEmail.setText(tmp);			
			contentPanel.add(textEmail);
		}
		{
			textHomedir = new JTextField();
			textHomedir.setColumns(10);
			textHomedir.setBounds(156, 101, 374, 16);
			textHomedir.setText(Configuration.getConfiguration().getHomeDir());	
			contentPanel.add(textHomedir);
		}
		{
			textWorkspace = new JTextField();
			textWorkspace.setColumns(10);
			textWorkspace.setBounds(156, 130, 374, 16);
			String tmp = Configuration.getConfiguration().getWorkspace();
			if(tmp == null) {
				tmp = "<Your Workspace>";
			}
			textWorkspace.setText(tmp);	
			contentPanel.add(textWorkspace);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	private String validateText(boolean isInteger, String name, String str){
		String rc = "";

		if(str == null || str.equals("")) {
			if(name == null || name.equals("")){
				rc = "The specified field is a null or empty value.\n";
			}
			else {
				rc = "The " + name + " field is a null or empty value.\n";
			}
		}
		else if(isInteger) {
			int i = Integer.parseInt(str);
			if(i <= 0) {
				if(name == null || name.equals("")){
					rc = "The specified field must be between 1 and 100.\n";
				}
				else {
					rc = "The " + name + " field must be between 1 and 100.\n";
				}
			}
			else if(i > 100) {
				if(name == null || name.equals("")){
					rc = "The specified field must be between 1 and 100.\n";
				}
				else {
					rc = "The " + name + " field must be between 1 and 100.\n";
				}
			}
		}
		return rc;
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK")){

			String error = validateText(false, "Investigator Name", textName.getText())
			             + validateText(false, "Investigator Phone", textPhone.getText())
			             + validateText(false, "Investigator Email", textEmail.getText())
			             + validateText(false, "Home Directory", textHomedir.getText())
			             + validateText(false, "Workspace Directory", textWorkspace.getText());

			if(error.equals("")) {
				Configuration.getConfiguration().setInvestigatorName(textName.getText());
				Configuration.getConfiguration().setInvestigatorPhone(textPhone.getText()); 
				Configuration.getConfiguration().setInvestigatorEmail(textEmail.getText()); 
				Configuration.getConfiguration().setHomeDir(textHomedir.getText());
				Configuration.getConfiguration().setWorkspace(textWorkspace.getText());
	            setVisible(false);             
	            dispose(); 
			}	
			else {
				MessageDialog.displayMessage("FORM VALIDATION ERROR",  error);	
			}
		}
		else if (evt.getActionCommand().equals("Cancel")) {
            setVisible(false);             
            dispose(); 
		}
		return;
	} 
}
