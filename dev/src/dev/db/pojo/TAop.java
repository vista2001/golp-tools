package dev.db.pojo;

public class TAop {
	private String aopId;
	private String aopName;
	private String aopDesc;
	private String inputData;
	private String outputData;
	private String preCondition;
	private String postCondition;
	private String aopErrRecover;
	private String aopExts;
	private int upAopDll;
	private String aopLevel;
	private String aopRetVal;

	public String getAopId() {
		return aopId;
	}

	public void setAopId(String aopId) {
		this.aopId = aopId;
	}

	public String getAopName() {
		return aopName;
	}

	public void setAopName(String aopName) {
		this.aopName = aopName;
	}

	public String getAopDesc() {
		return aopDesc;
	}

	public void setAopDesc(String aopDesc) {
		this.aopDesc = aopDesc;
	}

	public String getInputData() {
		return inputData;
	}

	public void setInputData(String inputData) {
		this.inputData = inputData;
	}

	public String getOutputData() {
		return outputData;
	}

	public void setOutputData(String outputData) {
		this.outputData = outputData;
	}

	public String getPreCondition() {
		return preCondition;
	}

	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}

	public String getPostCondition() {
		return postCondition;
	}

	public void setPostCondition(String postCondition) {
		this.postCondition = postCondition;
	}

	/**
	 * 返回域aopErrRecover的值。
	 * 
	 * @return 域aopErrRecover的值
	 */
	public String getAopErrRecover() {
		return aopErrRecover;
	}

	/**
	 * 若域aopErrRecover的值为“0”，则返回“0-第一种”， 若域aopErrRecover的值为“1”，则返回“1-第二种”。
	 * 
	 * @return
	 */
	public String getAopErrRecover2() {
		if (aopErrRecover.equals("0")) {
			return "0-第一种";
		} else {
			return "1-第二种";
		}
	}

	/**
	 * 将参数aopErrRecover的值赋给域aopErrRecover。
	 * 
	 * @param aopErrRecover
	 */
	public void setAopErrRecover(String aopErrRecover) {
		this.aopErrRecover = aopErrRecover;
	}

	/**
	 * 将参数aopErrRecover的第一个字符赋给域aopErrRecover。
	 * 
	 * @param aopErrRecover
	 */
	public void setAopErrRecover2(String aopErrRecover) {
		this.aopErrRecover = aopErrRecover.substring(0, 1);
	}

	public String getAopExts() {
		return aopExts;
	}

	public void setAopExts(String aopExts) {
		this.aopExts = aopExts;
	}

	public int getUpAopDll() {
		return upAopDll;
	}

	public void setUpAopDll(int upAopDll) {
		this.upAopDll = upAopDll;
	}

	/**
	 * 返回域aopLevel的值。
	 * 
	 * @return 域aopLevel的值
	 */
	public String getAopLevel() {
		return aopLevel;
	}

	/**
	 * 若域aopLevel的值为“0”，则返回“0-APP”， 若域aopLevel的值为“1”，则返回“1-GOLP”。
	 * 
	 * @return
	 */
	public String getAopLevel2() {
		if (aopLevel.equals("0")) {
			return "0-APP";
		} else {
			return "1-GOLP";
		}
	}

	/**
	 * 将参数aopLevel的值赋给域aopLevel。
	 * 
	 * @param aopLevel
	 */
	public void setAopLevel(String aopLevel) {
		this.aopLevel = aopLevel;
	}

	/**
	 * 将参数aopLevel的第一个字符赋给域aopLevel。
	 * 
	 * @param aopLevel
	 */
	public void setAopLevel2(String aopLevel) {
		this.aopLevel = aopLevel.substring(0, 1);
	}

	public String getAopRetVal() {
		return aopRetVal;
	}

	public void setAopRetVal(String aopRetVal) {
		this.aopRetVal = aopRetVal;
	}

}
