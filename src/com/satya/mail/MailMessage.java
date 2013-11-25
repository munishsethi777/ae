package com.satya.mail;

import java.io.Serializable;

/**
 *
 * Object to represent a MailMessage being queued to the JMS queue.
 */
public class MailMessage implements Serializable {
	private String receiver;
	private String sender;
	private String msg;
	private String subject;
	private String userEmailId;
	private boolean isCopyMe;
	/**
	 * Constructor taking receiver, subject and msg information
	 * about the message to be sent. Sender is always fixed to
         * MailUtil.SENDER.
	 * @param receiver
	 * @param subject
	 * @param msg
	 */
	public MailMessage(String receiver,String subject,String msg){
            setReceiver(receiver);
            setSender(Mailer.DISPATCHER_MAIL_ID);
            setSubject(subject);
            setMsg(msg);
	}
	/**
	 * @return
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @return
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param string
	 */
	public void setMsg(String string) {
		msg = string;
	}

	/**
	 * @param string
	 */
	public void setReceiver(String string) {
		receiver = string;
	}

	/**
	 * @param string
	 */
	public void setSender(String string) {
		sender = string;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer("MAIL MESSAGE: ");
		sb.append(" :receiver = ");
		sb.append(receiver);
		sb.append(" :sender = ");
		sb.append(sender);
		sb.append(" :subject = ");
		sb.append(subject);
		sb.append(" : msg = ");
		sb.append(msg);
		return sb.toString();
	}
	/**
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param string
	 */
	public void setSubject(String string) {
		subject = string;
	}
	public String getUserEmailId() {
		return userEmailId;
	}
	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}
	public boolean getIsCopyMe() {
		return isCopyMe;
	}
	public void setIsCopyMe(boolean isCopyMe) {
		this.isCopyMe = isCopyMe;
	}

}
