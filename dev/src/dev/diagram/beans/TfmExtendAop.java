package dev.diagram.beans;

/**
 * 扩展点定义
 * @author 木木
 *
 */
public class TfmExtendAop extends TfmCommDeal{
	//扩展点所属的节点（块）
	private String nodeid;
	
	public TfmExtendAop() {
		nodeid="";
	}

	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	
}
