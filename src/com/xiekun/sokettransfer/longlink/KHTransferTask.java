package com.xiekun.sokettransfer.longlink;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

import com.xiekun.util.ReportUtil;

/**
 * @title 		
 * @description	
 * @usage		
 * @author		xiachen <xiachen@ebatong.com>
 * @version		
 * @create		2015-5-26 下午09:18:08 
 */
public class KHTransferTask implements Runnable{
	private Socket server ;//与ERP的short link;
	
	private KHMsgSender sender;//消息发送 long link
	
	private KHMsgReceiver receiver;//消息接收 long link
	
	private KHMsgTransferServer transfer; // 长连接主服务
	
	public KHTransferTask(Socket server,KHMsgSender sender,KHMsgReceiver receiver,KHMsgTransferServer transfer) {
		super();
		this.server = server;
		this.sender = sender;
		this.receiver = receiver;
		this.transfer = transfer;
	}

	@Override
	public void run() {
		InputStream sin = null;
		OutputStream out = null;
		try{
			sin = server.getInputStream();
			
			byte[] reqLengthB = new byte[8];
			sin.read(reqLengthB);
			int reqLength = ReportUtil.byte2int(reqLengthB);
			
			byte[] instCodeB = new byte[8];
			sin.read(instCodeB);
			
			byte[] transCodeB = new byte[8];
			sin.read(transCodeB);
			String transCode = new String(transCodeB).trim();
			
			byte[] reqB = new byte[reqLength];
			sin.read(reqB);
			String reqStr = new String(reqB);
			String serialNo = reqStr.substring(reqStr.indexOf("<SerialNo>")+10, reqStr.indexOf("</SerialNo>"));
			
			Map<String, byte[]> reqMap = new HashMap<String, byte[]>();
			reqMap.put("instCode", instCodeB);
			reqMap.put("transCode", transCodeB);
			reqMap.put("req", reqB);
			
			String key = transCode + serialNo;
			System.out.println("请求key："+key);
			sender.put(reqMap);//发送消息池 消息进入
			transfer.put(key, Thread.currentThread());//线程进入
			try{
				Thread.sleep(10*60*1000);//等待60s结果返回
			}catch (InterruptedException e) {
//				e.printStackTrace();
			}
			Map<String, byte[]> respMap = receiver.get(key);
			if(respMap!=null){//被receiver唤醒
				byte[] respInstCodeB = respMap.get("instCode");
				byte[] respTransCodeB = respMap.get("transCode");
				byte[] respB = respMap.get("resp");
				byte[] respLengthB = ReportUtil.int2byte(respB.length, 8);//报文长度
				
				byte[] tmpbyte = byteConnect(respLengthB, respInstCodeB);
				tmpbyte = byteConnect(tmpbyte, respTransCodeB);
				tmpbyte = byteConnect(tmpbyte, respB);
				
				out = server.getOutputStream();
				out.write(tmpbyte);
				out.flush();
			}else{//主动醒来
				//补单?
				transfer.get(key);//remove掉该线程
			}
		}catch (SocketTimeoutException e){
			e.printStackTrace();
		}catch (SocketException e){
//			e.printStackTrace();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try {
				if(sin != null){
					sin.close();
				}
				
				if(out != null){
					out.close();
				}
				
				if(server != null && !server.isClosed()){
					server.close();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
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
