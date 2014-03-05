/* 文件名：       NewRetCodeWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.12.12
 * 修改内容：   因将NewRetCodeWizardPage1中的内容移至NewRetCodeWizardPage0，所以从此向导中，
 *         删去NewRetCodeWizardPage1，只保留NewRetCodeWizardPage0。
 */

package dev.wizards.newRetCode;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.pojo.TRetCode;
import dev.db.service.EditorRetcodeServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.model.resource.RetCodeNodes;
import dev.util.DevLogger;
import dev.views.NavView;

public class NewRetCodeWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private IWorkbench workbench;

	private NewRetCodeWizardPage0 page0;

	private String retCodeUpProject = "";
	private TRetCode retCode;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
		retCode = new TRetCode();
	}

	public void addPages() {
		page0 = new NewRetCodeWizardPage0(selection);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		getData();
		try {
			doFinish(retCode, retCodeUpProject);
			updateNavView();
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		return true;
	}

	@Override
	public boolean canFinish() {
		return page0.isInputValid();
	}

	/**
	 * 获取用户在该新建向导中所输入的数据。
	 */
	private void getData() {
		retCodeUpProject = page0.getRetCodeUpProjectCombo().getText();
		String retCodeLevel = page0.getRetCodeLevelCombo().getText();
		retCode.setRetCodeLevel2(retCodeLevel);
		String retCodeId = page0.getRetCodeIdText().getText();
		retCode.setRetCodeId(retCodeId);
		String retCodeValue = page0.getRetCodeValueText().getText();
		retCode.setRetCodeValue(retCodeValue);
		String retCodeDesc = page0.getRetCodeDescText().getText();
		retCode.setRetCodeDesc(retCodeDesc);
	}

	/**
	 * 将从新建向导中得到的数据写入数据库
	 * 
	 * @param retCodeId
	 *            响应码ID
	 * @param retCodeValue
	 *            响应码值
	 * @param retCodeDesc
	 *            响应码描述
	 * @param retCodeLevel
	 *            响应码级别
	 * @throws SQLException
	 */
	private void doFinish(TRetCode retCode, String prjId) throws SQLException {
		EditorRetcodeServiceImpl editorRetcodeServiceImpl = new EditorRetcodeServiceImpl();
		editorRetcodeServiceImpl.insertRetCode(retCode, prjId);
	}

	/**
	 * 在导航视图中，对应工程的响应码节点下，插入一个新的子节点。
	 */
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
						.equals(retCodeUpProject))
					break;
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("响应码"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(
					retCode.getRetCodeValue(), retCode.getRetCodeId(),
					list.get(i));
			((RetCodeNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}
}
