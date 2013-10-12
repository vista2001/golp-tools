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

public class NewDataItemWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;

	private NewDataItemWizardPage0 page0;
	private NewDataItemWizardPage1 page1;

	private String dataItemId = "";
	private String dataItemName = "";
	private String dataItemDesc = "";
	private String dataItemLvL = "";
	private String dataItemType = "";
	private int dataItemlen = 0;
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
		doFinish(dataItemId, dataItemName, dataItemDesc, dataItemLvL, dataItemType, dataItemlen, dataItemAOP,dataItemUpProject);
		updateNavView();
		return true;
	}

	// //获取该新建向导中所设置的数据
	private void getData()
	{
		dataItemId = page0.getDataItemIdText().getText();
		dataItemName = page0.getDataItemNameText().getText();
		dataItemDesc = page0.getDataItemDescText().getText();
		dataItemLvL = page1.getDataItemLvLCombo().getText();
		dataItemType = page1.getDataItemTypeCombo().getText().substring(0, 1);
		dataItemlen = Integer.parseInt(page1.getDataItemlenText().getText());
		dataItemAOP = page1.getDataItemAOPText().getText();
		dataItemUpProject = page1.getDataItemUpProjectCombo().getText();
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(String dataItemId, String dataItemName,
			String dataItemDesc, String dataItemLvL, String dataItemType,
			int dataItemlen, String dataItemAOP, String dataItemUpProject)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into dataitem values(?,?,?,?,?,?,?,?)";
		try
		{
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, dataItemId);
			dbConnImpl.setPreparedString(2, dataItemName);
			dbConnImpl.setPreparedString(3, dataItemDesc);
			dbConnImpl.setPreparedString(4, dataItemLvL);
			dbConnImpl.setPreparedString(5, dataItemType);
			dbConnImpl.setPreparedInt(6, dataItemlen);
			dbConnImpl.setPreparedString(7, dataItemAOP);
			dbConnImpl.setPreparedString(8, dataItemUpProject);
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
					dataItemId, list.get(i));
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
