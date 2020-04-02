package clientserver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.StringTokenizer;
import javax.swing.*;

import clientserver.MyClient.Read;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class loginMyClient implements ActionListener {
	static	String iD,clientID="";
	static	DataInputStream din;
	static	DataOutputStream dout;
	static	DefaultListModel dlm;
static int k=0;		
		static JLabel l1,l2,l3;
		static JTextField msg;
		static JTextArea msgBox;
		static JButton send,SelectAll, disconnect;
		static JFrame f;
		static JList clist;
		static JPanel p;
		
		 static JLabel l;
		   static JTextField id;
		   static JButton cnn;
		   static JFrame fr;
		   
		   loginMyClient(int i){
				 fr=new JFrame();
				 
				 l=new JLabel();
				 l.setBounds(50,100,50,30);  
				 l.setText("ID :");
				 
				 id=new JTextField();
				 id.setBounds(100,100,250,30);
				 
				 cnn=new JButton("Connect");  
				 cnn.setBounds(150,200,100,30); 
				 cnn.addActionListener(this);
				 
				 fr.add(cnn);fr.add(l);fr.add(id);
				    fr.setSize(400,400);  
				    fr.setLayout(null);  
				    fr.setVisible(true); 
			 }
		   
		   
		   
		   
		   
		   
		loginMyClient(){
			
			f=new JFrame();
			l1=new JLabel();
		    l1.setBounds(25,25,100,30);  
		    l1.setText("Hello");
		    
		    l2=new JLabel();
		    l2.setBounds(120,25,100,30);  
		    l2.setText("name");
		    
		    SelectAll=new JButton("SelectAll");  
		    SelectAll.setBounds(310,25,120,30); 
		    SelectAll.addActionListener(this);
		    
		    disconnect=new JButton("Disconnect");  
		    disconnect.setBounds(175,325,100,30); 
		    disconnect.addActionListener(this);
		    
		    msgBox=new JTextArea();
		    msgBox.setBounds(25,70,200,200);
		    
		    p=new JPanel();
		    p.setBounds(235,70,200,150);
		    
		    
		    dlm=new DefaultListModel();
		   
		    clist=new JList();
		   
		  
		    p.add(clist);
		    
		    l3=new JLabel();
		    l3.setBounds(25,280,50,30);  
		    l3.setText("Msg");
		    
		    msg=new JTextField();
		    msg.setBounds(70,280,260,30);
		    
		    send=new JButton("Send");  
		    send.setBounds(350,280,80,30); 
		    send.addActionListener(this);
		    
		    f.add(l1);f.add(l2);f.add(msgBox);f.add(msg);f.add(send);f.add(l3);f.add(p);f.add(SelectAll);f.add(disconnect);
		    f.setSize(460,400);  
		    f.setLayout(null);  
		    f.setVisible(true);	
		}
		
//		private void formWindowClosing(java.awt.event.WindowEvent evt) {
//			String i="EX1T";
//			try {  dout.writeUTF(i);
//			       f.dispose();
//			}catch(IOException ex) {
//			  //	Logger.getLogger(MyClient.class.getName().log(Level.SEVERE,null,ex));
//				ex.printStackTrace();
//			}
//		}
//		private void clistValueChanged(javax.swing.event.ListSelectionEvent evt) {
//			clientID=(String)clist.getSelectedValue();
//			
//		}
//		
			
		 public void actionPerformed(ActionEvent e){ 
			
			 if(e.getSource()==disconnect) {
					try {
						dout.writeUTF("EX1T");
						f.dispose();
						
					}catch(Exception ex) {
						
						ex.printStackTrace();
					}
			}
			 
			 if(e.getSource()==SelectAll) {
				k=1;
		}
			
			 if(e.getSource()==cnn) {
				    
			       try {
			        	  int port=3537;
			        	  String ID=id.getText();
			        	  Socket s=new Socket("localhost",port);
			        	  DataInputStream di=new DataInputStream(s.getInputStream());
			        	  DataOutputStream dou = new DataOutputStream(s.getOutputStream());
			        	  dou.writeUTF(ID);
			        	  String i=new DataInputStream(s.getInputStream()).readUTF();
			        	  if(i.equals("you are already registered")) {
			        		  JOptionPane.showMessageDialog(f , "you are already LoggedIn");
			        	  }else {
			        		      new loginMyClient(ID , s);
			        		      fr.dispose();
			        	  }
			          }catch(Exception ex) {
			        	  ex.printStackTrace();
			          }
				 
				 
			}
			
			if(e.getSource()==send) {
			try {
				if(k!=1)
					clientID=(String)clist.getSelectedValue();
				else
					clientID="";
				String m=msg.getText(); 
				String mm=m;
				String CI=clientID;
				if(!clientID.isEmpty()) {
					m="!@#$%^"+CI+":"+mm;
					dout.writeUTF(m);
					msg.setText("");
					msgBox.append("< you send to "+CI+">"+mm+"\n");									
				} 	else {
					dout.writeUTF(m);
					msg.setText("");
					msgBox.append("< you to all >"+mm+"\n");
				}
			}catch(Exception ex) {
				JOptionPane.showMessageDialog(f, "user does not exist anymore");
			}
			   k=0;
			}
			
		}
		
		loginMyClient(String i,Socket s){
			new loginMyClient();
			iD=i;
			try {
			       dlm=new DefaultListModel();
			       dlm.addElement("All");
			       clist.setModel(dlm);
			       l2.setText(iD);
			       din=new DataInputStream(s.getInputStream());
			       dout =new DataOutputStream(s.getOutputStream());
			       new Read().start();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
	class Read extends Thread{
		 
		public void run() {
			while(true) {
				try {
					String m=din.readUTF();
					if(m.contains("$%^&*(")) {
					m=m.substring(6);	
					dlm.clear();
					StringTokenizer st= new StringTokenizer(m,",");
					while(st.hasMoreTokens()) {
						String u=st.nextToken();
						if(!iD.equals(u))
							dlm.addElement(u);
					     }
					}else {
						msgBox.append(""+m+"\n");
					}
					
				}catch(Exception ex) {
					break;
				}
			}
		}
	}
	 
	public static void main(String [] args) {
		new loginMyClient(3);
	}
 
}
