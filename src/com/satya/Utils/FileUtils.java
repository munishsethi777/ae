package com.satya.Utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;

import org.apache.log4j.Logger;

public class FileUtils {
	private static Logger logger = Logger.getLogger(FileUtils.class);

	public static void writeFile(String filePath, byte[] bytesIn)
			throws Exception {
		InputStream in = null;
		OutputStream out = null;
		try {

			in = new ByteArrayInputStream(bytesIn);
			out = new BufferedOutputStream(new FileOutputStream(filePath));
			byte[] buffer = new byte[512];
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
			out.flush();
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}

	}

	public static void saveTextFile(String str, String filePath) {

		try {
			File statText = new File(filePath);
			FileOutputStream is = new FileOutputStream(statText);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);
			w.write(str);
			w.close();
		} catch (Exception e) {
			logger.error("Error occured during file writing : ", e);
		}

	}

	public String readFile(String fileName) throws IOException {
		URL url = getClass().getResource(fileName);
		FileReader fr = new FileReader(url.getPath());
		BufferedReader br = new BufferedReader(fr);
		String str;
		StringBuilder content = new StringBuilder(1024);
		while ((str = br.readLine()) != null) {
			content.append(str);
		}
		return content.toString();
	}

	public String readFileFromFileSystem(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String str;
		StringBuilder content = new StringBuilder(1024);
		while ((str = br.readLine()) != null) {
			content.append(str);
		}
		return content.toString();
	}
}
