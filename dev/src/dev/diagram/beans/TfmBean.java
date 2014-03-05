package dev.diagram.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * ����ͼ����
 * 
 * @author ľľ
 * 
 */
public class TfmBean {
	// ����ͼ���
	private String tfmId;
	// ����ͼ��������
	private String tfmType;
	// ��������
	private String tfmdesc;
	// ����ͼ����
	private String tfmName;
	// �󶨽��׵�id
	private String tradeId;
	// ����ͼ�쳣����
	private List<TfmException> tfmExceptionList = new ArrayList<TfmException>();
	// ����ͼ��������
	private List<TfmCompersation> tfmCompersationList = new ArrayList<TfmCompersation>();
	// ����ͼ������
	private List<TfmEdge> tfmEdgesList = new ArrayList<TfmEdge>();
	// ����ͼ�Ŀ�����
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
