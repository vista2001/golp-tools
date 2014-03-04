package dev.diagram.beans;
/**
 * 流程图中的边定义
 * @author 木木
 *
 */
public class TfmEdge {
	//所属流程图的id
	private String tfmId;
	//源节点（块）的编号
	private String sourceid;
	//目标节点的编号
	private String targetid;
	//权值
	private String weight;
	public TfmEdge()
	{
		tfmId = "";
		sourceid= "";
		targetid = "";
		weight = "-1";
	}
	public String getTfmId() {
		return tfmId;
	}
	public void setTfmId(String tfmId) {
		this.tfmId = tfmId;
	}
	public String getSourceid() {
		return sourceid;
	}
	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}
	public String getTargetid() {
		return targetid;
	}
	public void setTargetid(String targetid) {
		this.targetid = targetid;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	
}
