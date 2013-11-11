package dev.wizards.newDataItem;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.DataItemNodes;
import dev.model.resource.ProjectNode;
import dev.model.resource.ServerNodes;
import dev.views.NavView;
/**
 * 该类定义了新建数据项向导所需要的 Wizard类
 */
public class NewDataItemWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;

	private NewDataItemWizardPage0 page0;
	private NewDataItemWizardPage1 page1;

	private int dataItemId;
	private String dataItemName = "";
	private String dataItemDesc = "";
	private String dataItemLvL = "";
	private String dataItemType = "";
	private String dataItemLenFixed = "";
	//dataItemlen初始化为-1，若新建向导中有输入该值，则更新为输入的值
	private int dataItemlen = -1;
	private String dataItemAOP = "";
	private String dataItemUpProject;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// TODO 自动生成的方法存根
		this.selection = selection;
		this.workbench = workbench;
	}

	public void addPages()
	{
		page0 = new NewDataItemWizardPage0(selection);
		page1 = new NewDataItemWizardPage1(selection);

		addPage(page0);
		addPage(page1);
	}

	@Override
	public boolean performFinish()
	{
		// TODO 自动生成的方法存根
		getData();
		doFinish(dataItemId, dataItemName, dataItemDesc, dataItemLvL, dataItemType, dataItemLenFixed, dataItemlen, dataItemAOP,dataItemUpProject);
		updateNavView();
		return true;
	}

	//获取该新建向导中所设置的数据
	private void getData()
	{
		dataItemUpProject = page0.getDataItemUpProjectCombo().getText();
		dataItemLvL = page0.getDataItemLvLCombo().getText().substring(0, 1);
		dataItemId = Integer.parseInt(page0.getDataItemIdText().getText());
		dataItemName = page0.getDataItemNameText().getText();
		dataItemDesc = page0.getDataItemDescText().getText();
		dataItemType = page1.getDataItemTypeCombo().getText().substring(0,1);
		if(page1.getDataItemlenText().isEnabled())
		{
			dataItemlen = Integer.parseInt(page1.getDataItemlenText().getText());
		}
		dataItemAOP = page1.getDataItemAOPText().getText();
		
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(int dataItemId, String dataItemName,
			String dataItemDesc, String dataItemLvL, String dataItemType,
			String dataItemLenFixed, int dataItemlen, String dataItemAOP,
			String dataItemUpProject)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into dataitem values(?,?,?,?,?,?,?,?,?,?)";
		try
		{
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedInt(1, dataItemId);
			dbConnImpl.setPreparedString(2, dataItemName);
			dbConnImpl.setPreparedString(3, dataItemDesc);
			dbConnImpl.setPreparedString(4, dataItemLvL);
			dbConnImpl.setPreparedString(5, dataItemType);
			//数据库表中DATALEN字段，当前插入null
			dbConnImpl.setPreparedString(6, null);
			dbConnImpl.setPreparedInt(7, dataItemlen);
			dbConnImpl.setPreparedString(8, dataItemAOP);
			//数据库表中FMLID字段，当前插入-1
			dbConnImpl.setPreparedInt(9, -1);
			dbConnImpl.setPreparedString(10, dataItemUpProject);
			dbConnImpl.executeExceptPreparedQuery();

		} 
		catch (SQLException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				dbConnImpl.closeConn();
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//更新左侧导航视图
	private void updateNavView()
	{
		IViewPart view = this.workbench.getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		if (view != null)
		{
			NavView v = (NavView) view;
			TreeViewer tv = v.getTreeViewer();
			RootNode root = (RootNode) tv.getInput();

			int index;
			for (index = 0; index < root.getChildren().size(); index++)
			{
				if (root.getChildren().get(index).getName().equals(dataItemUpProject))
					break;
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);
			//System.out.println(projectNode.getName());

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++)
			{
				if (list.get(i).getName().equals("数据项"))
					break;
			}
			//System.out.println(list.get(i).getName());
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(dataItemName,
					dataItemId+"", list.get(i));
			((DataItemNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	@Override
	public boolean canFinish()
	{
		// TODO 自动生成的方法存根
		return page0.canFlipToNextPage() && page1.validInput();

	}
}
