package dev.diagram.beans;

/**
 * �쳣�Ͳ����Ĺ�ͬ������Ϣ
 * 
 * @author ľľ
 * 
 */
public class TfmCommDeal {
	// ��������ͼ��id
	private String tfmId;
	// ִ�����б��
	private String seqNo;
	// ��������
	private String funcname;
	// ��̬���id
	private String dllId;
	// ����
	private String name;
	// ����
	private String desc;

	public TfmCommDeal() {
		tfmId = "";
		seqNo = "";
		funcname = "";
		dllId = "";
		name = "";
		desc = "";
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTfmId() {
		return tfmId;
	}

	public void setTfmId(String tfmId) {
		this.tfmId = tfmId;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getFuncname() {
		return funcname;
	}

	public void setFuncname(String funcname) {
		this.funcname = funcname;
	}

	public String getDllId() {
		return dllId;
	}

	public void setDllId(String dllId) {
		this.dllId = dllId;
	}
}
