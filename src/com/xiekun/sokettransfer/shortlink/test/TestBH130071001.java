package com.xiekun.sokettransfer.shortlink.test;

import java.util.HashMap;
import java.util.Map;

import com.xiekun.sokettransfer.shortlink.CmbcBHClent;
import com.xiekun.util.CmbcConfig;


public class TestBH130071001 {
	public static void main(String[] args) throws Exception{
		Map<String, Object> reqMap = new HashMap<String, Object>();
		Map<String, String> bodyMap = new HashMap<String, String>();
		
		//测试数据0
//		reqMap.put("TRAN_REQ", bodyMap);
//		bodyMap.put("COMPANY_ID", "1300702"); 
//		bodyMap.put("MCHNT_CD", "200290000006283"); 
//		bodyMap.put("TRAN_DATE", "20141114"); 
//		bodyMap.put("TRAN_TIME", "151448"); 
//		bodyMap.put("TRAN_ID", "13007022014111400151448"); 
//		bodyMap.put("BUSI_TYPE", "BUSI_TYPE"); 
//		bodyMap.put("BUSI_NO", "BUSI_NO"); 
//		bodyMap.put("CURRENCY", "RMB"); 
//		bodyMap.put("ACC_NO", "62262229008791"); 
//		bodyMap.put("ACC_NAME", "测试1156804026"); 
//		bodyMap.put("BANK_TYPE", "BANK_TYPE"); 
//		bodyMap.put("BANK_NAME", "BANK_NAME"); 
//		bodyMap.put("TRANS_AMT", "300"); 
//		bodyMap.put("CERT_TYPE", "ZR20"); 
//		bodyMap.put("CERT_NO", "idno1156804026"); 
//		bodyMap.put("CHK_FLAG", "2"); 
//		bodyMap.put("REMARK", "REMARK"); 
//		bodyMap.put("RESV", "RESV"); 
		
		//测试数据1
		reqMap.put("TRAN_REQ", bodyMap);
		bodyMap.put("COMPANY_ID", "1300702"); 
		bodyMap.put("MCHNT_CD", "200290000006283"); 
		bodyMap.put("TRAN_DATE", "20141114"); 
		bodyMap.put("TRAN_TIME", "151448"); 
		bodyMap.put("TRAN_ID", "13007022014111400151448"); 
		bodyMap.put("BUSI_TYPE", "BUSI_TYPE"); 
		bodyMap.put("BUSI_NO", "BUSI_NO"); 
		bodyMap.put("CURRENCY", "RMB"); 
		bodyMap.put("ACC_NO", "6226222900832168"); 
		bodyMap.put("ACC_NAME", "厦门民生测试"); 
		bodyMap.put("BANK_TYPE", "BANK_TYPE"); 
		bodyMap.put("BANK_NAME", "BANK_NAME"); 
		bodyMap.put("TRANS_AMT", "300"); 
		bodyMap.put("CERT_TYPE", "ZR20"); 
		bodyMap.put("CERT_NO", "350201197905060016"); 
		bodyMap.put("CHK_FLAG", "2"); 
		bodyMap.put("REMARK", "REMARK"); 
		bodyMap.put("RESV", "RESV"); 
		
//		{TRAN_REQ={MCHNT_CD=null, BUSI_TYPE=, ACC_NO=6225882114915470, COMPANY_ID=dk_bfkj, CHK_FLAG=2, CERT_NO=411303198305170035, TRAN_TIME=131108, RESV=, REMARK=浙江贝付认证支付, ACC_NAME=谢坤, TRAN_DATE=20150512, BANK_NAME=民生银行, TRAN_ID=dk_bfkj2015051213115352, TRANS_AMT=2, CERT_TYPE=ZR01, BUSI_NO=, CURRENCY=RMB, BANK_TYPE=CMBC}}
//		报文创建
//		try {
//			String reqStr = ReportFormat.formatReport(reqMap, "130071001");
//			System.out.println(reqStr);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		System.out.println(CmbcBHClent.sendReport(reqMap, "130071001", new CmbcConfig()));
		
//		报文解析	
//		String tmpStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRAN_RESP>  <RESP_TYPE>E</RESP_TYPE>  <RESP_CODE>PES10005</RESP_CODE>  <RESP_MSG>输入[14]位帐号[62262229008791]不存在</RESP_MSG>  <COMPANY_ID>1300702</COMPANY_ID>  <MCHNT_CD>200290000006283</MCHNT_CD>  <TRAN_DATE>20141114</TRAN_DATE>  <TRAN_TIME>151448</TRAN_TIME>  <TRAN_ID>13007022014111400151448</TRAN_ID>  <BANK_TRAN_ID>P1301201501260010085906P13590611</BANK_TRAN_ID>  <BANK_TRAN_DATE>20150126</BANK_TRAN_DATE>  <RESV>RESV</RESV></TRAN_RESP>";
//		System.out.println(ReportParse.parseReport(new ByteArrayInputStream(tmpStr.getBytes()), "130071001"));	
	
	}
}
