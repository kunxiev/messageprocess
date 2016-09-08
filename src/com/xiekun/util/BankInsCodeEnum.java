/**
 *
 * @copyright	Copyright 2009 5173 Corporation. All rights reserved.
 * 
 * 
 */
package com.xiekun.util;

/**
 * @title
 * @description 联行机构号类型 
 * @usage
 * @copyright Copyright 2009 5173 Corporation. All rights reserved.
 * @company 5173 corporation
 * @author huanggang<huanggang@5173.com>
 * @version AcctAttrTypeEnum.java
 * @create 2011-10-11
 * 
 */
public enum BankInsCodeEnum {

	ICBC_D_B2C("01020000", "工商银行"), 
	ABC_D_B2C("01030000", "农业银行"),
	CCB_D_B2C("01050000", "建设银行"),
	BOCSH_D_B2C("01040000", "中国银行"),
	POSTGC_D_B2C("01000000", "邮储银行"),
	CMB_D_B2C("03080000", "招商银行"),
	CEB_D_B2C("03030000", "光大银行"),
	GDB_D_B2C("03060000", "广发银行"),
	HXB_D_B2C("03040000", "华夏银行"),
	CIB_D_B2C("03090000", "兴业银行"),
	CNCB_D_B2C("03020000", "中信银行"),
	PINGAN_D_B2C("03070000", "平安银行"),
	COMM_D_B2C("03010000", "交通银行"),
	SPDB_D_B2C("03100000", "浦发银行"),
	BOL_D_B2C("04470000", "兰州银行");
	
	private String insCode;

	private String bankName;

	private BankInsCodeEnum(String insCode, String bankName) {
		this.insCode = insCode;
		this.bankName = bankName;
	}

	public String getInsCode() {
		return insCode;
	}

	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public static BankInsCodeEnum getBankInsCodeEnmuByAPI(String bankAPI){
		BankInsCodeEnum[] values = BankInsCodeEnum.values();
		for (BankInsCodeEnum bankInsCodeEnum : values) {
			if(bankInsCodeEnum.name().equals(bankAPI)){
				return bankInsCodeEnum;
			}
		}
		return null;
	}

}
