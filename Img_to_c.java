
package net.codejava;

import javax.swing.*; 

public class Img_to_c {
	
	public static long len = 0; // for debugging purpose only
	
	private static int invalid_file = 1;
	
	public static void MakeC( byte[] p, int iLen, int bLast, JTextArea textArea)
	{
		int i, j, iCount;
		String  szOut = new String(" ");
		String szTemp =  new String(" ");
		iCount = 0;
		
		for (i = 0; i<iLen >> 4; i++) // do lines of 16 bytes
		{
			szOut = String.format(" ");
			for (j = 0; j<16; j++)
			{
				
				if (iCount == (iLen - 1) && bLast > 0) // last one, skip the comma
					 szTemp =  String.format( "0x%02x", p[(i * 16) + j]);
				else
					szTemp = String.format( "0x%02x,", p[(i * 16) + j]);
				szOut = szOut + szTemp;
				iCount++;
			}
			if (!(bLast > 0) || iCount != iLen)
				szOut = szOut + String.format("\n");
			
			//System.out.println( szOut);
			textArea.append(szOut);
			len += szOut.length();
		}
		
		int lastSection = iLen & 0xfff0;
		
		if ((iLen & 0xf) > 0) // any remaining characters?
		{
			szOut = String.format(" ");
			for (j = 0; j<(iLen & 0xf); j++)
			{
				if (iCount == iLen - 1 && (bLast > 0))
					szTemp =  String.format( "0x%02x", p[j + lastSection]);
				else
					szTemp =  String.format( "0x%02x,",  p[j + lastSection]);
				szOut = szOut + szTemp;
				iCount++;
			}
			
			// if it is last byte in file
			if (!(bLast > 0))
				szOut = szOut + String.format("\n");
			
			textArea.append(szOut);
			len += szOut.length();
			
			
		}
	} 
	  //
	  // Make sure the name can be used in C/C++ as a variable
	  // replace invalid characters and make sure it starts with a letter
	  //
	public static void FixName(Container name)
	{
		char c;
		int i, iLen;
		
		String szTemp = new String("");

		iLen = name.data.length();
		
		if (name.data.charAt(0) >= '0' && name.data.charAt(0) <= '9') // starts with a digit
			szTemp = szTemp + '_'; // Insert an underscore
		for (i = 0; i<iLen; i++)
		{
			c = name.data.charAt(i);
			// these characters can't be in a variable name
			if (c < ' ' || (c >= '!' && c < '0') || (c > 'Z' && c < 'a'))
				c = '_'; // convert all to an underscore
			szTemp = szTemp + c;
		}
	
		name.data = String.valueOf(szTemp);
		
	} 
	  //
	  // Trim off the leaf name from a fully
	  // formed file pathname
	  //
	public static void GetLeafName(String fname, Container leaf)
	{
		int i, iLen;
		invalid_file = 1;
		len = 0;
		iLen = fname.length();
		for (i = iLen - 1; i >= 0; i--)
		{
			if (fname.charAt(i) == '\\' || fname.charAt(i) == '/') 
				break;
		}
		
		leaf.data = fname.substring(i + 1);
		
		
		
		// remove the filename extension
		iLen = leaf.data.length();
		for (i = iLen - 1; i >= 0; i--)
		{
			if (leaf.data.charAt(i) == '.')
			{
				String file_ext = leaf.data.substring(i + 1);
				if(file_ext.equalsIgnoreCase("png") ||file_ext.equalsIgnoreCase("jpeg") || file_ext.equalsIgnoreCase("jpg") || 
						file_ext.equalsIgnoreCase("gif") || file_ext.equalsIgnoreCase("bmp") || file_ext.equalsIgnoreCase("pcx") ||
						file_ext.equalsIgnoreCase("ppm") || file_ext.equalsIgnoreCase("tiff"))
					invalid_file = 0; // it is an image file
				
				leaf.data = leaf.data.substring(0, i);
				break;
			}
		}
	} 
	
	// to check if file is image file
	public static int is_invalid()
	{
		return invalid_file;
	}
}


