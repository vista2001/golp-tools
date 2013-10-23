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
	private NewServerWizardPage4 page4;
	private NewServerWizardPage5 page5;
	
	private String upProject = "";
	private String serverLevel="";
	private String svrId = "";
	private String svrName = "";
	private String svrDesc = "";
	private String serverSpeclibPath = "";
	private String serverSpeclibName = "";
	private String serverSpeclib = "";
	private String serverSpecIncludePath = "";
	private String callBackSource = "";
	private String othFunSrource = "";

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// TODO �Զ����ɵķ������
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
		page4 = new NewServerWizardPage4(selection);
		page5 = new NewServerWizardPage5(selection);

		addPage(page0);
		addPage(page1);
		addPage(page2);
		addPage(page3);
		addPage(page4);
		addPage(page5);

	}

	@Override
	public boolean performFinish()
	{
		// TODO �Զ����ɵķ������	
		getData();
		doFinish(svrId, svrName, svrDesc, serverSpeclib, serverSpecIncludePath,
				callBackSource, othFunSrource,serverLevel,upProject);
		updateNavView();
		return true;
	}
	//��ȡ���½����������õ�����
	private void getData()
	{
		upProject = page0.getUpProjectCombo().getText();
		serverLevel = page0.getServerLevelCombo().getText().substring(0, 1);
		svrId = page0.getSvrIdText().getText();
		svrName = page0.getSvrNameText().getText();
		svrDesc = page0.getSvrDescText().getText();
		
		if (page1.getServerSpeclibPathList().getItems().length > 0)
		{
			for (String s : page1.getServerSpeclibPathList().getItems())
			{
				if (serverSpeclibPath.length() == 0)
					serverSpeclibPath = serverSpeclibPath + s;
				else
					serverSpeclibPath = serverSpeclibPath + "|" + s;
			}
			for (String s : page2.getServerSpeclibNameList().getItems())
			{
				if (serverSpeclibName.length() == 0)
					serverSpeclibName = serverSpeclibName + s;
				else
					serverSpeclibName = serverSpeclibName + "|" + s;
			}
			serverSpeclib = "[" + serverSpeclibPath + "]" + "[" + serverSpeclibName + "]";
		}
		
		if (page3.getServerSpecIncludePathList().getItems().length > 0)
		{
			for (String s : page3.getServerSpecIncludePathList().getItems())
			{
				if (serverSpecIncludePath.length() == 0)
					serverSpecIncludePath = serverSpecIncludePath + s;
				else
					serverSpecIncludePath = serverSpecIncludePath + "|" + s;
			}

		}
		
		if (page4.getCallBackSourceList().getItems().length > 0)
		{
			for (String s : page4.getCallBackSourceList().getItems())
			{
				if (callBackSource.length() == 0)
					callBackSource = callBackSource + s;
				else
					callBackSource = callBackSource + "|" + s;
			}

		}
		if (page5.getOthFunSrourceList().getItems().length > 0)
		{
			for (String s : page5.getOthFunSrourceList().getItems())
			{
				if (othFunSrource.length() == 0)
					othFunSrource = othFunSrource + s;
				else
					othFunSrource = othFunSrource + "|" + s;
			}

		}
	}
	//�����½����еõ�������д�����ݿ�
	private void doFinish(String svrId, String svrName, String svrDesc,
			String serverSpeclib, String serverSpecAopDlls,
			String callBackSource, String othFunSrource, String serverLevel, String upProject)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into server values(?,?,?,?,?,?,?,?,?)";
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
			dbConnImpl.setPreparedString(8, serverLevel);
			dbConnImpl.setPreparedString(9, upProject);
			dbConnImpl.executeExceptPreparedQuery();

		} catch (SQLException e1)
		{
			// TODO �Զ����ɵ� catch ��
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
	//�ô��½����л�õ����ݸ��µ�����ͼ
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
				if (list.get(i).getName().equals("�������"))
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
		// TODO �Զ����ɵķ������
		if(page0.canFlipToNextPage())
		{
			if(page1.getServerSpeclibPathList().getItemCount() > 0)
			{
				if(page2.canFlipToNextPage())
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return true;
			}
			
		}
		return false;

	}

}
