/*
* tbrt - An open source application for plist display
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

import java.awt.List;
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
import org.apache.pdfbox.examples.pdmodel.ReplaceString;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.TextToPDF;



import com.dd.plist.*;


// http://pdfbox.apache.org/download.html
// http://pdfbox.apache.org/userguide/cookbook.html
// http://pdfbox.apache.org/userguide/cookbook/creation.html#HelloWorld

// Getting called from InvestigationTree.java 



public class PdfCreate {

	static boolean PdfCreated = false;
	static boolean Pdfmulti = false;
	
	public boolean pdfStatus() {
		return(PdfCreated);
	}
	
	public void pdfReset() {
		Pdfmulti = false;
	}
	
	
	public PdfCreate(String evidenceName, String notesName, String plistName, String PdfName) {
		
		System.out.println("Debug:pdfcreate parameters: "+ evidenceName + "," + notesName + "," + plistName+ "," + PdfName);
        
		try {
			
			// During an investigation, plist name is blank, evidenceName is valid, if there is notes, output notes.
			if (plistName.length() == 0)  {
				if (evidenceName.length() != 0)  {
					PdfCreated = true;
					Pdfmulti = true;
					OutputInvestigation (evidenceName, PdfName, notesName);
					return;
				}
				
			}
			File file = new File(plistName);
			
			// If plist file does not exist, nothing to do.
			if (! file.exists())  {
				PdfCreated = false;
				return;
			}
			
			// If the file is empty, nothing to do say it is a success and return.
			if ( file.length() == 0)  {
				PdfCreated = false;
				return;				
			}
			
			// Clear the error flag, assume everything is fine.
			PdfCreated = true;
			
			GetOutput (file, PdfName, notesName, evidenceName);
		} catch (Exception e) {
			System.err.println("Problem in PdfCreate Constructor");
		}
	}

	public static final int MAX_STR_LEN = 60;
	public static final int MAX_ARRAY_SIZE = 30;
	public static final int MAX_LINE_LEN = (MAX_STR_LEN * MAX_ARRAY_SIZE);
	private static String[] stringArray = new String[20];
	public static int splitStr (String inStr)  {
		
		int iterator = 0;
		int total = inStr.length();
		for (iterator = 0; (iterator * MAX_STR_LEN) < total; iterator++)  // Max. string size is 70
		{
			int start = iterator * MAX_STR_LEN;
			int end = start + MAX_STR_LEN;
			if (end > total)
			{
				end = inStr.length();
			}
			stringArray[iterator] = inStr.substring(start, end) + "\n";
		}
		
		//System.err.println("====> Split Str, total ARRAY SIZE: " + iterator);
		// Test, splitting the string.
		for (int t=0; t < iterator; t++)  {
			//System.out.println("====> Split Str: " + stringArray[t]);
		}
		
		return iterator;
	}
    
    public static void WriteFile (String str, String OFile, int TuckIn)  {
    	
    	// System.err.print("&&&&&&&& WriteFile:, TuckIn value: " + TuckIn);
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
	    	    
	    		for(int j = 0; j < TuckIn; j++) {
	    		    Ostr = ".." + Ostr;
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

    		   String Wstr = "";
    		   // Good for debug - System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    		   if ( (KeyTypeName == "Dictionary") ||(KeyTypeName == "Array"))  {
    			   // For these types, don't print anything for the very first object.  For some reason, it is empty.
    			   if (Indent != 0)  {
    				   System.out.println(KeyName + ":  " + KeyValue);
    				   Wstr = KeyName + ":  " + KeyValue;
    		            
    			   }
    		   }  else {
    			   // System.out.println("MyObj: " + "" + KeyName + " of type " + KeyTypeName + " = " + KeyValue);
    			   System.out.println(KeyName + " = " + KeyValue);
    			   Wstr = KeyName + " = " + KeyValue;
    		   }
    		   
    		   int nStr = splitStr (Wstr);
    		   for (int j = 0; j < nStr; j++) {
    			   WriteFile (stringArray[j], OutFile, (Indent+j) );
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

    
    public static void OutputInvestigation (String evidenceName, String PdfFileName, String notesName)  {
		try  {
			
			File f = null;
			File pF = null;
			String Name = null;
			String TitleName = "";
			String S1 = "";
			String S2 = "";
			
			System.out.println("Entering OutputInvestigation");
			// Construct the .txt file, in the same directory in which we are going to create the pdf file
			f = new File (PdfFileName);
			String dir = f.getParent();
			int index = f.getName().lastIndexOf('.');
		    if (index>0&& index <= f.getName().length() - 2 ) {
		        Name = f.getName().substring(0, index);
		    }  
		    String TextFile = dir + "\\" + Name + ".txt";
		    f.delete();
		    System.out.println("OutputInvestigation: TextFile is: " + TextFile);
			
		    // If the tmp file exists, delete and recreate it.
    	    f = new File(TextFile);
    	    if (f.exists())  {
    	    	f.delete();
    	    }
    	    f.createNewFile();
		    
			
			// Write the investigation header - i.e. file name of plist file
			TitleName = "Evidence Name: " + evidenceName;
			
			WriteFile ((TitleName + "\n"), TextFile, 0);

			// Output the title to pdf
			TextToPDF mine = new TextToPDF();
	        PDDocument MyPdfDoc = null;
			BufferedReader data = new BufferedReader( new FileReader(TextFile) );
			mine.setFontSize(18);
			MyPdfDoc = mine.createPDFFromText(data);
	    	data.close();
		
			// Test. MyPdfDoc = text2Pdf (data);
	    	
			MyPdfDoc.save(PdfFileName);
			MyPdfDoc.close();
		
			
			// Put the comment after the title
			File Txtf = new File(TextFile);
			File Notef = new File(notesName);
			
			if (Notef.exists())  {
				InputStream in = new FileInputStream(Notef);    
				OutputStream out = new FileOutputStream(Txtf,true);
				
				S1 = "\n\nInvestigation Comments:    ";
				WriteFile (S1, TextFile, 0);
				
				int len;
				byte[] buf = new byte[10240];
				
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				
				// Write new lines after the comment
				S1 = "\n\n";
				WriteFile (S1, TextFile, 0);
			}
			
	
			TextToPDF nmine = new TextToPDF();
	        PDDocument nMyPdfDoc = null;
			BufferedReader ndata = new BufferedReader( new FileReader(TextFile) );
			nmine.setFontSize(10);
			nMyPdfDoc = mine.createPDFFromText(ndata);
	    	data.close();
		
			// Test. MyPdfDoc = text2Pdf (data);
	    	
			nMyPdfDoc.save(PdfFileName);
			nMyPdfDoc.close();
			

			
			
		}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
		
    }
    
	public static void GetOutput (File PlistFile, String PdfFileName,  String NotesFile, String evidenceName)  {
		
		try  {
			
			File f = null;
			File pF = null;
			String Name = null;
			boolean plistValid = false;
			String TitleName = "";
			String S1 = "";
			String S2 = "";
			
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
		    if (Pdfmulti == false) {
    	         f = new File(TextFile);
    	         if (f.exists())  {
    	             f.delete();
    	        }
    	        f.createNewFile();
		    }
		    
			NSDictionary LocRootDict = null;
			try {
				LocRootDict = (NSDictionary)PropertyListParser.parse(PlistFile);
				if (LocRootDict == null)  {
					plistValid = false;
					return;
				}  else  {
					plistValid = true;
				}
			} catch (Exception ex) {
				plistValid = false;
		    }
		 	
			// Write the plist header - i.e. file name of plist file
			int len;
			String border = "";
			TitleName = "PLIST NAME: " + evidenceName;
			TitleName = TitleName.toUpperCase();
			
			WriteFile ((TitleName + "\n"), TextFile, 0);
			len = TitleName.length();
			for(int j = 0; j < len; j++) {
    		    border = "=" + border;
    		}
			border = border + "\n";
			
			WriteFile (border, TextFile, 0);
			
		
			
			// Put the comment after the title
			File Txtf = new File(TextFile);
			File Notef = new File(NotesFile);
			
			if (Notef.exists())  {
				InputStream in = new FileInputStream(Notef);    
				OutputStream out = new FileOutputStream(Txtf,true);
				
				S1 = "\n\nPlist Comments:\n";
				S2 = "~~~~~~~~~~~~~\n";
				WriteFile (S1, TextFile, 0);
				WriteFile (S2, TextFile, 0);
				byte[] buf = new byte[10240];
				
        
				while ((len = in.read(buf)) > 0){
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				
				// Write new lines after the comment
				S1 = "\n\n";
				WriteFile (S1, TextFile, 0);
			}
			
			S1 = "Plist Content\n";
			WriteFile (S1, TextFile, 0);
			S1 = "~~~~~~~~~~\n";
			WriteFile (S1, TextFile, 0);
			
			if (plistValid)  {
				// Go parse the plist Dictionary
				PlistModel nPModel = new PlistModel(LocRootDict);
				ParseNSObject (nPModel, LocRootDict, TextFile);
			}  else {
				S1 = "===> *** Encountered Invalid Plist file *** <===\n";
				WriteFile (S1, TextFile, 0);
				System.out.println("===> *** Encountered Invalid Plist file *** <===\n");
			}
	
			TextToPDF mine = new TextToPDF();
	        PDDocument MyPdfDoc = null;
			BufferedReader data = new BufferedReader( new FileReader(TextFile) );
			mine.setFontSize(10);
			MyPdfDoc = mine.createPDFFromText(data);
	    	data.close();
		
			// Test. MyPdfDoc = text2Pdf (data);
	    	
			MyPdfDoc.save(PdfFileName);
			MyPdfDoc.close();
			
			/***  But this does do the trick.
			String tmpPdf = PdfFileName + ".tmp";
			// if we can replace the .. with space, it will be great.
	        try
	        {
	            ReplaceString app = new ReplaceString();
	            app.doIt( tmpPdf, PdfFileName, "%%", "  " );
	        }
	        catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	        ***/
			
			
		}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
		
	}
	

	public static void main(String[] args) {

		try {
			// GOOD. File file = new File("c:\\tmp\\plistfile.plist");
			// BAD: for testing. File file = new File("c:\\tmp\\ravinotes.txt");
			File file = new File("c:\\tmp\\plistfile.plist");
			GetOutput (file, "C:\\tmp\\ravididit.pdf", "C:\\tmp\\ravinotes.txt", "myEvidence");
			
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
