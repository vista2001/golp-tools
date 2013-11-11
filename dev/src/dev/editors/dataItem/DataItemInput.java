package dev.editors.dataItem;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;
/**
 * DataItem��༭����Input��
 * <p>�����ʵ����IEditorInput�ӿڣ���DataItemEditor��һ�����DataItem�༭���Ĺ���<br>
 * �ڱ༭���ĳ�ʼ����ʱ�򣬽�DataItemEditor����ɱ༭���ĳ�ʼ������Ҫ�����ݴ��룬���ڱ༭��
 * ���ڵ�ʱ�򱣴�༭����������Ϣ��
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class DataItemInput implements IEditorInput {

	private String input;			//��ű༭������ʾ�Ľڵ�ı�ʶ
	private ResourceLeafNode node;	//�洢�༭������ʾ�Ľڵ�Ķ���
	/**�ýڵ��ʶ������г�ʼ��*/
	public DataItemInput(String input){
		this.input=input;
	}
	/**�ýڵ���������г�ʼ��*/
	public DataItemInput(ResourceLeafNode node){
		this.node=node;
		this.input=node.getId();
	}
	/**��ñ༭������Ӧ�ĵĽڵ�**/
	public ResourceLeafNode getSource() {
		return node;
	}
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	/**���ñ༭������Ӧ�Ľڵ�**/
	public void setSource(ResourceLeafNode node){
		this.node=node;
	}
	
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}
	/**���ñ༭���ı�ʶ��ʹ�༭����ָ��ԭ���ڵ㵱ʹ�ñ༭����ѯ��ť�ɹ�ʱ*/
	public void setName(String input){
		this.input=input;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "������"+input;
	}
	/**���ݱ༭���ı�ʶ�ж�һ���ڵ�ı༭���ǲ��Ǵ�����*/
	public boolean equals(Object obj) {
		  if(null == obj) return false;
		   
		  if(!(obj instanceof DataItemInput)) return false;
		   
		  if(!getName().equals(((DataItemInput)obj).getName())) return false;
		   
		  if(!node.getRootProject().equals(((DataItemInput)obj).getSource().getRootProject())) return false;
		  return true;
		 }
}
