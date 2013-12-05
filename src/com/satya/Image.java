package com.satya;

import java.util.Date;

import org.json.JSONObject;

import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.Utils.DateUtils;

public class Image {
	private static final String IMAGE_SAVE_DATE = "imageSaveDate";
	private static final String IMAGE_SIZE = "imageSize";
	private static final String IMAGE_PATH = "imagePath";
	private static final String IMAGE_TITLE = "imageTitle";
	private static final String ID = "seq";
	private long seq;
	private String imageTitle;
	private String imagePath;
	private byte[] imageBytes;
	private Date imageSaveDate;
	private Project project;
	private Admin admin;
	private String imageType;
	private long imageSize;

	public long getImageSize() {
		return imageSize;
	}

	public void setImageSize(long imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getImageTitle() {
		return imageTitle;
	}

	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public Date getImageSaveDate() {
		return imageSaveDate;
	}

	public void setImageSaveDate(Date imageSaveDate) {
		this.imageSaveDate = imageSaveDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public static JSONObject toJson(Image image) {
		JSONObject json = new JSONObject();
		try {
			json.put(ID, image.getSeq());
			json.put(IMAGE_TITLE, image.getImageTitle());
			json.put(IMAGE_PATH, image.getImagePath());
			json.put(IMAGE_SIZE, image.getImageSize());
			json.put(IMAGE_SAVE_DATE,
					DateUtils.getGridDateFormat(image.getImageSaveDate()));
		} catch (Exception e) {

		}
		return json;
	}
}
