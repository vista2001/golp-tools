package dev.diagram.model;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import dev.diagram.beans.TfmBlock;

/**
 * һ��ģ�͡���������ͼ�нڵ��ȫ����Ϣ���̳��ڻ���ģ��
 * <p>
 * ElementModel
 * </p>
 * 
 * @author ľľ
 * 
 */
public abstract class CommonModel extends ElementModel {
	// ����ͼ�нڵ������(�����Ϣ)�����ջ��Դ�Ϊ����д��XML
	private TfmBlock tfmBlock = new TfmBlock();
	// �ڵ����������
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
		// �����������ʾ�ڵ����Ƹı�
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
	 * �ڵ��Ƿ��Ѿ����ӣ������ߺ���ߣ����˿�ʼ�ͽ����ڵ㣩�Ƿ���ڣ������򷵻��� ͼ���ʱ����ã��ж�ͼ�Ƿ�����
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
		 * �ڶ����ʱ��õ���ǰ�����нڵ��е�����ţ���������ģ�͵�maxNumber�У������ڸ��½ڸ�ֵʱ�ο��������ŵ�ֵ�����ϵı��
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
	 * ����ģ��λ�úʹ�С����֪ͨ��������ͬʱд��tfmBlock��
	 */
	public void setConstraint(Rectangle rect) {
		super.setConstraint(rect);
		tfmBlock.setLocationX(String.valueOf(rect.x));
		tfmBlock.setLocationY(String.valueOf(rect.y));
		tfmBlock.setHeight(String.valueOf(rect.height));
		tfmBlock.setWidth(String.valueOf(rect.width));
		// ����������ڵ�������Ѿ��ı�
		firePropertyChange(ElementModel.CONSTRAINT, null, rect);
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] decriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(CommonModel.TFMID, "ID"),
				new TextPropertyDescriptor(CommonModel.NODEID, "�ڵ���"),
				new TextPropertyDescriptor(CommonModel.NODETYPE, "�ڵ�����"),
				new TextPropertyDescriptor(CommonModel.AOPNAME, "ԭ�ӽ�������"),
				new TextPropertyDescriptor(CommonModel.DLLID, "��̬��"),
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
	 * ģ�����ӱߣ�ͬʱ���ߴ���edgeList�У�����һ�������������ڵ㣬 ��������ѡ����Ŀ��ڵ���ӱߵ�ʱ��ͬʱ����д��edgeList
	 */
	@Override
	public void addTargetConnection(AbstractConnectionModel connx) {
		// �߼���ڵ�ģ�͵ı�������
		// edgeList.add(connx.getTfmEdge());
		super.addTargetConnection(connx);

	}

	/**
	 * ���ӵ�Ŀ��ڵ��Ƴ��ߣ�ͬʱ��edgeList���Ƴ���
	 */
	@Override
	public void removeTargetConnection(AbstractConnectionModel connx) {
		// ���ߴ�ģ�͵ı�������ɾ��
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
