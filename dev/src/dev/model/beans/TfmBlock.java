package dev.model.beans;

import java.util.List;

public class TfmBlock {
	private String tfmId;
	private String nodeId;
	private String nodeType;
	private String aopName="";
	private String nestedtfm="-1";
	private String condition;
	private String dllId;
	private String locationX;
	private String locationY;
	private String height;
	private String width;
	
	private List<TfmCommDeal> tfmExtendAopsList;
	
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
	public List<TfmCommDeal> getTfmExtendAopsList() {
		return tfmExtendAopsList;
	}
	public void setTfmExtendAopsList(List<TfmCommDeal> tfmExtendAopsList) {
		this.tfmExtendAopsList = tfmExtendAopsList;
	}
	
}
