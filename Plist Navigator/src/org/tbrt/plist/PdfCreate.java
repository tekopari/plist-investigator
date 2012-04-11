package org.tbrt.plist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;





import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.*;

import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDSimpleFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import com.dd.plist.*;


// http://pdfbox.apache.org/download.html
// http://pdfbox.apache.org/userguide/cookbook.html
// http://pdfbox.apache.org/userguide/cookbook/creation.html#HelloWorld

// Getting called from InvestigationTree.java 


public class PdfCreate {

	public int pdfStatus() {
		int rc = 0;
		
		//TODO Handle PDF operation status. "rc=0" means success, "rc=1" means error
		// PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\tmp\ravi.pdf"));
		
		return(rc);
	}
	
	public PdfCreate(String evidenceName, String notesName, String plistName, String PdfName) {
		NSDictionary rootDict = null;
		System.out.println("Debug:pdfcreate parameters: "+ evidenceName + "," + notesName + "," + plistName+ "," + PdfName);
		
		try {
			File file = new File(plistName);
			// rootDict = (NSDictionary) PropertyListParser.parse(file);
			System.out.println("=====> The Dir the Plist file is at: " + file.getParent() );
			GetOutput (file, PdfName, notesName);
			
			
			System.out.println(rootDict);
		} catch (Exception e) {
			System.err.println("Cannot use the PropertyListParser");
		}

	}
    
    public static void WriteFile (String str, String OFile, int TuckIn)  {
    	
    	// For Better formatting in the txt file.
    	if (TuckIn <= 1)  {
    		TuckIn = 0; 
    	}  else  {
    		TuckIn = TuckIn - 1;
    	}
    	
    	// System.out.print("&&&&&&&& WriteFile: Str" + str + ", " + "OFile " + OFile + "\n");
		  try  { 
	    	    FileWriter fw = new FileWriter(OFile, true);
	    	    BufferedWriter bw = new BufferedWriter(fw);
	    	    String Ostr = str;
	    	    
	    		for(int j = 0; j < Indent; j++) {
	    		    Ostr = "\t" + Ostr;
	    		}
                bw.write(Ostr);
                bw.newLine();
                bw.flush();
                bw.close();
		  }
		  catch(Exception ex) {
			  ex.printStackTrace();
		  }
    }

