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
		ResourceNode[] tmp = { new AopNodes("��̬��", "DLL", parent),
				new DataItemNodes("������", "DATAITEM", parent),
				new RetCodeNodes("��Ӧ��", "RETCODE", parent),
				new MessageNodes("���ױ���", "MESSAGE", parent),
				new ServerNodes("�������", "SERVER", parent),
				new TradeNodes("����", "TRADE", parent),
				new AopNodes("ԭ�ӽ���", "AOP", parent),
				new TFMNodes("����ͼ", "TFM", parent) };
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
 * ResourceNode dlls=new ResourceNode("��̬��", "DLL", parent); ResourceNode
 * dataItems=new ResourceNode("������", "DATAITEM", parent); ResourceNode
 * retCodes=new ResourceNode("��Ӧ��", "RETCODE", parent); ResourceNode
 * messages=new ResourceNode("���ױ���", "MESSAGE", parent); ResourceNode
 * servers=new ResourceNode("�������", "SERVER", parent); ResourceNode trades=new
 * ResourceNode("����", "TRADE", parent); ResourceNode aops=new
 * ResourceNode("ԭ�ӽ���", "AOP", parent); ResourceNode tfms=new
 * ResourceNode("����ͼ", "TFM", parent);
 */
// type=new ResourceNode[8];