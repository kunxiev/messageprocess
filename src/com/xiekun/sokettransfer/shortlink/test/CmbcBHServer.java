package com.xiekun.sokettransfer.shortlink.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

import com.xiekun.util.MD5;
import com.xiekun.util.ReportUtil;

public class CmbcBHServer extends Thread{
	private boolean runflag = true;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CmbcBHServer server = new CmbcBHServer();
		server.start();
	}

	public void run() {
		
		ServerSocket ss = null;

		try {
			ss = new ServerSocket(11323);
		}catch (Exception e){
			e.printStackTrace();
			return ;
		}
		
		while(runflag){
			Socket socket = null;
			try {
				socket = ss.accept();

				InputStream in = null;
				OutputStream out = null;
				try{
					in = socket.getInputStream();
					out = socket.getOutputStream();
					
					byte[] lengthB = new byte[6];
					in.read(lengthB);
					int reqLength = ReportUtil.byte2int(lengthB);
					System.out.println("请求报文长度：" + reqLength);
					
					byte[] transCodeB = new byte[15];
					in.read(transCodeB);
					String transCode = ReportUtil.byte2string(transCodeB).trim();
					System.out.println("请求报文交易类型：" + transCode);
					
					byte[] reqB = new byte[reqLength];
					in.read(reqB);
					String reqStr = ReportUtil.byte2string(reqB);
					System.out.println("请求报文：" + reqStr);
					
					byte[] secretKeyB = new byte[32];
					in.read(secretKeyB);
					String md5Str = ReportUtil.byte2string(secretKeyB).trim();
					System.out.println("请求报文MD5串：" + md5Str);
					
					System.out.println("验签结果：" + MD5.verify(reqStr, md5Str, "123456", "UTF-8"));
					
					String respStr = null;
					if("130071001".equals(transCode)){//代扣
						String ReqSerialNo = reqStr.substring(reqStr.indexOf("<TRAN_ID>")+9, reqStr.indexOf("</TRAN_ID>"));
						
//						respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>  <RESP_TYPE>E</RESP_TYPE>  <RESP_CODE>PES10005</RESP_CODE>  <RESP_MSG>输入[14]位帐号[62262229008791]不存在</RESP_MSG>  <COMPANY_ID>1300702</COMPANY_ID>  <MCHNT_CD>200290000006283</MCHNT_CD>  <TRAN_DATE>20141114</TRAN_DATE>  <TRAN_TIME>151448</TRAN_TIME>  <TRAN_ID>"+ReqSerialNo+"</TRAN_ID>  <BANK_TRAN_ID>P1301201501260010085906P13590611</BANK_TRAN_ID>  <BANK_TRAN_DATE>20150126</BANK_TRAN_DATE>  <RESV>RESV</RESV></TRAN_RESP>";
						respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>  <RESP_TYPE>S</RESP_TYPE>  <RESP_CODE>00000000</RESP_CODE>  <RESP_MSG>交易成功完成</RESP_MSG>  <COMPANY_ID>1300701</COMPANY_ID>  <MCHNT_CD>808080450104973</MCHNT_CD>  <TRAN_DATE>20141114</TRAN_DATE>  <TRAN_TIME>154600</TRAN_TIME>  <TRAN_ID>"+ReqSerialNo+"</TRAN_ID>  <BANK_TRAN_ID>P1301201501260010086101P13610111</BANK_TRAN_ID>  <BANK_TRAN_DATE>20150126</BANK_TRAN_DATE>  <RESV>RESV</RESV></TRAN_RESP>";
					}else if("130073001".equals(transCode)){//代扣查询
						String ReqSerialNo = reqStr.substring(reqStr.indexOf("<TRAN_ID>")+9, reqStr.indexOf("</TRAN_ID>"));
						String ReqSerialNo1 = reqStr.substring(reqStr.indexOf("<ORI_TRAN_ID>")+13, reqStr.indexOf("</ORI_TRAN_ID>"));
						
						respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>  <RESP_TYPE>S</RESP_TYPE>  <RESP_CODE>00000000</RESP_CODE>  <RESP_MSG>交易成功完成</RESP_MSG>  <COMPANY_ID>1300701</COMPANY_ID>  <MCHNT_CD>808080450104973</MCHNT_CD>  <TRAN_DATE>20141114</TRAN_DATE>  <TRAN_TIME>155332</TRAN_TIME>  <TRAN_ID>"+ReqSerialNo+"</TRAN_ID>  <ORI_TRAN_DATE>20141114</ORI_TRAN_DATE>  <ORI_TRAN_ID>" + ReqSerialNo1 + "</ORI_TRAN_ID>  <ORI_BANK_TRAN_ID>P1301201501260010086101P13610111</ORI_BANK_TRAN_ID>  <ORI_BANK_TRAN_DATE>20141114</ORI_BANK_TRAN_DATE>  <ORI_TRAN_FLAG>1</ORI_TRAN_FLAG>  <ORI_RESP_CODE>00000000</ORI_RESP_CODE>  <ORI_RESP_MSG>交易处理成功</ORI_RESP_MSG>  <RESV>RESV</RESV></TRAN_RESP>";
					}else if("130073003".equals(transCode)){//代扣对账
						respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>  <RESP_TYPE>S</RESP_TYPE>  <RESP_CODE>00000000</RESP_CODE>  <RESP_MSG>交易成功完成</RESP_MSG></TRAN_RESP>";
					}
					
					byte[] respB = ReportUtil.string2byte(respStr);
					byte[] respLengthB = ReportUtil.int2byte(respB.length, 6);
					byte[] secretKey = MD5.sign(respStr, "123456", "UTF-8").getBytes();
					
					
					byte[] tmpbyte = byteConnect(respLengthB, transCodeB);
					tmpbyte = byteConnect(tmpbyte, respB);
					tmpbyte = byteConnect(tmpbyte, secretKey);
					
					out.write(tmpbyte);
					out.flush();
					
				}catch(Exception e1){
					e1.printStackTrace();
				}finally{
					if(in != null){
						try {
							in.close();
						} catch (Exception e) {}
					}
					
					if(out != null){
						try {
							out.close();
						} catch (Exception e) {}
					}
					
					if(socket != null && !socket.isClosed()){
						try {
							socket.close();
						} catch (Exception e) {}
					}
				}
			
				
			}catch (SocketTimeoutException e){
				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		if(ss != null && !ss.isClosed()){
			try {
				ss.close();
			} catch (Exception e) {}
		}
			
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
