package com.satya;


import org.apache.log4j.Logger;


public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler   {


	private static Logger log = Logger.getLogger(CustomExceptionHandler.class);

	 @Override
	  public void uncaughtException(Thread t, Throwable ex) {
	    log.error("Uncaught exception in thread: " + t.getName(), ex);
	  }
	

}
