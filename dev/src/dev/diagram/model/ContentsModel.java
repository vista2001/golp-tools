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
 * 内容模型，存有图的全部信息，代表一张完整的图
 * 
 * @author 木木
 * 
 */
public class ContentsModel extends AbstractModel
{
	// 流程图的javabean
	private TfmBean tfmBean = new TfmBean();
	// 流程图子孩子节点，这里用于在画布中显示子模型
	private List<Object> children = new ArrayList<Object>();
	public final String projectId;
	// 流程图的Id
	public final int  diagramId;
	// 流程图xml文件名称
	public final String fileName;

	/**
	 * 初始化，并添加异常和补偿节点
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
	 * 初始化流程图信息，当打开编辑器时，做初始化调用
	 * 
	 * @param name
	 * @param desc
	 * @param type
	 * @param tradeid
	 */

	/**
	 * 流程图检验，流程图中是否有孤立节点，或除了开始和结束节点，有节点的出边和入边是1，具体请查看
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
	 * 自动布局
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
	 * 流程图加入子孩子节点，同时模型中加入子模型节点，并通知触发器
	 * 
	 * @param child
	 */
	public void addChild(Object child)
	{
		// 流程图中加入子孩子
		if (child instanceof CommonModel)
		{
			getTfmBean().getTfmBlockList().add(
					((CommonModel) child).getTfmBlock());
			((CommonModel) child).setEdgeList(tfmBean.getTfmEdgesList());
		}
		// 内容模型中加入子模型
		children.add(child);

		// 通知触发器
		firePropertyChange(ContentsModel.CHILDREN, null, null);
	}

	/**
	 * 移除模型中的子模型，同时流程图中移除子孩子节点；通知触发器
	 * 
	 * @param child
	 */
	public void removeChild(Object child)
	{
		// 模型中异常子模型
		children.remove(child);
		// 流程图中移除子孩子节点
		if (child instanceof CommonModel)
		{
			getTfmBean().getTfmBlockList().remove(
					((CommonModel) child).getTfmBlock());
		}
		if (children.size() == 1)
			ElementModel.setNumber(0);
		// 通知触发器
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
				new TextPropertyDescriptor(ContentsModel.TFM_DESC, "描述"),
				new TextPropertyDescriptor(ContentsModel.TFM_ID, "ID"),
				new TextPropertyDescriptor(ContentsModel.TFM_NAME, "名称"),
				new ComboBoxPropertyDescriptor(ContentsModel.TFM_TYPE, "事务类型",
						new String[] { "非事务", "单事务", "多事物" }),
				new TextPropertyDescriptor(ContentsModel.TFM_TRADEID, "交易码") };

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
