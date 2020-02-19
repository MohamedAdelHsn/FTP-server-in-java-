package com.jftp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

public class ClientHandler {

	 Socket socket;
	 File myFile;
	 BufferedInputStream bis ;
	 DataInputStream dis ;
	 OutputStream os;
	 DataOutputStream dos ;
     FileInputStream fis;
	
	public ClientHandler() 
	{
		this.socket = null;
		
	}
	
	public void Connect(String host , int port)
	{
		 try {
			 
			 socket = new Socket(host , port);
		
			
		   }catch(java.net.ConnectException ex)
		   {
				JOptionPane.showMessageDialog(null , "\r\n" + 
						"run the server first then try again !", "Server Not responding !" ,2);
		    			    	
		   }catch( java.lang.NumberFormatException numEx)
		   {
			   JOptionPane.showMessageDialog(null , "please enter real port", "\r\n" + 
						"server Exception" ,2);
			  
		  }catch(java.net.UnknownHostException unknownEx)
		  {
			  JOptionPane.showMessageDialog(null , "please enter real host", "\r\n" + 
					  "server Exception" ,2);

		  }catch( java.lang.IllegalArgumentException portEx) {
			  
			  JOptionPane.showMessageDialog(null , "please enter real port", "\r\n" + 
						"server Exception" ,2);
			  
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void uploadFile(String path) throws IOException 
	{
		
		 try {
			 
			    myFile  = new File(path);
			     
			    // store data using byte array  
			    byte[] mybytearray = new byte[(int) myFile.length()];
			         
				fis = new FileInputStream(myFile);
			
				bis = new BufferedInputStream(fis);  
			     
				// represent primitive data type that carries file name , file size and etc....
			    dis = new DataInputStream(bis);     
			  
			    // read data to byte array
			    dis.readFully(mybytearray, 0, mybytearray.length);
			
			    // connect to stream socket to send data        
			    os = socket.getOutputStream();  
			           
			   //Sending file name and file size to the server  
			    dos = new DataOutputStream(os);     
			    dos.writeUTF(myFile.getName());
			    dos.writeLong(mybytearray.length);     
			    dos.flush();  
					    
					    
			   // sending file data over socket
			   os.write(mybytearray, 0, mybytearray.length);  
			   os.flush();
			   
			 }catch(java.net.ConnectException ex) 
			 {
				 JOptionPane.showMessageDialog(null , "\r\n" + 
							"run the server first then try again !", "Server Not responding !" ,2);
				 
			 }catch (FileNotFoundException e) {
				
					JOptionPane.showMessageDialog(null , "InValid Path !", "InValid Alert" ,2);
				  
			 }catch(java.lang.NullPointerException nullP)
			 {
				  JOptionPane.showMessageDialog(null , "please enter real data", "\r\n" + 
							"server Exception" ,2);
				
			 }catch(java.net.SocketException socketEX) 
			 {
				 // -------  write code here  ------- //
				 
			 }
			
			 finally {
				
				  //Closing socket
				 if(os != null) {
				  os.close();
				 }
				 if(dos != null)
				 {
				  dos.close();
				 }
				
				 
			}
		
		
		
	}
}
