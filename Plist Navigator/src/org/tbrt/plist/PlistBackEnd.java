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
import java.io.*;
import java.math.BigInteger;

import com.dd.plist.*;


public class PlistBackEnd {
	private static void usage() {
		System.out.println("USAGE: tbrtplist.exe –i <plistfile> -o <outputfile> -t <xml | pdf | bin>");
		System.out.println();
		return;
	}
	
    public static void main(String args[])
    {
    	//-------------------------------------------------------------------
    	// Check for the correct number of command line arguments
    	//-------------------------------------------------------------------
    	if (args.length != 6) {
    		usage();
    		System.exit(1);
    	}
    	
    	//-------------------------------------------------------------------
    	// Parse the command line arguments
    	//-------------------------------------------------------------------
    	String inputFile  = "";
    	String outputFile = "";
    	boolean xmlOutput = false;
    	boolean pdfOutput = false;
    	boolean binOutput = false;
    	boolean processingInputOption  = false;
    	boolean processingOutputOption = false; 
    	boolean processingOutputType   = false;
        for (String s: args) {
        	
        	//---------------------------------------------------------------
        	// Ignore null or empty strings
        	//---------------------------------------------------------------
        	if(s == null || s.equals("")) {
        		continue;
        	}
        	//---------------------------------------------------------------
        	// User specified the input file option
        	//---------------------------------------------------------------   	
        	else if(s.equals("-i")) {
        		if(processingInputOption ||
             	   processingOutputOption || 
             	   processingOutputType) {
             		usage();
             		System.exit(1);
             	}
        		inputFile = "";
        		processingInputOption = true;
        	}
        	//---------------------------------------------------------------
        	// User specified the input file option
        	//---------------------------------------------------------------   	
        	else if(s.equals("-o")) {
        		if(processingInputOption ||
        		   processingOutputOption || 
        		   processingOutputType) {
        			usage();
        			System.exit(1);
        		}
        		outputFile = "";
        		processingOutputOption = true;
        	}
        	//---------------------------------------------------------------
        	// User specified the input file option
        	//---------------------------------------------------------------   	
        	else if(s.equals("-t")) {
        		if(processingInputOption ||
                   processingOutputOption || 
             	   processingOutputType) {
             		usage();
             		System.exit(1);
             	}
            	xmlOutput = false;
            	pdfOutput = false;
            	binOutput = false;
            	processingOutputType = true;
        	}
        	//---------------------------------------------------------------
        	// Looking for the output filename
        	//---------------------------------------------------------------   	
        	else if(processingInputOption) {
        		inputFile = s;
        		processingInputOption = false;
        	}
        	//---------------------------------------------------------------
        	// Looking for the output filename
        	//---------------------------------------------------------------   	
        	else if(processingOutputOption) {
        		outputFile = s;
        		processingOutputOption = false;
        	}
        	//---------------------------------------------------------------
        	// Looking for a valid output type
        	//---------------------------------------------------------------   	
        	else if(processingOutputType) {
        		if(s.equals("xml")) {
        			xmlOutput = true;
        			processingOutputType = false;
        		}
        		else if(s.equals("pdf")) {
        			pdfOutput = true; 
        			processingOutputType = false;
        		}
        		else if(s.equals("bin")) {
        			binOutput = true;
        			processingOutputType = false;
        		}
        		else {
        			usage();
        			System.exit(1);
        		}
        	} 
        }
        
        //-------------------------------------------------------------------
        // Check if specified all the options and parsing completed
        //-------------------------------------------------------------------
    	if(inputFile.equals("") ||
    	   outputFile.equals("") ||
    	   (!xmlOutput && !pdfOutput && !binOutput) ||
    	   (xmlOutput && pdfOutput) ||
    	   (xmlOutput && binOutput) ||
    	   (pdfOutput && binOutput) ||
    	   processingInputOption  ||
    	   processingOutputOption || 
    	   processingOutputType) {
			usage();
			System.exit(1);	
    	}
    	
        //-------------------------------------------------------------------
        // Check to see if the input file exists
        //-------------------------------------------------------------------
        File in = null;
        try {
        	in = new File(inputFile);   	
        	if(!in.exists()) {
        		System.err.println("Specified input file [" 
        	                      + inputFile 
        	                      + "] does not exist");
        		System.exit(1);
        	}
        }
        catch (Exception e) {
        	System.err.println("Error occurred while check if input file exists");
        	System.err.println("Exception details:");
        	System.err.println(e);
        	System.exit(1);
        }
        
        //-------------------------------------------------------------------
        // Parse the input file
        //-------------------------------------------------------------------
        NSDictionary rootDict = null;
        try
        {   
        	rootDict = (NSDictionary)PropertyListParser.parse(in);
        }
        catch (Exception e)
        {
        	System.err.println("Error occurred while parsing input file.");
        	System.err.println("Exception details:");
        	System.err.println(e);
        	System.exit(1);
        }
        
        //-------------------------------------------------------------------
        // Write the output file based on the user specified type
        //-------------------------------------------------------------------
        try
        { 
        	// TODO: what if user wants to send the output to stdout
        	File out = new File(outputFile);
        	
        	if(xmlOutput) {
        		PropertyListParser.saveAsXML(rootDict, out);
        	}
        	else if(pdfOutput) {
        		// TODO:  will have to save it as xml then convert to pdf
        		PropertyListParser.saveAsXML(rootDict, out);
        	}
        	else if(binOutput) {
        		PropertyListParser.saveAsBinary(rootDict, out);
        	}
        }
        catch (Exception e)
        {
        	System.err.println("Error occurred creating output file.");
        	System.err.println("Exception details:");
        	System.err.println(e);
        	System.exit(1);
        }
        
    	System.exit(0);
    }
}