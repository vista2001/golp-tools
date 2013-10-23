package dev.wizards.newAop;

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
import dev.model.resource.AopNodes;
import dev.model.resource.ProjectNode;
import dev.views.NavView;

public class NewAopWizard extends Wizard implements INewWizard
{
	private ISelection selection;
	private IWorkbench workbench;

	private NewAopWizardPage0 page0;
	private NewAopWizardPage1 page1;

	private String aopId = "";
	private String aopName = "";
	private String aopDesc = "";
	private String aopInputData ="";
	private String aopOutputData="";
	private String aopPrecondition="";
	private String aopPostcondition="";
	private String aopErrRecover="";
	private String aopLvL = "";
	private String aopUpDll = "";
	private String aopUpProject;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		this.selection = selection;
		this.workbench = workbench;
	}

	public void addPages()
	{
		page0 = new NewAopWizardPage0(selection);
		page1 = new NewAopWizardPage1(selection);

		addPage(page0);
		addPage(page1);
	}

	@Override
	public boolean performFinish()
	{
		getData();
		doFinish(aopId, aopName, aopDesc, aopInputData, aopOutputData,
				aopPrecondition, aopPostcondition, aopErrRecover, aopUpDll,
				aopUpProject, aopLvL);
		updateNavView();
		return true;
	}

	// //获取该新建向导中所设置的数据
	private void getData()
	{
		aopId = page0.getAopIdText().getText();
		aopName = page0.getAopNameText().getText();
		aopDesc = page0.getAopDescText().getText();
		aopInputData = page1.getInputData().getText();
		aopOutputData = page1.getOutPutData().getText();
		aopPrecondition = page1.getAopPreConditionText().getText();
		aopPostcondition = page1.getAopPostConditionText().getText();
		String errRectmp=page1.getAopErrRecoverCombo().getText();
		if(errRectmp.equals("第一种机制")){
			aopErrRecover = "0";
		}else{
			aopErrRecover= "1";
		}
		aopUpDll=page1.getUpDllCombo().getText();
		aopUpProject = page0.getUpProjectCombo().getText();
		aopLvL = page0.getAopLvlCombo().getText();
		String lvltmp=page0.getAopLvlCombo().getText();
		if(lvltmp.equals("GOLP")){
			aopLvL = "0";
		} else {
			aopLvL = "1";
		}
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(String aopId, String aopName,
			String aopDesc, String aopInputData, String aopOutputData, String aopPrecondition,
			String aopPostcondition, String aopErrRecover, String aopUpDll,String aopUpProject,String aopLvL)
	{
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into aop values(?,?,?,?,?,?,?,?,null,?,?,?)";
		try
		{
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, aopId);
			dbConnImpl.setPreparedString(2, aopName);
			dbConnImpl.setPreparedString(3, aopDesc);
			dbConnImpl.setPreparedString(4, aopInputData);
			dbConnImpl.setPreparedString(5, aopOutputData);
			dbConnImpl.setPreparedString(6, aopPrecondition);
			dbConnImpl.setPreparedString(7, aopPostcondition);
			dbConnImpl.setPreparedString(8, aopErrRecover);
			dbConnImpl.setPreparedString(9, aopUpDll);
			dbConnImpl.setPreparedString(10, aopUpProject);
			dbConnImpl.setPreparedString(11, aopLvL);
			dbConnImpl.executeExceptPreparedQuery();
		} 
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		finally
		{
			try
			{
				dbConnImpl.closeConn();
			} catch (SQLException e)
			{
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
			for (index = 0; index < root.getChildren().size(); index++){
				if (root.getChildren().get(index).getName().equals(aopUpProject)){
					break;
				}
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("原子交易"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopId, aopName, list.get(i));
			((AopNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
		
	}

	@Override
	public boolean canFinish()
	{
		return page0.canFlipToNextPage() && page1.validInput() ;
		//return page0.canFlipToNextPage() && page1.validInput();
	}
	
}
