package dev.editors.aop;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;

/**
 * Aop��༭����Input��
 * <p>
 * �����ʵ����IEditorInput�ӿڣ���AopEditor��һ�����Aop�༭���Ĺ���<br>
 * �ڱ༭���ĳ�ʼ����ʱ�򣬽�AopEditor����ɱ༭���ĳ�ʼ������Ҫ�����ݴ��룬���ڱ༭�� ���ڵ�ʱ�򱣴�༭����������Ϣ��
 * 
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class AopEditorInput implements IEditorInput {
	private String input; // ��ű༭������ʾ�Ľڵ�ı�ʶ
	private ResourceLeafNode node; // �洢�༭������ʾ�Ľڵ�Ķ���

	public void setNode(ResourceLeafNode node) {
		this.node = node;
	}

	/** �ýڵ��ʶ������г�ʼ�� */
	public AopEditorInput(String input) {
		this.input = input;
	}

	/** �ýڵ���������г�ʼ�� */
	public AopEditorInput(ResourceLeafNode node) {
		this.node = node;
		this.input = node.getId();
	}

	/** ��ñ༭������Ӧ�ĵĽڵ� **/
	public ResourceLeafNode getSource() {
		return node;
	}

	/** ���ñ༭������Ӧ�Ľڵ� **/
	public void setSource(ResourceLeafNode node) {
		this.node = node;
	}

	public Object getAdapter(Class adapter) {

		return null;
	}

	@Override
	public boolean exists() {

		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {

		return null;
	}

	@Override
	public String getName() {

		return input;
	}

	@Override
	public IPersistableElement getPersistable() {

		return null;
	}

	@Override
	public String getToolTipText() {

		return input;
	}

	/** ���ñ༭���ı�ʶ��ʹ�༭����ָ��ԭ���ڵ㵱ʹ�ñ༭����ѯ��ť�ɹ�ʱ */
	public void setName(String input) {
		this.input = input;
	}

	/** ���ݱ༭���ı�ʶ�ж�һ���ڵ�ı༭���ǲ��Ǵ��� */
	public boolean equals(Object obj) {
		if (null == obj)
			return false;

		if (!(obj instanceof AopEditorInput))
			return false;

		if (!getName().equals(((AopEditorInput) obj).getName()))
			return false;
		if (!node.getRootProject().equals(
				((AopEditorInput) obj).getSource().getRootProject()))
			return false;

		return true;
	}
}
