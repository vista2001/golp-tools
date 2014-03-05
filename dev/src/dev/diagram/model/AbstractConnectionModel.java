package dev.diagram.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import dev.diagram.beans.TfmEdge;

/**
 * ��ģ�͵ĳ�����
 * 
 * @author ľľ
 * 
 */
public abstract class AbstractConnectionModel extends AbstractModel {
	// ģ���еı���Ϣ
	private TfmEdge tfmEdge = new TfmEdge();
	// �ߵ����ģ��
	private ElementModel source;
	// �ߵ��յ�ģ��
	private ElementModel target;

	public AbstractConnectionModel() {
		super();
	}

	public final static String EDGE = "edge";

	// ����ģ���еıߵ���Ϣ
	public TfmEdge getTfmEdge() {
		tfmEdge.setSourceid(((CommonModel) source).getTfmBlock().getNodeId());
		tfmEdge.setTargetid(((CommonModel) target).getTfmBlock().getNodeId());
		tfmEdge.setTfmId(((CommonModel) source).getTfmBlock().getTfmId());
		tfmEdge.setWeight(String.valueOf(this.getWeight()));
		return tfmEdge;
	}

	public void setTfmEdge(TfmEdge tfmEdge) {
		this.tfmEdge = tfmEdge;
	}

	public void setWeight(String w) {
		tfmEdge.setWeight(w);
		firePropertyChange(EDGE, null, w);

	}

	/**
	 * ����Ȩֵ
	 * 
	 * @return
	 */
	public int getWeight() {
		if (tfmEdge.getWeight().equals(""))
			return -1;
		else
			return Integer.parseInt(tfmEdge.getWeight());
	}

	public void setSource(Object model) {
		source = (ElementModel) model;

	}

	public void setTarget(Object model) {
		target = (ElementModel) model;

	}

	public ElementModel getSource() {
		return source;
	}

	public ElementModel getTarget() {
		return target;
	}

	/**
	 * Դ�ڵ��м��뵱ǰ�ߣ���ʾ�ߵ�һ���Ѻͽڵ�����
	 */
	public void attachSource() {
		if (!source.getModelSourceConnections().contains(this))
			source.addSourceConnection(this);
	}

	/**
	 * Ŀ��ڵ��м��뵱ǰ�ߣ���ʾ�ߵ�һ���Ѻͽڵ�����
	 */
	public void attachTarget() {
		if (!target.getModelTargetConnections().contains(this))
			target.addTargetConnection(this);
	}

	/**
	 * Դ�ڵ���ɾ����ǰ�ߣ���ʾ�ߵ�һ���Ѻͽڵ�Ͽ�
	 */
	public void detachSource() {
		source.removeSourceConnection(this);
	}

	/**
	 * Ŀ��ڵ���ɾ����ǰ�ߣ���ʾ�ߵ�һ���Ѻͽڵ�Ͽ�
	 */
	public void detachTarget() {
		target.removeTargetConnection(this);
	}

	public final static String vWeight = "weight";

	@Override
	public Object getEditableValue() {

		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		IPropertyDescriptor[] discriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(
				vWeight, "Ȩֵ") };
		return discriptors;
	}

	@Override
	public Object getPropertyValue(Object id) {

		if (AbstractConnectionModel.vWeight.equals(id))
			return String.valueOf(getWeight());
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {

		return true;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

}
