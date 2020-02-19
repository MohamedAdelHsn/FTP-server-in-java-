package com.jftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

public class ClientSide extends ClientGUI {

	static ClientHandler handler;
	
	ClientSide()
	{
		 handler = new ClientHandler();
		 
	}
	
	public static void main(String[] args) {
		
		
		new ClientSide();
		
		upload_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					
				      // check connection to server
				     handler.Connect(host_field.getText(), Integer.parseInt(port_field.getText()));
				     
				 	 // try to upload file if exist and server is running 
				     handler.uploadFile(path_file.getText());
						
				 }catch( java.lang.NumberFormatException numEx)
				   {
					   JOptionPane.showMessageDialog(null , "please enter real port", "\r\n" + 
								"server Exception" ,2);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		

	}
}
	
	