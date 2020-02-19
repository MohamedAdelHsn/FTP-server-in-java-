package com.jftp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class FTPGUI extends JFrame {


     Object column[]={"Name","IP Address","File Name" , "Size" ,"Time","Byte Received"}; 
        	
	 static  InputStream in;
	 static OutputStream out;
	 static InetAddress inetAddress;
	 static Socket socket;
     static  DataInputStream clientData ;
	 static ServerSocket server_socket = null;	 
	 static String fileName;
	 static long size , estimatedTime = 0;
	 static int count = 0;
	 static boolean isDir = false;
	 static File file;
	 static JTextField port_field;
	 static JTextField path_field;
	 static boolean isrunning = true;
	 static Thread th;

	 
	 public FTPGUI()
	 {
		 				 
		 initComponents();
		 
	 }
	 
	 public void initComponents()
	 {
		 
		 JPanel newPanel = new JPanel(null);
		   newPanel.setBorder(BorderFactory.createTitledBorder(null , "Server Info" 
				   , TitledBorder.CENTER, TitledBorder.TOP ,null,Color.WHITE));
		   newPanel.setBackground(new Color(48,57,82));
		   newPanel.setBounds(90, 40 , 750 , 210);
		   
		   // (label_server.getY()+label_server.getHeight())-40
		   
		   //*** status ***//	   
		   label_status = new JLabel("Server status  : ");
		   label_status.setBounds(30, 30 , 140 , 40);
		   label_status.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   label_status.setForeground(Color.WHITE);
		   	
		   label_running = new JLabel("pending ....");
		   label_running.setBounds(170, label_status.getY(), 200, 40);  
		   // label_server.getY()+label_server.getHeight())+10
		   label_running.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   label_running.setForeground(new Color(52, 152, 219));
		   
		   
		   run_server = label_running.getText();
		   str_length = run_server.length();
		   
		   tm = new Timer(400, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				counter++;
				if(counter > str_length)
				{
					  label_running.setText("");
					  counter  = 0;				
				}
				else
				{
				
				     counter++;
				     label_running.setText(run_server.substring(0 ,counter));
				
				}
				
			}
		});
		  
		   tm.start();
		
		   //** Server Waiting time **//	   
		   label_waiting_time  = new JLabel("Time :  ");
		   label_waiting_time.setBounds(label_status.getX()+label_status.getWidth()+label_running.getWidth()*2-55		   
				   ,label_status.getY()-3, 170 , 40);
		   label_waiting_time.setFont(new Font("Consolas" , Font.PLAIN , 15));  // Comic Sans MS
		   label_waiting_time.setForeground(Color.WHITE);	   
		   
		   // Server time_val	   
		   label_time_val = new JLabel("0");
		   label_time_val.setBounds( label_waiting_time.getX()+50			   
				   , label_waiting_time.getY(), 170 , 40);
		   label_time_val.setFont(new Font("Comic Sans MS" , Font.PLAIN , 15));
		   label_time_val.setForeground(Color.YELLOW);	   
		   

		   // timer for server
		   tm = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				valTime_counter++;
				if(mints == 59 && valTime_counter == 59)
				{
					++hour;
					mints = 0;
					valTime_counter= 0;
					label_time_val.setText(Integer.toString(hour)+":"+Integer.toString(mints) +":"+Integer.toString(valTime_counter)+"/s");
					
					
				}
				if(valTime_counter == 59)
				{
					++mints;
					valTime_counter= 0;
					label_time_val.setText(Integer.toString(mints) +":"+Integer.toString(valTime_counter)+"/s");
				}
				
				
				else {
					
				    label_time_val.setText(Integer.toString(hour)+":"+Integer.toString(mints) +":"+Integer.toString(valTime_counter)+"/s");
				
				}
				
			}
		});
		  
		   tm.start();
		   
		    //*** client hostName ***//	   
		   host_name = new JLabel("Clients status : ");
		   host_name.setBounds(30 , (label_status.getY()+ label_status.getHeight())-13 ,170 ,40);
		   host_name.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   host_name.setForeground(Color.WHITE);
		   
		   
		   host_name_val = new JLabel("NO host name connected !");
		   host_name_val.setBounds(host_name.getX()+host_name.getWidth()-12, host_name.getY(), 200, 40);
		   host_name_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   host_name_val.setForeground(Color.YELLOW);
		   
		   //*** client connection ***//	   
		   label_clientConn = new JLabel("Client Ip Address  : ");
		   label_clientConn.setBounds(30, (host_name.getY()+  host_name.getHeight())-13, 190 , 40);
		   label_clientConn.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   label_clientConn.setForeground(Color.WHITE);	   
		   
		   
		   client_conn_val = new JLabel("NO Connection !");
		   client_conn_val.setBounds(label_clientConn.getX()+label_clientConn.getWidth()-10, label_clientConn.getY(), 170, 40);
		   client_conn_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   client_conn_val.setForeground(Color.YELLOW);
		   
		   
		   //*** File name ***//	   
		   file_name_labl = new JLabel("File name : ");
		   file_name_labl.setBounds(30, (label_clientConn.getY()+label_clientConn.getHeight())-13, 120 , 40);
		   file_name_labl.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   file_name_labl.setForeground(Color.WHITE);	   
		   
		   
		   file_name_val = new JLabel("No file !");
		   file_name_val.setBounds( file_name_labl.getX()+ file_name_labl.getWidth()-22 ,file_name_labl.getY(), 170, 40);
		   file_name_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   file_name_val.setForeground(Color.YELLOW);
		   
		   
		   //*** File Size ***//	   
		   file_size = new JLabel("File size : ");
		   file_size.setBounds(30, ( file_name_labl.getY()+ file_name_labl.getHeight())-13, 120 , 40);
		   file_size.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   file_size.setForeground(Color.WHITE);	   
		   
		   
		   file_size_val = new JLabel("null");
		   file_size_val.setBounds(file_size.getX()+ file_size.getWidth()-22 ,file_size.getY(), 170, 40);
		   file_size_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   file_size_val.setForeground(Color.YELLOW);
		  			
		   
		   //***  Received Files ***//	   	
		   label_fileRecieved  = new JLabel("Received Files : ");
		   label_fileRecieved.setBounds(30, (file_size.getY()+ file_size.getHeight()-13), 170 , 40);
		   label_fileRecieved.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   label_fileRecieved.setForeground(Color.WHITE);	 
		   
		   //***  Received Files value ***//	   
		   file_recieved_val = new JLabel("NO file received !");
		   file_recieved_val.setBounds(label_fileRecieved.getX()+label_fileRecieved.getWidth()-25,  label_fileRecieved.getY()
				   , 340, 40);
		   file_recieved_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   file_recieved_val.setForeground(Color.YELLOW);
		   
		   //*** Buffer Label ***//	   
		   buffer_labl = new JLabel("Server buffer size : ");
		   buffer_labl.setBounds(label_waiting_time.getX() ,host_name.getY(), 190 , 40);
		   buffer_labl.setFont(new Font("Consolas" , Font.PLAIN , 15));  // Comic Sans MS
		   buffer_labl.setForeground(Color.WHITE);	   
		   
		   
		   buffer_label_val = new JLabel("16");
		   buffer_label_val.setBounds(buffer_labl.getX()+165			   
				   ,buffer_labl.getY(), 170 , 40);
		   buffer_label_val.setFont(new Font("Comic Sans MS" , Font.PLAIN , 15));
		   buffer_label_val.setForeground(Color.YELLOW);
		   
		   //*** Number of clients connected ***//	   
		   no_clients = new JLabel("Uploaded files : ");
		   no_clients.setBounds(buffer_labl.getX() ,label_clientConn.getY(), 170 , 40);
		   no_clients.setFont(new Font("Consolas" , Font.PLAIN , 15));  // Comic Sans MS
		   no_clients.setForeground(Color.WHITE);	   
		   
		   //*** value of clients connected ***//
		   
		   no_clients_val = new JLabel(""+number_of_clients);
		   no_clients_val.setBounds(no_clients.getX()+135			   
				   ,no_clients.getY(), 170 , 40);
		   no_clients_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   no_clients_val.setForeground(Color.YELLOW);	
		   
		   
		   //*** data received  ***//	   
		   byte_sent = new JLabel("Bytes received : ");
		   byte_sent.setBounds(no_clients.getX() ,file_name_labl.getY(), 170 , 40);
		   byte_sent.setFont(new Font("Consolas" , Font.PLAIN , 15));  // Comic Sans MS
		   byte_sent.setForeground(Color.WHITE);	   
		   
		   //*** value of clients connected ***//	   
		   byte_sent_val = new JLabel(""+number_of_clients);
		   byte_sent_val.setBounds(byte_sent.getX()+135			   
				   ,byte_sent.getY(), 170 , 40);
		   byte_sent_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   byte_sent_val.setForeground(Color.YELLOW);
		   
		    //*** loading time ***//	   
		   loading_time = new JLabel("Time taken : ");
		   loading_time.setBounds(byte_sent.getX() ,file_size.getY(), 140 , 40);
		   loading_time.setFont(new Font("Consolas" , Font.PLAIN , 15));  // Comic Sans MS
		   loading_time.setForeground(Color.WHITE);	   
		   
		   //*** loading time value ***//
		   loading_time_val = new JLabel("0.0 ms");
		   loading_time_val.setBounds(loading_time.getX()+110			   
				   ,loading_time.getY(), 170 , 40);
		   loading_time_val.setFont(new Font("Consolas" , Font.PLAIN , 15));
		   loading_time_val.setForeground(Color.YELLOW);	  
		   	   
		   
		   JPanel config_panel = new JPanel(null);
		   config_panel.setBorder(BorderFactory.createTitledBorder(null , "Configuration" 
				   , TitledBorder.CENTER, TitledBorder.TOP ,null,Color.WHITE));
		   config_panel.setBackground(new Color(48,57,82));
		   config_panel.setBounds(90, label_fileRecieved.getY()+label_fileRecieved.getHeight()+50, 750 , 70);
		   
		   //** Table **//
		   DefaultTableModel dfT = new DefaultTableModel();
		   jt = new JTable(dfT);
		   dfT.addColumn(column[0]);
		   dfT.addColumn(column[1]);
		   dfT.addColumn(column[2]);
		   dfT.addColumn(column[3]);
		   dfT.addColumn(column[4]);
		   dfT.addColumn(column[5]);	   
		   jt.setRowHeight(25);
		   jt.setEnabled(false);
		   JScrollPane sp=new JScrollPane(jt);    
		   sp.setBounds(90,  config_panel.getY()+config_panel.getHeight()+10 , 750, 190);
	   
		    
		   JLabel path_files = new JLabel("PATH : ");
		   path_files.setBounds(30 , (config_panel.getHeight()/4)-4 , 80 ,50);
		   path_files.setForeground(Color.WHITE);
		   
		   path_field = new JTextField();
		   path_field.setBounds(path_files.getWidth()-6 ,path_files.getY()+14 , 150 , 21);	   
		   	   
		   
		   JLabel port_number = new JLabel("PORT : ");
		   port_number.setBounds(path_field.getX()+path_field.getWidth()+30 , (config_panel.getHeight()/4)-4 , 65 ,50);
		   port_number.setForeground(Color.WHITE);
		   
		   port_field = new JTextField();
		   port_field.setBounds(port_number.getX()+port_number.getWidth()-16 ,path_field.getY() , 80 , 21);
		   
		   btn_start = new JButton("Start");
		   btn_start.setBounds(port_field.getX()+port_field.getWidth()+13 , port_field.getY(), 80 , 23);		
		   btn_start.setFocusPainted(false);
		   btn_start.setFont(new Font("Consolas" , Font.PLAIN , 12));
		   
		   
		   btn_stop = new JButton("Stop");
		   btn_stop.setBounds(btn_start.getX()+ btn_start.getWidth()+10 ,  btn_start.getY(), 90 , 23);		
		   btn_stop.setFocusPainted(false);
		   btn_stop.setFont(new Font("Consolas" , Font.PLAIN , 12));
		   btn_stop.setEnabled(false);
		   
		   btn_showAll = new JButton("ShowAll");
		   btn_showAll.setBounds(btn_stop.getX()+ btn_stop.getWidth()+10 ,  btn_stop.getY(), 90 , 23);		
		   btn_showAll.setFocusPainted(false);
		   btn_showAll.setFont(new Font("Consolas" , Font.PLAIN , 12));
	   		   
		   config_panel.add(path_files);
		   config_panel.add(path_field);
		   config_panel.add(port_number);
		   config_panel.add(port_field);
		   config_panel.add(btn_start);
		   config_panel.add(btn_stop);
		   config_panel.add(btn_showAll);
		   

		   newPanel.add(label_status);	   
		   newPanel.add(label_clientConn);
		   newPanel.add(label_fileRecieved);
		   newPanel.add(label_waiting_time);
		   newPanel.add(label_running);
		   newPanel.add(label_time_val);
		   newPanel.add(client_conn_val);
		   newPanel.add(file_recieved_val);
		   newPanel.add(host_name);
		   newPanel.add(host_name_val);
	       newPanel.add(file_name_labl);
		   newPanel.add(file_name_val);
		   newPanel.add(file_size);
		   newPanel.add(file_size_val);
		   newPanel.add(buffer_labl); 
		   newPanel.add(buffer_label_val);	
		   newPanel.add(no_clients);
		   newPanel.add(no_clients_val);
		   newPanel.add(byte_sent);
		   newPanel.add(byte_sent_val);
		   newPanel.add(loading_time);
		   newPanel.add(loading_time_val);
		
		 
		   add(newPanel);
		   add(config_panel);
		   getContentPane().add(sp);
		   	 	   	   
		   
		   getContentPane().setBackground(new Color(48,57,82)); // 48, 57, 82  new Color(48, 57, 82)
		   setSize(950, 590);
		   setLayout(null);
		   setDefaultLookAndFeelDecorated(true);
		   setDefaultCloseOperation(EXIT_ON_CLOSE);	
		   setResizable(false);
		   setTitle("OWNFTP SERVER");
		   setVisible(true);
		 
		 
	 }
	 
	 
	 // defining class fields
	 
	    private JLabel label_status;
		private JLabel label_clientConn;
		private JLabel label_fileRecieved;
		private JLabel label_waiting_time;
		static JLabel label_running;
		private JLabel label_time_val;	
	    static JLabel client_conn_val , file_recieved_val;
		static JLabel host_name ,host_name_val;
		static JLabel file_name_labl ,file_name_val;
		static JLabel file_size ,file_size_val;
		private JLabel  buffer_labl , buffer_label_val;
		static JLabel no_clients , no_clients_val;
		static JLabel byte_sent , byte_sent_val;
		static JLabel loading_time , loading_time_val;
		static JButton btn_start;
		static JButton btn_stop;
		static JButton btn_showAll;
		JPanel newPanel;
		static JTable jt;
		JLabel path_files;
		JButton browse_btn;
		int str_length = 0;
		static Timer tm;
		static String run_server = null;
		static int number_of_clients = 0;
		int counter = 0;	
		//Timer tm2 , ip_tm;
		int valTime_counter = 0;
		int mints = 0 
				, hour = 0;
	 
	
	
}
