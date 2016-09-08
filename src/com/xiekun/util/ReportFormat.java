package com.xiekun.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ReportFormat {
	
	private static String DEFAULT_PATH = GetPath.getConfigPath();
	private static String DEFAULT_FILE = "xmlreportformat.xml";
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
	
	public static String formatReport(Map reportMap, String reportName) throws Exception{
		List list = document.selectNodes("/reports/report[@name='" + reportName + "']");
		if(list.size() == 0){
			//创建报文格式不存在
			return null;
		}
		
		Element reportFormat = (Element) list.get(0);
		
		return XmlReportFormat.formatXml(reportMap,reportFormat);
	}
	/**
	 * 获取指定的解析块
	 * @param refStr
	 * @return
	 * @throws Exception
	 */
	public static Element getFormat(String refStr) throws Exception{
		String elementPath = refStr.substring(refStr.indexOf("@")+1);
		
		Element elementFormat = (Element)document.selectSingleNode(elementPath);

		return elementFormat;
	}
}
