package com.satya.Managers.Impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.satya.ApplicationContext;
import com.satya.IConstants;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.Managers.ProjectMgrI;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Utils.DateUtils;

public class ProjectMgr implements ProjectMgrI {
	private static final String CREATED_ON = "createdOn";
	private static final String COUNTRY = "country";
	private static final String CITY = "city";
	private static final String ADDRESS = "address";
	private static final String CONTACTPERSON = "contactPerson";
	private static final String PHONE = "phone";
	private static final String DESCRIPTION = "description";
	private static final String DELETED_SUCCESSFULLY = "Deleted Successfully";
	private static final String PROJECT_SAVED_SUCCESSFULLY = "Project Saved Successfully";
	private static final String MESSAGE = "message";
	private static final String STATUS = "status";
	private static final String ERROR = "Error: ";
	private static final String FAILD = "faild";
	private static final String SUCCESS = "success";
	private static final String MOBILE = "mobile";
	private static final String EMAIL = "email";
	private static final String NAME = "name";
	private static final String ID = "seq";
	private static final String REGISTRATION_URL = "registrationUrl";

	public List<Project> getAllProjects() throws ServletException, IOException {
		List<Project> projects = null;
		ProjectDataStoreI PDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getProjectDataStore();
		projects = PDS.findAll();
		return projects;
	}

	public Project FindBySeq(long seq) throws Exception {
		Project project = null;
		ProjectDataStoreI PDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getProjectDataStore();
		project = PDS.findBySeq(seq);
		return project;
	}

	public JSONArray getAllProjectsJson() throws ServletException, IOException {
		List<Project> projects = getAllProjects();
		JSONArray jsonArr = new JSONArray();
		JSONObject mainJsonObject = new JSONObject();
		for (Project project : projects) {
			jsonArr.put(toJson(project));
		}
		try {
			mainJsonObject.put("jsonArr", jsonArr);
		} catch (Exception e) {

		}
		return jsonArr;
	}

	private JSONObject toJson(Project project) {
		JSONObject json = new JSONObject();
		try {
			json.put(ID, project.getSeq());
			json.put(NAME, project.getName());
			json.put(DESCRIPTION, project.getDescription());
			json.put(EMAIL, project.getEmail());
			json.put(PHONE, project.getPhone());
			json.put(MOBILE, project.getMobile());
			json.put(CONTACTPERSON, project.getContactPerson());
			json.put(ADDRESS, project.getAddress());
			json.put(CITY, project.getCity());
			json.put(COUNTRY, project.getCountry());
			json.put(CREATED_ON,
					DateUtils.getGridDateFormat(project.getCreatedOn()));
			json.put(IConstants.IS_ENABLED, project.isEnable());
			json.put(IConstants.LAST_MODIFIED_DATE,
					DateUtils.getGridDateFormat(project.getLastModifiedDate()));
			json.put(REGISTRATION_URL, project.getRegistrationURL());
		} catch (Exception e) {

		}
		return json;
	}

	public JSONObject addProject(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		String id = request.getParameter(ID);
		String name = request.getParameter(NAME);
		String description = request.getParameter(DESCRIPTION);
		String email = request.getParameter(EMAIL);
		String phone = request.getParameter(PHONE);
		String mobile = request.getParameter(MOBILE);
		String contactPerson = request.getParameter(CONTACTPERSON);
		String address = request.getParameter(ADDRESS);
		String city = request.getParameter(CITY);
		String country = request.getParameter(COUNTRY);
		String isEnableStr = request.getParameter(IConstants.IS_ENABLED);

		Project project = new Project();
		long projectSeq = 0;
		if (id != null && !id.equals("") && !id.equals("0")) {
			projectSeq = Long.parseLong(id);
		} else {
			project.setCreatedOn(new Date());
		}
		project.setSeq(projectSeq);
		project.setName(name);
		project.setDescription(description);
		project.setEmail(email);
		project.setPhone(phone);
		project.setMobile(mobile);
		project.setContactPerson(contactPerson);
		project.setAddress(address);
		project.setCity(city);
		project.setCountry(country);

		project.setLastModifiedDate(new Date());
		// project.setCreatedOn(new Date());
		boolean isEnable = false;
		if (isEnableStr != null && !isEnableStr.equals("")) {
			isEnable = Boolean.parseBoolean(isEnableStr);
		}
		project.setEnable(isEnable);
		Admin admin = ApplicationContext.getApplicationContext()
				.getLoggedinAdmin(request);
		project.setAdmin(admin);
		ProjectDataStoreI PDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getProjectDataStore();

		String status = SUCCESS;
		String message = PROJECT_SAVED_SUCCESSFULLY;
		Project dupeProject = PDS.findByNameAndAdmin(name, admin.getSeq());
		if (dupeProject != null && (dupeProject.getSeq() != project.getSeq())) {
			status = FAILD;
			message = ERROR + "Duplicate Project is now allowed.";
		} else {
			try {
				PDS.Save(project);
				// just trying to grab already saved createdOn
				// if its update case, then make a find call, or createdOn is
				// current date only
				// if(projectSeq != 0){
				// project = PDS.findBySeq(projectSeq);
				// }

				json.put(IConstants.LAST_MODIFIED, DateUtils
						.getGridDateFormat(project.getLastModifiedDate()));
				json.put(IConstants.CREATED_ON,
						DateUtils.getGridDateFormat(project.getCreatedOn()));
				json.put(IConstants.CREATED_ON,
						DateUtils.getGridDateFormat(project.getCreatedOn()));
			} catch (Exception e) {
				status = FAILD;
				message = ERROR + e.getMessage();
			}
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, project.getSeq());

		return json;
	}

	public JSONObject delete(long projectSeq) throws Exception {
		JSONObject json = new JSONObject();
		String status = SUCCESS;
		String message = DELETED_SUCCESSFULLY;
		try {
			ProjectDataStoreI UDS = ApplicationContext.getApplicationContext()
					.getDataStoreMgr().getProjectDataStore();
			UDS.Delete(projectSeq);
		} catch (Exception e) {
			status = FAILD;
			message = ERROR + e.getMessage();
		}
		json.put(STATUS, status);
		json.put(MESSAGE, message);
		json.put(ID, projectSeq);
		return json;
	}

	public JSONArray deleteBulk(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		JSONObject json = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String idsStr = request.getParameter("ids");
		if (idsStr != null && !idsStr.equals("")) {
			String[] ids = idsStr.split(",");
			for (String id : ids) {
				long projectSeq = Long.parseLong(id);
				json = this.delete(projectSeq);
				jsonArr.put(json);
			}
		}
		return jsonArr;
	}

	@Override
	public Project getProject(long seq) {
		ProjectDataStoreI PDS = ApplicationContext.getApplicationContext()
				.getDataStoreMgr().getProjectDataStore();
		return PDS.findBySeq(seq);
	}
}
