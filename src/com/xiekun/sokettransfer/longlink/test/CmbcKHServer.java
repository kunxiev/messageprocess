package com.xiekun.sokettransfer.longlink.test;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import com.xiekun.util.RSAHelper;
import com.xiekun.util.ReportUtil;

public class CmbcKHServer extends Thread{
	private boolean runflag = true;
	private static RSAHelper cipher = null;
	
	//本地测试
	public static final String privKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDW+J3i8/zTkMvDyCftOsGNNVEmoodxUr23VeYUW9UI6do8U83ExJVtF9vX2O/RUru7ExJTMOxVg2QZh/IekFFv67iVPSfGBX70DC8vobZKEfhE8V82tWmCtH45zx6U73hVALiV6X8EslIosdT9xWAMY2NH0HvZ7V/06SvNNO0zKOm2vPyspzbwRDUpA89dmjEAwAlDx12/X+EYAR8VEU9ctLjVwYFXuFjc/BCryColK9HIg21P3vubHZM6GKIXfq6zJawV8lTkJlb5QEdcNk33VjpMhGs7ZRzJXgMG07uaPIBI6k7VQ2UZM7GtKUH8KBfWzViQ+Asz2cpE0t0VUoF9AgMBAAECggEBAKh9zwp+oDCW8g7vB9RZ1DDAlG2KwEwjRQ24pxBX9f75hBL6wHI0fsY2CBsDLtzLUtdLGHbaBrLzu/aC5lPsW9g0UsWuXElKL3pLPoS/5CfkM8qdwToZMKzAmZrn6xljJNbDLOpbTDI7Lkg1MjMBi8nJ8JvuHdTux+InDCzYCf6o51aZN2XMLmut4e5zEdjHHknV1On2Jjgc5Gu0CbzrLTUpwOlhICRGiWQY/Mnt73HJvZv7EQ0FjZk7UnWEweEfbgDBok283z6NaClvU5/MUaND2wWVdJfNPaUjHYtf7cE9S3rNrY5mpwVdAHmvAFKfx9PhB4A0hnY2h+i1VmLjBoECgYEA9542ca50sXNsbWwqW3T/78ZWKmQi9nP4MAtZkXzEFJtgEyIXT5CU6J8L1RhZ80J9tokR9FH3cwNUZl9/PiGD8l6jTjd08LfSW9t9z+4E0M6XelHG7ZfRtfV3HgzIHaBWI+nxd7yg4BoY21mgTmypz+88aPHDvC8Ss8T3jkJZul0CgYEA3j9+5JXt9sYbfw2Yln3XSRAXS9MDWRCNK/NZiQ4RZ22DpMOjNanH4jypIpuV1pnU8BM1iv4e5l49Kx2I5vqnE/GDkouhEzM8vPhBKHKLXloKtziIAR0lJOGGER2FtU3iuqSb7nRjt53G9OSnfJ2IL7H6Br7L0O+r679ofKiSsaECgYB2T0iyHmmxE3Yd/g1q70cN+FTZIkk2Ogi+Y93izpsdQXOxEJvUrz8Gul877MulmAJawbkrZDJ36IJd+4jfVcImfqNGTub30MyYiRHe1FnGrr7fec0zXlObvfGxEOhYh3BA7pkp3Z18FdwEihk2/2JPcH4LomAkPNWRwS2K8hbPHQKBgQCcBYdXgcmkzD7RWwIb5AwWxq0UFfbrt6rjh9r7VFzzdvZL3Ove6GnicSNroD34gdXzFAkqomue3dmjQwCw5pYUciAj6NITYIzrPHzBoGgmvJ95ML6JyaQh2BD+QvNy7FKXJKgzJpI6fREHKt5JpW3NzevwgFElRJw0zBLWMKGLAQKBgCXMuSed+xQk5q8of4SYAoHi+1JFQQw7GLa06PpphmlUJxj8WBijnpPJSfktLrgx9SAQ5Bfqw4cGiRsS7zIuc+FDrSSnQrHCwrRmgsaTm4kvqxglcbJAnbuNescAPPkvkK9ZI2gMsIHeHMi0lLSkkwO17IvdhztJbUzFhOSSiMe6";
	//本地测试
	public static final String pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2e5OE+yXcPEB1ZJFNOIS+ATTDQohVV/Pg1KyH+F8j0wIHKweNrCcchxicclA/xejhahXKsONzzjptQFzOe0y3Qi49gNIdfzsxwrPkkg7m5zovqjCag38UcTuTkbSWS4vl3G+1UmIK7jHx8Ec/tb0e92BJuXHcOvmYx0T+IfJUHnsKUDWAaJPZAIDoAeTISPIDKsHEvoa17rAmOXxXTEaDlkAppT93Ekjywc05zpgFLDqg30fvSQc1Jrk+TUqy/iNdhC02fTR6+zhT9uqA/c0D8dAYX0Gspren9no2IR/fUq9UUpcpiIXlYYHXdu3gupuIdtLcKg9wuZpIbg5aTMtJwIDAQAB";
		
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CmbcKHServer server = new CmbcKHServer();
		
