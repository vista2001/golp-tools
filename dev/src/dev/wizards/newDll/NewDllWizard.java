/* 文件名：       NewDllWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.12.13
 * 修改内容：   1.在写数据库的方法中，除去UPPROJECT这个字段；
 *         2.增加对动态库路径的处理。
 */

package dev.wizards.newDll;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.pojo.TAopDll;
import dev.db.service.EditorAopDllServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.DllNodes;
import dev.model.resource.ProjectNode;
import dev.views.NavView;
/**
 * 该类定义了新建动态库向导所需要的 Wizard类
 */
public class NewDllWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;
	private NewDllWizardPage0 page0;
	private String dllUpProject = "";
	private TAopDll aopDll;
//	private String dllLevel = "";
//	private String dllId = "";
//	private String dllName = "";
//	private String dllDesc = "";
//	private String dllType = "";
//	private String dllPath = "";
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		this.selection = selection;
		this.workbench = workbench;
		aopDll = new TAopDll();
	}
	
	public void addPages()
	{
		page0 = new NewDllWizardPage0(selection);
		addPage(page0);
	}

	@Override
	public boolean performFinish()
	{
		getData();
		try
        {
            doFinish(aopDll, dllUpProject);
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
		dllUpProject = page0.getAopDllUpProjectCombo().getText();
		String dllLevel = page0.getAopDllLevelCombo().getText();
		aopDll.setAopDllLevel2(dllLevel);
		int dllId = Integer.parseInt(page0.getAopDllIdText().getText());
		aopDll.setAopDllId(dllId);
		String dllName = page0.getAopDllNameText().getText();
		aopDll.setAopDllName(dllName);
		String dllPath = page0.getAopDllPathText().getText();
		aopDll.setAopDllPath(dllPath);
		String dllDesc = page0.getAopDllDescText().getText();
		aopDll.setAopDllDesc(dllDesc);
		String dllType = page0.getAopDllTypeCombo().getText();
		aopDll.setAopDllType(dllType);
	}
	
	// 将从新建向导中得到的数据写入数据库
	private void doFinish(TAopDll aopDll, String prjId) throws SQLException
	{
	    EditorAopDllServiceImpl editorAopDllServiceImpl = new EditorAopDllServiceImpl();
	    editorAopDllServiceImpl.insertAopDll(aopDll, prjId);
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
				if (root.getChildren().get(index).getName().equals(dllUpProject)){
					break;
				}
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++)
			{
				if (list.get(i).getName().equals("动态库"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopDll.getAopDllName(),
			        aopDll.getAopDllId() + "", list.get(i));
			((DllNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}
	
	@Override
	public boolean canFinish()
	{
		return page0.validInput();

	}
}
