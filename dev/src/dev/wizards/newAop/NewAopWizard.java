/* 文件名：       NewAopWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：   1.修改updateNavView方法，将以下代码
 *         ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopId, aopName, list.get(i));
 *         修改为
 *         ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopName, aopId, list.get(i));
 *         解决在新建原子交易后，导航中新建的节点显示的是ID而不是NAME的问题。
 *         2.增加对AopRetVal（原子交易返回值）的处理；
 *         3.在写数据库的方法中，除去UPPROJECT这个字段。
 */

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

import dev.db.pojo.TAop;
import dev.db.service.EditorAopServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.AopNodes;
import dev.model.resource.ProjectNode;
import dev.util.DevLogger;
import dev.views.NavView;

public class NewAopWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private IWorkbench workbench;

	private NewAopWizardPage0 page0;
	private NewAopWizardPage1 page1;

	// private String aopId = "";
	// private String aopName = "";
	// private String aopDesc = "";
	// private String aopInputData ="";
	// private String aopOutputData="";
	// private String aopPrecondition="";
	// private String aopPostcondition="";
	// private String aopErrRecover="";
	// private String aopLvL = "";
	// private String aopUpDll = "";
	private String aopUpProject;
	private TAop aop;

	// private String aopRetVal = "";

	// 用于加载存储有工程信息的本地文件的PreferenceStore对象
	// private PreferenceStore ps;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
		aop = new TAop();
	}

	public void addPages() {
		page0 = new NewAopWizardPage0(selection);
		page1 = new NewAopWizardPage1(selection);

		addPage(page0);
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		getData();
		try {
			doFinish(aop, aopUpProject);
			updateNavView();
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		return true;
	}

	// //获取该新建向导中所设置的数据
	private void getData() {
		aopUpProject = page0.getUpProjectCombo().getText();
		String aopId = page0.getAopIdText().getText();
		aop.setAopId(aopId);
		String aopName = page0.getAopNameText().getText();
		aop.setAopName(aopName);
		String aopDesc = page0.getAopDescText().getText();
		aop.setAopDesc(aopDesc);
		String aopInputData = page1.getInputData().getText();
		aop.setInputData(aopInputData);
		String aopOutputData = page1.getOutPutData().getText();
		aop.setOutputData(aopOutputData);
		String aopRetVal = page1.getAopRetValText().getText();
		aop.setAopRetVal(aopRetVal);
		String aopPrecondition = page1.getAopPreConditionText().getText();
		aop.setPreCondition(aopPrecondition);
		String aopPostcondition = page1.getAopPostConditionText().getText();
		aop.setPostCondition(aopPostcondition);
		String aopErrRecover = page1.getAopErrRecoverCombo().getText()
				.substring(0, 1);
		aop.setAopErrRecover(aopErrRecover);
		String aopUpDll = page1.getUpDllCombo().getText();
		aop.setUpAopDll(Integer.parseInt(aopUpDll));
		String aopLvL = page0.getAopLvlCombo().getText().substring(0, 1);
		aop.setAopLevel(aopLvL);
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(TAop aop, String prjId) throws SQLException {
		EditorAopServiceImpl editorAopServiceImpl = new EditorAopServiceImpl();
		editorAopServiceImpl.insertAop(aop, prjId);
	}

	private void updateNavView() {
		IViewPart view = this.workbench.getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		if (view != null) {
			NavView v = (NavView) view;
			TreeViewer tv = v.getTreeViewer();
			RootNode root = (RootNode) tv.getInput();

			int index;
			for (index = 0; index < root.getChildren().size(); index++) {
				if (root.getChildren().get(index).getName()
						.equals(aopUpProject)) {
					break;
				}
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("原子交易"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(
					aop.getAopName(), aop.getAopId(), list.get(i));
			((AopNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}

	}

	@Override
	public boolean canFinish() {
		return page0.canFlipToNextPage() && page1.validInput();
		// return page0.canFlipToNextPage() && page1.validInput();
	}

}
