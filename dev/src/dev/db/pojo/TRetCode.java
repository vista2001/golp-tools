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
	 * 返回域retCodeLevel的值。
	 * 
	 * @return 域retCodeLevel的值
	 */
	public String getRetCodeLevel() {
		return retCodeLevel;
	}

	/**
	 * 若域retCodeLevel的值为“0”，则返回“0-APP”， 若域retCodeLevel的值为“1”，则返回“1-GOLP”。
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
	 * 将参数retCodeLevel的值赋给域retCodeLevel。
	 * 
	 * @param retCodeLevel
	 */
	public void setRetCodeLevel(String retCodeLevel) {
		this.retCodeLevel = retCodeLevel;
	}

	/**
	 * 将参数retCodeLevel的第一个字符赋给域retCodeLevel。
	 * 
	 * @param retCodeLevel
	 */
	public void setRetCodeLevel2(String retCodeLevel) {
		this.retCodeLevel = retCodeLevel.substring(0, 1);
	}

}
