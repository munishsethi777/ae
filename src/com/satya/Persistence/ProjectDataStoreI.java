package com.satya.Persistence;

import java.util.List;

import com.satya.BusinessObjects.Project;
import com.satya.BusinessObjects.User;

public interface ProjectDataStoreI {
	public void Save(Project project);
	public void Delete(long userSeq);
	public List<Project> findAll();
	public Project findBySeq(long seq);
	public List<Project> findByAdminSeq(long adminSeq);
	public Project findByNameAndAdmin(String projectName, long adminSeq);
}
