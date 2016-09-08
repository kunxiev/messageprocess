package com.xiekun.sokettransfer.longlink;

import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import com.xiekun.util.RSAHelper;
import com.xiekun.util.ReportUtil;
import com.xiekun.util.StpLogger;

public class KHMsgSender extends Thread{
	
	private static Socket client = null;
	
	private static KHMsgSender sender = null;
	
	private static RSAHelper cipher = null;
	
	private static LinkedList<Map<String, byte[]>> sendList = new LinkedList<Map<String, byte[]>>();
	
	private static boolean runFlag = true;
	
	private static final Logger log = Logger.getLogger(KHMsgSender.class);
	
	private KHMsgSender(){}
	
	public static KHMsgSender getInstance(Socket socket, RSAHelper rsaHelper, boolean isCreate){
		if(sender == null & isCreate){
			sender = new KHMsgSender();
			cipher = rsaHelper;
			client = socket;
			sender.start();
		}
		return sender;
	}
	
	public void run() {
		while(runFlag){
			if(isEmpty()){
				OutputStream out = null;
				
				try {
					
					out = client.getOutputStream();
					
					byte[] reqLengthB = ReportUtil.int2byte(0, 8);
					
					out.write(reqLengthB);//8位报文总长度
					out.flush();
					
					System.out.println("心跳信号：嘭嘭！~");
					
					if(isEmpty()){
						try {
							synchronized (sender) {
								sender.wait(30*1000);
							}
							
						} catch (InterruptedException e) {
							System.out.println("起来干活了~~~~~~~~~~");
						}
					}
					
				} catch (Exception e) {
					sender = null;
					log.error(e.getMessage());
					e.printStackTrace();
					break;
				}
			}else{
				OutputStream out = null;
				try {
					out = client.getOutputStream();
					
					Map<String, byte[]> reqMap = get();
					
					byte[] reqB = reqMap.get("req");
					StpLogger.logInfo("跨行转发贝付到民生报文：" + new String(reqB, "UTF-8"));
					byte[] instCodeB = reqMap.get("instCode");//8位合作方编号
					byte[] transCodeB = reqMap.get("transCode");//8位交易码
					byte[] signBytes = cipher.signRSA(reqB, false, "UTF-8");//签名域值
					byte[] signLengthB = ReportUtil.int2byte(signBytes.length, 4);//4位签名域长度
					byte[] cryptedBytes = cipher.encryptRSA(reqB, false, "UTF-8");//XML报文数据主体密文
					byte[] reqLengthB = ReportUtil.int2byte(instCodeB.length+transCodeB.length+signLengthB.length+signBytes.length+cryptedBytes.length, 8);//8位报文总长度
					
					byte[] tmpByte = byteConnect(reqLengthB, instCodeB);
					tmpByte = byteConnect(tmpByte, transCodeB);
					tmpByte = byteConnect(tmpByte, signLengthB);
					tmpByte = byteConnect(tmpByte, signBytes);
					tmpByte = byteConnect(tmpByte, cryptedBytes);
					
					out.write(tmpByte);
					out.flush();
					StpLogger.logInfo("跨行转发贝付到民生字节数：" + tmpByte.length);
					
//				}catch(SocketException e){
//					e.printStackTrace();
				}catch (Exception e) {
					sender = null;
					e.printStackTrace();
					break;
				}
			
			}
		}
	}
	
	/**
	 * 请求Map进入队列尾
	 * @param socket
	 * @return
	 */
	public boolean put(Map<String, byte[]> reqMap){
		synchronized (sendList) {
			if(sendList.size()>10000){
				return false;
			}
			sendList.addLast(reqMap);
			synchronized (sender) {
				sender.notify();
			}
		}
		return true;
	}
	
	/**
	 * 队列头请求Map出队列
	 * @return
	 */
	public Map<String, byte[]> get(){
		Map<String, byte[]> reqMap = null;
		synchronized (sendList) {
			reqMap = (Map<String, byte[]>) sendList.removeFirst();
		}
		return reqMap;
	}
	
	/**
	 * 获取当前队列是否为空
	 * @return
	 */
	public boolean isEmpty(){
		synchronized (sendList) {
			return sendList.isEmpty();
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
