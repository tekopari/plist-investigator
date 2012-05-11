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

//SimpleEditor.java
//An example showing several DefaultEditorKit features. This class is designed
//to be easily extended for additional functionality.
//

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.JScrollPane;

public class MyEditor {
    private JTextComponent textComp;
    private String notesFileName;
    private String titleName;
    private boolean changed;
    
	public void doEdit(String title, String s) {
		changed = false;
        titleName = title;
        notesFileName = s;
        
	    SimpleEditor editor = new SimpleEditor();
	    editor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    editor.setVisible(true);
	      
	    ImageIcon img = new ImageIcon(this.getClass().getResource("images/tbrt_logo.jpg"));
		editor.setIconImage(img.getImage());
		 
	    editor.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	        	if (changed) {
	        		int n = JOptionPane.showConfirmDialog (
							textComp,
							"Save the Notes Before Exit?",
							titleName,
							JOptionPane.YES_NO_OPTION);
				    if (n == 0) {
				    	File file = new File(notesFileName);
			            FileWriter writer = null;
			            try {
			                writer = new FileWriter(file);
			                textComp.write(writer);
			            } catch (IOException ex) {
			            } finally {
			                if (writer != null) {
			                    try {
			                        writer.close();
			                    } catch (IOException x) {
			                    }
			                }
			            }
					}
	        	}
				
	         }
	     });
    }
	
	public class SimpleEditor extends JFrame {
	     private Action openAction = new OpenAction();
	     private Action saveAction = new SaveAction();	
	     private Hashtable actionHash = new Hashtable();	        
			
	     // Create an editor.
	     public SimpleEditor() {
	    	 super(titleName);
	    	 
	    	 changed = false;
	         textComp = createTextComponent();
	         makeActionsPretty();	   
	         
	         //TC Container content = getContentPane();
	         //TC content.add(textComp, BorderLayout.CENTER);
	         //TC content.add(createToolBar(), BorderLayout.NORTH);
	         
	         setJMenuBar(createMenuBar());
	         setBounds(200, 100, 500, 400);	                	         
	         
	         myReadFile(notesFileName);
	         
	         textComp.getDocument().addDocumentListener(new DocumentListener() {
	        	 public void insertUpdate(DocumentEvent e) {
	     		    changed = true;
	     		} 
	     		    
	     	    public void removeUpdate(DocumentEvent e) {
	     		    changed = true;
	     		}
	     		    
	     		public void changedUpdate(DocumentEvent e) {
	     		    changed = true;
	     		}
	         });
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
	        	if (changed) {
				    int n = JOptionPane.showConfirmDialog (
						    textComp,
						    "Save the Notes Before Exit?",
						    titleName,
						    JOptionPane.YES_NO_CANCEL_OPTION);
				    if ((n == 0) || (n == 1)) {
					    if (n == 0) {
				           myWriteFile(notesFileName);
					    }
				    }
				}
	        	dispose();
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
	            changed = false;
	            
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