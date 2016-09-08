package com.xiekun.sokettransfer.longlink.test;

import java.util.HashMap;
import java.util.Map;

import com.xiekun.sokettransfer.longlink.CmbcKHClent;
import com.xiekun.util.CmbcConfig;

public class TestKH1003 extends Thread{
	int i;
	
	public TestKH1003(int i){
		this.i = i;
	}
	
	public void run() {

		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, String> bodyMap = new HashMap<String, String>();
		reqMap.put("Req", bodyMap);
		bodyMap.put("Version", "1.0"); 
		bodyMap.put("TransDate", "20150305"); 
		bodyMap.put("TransTime", "114026"); 
		bodyMap.put("SerialNo", "20150305A400008" + i); 
		bodyMap.put("MerId", "test"); 
		bodyMap.put("MerName", "test"); 
		bodyMap.put("BizType", "01"); 
		bodyMap.put("BizNo", ""); 
		bodyMap.put("BizObjType", "00"); 
		bodyMap.put("PayerAcc", "6228482830891396812"); 
		bodyMap.put("PayerName", "蒙世花"); 
		bodyMap.put("CardType", "0"); 
		bodyMap.put("PayerBankName", "农业银行"); 
		bodyMap.put("PayerBankInsCode", "01030000"); 
		bodyMap.put("PayerBankSettleNo", ""); 
		bodyMap.put("PayerBankSwitchNo", ""); 
		bodyMap.put("PayerPhone", ""); 
		bodyMap.put("TranAmt", "102"); 
		bodyMap.put("Currency", "RMB"); 
		bodyMap.put("CertType", "ZR01"); 
		bodyMap.put("CertNo", "350821198706134513"); 
		bodyMap.put("ProvNo", "350000");//350000 
		bodyMap.put("CityNo", "");// 350200
		bodyMap.put("AgtNo", ""); 
		bodyMap.put("Purpose", "测试"); 
		bodyMap.put("Postscript", "无"); 
		
//		try {
//			String reqStr = ReportFormat.formatReport(reqMap, "1003");
//			System.out.println(reqStr);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(CmbcKHClent.sendReport(reqMap, "1003", new CmbcConfig()));
	
//		String tmpStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Ans>	<Version>1.0</Version>	<SettDate>20150305</SettDate>	<TransTime>114017</TransTime>	<ReqSerialNo>20150305A4000000</ReqSerialNo>	<ExecType>S</ExecType>	<ExecCode>000000</ExecCode>	<ExecMsg>交易成功</ExecMsg>	<MerId>test</MerId>	<PaySerialNo>2015030500532761</PaySerialNo>	<Resv></Resv></Ans>";
//		System.out.println(ReportParse.parseReport(new ByteArrayInputStream(tmpStr.getBytes()), "1003"));	
			
	}
}
