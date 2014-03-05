package dev.diagram.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * ����ģ�ͣ��̳���
 * <p>
 * AbstractModel
 * </p>
 * ��������ģ�͵�����������ԣ����� �ı���ʾ��λ�ã���С����ţ����ߣ���ߣ�
 * 
 * @author ľľ
 * 
 */
public abstract class ElementModel extends AbstractModel {

	// ģ�ͱ��
	private final int id;
	// λ�úʹ�С��Ϣ
	private Rectangle constraint;

	public static final String CONSTRAINT = "constraint";
	public static final String NAME = "name";

	/*
	 * ���󷽷����������̳У� ���м̳е�ǰģ�͵���ģ��Ҫʵ�ִ˷����� ��Ϊ��ģ�͵ķ���������Ϣ�Ľӿ�
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
	 * ����Զ����ɺ���
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

	// ��������
	// �������
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
	 * ģ�ͽڵ����ӱߣ��˱���ģ��ΪԴ��
	 * 
	 * @param connx
	 */
	public void addSourceConnection(AbstractConnectionModel connx) {

		sourceConnection.add(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, null);
	}

	/**
	 * ģ�ͽڵ����ӱߣ��˱���ģ��Ϊ�յ�
	 * 
	 * @param connx
	 */
	public void addTargetConnection(AbstractConnectionModel connx) {
		targetConnection.add(connx);
		firePropertyChange(P_TARGET_CONNECTION, null, null);
	}

	/**
	 * ģ�ͽڵ�ɾ���ߣ��˱���ģ��Ϊ���
	 * 
	 * @param connx
	 */
	public void removeSourceConnection(AbstractConnectionModel connx) {

		sourceConnection.remove(connx);
		firePropertyChange(P_SOURCE_CONNECTION, null, null);
	}

	/**
	 * ģ�ͽڵ�ɾ���ߣ��˱���ģ��Ϊ�յ�
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
