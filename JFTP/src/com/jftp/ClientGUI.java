package com.jftp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


 public class ClientGUI extends JFrame {

	 
	 
	public ClientGUI() 
	{
		
		initComponents(); // creating frame using method.
		
	}


	private void  initComponents() 
	{
		
				
		select_labl = new JLabel("Select File : ");
		select_labl.setBounds(30 , 20 , 80 , 40);
		select_labl.setFont(new Font("" , Font.PLAIN , 13));		
		
		path_file = new JTextField();
		path_file.setBounds(select_labl.getX()+select_labl.getWidth(), select_labl.getY()+7 ,250 , 25);
		path_file.setFont(new Font("Consolas" , Font.PLAIN  , 13));
		path_file.setEditable(false);
		path_file.setForeground(Color.BLACK);
		
		browse_btn = new JButton("browse");
		browse_btn.setBounds(path_file.getX()+path_file.getWidth()+5 , path_file.getY(), 80 , 25);		
		browse_btn.setFocusPainted(false);
		browse_btn.setFont(new Font("Consolas" , Font.PLAIN , 12));
		
		// choose file to upload
		browse_btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser file_chooser = new JFileChooser();
				file_chooser.showOpenDialog(browse_btn);
				path_file.setText(file_chooser.getSelectedFile().getAbsolutePath());
				
			}
		});
		
		

		 newPanel = new JPanel(null);
		 newPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));
		 newPanel.setBounds(30  ,select_labl.getY()+40  , 337+browse_btn.getWidth() , 180);
		 
		 
		 JLabel label1 = new JLabel("Server Host : ");
		 label1.setFont(new Font("" , Font.PLAIN , 15));
	     JLabel label2 = new JLabel("Server Port : ");
	     label2.setFont(new Font("" , Font.PLAIN , 15));
	     JLabel label3 = new JLabel("Username :    ");
	     label3.setFont(new Font("" , Font.PLAIN , 15));
	     JLabel label4 = new JLabel("Password  :   ");
	     label4.setFont(new Font("" , Font.PLAIN , 15));	     

	     
	     label1.setBounds(38 , 35 , 120 , 25);
	     label2.setBounds(38 , label1.getY()+30 ,120 ,25);
	     label3.setBounds(38,  label2.getY()+30 , 120 , 25);
	     label4.setBounds(38 , label3.getY()+30 , 120 , 25 ); 
		
	     host_field = new JTextField();
	     port_field = new JTextField();
	     host_field.setBounds(label1.getWidth()+12 , label1.getY()+2, 140, 21);
	     port_field.setBounds(label2.getWidth()+12 ,label2.getY()+2 ,140 , 21);
	     
	     // if you want to set username & password
	     text_username = new JTextField();
		 text_username.setBounds(label3.getWidth()+12 , label3.getY()+2 , 140 , 21);
		 		 
		 text_password= new  JPasswordField();
		 text_password.setBounds(label4.getWidth()+13 , label4.getY()+2 , 140 , 21);
	     
	     newPanel.add(label1); newPanel.add(label2); newPanel.add(label3); newPanel.add(label4);
	     newPanel.add(host_field); newPanel.add(port_field); 	     
	     
	     // username & password
	     //newPanel.add(text_username);
	     //newPanel.add(text_password);
	     				
		 upload_btn = new JButton("Upload");
		 upload_btn.setBounds(path_file.getX()+(path_file.getWidth()/3), path_file.getY()+200+35 ,95, 30);
		 upload_btn.setFocusPainted(false);
		 upload_btn.setFont(new Font("Consolas" , Font.PLAIN ,12));
		 upload_btn.setForeground(Color.BLACK);
		
         
	     // add components to Frame
		 add(select_labl);
		 add(path_file);
		 add(browse_btn);
		 add(upload_btn);		
		 add(newPanel);		
		 // set frame properties
		 setTitle("Client Administrator");
		 setSize(540 , 380);
		 setLayout(null);
		 setDefaultCloseOperation(EXIT_ON_CLOSE);
		 setDefaultLookAndFeelDecorated(true);
         setLocationRelativeTo(null);
         setResizable(false);
		 setVisible(true);		
		
	}
	
	
	
	private JLabel select_labl;
	static JTextField path_file;
	private JButton browse_btn;
	static JButton upload_btn;
	JLabel file_name_lbl , file_name_val;
	JLabel file_type_lbl , file_type_val;
	JPanel file_info_panel;
	//JLabel space_label;
	JPanel newPanel;
    JPanel config_Panel; 
	static JTextField host_field;
    static JTextField port_field;
	JTextField text_username;
	JPasswordField text_password;
   	
		
 }
	

