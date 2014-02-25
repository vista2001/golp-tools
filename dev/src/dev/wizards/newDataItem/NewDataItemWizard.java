/* 文件名：       NewDataItemWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.在写数据库的方法中，除去UPPROJECT这个字段。    
 */

package dev.wizards.newDataItem;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.pojo.TDataItem;
import dev.db.service.EditorDataitemServiceImpl;
import dev.generate.fml.FmlId;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.DataItemNodes;
import dev.model.resource.ProjectNode;
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

//	private int dataItemId;
//	private String dataItemName = "";
//	private String dataItemDesc = "";
//	private String dataItemLvL = "";
//	private String dataItemType = "";
//	private int dataItemlen;
//	private String dataItemAOP = "";
	private String dataItemUpProject;
	private TDataItem dataItem;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		this.selection = selection;
		this.workbench = workbench;
		this.dataItem = new TDataItem();
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
		getData();
		try
        {
            doFinish(dataItem, dataItemUpProject);
            updateNavView();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
		return true;
	}

	//获取该新建向导中所设置的数据
	private void getData()
	{
		dataItemUpProject = page0.getDataItemUpProjectCombo().getText();
		String dataItemLvL = page0.getDataItemLvLCombo().getText();
		dataItem.setDataLvL2(dataItemLvL);
		int dataItemId = Integer.parseInt(page0.getDataItemIdText().getText());
		dataItem.setDataItemId(dataItemId);
		String dataItemName = page0.getDataItemNameText().getText();
		dataItem.setDataName(dataItemName);
		String dataItemDesc = page0.getDataItemDescText().getText();
		dataItem.setDataDesc(dataItemDesc);
		String dataItemType = page1.getDataItemTypeCombo().getText();
		dataItem.setDataType2(dataItemType);
		int dataItemlen = Integer.parseInt(page1.getDataItemlenText().getText());
		dataItem.setDataLen(dataItemlen);
		String dataItemAOP = page1.getDataItemAOPText().getText();
		dataItem.setDataAop(dataItemAOP);
		dataItemType = dataItemType.substring(2);
		try
        {
            long fmlId = FmlId.getFmlId(dataItemName, dataItemId, dataItemType);
            dataItem.setFmlId(fmlId);
        }
        catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
            dataItem.setFmlId(-1);
        }
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(TDataItem dataItem, String prjId) throws SQLException
	{
	    EditorDataitemServiceImpl editorDataitemServiceImpl = new EditorDataitemServiceImpl();
	    editorDataitemServiceImpl.insertDataItem(dataItem, prjId);
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
			//DebugOut.println(projectNode.getName());

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++)
			{
				if (list.get(i).getName().equals("数据项"))
					break;
			}
			//DebugOut.println(list.get(i).getName());
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(dataItem.getDataName(),
					dataItem.getDataItemId()+"", list.get(i));
			((DataItemNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}
	
	@Override
	public boolean canFinish()
	{
		return page0.canFlipToNextPage() && page1.validInput();

	}
}
