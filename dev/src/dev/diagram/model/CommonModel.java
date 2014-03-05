package dev.diagram.model;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import dev.diagram.beans.TfmBlock;

/**
 * 一般模型。包含流程图中节点的全部信息，继承于基本模型
 * <p>
 * ElementModel
 * </p>
 * 
 * @author 木木
 * 
 */
public abstract class CommonModel extends ElementModel {
	// 流程图中节点的数据(块的信息)，最终会以此为基础写入XML
	private TfmBlock tfmBlock = new TfmBlock();
	// 节点的类型名称
	private String typeName;
	private ContentsModel contentModel;

	public CommonModel(int typeId, String text) {
		super();
		typeName = text;
		setText(text);
		tfmBlock.setNodeType(String.valueOf(typeId));
		tfmBlock.setNodeId(getId() + "");
	}

	@Override
	public void setText(String text) {
		tfmBlock.setName(text);
		// 激活触发器，表示节点名称改变
		firePropertyChange(ElementModel.NAME, null, text);
	}

	@Override
	public String getDesc() {
		return tfmBlock.getDesc();
	}

	@Override
	public String getText() {
		return tfmBlock.getName();
	}

	public String getTypeName() {
		return typeName;
	}

	/**
	 * 节点是否已经连接，即出边和入边（除了开始和结束节点）是否存在，存在则返回真 图检测时会调用，判断图是否完整
	 * 
	 * @return
	 */
	public boolean isConnected() {
		if ((getSourceConnection().size() == 0 && getTypeId() != 5)
				|| (getTargetConnection().size() == 0 && getTypeId() != 4))
			return false;
		return true;
	}

	public void setTfmBlock(TfmBlock tfmBlock) {
		this.tfmBlock = tfmBlock;
		int nodeId = Integer.parseInt(tfmBlock.getNodeId());
		/*
		 * 在读入的时候得到当前的所有节点中的最大编号，并保存在模型的maxNumber中，用于在给新节赋值时参考，否则编号的值将不断的变大
		 */
		if (getId() <= nodeId) {
			ElementModel.setNumber(nodeId);

		}

	}

	public TfmBlock getTfmBlock() {
		tfmBlock.setTfmId(contentModel.diagramId + "");
		return tfmBlock;
	}

	public int getTypeId() {
		return Integer.parseInt(tfmBlock.getNodeType());
	}

	public static final String TFMID = "tfmid";
	public static final String NODEID = "nodeid";
	public static final String NODETYPE = "nodetype";
	public static final String AOPNAME = "aopname";
	public static final String NESTEDTFM = "nestedname";
	public static final String CONDITION = "condition";
	public static final String DLLID = "dllid";

	/**
	 * 设置模型位置和大小，并通知触发器，同时写入tfmBlock中
	 */
	public void setConstraint(Rectangle rect) {
		super.setConstraint(rect);
		tfmBlock.setLocationX(String.valueOf(rect.x));
		tfmBlock.setLocationY(String.valueOf(rect.y));
		tfmBlock.setHeight(String.valueOf(rect.height));
		tfmBlock.setWidth(String.valueOf(rect.width));
		// 激活触发器，节点的坐标已经改变
		firePropertyChange(ElementModel.CONSTRAINT, null, rect);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] decriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(CommonModel.TFMID, "ID"),
				new TextPropertyDescriptor(CommonModel.NODEID, "节点编号"),
				new TextPropertyDescriptor(CommonModel.NODETYPE, "节点类型"),
				new TextPropertyDescriptor(CommonModel.AOPNAME, "原子交易名称"),
				new TextPropertyDescriptor(CommonModel.DLLID, "动态库"),
		// new TextPropertyDescriptor(CommonModel.CONDITION,"CONDITION")
		};
		return decriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (CommonModel.TFMID.equals(id))
			return tfmBlock.getTfmId();
		if (CommonModel.NODEID.equals(id))
			return tfmBlock.getNodeId();
		if (CommonModel.NODETYPE.equals(id))
			return getTypeName();
		if (CommonModel.AOPNAME.equals(id))
			return tfmBlock.getAopName();
		if (CommonModel.DLLID.equals(id))
			return tfmBlock.getDllId();
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		return false;

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
	}

	/**
	 * 模型增加边，同时将边存入edgeList中，由于一条边连接俩个节点， 所以这里选择在目标节点添加边的时候，同时将边写入edgeList
	 */
	@Override
	public void addTargetConnection(AbstractConnectionModel connx) {
		// 边加入节点模型的边链表中
		// edgeList.add(connx.getTfmEdge());
		super.addTargetConnection(connx);

	}

	/**
	 * 连接的目标节点移除边，同时在edgeList中移除边
	 */
	@Override
	public void removeTargetConnection(AbstractConnectionModel connx) {
		// 将边从模型的边链表中删除
		// edgeList.remove(connx.getTfmEdge());
		super.removeTargetConnection(connx);
	}

	@Override
	public void addSourceConnection(AbstractConnectionModel connx) {
		super.addSourceConnection(connx);
	}

	@Override
	public void removeSourceConnection(AbstractConnectionModel connx) {
		super.removeSourceConnection(connx);
	}

	public ContentsModel getContentModel() {
		return contentModel;
	}

	public void setContentModel(ContentsModel contentModel) {
		this.contentModel = contentModel;
	}

}
