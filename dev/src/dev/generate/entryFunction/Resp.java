/* �ļ�����       Resp.java
 * ������           ���ļ���������Resp�����������滻ģ�����Resp��
 * �����ˣ�       zxh
 * ����ʱ�䣺   2013.11.27
 */

package dev.generate.entryFunction;

/**
 * ���ɽ�����ں������滻��������
 * 
 * @author zxh
 * 
 */
public class Resp {

	public String getRespID() {
		return respID;
	}

	public void setRespID(String respID) {
		this.respID = respID;
	}

	public String getIsNeed() {
		return isNeed;
	}

	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}

	public String getComes() {
		return comes;
	}

	public void setComes(String comes) {
		this.comes = comes;
	}

	public String respID;
	public String isNeed;
	public String comes;

	// ���ݴ��������ת�����滻������
	public Resp(String respDate) {

		String[] tem = respDate.split("@");
		respID = tem[0];
		isNeed = tem[1];
		comes = tem[2];

	}
}
