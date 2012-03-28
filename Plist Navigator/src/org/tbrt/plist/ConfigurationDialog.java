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

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		try {
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
		setBounds(100, 100, 450, 300);
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
			textName = new JTextField();
			textName.setColumns(10);
			textName.setText(Configuration.getConfiguration().getInvestigatorName());		
			textName.setBounds(156, 14, 274, 16);
			contentPanel.add(textName);
		}
		{
			textPhone = new JTextField();
			textPhone.setBounds(156, 43, 274, 16);
			textPhone.setText(Configuration.getConfiguration().getInvestigatorPhone());
			contentPanel.add(textPhone);
			textPhone.setColumns(10);
		}
		{
			textEmail = new JTextField();
			textEmail.setColumns(10);
			textEmail.setBounds(156, 72, 274, 16);
			textEmail.setText(Configuration.getConfiguration().getInvestigatorEmail());			
			contentPanel.add(textEmail);
		}
		{
			textHomedir = new JTextField();
			textHomedir.setColumns(10);
			textHomedir.setBounds(156, 101, 274, 16);
			textHomedir.setText(Configuration.getConfiguration().getHomeDir());	
			contentPanel.add(textHomedir);
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
			             + validateText(false, "Home Directory", textHomedir.getText());

			if(error.equals("")) {
				Configuration.getConfiguration().setInvestigatorName(textName.getText());
				Configuration.getConfiguration().setInvestigatorPhone(textPhone.getText()); 
				Configuration.getConfiguration().setInvestigatorEmail(textEmail.getText()); 
				Configuration.getConfiguration().setHomeDir(textHomedir.getText());
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
