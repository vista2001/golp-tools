package dev.diagram.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * ����Ϣ
 * 
 * @author ľľ
 * 
 */
public class TfmBlock {
	// ������ͼ�ı��
	private String tfmId;
	// ����
	private String nodeId;
	// ������
	private String nodeType;
	// ���ԭ�ӽ��׵ĺ�������
	private String aopName = "";
	// Ƕ�׵�����ͼidû��Ϊ-1
	private String nestedtfm = "-1";
	// ���������Ϣ
	private String condition;
	// ���ж�̬��id
	private String dllId;
	// ���λ����Ϣ
	private String locationX;
	private String locationY;
	private String height;
	private String width;
	// ��������Ϣ
	private String desc;
	// ������
	private String name;
	// ����չ����
	private List<TfmExtendAop> tfmExtendAopsList = new ArrayList<TfmExtendAop>();

	public TfmBlock() {
		tfmId = "";
		nodeId = "";
		nodeType = "";
		condition = "";
		dllId = "";
		desc = "";
		name = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTfmId() {
		return tfmId;
	}

	public void setTfmId(String tfmId) {
		this.tfmId = tfmId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getAopName() {
		return aopName;
	}

	public void setAopName(String aopName) {
		this.aopName = aopName;
	}

	public String getNestedtfm() {
		return nestedtfm;
	}

	public void setNestedtfm(String nestedtfm) {
		this.nestedtfm = nestedtfm;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDllId() {
		return dllId;
	}

	public void setDllId(String dllId) {
		this.dllId = dllId;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public List<TfmExtendAop> getTfmExtendAopsList() {
		return tfmExtendAopsList;
	}

	public void setTfmExtendAopsList(List<TfmExtendAop> tfmExtendAopsList) {
		this.tfmExtendAopsList = tfmExtendAopsList;
	}

}