    /*
     *  Below Variable is only Ussed by the below method.
     *  It is used for formatting the output of objects.
     */
    static int Indent = 0;
    public static void ParseNSObject (PlistModel MyModel, NSObject MyObj, String OutFile)  {

    	try {
    		
    		if (MyObj == null)  {
    			System.out.println("MyObj is null");
    			return;
    		} 
    		
    		//---------------------------
    		// Process current node here
    		//---------------------------
 			   // 0 for key name, 1 for Key Type Name and 2 for Key Value
    		   String KeyName = (String) MyModel.getValueAt(MyObj, 0);
    		   String KeyTypeName = (String) MyModel.getValueAt(MyObj, 1);
    		   String KeyValue = (String) MyModel.getValueAt(MyObj, 2);	   
    		   
    		   for (int j = 0; j < Indent; j++) {
    			    System.out.print("\t");
    			}

    		   // Good for debug - System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    		   if ( (KeyTypeName == "Dictionary") ||(KeyTypeName == "Array"))  {
    			   // For these types, don't print anything for the very first object.  For some reason, it is empty.
    			   if (Indent != 0)  {
    				   System.out.println(KeyName + ":  " + KeyValue);
    				   String Wstr = KeyName + ":  " + KeyValue;
    				   WriteFile (Wstr, OutFile, Indent);
    		            
    			   }
    		   }  else {
    			   // System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    			   System.out.println(KeyName + " = " + KeyValue);
    			   String Wstr = KeyName + " = " + KeyValue;
				   WriteFile (Wstr, OutFile, Indent);
    		   }
    		   
			int ChildCount = MyModel.getChildCount(MyObj);
			// System.out.println("getChildCount(): is " + ChildCount);
			
			for ( int i = 0; i < ChildCount; i++)  {
				// System.out.println("calling nPModel with ChildCount " + i);
				Indent = Indent + 1;
				NSObject ChildObj = (NSObject) MyModel.getChild(MyObj, i);
				if (ChildObj == null)  {
					System.out.println("***** Child Model is null: " + i);
					// It looks like we never reach here by design.
					continue;
				}
				
    		   ParseNSObject (MyModel, ChildObj, OutFile);
    		   Indent = Indent - 1 ;
			}

    	}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
    }
	
    public static PDDocument text2Pdf ( BufferedReader data) 
    {

    	        PDDocument doc = null;
    	        try
    	        {
    	        	
    	            final int margin = 0;
    	            float font = 6;
    	            float height = 6;
    	            float fontSize = 6;

    	            //calculate font height and increase by 5 percent.
    	            height = height*fontSize*1.05f;
    	            doc = new PDDocument();
    	            String nextLine = null;
    	            PDPage page = new PDPage();
    	            PDPageContentStream contentStream = null;
    	            float y = -1;
    	            float maxStringLength = page.getMediaBox().getWidth() - 2*margin;
    	            
    	            // There is a special case of creating a PDF document from an empty string.
    	            boolean textIsEmpty = true;
    	            
    	            while( (nextLine = data.readLine()) != null )
    	            {
    	            	
    	            	// The input text is nonEmpty. New pages will be created and added
    	            	// to the PDF document as they are needed, depending on the length of
    	            	// the text.
    	            	textIsEmpty = false;

    	                //TBRT: original. String[] lineWords = nextLine.trim().split( " " );
    	            	String[] lineWords = new String[3];
    	            	lineWords[0] = nextLine;
    	                int lineIndex = 0;
    	                while( lineIndex < lineWords.length )
    	                {
    	                    StringBuffer nextLineToDraw = new StringBuffer();
    	                    float lengthIfUsingNextWord = 0;
    	                    do
    	                    {
    	                        nextLineToDraw.append( lineWords[lineIndex] );
    	                        nextLineToDraw.append( " " );
    	                        lineIndex++;
    	                        if( lineIndex < lineWords.length )
    	                        {
    	                            String lineWithNextWord = nextLineToDraw.toString() + lineWords[lineIndex];
    	                            lengthIfUsingNextWord = 80;
    	                        }
    	                    }
    	                    while( lineIndex < lineWords.length &&
    	                           lengthIfUsingNextWord < maxStringLength );
    	                    if( y < margin )
    	                    {
    	                    	// We have crossed the end-of-page boundary and need to extend the
    	                    	// document by another page.
    	                        page = new PDPage();
    	                        doc.addPage( page );
    	                        if( contentStream != null )
    	                        {
    	                            contentStream.endText();
    	                            contentStream.close();
    	                        }
    	                        contentStream = new PDPageContentStream(doc, page);
    	                        contentStream.beginText();
    	                        y = page.getMediaBox().getHeight() - margin + height;
    	                        contentStream.moveTextPositionByAmount(
    	                            margin, y );

    	                    }
    	                    //System.out.println( "Drawing string at " + x + "," + y );

    	                    if( contentStream == null )
    	                    {
    	                        throw new IOException( "Error:Expected non-null content stream." );
    	                    }
    	                    contentStream.moveTextPositionByAmount( 0, -height);
    	                    y -= height;
    	                    contentStream.drawString( nextLineToDraw.toString() );
    	                }


    	            }
    	            
    	        }
    	        catch(Exception ex) {
    	    			  ex.printStackTrace();
    	        }
    	        return doc;

    }

    
	public static void GetOutput (File PlistFile, String PdfFileName,  String NotesFile)  {
		
		try  {
			
			File f = null;
			String Name = null;
			
			System.out.println("Entering GetOutput");
			// Construct the .txt file, in the same directory in which we are going to create the pdf file
			f = new File (PdfFileName);
			String dir = f.getParent();
			int index = f.getName().lastIndexOf('.');
		    if (index>0&& index <= f.getName().length() - 2 ) {
		        Name = f.getName().substring(0, index);
		    }  
		    String TextFile = dir + "\\" + Name + ".txt";
		    f.delete();
		    System.out.println("GetOutput: TextFile is: " + TextFile);
			
		    // If the tmp file exists, delete and recreate it.
    	    f = new File(TextFile);
    	    if (f.exists())  {
    	    	f.delete();
    	    }
    	    f.createNewFile();
		    
			NSDictionary LocRootDict = null;
			LocRootDict = (NSDictionary)PropertyListParser.parse(PlistFile);
			PlistModel nPModel = new PlistModel(LocRootDict);
			ParseNSObject (nPModel, LocRootDict, TextFile);
			
			// Having parsed plist into txt file, append the comment file to it.
			String S1 = "\n\nComments:\n";
			String S2 = "========\n";
			WriteFile (S1, TextFile, 0);
			WriteFile (S2, TextFile, 0);
			
			File f1 = new File(TextFile);
			File f2 = new File(NotesFile);
            InputStream in = new FileInputStream(f2);
            
            
            OutputStream out = new FileOutputStream(f1,true);
 
            byte[] buf = new byte[10240];
            int len;
            
            

            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
			
			
			TextToPDF mine = new TextToPDF();
	        PDDocument MyPdfDoc = null;
			BufferedReader data = new BufferedReader( new FileReader(TextFile) );
			mine.setFontSize(6);
			MyPdfDoc = mine.createPDFFromText(data);
			
			// Test. MyPdfDoc = text2Pdf (data);
			MyPdfDoc.save(PdfFileName);
			
		}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
		
	}
	

	public static void main(String[] args) {

		try {
			File file = new File("c:\\tmp\\plistfile.plist");
			GetOutput (file, "C:\\tmp\\ravididit.pdf", "C:\\tmp\\ravinotes.txt");
			
			// ravi: Why can't we also use rootDict to create PDF?
			// rootDict = (NSDictionary) PropertyListParser.parse(file);
			// rootDict.setKey(file.getName());
			// System.out.println(rootDict);
			
			//TC new PdfCreate(rootDict);
		} catch (Exception e) {
			System.err.println("Cannot use the PropertyListParser");
		}

	}
}
