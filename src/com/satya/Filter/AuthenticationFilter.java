package com.satya.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
public class AuthenticationFilter implements Filter {

	private List<String> publicURLs = null;
	private List<String> publicExtensions = null;
	private List<String> userURLs = null;
	private List<String> adminURLs = null;
    public AuthenticationFilter() {
    	publicURLs = new ArrayList<String>();
    	publicExtensions = new ArrayList<String>();
    	userURLs = new ArrayList<String>();
    	adminURLs = new ArrayList<String>();
    	
    	publicURLs.add("index.jsp");
    	publicURLs.add("header.jsp");
    	publicURLs.add("userHeader.jsp");
    	publicURLs.add("menu.jsp");
    	publicURLs.add("includeJars.jsp");
    	publicURLs.add("includeJS.jsp");
    	publicURLs.add("User?action=login");
    	publicURLs.add("signup.jsp");
    	publicURLs.add("register.jsp");
    	publicURLs.add("login");
    	publicURLs.add("logout");
    	publicURLs.add("signup");
    	publicURLs.add("player.jsp");
    	publicURLs.add("userLogin.jsp");
    	
    	publicExtensions.add("html");
    	publicExtensions.add("css");
    	publicExtensions.add("js");
    	publicExtensions.add("swf");
    	publicExtensions.add("txt");
    	
    	userURLs.add("userDashboard.jsp");
    	
    	adminURLs.add("dashboard.jsp");
    	adminURLs.add("projects.jsp");
    	adminURLs.add("users.jsp");
    	adminURLs.add("usergroups.jsp");
    	adminURLs.add("questions.jsp");
    	adminURLs.add("gameTemplates.jsp");
    	adminURLs.add("games.jsp");
    	adminURLs.add("sets.jsp");
    	adminURLs.add("campaigns.jsp");
    	adminURLs.add("results.jsp");
    	adminURLs.add("myAccount.jsp");
    }
    Logger log = Logger.getLogger(AuthenticationFilter.class);
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		String pageUrl = req.getRequestURI();
		String action = request.getParameter("action");
		if(action == null){
			action ="";
		}
		List<String> errorMsgs = new ArrayList<String>();
		String pageExtension = "";
		try{
			if(pageUrl!=null){
				pageUrl = pageUrl.substring(pageUrl.lastIndexOf("/")+1, pageUrl.length());
				pageExtension = pageUrl.substring(pageUrl.lastIndexOf(".")+1);
			}
			if(pageUrl!=null && !publicExtensions.contains(pageExtension)){
				//pageUrl = pageUrl.substring(pageUrl.lastIndexOf("/")+1, pageUrl.length());
				//already done above.
				
				if(!pageUrl.equals("") && !publicURLs.contains(pageUrl) && !publicURLs.contains(action)){
					//if its not a public URL, then check if user logged in or not
					Admin admin = (Admin) req.getSession().getAttribute(IConstants.loggedInAdmin);
					User user = (User) req.getSession().getAttribute(IConstants.loggedInUser);
					//check if projects are there or not
					
					if(admin != null || user != null){
						if(adminURLs.contains(pageUrl)){
							if(ApplicationContext.getApplicationContext().getAdminWorkspaceProject(req) == null){
								request.getRequestDispatcher("projects.jsp").forward(request, response);
							}else{
								chain.doFilter(request, response);
							}
						}else{
							chain.doFilter(request, response);
						}
						
					}else{
						setSourceURL(req);
						errorMsgs.add(IConstants.err_pleaseLogin);
						request.setAttribute(IConstants.errMessages, errorMsgs);
						request.getRequestDispatcher(IConstants.admin_loginIndex).forward(request, response);
					}
				}else{
					chain.doFilter(request, response);
				}
			}else{
				chain.doFilter(request, response);
			}
		}catch(Exception e){
			log.error("Exception ouccered" ,e);
			chain.doFilter(request, response);
		}
	}
	private void setSourceURL(HttpServletRequest req){
		String sourceURL = req.getRequestURL().toString();
		 if(req.getQueryString()!= null){
			 sourceURL = req.getRequestURL().append("?").append( 
					 req.getQueryString()).toString();	 
		 }
		 
		 try{
			 int lastIndexOfSlash = sourceURL.lastIndexOf("/");
			 sourceURL = sourceURL.substring(lastIndexOfSlash);
			 HttpSession session = req.getSession();
			 session.setAttribute(IConstants.SOURCE_URL, sourceURL);
		 }
		 catch(Exception e){
			 
		 }
	}
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
