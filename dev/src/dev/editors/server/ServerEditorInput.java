package dev.editors.server;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;
import dev.model.base.ResourceLeafNode;
/**
 * 该类定义了服务程序所对应的Input 
 */
public class ServerEditorInput implements IEditorInput{
	/**
	 * 该node变量用于引用初始化ServerEditorInput对象时所传入的ResourceLeafNode对象
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
	 * 该函数为ServerEditorInput的构造函数，构造一个该类型的对象时，应传入一个ResourceLeafNode类型的对象
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
	  //若node所属的工程的id不同，则返回false
	  if(!node.getParent().getParent().getId().equals(((ServerEditorInput)obj).getNode().getParent().getParent().getId()))
	  {
		  return false;
	  }
	   
	  return true;
		// return false;
	 }
}
