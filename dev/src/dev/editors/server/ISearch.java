package dev.editors.server;

import java.util.Map;

import org.eclipse.jface.preference.PreferenceStore;

import dev.model.base.ResourceLeafNode;
/**
 * 2013.11.7
 * ȥ��setSaveButtonEnable����������setDirty����������mySave������
 * 2013.11.1
 * �ýӿڶ�����һ��༭��Ӧ��ʵ�ֵķ������༭��ʵ���˸ýӿں󣬼�������ʹ��Search���͵Ķ���
 */
public interface ISearch
{
	/**
	 * �õ���ǰ�༭���У�����ʾ�Ľ�����Դ��Id
	 */
	public String getTargetId();
	/**
	 * �õ���ǰ�༭���У�����ʾ�Ľ�����Դ��Name
	 */
	public String getTargetName();
	/**
	 * ���õ�ǰ�༭����map
	 * @param map Ҫ���õ�map
	 */
	public void setTargetMap(Map<String, String> map);
	/**
	 * �õ������˹��̶�Ӧ��properties�ļ���PreferenceStore����
	 */
	public PreferenceStore getPs();
//	/**
//	 * ���ñ��水ť��״̬
//	 * @param b true����ã�false�򲻿���
//	 */
//	public void setSaveButtonEnable(boolean b);
	/**
	 * ���ý�����ť�͵��ı�
	 * @param text Ҫ���õ��ı�
	 */
	public void setUnLockButtonText(String text);
	/**
	 * ���ñ༭����dirty
	 * @param b
	 */
	public void setDirty(boolean b);
	/**
	 * �༭���ڴ˷�����ʵ�ֱ������
	 */
	public void mySave();
	/**
	 * ���õ�ǰ�༭����thisNode
	 * @param node Ҫ���õ�node
	 */
	public void setThisNode(ResourceLeafNode node);
	/**
	 * �õ���ǰ�༭����thisNode
	 * @return ��ǰ�༭����thisNode
	 */
	public ResourceLeafNode getThisNode();
	/**
	 * �ı�Input�е�node
	 * @param node Ҫ���õ�node
	 */
	public void setInputNode(ResourceLeafNode node);
	/**
	 * �ڴ˽ӿ���ʵ�ֶ�MessageBox��ķ�װ
	 * @param style MessageBox��style
	 * @param title MessageBox��title
	 * @param message MessageBox��message
	 * @return MessageBox��return
	 */
	public int showMessage(int style,String title,String message);
	/**
	 * �õ���ǰ�༭����Search����
	 * @return ��ǰ�༭����Search����
	 */
	public Search getSearch();
	/**
	 * �õ�ǰ�༭����map�����ñ༭���е����пؼ��������
	 */
	public void setControlsText();
	/**
	 * ��setPartName�ķ�װ
	 * @param name Ҫ��ʾ�ı���
	 */
	public void setMyPartName(String name);
	/**
	 * �ڴ˽ӿ���ʵ�ֵ��������/��������ťʱ�������Ĳ���
	 * @param b true��������false�����
	 */
	public void setEnable(boolean b);
	/**
	 * �ڴ˽ӿ���ʵ���������Ľ���
	 * @param node Ҫ���趨Ϊ�����ResourceLeafNode����
	 */
	public void setSelectNode(ResourceLeafNode node);
}
