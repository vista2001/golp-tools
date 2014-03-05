/* 文件名：       Req.java
 * 描述：           该文件定义了类Req，该类用于替换模板里的Req。
 * 创建人：       zxh
 * 创建时间：   2013.11.27
 * 修改人：       rxy
 * 修改时间：   2013.12.21
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。
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
