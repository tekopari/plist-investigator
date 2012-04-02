package org.tbrt.plist;

//SimpleEditor.java
//An example showing several DefaultEditorKit features. This class is designed
//to be easily extended for additional functionality.
//

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.JScrollPane;

public class MyEditor {

	public void doEdit(String title, String s) {
	    SimpleEditor editor = new SimpleEditor(title, s);
	    editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    editor.setVisible(true);
	    
		 ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
		 editor.setIconImage(img.getImage());
    }

	public class SimpleEditor extends JFrame {
	     private Action openAction = new OpenAction();
	     private Action saveAction = new SaveAction();	
	     private Hashtable actionHash = new Hashtable();
	   
	     private JTextComponent textComp;
	     private String notesFileName;
	     private String titleName;
			
	     // Create an editor.
	     public SimpleEditor(String title, String name) {
	    	 super(title);
	    	 
	         titleName = title;
	         notesFileName = name;

	         textComp = createTextComponent();
	         makeActionsPretty();	   
	         
	         //TC Container content = getContentPane();
	         //TC content.add(textComp, BorderLayout.CENTER);
	         //TC content.add(createToolBar(), BorderLayout.NORTH);
	         
	         setJMenuBar(createMenuBar());
	         setBounds(200, 100, 500, 400);
	         
	         myReadFile(notesFileName);
	     }
	
	     // Create the JTextComponent subclass.
	     protected JTextComponent createTextComponent() {
	         JTextArea textArea = new JTextArea();	        
	         textArea.setLineWrap(true);
	         textArea.setWrapStyleWord(true);
	         
		     JScrollPane scrollText = new JScrollPane(textArea);
		     add(scrollText);	
		     scrollText.isVisible();	
		        
	         return textArea;
	    }
	     
	    // Add icons and friendly names to actions we care about.
	    protected void makeActionsPretty() {
	        Action a;
	        a = textComp.getActionMap().get(DefaultEditorKit.cutAction);
	        a.putValue(Action.SMALL_ICON, new ImageIcon("cut.gif"));
	        a.putValue(Action.NAME, "Cut");
	
	        a = textComp.getActionMap().get(DefaultEditorKit.copyAction);
	        a.putValue(Action.SMALL_ICON, new ImageIcon("copy.gif"));
	        a.putValue(Action.NAME, "Copy");
	
	        a = textComp.getActionMap().get(DefaultEditorKit.pasteAction);
	        a.putValue(Action.SMALL_ICON, new ImageIcon("paste.gif"));
	        a.putValue(Action.NAME, "Paste");
	
	        a = textComp.getActionMap().get(DefaultEditorKit.selectAllAction);
	        a.putValue(Action.NAME, "Select All");
	    }
	
	    // Create a simple JToolBar with some buttons.
	    protected JToolBar createToolBar() {
	        JToolBar bar = new JToolBar();

	        // Add simple actions for opening & saving.
	        bar.add(getOpenAction()).setText("");
	        bar.add(getSaveAction()).setText("");
	        bar.addSeparator();
	        
	        // Add cut/copy/paste buttons.
	        bar.add(textComp.getActionMap().get(DefaultEditorKit.cutAction))
	            .setText("");
	        bar.add(textComp.getActionMap().get(DefaultEditorKit.copyAction))
	            .setText("");
	        bar.add(textComp.getActionMap().get(DefaultEditorKit.pasteAction))
	            .setText("");

	        return bar;
	    }
	
	    // Create a JMenuBar with file & edit menus.
	    protected JMenuBar createMenuBar() {
	        JMenuBar menubar = new JMenuBar();
	        JMenu file = new JMenu("File");
	        JMenu edit = new JMenu("Edit");
	        menubar.add(file);
	        menubar.add(edit);
	
	        //TC file.add(getOpenAction());
	        file.add(getSaveAction());
	        file.add(new ExitAction());
	        edit.add(textComp.getActionMap().get(DefaultEditorKit.cutAction));
	        edit.add(textComp.getActionMap().get(DefaultEditorKit.copyAction));
	        edit.add(textComp.getActionMap().get(DefaultEditorKit.pasteAction));
	        edit.add(textComp.getActionMap().get(DefaultEditorKit.selectAllAction));
	        return menubar;
	    }
	
