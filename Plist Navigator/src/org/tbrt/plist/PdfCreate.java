package org.tbrt.plist;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

// http://pdfbox.apache.org/download.html
// http://pdfbox.apache.org/userguide/cookbook.html
// http://pdfbox.apache.org/userguide/cookbook/creation.html#HelloWorld

public class PdfCreate {

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
			rootDict.setKey(file.getName());
			testcreate(pdfName);
			
			System.out.println(rootDict);
		} catch (Exception e) {
			System.err.println("Cannot use the PropertyListParser");
		}

	}	
	
	public void testcreate(String fileName) throws IOException, COSVisitorException {
		// the document
		PDDocument doc = null;
		try {
			doc = new PDDocument();

			PDPage page = new PDPage();
			doc.addPage(page);
			PDFont font = PDType1Font.HELVETICA_BOLD;

			PDPageContentStream contentStream = new PDPageContentStream(doc,
					page);
			contentStream.beginText();
			contentStream.setFont(font, 12);
			contentStream.moveTextPositionByAmount(100, 700);
			String message = "Test by TBRT";
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

	public static void main(String[] args) {
		NSDictionary rootDict = null;
		try {
			File file = new File("c:\\tmp\\plistfile.plist");
			rootDict = (NSDictionary) PropertyListParser.parse(file);
			// ravi: Why can't we also use rootDict to create PDF?
			rootDict.setKey(file.getName());
			
			System.out.println(rootDict);
			
			//TC new PdfCreate(rootDict);
		} catch (Exception e) {
			System.err.println("Cannot use the PropertyListParser");
		}

	}
}
