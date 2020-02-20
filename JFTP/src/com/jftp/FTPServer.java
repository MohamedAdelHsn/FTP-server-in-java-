package com.jftp;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class FTPServer extends FTPGUI{

	 @SuppressWarnings("unused")
	private static File server_data; 		
	
	 public FTPServer() 
	 {
		 server_data = new File("data.txt");	
		 
	 }

	public static void main(String[] args) throws IOException {

		
		new FTPServer();
		btn_start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				tm.stop();
				label_running.setForeground(Color.YELLOW);
				run_server = "Running ....";
				tm.start();
				path_field.setEditable(false);
				port_field.setEditable(false);
				
				try {
					
					openServerSocket(path_field.getText() ,Integer.parseInt(port_field.getText()));
					btn_start.setEnabled(false);
					btn_stop.setEnabled(true);
					
				}catch(IllegalArgumentException illegalEx) {
					
					  JOptionPane.showMessageDialog(null , "please enter real port", "\r\n" + 
								"Server Exception" ,2);
					  
						path_field.setEditable(true);
						port_field.setEditable(true);

				} catch (IOException e1) {
					
					 JOptionPane.showMessageDialog(null , "please enter real path to continue", "\r\n" + 
								"Server Exception" ,2);
				}
				
			}
		}); 
		
		
		btn_stop.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				tm.stop();
				run_server = "Pending ....";
				label_running.setForeground(new Color(52, 152, 219));
				tm.start();
				path_field.setEditable(true);
				port_field.setEditable(true);
				
			    btn_stop.setEnabled(false);
			    btn_start.setEnabled(true);
			   
			    th.stop();
			    try {
			    	
					server_socket.close();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		btn_showAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				 DefaultTableModel model =  (DefaultTableModel)jt.getModel();
				 model.setRowCount(0);  // clear data from JTable
				 try {
					 
					   printDataServer();
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
			}
		});


 
	}

	@SuppressWarnings("static-access")
	private static void openServerSocket(String path , int port) throws IOException {
				
		try {
				// open server socket
			   server_socket = new ServerSocket(port);
			
			}catch(java.net.BindException bind_ex)
			{
				 JOptionPane.showMessageDialog(null , "\r\n" + 
							"the server is already running ! ", "ServerIsWorked" ,2);
				 System.exit(0);			
			}
			 
			 // support multiusers to upload files
			 Runnable runnable = new Runnable() {
				
				@Override
				public void run() {
					
					while(true) {
					 	
						try {
							
					         // waiting to accept any socket 
					             socket = server_socket.accept();

						     // if new client connected increase counter by 1
						     number_of_clients++;	
							 
							 // getting ip address of client  [1]***
						     host_name_val.setText(socket.getInetAddress().getLocalHost().getHostName()+" Connected");
						     client_conn_val.setText(socket.getInetAddress().getLocalHost().getHostAddress());						
							
						     no_clients_val.setText(""+number_of_clients);
							 // connect to stream socket to read data        
						     in = socket.getInputStream();				        
						             
						      // represent file name , size from clientSocket 
						     clientData  = new DataInputStream(in); 
						             
						      // write file name , size to server 				            	
						     fileName = clientData.readUTF();
						     size = clientData.readLong();

			    
					             out = new FileOutputStream(path+fileName);
						             
						             
						     file_name_val.setText(fileName);
						     file_size_val.setText(""+Math.ceil((size/1024.0))+" KB");
						             
						       // getting data from client using byte array 
						     byte[] buffer = new byte[16*1024];  
							    
						     
						      // measure time of loading file. 
						       long startTime = System.nanoTime();					  
						    
						       // writing file data to server 
						       while(size>0 &&((count =in.read(buffer))!= -1))
						       {						    	   
						            out.write(buffer, 0, count);
						            
						       }
						       
						       // get real time 
						       estimatedTime =  System.nanoTime() - startTime;
						       
						       String time =  estimatedTime * Math.pow(10, -6)+"";
						       time  = time.substring(0, 5);
						       
						       loading_time_val.setText(time+" ms");
						       
						       byte_sent_val.setText(Math.ceil(size/1024.0)+" KB");
						       
						         // print file recevied and ip address of client  [2]***
						       file_recieved_val.setText(fileName);
						        
						        // write server info in table  
						       DefaultTableModel model =  (DefaultTableModel)jt.getModel();
						       Object [] obj_data = new Object[] {host_name_val.getText() ,client_conn_val.getText()
						    	
						    		   , fileName ,  file_size_val.getText() , loading_time_val.getText() ,byte_sent_val.getText()};
						       
						       model.addRow(obj_data);
						       // store server info in txt file 
						       WriteDataToServer(obj_data);
						       
						         // closing socket					        
						      in.close();
					              clientData.close();
					              out.close();
					              socket.close();
					            				             
						             
							    }catch (IOException e) {
										
							           host_name_val.setText("NO Host Name connected !");
								   client_conn_val.setText("No Client Connected");
								   file_name_val.setText("No File !");
								   file_size_val.setText("null");
								   file_recieved_val.setText("No File Recevied !");
								   no_clients_val.setText((--number_of_clients)+"");
								  

							    }
											   
						
					    }
					
				}
			};
			
		    th = new Thread(runnable);
		    th.start();
	}
	
	
	private static void WriteDataToServer(Object[] obj) throws IOException 
	{
	
		BufferedWriter buffer_writer = new BufferedWriter(
                   new FileWriter(server_data, true)  //Set true for append mode
             );  
				
		int size = 0;
		while(size < obj.length)
		{
			if(size == obj.length-1)
			{
				buffer_writer.write((String)obj[size]);
			}
			
			else {
			buffer_writer.write(obj[size]+",");
			}
			size++;
		}
		
		buffer_writer.newLine(); 
		buffer_writer.close();

		
		
	}
	
	private static void printDataServer() throws IOException
	{
		
		FileReader reader = new FileReader(server_data);
		BufferedReader buffer_reader = new BufferedReader(reader);
			
		String line;
		DefaultTableModel model =  (DefaultTableModel)jt.getModel();
	        String[] delimeter_line; 
		
		while((line = buffer_reader.readLine()) != null) 
		{
			delimeter_line = line.split(",");
			model.addRow(new Object[] {
					
					delimeter_line[0] , delimeter_line[1] , delimeter_line[2] , delimeter_line[3] , delimeter_line[4]
							, delimeter_line[5]
			});
			
		}
		
		buffer_reader.close();
		reader.close();
		
		
	}
	
	

}