	    // Subclass can override to use a different open action.
	    protected Action getOpenAction() {
	        return openAction;
	    }
	
	    // Subclass can override to use a different save action.
	    protected Action getSaveAction() {
	        return saveAction;
	    }
	
	    protected JTextComponent getTextComponent() {
	        return textComp;
	    }

	    // Read the file
	    protected void myReadFile(String s) {
	    	File file = new File(s);
	    	FileReader reader = null;
            try {
                reader = new FileReader(file);
                textComp.read(reader, null);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SimpleEditor.this,
                    "File Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException x) {
                    }
                }
            }
	    }

	    // Write to file
	    protected void myWriteFile(String s) {
	    	File file = new File(s);
            FileWriter writer = null;
            try {
                writer = new FileWriter(file);
                textComp.write(writer);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(SimpleEditor.this,
                "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException x) {
                    }
                }
            }
	    }
        
        
	    // ********** ACTION INNER CLASSES ********** //

	    // A very simple exit action
	    public class ExitAction extends AbstractAction {
	        public ExitAction() {
	            super("Exit");
	        }
	
	        public void actionPerformed(ActionEvent ev) {
				int n = JOptionPane.showConfirmDialog (
						textComp,
						"Save the Notes Before Exit?",
						titleName,
						JOptionPane.YES_NO_CANCEL_OPTION);
				System.out.println("TC:"+n);
				if ((n == 0) || (n == 1)) {
					if (n == 0) {
				       myWriteFile(notesFileName);
					}
				    dispose();
				}
	        }
	    }
	
	    // An action that opens an existing file
	    class OpenAction extends AbstractAction {
	        public OpenAction() {
	          super("Open", new ImageIcon("icons/open.gif"));
	        }
	
	        // Query user for a filename and attempt to open and read the file into
	        // the text component.
	        public void actionPerformed(ActionEvent ev) {
	            JFileChooser chooser = new JFileChooser();
	            if (chooser.showOpenDialog(SimpleEditor.this) != JFileChooser.APPROVE_OPTION)
	                return;
	            File file = chooser.getSelectedFile();
	            if (file == null)
	                return;
	
	            FileReader reader = null;
	            try {
	                reader = new FileReader(file);
	                textComp.read(reader, null);
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(SimpleEditor.this,
	                    "File Not Found", "ERROR", JOptionPane.ERROR_MESSAGE);
	            } finally {
	                if (reader != null) {
	                    try {
	                        reader.close();
	                    } catch (IOException x) {
	                    }
	                }
	            }
	        }
	    }
	
	    // An action that saves the document to a file
	    class SaveAction extends AbstractAction {
	        public SaveAction() {
	            super("Save", new ImageIcon("icons/save.gif"));
	        }

	        public void actionPerformed(ActionEvent ev) {
	            myWriteFile(notesFileName);
	            
	            /*TC
	            // Query user for a filename and attempt to open and write the text
	            // component's content to the file.
	            JFileChooser chooser = new JFileChooser();
	            if (chooser.showSaveDialog(SimpleEditor.this) != JFileChooser.APPROVE_OPTION)
	                return;
	            File file = chooser.getSelectedFile();
	            if (file == null)
	                return;
	
	            FileWriter writer = null;
	            try {
	                writer = new FileWriter(file);
	                textComp.write(writer);
	            } catch (IOException ex) {
	                JOptionPane.showMessageDialog(SimpleEditor.this,
	                "File Not Saved", "ERROR", JOptionPane.ERROR_MESSAGE);
	            } finally {
	                if (writer != null) {
	                    try {
	                        writer.close();
	                    } catch (IOException x) {
	                    }
	                }
	            }
	            TC*/
	        }	        
	    }
	}
}