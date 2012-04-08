package org.tbrt.plist;
//import xmlpdf.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		System.out.println("TC:pdfcreate:"+plistName+","+pdfName);
		
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

	
	public static void GetOutput (File file)  {
		
		try  {
			NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
			PropertyListParser.saveAsXML(rootDict, new File("C:\\tmp\\ravi.xml"));
			String PlistXmlString = readFileAsString ("C:\\tmp\\ravi.xml");
			Str2Pdf(PlistXmlString);
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