		if(cipher == null){
			cipher = new RSAHelper();
			try {
				cipher.initKey(privKey, pubKey, 2048);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		server.start();
	}

	public void run() {
		
		ServerSocket ss = null;

		try {
			ss = new ServerSocket(9006);
		}catch (Exception e){
			e.printStackTrace();
			return ;
		}
		
		while(runflag){
			Socket socket = null;
			try {
				socket = ss.accept();
				
				System.out.println("连接过一次了！！！！！！！！！！！！！！");

				InputStream in = null;
				OutputStream out = null;
				try{
					in = socket.getInputStream();
					out = socket.getOutputStream();
					
					while(true){
						if(in.available()<=0) {
							Thread.sleep(100);
							continue;
						}
						
						byte[] lengthB = new byte[8];
						in.read(lengthB);
						int reqLength = ReportUtil.byte2int(lengthB);
						System.out.println("请求报文长度：" + reqLength);
						
						if(reqLength>0){
							byte[] instCodeB = new byte[8];
							in.read(instCodeB);
							String instCode = ReportUtil.byte2string(instCodeB).trim();
							System.out.println("请求报文合作方编号：" + instCode);
							
							byte[] transCodeB = new byte[8];
							in.read(transCodeB);
							String transCode = ReportUtil.byte2string(transCodeB).trim();
							System.out.println("请求报文交易类型：" + transCode);
							
							byte[] signLengthB = new byte[4];
							in.read(signLengthB);
							int reqsignLength = ReportUtil.byte2int(signLengthB);
							System.out.println("请求报文签名域长度：" + reqsignLength);
							
							byte[] reqSignB = new byte[reqsignLength];
							in.read(reqSignB);
							String reqSignStr = ReportUtil.byte2string(reqSignB);
							System.out.println("请求报文签名域：" + reqSignStr);
							
							System.out.println("请求报文长度：" + (reqLength - instCodeB.length - transCodeB.length - signLengthB.length - reqSignB.length));
							
							byte[] reqB = new byte[reqLength - instCodeB.length - transCodeB.length - signLengthB.length - reqSignB.length];
							in.read(reqB);
							byte[] decryptedBytes = cipher.decryptRSA(reqB, false, "UTF-8");
							String reqStr = ReportUtil.byte2string(decryptedBytes);
							System.out.println("请求报文：" + reqStr);
							
							String ReqSerialNo = reqStr.substring(reqStr.indexOf("<SerialNo>")+10, reqStr.indexOf("</SerialNo>"));
							
							System.out.println("验签结果：" + cipher.verifyRSA(decryptedBytes, reqSignB, false, "UTF-8"));
							
//							try{
//								Thread.sleep(RandomUtils.nextInt(10)*1000);
//							}catch(InterruptedException e){}
							
							String respStr = null;
							if("1003".equals(transCode)){//代扣
								respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ans><Version>1.0</Version><SettDate>20150305</SettDate><TransTime>114017</TransTime><ReqSerialNo>"+ReqSerialNo+"</ReqSerialNo><ExecType>S</ExecType><ExecCode>000000</ExecCode><ExecMsg>交易成功</ExecMsg><MerId>test</MerId><PaySerialNo>2015030500532761</PaySerialNo><Resv></Resv></Ans>";
							}else if("3003".equals(transCode)){//代扣查询
								String oriReqSerialNo = reqStr.substring(reqStr.indexOf("<OriReqSerialNo>")+16, reqStr.indexOf("</OriReqSerialNo>"));
								respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ans><Version>1.0</Version><SettDate>20150305</SettDate><TransTime>124929</TransTime><ReqSerialNo>"+ReqSerialNo+"</ReqSerialNo><ExecType>S</ExecType><ExecCode>000000</ExecCode><ExecMsg>交易成功</ExecMsg><MerId></MerId><OriReqSerialNo>"+oriReqSerialNo+"</OriReqSerialNo><OriSettDate>20150305</OriSettDate><OriTransTime>114011</OriTransTime><OriPaySerialNo>2015030500532761</OriPaySerialNo><OriExecType>S</OriExecType><OriExecCode>000000</OriExecCode><OriExecMsg>交易成功</OriExecMsg><Resv>预留</Resv></Ans>";
							}else if("1004".equals(transCode)){//实名认证
								respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ans><Version>1.0</Version><SettDate>20150305</SettDate><TransTime>125503</TransTime><ReqSerialNo>"+ReqSerialNo+"</ReqSerialNo><ExecType>S</ExecType><ExecCode>000000</ExecCode><ExecMsg>交易成功</ExecMsg><ValidateStatus>00</ValidateStatus><PaySerialNo>2015030500532769</PaySerialNo><Resv></Resv></Ans>";
							}else if("3004".equals(transCode)){//实名认证查询
								String oriReqSerialNo = reqStr.substring(reqStr.indexOf("<OriReqSerialNo>")+16, reqStr.indexOf("</OriReqSerialNo>"));
								respStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ans><Version>1.0</Version><SettDate>20150305</SettDate><TransTime>125748</TransTime><ReqSerialNo>"+ReqSerialNo+"</ReqSerialNo><ExecType>S</ExecType><ExecCode>000000</ExecCode><ExecMsg>交易成功</ExecMsg><ValidateNo></ValidateNo><ValidateStatus>00</ValidateStatus><MerId></MerId><OriReqSerialNo>"+oriReqSerialNo+"</OriReqSerialNo><OriSettDate>20150305</OriSettDate><OriTransTime>125502</OriTransTime><OriPaySerialNo>2015030500532769</OriPaySerialNo><OriExecType>S</OriExecType><OriExecCode>000000</OriExecCode><OriExecMsg>交易成功</OriExecMsg><Resv>预留</Resv></Ans>";
							}
							
							byte[] respB = ReportUtil.string2byte(respStr);
							byte[] instCodeRespB = ReportUtil.string2byte(new String(instCode), 8, false);
							byte[] transCodeRespB = ReportUtil.string2byte(transCode, 8, false);
							byte[] signBytes = cipher.signRSA(respB, false, "UTF-8");
							byte[] signLengthRespB = ReportUtil.int2byte(signBytes.length, 4);
							byte[] cryptedBytesResp = cipher.encryptRSA(respB, false, "UTF-8");
							byte[] respLengthB = ReportUtil.int2byte(instCodeRespB.length+transCodeRespB.length+signLengthRespB.length+signBytes.length+cryptedBytesResp.length, 8);
							
							
							byte[] tmpbyte = byteConnect(respLengthB, instCodeRespB);
							tmpbyte = byteConnect(tmpbyte, transCodeRespB);
							tmpbyte = byteConnect(tmpbyte, signLengthRespB);
							tmpbyte = byteConnect(tmpbyte, signBytes);
							tmpbyte = byteConnect(tmpbyte, cryptedBytesResp);
							
							out.write(tmpbyte);
							
							out.flush();
							
							System.out.println("回复报文OK");
						}else{
							System.out.println("心跳嘭~嘭");
						}
						
					}
					
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
