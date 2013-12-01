package com.satya.Persistence;

import java.util.List;

import com.satya.Image;

public interface ImageDataStoreI {
	public void save(Image image);

	public void delete(long Seq);

	public List<Image> findAll();

	public Image findBySeq(long seq);

	public Image findByAdmin(long adminSeq);

	public List<Image> findByProject(long projectSeq);

}
