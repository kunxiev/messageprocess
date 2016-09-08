package com.xiekun.sokettransfer.longlink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xiekun.util.CmbcConfig;
import com.xiekun.util.ReportFormat;
import com.xiekun.util.ReportParse;
import com.xiekun.util.ReportUtil;

public class CmbcKHClent {
	
	private static final Logger log = Logger.getLogger(CmbcKHClent.class);
	
	public static Map<String, Object> sendReport(Map<String, Object> reqMap, String transCode, CmbcConfig config){
		Map<String, Object> retnMap = new HashMap<String, Object>();
		
		Socket socket = null;
		InputStream in = null;
		OutputStream out = null;
		
		try {
			socket = new Socket(config.getKHIp(), config.getKHPort());
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			String reqStr = ReportFormat.formatReport(reqMap, transCode);
			byte[] reqB = ReportUtil.string2byte(reqStr);//报文体
			log.info("民生跨行发送报文：" + reqStr);
			byte[] reqLengthB = ReportUtil.int2byte(reqB.length, 8);//报文长度
			byte[] transCodeB = ReportUtil.string2byte(transCode, 8, true);//服务码
			byte[] instCodeB = ReportUtil.string2byte(config.getKHInstCode(), 8, false);//8位合作方编号
			
			byte[] tmpbyte = byteConnect(reqLengthB, instCodeB);
			tmpbyte = byteConnect(tmpbyte, transCodeB);
			tmpbyte = byteConnect(tmpbyte, reqB);
			
			out.write(tmpbyte);
			out.flush();
			
			System.out.println("发送报文总长度：" + (tmpbyte.length));
			
			byte[] respLengthB = new byte[8];
			in.read(respLengthB);//报文长度
			
			byte[] insCodeRespB = new byte[8];
			in.read(insCodeRespB);//8位合作方编号
			
			byte[] transCodeRespB = new byte[8];
			in.read(transCodeRespB);//服务码
			
			int respLength = ReportUtil.byte2int(respLengthB);
			byte[] respB = new byte[respLength];
			in.read(respB);
			String respStr = ReportUtil.byte2string(respB);//报文体
			
			log.info("民生跨行接收报文：" + respStr);
			
			if(!config.getKHInstCode().equals(new String(insCodeRespB).trim())){
				log.error("回复报文机构编号" + new String(insCodeRespB).trim() + "与发送报文机构编号" + config.getKHInstCode() + "不符");
				retnMap.put("ERR_MSG", "回复报文机构编号不符");
			}else if(!transCode.equals(new String(transCodeRespB).trim())){
				log.error("回复报文类型" + new String(transCodeRespB).trim() + "与发送报文类型" + transCode + "不符");
				retnMap.put("ERR_MSG", "回复报文类型不符");
			}else{
				retnMap.put("Ans", ReportParse.parseReport(respStr, transCode).get("Ans"));
			}
			
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			retnMap.put("ERR_MSG", "创建短连接失败");
		} catch (IOException e) {
			log.error(e.getMessage());
			retnMap.put("ERR_MSG", "报文发送接收出错");
		} catch (Exception e) {
			e.printStackTrace();
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
