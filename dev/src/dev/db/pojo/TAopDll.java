package dev.db.pojo;

public class TAopDll {
	private int aopDllId;
	private String aopDllName;
	private String aopDllDesc;
	private String aopDllType;
	private String aopDllLevel;
	private String aopDllPath;

	public int getAopDllId() {
		return aopDllId;
	}

	public void setAopDllId(int aopDllId) {
		this.aopDllId = aopDllId;
	}

	public String getAopDllName() {
		return aopDllName;
	}

	public void setAopDllName(String aopDllName) {
		this.aopDllName = aopDllName;
	}

	public String getAopDllDesc() {
		return aopDllDesc;
	}

	public void setAopDllDesc(String aopDllDesc) {
		this.aopDllDesc = aopDllDesc;
	}

	public String getAopDllType() {
		return aopDllType;
	}

	public void setAopDllType(String aopDllType) {
		this.aopDllType = aopDllType;
	}

	/**
	 * 返回域aopDllLevel的值。
	 * 
	 * @return 域aopDllLevel的值
	 */
	public String getAopDllLevel() {
		return aopDllLevel;
	}

	/**
	 * 若域aopDllLevel的值为“0”，则返回“0-APP”， 若域aopDllLevel的值为“1”，则返回“1-GOLP”。
	 * 
	 * @return
	 */
	public String getAopDllLevel2() {
		if (aopDllLevel.equals("0")) {
			return "0-APP";
		} else {
			return "1-GOLP";
		}
	}

	/**
	 * 将参数aopDllLevel的值赋给域aopDllLevel。
	 * 
	 * @param aopDllLevel
	 */
	public void setAopDllLevel(String aopDllLevel) {
		this.aopDllLevel = aopDllLevel;
	}

	/**
	 * 将参数aopDllLevel的第一个字符赋给域aopDllLevel。
	 * 
	 * @param aopDllLevel
	 */
	public void setAopDllLevel2(String aopDllLevel) {
		this.aopDllLevel = aopDllLevel.substring(0, 1);
	}

	public String getAopDllPath() {
		return aopDllPath;
	}

	public void setAopDllPath(String aopDllPath) {
		this.aopDllPath = aopDllPath;
	}

}
