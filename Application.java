package net.codejava;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 

//Import the File class
import java.io.File;

//Import this class for handling errors
import java.io.FileNotFoundException; 
import java.io.IOException;

//Import the Scanner class to read content from files
import java.util.Scanner; 
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.RandomAccessFile;




public class Application{
	
	private static JFrame f;
	private static JButton b;
	private static ImageIcon icon;
	private static JLabel Jicon;
	private static JTextArea textArea;
	private static JScrollPane scrollPane;
	private static JScrollBar bar;
	private static Container leafname;
	private static JFileChooser fileChooser;
	
	
		  public Application()
		  {
			  // Initialize objects
			  f=new JFrame("Image to C Array");//creating instance of JFrame
			  b = new JButton("Load Image");
			  icon = new ImageIcon();
			  Jicon = new JLabel(icon);
			  textArea = new JTextArea();
			  bar = new JScrollBar();
			  leafname = new Container();
			  fileChooser = new JFileChooser();
			  
			  
			  b.setBounds(340, 400, 100, 30);
			  Jicon.setBounds(10, 20, 300, 300);
			  textArea.setBounds(400, 20, 300, 300);
		      textArea.setEditable(false);
		      
		      scrollPane = new JScrollPane(textArea);
		      
		      
		      scrollPane.add(bar);
		      scrollPane.setBounds(400, 20, 300, 300);
		      
		      f.add(b);
		      f.add(Jicon);
		      f.add(scrollPane);	 
		    	
		      f.setSize(800,500);//800 width and 500 height  
			  f.setLayout(null);//using no layout managers  
			  f.setVisible(true);//making the frame visible  
			  
			  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
			  
		  }
		  
		  // run main application
		  public static void run()
		  {
			  // button event listner
			  b.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
				  int result = fileChooser.showOpenDialog(f);
				  if (result == JFileChooser.APPROVE_OPTION) {
					  try {
						  textArea.setText("");
						  File selectedFile = fileChooser.getSelectedFile();
					 	  byte[] bytes  = new byte[64*1024]; //64 kb buffer used in MakeC method
						  RandomAccessFile obj = new RandomAccessFile(selectedFile, "r");
						  long file_size = selectedFile.length();
						  
						  ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
						 
					      Image image = icon.getImage(); // transform it 
					      Image newimg = image.getScaledInstance(300, 300,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way 
					      icon = new ImageIcon(newimg);  // transform it back
					      
					      Jicon.setIcon(icon);
					      
					      //for debugging purpose
					 	  System.out.println("file size " + file_size);
						  System.out.println("Selected file: " + selectedFile.getAbsolutePath());
										  
						  // get file name with no extensions				
						  Img_to_c.GetLeafName(selectedFile.getAbsolutePath(), leafname);
						  
						  //check if it is valid image file
						  if((Img_to_c.is_invalid()) == 1)
						  {
							  textArea.append("Please Load valid Image file !!!");
							  return;
						  }
						  Img_to_c.FixName(leafname);
										    
						  System.out.println("Selected file: " + leafname.data);
				 	  
					  
				 		  int offset = 0;
						  try{
							  String c_data = new String("");
							  c_data = String.format("\nconst uint8_t %s[] = {\n ", leafname.data);
						      textArea.append(c_data);
							  
						      while (file_size > 0) {
								                
								  int bytes_read = obj.read(bytes); //try to read 64K data 
								  if (bytes_read == -1) {
									  break;
								  }
								  if(file_size == bytes_read)
									  Img_to_c.MakeC(bytes, bytes_read, 1, textArea);
								  else
									  Img_to_c.MakeC(bytes, bytes_read, 0, textArea);
								                    
								  file_size -= bytes_read;
								  offset += bytes_read;
								  obj.seek(bytes_read);
								}
						      
						      c_data = String.format("};\n");
						      textArea.append(c_data);
									    	 
							  obj.close();
							  
							  System.out.println( "Total length" + Img_to_c.len);
						  }catch (IOException er) {
							  er.printStackTrace();
						  }
						}catch (FileNotFoundException ef) {
							System.out.println("An error has occurred.");
							ef.printStackTrace();
						  }
				         }
				      }});
		  }
		  
		  public static void main(String argv[]) {	
			  Application app = new Application();
			  app.run();
			} 				          
				 
		} 
			





