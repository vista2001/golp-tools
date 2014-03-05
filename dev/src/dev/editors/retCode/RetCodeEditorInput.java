/* �ļ�����       RetCodeEditorInput.java
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�   ���������ļ�����RetCodeInput��ΪRetCodeEditorInput��     
 */

package dev.editors.retCode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;
import dev.model.base.ResourceLeafNode;

/**
 * RetCode��༭����Input��
 * <p>
 * �����ʵ����IEditorInput�ӿڣ���RetCodeEditor��һ�����RetCode�༭���Ĺ���<br>
 * �ڱ༭���ĳ�ʼ����ʱ�򣬽�RetCodeEditor����ɱ༭���ĳ�ʼ������Ҫ�����ݴ��룬���ڱ༭�� ���ڵ�ʱ�򱣴�༭����������Ϣ��
 * 
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class RetCodeEditorInput implements IEditorInput {
	private String input; // ��ű༭������ʾ�Ľڵ�ı�ʶ
	private ResourceLeafNode node; // �洢�༭������ʾ�Ľڵ�Ķ���

	/** �ýڵ��ʶ������г�ʼ�� */
	public RetCodeEditorInput(String input) {
		this.input = input;
	}

	/** �ýڵ���������г�ʼ�� */
	public RetCodeEditorInput(ResourceLeafNode node) {
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

	/** ���ñ༭���ı�ʶ��ʹ�༭����ָ��ԭ���ڵ㵱ʹ�ñ༭����ѯ��ť�ɹ�ʱ */
	public void setName(String input) {
		this.input = input;
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

		return Activator.getImageDescriptor("icons/sample.gif");
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

		return this.getName();
	}

	/** ���ݱ༭���ı�ʶ�ж�һ���ڵ�ı༭���ǲ��Ǵ����� */
	public boolean equals(Object obj) {
		if (null == obj)
			return false;

		if (!(obj instanceof RetCodeEditorInput))
			return false;

		if (!getName().equals(((RetCodeEditorInput) obj).getName()))
			return false;

		if (!node.getRootProject().equals(
				((RetCodeEditorInput) obj).getSource().getRootProject()))
			return false;

		return true;
	}

}
