package com.satya.Utils;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class ImageUtils {
	private final static String USER_FOLDER_PREFIX = "user";

	private static String getSmallImagePathFromMainImagePath(
			String mainImagepath) {
		if (mainImagepath.contains("_small"))
			return mainImagepath;
		int decimalLocation = mainImagepath.lastIndexOf(".");
		mainImagepath = mainImagepath.substring(0, decimalLocation)
				+ "_small"
				+ mainImagepath.substring(decimalLocation,
						mainImagepath.length());
		return mainImagepath;
	}

	private static String getThumnailImagePathFromMainImagePath(
			String mainImagepath) {
		if (mainImagepath.contains("_thumnail"))
			return mainImagepath;
		int decimalLocation = mainImagepath.lastIndexOf(".");
		mainImagepath = mainImagepath.substring(0, decimalLocation)
				+ "_thumnail"
				+ mainImagepath.substring(decimalLocation,
						mainImagepath.length());
		return mainImagepath;
	}

	public static String getMainImagePath(String imagepath) {
		if (imagepath.contains("_thumnail")) {
			return imagepath.replace("_thumnail", "");
		} else if (imagepath.contains("_small")) {
			return imagepath.replace("_small", "");
		}
		return imagepath;
	}

	public static URI getEncodedURI(URI uri, String path)
			throws URISyntaxException {
		URI newUri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(),
				uri.getPort(), uri.getPath() + path, uri.getQuery(),
				uri.getFragment());
		return newUri;
	}




	public static String getImagesDirectoryForUser(Long userSeq) {
		/*
		 * String imgPath =
		 * Configuration.getSystemConfig(Configuration.IMAGES_PATH ); if(imgPath
		 * == null || imgPath.trim().length() == 0){ throw new
		 * RuntimeException("Image Path not set"); }
		 */
		String imageFilePath = // imgPath + File.separator +
		USER_FOLDER_PREFIX + userSeq;
		return imageFilePath;
	}

	public static String getImageNameFromPath(String imgPath) {
		int lastSlash = imgPath.lastIndexOf("/");
		String imageName = imgPath.substring(lastSlash + 1, imgPath.length());
		return imageName;
	}

	public static String getImageFileNameWithoutExtn(String imgFileName) {
		int lastDotIndex = imgFileName.lastIndexOf(".");
		String fileNameWithoutExtn = imgFileName.substring(0, lastDotIndex);
		return fileNameWithoutExtn;
	}

	public static String getImageExtension(String imgFileName) {
		String ext = imgFileName.substring(imgFileName.lastIndexOf('.') + 1,
				imgFileName.length());
		if (ext.contains("?")) {
			ext = ext.substring(0, ext.lastIndexOf('?'));
		}
		return ext;

	}

	

	private static String appendCounterToImagePath(String imgPath, int counter) {
		int lastDotIndex = imgPath.lastIndexOf(".");
		String fileExtn = imgPath.substring(lastDotIndex);
		String fileNameWithoutExtn = imgPath.substring(0, lastDotIndex);
		String appendedImgPath = null;
		if (fileNameWithoutExtn.endsWith("_" + (counter - 1))) {
			int Indexs = imgPath.lastIndexOf("_" + (counter - 1));
			appendedImgPath = imgPath.substring(0, Indexs);
		} else {
			appendedImgPath = fileNameWithoutExtn;
		}
		appendedImgPath = appendedImgPath + "_" + counter + fileExtn;

		return appendedImgPath;
	}

	

	public static String getSmallURL(String anyURL) {
		// if its an ebay image or is already a _small image from AL return the
		// path as it is.
		if (anyURL.contains("i.ebayimg") || anyURL.contains("_small")) {
			return anyURL;
		}

		else if (anyURL.contains("_thumnail")) {
			return anyURL.replaceAll("_thumnail", "_small");
		}

		else {
			return ImageUtils.getSmallImagePathFromMainImagePath(anyURL);
		}

	}

	public static String getThumbURL(String anyURL) {
		if (anyURL.contains("_thumnail")) {
			return anyURL;
		}

		else if (anyURL.contains("_small")) {
			return anyURL.replaceAll("_small", "_thumnail");
		}

		else {
			return ImageUtils.getThumnailImagePathFromMainImagePath(anyURL);
		}
	}

	public static String getThumbURLPath(File thumbFile) {
		String path = thumbFile.getAbsolutePath();
		String ext = path.substring(path.lastIndexOf('.') + 1, path.length());
		String imagePathWithoutExt = path.substring(0, path.lastIndexOf('.'));
		return imagePathWithoutExt + "_thumnail." + ext;
	}

	public static String getSmallThumbURLPath(File thumbFile) {
		String path = thumbFile.getAbsolutePath();
		String ext = path.substring(path.lastIndexOf('.') + 1, path.length());
		String imagePathWithoutExt = path.substring(0, path.lastIndexOf('.'));
		return imagePathWithoutExt + "_small." + ext;
	}
	public static String getThumbURLBySuffix(File thumbFile,String Suffix){
		String path = thumbFile.getAbsolutePath();
		String ext = path.substring(path.lastIndexOf('.') + 1, path.length());
		String imagePathWithoutExt = path.substring(0, path.lastIndexOf('.'));
		return imagePathWithoutExt + "_" + Suffix + "." + ext;
	}
	/**
	 * Checks if passed in image size exceeds user's quota of image space.
	 * Returns true if image quota is exceeded, false otherwise.
	 * 
	 * @param imageSize
	 *            in KB
	 * @param user
	 * @return
	 */
	

	private static boolean imagePathAlreadyExists(String path) {
		File file = new File(path);
		return file.exists();
	}

	public static String createThumbnail(String originalImagePath,
			int largestDimension, String suffix) {

		String ext = originalImagePath.substring(
				originalImagePath.lastIndexOf('.') + 1,
				originalImagePath.length());
		String imagePathWithoutExt = originalImagePath.substring(0,
				originalImagePath.lastIndexOf('.'));
		String imageThumnailPath = imagePathWithoutExt + "_" + suffix + "."
				+ ext;

		try {
			double scale;
			int sizeDifference, originalImageLargestDim;
			if (!originalImagePath.endsWith(".jpg")
					&& !originalImagePath.endsWith(".jpeg")
					&& !originalImagePath.endsWith(".gif")
					&& !originalImagePath.endsWith(".png")) {
				return "Error: Unsupported image type, please only either JPG, GIF or PNG";
			} else {
				Image inImage = Toolkit.getDefaultToolkit().getImage(
						originalImagePath);
				if (inImage.getWidth(null) == -1
						|| inImage.getHeight(null) == -1) {
					return "Error loading file: \"" + originalImagePath + "\"";
				} else {
					// find biggest dimension
					if (inImage.getWidth(null) > inImage.getHeight(null)) {
						scale = (double) largestDimension
								/ (double) inImage.getWidth(null);
						sizeDifference = inImage.getWidth(null)
								- largestDimension;
						originalImageLargestDim = inImage.getWidth(null);
					} else {
						scale = (double) largestDimension
								/ (double) inImage.getHeight(null);
						sizeDifference = inImage.getHeight(null)
								- largestDimension;
						originalImageLargestDim = inImage.getHeight(null);
					}
					// create an image buffer to draw to
					BufferedImage outImage = new BufferedImage(100, 100,
							BufferedImage.TYPE_INT_RGB); // arbitrary init so
															// code compiles
					Graphics2D g2d;
					AffineTransform tx;
					if (scale < 1.0d) // only scale if desired size is smaller
										// than original
					{
						int numSteps = sizeDifference / 100;
						int stepSize = sizeDifference / numSteps;
						int stepWeight = stepSize / 2;
						int heavierStepSize = stepSize + stepWeight;
						int lighterStepSize = stepSize - stepWeight;
						int currentStepSize, centerStep;
						double scaledW = inImage.getWidth(null);
						double scaledH = inImage.getHeight(null);
						if (numSteps % 2 == 1) // if there's an odd number of
												// steps
							centerStep = (int) Math
									.ceil((double) numSteps / 2d); // find the
																	// center
																	// step
						else
							centerStep = -1; // set it to -1 so it's ignored
												// later
						Integer intermediateSize = originalImageLargestDim, previousIntermediateSize = originalImageLargestDim;
						Integer calculatedDim;
						for (Integer i = 0; i < numSteps; i++) {
							if (i + 1 != centerStep) // if this isn't the center
														// step
							{
								if (i == numSteps - 1) // if this is the last
														// step
								{
									// fix the stepsize to account for decimal
									// place errors previously
									currentStepSize = previousIntermediateSize
											- largestDimension;
								} else {
									if (numSteps - i > numSteps / 2) // if we're
																		// in
																		// the
																		// first
																		// half
																		// of
																		// the
																		// reductions
										currentStepSize = heavierStepSize;
									else
										currentStepSize = lighterStepSize;
								}
							} else // center step, use natural step size
							{
								currentStepSize = stepSize;
							}
							intermediateSize = previousIntermediateSize
									- currentStepSize;
							scale = (double) intermediateSize
									/ (double) previousIntermediateSize;
							scaledW = (int) scaledW * scale;
							scaledH = (int) scaledH * scale;
							outImage = new BufferedImage((int) scaledW,
									(int) scaledH, BufferedImage.TYPE_INT_RGB);
							g2d = outImage.createGraphics();
							g2d.setBackground(Color.WHITE);
							g2d.clearRect(0, 0, outImage.getWidth(),
									outImage.getHeight());
							g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
									RenderingHints.VALUE_RENDER_QUALITY);
							tx = new AffineTransform();
							tx.scale(scale, scale);
							g2d.drawImage(inImage, tx, null);
							g2d.dispose();
							inImage = new ImageIcon(outImage).getImage();
							previousIntermediateSize = intermediateSize;
						}
					} else {
						// just copy the original
						outImage = new BufferedImage(inImage.getWidth(null),
								inImage.getHeight(null),
								BufferedImage.TYPE_INT_RGB);
						g2d = outImage.createGraphics();
						g2d.setBackground(Color.WHITE);
						g2d.clearRect(0, 0, outImage.getWidth(),
								outImage.getHeight());
						tx = new AffineTransform();
						tx.setToIdentity(); // use identity matrix so image is
											// copied exactly
						g2d.drawImage(inImage, tx, null);
						g2d.dispose();
					}
					// JPEG-encode the image and write to file.
					OutputStream os = new FileOutputStream(imageThumnailPath);
					JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(os);
					encoder.encode(outImage);
					os.close();
				}
			}
		} catch (Exception ex) {
			String errorMsg = "";
			errorMsg += "<br>Exception: " + ex.toString();
			errorMsg += "<br>Cause = " + ex.getCause();
			errorMsg += "<br>Stack Trace = ";
			StackTraceElement stackTrace[] = ex.getStackTrace();
			for (int traceLine = 0; traceLine < stackTrace.length; traceLine++) {
				errorMsg += "<br>" + stackTrace[traceLine];
			}
			return errorMsg;
		}
		return ""; // success
	}
	
	public static void createThumbnail(String imagePath, int thumbWidth,
			int thumbHeight, String newImgName) throws Exception {
//		String ext = newImgName.substring(imagePath.lastIndexOf('.') + 1,
//				newImgName.length());
		String fullImageFileName = imagePath;
	    File f = new File(fullImageFileName);
		File parentDir = f.getParentFile();		
//		String imagePathWithoutExt = imagePath.substring(0,
//				imagePath.lastIndexOf('.'));
		String paretnDirPath = parentDir.getPath();		
		String imageThumnailPath = paretnDirPath + "\\" + newImgName;
		Image image = Toolkit.getDefaultToolkit().getImage(imagePath);
		MediaTracker mediaTracker = new MediaTracker(new Container());
		mediaTracker.addImage(image, 0);
		mediaTracker.waitForID(0);
		double thumbRatio = (double) thumbWidth / (double) thumbHeight;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;
		if (thumbRatio < imageRatio) {
			thumbHeight = (int) (thumbWidth / imageRatio);
		} else {
			thumbWidth = (int) (thumbHeight * imageRatio);
		}
		BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2D = thumbImage.createGraphics();
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(imageThumnailPath));
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);

		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		int quality = 80;
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		out.close();
	}

	
}
