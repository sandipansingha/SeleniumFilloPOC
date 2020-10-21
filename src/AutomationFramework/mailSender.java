package AutomationFramework;

import java.util.*;
import javax.mail.*;  
import javax.mail.internet.*;
import javax.activation.*;

/**
 * @Description This is the Master Class to handle all report mail communication related activity
 * @author sandipan.singha
 *
 */
public class mailSender 
{
	public static void email(Properties prop)
	{  		  
	  String to=prop.getProperty("recipient");//change accordingly  
	  final String user=prop.getProperty("senderUser");//change accordingly  
	  final String password=prop.getProperty("senderPass");//change accordingly  
	   
	  //1) get the session object     
	  Properties properties = System.getProperties();  
	  properties.setProperty(prop.getProperty("mailServerType"), prop.getProperty("smtpServer"));  
	  properties.put("mail.smtp.auth", "true"); 
	  properties.put("mail.smtp.starttls.enable", "true");
	  properties.put("mail.smtp.port", "587");
	  
	  Session session = Session.getDefaultInstance(properties,  
	   new javax.mail.Authenticator() {  
	   protected PasswordAuthentication getPasswordAuthentication() {  
	   return new PasswordAuthentication(user,password);  
	   }  
	  });  
	     
	  //2) compose message     
	  try{  
	    MimeMessage message = new MimeMessage(session);  
	    message.setFrom(new InternetAddress(user));  
	    message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	    message.setSubject("Message Aleart");  
	      
	    //3) create MimeBodyPart object and set your message text     
	    BodyPart messageBodyPart1 = new MimeBodyPart();  
	    messageBodyPart1.setText("This is message body");  
	      
	    //4) create new MimeBodyPart object and set DataHandler object to this object      
	    MimeBodyPart messageBodyPart2 = new MimeBodyPart();  
	  
	    String filename = prop.getProperty("ZipArchive")+ReportManager.zipName+".zip";//change accordingly  
	    DataSource source = new FileDataSource(filename);  
	    messageBodyPart2.setDataHandler(new DataHandler(source));  
	    messageBodyPart2.setFileName(filename);  
	     
	     
	    //5) create Multipart object and add MimeBodyPart objects to this object      
	    Multipart multipart = new MimeMultipart();  
	    multipart.addBodyPart(messageBodyPart1);  
	    multipart.addBodyPart(messageBodyPart2);  
	  
	    //6) set the multiplart object to the message object  
	    message.setContent(multipart );  
	     
	    //7) send message  
	    Transport.send(message);  
	    ReportManager.logger.info("message sent....");	     
	   }
	  catch (MessagingException ex)
	  {
		ReportManager.logger.info("\n"+ex.toString()+"\n");
	  }
	}
}
