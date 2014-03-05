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
	 * ������aopDllLevel��ֵ��
	 * 
	 * @return ��aopDllLevel��ֵ
	 */
	public String getAopDllLevel() {
		return aopDllLevel;
	}

	/**
	 * ����aopDllLevel��ֵΪ��0�����򷵻ء�0-APP���� ����aopDllLevel��ֵΪ��1�����򷵻ء�1-GOLP����
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
	 * ������aopDllLevel��ֵ������aopDllLevel��
	 * 
	 * @param aopDllLevel
	 */
	public void setAopDllLevel(String aopDllLevel) {
		this.aopDllLevel = aopDllLevel;
	}

	/**
	 * ������aopDllLevel�ĵ�һ���ַ�������aopDllLevel��
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
