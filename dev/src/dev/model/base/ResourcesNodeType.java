package dev.model.base;

import dev.model.resource.AopNodes;
import dev.model.resource.DataItemNodes;
import dev.model.resource.MessageNodes;
import dev.model.resource.RetCodeNodes;
import dev.model.resource.ServerNodes;
import dev.model.resource.TFMNodes;
import dev.model.resource.TradeNodes;

public class ResourcesNodeType {
	public  ResourceNode[] type;
	
	public ResourcesNodeType(TreeNode parent) {
		ResourceNode[] tmp = { new AopNodes("动态库", "DLL", parent),
				new DataItemNodes("数据项", "DATAITEM", parent),
				new RetCodeNodes("响应码", "RETCODE", parent),
				new MessageNodes("交易报文", "MESSAGE", parent),
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

/*
 * private ResourceNode dlls; private ResourceNode dataItems; private
 * ResourceNode retCodes; private ResourceNode messages; private ResourceNode
 * servers; private ResourceNode trades; private ResourceNode aops; private
 * ResourceNode tfms;
 */
// private List<ResourceNode> list;
/*
 * ResourceNode dlls=new ResourceNode("动态库", "DLL", parent); ResourceNode
 * dataItems=new ResourceNode("数据项", "DATAITEM", parent); ResourceNode
 * retCodes=new ResourceNode("响应码", "RETCODE", parent); ResourceNode
 * messages=new ResourceNode("交易报文", "MESSAGE", parent); ResourceNode
 * servers=new ResourceNode("服务程序", "SERVER", parent); ResourceNode trades=new
 * ResourceNode("交易", "TRADE", parent); ResourceNode aops=new
 * ResourceNode("原子交易", "AOP", parent); ResourceNode tfms=new
 * ResourceNode("流程图", "TFM", parent);
 */
// type=new ResourceNode[8];