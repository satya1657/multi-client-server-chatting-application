package clientserver;


import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.swing.*;

public class MyServer {
      
	ServerSocket ss;
	HashMap clientcoll=new HashMap();
	
static JLabel sstatus;
static JTextArea msgBox;
	
	public MyServer() {
	     
	JFrame f=new JFrame("MyServer");	
	
	sstatus=new JLabel();
	sstatus.setBounds(180,25,200,30);  
	sstatus.setText("Status");
	    
	    msgBox=new JTextArea();
	    msgBox.setBounds(25,60,370,300);
	    
	    f.add(sstatus);f.add(msgBox);
	    f.setSize(450,450);  
	    f.setLayout(null);  
	    f.setVisible(true);
	    
	}
	
	public MyServer(int port) {
		
		new MyServer();
		
		  try {
			  ss=new ServerSocket(port);
			  this.sstatus.setText("Server Started...");
			  new ClientAccept().start();
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
	}
	
class ClientAccept extends Thread{
		public void run() {
			while(true) {
				try {
					     Socket s=ss.accept();
					     String i=new DataInputStream(s.getInputStream()).readUTF();
					     if(clientcoll.containsKey(i)) {
					    	 DataOutputStream dout =new DataOutputStream(s.getOutputStream());
					    	 dout.writeUTF(i+" is already LoggedIn");
					    	// JOptionPane.showMessageDialog(null , "you are already LoggedIn");
					     }else {
					    	 clientcoll.put(i, s);
					    	 msgBox.append(i+" joined!"+"\n");
					    	 DataOutputStream dout =new DataOutputStream(s.getOutputStream());
					    	 dout.writeUTF("");
					    	 new msgRead(s,i).start();
					    	 new PrepareClientList().start();
					     }
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
}

class msgRead extends Thread{
	Socket s;
	String ID;
	msgRead(Socket s,String ID){
		this.ID=ID;
		this.s=s;
	}
	
	public void run() {
		while(!clientcoll.isEmpty()) {
			try {
				   String i=new DataInputStream(s.getInputStream()).readUTF();
				   if(i.contains("EX1T")) {
					   clientcoll.remove(ID);
					   msgBox.append(ID+" removed"+"\n");
					   new PrepareClientList().start();
					   Set k=clientcoll.keySet();
					   Iterator itr=k.iterator();
					   while(itr.hasNext()) {
						   String key=(String)itr.next();
						   
						   if(!key.equalsIgnoreCase(ID)) {
							   try {
								   new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF("<"+ID+" to all >"+i);
								   
							   }catch(Exception e) {
								   clientcoll.remove(key);
								   msgBox.append(key+" removed");
								   new PrepareClientList().start();
							   }
						   }
						   
					   }
				   }else if(i.contains("!@#$%^")) {
					   i=i.substring(6);
					   StringTokenizer st =new StringTokenizer(i,":");
					   String id = st.nextToken();
					   try {
						   new DataOutputStream(((Socket)clientcoll.get(id)).getOutputStream()).writeUTF("<"+ID+" to "+id+">"+i);
					   }catch(Exception e) {
						   clientcoll.remove(id);
						   msgBox.append(id+" removed");
						   new PrepareClientList().start();
					   }
					   
				   }
				   else {
					   Set k=clientcoll.keySet();
					   Iterator itr = k.iterator();
					   while(itr.hasNext()) {
							String key=(String)itr.next();
							try {
								new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF("<"+ID+" to all"+">"+i);
							}catch(Exception e) {e.printStackTrace();}
							}
				   }
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}


class PrepareClientList extends Thread{
	
	public void run() {
		try { 
             
			String ids="";
			Set k=clientcoll.keySet();
			Iterator itr = k.iterator();
			while(itr.hasNext()) {
				String key=(String)itr.next();
				ids+=key+",";
			}
		if(ids.length()!=0) 
			ids=ids.substring(0, ids.length()-1);
			
		itr=k.iterator();
		while(itr.hasNext()) {
			String key=(String)itr.next();
			try {
				new DataOutputStream(((Socket)clientcoll.get(key)).getOutputStream()).writeUTF("$%^&*("+ids);
			}catch(Exception e) {
				clientcoll.remove(key);
				msgBox.append(key +" removed");}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	  }
	}
	
	public static void main(String[] args) {
		    int port=3537;
           new MyServer(port);
	}

}
