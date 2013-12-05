package com.satya.Persistence.Impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.satya.Image;
import com.satya.BusinessObjects.Admin;
import com.satya.BusinessObjects.Project;
import com.satya.Persistence.ImageDataStoreI;
import com.satya.Persistence.RowMapper;
import com.satya.Utils.FileUtils;
import com.satya.Utils.ImageUtils;

public class ImageDataStore implements ImageDataStoreI, RowMapper {

	Logger logger = Logger.getLogger(GameDataStore.class);
	private PersistenceMgr persistenceMgr;
	private final static String SAVE = "insert into images(imagetitle, imagepath, imagetype, "
			+ "imageSize,imagesavedate,projectseq,adminseq) "
			+ "values (?,?,?,?,?,?,?)";
	private final static String UPDATE = "update images set imagetitle=?, imagepath=?, imagetype=?, "
			+ " imageSize=?, imagesavedate=?,projectseq=?,adminseq=? where seq=?";
	private final static String SELECT = "select * from images";
	private final static String FIND_BY_SEQ = "select * from images where seq=?";
	private final static String DELETE = "delete from images where seq = ?";
	private final static String FIND_BY_ADMIN_SEQ = "select * from images where adminseq = ?";
	private final static String FIND_BY_PROJECT_SEQ = "select * from images where projectseq = ?";

	public ImageDataStore(PersistenceMgr psmgr) {
		this.persistenceMgr = psmgr;
	}

	@Override
	public void save(Image image) {

		String SQL = image.getSeq() != 0 ? UPDATE : SAVE;
		int size = image.getSeq() != 0 ? 8 : 7;
		long projectSeq = 0;
		if (image.getProject() != null) {
			projectSeq = image.getProject().getSeq();
		}
		long adminSeq = 0;
		if (image.getAdmin() != null) {
			adminSeq = image.getAdmin().getSeq();
		}
		try {
			saveFileContent(image);
			Object[] params = new Object[size];
			params[0] = image.getImageTitle();
			params[1] = image.getImagePath();
			params[2] = image.getImageType();
			params[3] = image.getImageSize();
			params[4] = image.getImageSaveDate();
			params[5] = projectSeq;
			params[6] = adminSeq;
			if (image.getSeq() != 0) {
				params[7] = image.getSeq();
			}
			persistenceMgr.excecuteUpdate(SQL, params);
			if (image.getSeq() == 0) {
				image.setSeq(persistenceMgr.getLastUpdatedSeq());
			}
		} catch (Exception e) {
			logger.error("Exception during save image", e);
		}

	}

	@Override
	public void delete(long seq) {
		Image image = findBySeq(seq);
		deleteFileContent(image);
		Object[] params = new Object[] { seq };
		persistenceMgr.excecuteUpdate(DELETE, params);
	}

	private void deleteFileContent(Image image) {
		String fullImagePath = "C:\\Users\\arvinder singh\\Desktop\\ae\\WebContent\\images\\userImages\\"
				+ image.getImagePath();
		String thum100Path = ImageUtils.getThumPathBySuffix(fullImagePath,
				ImageUtils.THUMNAIL_100);
		FileUtils.deleteFile(thum100Path);
		String thumb300Path = ImageUtils.getThumPathBySuffix(fullImagePath,
				ImageUtils.THUMNAIL_300);
		FileUtils.deleteFile(thumb300Path);
		FileUtils.deleteFile(fullImagePath);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Image> findAll() {
		return (List<Image>) persistenceMgr.executePSQuery(SELECT, null, this);
	}

	@Override
	public Image findBySeq(long seq) {
		Object[] params = new Object[] { seq };
		Image image = (Image) persistenceMgr.executeSingleObjectQuery(
				FIND_BY_SEQ, params, this);
		return image;
	}

	@Override
	public Image findByAdmin(long adminSeq) {
		Object[] params = new Object[] { adminSeq };
		Image image = (Image) persistenceMgr.executeSingleObjectQuery(
				FIND_BY_ADMIN_SEQ, params, this);
		return image;
	}

	@Override
	public List<Image> findByProject(long projectSeq) {
		Object[] params = new Object[] { projectSeq };
		@SuppressWarnings("unchecked")
		List<Image> image = (List<Image>) persistenceMgr.executePSQuery(
				FIND_BY_PROJECT_SEQ, params, this);
		return image;
	}

	@Override
	public Object mapRow(ResultSet rs) throws SQLException {
		return populateObjectFromResultSet(rs);
	}

	private void saveFileContent(Image image) throws Exception {
		String fullImagePath = "C:\\Users\\arvinder singh\\Desktop\\ae\\WebContent\\images\\userImages\\"
				+ image.getImagePath();
		File f = new File(fullImagePath);
		File parentDir = f.getParentFile();
		if (!parentDir.exists()) {
			parentDir.mkdir();
		}
		fullImagePath = ImageUtils.renameImageIfAlReadyExists(fullImagePath);
		String imageNameFromPath = ImageUtils
				.getImageNameFromPath(fullImagePath);
		String nameWithoutExt = ImageUtils
				.getImageFileNameWithoutExtn(imageNameFromPath);
		String extention = ImageUtils.getImageExtension(imageNameFromPath);
		FileUtils.writeFile(fullImagePath, image.getImageBytes());

		String newImageName = nameWithoutExt + ImageUtils.THUMNAIL_100 + "."
				+ extention;
		ImageUtils.createThumbnail(fullImagePath, 100, 100, newImageName);

		newImageName = nameWithoutExt + ImageUtils.THUMNAIL_300 + "."
				+ extention;
		ImageUtils.createThumbnail(fullImagePath, 300, 300, newImageName);
		image.setImagePath(imageNameFromPath);
	}

	protected Object populateObjectFromResultSet(ResultSet rs)
			throws SQLException {
		Image image = null;
		try {
			long seq = rs.getLong("seq");
			String imageTitle = rs.getString("imagetitle");
			String imagePath = rs.getString("imagepath");
			String imageType = rs.getString("imagetype");
			int imageSize = rs.getInt("imageSize");
			Date imageSaveDate = rs.getDate("imagesavedate");
			long projectSeq = rs.getLong("projectseq");
			long adminSeq = rs.getLong("adminseq");
			image = new Image();
			image.setSeq(seq);
			image.setImageTitle(imageTitle);
			image.setImagePath(imagePath);
			image.setImageSaveDate(imageSaveDate);
			image.setImageSize(imageSize);
			Project project = new Project();
			project.setSeq(projectSeq);
			image.setProject(project);
			Admin admin = new Admin();
			admin.setSeq(adminSeq);
			image.setAdmin(admin);
			image.setImageType(imageType);
		} catch (Exception e) {
			logger.error("ImageDataStore populate method error", e);
		}
		return image;

	}

}
