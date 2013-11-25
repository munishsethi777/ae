package com.satya.Servlets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.User;

public class BaseServletClass extends HttpServlet {

	private final String ENCODING="UTF-8";

	public User getLoggedinUser(HttpServletRequest request){
		return ApplicationContext.getApplicationContext().getLoggedinUser(request);
	}
	
	public Admin getLoggedinAdmin(HttpServletRequest request){
		return ApplicationContext.getApplicationContext().getLoggedinAdmin(request);
	}
	
	protected boolean isValidSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            if(session.getAttribute(IConstants.loggedInUser) != null){
                return true;
            }
        }
        return false;
    }
	
	protected String encodeMsg(String errorMsg){
        try {
            errorMsg=URLEncoder.encode(errorMsg,ENCODING);
            return errorMsg;
        }catch(UnsupportedEncodingException uex){
              throw new RuntimeException(uex);
        }
    }
	protected void checkSession(HttpServletRequest request, 
					HttpServletResponse response){
		try{
			List<String> errorMsgs = new ArrayList<String>();
			if(getLoggedinUser(request) == null){
				errorMsgs.add(IConstants.err_pleaseLogin);
				request.setAttribute(IConstants.errMessages, errorMsgs);
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}	
		}catch(Exception e){}
		
	}
	

}
