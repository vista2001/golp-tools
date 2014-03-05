/* �ļ�����       NewRetCodeWizard.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.12
 * �޸����ݣ�   ��NewRetCodeWizardPage1�е���������NewRetCodeWizardPage0�����ԴӴ����У�
 *         ɾȥNewRetCodeWizardPage1��ֻ����NewRetCodeWizardPage0��
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
	 * ��ȡ�û��ڸ��½���������������ݡ�
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
	 * �����½����еõ�������д�����ݿ�
	 * 
	 * @param retCodeId
	 *            ��Ӧ��ID
	 * @param retCodeValue
	 *            ��Ӧ��ֵ
	 * @param retCodeDesc
	 *            ��Ӧ������
	 * @param retCodeLevel
	 *            ��Ӧ�뼶��
	 * @throws SQLException
	 */
	private void doFinish(TRetCode retCode, String prjId) throws SQLException {
		EditorRetcodeServiceImpl editorRetcodeServiceImpl = new EditorRetcodeServiceImpl();
		editorRetcodeServiceImpl.insertRetCode(retCode, prjId);
	}

	/**
	 * �ڵ�����ͼ�У���Ӧ���̵���Ӧ��ڵ��£�����һ���µ��ӽڵ㡣
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
				if (list.get(i).getName().equals("��Ӧ��"))
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
