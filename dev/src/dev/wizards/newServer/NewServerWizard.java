package dev.wizards.newServer;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.model.resource.ServerNodes;
import dev.views.NavView;

public class NewServerWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;
	
	private NewServerWizardPage0 page0;
	private NewServerWizardPage1 page1;
	private NewServerWizardPage2 page2;
	private NewServerWizardPage3 page3;
	
	private String svrId = "";
	private String svrName = "";
	private String svrDesc = "";
	private String serverSpeclib = "";
	private String serverSpecAopDlls = "";
	private String upProject = "";
	private String callBackSource = "";
	private String othFunSrource = "";

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// TODO 自动生成的方法存根
		this.selection = selection;
		this.workbench = workbench;
	}
	
	@Override
	public void addPages()
	{
		page0 = new NewServerWizardPage0(selection);
		page1 = new NewServerWizardPage1(selection);
		page2 = new NewServerWizardPage2(selection);
		page3 = new NewServerWizardPage3(selection);

		addPage(page0);
		addPage(page1);
		addPage(page2);
		addPage(page3);

	}

	@Override
	public boolean performFinish()
	{
		// TODO 自动生成的方法存根	
		getData();
		doFinish(svrId, svrName, svrDesc, serverSpeclib, serverSpecAopDlls,
				upProject, callBackSource, othFunSrource);
		updateNavView();
		return true;
	}
	//获取该新建向导中所设置的数据
	private void getData()
	{
		svrId = page0.getSvrIdText().getText();
		svrName = page0.getSvrNameText().getText();
		svrDesc = page0.getSvrDescText().getText();
		serverSpeclib = page1.getServerSpeclibText().getText();
		serverSpecAopDlls = page1.getServerSpecAopDllsText().getText();
		upProject = page1.getUpProjectCombo().getText();
		if (page2.getCallBackSourceList().getItems().length > 0)
		{
			for (String s : page2.getCallBackSourceList().getItems())
			{
				if (callBackSource.length() == 0)
					callBackSource = callBackSource + s;
				else
					callBackSource = callBackSource + "|" + s;
			}

		}
		if (page3.getOthFunSrourceList().getItems().length > 0)
		{
			for (String s : page3.getOthFunSrourceList().getItems())
			{
				if (othFunSrource.length() == 0)
					othFunSrource = othFunSrource + s;
				else
					othFunSrource = othFunSrource + "|" + s;
			}

		}
	}
	//将从新建向导中得到的数据写入数据库
	private void doFinish(String svrId, String svrName, String svrDesc,
			String serverSpeclib, String serverSpecAopDlls, String upProject,
			String callBackSource, String othFunSrource)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into server values(?,?,?,?,?,?,?,?)";
		try
		{
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, svrId);
			dbConnImpl.setPreparedString(2, svrName);
			dbConnImpl.setPreparedString(3, svrDesc);
			dbConnImpl.setPreparedString(4, serverSpeclib);
			dbConnImpl.setPreparedString(5, serverSpecAopDlls);
			dbConnImpl.setPreparedString(6, callBackSource);
			dbConnImpl.setPreparedString(7, othFunSrource);
			dbConnImpl.setPreparedString(8, upProject);
			dbConnImpl.executeExceptPreparedQuery();

		} catch (SQLException e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		} finally
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
	//用从新建向导中获得的数据更新导航视图
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
				if (root.getChildren().get(index).getName().equals(upProject))
					break;
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);
			System.out.println(projectNode.getName());

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++)
			{
				if (list.get(i).getName().equals("服务程序"))
					break;
			}
			System.out.println(list.get(i).getName());
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(svrName,
					svrId, list.get(i));
			((ServerNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	@Override
	public boolean canFinish()
	{
		// TODO 自动生成的方法存根
		return page0.canFlipToNextPage() && page1.canFlipToNextPage();

	}

}
