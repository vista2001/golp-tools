package dev.editors.aopDll;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.model.base.ResourceLeafNode;

/**
 * AopDll表编辑器的Input类
 * <p>
 * 这个类实现了IEditorInput接口，与AopDllEditor类一起完成AopDll编辑器的功能<br>
 * 在编辑器的初始化的时候，将AopDllEditor类完成编辑器的初始化所需要的数据传入，并在编辑器 存在的时候保存编辑器的配置信息。
 * 
 * @see#getName()
 * @see#getToolTipText()
 * @see#setName()
 * @see#equals()
 * */
public class AopDllEditorInput implements IEditorInput {

	private String input; // 存放编辑器所显示的节点的标识
	private ResourceLeafNode node; // 存储编辑器所显示的节点的对象

	/** 用节点标识对类进行初始化 */
	public AopDllEditorInput(String input) {
		this.input = input;
	}

	/** 用节点对象对类进行初始化 */
	public AopDllEditorInput(ResourceLeafNode node) {
		this.node = node;
		this.input = node.getId();
	}

	/** 获得编辑器所对应的节点 **/
	public ResourceLeafNode getSource() {
		return node;
	}

	/** 设置编辑器所对应的节点 **/
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

	/** 设置编辑器的标识，使编辑器不指向原来节点当使用编辑器查询按钮成功时 */
	public void setName(String input) {
		this.input = input;
	}

	@Override
	public IPersistableElement getPersistable() {

		return null;
	}

	@Override
	public String getToolTipText() {

		return "原子交易库" + input;
	}

	/** 根据编辑器的标识判断一个节点的编辑器是不是打开了了 */
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
