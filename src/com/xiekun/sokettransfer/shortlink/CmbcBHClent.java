package com.xiekun.sokettransfer.shortlink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xiekun.util.CmbcConfig;
import com.xiekun.util.MD5;
import com.xiekun.util.ReportFormat;
import com.xiekun.util.ReportParse;
import com.xiekun.util.ReportUtil;

public class CmbcBHClent {
	
	private static final Logger log = Logger.getLogger(CmbcBHClent.class);
	
	public static Map<String, Object> sendReport(Map<String, Object> reqMap, String transCode, CmbcConfig config){
		Map<String, Object> retnMap = new HashMap<String, Object>();
		
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		
		try {
			socket = new Socket(config.getBHIp(), config.getBHPort());
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			//报文长度（定长6位，右对齐，左补零，不包含服务码和密钥识别码）
			//服务码（定长15位，左对齐，右补空）
			//报文体   
			//密钥识别码
			String reqStr = ReportFormat.formatReport(reqMap, transCode);
			log.info("民生本行发送报文：" + reqStr);
			byte[] reqB = ReportUtil.string2byte(reqStr);//报文体
			byte[] reqLengthB = ReportUtil.int2byte(reqB.length, 6);//报文长度
			byte[] transCodeB = ReportUtil.string2byte(transCode, 15, true);//服务码
			byte[] secretKey = MD5.sign(reqStr, config.getMD5Key(), "UTF-8").getBytes();//密钥识别码
			
			byte[] tmpbyte = byteConnect(reqLengthB, transCodeB);
			tmpbyte = byteConnect(tmpbyte, reqB);
			tmpbyte = byteConnect(tmpbyte, secretKey);
			
			out.write(tmpbyte);
			
			out.flush();
			
			System.out.println("发送报文总长度：" + (reqLengthB.length+transCodeB.length+reqB.length+secretKey.length));
			
			byte[] respLengthB = new byte[6];
			in.read(respLengthB);//报文长度
			byte[] transCodeRespB = new byte[15];
			in.read(transCodeRespB);//服务码
			
			int respLength = ReportUtil.byte2int(respLengthB);
			byte[] respB = new byte[respLength];
			in.read(respB);
			String respStr = ReportUtil.byte2string(respB);//报文体
			
			log.info("民生本行接收报文：" + respStr);
			
			byte[] secretKeyBResp = new byte[32];
			in.read(secretKeyBResp);//密钥识别码
			
			if(!transCode.equals(new String(transCodeRespB).trim())){
				log.error("回复报文类型" + new String(transCodeRespB).trim() + "与发送报文类型" + transCode + "不符");
				retnMap.put("ERR_MSG", "回复报文类型不符");
			}else if(!MD5.verify(respStr, new String(secretKeyBResp), config.getMD5Key(), "UTF-8")){
				retnMap.put("ERR_MSG", "回复MD5验签失败");
			}else{
				retnMap.put("TRAN_RESP", ReportParse.parseReport(respStr, transCode).get("TRAN_RESP"));
			}
			
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			retnMap.put("ERR_MSG", "创建短连接失败");
		} catch (IOException e) {
			log.error(e.getMessage());
			retnMap.put("ERR_MSG", "报文发送接收出错");
		} catch (Exception e) {
			log.error(e.getMessage());
			retnMap.put("ERR_MSG", e.getMessage());
		}finally{
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {}
			}
			
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {}
			}
			
			if(socket != null){
				try {
					socket.close();
				} catch (IOException e) {}
			}
		}
		
		return retnMap;
	}
	
	private static byte[] byteConnect (byte[] byte1, byte[] byte2){
		byte[] tmpByte = new byte[byte1.length + byte2.length];
		
		for(int i=0; i<byte1.length; i++){
			tmpByte[i] = byte1[i];
		}
		
		for(int i=0; i<byte2.length; i++){
			tmpByte[i+byte1.length] = byte2[i];
		}
		
		return tmpByte;
	}
	
}
