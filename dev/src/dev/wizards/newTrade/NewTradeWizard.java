package dev.wizards.newTrade;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import dev.model.resource.TradeNodes;
import dev.views.NavView;

public class NewTradeWizard extends Wizard implements INewWizard {
	private ISelection selection;
	private NewTradeWizardPage0 page0;
	private NewTradeWizardPage1 page1;
	private String tradeId;
	private String tradeName;
	private String tradeDesc;
	private String tradeUpServer;
	private String tradeModel;
	private String tradeServerModel;
	private String inputData;
	private String outputData;
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

	public String getTradeId() {
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
		//setNeedsProgressMonitor(true);
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
		//获取数据
		getData();
		//创建数据库记录
		createDbRecord(tradeId,        
				tradeName,      
				tradeDesc,      
				tradeUpServer,  
				tradeModel,     
				tradeServerModel,
				inputData,      
				outputData,     
				precondition,   
				postcondition,  
				callService,
				tradeLevel);
		//通知其它视图或编辑器等
		informParts(tradeId, tradeName, tradeDesc);
		
		return true;
	}

	@Override
	public boolean canFinish() {
		boolean p0 = page0.canFlipToNextPage();
		boolean p1 = page1.canFlipToNextPage();
		if (p0 && p1 && p1) {
			return true;
		}
		return false;
	}

	private void createDbRecord(
			String tradeId,
			String tradeName,
			String tradeDesc,
			String tradeUpServer,
			String tradeModel,
			String tradeServerMode,
			String inputData,
			String outputData,
			String precondition,
			String postcondition,
			String callService,
			String tradeLevel) {
		DbConnectImpl dbConnImpl = new DbConnectImpl();
		dbConnImpl.openConn();
		try {
			String preSql="insert into trade values(?,?,?,?,?,?,'',?,?,?,?,?,'','',?)";
			dbConnImpl.setPrepareSql(preSql);
			dbConnImpl.setPreparedString(1, tradeId);
			dbConnImpl.setPreparedString(2, tradeName);
			dbConnImpl.setPreparedString(3, tradeDesc);
			dbConnImpl.setPreparedString(4, tradeUpServer);
			dbConnImpl.setPreparedString(5, tradeModel);
			dbConnImpl.setPreparedString(6, tradeServerMode);
			
			dbConnImpl.setPreparedString(7, inputData);
			dbConnImpl.setPreparedString(8, outputData);
			dbConnImpl.setPreparedString(9, precondition);
			dbConnImpl.setPreparedString(10, postcondition);
			
			dbConnImpl.setPreparedString(11, callService);
			
			dbConnImpl.setPreparedString(12, tradeLevel);
			dbConnImpl.executeExceptPreparedQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/** 通知其他视图或编辑器*/
	private void informParts(String prjId, String prjName, String prjDesc) {
	IViewPart view = this.workbench.getActiveWorkbenchWindow().getActivePage().findView(NavView.ID);
	if (view != null) {
		NavView v = (NavView) view;
		TreeViewer tv = v.getTreeViewer();
		RootNode root = (RootNode) tv.getInput();
		int index;
		for (index = 0; index < root.getChildren().size(); index++)
		{
			if (root.getChildren().get(index).getName().equals(tradeUpProject))
				break;
		}
		ProjectNode projectNode = (ProjectNode) root.getChildren().get(index);

		List<TreeNode> list = projectNode.getChildren();
		int i;
		for (i = 0; i < list.size(); i++)
		{
			if (list.get(i).getName().equals("交易")){
				break;
			}
		}
		ResourceLeafNode resourceLeafNode = new ResourceLeafNode(tradeId,tradeName, list.get(i));
		((TradeNodes) list.get(i)).add(resourceLeafNode);
		tv.refresh();
		}
	}
	
	private void getData(){
		tradeId=page0.getTradeIdText().getText();
		tradeName=page0.getTradeNameText().getText();
		tradeDesc=page0.getTradeDescText().getText();
		tradeUpServer=page1.getTradeUpServerCombo().getText();
		tradeModel=page1.getTradeModelCombo().getText();
		if(tradeModel.equals("手工编码")){
			tradeModel="0";
		}else{
			tradeModel="1";
		}
		tradeServerModel=page1.getTradeServerModelCombo().getText();
		if(tradeServerModel.equals("模式1")){
			tradeServerModel="0";
		}else{
			tradeServerModel="1";
		}
		inputData=page1.getInputDataText().getText();
		outputData=page1.getOutputDataText().getText();
		precondition=page1.getTradePreConditionText().getText();
		postcondition=page1.getTradePostConditionText().getText();
		callService=page1.getTradeCallServiceText().getText();
		tradeUpProject=page0.getTradeUpProjectCombo().getText();
		tradeLevel=page0.getTradeLvlCombo().getText();
		String lvltmp=page0.getTradeLvlCombo().getText();
		if(lvltmp.equals("GOLP")){
			tradeLevel = "0";
		} else {
			tradeLevel = "1";
		}
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "ccp", IStatus.OK, message,
				null);
		throw new CoreException(status);
	}
}
