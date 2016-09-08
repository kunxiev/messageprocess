package com.xiekun.sokettransfer.longlink;

import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xiekun.util.RSAHelper;
import com.xiekun.util.ReportUtil;
import com.xiekun.util.StpLogger;

public class KHMsgReceiver extends Thread{
	
	private static Socket client = null;
	
	private static KHMsgReceiver receiver = null;
	
	private static KHMsgTransferServer transfer = null;
	
	private static RSAHelper cipher = null;
	
	private static Map<String, Map<String, byte[]>> resultMap = new HashMap<String, Map<String, byte[]>>();
	
	private static boolean runFlag = true;
	
	private static final Logger log = Logger.getLogger(KHMsgReceiver.class);
	
	private KHMsgReceiver(){}
	
	public static KHMsgReceiver getInstance(KHMsgTransferServer server, Socket socket, RSAHelper rsaHelper, boolean isCreate){
		if(receiver == null && isCreate){
			receiver = new KHMsgReceiver();
			cipher = rsaHelper;
			client = socket;
			transfer = server;
			receiver.start();
		}
		return receiver;
	}
	
	public void run() {
		while(runFlag){
			try {
				
				InputStream in = client.getInputStream();
				
				byte[] respLengthB = new byte[8];
				in.read(respLengthB);//8位报文总长度	
				StpLogger.logInfo("respLengthB:"+ new String(respLengthB));
				
				byte[] instCodeB = new byte[8];
				in.read(instCodeB);//8位合作方编号	
				StpLogger.logInfo("instCodeB:"+ new String(instCodeB));
				
				byte[] transCodeB = new byte[8];
				in.read(transCodeB);//8位交易码
				String transCode = new String(transCodeB).trim();
				StpLogger.logInfo("transCodeB:"+ new String(transCodeB));
				
				byte[] respSignLengthB = new byte[4];
				in.read(respSignLengthB);//4位签名域长度		
				
				StpLogger.logInfo("-------------------------------------"+new String(respSignLengthB)+"=");
				int respSignLength = ReportUtil.byte2int(respSignLengthB);
				byte[] respSignB = new byte[respSignLength];
				in.read(respSignB);//签名域值	
				
				int respLength = ReportUtil.byte2int(respLengthB) - instCodeB.length - transCodeB.length - respSignLengthB.length - respSignB.length;
				byte[] respB = new byte[respLength];
				in.read(respB);
				byte[] decryptedBytes = cipher.decryptRSA(respB, false, "UTF-8");
				String resp = new String(decryptedBytes, "UTF-8");//XML报文数据主体密文
				String serialNo = resp.substring(resp.indexOf("<ReqSerialNo>")+13, resp.indexOf("</ReqSerialNo>"));
				
				StpLogger.logInfo("跨行转发民生到贝付报文：" + resp);
				StpLogger.logInfo("跨行转发民生到贝付字节数：" + (respLength+8));
				
				String key = transCode + serialNo;
				System.out.println("回复key：" + key);
				Thread thread = transfer.get(key);
				
				if(cipher.verifyRSA(decryptedBytes, respSignB, false, "UTF-8")){
					Map<String, byte[]> resqMap = new HashMap<String, byte[]>();
					resqMap.put("instCode", instCodeB);
					resqMap.put("transCode", transCodeB);
					resqMap.put("resp", decryptedBytes);
					if(thread != null){
						put(key, resqMap);
					}
				}
				
				if(thread != null){
					thread.interrupt();
				}
				
//			}catch(SocketException e){
//				e.printStackTrace();
			}catch (Exception e) {
				receiver = null;
				log.error(e.getMessage());
				e.printStackTrace();
				break;
			}
			
		}
	}
	
	/**
	 * 结果Map放入
	 * @param socket
	 * @return
	 */
	public void put(String key, Map<String, byte[]> respMap){
		synchronized (resultMap) {
			resultMap.put(key, respMap);
		}
	}
	
	/**
	 * 获取结果头请求Map
	 * @return
	 */
	public Map<String, byte[]> get(String key){
		Map<String, byte[]> respMap = null;
		synchronized (resultMap) {
			respMap = (Map<String, byte[]>) resultMap.remove(key);
		}
		return respMap;
	}
	
}

