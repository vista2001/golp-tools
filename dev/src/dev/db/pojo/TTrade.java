package dev.db.pojo;

public class TTrade {
	private int tradeId;
	private String tradeName;
	private String tradeDesc;

	public int getTradeId() {
		return tradeId;
	}

	public void setTradeId(int tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getTradeDesc() {
		return tradeDesc;
	}

	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("���ױ�ʶ��" + tradeId
				+ System.getProperty("line.separator"));
		stringBuilder.append("�������ƣ�" + tradeName
				+ System.getProperty("line.separator"));
		stringBuilder.append("����������" + tradeDesc);
		return stringBuilder.toString();
	}

}
