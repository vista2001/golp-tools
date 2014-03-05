/* 文件名：       NewTradeWizard.java
 * 修改人：       rxy
 * 修改时间：   2013.12.3
 * 修改内容：   1.修改canFinish方法，使得在新建交易时，只有所有必填项（带星号的）都输入后，完成按钮才可用；
 *         2.解决在新建交易后，导航中新建的节点显示的是ID而不是NAME的问题；
 *         3.更改tradeId的类型为int。
 */

package dev.wizards.newTrade;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;

import dev.db.service.EditorsDaoServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.ProjectNode;
import dev.model.resource.TradeNodes;
import dev.util.DevLogger;
import dev.views.NavView;

public class NewTradeWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private NewTradeWizardPage0 page0;
	private NewTradeWizardPage1 page1;
	private int tradeId;
	private String tradeName;
	private String tradeDesc;
	private String tradeUpServer;
	private String tradeModel;
	private String tradeServerModel;
	private String inputData;
	private String outputData;
	private String tradeSrcPath;
	private String precondition;
	private String postcondition;
	private String callService;
	private String tradeUpProject;
	private String tradeLevel;

	private IWorkbench workbench;

	public NewTradeWizardPage0 getPage0() {
		return page0;
	}

	public NewTradeWizardPage1 getPage1() {
		return page1;
	}

	public int getTradeId() {
		return tradeId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public String getTradeDesc() {
		return tradeDesc;
	}

	public String getTradeUpServer() {
		return tradeUpServer;
	}

	public String getTradeModel() {
		return tradeModel;
	}

	public String getTradeServerModel() {
		return tradeServerModel;
	}

	public String getInputData() {
		return inputData;
	}

	public String getOutputData() {
		return outputData;
	}

	public String getPrecondition() {
		return precondition;
	}

	public String getPostcondition() {
		return postcondition;
	}

	public String getCallService() {
		return callService;
	}

	public IWorkbench getWorkbench() {
		return workbench;
	}

	public String getTradeUpProject() {
		return tradeUpProject;
	}

	public String getTradeLevel() {
		return tradeLevel;
	}

	public NewTradeWizard() {
		super();
		// setNeedsProgressMonitor(true);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		this.workbench = workbench;
	}

	@Override
	public void addPages() {
		page0 = new NewTradeWizardPage0(selection);
		page1 = new NewTradeWizardPage1(selection);

		addPage(page0);
		addPage(page1);
	}

	@Override
	public boolean performFinish() {
		// 获取数据
		getData();

		// 创建数据库记录
		try {
			createDbRecord(tradeId, tradeName, tradeDesc, tradeUpServer,
					tradeModel, tradeServerModel, inputData, outputData,
					precondition, postcondition, callService, tradeLevel,
					tradeSrcPath);
			// 通知其它视图或编辑器等
			informParts(tradeId, tradeName, tradeDesc);
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		return true;
	}

	@Override
	public boolean canFinish() {
		boolean p0 = page0.canFlipToNextPage();
		boolean p1 = page1.validInput();
		if (p0 && p1) {
			return true;
		}
		return false;
	}

	private void createDbRecord(int tradeId, String tradeName,
			String tradeDesc, String tradeUpServer, String tradeModel,
			String tradeServerMode, String inputData, String outputData,
			String precondition, String postcondition, String callService,
			String tradeLevel, String tradeSrcPath) throws SQLException {

		List<String> dataToUp = new ArrayList<String>();
		dataToUp.add(tradeId + "");
		dataToUp.add(tradeName);
		dataToUp.add(tradeDesc);
		dataToUp.add(tradeUpServer);
		dataToUp.add(tradeModel);
		dataToUp.add(tradeServerMode);
		dataToUp.add(inputData);
		dataToUp.add(outputData);
		dataToUp.add(precondition);
		dataToUp.add(postcondition);
		dataToUp.add(callService);
		dataToUp.add(tradeLevel);
		dataToUp.add(tradeSrcPath);

		EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
		editorsDaoServiceImpl.insertTrade(dataToUp, tradeUpProject);
	}

	/** 通知其他视图或编辑器 */
	private void informParts(int prjId, String prjName, String prjDesc) {
		IViewPart view = this.workbench.getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		if (view != null) {
			NavView v = (NavView) view;
			TreeViewer tv = v.getTreeViewer();
			RootNode root = (RootNode) tv.getInput();
			int index;
			for (index = 0; index < root.getChildren().size(); index++) {
				if (root.getChildren().get(index).getName()
						.equals(tradeUpProject))
					break;
			}
			ProjectNode projectNode = (ProjectNode) root.getChildren().get(
					index);

			List<TreeNode> list = projectNode.getChildren();
			int i;
			for (i = 0; i < list.size(); i++) {
				if (list.get(i).getName().equals("交易")) {
					break;
				}
			}
			ResourceLeafNode resourceLeafNode = new ResourceLeafNode(tradeName,
					tradeId + "", list.get(i));
			((TradeNodes) list.get(i)).add(resourceLeafNode);
			tv.refresh();
		}
	}

	private void getData() {
		tradeId = Integer.parseInt(page0.getTradeIdText().getText());
		tradeName = page0.getTradeNameText().getText();
		tradeDesc = page0.getTradeDescText().getText();
		tradeUpServer = page1.getTradeUpServerText().getText();
		tradeModel = page1.getTradeModelCombo().getText().substring(0, 1);
		/*
		 * if(tradeModel.equals("手工编码")){ tradeModel="0"; }else{ tradeModel="1";
		 * }
		 */
		tradeServerModel = page1.getTradeServerModelCombo().getText()
				.substring(0, 1);
		/*
		 * if(tradeServerModel.equals("模式1")){ tradeServerModel="0"; }else{
		 * tradeServerModel="1"; }
		 */
		inputData = page1.getInputDataText().getText();
		outputData = page1.getOutputDataText().getText();
		tradeSrcPath = page1.getTradeSrcPathText().getText().trim();
		precondition = page1.getTradePreConditionText().getText();
		postcondition = page1.getTradePostConditionText().getText();
		callService = page1.getTradeCallServiceText().getText();
		tradeUpProject = page0.getTradeUpProjectCombo().getText();
		tradeLevel = page0.getTradeLvlCombo().getText().substring(0, 1);
		/*
		 * String lvltmp=page0.getTradeLvlCombo().getText();
		 * if(lvltmp.equals("GOLP")){ tradeLevel = "0"; } else { tradeLevel =
		 * "1"; }
		 */
	}

	/*
	 * private void throwCoreException(String message) throws CoreException {
	 * IStatus status = new Status(IStatus.ERROR, "ccp", IStatus.OK, message,
	 * null); throw new CoreException(status); }
	 */
}
