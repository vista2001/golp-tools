/* �ļ�����       Req.java
 * ������           ���ļ���������Req�����������滻ģ�����Req��
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.27
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.21
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������
 */

package dev.generate.entryFunction;

import dev.util.DevLogger;

public class Req {
	public String getReqID() {
		return reqID;
	}

	public void setReqID(String reqID) {
		this.reqID = reqID;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	public String reqID;
	public String isNeed;

	Req(String reqDate) {
		DevLogger.printDebugMsg(reqDate);
		String[] tem = reqDate.split("@");
		reqID = tem[0];
		isNeed = tem[1];
	}
}
