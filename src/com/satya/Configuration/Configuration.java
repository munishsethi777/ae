/*package com.satya.Configuration

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.User;
import com.satya.Persistence.UserDataStoreI;
import com.satya.Utils.SecurityUtil;
public class Configuration {
    private Map _configMap = new HashMap();
    public final static String FEEDBACK_LINK = "feedbackLink";
    public final static String PROFILE_LINK = "profileLink";
    public final static String CONTACT_FORM_MAIL_ID= "contactFormMailId";
    public final static String API_SERVER_URL = "apiServerURL";
    public final static String SANDBOX_API_SERVER_URL = "sandboxApiServerURL";
    public final static String EPS_SERVER_URL = "epsServerURL";
    public final static String APP_ID = "applicationId";
    public final static String DEV_ID = "developerId";
    public final static String CERT_ID = "certificationId";   
    public final static String EBAY_TOKEN = "ebayToken";
    public final static String EBAY_USER = "ebayUser";
    public final static String EBAY_PASS = "ebayPassword";
    public final static String IMAGES_PATH = "imagePath";
    public final static String WEB_ROOT_PATH = "webRootPath";
    public final static String IMAGES_URL = "imagesURL";
    public final static String MAX_NO_CALLS = "maxNoCalls";
    public final static String ADMIN_MAIL_ID = "adminMailId";
    public final static String ADMIN_MAIL_PASSWD = "adminMailPassword";
    public final static String SMTP_SERVER = "smtpServer";
    public final static String APP_URL = "applicationURL";
    public final static String EBAY_ITEM_URL = "itemViewURL";
    public final static String RUNAME = "runame";
    public final static String LAST_SYNCH_TIME = "lastSynchronization";
    public final static String IMAGE_QUOTA_MB = "imageQuotaMB";
    public final static String NOTIFICATIONS_OFF = "notificationsOff";
    
    public final static String CATEGORY_TREE_VERSION = "categoryTreeVersion";
    public final static String CATEGORY_TREE_VERSION_US = "categoryTreeVersionUS";
    public final static String CATEGORY_TREE_VERSION_EBAYMOTORS = "categoryTreeVersioneBayMotors";
    
    
    public final static String ATTRIBUTES_VERSION = "attributeSystemVersion";
    public final static String ATTRIBUTES_VERSION_US = "attributeSystemVersionUS";
    public final static String ATTRIBUTES_VERSION_EBAY_MOTORS = "attributesXmlSystemVersioneBayMotors";
    
    public final static String ATTRIBUTES_XML_VERSION = "attributeXmlVersion";
    public final static String ATTRIBUTES_XML_VERSION_US = "attributeXmlVersionUS";
    public final static String ATTRIBUTES_XML_VERSION_EBAY_MOTORS = "attributesXmlVersioneBayMotors";
    
    public final static String EBAYOPTIONS_SYNC_DATE = "ebayOptionsSynchDate";
    
    public final static String CATEGORY_TREE_VERSION_UK = "categoryTreeVersionUK";
    public final static String MAX_SYNCH_THREADS = "maxSynchThreads";
    public final static String PAYPAL_REC_MAIL = "paypalReceiverEmail";
    public final static String SECRET = "secret";
    public final static String SMTPPORT = "smtpPort";
    public final static String NOTIFICATIONMAILFROM = "notificationMailFrom";
    public final static String TOKEN_SIGNIN_URL = "tokenSignInUrl";    
    public final static String NOTIFICATIONMAILID="notificationMailId";
    public final static String NOTIFICATIONMAILPASSWORD="notificationMailPassword";
    public final static String NOTIFICATIONPOPSERVER="notificationPOPServer";
    public final static String NOTIFICATIONPOPPORT="notificationPOPPort";
    public final static String NOTIFICATIONMAILUSESSL="notificationMailUseSSL";
    public final static String BUFFER_PERIOD = "bufferPeriod";
    public final static String TRIAL_DAYS = "trialDays";
    //public final static String MONTHLY_FEE = "monthlyFee";
    public final static String ORIGINPOSTALCODE = "originPostalCode";
    public final static String TOKEN_RENEWAL_URL = "tokenRenewalUrl";
    public final static String NOTIFICATION_INTERVAL = "notificationInterval";
    public final static String ISCYCLEDFEEDBACK = "isCycledFeedback";
    public final static String APPPATH = "appPath";
    public final static String ATTRIBUTES_XML_PATH = "attributesXmlPath";
    public final static String IS_IMAGES_ACROSS_EBAY_ACCOUNT = "isImagesAcrossEbayAccount";
    //new plans based configs.
    public final static String USER_REGISTRATION_PLAN = "userRegistrationPlan";
    public final static String USER_SUBSCRIPTION = "userSubscription";
    
    public final static String FREE_IMAGE_QUOTA = "freeImageQuotaMB";
    public final static String BASIC_IMAGE_QUOTA = "basicImageQuotaMB";
    public final static String UNLIMITED_IMAGE_QUOTA = "unlimitedImageQuotaMB";
    public final static String PREMIUM_IMAGE_QUOTA = "premiumImageQuotaMB";
    
    public final static String FREE_MAX_LISTINGS = "freeMaxListings";
    public final static String BASIC_MAX_LISTINGS = "basicMaxListings";
    
    public final static String BASIC_SUBSCRIPTION = "basicSubscription";
    public final static String UNLIMITED_SUBSCRIPTION = "unlimitedSubscription";
    public final static String PREMIUM_SUBSCRIPTION = "premiumSubscription";
    
    
    //setting indicates whether to show exception stack trace in error.jsp
    public final static String SHOW_EXCEPTION="showExceptionStack";
    
    
    // last time getSellerEvents() was called
    public  final static String LAST_ITEMS_SYNC_TIME = "lastItemsSynch";
    
    //last time getSellerTxns() was called
    public  final static String LAST_TXNS_SYNC_TIME = "lastTxnSynch";    
    //last time getFeedbacks() was called
    public  final static String LAST_FEEDBACK_SYNC_TIME = "lastFeedbackSynch";
    //synchronization status as per earlier ebay transactions(before user getting signedup)
    public final static String SYNCHRONIZATION_STATUS ="synchronizationStatus";
   
    public final static String LISTINGHTMLTEMPLATE_HTMLPATH = "listingHtmlTemplatePath";
    public final static String LISTINGHTMLTEMPLATE_FULL_HTMLPATH = "listingtemplatefullpath";
    public final static String LISTINGHTMLTEMPLATE_IMAGE_PATH = "listingHtmlTemplateImagePath";
    private  final static String SANDBOX_PREFIX = "sandbox";
    private final static String PRODUCTION_PREFIX = "production";
    public final static SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss");
    public final static String CHANGE_PASSWORD_URL = "chagePasswordURL";
    public final static String WATERMARK_LABEL = "watermarkLabel";
    public final static String WATERMARK_LOCATION = "watermarkLocation";
    public final static String LOGSDAYS	= "logsdays";
    public final static String UNIQUEITEMIDLASTCOUNT = "uniqueItemIdLastCount";
    public final static String ENABLE_PAYMENTGENERATION = "enablePaymentGeneration";
    
    public final static String PAYPALURL = "paypalURL";
    public final static String IS_SMSENABLED ="isSMSEnabled";
    public final static String IS_ACCOUNTSUMMARY_ENABLED ="isAccountSummaryEnabled";
    
    public final static String IMAGE_VIEWER_URL = "imageViewerUrl";
    public final static String IMAGES_LAYOUT_TEMPLATES = "imagesLayoutTemplates";
    public final static String EBAY_LISTINGS_URL="ebayListingsURL";
    
    public final static String SCROLLING_GALLERY_DEFAULT_COLOR_THEME = "scrollingGalleryDefaultColorTheme";
    public final static String SCROLLING_FEEDBACK_GALLERY_DEFAULT_COLOR_THEME = "scrollingFeedbackGalleryDefaultColorTheme";
    
    public final static String PAYPAL_API_USERNAME = "paypalAPIUsername";
    public final static String PAYPAL_API_PASSWORD = "paypalAPIPassword";
    public final static String PAYPAL_API_SIGNATURE = "paypalAPISignature";
    public final static String PAYPAL_API_ENV = "paypalAPIEnv";
    public final static String PAYPAL_API_SUBJECT = "paypalAPISubject";
    
    public final static String IS_SUBSCRIPTION = "isSubscription";
    public final static String PAYMENTMODE = "paymentMode";
    public final static String SELLER_LISTINGS_URL = "sellerListingsApiURL";	
    
    public final static String DISABLE_ATTR_XML_SYNCH = "developer.disableAttributesXmlSynch";
    public final static String DISABLE_ATTRIBUTES_SYNCH = "developer.disableAttributesMappingSynch";
    public final static String DISABLE_CATTREE_SYNCH = "developer.disableCategoryTreeSynch";
    public final static String IS_AUCTIONLINC_BANNER_DISABLED = "isAuctionLincBannerDisabled";
    public final static String DEFAULT_LISTING_LOCATION = "defaultListingLocation";
    public final static String EBAY_ITEM_URL_FR = "itemViewUrlFR";
    public final static String EBAY_VIEW_URL_DE = "itemViewUrlDe";
    public final static String IS_DELETE_NOTIFICATION = "isDeleteNotification";
    public final static String IS_UPON_RECEIPT_OF_PAYMENT  = "isUponReceiptOfPayment";
    public final static String PACKING_SLIP_FOMRAT = "packingSlipFormat";
    public final static String SHIPPING_LABEL_FOMRAT = "shippingLabelFormat";
    public final static String INVOICE_NUMBER_PATTERN = "invoiceNumberPattern";
    public final static String NOTIFICATION_MESSAGES_PATH= "notificationMessagesPath";
    public final static String SYSTEM_GENERATED_EBAYUSER_ID= "systemGeneratedEBayUserID";
    public final static String LAST_SELECTED_EBAYUSERID = "lastSelectedEbayUserId";
    public final static String PREVIEW_SCHEDULE_PAGESIZE = "previewSchedulePageSize";
    public final static String LAST_IMPORT_FORMAT = "lastImportFormat";
    public final static String LAST_IMPORT_UPLOAD_ON_EBAY = "lastImportUploadOnEbayBool";
    
    
    *//** configuration option being asked whether to launch/ schedule a copy of the original
     * or launch the original one.
     *//*
    public final static String IS_REMOVE_FROM_PENDING = "isRemoveFromPending";
    
    
    public final static SimpleDateFormat sdfDateOnly = new SimpleDateFormat("MM.dd.yyyy");
    public final static long SYSTEM_USER_SEQ = 1;
    private long _configSeq;  
    private long _user;
    private String _configkey,_configValue;
    

    private final static String SLASH = "/";
    

	 public Configuration(){}
 
	 public  Date getDateConfig(String value){
	        String lastSynchStr = value; //(String)_configMap.get(propName);
	        if(lastSynchStr == null){
	        	return null;
	        }
	        Date lastSynch = null;
	        try{
	        	lastSynch = sdf.parse(lastSynchStr);
	        }catch(ParseException pe){

	        }
	        return lastSynch;
	    	
	    }

	
	public static Boolean getSystemConfigBool(String configKey){
		String booleanConfig = getSystemConfig(configKey,"false");
		return Boolean.parseBoolean(booleanConfig);
	}
	
	public static Boolean getUserConfigBool(User user,String configKey){
		return getUserConfigBool(user,configKey,false);
	}
	
	public static Boolean getUserConfigBool(User user,String configKey,boolean defaultVal){
		String booleanConfig = getUserConfig(user, configKey);
		if(booleanConfig!= null && !booleanConfig.equals("")){
			return Boolean.parseBoolean(booleanConfig);
		}
		return defaultVal;
	}
	
	public static String getSystemConfig(String configKey){
		return getSystemConfig(configKey,null);
	}
	
	public  void setSystemUser(long userSeq){
		_user = userSeq;
	}
	public long getSystemUser(){
		return this._user;
	}
	public void setConfigkey(String key){
		_configkey = key;
	}
	public String getConfigkey(){
		return this._configkey;
	}
	public void setConfigValue(String value){
		_configValue = value;
	}
	public String getConfigValue(){
		return this._configValue;
	}
	*//**
	 * Reads a row from Configuration table for a given configKey. If the key is found,
	 * the stored value is returned, otherwise the defaultValue passed is returned.
	 * @param configKey
	 * @param defaultValue
	 * @return
	 *//*
	public static String getSystemConfig(String configKey, String defaultValue){		
//		ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
//		configKey = getActualConfigKey(configKey);
//		String val = cds.findConfigValue(SYSTEM_USER_SEQ,configKey);		  
//		return val == null ? defaultValue : val; 
	}
	
	private static String getActualConfigKey(String configKey){
		// a system property by name auctionlinc.enc.sandbox must be set to true
		Boolean isSandbox = Boolean.getBoolean(IConstants.SANDBOX_KEY);
		if(isSandbox){
			configKey = SANDBOX_PREFIX + "." + configKey;
		}else{
			configKey = PRODUCTION_PREFIX + "." + configKey;
		}
		return configKey;
	}
	public  String getTokenRenewalUrl(){
		return getSystemConfig(TOKEN_RENEWAL_URL);
	}
	
	public String getApplicationURL(){
		return getSystemConfig(APP_URL);
	}
	public String getChangePasswordURL(){
		return getSystemConfig(CHANGE_PASSWORD_URL);
	}

	*//**
	 * Stores the passed in system config key and config value in the db.
	 * @param configKey
	 * @param configValue
	 *//*
	public static void setSystemConfig(String configKey, String configValue){
		//find system user	
		UserDataStoreI uds = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
    	User system = uds.findByUsername(IConstants.GLOBAL_USER);
		setUserConfig(system,getActualConfigKey(configKey),configValue);
	}
	public static void setEncryptedConfig(String configKey ,String configValue){
		configValue=SecurityUtil.encrypt(configValue);
		UserDataStoreI uds = ApplicationContext.getApplicationContext().getDataStoreMgr().getUserDataStore();
		User system = uds.findByUsername(IConstants.GLOBAL_USER);
		setUserConfig(system,getActualConfigKey(configKey),configValue);
	}
	*//**
	 * Stores config key and value for a given user in the db.
	 * @param user
	 * @param configKey
	 * @param configValue
	 *//*
	public static void setUserConfig(User user,String configKey, String configValue){	   
	   ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
	   cds.save(user,configKey,configValue);
	
	}
	public static void setUserConfig(User user,String configKey, int configValue){	   
		   ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		   String valueStr = String.valueOf(configValue);
		   cds.save(user,configKey,valueStr);
		}
	
	*//**
	 * Returns config key and value for a given user from the db. Null is returned 
	 * if no config is found for this key or if the value in the db is null.
	 * @param user
	 * @param configKey
	 * @param configValue
	 * @return
	 *//*
	public static String getUserConfig(User user,String configKey){
		 ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		 String configValue = cds.findConfigValue(user.getSeq(),configKey);		  
		 return configValue;
	}
	public static String getUserConfig(User user,String configKey,String defaultValue){
		 ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		 String configValue = cds.findConfigValue(user.getSeq(),configKey);		  
		 return configValue == null ? defaultValue : configValue; 
	}
	
	public static void cleanConfigurations(User user){
		ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		cds.cleanConfigurations(user.getSeq());
	}
	
	public static int getUserConfigInt(User user,String configKey){
		 ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		 String configValue = cds.findConfigValue(user.getSeq(),configKey);
		
	    	if(configValue == null){
	    		return 0;
	    	}
	    	try{
	    		int value = Integer.parseInt(configValue);
	    		return value;
	    	}catch(Exception e){
	    		logger.error("Error during getUserConfigInt for configKey: " + configKey, e);
	    		return 0;
	    	}
		 
	}
	public static int getUserConfigInt(User user,String configKey,int defaultValue){
		 ConfigurationDataStore cds = ConfigurationDataStore.getInstance();
		 String configValue = cds.findConfigValue(user.getSeq(),configKey);
		
	    	if(configValue == null){
	    		return defaultValue;
	    	}
	    	try{
	    		int value = Integer.parseInt(configValue);
	    		return value;
	    	}catch(Exception e){
	    		logger.error("Error during getUserConfigInt for configKey: " + configKey, e);
	    		return 0;
	    	}
		 
	}
	
	
	 
     *   public final static String CATEGORY_TREE_VERSION_US = "categoryTreeVersionUS";
    public final static String CATEGORY_TREE_VERSION_EBAYMOTORS = "categoryTreeVersioneBayMotors";
    
    
    public final static String ATTRIBUTES_VERSION_US = "attributeSystemVersionUS";
    public final static String ATTRIBUTES_XML_VERSION_US = "attributeXmlVersionUS";
    
    public final static String ATTRIBUTES_VERSION_EBAY_MOTORS = "attributesXmlSystemVersioneBayMotors";
    public final static String ATTRIBUTES_XML_VERSION_EBAY_MOTORS = "attributesXmlVersioneBayMotors";
    
    public final static String CATEGORY_TREE_VERSION_UK = "categoryTreeVersionUK";
     
	private static  Logger logger = LoggingFactory.getLogger(Configuration.class.getName());
    public static Date getSystemConfigurationDate(String configName){
    	String valueStr = Configuration.getSystemConfig(configName);
    	if(valueStr == null){
    		return null;
    	}
    	try{
    		Date date = sdfDateOnly.parse(valueStr);
    		return date;
    	}catch(Exception e){
    		logger.error("Error during getSystemConfigurationDate for configName: " + configName, e);
    		return null;
    	}
    }
    public static Integer getSystemConfigurationInt(String configName){
    	String valueStr = Configuration.getSystemConfig(configName);
    	if(valueStr == null){
    		return 0;
    	}
    	try{
    		int value = Integer.parseInt(valueStr);
    		return value;
    	}catch(Exception e){
    		logger.error("Error during getSystemConfigurationInt for configName: " + configName, e);
    		return 0;
    	}
    }
    public static String getSystemConfiguration(Site site,String configName){
    	String siteSpecificConfigName = configName + site.getSiteCodeType().value() ;
    	return Configuration.getSystemConfig(siteSpecificConfigName);
    }
    
    public static void setSystemConfiguration(String configName,Date value){
    	String dateStr = sdfDateOnly.format(value);
    	Configuration.setSystemConfig(configName, dateStr);
    }
    public static void setSystemConfiguration(String configName,int value){
    	String dateStr = String.valueOf(value);
    	Configuration.setSystemConfig(configName, dateStr);
    }
    
    
    public static void setSystemConfiguration(Site site,String configName,String value){
    	String siteSpecificConfigName = configName + site.getSiteCodeType().value() ;
    	Configuration.setSystemConfig(siteSpecificConfigName,value);
    }
	
}






*/