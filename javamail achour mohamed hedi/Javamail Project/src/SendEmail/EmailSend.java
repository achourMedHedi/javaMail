package SendEmail;
import java.util.*
;

import javax.activation.DataHandler;

import javax.activation.DataSource;

import javax.activation.FileDataSource;

import javax.mail.*;

import javax.mail.internet.*;

import javax.mail.internet.MimeMessage;
import java.sql.*;

import java.util.Date;

public class EmailSend {
   
 public static void main(String args[]){
     
   try{
           
 String host ="smtp.gmail.com" ;
      
      String user = "mha2506@gmail.com";
   
         String pass = "25062644";
    
        String from = "mha2506GLJavamail@gmail.com";
  
          ArrayList <String> to = new ArrayList<String>();
 
           String subject ="";
          
  String messageText ="";
            boolean sessionDebug = false;

            Properties props = System.getProperties();

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(props, null);
            mailSession.setDebug(sessionDebug);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(from));
            
            Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/javamail","root","");
			Statement stmt = con.createStatement();
		    String req = "select emailAbonne from abonnes";
		    ResultSet rs=stmt.executeQuery(req);

		    if(rs.next()) {
		    	to.add( rs.getString(1));
		    	while(rs.next())
		    	{
		    		to.add( rs.getString(1));
		    	}
		    	      
			    InternetAddress[] address = new InternetAddress[to.size()];
			    for (int i = 0; i < to.size(); i++) {
			        address[i] = new InternetAddress(to.get(i));
			    }
			    System.out.println("Succ");
		    	msg.setRecipients(Message.RecipientType.TO, address);
		    	
		    	 req = "select * from catalogues ORDER BY `catalogues`.`idCat` DESC limit 1 ";
				 rs=stmt.executeQuery(req);
				    if(rs.next()) {
				    	
			    subject =rs.getString("themeCat");
			    messageText =rs.getString("dataCreation");
	            msg.setSubject(subject);
				msg.setSentDate(new Date());
	            msg.setText(messageText);
	            // Part two is attachment
	            DataSource source = new FileDataSource(rs.getString("pathFichierCatalogue"));
	            msg.setDataHandler(new DataHandler(source));
	            msg.setFileName(rs.getString("pathFichierCatalogue"));


	           Transport transport=mailSession.getTransport("smtp");
	           transport.connect(host, user, pass);
	           transport.sendMessage(msg, msg.getAllRecipients());
	           transport.close();
	           System.out.println("message send successfully");	
				    }
				    else
				    	System.out.println("no catalogues..");	
		    	
		    	
		    }else
		    	 System.out.println("you have no clients..");
		    
		    con.close();
           
        }catch(Exception ex)
        {
            System.out.println(ex);
        }

    }
}
