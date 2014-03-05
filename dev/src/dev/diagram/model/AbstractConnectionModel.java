package dev.diagram.model;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import dev.diagram.beans.TfmEdge;

/**
 * 边模型的抽象类
 * 
 * @author 木木
 * 
 */
public abstract class AbstractConnectionModel extends AbstractModel {
	// 模型中的边信息
	private TfmEdge tfmEdge = new TfmEdge();
	// 边的起点模型
	private ElementModel source;
	// 边的终点模型
	private ElementModel target;

	public AbstractConnectionModel() {
		super();
	}

	public final static String EDGE = "edge";

	// 返回模型中的边的信息
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
	 * 设置权值
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
	 * 源节点中加入当前边，表示边的一边已和节点连接
	 */
	public void attachSource() {
		if (!source.getModelSourceConnections().contains(this))
			source.addSourceConnection(this);
	}

	/**
	 * 目标节点中加入当前边，表示边的一边已和节点连接
	 */
	public void attachTarget() {
		if (!target.getModelTargetConnections().contains(this))
			target.addTargetConnection(this);
	}

	/**
	 * 源节点中删除当前边，表示边的一边已和节点断开
	 */
	public void detachSource() {
		source.removeSourceConnection(this);
	}

	/**
	 * 目标节点中删除当前边，表示边的一边已和节点断开
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
				vWeight, "权值") };
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
