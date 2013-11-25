package com.satya.Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.satya.ApplicationContext;
import com.satya.BusinessObjects.Game;
import com.satya.BusinessObjects.GameTemplates;
import com.satya.BusinessObjects.Result;
import com.satya.BusinessObjects.User;
import com.satya.Managers.CampaignMgrI;
import com.satya.Managers.GameMgrI;
import com.satya.Managers.ResultsMgrI;
import com.satya.Managers.SetMgrI;
import com.satya.Managers.UserMgrI;
import com.satya.Persistence.GameDataStoreI;
import com.satya.Persistence.GameTemplatesDataStoreI;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserServlet.class);
    
	private static final String LOGOUT = "logout";
	private static final String LOGIN = "login";
	private static final String SIGNUP = "signup";
	
	private static final String GET_CAMPAIGNS ="getCampaigns";
	private static final String GET_SETS_FOR_CAMPAIGN = "getSetsByCampaign";
	private static final String PLAYER = "loadPlayer";
	
	
	public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CampaignMgrI campaignMgr = ApplicationContext.getApplicationContext().getCampaiMgr();
		SetMgrI setMgr = ApplicationContext.getApplicationContext().getSetMgr();
		UserMgrI userMgr = ApplicationContext.getApplicationContext().getUserMgr();
		GameMgrI gameMgr = ApplicationContext.getApplicationContext().getGamesMgr();
		
		String action = request.getParameter("action");
		User loggedInUser = ApplicationContext.getApplicationContext().getLoggedinUser(request);
		if (action.equals(GET_CAMPAIGNS)) {
			try{
				JSONArray json = campaignMgr.getCampaignsForUserJSON(loggedInUser.getSeq());
				response.getWriter().print(json.toString());
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}else if(action.equals(GET_SETS_FOR_CAMPAIGN)){
			try{
				JSONArray json = setMgr.getSetsByCampaign(request, response);
				response.getWriter().print(json.toString());
			}catch(Exception e){
				logger.error(e.getMessage());
			}
		}else if(action.equals(LOGOUT)){
			userMgr.logout(request, response);
			
		}else if(action.equals(PLAYER)){
			gameMgr.loadPlayer(request, response);
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			UserMgrI userMgr = ApplicationContext.getApplicationContext().getUserMgr();
			ResultsMgrI resultMgr = ApplicationContext.getApplicationContext().getResultMgr();
			
			String action = request.getParameter("action");
			if(action.equals("getGameInfo")){
				String gameIdStr = request.getParameter("game_id");
				String campaignSetSeqStr = request.getParameter("campaignSetSeq");
				
				Long gameId = Long.parseLong(gameIdStr);
				Long campaignSetSeq = Long.parseLong(campaignSetSeqStr);
				
				GameDataStoreI GDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameDataStore();
				Game game = GDS.findBySeq(gameId);
				
//				GameTemplatesDataStoreI GTDS = ApplicationContext.getApplicationContext().getDataStoreMgr().getGameTemplateDataStore();
//				GameTemplates gameTemplate = GTDS.findBySeq(Long.parseLong(gameId));
				User user = ApplicationContext.getApplicationContext().getLoggedinUser(request);
				String vars = "gamepath="+ game.getGameTemplate().getPath() +
						"&gameId=" + game.getSeq() +
						"&campaignSetSeq=" + campaignSetSeq +
						"&gamename=" + game.getTitle() +
						"&xmlfile=" + game.getQuestionsXMLPath() +
						"&userId="+ user.getSeq() +
						"&resulturl=User?action=result";
				response.getWriter().print(vars);
				return;
			}else if(action.equals("result")){
				String resultXML = request.getParameter("resultXML");
				
				Result result = resultMgr.ConvertXMLToResult(resultXML);
				resultMgr.SaveResult(result);
			}else if (action.equals(SIGNUP)) {
				userMgr.signup(request, response);
				
			}else if(action.equals(LOGIN)) {
				userMgr.login(request, response);
			
			}
		}catch(Exception e){
			logger.error("Exception occured while fetching gameTemplate info for player :" + e.getMessage());
		}
		
	}

}
