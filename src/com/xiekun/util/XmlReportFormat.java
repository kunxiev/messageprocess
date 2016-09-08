package com.xiekun.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XmlReportFormat {

	public static String formatXml(Map<String, Object> reportMap, Element reportFormat) throws Exception {
		
		Document document = DocumentHelper.createDocument();
		
		String encoding = reportFormat.attributeValue("encoding");
		if(encoding!=null && encoding.length()>0){
			document.setXMLEncoding("UTF-8");
		}
		Element rootFormat = reportFormat.element("root");
		String rootName = rootFormat.attributeValue("name");
		String rootFrom = rootFormat.attributeValue("from");
		
		@SuppressWarnings("unchecked")
		Map<String, Object> rootMap = (Map<String, Object>) reportMap.get(rootFrom);
		Element rootElement = document.addElement(rootName);
		
		formatElement(rootFormat, rootMap, rootElement);
		
		return document.asXML().replaceAll( "[\\n\\r]*", "");
	}
	
	private static void formatElement(Element elementFormatP, Map<String, Object> reportMap, Element parentElement) throws Exception{
		for(@SuppressWarnings("unchecked")
		Iterator<Element> i=elementFormatP.elementIterator(); i.hasNext();){
			Element elementFormat = (Element) i.next();
			if(isFormat(elementFormatP, reportMap)){
				String nodeName = elementFormat.getName();
				
				if("block".equals(nodeName)){
					String blockName = elementFormat.attributeValue("name");
					String blockFrom = elementFormat.attributeValue("from");
					String blockRef = elementFormat.attributeValue("ref");
					
					if(blockRef!=null && blockRef.length()>0){
						elementFormat = ReportFormat.getFormat(blockRef);
						if(elementFormat == null){
							throw new Exception("引用的解析" + blockRef + "不存在!");
						}
					}
					
					@SuppressWarnings("unchecked")
					Map<String, Object> blockMap = (Map<String, Object>) reportMap.get(blockFrom);
					Element element = parentElement.addElement(blockName);
					formatElement(elementFormat, blockMap, element);
				}else if("repeat".equals(nodeName)){
					String repeatName = elementFormat.attributeValue("name");
					String repeatFrom = elementFormat.attributeValue("from");
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> repeatList = (List<Map<String, Object>>) reportMap.get(repeatFrom);
					for(Iterator<Map<String, Object>> j=repeatList.iterator(); j.hasNext();){
						Map<String, Object> repeatMap = (Map<String, Object>) j.next();
						Element element = parentElement.addElement(repeatName);
						formatElement(elementFormat, repeatMap, element);
					}
					
				}else if("field".equals(nodeName)){
					String fieldName = elementFormat.attributeValue("name");
					String fromField = elementFormat.attributeValue("from");
					String defaultValue = elementFormat.attributeValue("default");
					String ref = elementFormat.attributeValue("ref");
					Element element = parentElement.addElement(fieldName);
					if(fromField==null || fromField.length()==0){
						element.setText(defaultValue);
					}else {
						element.setText(reportMap.get(fromField).toString());
					}
				}
			}
		}
		
	}
	
	private static boolean isFormat(Element fieldFormat, Map<String, Object> reportMap){
		
		String condition = fieldFormat.attributeValue("condition");
		if(condition==null){
			return true;
		}else{
			String[] cArr = condition.split("=");
			String value = getValue(reportMap, cArr[0]);
			if(value != null && !value.matches(cArr[1])) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static String getValue(Map<String, Object>  reportMap, String condition) {
		if(condition == null){
			return null;
		}
		int pos = condition.indexOf("/");
		
		if(pos == -1){
			return (String)reportMap.get(condition);
		}
		Object obj = reportMap.get(condition.substring(0, pos));
		String value;
		if(obj instanceof Map){
			value = getValue((Map<String, Object>)obj, condition.substring(pos));
		}else{
			value = (String) obj;
		}
		return value;
	}
}
