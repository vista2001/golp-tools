/* �ļ�����      Trade.java
 * ������           ���ļ���������Trade�����������滻ģ�����Trade��
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.27
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.21
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������
 */

package dev.generate.entryFunction;

import java.util.ArrayList;
import java.util.List;

import dev.util.DevLogger;

/**
 * ���ɽ�����ں������滻��������
 * 
 * @author zxh
 * 
 */
public class Trade {

	public String getTradeID() {
		return tradeID;
	}

	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<Req> getReq() {
		return req;
	}

	public void setReq(List<Req> req) {
		this.req = req;
	}

	public List<Resp> getResp() {
		return resp;
	}

	public void setResp(List<Resp> resp) {
		this.resp = resp;
	}

	public String tradeID;
	public String tradeType;
	public String actionName;
	public List<Req> req;
	public List<Resp> resp;

	// ���ݴ���������ݿ����ݳ�ʼ����
	public Trade(String tradeID, String tradeType, String actionName,
			String req, String resp) {

		this.tradeID = tradeID;
		this.tradeType = tradeType;
		this.actionName = actionName;

		String reqtem[] = req.split("\\|");
		DevLogger.printDebugMsg(reqtem.length);
		this.req = new ArrayList<Req>();
		for (String tem : reqtem) {
			this.req.add(new Req(tem));
		}

		String[] resptem = resp.split("\\|");
		this.resp = new ArrayList<Resp>();
		for (int i = 0; i < resptem.length; i++)
			this.resp.add(new Resp(resptem[i]));
	}
}