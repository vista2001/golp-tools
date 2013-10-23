package dev.editors.Trade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.model.base.ResourceLeafNode;

public class TradeEditor extends EditorPart {

	public static final String ID = "dev.editors.Trade.TradeEditor"; //$NON-NLS-1$
	private Text upProjectText;
	private Text tradeIdText;
	private Text tradeNameText;
	private Text upProjectText1;
	private Text tradeNameText1;
	private Text tradeLevelText;
	private Text tradeIdText1;
	private Text trancationFlagText;
	private Text tradeExtFlagText;
	private Text inputDataText;
	private Text outputDataText;
	private Text preconditionText;
	private Text postconditionText;
	private Text callServiceText;
	private Text tradeDescText;
	private Text tradeExtNodesText;

	private IEditorInput input;

	public TradeEditor() {
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		createControls(parent);
		//初始化数据
		initDataAndState(input);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		this.input=input;
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void initDataAndState(IEditorInput input) {
		// 在workspace中查询对应工程的数据库配置
		ResourceLeafNode rln = ((TradeEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'+ prjId + ".properties";
		System.out.println("dbfiles==="+dbfiles);
		PreferenceStore ps = new PreferenceStore(dbfiles);
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		dbConnectImpl.openConn(ps);
		try {
			ResultSet rs = dbConnectImpl.retrive("select prjid from project");
			if(rs.next()){
				String tmp = rs.getString(1);
				upProjectText.setText(tmp);
				upProjectText1.setText(tmp);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				dbConnectImpl.closeConn();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		dbConnectImpl.openConn(ps);
		/*ResultSet rs=dbConnectImpl.retrive("select * from trade where tradeid='"+input.getName()+"'");
		Map<String, String> map=new HashMap<String,String>();
		while(rs.next()){
			map.put("tradeNameText1", rs.getString(2));
			map.put("", )
			tradeLevelText;
			tradeIdText1;
			trancationFlagText;
			tradeExtFlagText;
			inputDataText;
			outputDataText;
			preconditionText;
			postconditionText;
			callServiceText;
			tradeDescText;
			tradeExtNodesText;
		}*/
	}

	private void createControls(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout(8, false));
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		group.setText("交易查询");

		Label upProjectLabel = new Label(group, SWT.NONE);
		upProjectLabel.setText("所属工程");
		upProjectText = new Text(group, SWT.BORDER);
		GridData gd_upProjectText = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_upProjectText.widthHint = 70;
		upProjectText.setLayoutData(gd_upProjectText);
		upProjectText.setEnabled(false);
		Label tradeIdLabel = new Label(group, SWT.NONE);
		tradeIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		tradeIdLabel.setText("交易标识符");
		tradeIdText = new Text(group, SWT.BORDER);
		GridData gd_tradeIdText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeIdText.widthHint = 70;
		tradeIdText.setLayoutData(gd_tradeIdText);
		Label tradeNameLabel = new Label(group, SWT.NONE);
		tradeNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		tradeNameLabel.setText("交易名称");
		tradeNameText = new Text(group, SWT.BORDER);
		GridData gd_tradeNameText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeNameText.widthHint = 70;
		tradeNameText.setLayoutData(gd_tradeNameText);
		new Label(group, SWT.NONE);
		Button tradeQueryBtn = new Button(group, SWT.NONE);
		GridData gd_tradeQueryBtn = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeQueryBtn.widthHint = 80;
		tradeQueryBtn.setLayoutData(gd_tradeQueryBtn);
		tradeQueryBtn.setText("查询");
		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayout(new GridLayout(8, false));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		group_1.setText("交易");

		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label label = new Label(group_1, SWT.NONE);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2,
				1);
		gd_label.widthHint = 70;
		label.setLayoutData(gd_label);

		Label upProjectLabel1 = new Label(group_1, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		upProjectLabel1.setText("所属工程");

		upProjectText1 = new Text(group_1, SWT.BORDER);
		upProjectText1.setEnabled(false);
		GridData gd_upProjectText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_upProjectText1.widthHint = 70;
		upProjectText1.setLayoutData(gd_upProjectText1);

		Label label_1 = new Label(group_1, SWT.NONE);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				2, 1);
		gd_label_1.widthHint = 70;
		label_1.setLayoutData(gd_label_1);

		Label tradeLevelLabel = new Label(group_1, SWT.NONE);
		tradeLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeLevelLabel.setText("交易级别");

		tradeLevelText = new Text(group_1, SWT.BORDER);
		tradeLevelText.setEnabled(false);
		GridData gd_tradeLevelText = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeLevelText.widthHint = 70;
		tradeLevelText.setLayoutData(gd_tradeLevelText);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeIdLabel1 = new Label(group_1, SWT.NONE);
		tradeIdLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		tradeIdLabel1.setText("交易标识符");

		tradeIdText1 = new Text(group_1, SWT.BORDER);
		GridData gd_tradeIdText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeIdText1.widthHint = 70;
		tradeIdText1.setLayoutData(gd_tradeIdText1);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeNameLabel1 = new Label(group_1, SWT.NONE);
		tradeNameLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeNameLabel1.setText("交易名称");

		tradeNameText1 = new Text(group_1, SWT.BORDER);
		GridData gd_tradeNameText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeNameText1.widthHint = 70;
		tradeNameText1.setLayoutData(gd_tradeNameText1);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label upServerLabel = new Label(group_1, SWT.NONE);
		upServerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		upServerLabel.setText("所属服务程序");

		Combo upServerCombo = new Combo(group_1, SWT.READ_ONLY);
		GridData gd_upServerCombo = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_upServerCombo.widthHint = 53;
		upServerCombo.setLayoutData(gd_upServerCombo);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeModelLabel = new Label(group_1, SWT.NONE);
		tradeModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeModelLabel.setText("交易类型");

		Combo tradeModelCombo = new Combo(group_1, SWT.READ_ONLY);
		GridData gd_tradeModelCombo = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeModelCombo.widthHint = 53;
		tradeModelCombo.setLayoutData(gd_tradeModelCombo);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeServerModelLabel = new Label(group_1, SWT.NONE);
		tradeServerModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeServerModelLabel.setText("交易服务模式");

		Combo tradeServerModelCombo = new Combo(group_1, SWT.READ_ONLY);
		GridData gd_tradeServerModelCombo = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_tradeServerModelCombo.widthHint = 53;
		tradeServerModelCombo.setLayoutData(gd_tradeServerModelCombo);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label trancationFlagLabel = new Label(group_1, SWT.NONE);
		trancationFlagLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		trancationFlagLabel.setText("事务标志");

		trancationFlagText = new Text(group_1, SWT.BORDER);
		trancationFlagText.setEditable(false);
		GridData gd_trancationFlagText = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_trancationFlagText.widthHint = 70;
		trancationFlagText.setLayoutData(gd_trancationFlagText);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeExtFlagLabel = new Label(group_1, SWT.NONE);
		tradeExtFlagLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeExtFlagLabel.setText("扩展点标志");

		tradeExtFlagText = new Text(group_1, SWT.BORDER);
		tradeExtFlagText.setEnabled(false);
		GridData gd_tradeExtFlagText = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_tradeExtFlagText.widthHint = 70;
		tradeExtFlagText.setLayoutData(gd_tradeExtFlagText);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeExtNodesLabel = new Label(group_1, SWT.NONE);
		tradeExtNodesLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeExtNodesLabel.setText("交易扩展点");

		tradeExtNodesText = new Text(group_1, SWT.BORDER);
		tradeExtNodesText.setEnabled(false);
		tradeExtNodesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 4, 1));

		Button tradeExtNodesBtn = new Button(group_1, SWT.NONE);
		GridData gd_tradeExtNodesBtn = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_tradeExtNodesBtn.widthHint = 80;
		tradeExtNodesBtn.setLayoutData(gd_tradeExtNodesBtn);
		tradeExtNodesBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		tradeExtNodesBtn.setText("详情");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label inputDataLabel = new Label(group_1, SWT.NONE);
		inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		inputDataLabel.setText("输入数据");

		inputDataText = new Text(group_1, SWT.BORDER);
		inputDataText.setEnabled(false);
		inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));

