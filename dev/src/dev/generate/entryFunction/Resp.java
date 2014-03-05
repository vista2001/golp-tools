/* 文件名：       Resp.java
 * 描述：           该文件定义了类Resp，该类用于替换模板里的Resp。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 */

package dev.generate.entryFunction;

/**
 * 生成交易入口函数的替换子数据类
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

	// 根据传入的数据转换成替换的数据
	public Resp(String respDate) {

		String[] tem = respDate.split("@");
		respID = tem[0];
		isNeed = tem[1];
		comes = tem[2];

	}
}
