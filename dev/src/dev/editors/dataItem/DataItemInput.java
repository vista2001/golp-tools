package dev.editors.dataItem;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;
/**
 * DataItem表编辑器的Input类
 * <p>这个类实现了IEditorInput接口，与DataItemEditor类一起完成DataItem编辑器的功能<br>
 * 在编辑器的初始化的时候，将DataItemEditor类完成编辑器的初始化所需要的数据传入，并在编辑器
 * 存在的时候保存编辑器的配置信息。
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class DataItemInput implements IEditorInput {

	private String input;			//存放编辑器所显示的节点的标识
	private ResourceLeafNode node;	//存储编辑器所显示的节点的对象
	/**用节点标识对类进行初始化*/
	public DataItemInput(String input){
		this.input=input;
	}
	/**用节点对象对类进行初始化*/
	public DataItemInput(ResourceLeafNode node){
		this.node=node;
		this.input=node.getId();
	}
	/**获得编辑器所对应的的节点**/
	public ResourceLeafNode getSource() {
		return node;
	}
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	/**设置编辑器所对应的节点**/
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
	/**设置编辑器的标识，使编辑器不指向原来节点当使用编辑器查询按钮成功时*/
	public void setName(String input){
		this.input=input;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return "数据项"+input;
	}
	/**根据编辑器的标识判断一个节点的编辑器是不是打开了了*/
	public boolean equals(Object obj) {
		  if(null == obj) return false;
		   
		  if(!(obj instanceof DataItemInput)) return false;
		   
		  if(!getName().equals(((DataItemInput)obj).getName())) return false;
		   
		  if(!node.getRootProject().equals(((DataItemInput)obj).getSource().getRootProject())) return false;
		  return true;
		 }
}
