package com.xiekun.sokettransfer.shortlink;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ResourceBundle;

import com.xiekun.util.StpLogger;

public class BHMsgTransferServer extends Thread{
	
	private boolean runflag = true;
	
	private static ResourceBundle config = ResourceBundle.getBundle("config");
	
	private static String bfIpList = config.getString("bf_ip_list");
	
	public void run() {
		
		ServerSocket serverSocket = null;

		try {
			String serverPort = config.getString("server_bh_port");
			serverSocket = new ServerSocket(Integer.parseInt(serverPort));
			serverSocket.setSoTimeout(10*60*1000);
			System.out.println("本行转发启动成功");
		}catch (Exception e){
			e.printStackTrace();
			return ;
		}
		
		while(runflag){
			Socket server = null;
			Socket client = null;
			InputStream sin = null;
			OutputStream sout = null;
			InputStream cin = null;
			OutputStream cout = null;
			try {
				server = serverSocket.accept();
				
				String ip = server.getInetAddress().getHostAddress();
				if(bfIpList.indexOf(ip) == -1){
					try{
						server.close();
					}catch(Exception ie){}
					
					continue;
				}
				
				sin = server.getInputStream();
				sout = server.getOutputStream();
				
				String cmbcIp = config.getString("cmbc_bh_ip");
				String cmbcPort = config.getString("cmbc_bh_port");
				client = new Socket(cmbcIp, Integer.parseInt(cmbcPort));
				cin = client.getInputStream();
				cout = client.getOutputStream();
				
				byte[] bufferByte = new byte[2048];
				int n = sin.read(bufferByte);
				
				byte[] tmpByte = new byte[n];
				System.arraycopy(bufferByte, 0, tmpByte, 0, n);
				
				cout.write(tmpByte);
				cout.flush();
				
				StpLogger.logInfo("本行转发贝付到民生报文：" + new String(tmpByte, "UTF-8"));
				StpLogger.logInfo("本行转发贝付到民生字节数：" + tmpByte.length);
				
				n = cin.read(bufferByte);
				
				tmpByte = new byte[n];
				System.arraycopy(bufferByte, 0, tmpByte, 0, n);
				sout.write(tmpByte);
				sout.flush();
				
				StpLogger.logInfo("本行转发民生到贝付报文：" + new String(tmpByte, "UTF-8"));
				StpLogger.logInfo("本行转发民生到贝付字节数：" + n);
				
			}catch (SocketTimeoutException e){
//				e.printStackTrace();
			}catch (SocketException e){
				e.printStackTrace();
			}catch (Exception e){
//				e.printStackTrace();
			}finally{
				try {
					if(cin != null){
						cin.close();
					}
					
					if(cout != null){
						cout.close();
					}
					
					if(sin != null){
						sin.close();
					}
					
					if(sout != null){
						sout.close();
					}
					
					if(server != null){
						server.close();
					}
					
					if(client != null){
						client.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		if(serverSocket != null && !serverSocket.isClosed()){
			try {
				serverSocket.close();
			} catch (Exception e) {}
		}
			
	}
	
}
