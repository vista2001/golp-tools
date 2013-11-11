package dev.editors.server;

import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.model.base.ResourceLeafNode;
/**
 * 2013.11.7
 * 去除setSaveButtonEnable方法，增加setDirty方法，增加mySave方法。
 * 2013.11.1
 * 该接口定义了一组编辑器应当实现的方法。编辑器实现了该接口后，即可正常使用Search类型的对象。
 */
public interface ISearch
{
	/**
	 * 得到当前编辑器中，所显示的交易资源的Id
	 */
	public String getTargetId();
	/**
	 * 得到当前编辑器中，所显示的交易资源的Name
	 */
	public String getTargetName();
	/**
	 * 设置当前编辑器的map
	 * @param map 要设置的map
	 */
	public void setTargetMap(Map<String, String> map);
	/**
	 * 得到加载了工程对应的properties文件的PreferenceStore对象
	 */
	public PreferenceStore getPs();
//	/**
//	 * 设置保存按钮的状态
//	 * @param b true则可用，false则不可用
//	 */
//	public void setSaveButtonEnable(boolean b);
	/**
	 * 设置解锁按钮就的文本
	 * @param text 要设置的文本
	 */
	public void setUnLockButtonText(String text);
	/**
	 * 设置编辑器的dirty
	 * @param b
	 */
	public void setDirty(boolean b);
	/**
	 * 编辑器在此方法中实现保存操作
	 */
	public void mySave();
	/**
	 * 设置当前编辑器的thisNode
	 * @param node 要设置的node
	 */
	public void setThisNode(ResourceLeafNode node);
	/**
	 * 得到当前编辑器的thisNode
	 * @return 当前编辑器的thisNode
	 */
	public ResourceLeafNode getThisNode();
	/**
	 * 改变Input中的node
	 * @param node 要设置的node
	 */
	public void setInputNode(ResourceLeafNode node);
	/**
	 * 在此接口中实现对MessageBox类的封装
	 * @param style MessageBox的style
	 * @param title MessageBox的title
	 * @param message MessageBox的message
	 * @return MessageBox的return
	 */
	public int showMessage(int style,String title,String message);
	/**
	 * 得到当前编辑器的Search对象
	 * @return 当前编辑器的Search对象
	 */
	public Search getSearch();
	/**
	 * 用当前编辑器的map，给该编辑器中的所有控件填充内容
	 */
	public void setControlsText();
	/**
	 * 对setPartName的封装
	 * @param name 要显示的标题
	 */
	public void setMyPartName(String name);
	/**
	 * 在此接口中实现点击“锁定/解锁”按钮时，所做的操作
	 * @param b true则锁定，false则解锁
	 */
	public void setEnable(boolean b);
	/**
	 * 在此接口中实现设置树的焦点
	 * @param node 要被设定为焦点的ResourceLeafNode对象
	 */
	public void setSelectNode(ResourceLeafNode node);
}
