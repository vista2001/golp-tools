package dev.diagram.beans;
/**
 * ����ͼ�еı߶���
 * @author ľľ
 *
 */
public class TfmEdge {
	//��������ͼ��id
	private String tfmId;
	//Դ�ڵ㣨�飩�ı��
	private String sourceid;
	//Ŀ��ڵ�ı��
	private String targetid;
	//Ȩֵ
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
