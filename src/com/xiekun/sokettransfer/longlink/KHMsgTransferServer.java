package com.xiekun.sokettransfer.longlink;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.xiekun.util.RSAHelper;

public class KHMsgTransferServer extends Thread{
	
	private static Map<String, Thread> threadMap = new HashMap<String, Thread>();
	
	ExecutorService threadPool = Executors.newFixedThreadPool(100);

	
	private boolean runflag = true;
	
	private static ResourceBundle config = ResourceBundle.getBundle("config");
	
	private static RSAHelper cipher = null;
	
	private static String bfIpList = config.getString("bf_ip_list");
	
	static{
		cipher = new RSAHelper();
		try {
			cipher.initKey(config.getString("cmbc_kh_sign"), config.getString("cmbc_kh_verify"), 2048);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		while(runflag){
			
			ServerSocket serverSocket = null;
			Socket client = null;
			
			InputStream cin = null;
			OutputStream cout = null;
			
			try {
				String cmbcIp = config.getString("cmbc_kh_ip");
				String cmbcPort = config.getString("cmbc_kh_port");
				client = new Socket(cmbcIp, Integer.parseInt(cmbcPort));
				
				cin = client.getInputStream();
				cout = client.getOutputStream();
				
				KHMsgSender sender = KHMsgSender.getInstance(client, cipher, true);
				KHMsgReceiver receiver = KHMsgReceiver.getInstance(this, client, cipher, true);
				
				String serverPort = config.getString("server_kh_port");
				serverSocket = new ServerSocket(Integer.parseInt(serverPort));
				serverSocket.setSoTimeout(10*60*1000);
				System.out.println("跨行转发启动成功");
				
				while(runflag){
					if(sender==null || receiver==null || serverSocket.isClosed()){
						break;
					}
					Socket server = null;
					try {
						server = serverSocket.accept();
						
						String ip = server.getInetAddress().getHostAddress();
						if(bfIpList.indexOf(ip) == -1){
//							System.out.println("非法访问的IP："+ip);
							try{
								server.close();
							}catch(Exception ie){}
							
							sender = KHMsgSender.getInstance(client, cipher, false);
							receiver = KHMsgReceiver.getInstance(this, client, cipher, false);
							
							continue;
						}
						
						threadPool.execute(new KHTransferTask(server, sender, receiver, this));
					}catch (SocketTimeoutException e){
						e.printStackTrace();
					}catch (Exception e){
						e.printStackTrace();
					}
					
					sender = KHMsgSender.getInstance(client, cipher, false);
					receiver = KHMsgReceiver.getInstance(this, client, cipher, false);
				}
				
			}catch (Exception e){
				e.printStackTrace();
				try {
					Thread.sleep(1*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
			}finally{
				try {
					if(cin != null){
						cin.close();
					}
					
					if(cout != null){
						cout.close();
					}
					
					if(client != null){
						client.close();
					}
					
					if(serverSocket != null){
						serverSocket.close();
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}
		
		threadPool.shutdownNow();
	}
	
	/**
	 * 休眠线程进入
	 * @param socket
	 * @return
	 */
	public void put(String key, Thread socket){
		synchronized (threadMap) {
			threadMap.put(key, socket);
		}
	}
	
	/**
	 * 休眠线程退出
	 * @return
	 */
	public Thread get(String key){
		Thread socket = null;
		synchronized (threadMap) {
			socket = (Thread) threadMap.remove(key);
		}
		return socket;
	}
	
}
