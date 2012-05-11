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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class PlistFileDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	//TC private JTextField textId;
	private JTextField textName;
	private JTextField textFilename;
	private PlistMetaData metadata;
	
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		
		try {			
			PlistMetaData metadata = importPlist("c:\\");
			System.out.println("Evidence Id=" + metadata.getEvidenceId());
			System.out.println("Evidence Name=" + metadata.getEvidenceName());
			System.out.println("Plist Filename=" + metadata.getPlistFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static PlistMetaData importPlist(String dirname) {
    	PlistMetaData metadata = new PlistMetaData();
		try {			
			//---------------------------------------------------------------
			// Display GUI
			//---------------------------------------------------------------
			PlistFileDialog dialog = new PlistFileDialog(dirname);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setVisible(true);
			metadata = dialog.getMetadata();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return metadata;
	}



	public PlistFileDialog(String dirname) {
		metadata = new PlistMetaData();
		metadata.setInvestigationDir(dirname);
		setTitle("Plist File Importer");
		setBounds(100, 100, 550, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
		setIconImage(img.getImage());
		
		contentPanel.setLayout(null);
		{
		    //TC JLabel lblID= new JLabel("Evidence Id:");
			//TC lblID.setBounds(12, 14, 140, 16);
			//TC contentPanel.add(lblID);
		}
		{
			JLabel lblName = new JLabel("Evidence Name:");
			lblName.setBounds(12, 43, 140, 16);
			contentPanel.add(lblName);
		}
		{
			JLabel lblEmail = new JLabel("Plist Filename:");
			lblEmail.setBounds(12, 72, 140, 16);
			contentPanel.add(lblEmail);
		}
		{
			//TC textId = new JTextField();
			//TC textId.setColumns(10);
			//TC textId.setText("");		
			//TC textId.setBounds(106, 14, 274, 16);
			//TC contentPanel.add(textId);
		}		
		{
			textName = new JTextField();
			textName.setColumns(10);
			textName.setText("");		
			textName.setBounds(106, 43, 274, 16);
			contentPanel.add(textName);
		}
		{
			textFilename = new JTextField();
			textFilename.setColumns(10);
			textFilename.setText("");
			textFilename.setBounds(106, 72, 340, 16);
			contentPanel.add(textFilename);
		}
		{
			JButton browseButton = new JButton("Browse");
			browseButton.setActionCommand("BROWSE");
			browseButton.addActionListener(this);
			browseButton.setBounds(450, 72, 80, 16);
			contentPanel.add(browseButton);
			getRootPane().setDefaultButton(browseButton);
			
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
				cancelButton.setActionCommand("CANCEL");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	private String validateText(String name, String str){
		String rc = "";		
		
		if(str == null || str.equals("")) {
			if(name == null || name.equals("")){
				rc = "The specified field is a null or empty value.\n";
			}
			else {
				rc = "Populate " + name + ".\n";
			}
		}
		return rc;
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
        };;
        chooser.setDialogTitle(title);
        
        int c = chooser.showOpenDialog(frame);
        if (c == JFileChooser.APPROVE_OPTION){
            File f = chooser.getSelectedFile();
            return f;
        }
        return null;
    }
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK")){

			String error1 = validateText("Plist Filename", textName.getText());
			String error2 = validateText("Plist Filename", textFilename.getText());
			
			if ((error1.equals("") && error2.equals(""))) {
	            metadata.setEvidenceName(textName.getText());
	          //TC metadata.setEvidenceId(textId.getText());	
				metadata.setPlistFilename(textFilename.getText());
	            setVisible(false);             
	            dispose(); 
			}	
			else {
				//TC MessageDialog.displayMessage("FORM VALIDATION ERROR",  error);	
			}
		}
		else if (evt.getActionCommand().equals("BROWSE")) {
		    metadata.setFile(doFileChooser(contentPanel, "Choose a PList File to Import"));
		    
		    File file = metadata.getFile();
		    if(file != null) {
		    	textFilename.setText(PlistMetaData.removeBackSlashes(file.getAbsolutePath()));
		    	
		        if(textName.getText().equals("")) {
		    	    textName.setText(file.getName());
		        }
		    }
		}
		else if (evt.getActionCommand().equals("CANCEL")) {
            metadata.setEvidenceName("");
			metadata.setEvidenceId("");	
			metadata.setPlistFilename("");
            setVisible(false);             
            dispose(); 
		}
		return;
	}
	
	public PlistMetaData getMetadata() {
	    return metadata;
	}
}

