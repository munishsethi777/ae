package com.satya.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;


public class EmailTemplate {
	Logger logger = Logger.getLogger(Mailer.class);
	private String _templatename, _subject, _templatetext;
	private int _templateSeq;
	private long _userseq;
	private boolean _isSystem;
	private MailTemplateType mailTemplateType;
	private TemplateKeyword[] keywords;
	private String templateDisplayName;

	private String DECORATOR_START = "{";
	private String DECORATOR_END = "}";
	private String decoratedMessage;
	


	public final static String NOT_AVAILABLE = "--";

	public final static String TEMPLATENAME[] = {
			"WINNING_BIDDER_NOTIFICATION", "PAYMENT_REMINDER",
			"PAYMENT_RECEIVED", "SHIPPING_NOTIFICATION", "ACCOUNT_ACTIVATION",
			"FEEDBACK_REMINDER", "PERSONALIZED_EMAIL,FORGOTTEN_PASSWORD" };

	
	public boolean getIsSystem() {
		return _isSystem;
	}

	public void setIsSystem(boolean system) {
		_isSystem = system;
	}

	public int getTemlpateSeq() {
		return _templateSeq;
	}

	public void setTemplateSeq(int templateseq) {
		_templateSeq = templateseq;
	}

	public String getTemplateName() {
		return _templatename;
	}

	public void setTemplateName(String templatename) {
		_templatename = templatename;
	}

	public String getSubject() {
		return _subject;
	}

	public void setTemplateDisplayName(String name) {
		this.templateDisplayName = name;
	}

	public String getTemplateDisplayName() {
		return this.templateDisplayName;
	}

	public void setSubject(String subject) {
		_subject = subject;
	}

	public String getTemplateText() {
		return _templatetext;
	}

	public void setTemplateText(String templatetext) {
		_templatetext = templatetext;
	}

	public long getUserSeq() {
		return _userseq;
	}

	public void setUserSeq(long userseq) {
		_userseq = userseq;
	}

	public MailMessage getMailMessage(MailContext mailContext) {
		TemplateKeyword[] keywords = getTemplateKeyword();
		Map<String, String> substs = new HashMap<String, String>();
		for (TemplateKeyword keyword : keywords) {
			try {
				String keywordValue = resolveTemplateKeyword(keyword,
						mailContext);
				if (keyword.isLink()) {
					keywordValue = "<a target='_blank' href='" + keywordValue
							+ "'>" + keywordValue + "</a>";
				}
				substs.put(keyword.getKey(), keywordValue);
			} catch (Exception nsme) {
				logger.error("Error resolving keyword " + keyword, nsme);
				substs.put(keyword.getKey(), NOT_AVAILABLE);
			}
		}
		String body = getSubstitutedString(_templatetext, substs);
		String subject = null;
		if (_subject != null && !_subject.equals("")) {
			subject = getSubstitutedString(_subject, substs);
		}
		// receiver will be filled in by the caller
		return new MailMessage(null, subject, body);
	}

	public String resolveTemplateKeyword(TemplateKeyword keyword,
			MailContext mailContext) throws Exception {
		Object target = mailContext;
		for (String token : keyword.getMappingTokens()) {
			Object nextTarget = PropertyUtils.getProperty(target, token);
			if (nextTarget != null) {
				if (!nextTarget.equals("")) {
					target = nextTarget;
				} else {
					return NOT_AVAILABLE;
				}
			} else {
				return NOT_AVAILABLE;
			}
		}
		String targetValue = target.toString();
//		if (keyword == TemplateKeyword.ITEM_LAUNCH_TIME
//				|| keyword == TemplateKeyword.SCHEDULED_DATE) {
//			//targetValue = JSONUtils.exportAttributeValue(target, true);
//		} else {
//		//	targetValue = JSONUtils.exportAttributeValue(target, false);
//		}
		return targetValue;
	}

//	public String getTestTemplateText(String templateText, User user) {
//		TemplateKeywordsDataStore TKDS = ApplicationContext
//				.getApplicationContext().getDataStoreMgr()
//				.getTemplateKeywordsDataStore();
//		Map<String, String> map = TKDS.getKeywordsTestValues(this);
//		String signature = user.getEmailSignature();
//		if (signature != null) {
//			signature = signature.replaceAll("\r\n", "<br/>");
//		}
//		map.put("SIGNATURE", signature);
//
//		String updatedTemplateText = this.getSubstitutedString(templateText,
//				map);
//
//		return updatedTemplateText;
//	}

	private String getSubstitutedString(String templateString,
			Map<String, String> substitutions) {
		StringBuffer sb = new StringBuffer(templateString);
		Set<Map.Entry<String, String>> entries = substitutions.entrySet();
		for (Map.Entry<String, String> entry : entries) {
			String token = entry.getKey();
			String replacement = entry.getValue();
			if (token != null && replacement != null) {
				try {
					replaceToken(sb, token, replacement);
				} catch (Exception e) {
					logger.error("Error occured while replacing token(" + token
							+ ") for emailtemplate. ", e);
				}
			} else {
				logger
						.error("Either token or replace is null while replacing token for email templates. "
								+ "Token :"
								+ token
								+ ", Replacement:"
								+ replacement);
			}
		}
		return sb.toString();
	}

	private void replaceToken(StringBuffer string, String token,
			String replacement) {
		String decToken = DECORATOR_START + token;
		decToken += DECORATOR_END;
		while (true) {
			int startIndex = string.indexOf(decToken);
			if (startIndex > 0) {
				string.replace(startIndex, startIndex + decToken.length(),
						replacement);
			} else {
				break;
			}
		}
	}

	

	public TemplateKeyword[] getTemplateKeyword() {
		return keywords;
	}

	public void setTemplateKeyword(TemplateKeyword keyword[]) {
		this.keywords = keyword;
	}

	public void setDecoratedTemplateText(String message) {
		decoratedMessage = message;
	}

	public String getDecoratedTemplateText() {
		return decoratedMessage;
	}

}