package dev.diagram.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程图定义
 * 
 * @author 木木
 * 
 */
public class TfmBean {
	// 流程图编号
	private String tfmId;
	// 流程图事务类型
	private String tfmType;
	// 流程描述
	private String tfmdesc;
	// 流程图名称
	private String tfmName;
	// 绑定交易的id
	private String tradeId;
	// 流程图异常链表
	private List<TfmException> tfmExceptionList = new ArrayList<TfmException>();
	// 流程图补偿链表
	private List<TfmCompersation> tfmCompersationList = new ArrayList<TfmCompersation>();
	// 流程图边链表
	private List<TfmEdge> tfmEdgesList = new ArrayList<TfmEdge>();
	// 流程图的块链表
	private List<TfmBlock> tfmBlockList = new ArrayList<TfmBlock>();

	public TfmBean() {
		tfmId = "";
		tfmType = "";
		tfmdesc = "";
		tradeId = "";
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTfmName() {
		return tfmName;
	}

	public void setTfmName(String tfmName) {
		this.tfmName = tfmName;
	}

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

	public String getTfmdesc() {
		return tfmdesc;
	}

	public void setTfmdesc(String tfmdesc) {
		this.tfmdesc = tfmdesc;
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
