package com.xiekun.util;

public class ReportUtil {
	/**
	 * 整数转字节
	 * @param tmpI
	 * @param bytes
	 * @return
	 */
	public static byte[] int2byte(int tmp, int bytes){
		StringBuffer tmpStr = new StringBuffer(tmp+"");
		int length = tmpStr.length();
		for(int i=0; i<bytes-length; i++){
			tmpStr.insert(0, '0');
		}
		
		return (tmpStr.toString()).getBytes();
	}
	/**
	 * 字节转整数
	 * @param b
	 * @return
	 * @throws Exception 
	 */
	public static int byte2int(byte[] b) throws Exception{
		String lengthS = new String(b).trim();
		if(lengthS==null || !lengthS.matches("^[0-9]+$")){
			throw new Exception("报文长度非法！");
		}
		return  Integer.parseInt(lengthS) ;
	}
	/**
	 * 字符串转字节
	 * @param s
	 * @return
	 */
	public static byte[] string2byte(String s){
		if(s == null){
			return null;
		}
		byte[] b = s.getBytes();
		return b;
	}
	/**
	 * 字符串转字节，前四个字节为长度
	 * @param s
	 * @return
	 */
	public static byte[] string2byte(String s, int length, boolean isRight){
		StringBuffer sb = new StringBuffer(s);
		if(s == null){
			return null;
		}else{
			for(int i=0; i<length-s.length(); i++){
				if(isRight){
					sb.append(" ");
				}else{
					sb.insert(0, " ");
				}
			}
		}
		
		return sb.toString().getBytes();
	}
	/**
	 * 字节转字符串
	 * @param b
	 * @return
	 */
	public static String byte2string(byte[] b){
		return new String(b);
	}
}
