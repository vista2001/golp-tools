package dev.editors.server;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;
import dev.model.base.ResourceLeafNode;
/**
 * ���ඨ���˷����������Ӧ��Input 
 */
public class ServerEditorInput implements IEditorInput{
	/**
	 * ��node�����������ó�ʼ��ServerEditorInput����ʱ�������ResourceLeafNode����
	 */
	private ResourceLeafNode node;
	
	public ResourceLeafNode getNode()
	{
		return node;
	}

	public void setNode(ResourceLeafNode node)
	{
		this.node = node;
	}
	/**
	 * �ú���ΪServerEditorInput�Ĺ��캯��������һ�������͵Ķ���ʱ��Ӧ����һ��ResourceLeafNode���͵Ķ���
	 * @param node
	 */
	public ServerEditorInput(ResourceLeafNode node){
		this.node=node;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/sample.gif");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return node.getId();
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return node.getId();
	}
	 @Override
	 public boolean equals(Object obj) {
	  if(null == obj) return false;
	   
	  if(!(obj instanceof ServerEditorInput)) return false;
	   
	  if(!getName().equals(((ServerEditorInput)obj).getName())) return false;
	  //��node�����Ĺ��̵�id��ͬ���򷵻�false
	  if(!node.getParent().getParent().getId().equals(((ServerEditorInput)obj).getNode().getParent().getParent().getId()))
	  {
		  return false;
	  }
	   
	  return true;
		// return false;
	 }
}
