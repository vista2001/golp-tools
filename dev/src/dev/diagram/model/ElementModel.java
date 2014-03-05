package dev.diagram.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * 基本模型，继承于
 * <p>
 * AbstractModel
 * </p>
 * 用于描述模型的最基本的属性，包括 文本显示，位置，大小，编号，出边，入边；
 * 
 * @author 木木
 * 
 */
public abstract class ElementModel extends AbstractModel {

	// 模型编号
	private final int id;
	// 位置和大小信息
	private Rectangle constraint;

	public static final String CONSTRAINT = "constraint";
	public static final String NAME = "name";

	/*
	 * 抽象方法，子类必须继承； 所有继承当前模型的子模型要实现此方法； 作为子模型的返回描述信息的接口
	 */
	public abstract void setText(String text);

	public abstract String getText();

	public abstract String getDesc();

	public abstract String getTypeName();

	public ElementModel() {
		id = generateNumber();
	}

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
		firePropertyChange(ElementModel.CONSTRAINT, null, constraint);
	}

	private static int number = -1;

	/**
	 * 编号自动生成函数
	 * 
	 * @return
	 */
	private int generateNumber() {
		return ++number;
	}

	public int getId() {
		return id;
	}

	public static void setNumber(int n) {
		number = n;
	}

	@Override
	public Object getEditableValue() {

		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {

		return new IPropertyDescriptor[0];
	}

	@Override
	public Object getPropertyValue(Object id) {

		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {

		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {

	}

	@Override
	public void setPropertyValue(Object id, Object value) {

	}

	// 出边链表
	// 入边链表
	protected List<AbstractConnectionModel> sourceConnection = new ArrayList<AbstractConnectionModel>();
	protected List<AbstractConnectionModel> targetConnection = new ArrayList<AbstractConnectionModel>();
	public static final String P_SOURCE_CONNECTION = "source_connection";
	public static final String P_TARGET_CONNECTION = "target_connection";

	public List<AbstractConnectionModel> getSourceConnection() {
		return sourceConnection;
	}

	public List<AbstractConnectionModel> getTargetConnection() {
		return targetConnection;
	}

	/**
	 * 模型节点增加边，此边以模型为源点
	 * 
	 * @param connx
	 */
	public void addSourceConnection(AbstractConnectionModel connx) {

		sourceConnection.add(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, null);
	}

	/**
	 * 模型节点增加边，此边以模型为终点
	 * 
	 * @param connx
	 */
	public void addTargetConnection(AbstractConnectionModel connx) {
		targetConnection.add(connx);
		firePropertyChange(P_TARGET_CONNECTION, null, null);
	}

	/**
	 * 模型节点删除边，此边以模型为起点
	 * 
	 * @param connx
	 */
	public void removeSourceConnection(AbstractConnectionModel connx) {

		sourceConnection.remove(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, null);
	}

	/**
	 * 模型节点删除边，此边以模型为终点
	 * 
	 * @param connx
	 */
	public void removeTargetConnection(AbstractConnectionModel connx) {
		targetConnection.remove(connx);
		firePropertyChange(P_TARGET_CONNECTION, null, null);
	}

	public List<AbstractConnectionModel> getModelSourceConnections() {
		return sourceConnection;
	}

	public List<AbstractConnectionModel> getModelTargetConnections() {
		return targetConnection;
	}

}
