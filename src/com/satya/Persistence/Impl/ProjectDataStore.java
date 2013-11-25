package com.satya.Persistence.Impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.BusinessObjects.Project;
import com.satya.Persistence.ProjectDataStoreI;
import com.satya.Persistence.RowMapper;


public class ProjectDataStore implements ProjectDataStoreI,RowMapper {
	Logger logger = Logger.getLogger(ProjectDataStore.class);
	private final static String SELECT = "select * from projects ";
	private  final static String SELECT_BY_SEQ = SELECT + "where seq = ?";
	private  final static String SELECT_BY_NAME_ADMIN = SELECT + "where name = ? and adminseq = ?";
	private  final static String SELECT_BY_ADMIN = SELECT + "where adminseq = ?";
	private final static String SAVE_PROJECT = "insert into projects(adminseq, name, description, " +
	"email,phone, mobile, contactperson, address, city,country,isenabled,lastmodifieddate,createdon) " +
	"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
private final static String UPDATE_PROJECT = "update projects set adminseq=?, name=?, description=?, " +
	"email=?,phone=?, mobile=?, contactperson=?, address=?, city=? ,country=?,isenabled=?,lastmodifieddate=? where seq=?";

	private final static String DELETE = "delete from projects where seq=?";
	private PersistenceMgr persistenceMgr;
	public ProjectDataStore(PersistenceMgr psmgr){
		this.persistenceMgr = psmgr;
	}
	
	@Override
	public void Save(Project project) {
		String SQL = project.getSeq() != 0 ? UPDATE_PROJECT : SAVE_PROJECT;
		try{
			long adminSeq = 0;
			if(project.getAdmin()!= null){
				adminSeq = project.getAdmin().getSeq();
			}
			Object[] params  = new Object[13];
			
					params[0]= adminSeq;
					params[1] = project.getName();
					params[2] = project.getDescription();
					params[3] = project.getEmail();
					params[4] = project.getPhone();
					params[5] = project.getMobile();
					params[6] = project.getContactPerson();
					params[7] = project.getAddress();
					params[8] = project.getCity();
					params[9] = project.getCountry();
					params[10] = project.isEnable();
					params[11] = project.getLastModifiedDate();
					if(project.getSeq() != 0){
						params[12] =  project.getSeq();
					}else{
						params[12] = project.getCreatedOn();
					}
		
			persistenceMgr.excecuteUpdate(SQL, params);
			if(project.getSeq() != 0){
				project.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
		}catch(Exception e){
			logger.error("Error during save project for Seq : " + project.getSeq(),e);
		}
		
	}
	@Override
	public void Delete(long projectSeq) {
		Object [] params = new Object [] {projectSeq};
		persistenceMgr.excecuteUpdate(DELETE, params);		
	}
	
	
	public List<Project> findAll() {		
		return (List<Project>)persistenceMgr.executePSQuery(SELECT, null, this);	
	}
	@Override
	public Project findByNameAndAdmin(String projectName, long adminSeq){
		Object [] params = new Object [] {projectName,adminSeq};
		return (Project) persistenceMgr.executeSingleObjectQuery(SELECT_BY_NAME_ADMIN, params, this);
	}
	@Override
	public Project findBySeq(long seq) {
		Object [] params = new Object [] {seq};
		return (Project) persistenceMgr.executeSingleObjectQuery(SELECT_BY_SEQ, params, this);
	}
	@Override
	public List<Project>  findByAdminSeq(long adminSeq) {
		Object [] params = new Object [] {adminSeq};
		return persistenceMgr.executePSQuery(SELECT_BY_ADMIN, params, this);
	}

	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}
	

	protected Object populateObjectFromResultSet(ResultSet rs)throws SQLException{
		Project project  = null;
		try{			
			long seq = rs.getLong("seq");
			//String adminSeq = rs.getString("adminseq");
			String name = rs.getString("name");
			String description = rs.getString("description");
			String email = rs.getString("email");
			String phone = rs.getString("phone");
			String mobile = rs.getString("mobile");
			String contactPerson = rs.getString("contactperson");
			String address = rs.getString("address");
			String city = rs.getString("city");
			String country = rs.getString("country");
			Timestamp createdOn = rs.getTimestamp("createdon");
			Boolean isEnabled = rs.getBoolean("isenabled");
			
			
			Timestamp lastModifiedDate = rs.getTimestamp("lastmodifieddate");
			project = new Project();
			project.setSeq(seq);
			project.setName(name);
			project.setDescription(description);
			project.setEmail(email);
			project.setPhone(phone);
			project.setMobile(mobile);
			project.setContactPerson(contactPerson);
			project.setAddress(address);
			project.setCity(city);
			project.setCountry(country);
			project.setCreatedOn(createdOn);
			project.setLastModifiedDate(lastModifiedDate);
			project.setEnable(isEnabled);
			
			
		}catch(Exception e){
			logger.error("ProjectDataStore populate method error",e);
		}
		
		return project;
		
	}
}
