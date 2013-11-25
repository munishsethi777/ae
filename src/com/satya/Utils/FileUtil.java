package com.satya.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

public class FileUtil {
	
	public  String  readFile(String fileName) throws IOException{
		URL url =  getClass().getResource(fileName);
		FileReader fr=new FileReader(url.getPath());
		BufferedReader br= new BufferedReader(fr);
		String str;
		StringBuilder content=new StringBuilder(1024);
		while((str=br.readLine())!=null)
		    {
		    content.append(str);
		    }
		return content.toString();
	}

}
