package dev.db.pojo;

public class TRetCode {
	private String RetCodeId;
	private String retCodeValue;
	private String retCodeDesc;
	private String retCodeLevel;

	public String getRetCodeId() {
		return RetCodeId;
	}

	public void setRetCodeId(String retCodeId) {
		RetCodeId = retCodeId;
	}

	public String getRetCodeValue() {
		return retCodeValue;
	}

	public void setRetCodeValue(String retCodeValue) {
		this.retCodeValue = retCodeValue;
	}

	public String getRetCodeDesc() {
		return retCodeDesc;
	}

	public void setRetCodeDesc(String retCodeDesc) {
		this.retCodeDesc = retCodeDesc;
	}

	/**
	 * ������retCodeLevel��ֵ��
	 * 
	 * @return ��retCodeLevel��ֵ
	 */
	public String getRetCodeLevel() {
		return retCodeLevel;
	}

	/**
	 * ����retCodeLevel��ֵΪ��0�����򷵻ء�0-APP���� ����retCodeLevel��ֵΪ��1�����򷵻ء�1-GOLP����
	 * 
	 * @return
	 */
	public String getRetCodeLevel2() {
		if (retCodeLevel.equals("0")) {
			return "0-APP";
		} else {
			return "1-GOLP";
		}
	}

	/**
	 * ������retCodeLevel��ֵ������retCodeLevel��
	 * 
	 * @param retCodeLevel
	 */
	public void setRetCodeLevel(String retCodeLevel) {
		this.retCodeLevel = retCodeLevel;
	}

	/**
	 * ������retCodeLevel�ĵ�һ���ַ�������retCodeLevel��
	 * 
	 * @param retCodeLevel
	 */
	public void setRetCodeLevel2(String retCodeLevel) {
		this.retCodeLevel = retCodeLevel.substring(0, 1);
	}

}
