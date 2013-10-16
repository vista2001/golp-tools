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

import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.model.resource.RetCodeNodes;
import dev.views.NavView;

public class NewRetCodeWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private IWorkbench workbench;

	private NewRetCodeWizardPage0 page0;
	private NewRetCodeWizardPage1 page1;

	private String retCodeId = "";
	private String retCodeName = "";
	private String retCodeDesc = "";
	private String retCodeUpProject;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

	public void addPages() {
		page0 = new NewRetCodeWizardPage0(selection);
		page1 = new NewRetCodeWizardPage1(selection); 
		addPage(page0);
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		getData();
		doFinish(retCodeId, retCodeName, retCodeDesc, retCodeUpProject);
		updateNavView();
		return true;
	}

	@Override
	public boolean canFinish() {
		return page0.canFlipToNextPage() && page1.validInput();
	
	}

	// //获取该新建向导中所设置的数据
	private void getData() {
		retCodeId = page0.getRetCodeIdText().getText();
		retCodeName = page0.getRetCodeNameText().getText();
		retCodeDesc = page0.getRetCodeDescText().getText();
		retCodeUpProject = page1.getRetCodeUpProjectCombo().getText();
	}

	// 将从新建向导中得到的数据写入数据库
	private void doFinish(String retCodeId, String retCodeName,
			String retCodeDesc, String retCodeUpProject) {
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		String preSql = "insert into retCode values(?,?,?,?)";
		try {
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, retCodeId);
			dbConnImpl.setPreparedString(2, retCodeName);
			dbConnImpl.setPreparedString(3, retCodeDesc);
			dbConnImpl.setPreparedString(4, retCodeUpProject);
			dbConnImpl.executeExceptPreparedQuery();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
				if (root.getChildren().get(index).getName().equals(retCodeUpProject))
					break;
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("响应码"))
					break;
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(
					retCodeName, retCodeId, list.get(i));
			((RetCodeNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}
}
