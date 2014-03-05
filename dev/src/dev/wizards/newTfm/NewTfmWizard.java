/* 文件名：       NewTfmWizard.java
 * 描述：           该文件定义了类NewTfmWizard，该类实现了新建流程图向导。
 * 创建人：       rxy
 * 创建时间：   2013.12.10
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
 * 该类定义了新建流程图向导所需要的 Wizard类。
 */
public class NewTfmWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private IWorkbench workbench;

	// 构成该向导的向导页
	private NewTfmWizardPage0 page0;

	// 流程图所属工程
	private String tfmUpProject = "";

	// 流程图所绑定的交易的ID
	private int bindingTradeId;

	// 流程图的类型
	private int tfmType;

	// 流程图的ID
	private int tfmId;

	// 流程图的名称
	private String tfmName = "";

	// 流程图的描述信息
	private String tfmDesc = "";

	// 用于加载存储有工程信息的本地文件的PreferenceStore对象
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
	 * 获取该新建向导中所输入的数据
	 */
	private void getData() {
		tfmUpProject = page0.getTfmUpProjectCombo().getText();
		if (page0.getBindingTradeIdText().getText().equals("不绑定")) {
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
	 * 将从新建向导中得到的数据写入数据库
	 * 
	 * @param tfmId
	 *            流程图的ID
	 * @param tfmName
	 *            流程图的名称
	 * @param tfmDesc
	 *            流程图的描述
	 * @param tfmType
	 *            流程图的类型
	 * @param bindingTradeId
	 *            流程图所绑定的交易ID
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
	 * 更新左侧导航视图
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
				if (list.get(i).getName().equals("流程图"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(tfmName,
					tfmId + "", list.get(i));
			((TFMNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	// /**
	// * 将存储工程信息的本地文件*.properties加载到ps中。
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
