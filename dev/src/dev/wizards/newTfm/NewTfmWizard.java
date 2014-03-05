/* �ļ�����       NewTfmWizard.java
 * ������           ���ļ���������NewTfmWizard������ʵ�����½�����ͼ�򵼡�
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.10
 */

package dev.wizards.newTfm;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.preference.PreferenceStore;
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
import dev.model.resource.TFMNodes;
import dev.util.CommonUtil;
import dev.util.DevLogger;
import dev.views.NavView;

/**
 * ���ඨ�����½�����ͼ������Ҫ�� Wizard�ࡣ
 */
public class NewTfmWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private IWorkbench workbench;

	// ���ɸ��򵼵���ҳ
	private NewTfmWizardPage0 page0;

	// ����ͼ��������
	private String tfmUpProject = "";

	// ����ͼ���󶨵Ľ��׵�ID
	private int bindingTradeId;

	// ����ͼ������
	private int tfmType;

	// ����ͼ��ID
	private int tfmId;

	// ����ͼ������
	private String tfmName = "";

	// ����ͼ��������Ϣ
	private String tfmDesc = "";

	// ���ڼ��ش洢�й�����Ϣ�ı����ļ���PreferenceStore����
	private PreferenceStore ps;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {

		this.selection = selection;
		this.workbench = workbench;
	}

	public void addPages() {
		page0 = new NewTfmWizardPage0(selection);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {

		getData();
		// initPs();
		ps = CommonUtil.initPs(tfmUpProject);
		try {
			doFinish(tfmId, tfmName, tfmDesc, tfmType, bindingTradeId);
			updateNavView();
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		return true;
	}

	/**
	 * ��ȡ���½����������������
	 */
	private void getData() {
		tfmUpProject = page0.getTfmUpProjectCombo().getText();
		if (page0.getBindingTradeIdText().getText().equals("����")) {
			bindingTradeId = -1;
		} else {
			bindingTradeId = Integer.parseInt(page0.getBindingTradeIdText()
					.getText());
		}
		tfmType = Integer.parseInt(page0.getTfmTypeCombo().getText()
				.substring(0, 1));
		tfmId = Integer.parseInt(page0.getTfmIdText().getText());
		tfmName = page0.getTfmNameText().getText();
		tfmDesc = page0.getTfmDescText().getText();

	}

	/**
	 * �����½����еõ�������д�����ݿ�
	 * 
	 * @param tfmId
	 *            ����ͼ��ID
	 * @param tfmName
	 *            ����ͼ������
	 * @param tfmDesc
	 *            ����ͼ������
	 * @param tfmType
	 *            ����ͼ������
	 * @param bindingTradeId
	 *            ����ͼ���󶨵Ľ���ID
	 * @throws SQLException
	 */
	private void doFinish(int tfmId, String tfmName, String tfmDesc,
			int tfmType, int bindingTradeId) throws SQLException {
		DbConnectImpl dbConnImpl = new DbConnectImpl();

		String preSql = "insert into t_tfm (tfmId, tfmName, tfmDesc, type, tradeId) "
				+ "values(?,?,?,?,?)";
		try {
			dbConnImpl.openConn(ps);
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedInt(1, tfmId);
			dbConnImpl.setPreparedString(2, tfmName);
			dbConnImpl.setPreparedString(3, tfmDesc);
			dbConnImpl.setPreparedInt(4, tfmType);
			dbConnImpl.setPreparedInt(5, bindingTradeId);
			dbConnImpl.executeExceptPreparedQuery();

		} finally {
			try {
				if (dbConnImpl != null) {
					dbConnImpl.closeConn();
				}
			} catch (SQLException e) {

				e.printStackTrace();
				DevLogger.printError(e);
			}
		}
	}

	/**
	 * ������ർ����ͼ
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
						.equals(tfmUpProject)) {
					break;
				}
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("����ͼ"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(tfmName,
					tfmId + "", list.get(i));
			((TFMNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	// /**
	// * ���洢������Ϣ�ı����ļ�*.properties���ص�ps�С�
	// */
	// private void initPs()
	// {
	// IWorkspace workspace = ResourcesPlugin.getWorkspace();
	// IWorkspaceRoot root = workspace.getRoot();
	// IProject project = root.getProject(tfmUpProject);
	// String propertyPath = project.getLocationURI().toString().substring(6)
	// + File.separator + tfmUpProject + ".properties";
	// ps = new PreferenceStore(propertyPath);
	// try
	// {
	// ps.load();
	// } catch (IOException e)
	// {
	//
	// e.printStackTrace();DevLogger.printError(e);
	// }
	// }

	@Override
	public boolean canFinish() {

		return page0.isInputValid();

	}
}
