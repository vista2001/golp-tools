/* 文件名：       ResourcesNodeType.java
 * 修改人：       rxy
 * 修改时间：   2013.12.30
 * 修改内容：   注释掉MessageNodes。 
 */

package dev.model.base;

import dev.model.resource.AopNodes;
import dev.model.resource.DataItemNodes;
import dev.model.resource.DllNodes;
import dev.model.resource.RetCodeNodes;
import dev.model.resource.ServerNodes;
import dev.model.resource.TFMNodes;
import dev.model.resource.TradeNodes;

public class ResourcesNodeType {
	public  ResourceNode[] type;
	
	public ResourcesNodeType(TreeNode parent) {
		ResourceNode[] tmp = { new DllNodes("动态库", "DLL", parent),
				new DataItemNodes("数据项", "DATAITEM", parent),
				new RetCodeNodes("响应码", "RETCODE", parent),
//				new MessageNodes("交易报文", "MESSAGE", parent),
				new ServerNodes("服务程序", "SERVER", parent),
				new TradeNodes("交易", "TRADE", parent),
				new AopNodes("原子交易", "AOP", parent),
				new TFMNodes("流程图", "TFM", parent) };
		this.setType(tmp);
	}
	
	private void setType(ResourceNode[] type) {
		this.type = type;
	}

	public ResourceNode[] getType() {
		return this.type;
	}

}
