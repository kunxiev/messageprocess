package com.xiekun.util;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReportParse {
	private static String DEFAULT_PATH = GetPath.getConfigPath();
	private static String DEFAULT_FILE = "xmlreportparse.xml";
	private static Document document;
	
	static{
		SAXReader reader = new SAXReader();
		
		File reportParseFile = new File(DEFAULT_PATH + DEFAULT_FILE);
		try {
			document = reader.read(reportParseFile);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException("民生代扣配置文件加载出错！");
		}
	}
	
	public static Map parseReport(InputStream reportStream, String reportName) throws Exception{
		SAXReader reader = new SAXReader();
		
		File reportParseFile = new File(DEFAULT_PATH + DEFAULT_FILE);
		Document document = reader.read(reportParseFile);
		Element reportParse = (Element)document.selectSingleNode("/reports/report[@name='" + reportName + "']");

		if(reportParse == null){
			//解析报文格式不存在
			return null;
		}
		
		return XmlReportParse.parseXml(reportStream,reportParse);
	}
	
	public static Map parseReport(String reportStr, String reportName) throws Exception{
		SAXReader reader = new SAXReader();
		
		File reportParseFile = new File(DEFAULT_PATH + DEFAULT_FILE);
		Document document = reader.read(reportParseFile);
		Element reportParse = (Element)document.selectSingleNode("/reports/report[@name='" + reportName + "']");

		if(reportParse == null){
			//解析报文格式不存在
			return null;
		}
		
		return XmlReportParse.parseXml(reportStr,reportParse);
	}
	
	/**
	 * 获取指定的解析块
	 * @param refStr
	 * @return
	 * @throws Exception
	 */
	public static Element getParse(String refStr) throws Exception{
		String elementPath = refStr.substring(refStr.indexOf("@")+1);
		
		SAXReader reader = new SAXReader();
		
		Element elementParse = (Element)document.selectSingleNode(elementPath);

		return elementParse;
	}
}
