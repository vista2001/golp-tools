package dev.diagram.beans;

/**
 * 异常和补偿的共同操作信息
 * 
 * @author 木木
 * 
 */
public class TfmCommDeal {
	// 所属流程图的id
	private String tfmId;
	// 执行序列编号
	private String seqNo;
	// 函数名称
	private String funcname;
	// 动态库的id
	private String dllId;
	// 名称
	private String name;
	// 描述
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
