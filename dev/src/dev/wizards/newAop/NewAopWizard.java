/* �ļ�����       NewAopWizard.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.�޸�updateNavView�����������´���
 *         ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopId, aopName, list.get(i));
 *         �޸�Ϊ
 *         ResourceLeafNode resourceLeafNode = new ResourceLeafNode(aopName, aopId, list.get(i));
 *         ������½�ԭ�ӽ��׺󣬵������½��Ľڵ���ʾ����ID������NAME�����⡣
 *         2.���Ӷ�AopRetVal��ԭ�ӽ��׷���ֵ���Ĵ���
 *         3.��д���ݿ�ķ����У���ȥUPPROJECT����ֶΡ�
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

	// ���ڼ��ش洢�й�����Ϣ�ı����ļ���PreferenceStore����
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

	// //��ȡ���½����������õ�����
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

	// �����½����еõ�������д�����ݿ�
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
				if (list.get(i).getName().equals("ԭ�ӽ���"))
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
