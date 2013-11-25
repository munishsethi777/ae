package com.satya.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.satya.ApplicationContext;
import com.satya.Managers.ApplicationMgr;

/**
 * Application Lifecycle Listener implementation class ServletListener
 * 
 */
public class ServletListener implements ServletContextListener {
	// private Logger logger = Logger.getLogger("logger");
	//
	//
	/**
	 * Default constructor.
	 */
	public ServletListener() {

	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		prepareApplicationContext(arg0);
	}

	private void prepareApplicationContext(ServletContextEvent arg0){
		ServletContext ctx = arg0.getServletContext();
		ApplicationContext applicationContext = new ApplicationContext();
		ApplicationMgr appMgr = new ApplicationMgr();
		appMgr.ContextLoader(ctx, applicationContext);

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}

}
