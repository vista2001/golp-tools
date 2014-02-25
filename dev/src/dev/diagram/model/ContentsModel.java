package dev.diagram.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import dev.diagram.beans.TfmBean;
import dev.views.NavView;

/**
 * ����ģ�ͣ�����ͼ��ȫ����Ϣ������һ��������ͼ
 * 
 * @author ľľ
 * 
 */
public class ContentsModel extends AbstractModel
{
	// ����ͼ��javabean
	private TfmBean tfmBean = new TfmBean();
	// ����ͼ�Ӻ��ӽڵ㣬���������ڻ�������ʾ��ģ��
	private List<Object> children = new ArrayList<Object>();
	public final String projectId;
	// ����ͼ��Id
	public final int  diagramId;
	// ����ͼxml�ļ�����
	public final String fileName;

	/**
	 * ��ʼ����������쳣�Ͳ����ڵ�
	 */
	public ContentsModel(String projectId, String diagramId, String fileName)
	{
		this.projectId = projectId;
		this.diagramId = Integer.parseInt(diagramId);
		this.fileName = fileName;
		tfmBean.setTfmId(diagramId);
		ExAndComModel exAndComModel = new ExAndComModel();
		exAndComModel.setConstraint(new org.eclipse.draw2d.geometry.Rectangle(
				300, 5, 150, 80));
		children.add(exAndComModel);
	}

	public void init(String name, String desc, String type,
			String tradeid)
	{
		tfmBean.setTfmName(name);
		tfmBean.setTfmdesc(desc);
		tfmBean.setTfmType(type);
		tfmBean.setTradeId(tradeid);
	}

	/**
	 * ��ʼ������ͼ��Ϣ�����򿪱༭��ʱ������ʼ������
	 * 
	 * @param name
	 * @param desc
	 * @param type
	 * @param tradeid
	 */

	/**
	 * ����ͼ���飬����ͼ���Ƿ��й����ڵ㣬����˿�ʼ�ͽ����ڵ㣬�нڵ�ĳ��ߺ������1��������鿴
	 * <p>
	 * isConnected
	 * </p>
	 * 
	 * @return
	 */
	public boolean isComplete()
	{
		for (int i = 1; i < children.size(); i++)
		{
			if (!((CommonModel) children.get(i)).isConnected())
				return false;
		}
		return true;
	}

	public TfmBean getTfmBean()
	{
		return tfmBean;
	}

	public void setTfmBean(TfmBean tfmBean)
	{
		this.tfmBean = tfmBean;
	}

	// ////////////////////////////////////////////////////////////////////
	public static final String CHILDREN = "CHILDREN";
	public static final String LAYOUT = "AUTOLAYOUT";
	protected boolean autoLayout = false;
	public static final String TFM_ID = "id";
	public static final String TFM_TYPE = "type";
	public static final String TFM_DESC = "desc";
	public static final String TFM_NAME = "name";
	public static final String TFM_TRADEID = "tradeid";

	// /////////////////////////////////////////////////////////////////////////
	/**
	 * �Զ�����
	 */
	public boolean isAutoLayout()
	{
		return autoLayout;
	}

	public void setAutoLayout(boolean b)
	{
		autoLayout = b;
		firePropertyChange(LAYOUT, null, null);
	}

	/**
	 * ����ͼ�����Ӻ��ӽڵ㣬ͬʱģ���м�����ģ�ͽڵ㣬��֪ͨ������
	 * 
	 * @param child
	 */
	public void addChild(Object child)
	{
		// ����ͼ�м����Ӻ���
		if (child instanceof CommonModel)
		{
			getTfmBean().getTfmBlockList().add(
					((CommonModel) child).getTfmBlock());
			((CommonModel) child).setEdgeList(tfmBean.getTfmEdgesList());
		}
		// ����ģ���м�����ģ��
		children.add(child);

		// ֪ͨ������
		firePropertyChange(ContentsModel.CHILDREN, null, null);
	}

	/**
	 * �Ƴ�ģ���е���ģ�ͣ�ͬʱ����ͼ���Ƴ��Ӻ��ӽڵ㣻֪ͨ������
	 * 
	 * @param child
	 */
	public void removeChild(Object child)
	{
		// ģ�����쳣��ģ��
		children.remove(child);
		// ����ͼ���Ƴ��Ӻ��ӽڵ�
		if (child instanceof CommonModel)
		{
			getTfmBean().getTfmBlockList().remove(
					((CommonModel) child).getTfmBlock());
		}
		if (children.size() == 1)
			ElementModel.setNumber(0);
		// ֪ͨ������
		firePropertyChange(ContentsModel.CHILDREN, null, null);

	}

	public List<Object> getChildren()
	{
		return children;
	}

	// ///////////////////////////////////////////////////////////

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors()
	{
		IPropertyDescriptor[] decriptors = {
				new TextPropertyDescriptor(ContentsModel.TFM_DESC, "����"),
				new TextPropertyDescriptor(ContentsModel.TFM_ID, "ID"),
				new TextPropertyDescriptor(ContentsModel.TFM_NAME, "����"),
				new ComboBoxPropertyDescriptor(ContentsModel.TFM_TYPE, "��������",
						new String[] { "������", "������", "������" }),
				new TextPropertyDescriptor(ContentsModel.TFM_TRADEID, "������") };

		return decriptors;
	}

	@Override
	public Object getPropertyValue(Object id)
	{

		if (TFM_DESC.equals(id))
			return tfmBean.getTfmdesc();
		if (TFM_TYPE.equals(id))
			return Integer.parseInt(tfmBean.getTfmType());
		if (TFM_ID.equals(id))
			return tfmBean.getTfmId();
		if (TFM_NAME.equals(id))
			return tfmBean.getTfmName();
		if (TFM_TRADEID.equals(id))
			return tfmBean.getTradeId();

		return new String("");
	}

	@Override
	public boolean isPropertySet(Object id)
	{
		return false;

	}

	@Override
	public void setPropertyValue(Object id, Object value)
	{
		if (TFM_NAME.equals(id))
		{
			tfmBean.setTfmName((String) value);
			NavView view = (NavView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.findView(NavView.ID);
			view.getTreeViewer().refresh();
		}
		if (TFM_DESC.equals(id))
			tfmBean.setTfmdesc((String) value);
		if (TFM_TYPE.equals(id))
			tfmBean.setTfmType((int) value + "");

	}

	@Override
	public Object getEditableValue()
	{

		return null;
	}

	@Override
	public void resetPropertyValue(Object id)
	{

	}

}
