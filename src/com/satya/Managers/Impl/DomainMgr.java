package com.satya.Managers.Impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.Managers.DomainMgrI;

public class DomainMgr implements DomainMgrI {

	@Override
	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if (idsStr != null && !idsStr.equals("")) {
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				long gameSeq = Long.parseLong(id);
				// json = this.delete(gameSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}
}
