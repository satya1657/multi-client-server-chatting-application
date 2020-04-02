 package clientserver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
public class UserLogIn implements ActionListener {
   static JLabel l;
   static JTextField id;
   static JButton cnn;
   static JFrame f;
	
   UserLogIn(){
		 f=new JFrame();
		 
		 l=new JLabel();
		 l.setBounds(50,100,50,30);  
		 l.setText("ID :");
		 
		 id=new JTextField();
		 id.setBounds(100,100,250,30);
		 
		 cnn=new JButton("Connect");  
		 cnn.setBounds(150,200,100,30); 
		 cnn.addActionListener(this);
		 
		 f.add(cnn);f.add(l);f.add(id);
		    f.setSize(400,400);  
		    f.setLayout(null);  
		    f.setVisible(true); 
	 }
   
   public void actionPerformed(ActionEvent e){ 
          try {
        	  int port=2876;
        	  String ID=id.getText();
        	  Socket s=new Socket("localhost",port);
        	  DataInputStream din=new DataInputStream(s.getInputStream());
        	  DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        	  dout.writeUTF(ID);
        	  String i=new DataInputStream(s.getInputStream()).readUTF();
        	  if(i.equals("you are already registered")) {
        		  JOptionPane.showMessageDialog(f , "you are already LoggedIn");
        	  }else {
        		      new MyClient(ID , s);
        		      f.dispose();
        	  }
          }catch(Exception ex) {
        	  ex.printStackTrace();
          }
           
   }
       
   
	public static void main(String[] args) {
                  new UserLogIn();
	}
 
}
