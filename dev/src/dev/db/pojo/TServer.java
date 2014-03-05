package dev.db.pojo;

public class TServer {
	private String serverId;
	private String serverName;
	private String serverDesc;
	private String serverSpecLib;
	private String serverSpecInclude;
	private String callbackSource;
	private String othFunSource;
	private String serverLevel;
	private String serverSpecMarco;
	private String serverSpecObj;

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerDesc() {
		return serverDesc;
	}

	public void setServerDesc(String serverDesc) {
		this.serverDesc = serverDesc;
	}

	public String getServerSpecLib() {
		return serverSpecLib;
	}

	public void setServerSpecLib(String serverSpecLib) {
		this.serverSpecLib = serverSpecLib;
	}

	public String getServerSpecInclude() {
		return serverSpecInclude;
	}

	public void setServerSpecInclude(String serverSpecInclude) {
		this.serverSpecInclude = serverSpecInclude;
	}

	public String getCallbackSource() {
		return callbackSource;
	}

	public void setCallbackSource(String callbackSource) {
		this.callbackSource = callbackSource;
	}

	public String getOthFunSource() {
		return othFunSource;
	}

	public void setOthFunSource(String othFunSource) {
		this.othFunSource = othFunSource;
	}

	/**
	 * ������serverLevel��ֵ��
	 * 
	 * @return ��serverLevel��ֵ
	 */
	public String getServerLevel() {
		return serverLevel;
	}

	/**
	 * ����serverLevel��ֵΪ��0�����򷵻ء�0-APP���� ����serverLevel��ֵΪ��1�����򷵻ء�1-GOLP����
	 * 
	 * @return
	 */
	public String getServerLevel2() {
		if (serverLevel.equals("0")) {
			return "0-APP";
		} else {
			return "1-GOLP";
		}
	}

	/**
	 * ������serverLevel��ֵ������serverLevel��
	 * 
	 * @param serverLevel
	 */
	public void setServerLevel(String serverLevel) {
		this.serverLevel = serverLevel;
	}

	/**
	 * ������serverLevel�ĵ�һ���ַ�������serverLevel��
	 * 
	 * @param serverLevel
	 */
	public void setServerLevel2(String serverLevel) {
		this.serverLevel = serverLevel.substring(0, 1);
	}

	public String getServerSpecMarco() {
		return serverSpecMarco;
	}

	public void setServerSpecMarco(String serverSpecMarco) {
		this.serverSpecMarco = serverSpecMarco;
	}

	public String getServerSpecObj() {
		return serverSpecObj;
	}

	public void setServerSpecObj(String serverSpecObj) {
		this.serverSpecObj = serverSpecObj;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("��������ʶ��" + serverId
				+ System.getProperty("line.separator"));
		stringBuilder.append("����������ƣ�" + serverName
				+ System.getProperty("line.separator"));
		stringBuilder.append("�������������" + serverDesc);
		return stringBuilder.toString();
	}

}
