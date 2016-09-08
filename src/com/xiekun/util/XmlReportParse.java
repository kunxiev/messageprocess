package com.xiekun.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlReportParse {
	
	/**
	 * xml格式报文解析(流)
	 * @param reportStream
	 * @param reportParse
	 * @return
	 * @throws Exception 
	 */
	protected static Map<String, Object> parseXml(InputStreamReader reportStream, Element reportParse) throws Exception{
		Map<String, Object> reportMap = new HashMap<String, Object>();
		SAXReader reader = new SAXReader();
		Document report = reader.read(reportStream);
		reportMap = parseXml(report, reportParse);
		return reportMap;
	}
	/**
	 * xml格式报文解析(流)
	 * @param reportStream
	 * @param reportParse
	 * @return
	 * @throws Exception 
	 */
	protected static Map<String, Object> parseXml(InputStream reportStream, Element reportParse) throws Exception{
		Map<String, Object> reportMap = new HashMap<String, Object>();
		SAXReader reader = new SAXReader();
		Document report = reader.read(reportStream);
		reportMap = parseXml(report, reportParse);
		return reportMap;
	}
	/**
	 * xml格式报文解析(string)
	 * @param reportStr
	 * @param reportParse
	 * @return
	 * @throws Exception
	 */
	protected static Map<String, Object> parseXml(String reportStr, Element reportParse) throws Exception{
		Map<String, Object> reportMap = null;
		Document report = DocumentHelper.parseText(reportStr);
		reportMap = parseXml(report, reportParse);
		return reportMap;
	}
	/**
	 * 报文解析
	 * @param report
	 * @param reportParse
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> parseXml(Document report, Element reportParse) throws Exception{
		Map<String, Object> reportMap = new HashMap<String, Object>();
		Element rootElement = report.getRootElement();
		Element rootParse = reportParse.element("root");
		String rootName = rootParse.attributeValue("name");
		Map<String, Object> rootMap = new HashMap<String, Object>();
		for(@SuppressWarnings("unchecked")
		Iterator<Element> i=rootParse.elementIterator(); i.hasNext();){
			Element nodeParse = i.next();
			String nodeName = nodeParse.attributeValue("name");
			
			Object nodeValue = parseNode(rootElement, nodeParse, report);
			
			if(nodeValue != null){
				rootMap.put(nodeName, nodeValue);
			}
			
		}
		
		reportMap.put(rootName, rootMap);
		
		return reportMap;
	}
	/**
	 * 解析块
	 * @param report
	 * @param repeatParse
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> parseBlock(Element parentElement, Element blockParse, Document report) throws Exception {
		
		String blockFrom = blockParse.attributeValue("from");
		String blockRef = blockParse.attributeValue("ref");
		
		if(blockRef!=null && blockRef.length()>0){
			blockParse = ReportParse.getParse(blockRef);
			if(blockParse == null){
				throw new Exception("引用的解析" + blockRef + "不存在!");
			}
		}
		
		Element blockElement = parentElement.element(blockFrom);
		if(blockElement == null){
			throw new Exception("报文字段" + "[" + blockFrom + "]不存在!");
		}
		
		Map<String, Object> valueMap = new HashMap<String, Object>();
		
		for(@SuppressWarnings("unchecked")
		Iterator<Element> j=blockParse.elementIterator(); j.hasNext();){
			Element nodeParse = j.next();
			
			String name = nodeParse.attributeValue("name");
			
			Object value = parseNode(blockElement, nodeParse, report);
			
			if(value != null){
				valueMap.put(name, value);
			}
			
		}
		
		return valueMap;
	}
	/**
	 * 解析循环体
	 * @param report
	 * @param repeatParse
	 * @return
	 * @throws Exception 
	 */
	private static List<Map<String, Object>> parseRepeat(Element parentElement, Element repeatParse, Document report) throws Exception {
		String repeatFrom = repeatParse.attributeValue("from");
		@SuppressWarnings("unchecked")
		List<Element> repeatElements = parentElement.elements(repeatFrom);
		if(repeatElements==null || repeatElements.size()==0){
			throw new Exception("报文字段" + "[" + repeatFrom + "]不存在!");
		}
		
		List<Map<String, Object>> valueList = new ArrayList<Map<String, Object>>();
		for (Iterator<Element> iterator = repeatElements.iterator(); iterator.hasNext();) {
			Element repeatElement = iterator.next();
			
			Map<String, Object> valueMap = new HashMap<String, Object>();
			
			for(@SuppressWarnings("unchecked")
			Iterator<Element> j=repeatParse.elementIterator(); j.hasNext();){
				Element nodeParse = j.next();
				
				String name = nodeParse.attributeValue("name");
				
				Object value = parseNode(repeatElement, nodeParse, report);
				
				if(value != null){
					valueMap.put(name, value);
				}
			}
			valueList.add(valueMap);
		}
		
		return valueList;
	}
	/**
	 * 结点解析
	 * @param nodeElement
	 * @param nodeParse
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public static Object parseNode(Element nodeElement, Element nodeParse, Document report) throws Exception{
		Object value = null;
		String nodeName = nodeParse.getName();
		if(isParse(nodeParse, report)){
			//判断是否循环块
			if(nodeName.equals("block")){
				//块返回List
				value = parseBlock(nodeElement, nodeParse, report);
			}else if(nodeName.equals("repeat")){
				//循环块返回list
				value = parseRepeat(nodeElement, nodeParse, report);
			}else{
				//字段解析
				value = getValue(nodeElement, nodeParse);
			}
			
		}
		return value;
	}
	/**
	 * 是否解析该字段或循环块  XPath中间的结点不可有多个平级 如果有只取第一个
	 * @param fieldParse
	 * @param report
	 * @return
	 */
	private static boolean isParse(Element fieldParse, Document report){
		String condition = fieldParse.attributeValue("condition");
		if(condition==null){
			return true;
		}else{
			Node node = report.selectSingleNode(condition);
			if(node==null) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 取值并校验
	 * @param report
	 * @param fieldParse
	 * @return
	 * @throws Exception
	 */
	private static Object getValue(Element nodeElement, Element fieldParse) throws Exception{
		
		String from = fieldParse.attributeValue("from");
		String type = fieldParse.attributeValue("type");//值类型
		String format = fieldParse.attributeValue("format");
		String isNull = fieldParse.attributeValue("isNull");//是否允许为空
		String zhName = fieldParse.attributeValue("zhName");
		String defaultValue = fieldParse.attributeValue("default");//默认值
		
		Element fieldElement = nodeElement.element(from);
		
		if(fieldElement == null){
			throw new Exception("报文字段" + zhName + "[" + from + "]不存在!");
		}
		
		String valueStr = fieldElement.getTextTrim();
		
		//空值校验
		if("false".equals(isNull) && (valueStr==null||valueStr.length()==0)){
			throw new Exception("报文字段" + zhName + "[" + from + "]不允许为空!");
		}
		//格式校验
		if(format!=null &&!"".equals(valueStr)&&!valueStr.matches(format)){
			throw new Exception("报文字段" + zhName + "[" + from + "]格式错误!");
		}
		//默认值
		if((valueStr==null||valueStr.length()==0) && defaultValue!=null){
			valueStr = defaultValue;
		}
		//类型转换
		Object value;
		if("string".equals(type)){
			value = valueStr;
		}else if("int".equals(type)){
			value = new Integer(valueStr);
		}else if("short".equals(type)){
			value = new Short(valueStr);
		}else if("double".equals(type)){
			value = new Double(valueStr);
		}else if("float".equals(type)){
			value = new Float(valueStr);
		}else if("boolean".equals(type)){
			value = new Boolean(valueStr);
		}else if("char".equals(type)){
			value = new Character(valueStr.charAt(0));
		}else {
			value = valueStr;
		}
		return value;
	}
	
}
