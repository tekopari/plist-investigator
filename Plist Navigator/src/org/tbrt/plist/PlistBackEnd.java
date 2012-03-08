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