package com.satya.mail;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;

public class Mailer implements MailerI {
	private final static Logger mailLogger = Logger.getLogger(Mailer.class);
	private static Session smtpSession = null;

	private String username;
	private String password;
	private int port;
	private String smtpServer;
	private boolean useSsl;

	private boolean running;

	public final static String DISPATCHER_MAIL_ID = "";

	private MailTemplateFactory templateFactory;

	private List<MailMessage> _msgQueue = new ArrayList<MailMessage>();

	/**
	 * Constructor taking the MailTemplateFactory which the Mailer uses to
	 * obtain the right mail template. It also takes the configuration like stmp
	 * server and the username password to connect to that smtp server.
	 * 
	 * @param templateFactory
	 * @param username
	 * @param password
	 * @param port
	 * @param smtpServer
	 * @param useSsl
	 */
	public Mailer(MailTemplateFactory templateFactory, String username,
			String password, int port, String smtpServer, boolean useSsl) {
		this.templateFactory = templateFactory;
		this.username = username;
		this.password = password;
		this.port = port;
		this.smtpServer = smtpServer;
		this.useSsl = false;
	}

	public MailTemplateFactory getTemplateFactory() {
		return this.templateFactory;
	}

	public void start() {
		if (useSsl) {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Properties props = System.getProperties();

			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", smtpServer);

			props.put("mail.smtp.host", smtpServer);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);

			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");

			smtpSession = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username,
									password);
						}
					});
			smtpSession.setDebug(true);
		} else {
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpServer);
			smtpSession = Session.getDefaultInstance(props, null);
		}
		running = true;
		Thread t = new Thread(new AsynchMailer());
		t.start();
	}

	public void sendMessages(List<MailMessage> messages) {
		for (MailMessage mailMsg : messages) {
			queueMessage(mailMsg);
		}
	}

	public void sendMessage(MailMessage mailMsg) {
		queueMessage(mailMsg);
	}

	private synchronized void queueMessage(MailMessage mm) {
		_msgQueue.add(mm);
	}

	private synchronized List<MailMessage> getQueuedMessages() {
		if (_msgQueue.size() > 0) {
			List<MailMessage> msgs = new ArrayList<MailMessage>();
			msgs.addAll(_msgQueue);
			return msgs;
		} else {
			return null;
		}
	}

	private synchronized void removeMessage(MailMessage mm) {
		_msgQueue.remove(mm);
	}

	public void sendUserSignUpNotification(User user) {
		MailContext mc = new MailContext();
		mc.setUser(user);
		MailMessage message = templateFactory.getUserSignUpEmail(mc);
		sendMessage(message);
	}

	public void sendAdminSignUpNotification(Admin admin) {
		MailContext mc = new MailContext();
		mc.setAdmin(admin);
		MailMessage message = templateFactory.getAdminSignUpEmail(mc);
		sendMessage(message);
	}

	public void sendCampaignAlertNotification(User user) {
		MailContext mc = new MailContext();
		mc.setUser(user);
		MailMessage message = templateFactory.getCampaignAlertEmail(mc);
		sendMessage(message);
	}

	public void destroy() {
		running = false;
	}

	private class AsynchMailer implements Runnable {

		public void run() {
			// we don't want to stop if there are mail messages in the queue.
			// may we need to persist this queue to avoid messages getting lost
			// due to application shutdown.

			while (running || getQueuedMessages().size() > 0) {
				mailLogger.info("Downloading messages");
				List<MailMessage> msgs = getQueuedMessages();
				if (msgs != null) {
					mailLogger.info("Downloaded " + msgs.size() + " messages");
					sendMessages(msgs);
				}
				try {
					// sleep for a minute
					Thread.currentThread().sleep(60 * 500);
				} catch (Exception e) {
					mailLogger
							.error("System Error during seding email in mailer "
									+ e);
				}
			}
		}

		private void sendMessages(List<MailMessage> msgs) {
			final String username = "baljeetgaheer@gmail.com";
			final String password = "xxxxxxx";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username,
									password);
						}
					});
			for (MailMessage mailMsg : msgs) {
				try {

					Message message = new MimeMessage(session);
					message
							.setFrom(new InternetAddress("from-email@gmail.com"));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(mailMsg.getReceiver()));
					message.setSubject(mailMsg.getSubject());
					message.setContent(mailMsg.getMsg(), "text/html");
					Transport.send(message);
					System.out.println("Done");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				} finally {
					removeMessage(mailMsg);
				}

				mailLogger.info("Sending Mail Notification with subject: "
						+ mailMsg.getSubject().toString());
				removeMessage(mailMsg);
			}
		}

	}

}