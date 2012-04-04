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
	private JTextField textId;
	private JTextField textName;
	private JTextField textFilename;
	private PlistMetaData metadata;
	private String investigationDirectory;
	
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
		investigationDirectory = dirname;
		metadata = new PlistMetaData();
		setTitle("Plist File Importer");
		setBounds(100, 100, 550, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
		    JLabel lblID= new JLabel("Evidence Id:");
		    lblID.setBounds(12, 14, 140, 16);
		    contentPanel.add(lblID);
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
			textId = new JTextField();
			textId.setColumns(10);
			textId.setText("");		
			textId.setBounds(106, 14, 274, 16);
			contentPanel.add(textId);
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

			String error = validateText("Plist Filename", textFilename.getText());

			if(error.equals("")) {
	            metadata.setEvidenceName(textName.getText());
				metadata.setEvidenceId(textId.getText());	
				metadata.setPlistFilename(textFilename.getText());
	            setVisible(false);             
	            dispose(); 
			}	
			else {
				MessageDialog.displayMessage("FORM VALIDATION ERROR",  error);	
			}
		}
		else if (evt.getActionCommand().equals("BROWSE")) {
		    metadata.setFile(doFileChooser(contentPanel, "Choose a PList File to Import"));
		    textName.setText(metadata.getFile().getName());
		    textFilename.setText(metadata.getFile().getAbsolutePath());
		}
		else if (evt.getActionCommand().equals("CANCEL")) {
            metadata.setEvidenceName("CANCEL");
			metadata.setEvidenceId("CANCEL");	
			metadata.setPlistFilename("CANCEL");
            setVisible(false);             
            dispose(); 
		}
		return;
	}
	
	public PlistMetaData getMetadata() {
	    return metadata;
	}
}
