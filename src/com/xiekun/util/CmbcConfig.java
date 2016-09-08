package com.xiekun.util;

public class CmbcConfig {
	
	/**
	 * 本行socket Ip
	 */
//	private String BHIp = "192.168.132.73";
	private String BHIp = "127.0.0.1";
	
	/**
	 * 本行socket port
	 */
	private int BHPort = 5001;

	/**
	 * 本行MD5签名key
	 */
	private String MD5Key = "123456";

	/**
	 * 本行机构编号
	 */
	private String BHInstCode = "13007ZB";
	
	/**
	 * 本行商户编号
	 */
	private String BHMerchantId = "201403110830001";
	/**
	 * 跨行socket Ip
	 */
//	private String KHIp = "192.168.132.73";
	private String KHIp = "127.0.0.1";

	/**
	 * 跨行socket port
	 */
	private int KHPort = 5000;

	/**
	 * 跨行机构编号
	 */
	private String KHInstCode = "dk_zjbf";

	public String getBHIp() {
		return BHIp;
	}

	public void setBHIp(String bHIp) {
		BHIp = bHIp;
	}

	public int getBHPort() {
		return BHPort;
	}

	public void setBHPort(int bHPort) {
		BHPort = bHPort;
	}

	public String getMD5Key() {
		return MD5Key;
	}

	public void setMD5Key(String mD5Key) {
		MD5Key = mD5Key;
	}

	public String getKHIp() {
		return KHIp;
	}

	public void setKHIp(String kHIp) {
		KHIp = kHIp;
	}

	public int getKHPort() {
		return KHPort;
	}

	public void setKHPort(int kHPort) {
		KHPort = kHPort;
	}

	public String getBHInstCode() {
		return BHInstCode;
	}

	public void setBHInstCode(String bHInstCode) {
		BHInstCode = bHInstCode;
	}

	public String getKHInstCode() {
		return KHInstCode;
	}

	public void setKHInstCode(String kHInstCode) {
		KHInstCode = kHInstCode;
	}

	public String getBHMerchantId() {
		return BHMerchantId;
	}

	public void setBHMerchantId(String bHMerchantId) {
		BHMerchantId = bHMerchantId;
	}

}
