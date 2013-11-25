package com.satya.tests;

import org.junit.Test;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;
import com.satya.mail.MailTemplateFactory;
import com.satya.mail.Mailer;
import com.satya.mail.MailerI;


public class EmailTest {
	
  
	
    @Test
	public void testEmail(){
		try{
			MailerI mailer = new Mailer(new MailTemplateFactory(),"","",0,"",false);
			User user = new User();
			user.setUsername("bgaheer");
			user.setName("BS");
			user.setEmail("baljeetgaheer@gmail.com");
			user.setName("baljeet"); 
			user.setProject(getProject());
			mailer.sendUserSignUpNotification(user);
		}catch(Exception e){
			String msg = e.getMessage();
		}
		
	}
    private Project getProject(){
    	Project project = new Project();
    	project.setAddress("address");
    	project.setName("New Games");
    	project.setAdmin(getAdmin());
    	return project;
    }
    private Admin getAdmin(){
    	Admin admin = new Admin();
    	admin.setName("Baljeet Gaheer");
    	return admin;
    }
}
