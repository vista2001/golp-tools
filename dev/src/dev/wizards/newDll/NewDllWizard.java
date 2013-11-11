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

import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.DllNodes;
import dev.model.resource.ProjectNode;
import dev.views.NavView;
/**
 * ���ඨ�����½���̬��������Ҫ�� Wizard��
 */
public class NewDllWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;
	private NewDllWizardPage0 page0;
	private String dllUpProject = "";
	private String dllLevel = "";
	private String dllId = "";
	private String dllName = "";
	private String dllDesc = "";
	private String dllType = "";
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// TODO �Զ����ɵķ������
		this.selection = selection;
		this.workbench = workbench;
	}
	
	public void addPages()
	{
		page0 = new NewDllWizardPage0(selection);
		addPage(page0);
	}

	@Override
	public boolean performFinish()
	{
		// TODO �Զ����ɵķ������
		getData();
		doFinish(dllId, dllName, dllDesc, dllType,dllLevel,dllUpProject);
		updateNavView();
		return true;
	}
	
	//��ȡ���½����������õ�����
	private void getData()
	{
		dllUpProject = page0.getDllUpProjectCombo().getText();
		dllLevel = page0.getDllLevelCombo().getText().substring(0, 1);
		dllId = page0.getDllIdText().getText();
		dllName = page0.getDllNameText().getText();
		dllDesc = page0.getDllDescText().getText();
		dllType = page0.getDllTypeCombo().getText();
		
	}
	
	// �����½����еõ�������д�����ݿ�
	private void doFinish(String dllId, String dllName,
			String dllDesc, String dllType,String dllLevel, String dllUpProject)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into aopdll values(?,?,?,?,?,?)";
		try
		{
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, dllId);
			dbConnImpl.setPreparedString(2, dllName);
			dbConnImpl.setPreparedString(3, dllDesc);
			dbConnImpl.setPreparedString(4, dllType);
			dbConnImpl.setPreparedString(5, dllLevel);
			dbConnImpl.setPreparedString(6, dllUpProject);
			dbConnImpl.executeExceptPreparedQuery();

		} 
		catch (SQLException e1)
		{
			// TODO �Զ����ɵ� catch ��
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
	//������ർ����ͼ
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
				if (list.get(i).getName().equals("��̬��"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(dllName,
					dllId, list.get(i));
			((DllNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	@Override
	public boolean canFinish()
	{
		// TODO �Զ����ɵķ������
		return page0.validInput();

	}
}