		Button btnNewButton = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_btnNewButton.widthHint = 80;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("...");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label outputDataLabel = new Label(group_1, SWT.NONE);
		outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		outputDataLabel.setText("输出数据");

		outputDataText = new Text(group_1, SWT.BORDER);
		outputDataText.setEnabled(false);
		outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));

		Button btnNewButton_1 = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton_1 = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_btnNewButton_1.widthHint = 80;
		btnNewButton_1.setLayoutData(gd_btnNewButton_1);
		btnNewButton_1.setText("...");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label callServiceLabel = new Label(group_1, SWT.NONE);
		callServiceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		callServiceLabel.setText("调用的交易");

		callServiceText = new Text(group_1, SWT.BORDER);
		callServiceText.setEnabled(false);
		callServiceText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 4, 1));

		Button btnNewButton_4 = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton_4 = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_btnNewButton_4.widthHint = 80;
		btnNewButton_4.setLayoutData(gd_btnNewButton_4);
		btnNewButton_4.setText("...");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label preconditionLabel = new Label(group_1, SWT.NONE);
		preconditionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		preconditionLabel.setText("前置条件");

		preconditionText = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		preconditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 5, 2));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label postconditionLabel = new Label(group_1, SWT.NONE);
		postconditionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		postconditionLabel.setText("后置条件");

		postconditionText = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		postconditionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 5, 2));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeDescLabel = new Label(group_1, SWT.NONE);
		tradeDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		tradeDescLabel.setText("描述");

		tradeDescText = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		tradeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 5, 3));
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Button btnNewButton_2 = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton_2 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnNewButton_2.widthHint = 80;
		btnNewButton_2.setLayoutData(gd_btnNewButton_2);
		btnNewButton_2.setText("解锁");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Button btnNewButton_3 = new Button(group_1, SWT.NONE);
		GridData gd_btnNewButton_3 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_btnNewButton_3.widthHint = 80;
		btnNewButton_3.setLayoutData(gd_btnNewButton_3);
		btnNewButton_3.setText("保存");
		new Label(group_1, SWT.NONE);
	}
}
