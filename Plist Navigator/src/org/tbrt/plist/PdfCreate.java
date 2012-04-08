package org.tbrt.plist;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


// import itext library.
import com.itextpdf.*;
import com.itextpdf.tool.*;
import com.itextpdf.tool.xml.*;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.pdfbox.*;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

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

public class PdfCreate {

	public int pdfStatus() {
		int rc = 0;
		
		//TODO Handle PDF operation status. "rc=0" means success, "rc=1" means error
		// PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\tmp\ravi.pdf"));
		
		return(rc);
	}
	
	public void foo() {
		PDFont font = PDType1Font.HELVETICA_BOLD;
	}

	//TC public PdfCreate(NSDictionary rootDict) {
	//TC }
	
	public PdfCreate(String evidenceName, String notesName, String plistName, String pdfName) {
		NSDictionary rootDict = null;
		System.out.println("Debug:pdfcreate:"+plistName+","+pdfName);
		
		try {
			File file = new File(plistName);
			rootDict = (NSDictionary) PropertyListParser.parse(file);
			// ravi: Why can't we also use rootDict to create PDF?
			
			System.out.println(rootDict);
		} catch (Exception e) {
			System.err.println("Cannot use the PropertyListParser");
		}

	}
	

	
	public static void Str2Pdf(String OutputStr) throws IOException, COSVisitorException {
		// the document
		PDDocument doc = null;
		try {
			String fileName = "C:\\tmp\\ravi.pdf";
			doc = new PDDocument();

			PDPage page = new PDPage();
			doc.addPage(page);
			PDFont font = PDType1Font.HELVETICA_BOLD;

			PDPageContentStream contentStream = new PDPageContentStream(doc,
					page);
			contentStream.beginText();
			contentStream.setFont(font, 12);
			contentStream.moveTextPositionByAmount(100, 700);
			String message = OutputStr;
			contentStream.drawString(message);
			contentStream.endText();
			contentStream.close();
			doc.save(fileName);
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}
	
    private static String readFileAsString(String filePath)
    throws java.io.IOException{
        StringBuffer fileData = new StringBuffer(1024 * 100);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024 * 100];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    
    private PDDocument createPDFFromText( Reader text ) throws IOException
    {
        PDDocument doc = null;
        try
        {
        	
            final int margin = 40;
            PDFont font = null;
			float height = font.getFontDescriptor().getFontBoundingBox().getHeight()/1000;

            int fontSize = 0;
			//calculate font height and increase by 5 percent.
            height = height*fontSize*1.05f;
            doc = new PDDocument();
            BufferedReader data = new BufferedReader( text );
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

                String[] lineWords = nextLine.trim().split( " " );
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
                            lengthIfUsingNextWord =
                                (font.getStringWidth( lineWithNextWord )/1000) * fontSize;
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
                        contentStream.setFont( font, fontSize );
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
            
            // If the input text was the empty string, then the above while loop will have short-circuited
            // and we will not have added any PDPages to the document.
            // So in order to make the resultant PDF document readable by Adobe Reader etc, we'll add an empty page.
            if (textIsEmpty)
            {
            	doc.addPage(page);
            }
            
            if( contentStream != null )
            {
                contentStream.endText();
                contentStream.close();
            }
        }
        catch( IOException io )
        {
            if( doc != null )
            {
                doc.close();
            }
            throw io;
        }
        return doc;
    }
	
	public static void GetOutput (File file)  {
		
		try  {
			NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
			PropertyListParser.saveAsXML(rootDict, new File("C:\\tmp\\ravi.xml"));
			//String PlistXmlString = readFileAsString ("C:\\tmp\\ravi.xml");
			//Str2Pdf(PlistXmlString);
			
			// Now create the Pdf document using Apache PdfBox
			// PDDocument document = new PDDocument();
			// PDPage page = new PDPage();
			// document.addPage( page );
			
			
			TextToPDF mine = new TextToPDF();
	        PDDocument MyPdfDoc = null;
			BufferedReader data = new BufferedReader( new FileReader("C:\\tmp\\ravi.xml") );
			MyPdfDoc = mine.createPDFFromText(data);
			MyPdfDoc.save("C:\\tmp\\ravididit.pdf");
			
		}
	    catch(Exception ex) {
		  ex.printStackTrace();
	    }
		
		/*********************************  BEGIN:RAVI
		
		try {
			  
			  NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
			  
			  String name = rootDict.objectForKey("Name").toString();
			  NSObject[] parameters = ((NSArray)rootDict.objectForKey("Parameters")).getArray();
			  
			  for(NSObject param:parameters) {
			    if(param.getClass().equals(NSNumber.class) ) {
			      NSNumber num = (NSNumber)param;
			      switch(num.type()) {
			        case NSNumber.BOOLEAN : {
			          boolean bool = num.boolValue();
			          //...
			          break;
			        }
			        case NSNumber.INTEGER : {
			          long l = num.longValue();
			          //or int i = num.intValue();
			          //...
			          break;
			        }
			        case NSNumber.REAL : {
			          double d = num.doubleValue();
			          //...
			          break;
			        }
			     }
			   }
			    else  {
			    	// Ravi: Add whatever is necessary.
			    }
			  }
			    
		} catch(Exception ex) {
			  ex.printStackTrace();
		}
		
		******************END:RAVI****/
	}
	
	public static void Xml2Pdf (String Xmlfile)  {
		
		
		
		/*** itext try BEGIN:RAVI
		try  {
			// Below is itext example.
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream("C:\\tmp\raviDoc.pdf") ); 
			// THIS LINE DOES NOT WORK ITextHandler xmlHandler = new ITextHandler(document); 
			// xmlHandler.Parse("ExampleDoc.xml"); 
		}
	        catch (Exception e) {
		    System.err.println("Cannot use the PropertyListParser");
	    }
	    ******* END:RAVI ******/
		
		/********** fopFactory example  BEGIN:RAVI
		File xmlfile = new File("C:\\tmp\\ravi.xml");
		File xsltfile = new File("C:\\tmp\\ravi.xsml");
		File pdffile = new File("C:\\tmp\\ResultXMLPDF.pdf");

		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		OutputStream out = new java.io.FileOutputStream(pdffile);
		out = new java.io.BufferedOutputStream(out);

		try
		{
		    Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		    // Setup XSLT
		    TransformerFactory factory = TransformerFactory.newInstance();
		    Transformer transformer = factory.newTransformer(new StreamSource(xsltfile));

		    transformer.setParameter("versionParam", "1.0");

		    Source src = new StreamSource(xmlfile);

		    Result res = new SAXResult(fop.getDefaultHandler());

		    transformer.transform(src, res);

		} finally {
		    out.close();
		}

		System.out.println("Success!");
		********** END:RAVI ********************/

	}

	public static void main(String[] args) {
		NSDictionary rootDict = null;
		try {
			File file = new File("c:\\tmp\\plistfile.plist");
			GetOutput (file);
			
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
