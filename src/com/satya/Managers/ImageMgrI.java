package com.satya.Managers;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.Image;

public interface ImageMgrI {
	public void uploadImage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	public JSONArray getAllImagesJson(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	public List<Image> getAllImages(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	public JSONObject delete(long gameSeq) throws Exception;

	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception;
}
