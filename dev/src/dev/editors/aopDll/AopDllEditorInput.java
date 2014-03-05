package dev.editors.aopDll;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;

/**
 * AopDll��༭����Input��
 * <p>
 * �����ʵ����IEditorInput�ӿڣ���AopDllEditor��һ�����AopDll�༭���Ĺ���<br>
 * �ڱ༭���ĳ�ʼ����ʱ�򣬽�AopDllEditor����ɱ༭���ĳ�ʼ������Ҫ�����ݴ��룬���ڱ༭�� ���ڵ�ʱ�򱣴�༭����������Ϣ��
 * 
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class AopDllEditorInput implements IEditorInput {

	private String input; // ��ű༭������ʾ�Ľڵ�ı�ʶ
	private ResourceLeafNode node; // �洢�༭������ʾ�Ľڵ�Ķ���

	/** �ýڵ��ʶ������г�ʼ�� */
	public AopDllEditorInput(String input) {
		this.input = input;
	}

	/** �ýڵ���������г�ʼ�� */
	public AopDllEditorInput(ResourceLeafNode node) {
		this.node = node;
		this.input = node.getId();
	}

	/** ��ñ༭������Ӧ�Ľڵ� **/
	public ResourceLeafNode getSource() {
		return node;
	}

	/** ���ñ༭������Ӧ�Ľڵ� **/
	public void setSource(ResourceLeafNode node) {
		this.node = node;
	}

	@Override
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

	/** ���ñ༭���ı�ʶ��ʹ�༭����ָ��ԭ���ڵ㵱ʹ�ñ༭����ѯ��ť�ɹ�ʱ */
	public void setName(String input) {
		this.input = input;
	}

	@Override
	public IPersistableElement getPersistable() {

		return null;
	}

	@Override
	public String getToolTipText() {

		return "ԭ�ӽ��׿�" + input;
	}

	/** ���ݱ༭���ı�ʶ�ж�һ���ڵ�ı༭���ǲ��Ǵ����� */
	public boolean equals(Object obj) {
		if (null == obj)
			return false;

		if (!(obj instanceof AopDllEditorInput))
			return false;

		if (!getName().equals(((AopDllEditorInput) obj).getName()))
			return false;

		if (!node.getRootProject().equals(
				((AopDllEditorInput) obj).getSource().getRootProject()))
			return false;
		return true;
	}
}
