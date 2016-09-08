package com.xiekun.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class GetPath {

	public static String getConfigPath() {

		return GetPath.getProjectContext() + "/config/";

	}

	public static String getProjectContext() {
		URL url = GetPath.class.getProtectionDomain().getCodeSource().getLocation();
		
		String filePath = null;
		try {
			filePath = URLDecoder.decode(url.getPath(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			StpLogger.logInfo(e.getMessage(), Constants.Server);
		}
		filePath = filePath.substring(0,filePath.length()-1);
		return filePath.substring(0, filePath.lastIndexOf("/"));
	}
	
}
