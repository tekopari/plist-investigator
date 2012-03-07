package org.tbrt.plist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;


public class MessageDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	
    public static void displayMessage(String title, String message) {
		try {
			MessageDialog dialog = new MessageDialog(title, message);
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return;	
    }

	/**
	 * Create the dialog.
	 */
	public MessageDialog(String title, String message) {
		setTitle(title);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
	    
		{
			JTextArea textArea = new JTextArea();
			textArea.setBackground(getContentPane().getBackground());
			textArea.setEditable(false);
			textArea.setFont(new Font("Monospaced", Font.BOLD, 16));
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setText(message);
			textArea.setBounds(0, 0, 442, 225);
			contentPanel.add(textArea);
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
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK")){
            setVisible(false);             
            dispose(); 
		}

		return;
	}
}
