/* �ļ�����       ResourcesNodeType.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.30
 * �޸����ݣ�   ע�͵�MessageNodes�� 
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
		ResourceNode[] tmp = { new DllNodes("��̬��", "DLL", parent),
				new DataItemNodes("������", "DATAITEM", parent),
				new RetCodeNodes("��Ӧ��", "RETCODE", parent),
//				new MessageNodes("���ױ���", "MESSAGE", parent),
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
