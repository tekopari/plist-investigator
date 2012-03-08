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
    public static void main(String args[])
    {
     try
       {
        // Open the file that is the first
        // command line parameter
        //FileInputStream fstream = new
        //FileInputStream("C:/tmp/plistfile.plist");
        // Convert our input stream to a
        // DataInputStream
        //DataInputStream in =  new DataInputStream(fstream);
        // Continue to read lines while
        // there are still some left to read
        //byte b[] = new byte[10];
        //in.read(b);
        try {
           File file = new File("C:/tmp/plistfile.plist");
           // File file = new File("C:/tmp/lab3.tgz");
           //using opensource parser, please see java files in com.dd.plist for more information.
            NSDictionary rootDict = (NSDictionary)PropertyListParser.parse(file);
             }
              catch (Exception e)
             {
               System.err.println("Cannot use the PropertyListParser");
             }
          //System.out.write(b);

          // Print file line to screen
            System.out.println ("TBRT team is testing\n");
            // in.close();
          }
        catch (Exception e)
          {
              System.err.println("File input error");
           }
    }
}