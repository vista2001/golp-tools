/* �ļ�����       DataItemEditorInput.java
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�   ���������ļ�����DataItemInput��ΪDataItemEditorInput��     
 */

package dev.editors.dataItem;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;

/**
 * DataItem��༭����Input��
 * <p>
 * �����ʵ����IEditorInput�ӿڣ���DataItemEditor��һ�����DataItem�༭���Ĺ���<br>
 * �ڱ༭���ĳ�ʼ����ʱ�򣬽�DataItemEditor����ɱ༭���ĳ�ʼ������Ҫ�����ݴ��룬���ڱ༭�� ���ڵ�ʱ�򱣴�༭����������Ϣ��
 * 
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class DataItemEditorInput implements IEditorInput {

	private String input; // ��ű༭������ʾ�Ľڵ�ı�ʶ
	private ResourceLeafNode node; // �洢�༭������ʾ�Ľڵ�Ķ���

	/** �ýڵ��ʶ������г�ʼ�� */
	public DataItemEditorInput(String input) {
		this.input = input;
	}

	/** �ýڵ���������г�ʼ�� */
	public DataItemEditorInput(ResourceLeafNode node) {
		this.node = node;
		this.input = node.getId();
	}

	/** ��ñ༭������Ӧ�ĵĽڵ� **/
	public ResourceLeafNode getSource() {
		return node;
	}

	@Override
	public Object getAdapter(Class adapter) {

		return null;
	}

	/** ���ñ༭������Ӧ�Ľڵ� **/
	public void setSource(ResourceLeafNode node) {
		this.node = node;
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

	/** ���ñ༭���ı�ʶ��ʹ�༭����ָ��ԭ���ڵ㵱ʹ�ñ༭����ѯ��ť�ɹ�ʱ */
	public void setName(String input) {
		this.input = input;
	}

	@Override
	public String getToolTipText() {

		return "������" + input;
	}

	/** ���ݱ༭���ı�ʶ�ж�һ���ڵ�ı༭���ǲ��Ǵ����� */
	public boolean equals(Object obj) {
		if (null == obj)
			return false;

		if (!(obj instanceof DataItemEditorInput))
			return false;

		if (!getName().equals(((DataItemEditorInput) obj).getName()))
			return false;

		if (!node.getRootProject().equals(
				((DataItemEditorInput) obj).getSource().getRootProject()))
			return false;
		return true;
	}
}
