package dev.model.beans;

import java.util.List;

public class TfmBean {
	private String tfmId;
	private String tfmType;
	private List<TfmException> tfmExceptionList;
	private List<TfmCompersation> tfmCompersationList;
	private List<TfmEdge> tfmEdgesList;
	private List<TfmBlock> tfmBlockList;
	
	public String getTfmId() {
		return tfmId;
	}
	public void setTfmId(String tfmId) {
		this.tfmId = tfmId;
	}
	public String getTfmType() {
		return tfmType;
	}
	public void setTfmType(String tfmType) {
		this.tfmType = tfmType;
	}
	public List<TfmException> getTfmExceptionList() {
		return tfmExceptionList;
	}
	public void setTfmExceptionList(List<TfmException> tfmExceptionList) {
		this.tfmExceptionList = tfmExceptionList;
	}
	public List<TfmEdge> getTfmEdgesList() {
		return tfmEdgesList;
	}
	public void setTfmEdgesList(List<TfmEdge> tfmEdgesList) {
		this.tfmEdgesList = tfmEdgesList;
	}
	public List<TfmCompersation> getTfmCompersationList() {
		return tfmCompersationList;
	}
	public void setTfmCompersationList(List<TfmCompersation> tfmCompersationList) {
		this.tfmCompersationList = tfmCompersationList;
	}
	public List<TfmBlock> getTfmBlockList() {
		return tfmBlockList;
	}
	public void setTfmBlockList(List<TfmBlock> tfmBlockList) {
		this.tfmBlockList = tfmBlockList;
	}
	
}
