package dev.editors.trade;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorsDaoServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.model.base.TreeNode;
import dev.util.PropertiesUtil;
import dev.views.NavView;
import dev.wizards.newAop.InformDialogEvent;
import dev.wizards.newAop.InformDialogListener;
import dev.wizards.newAop.QueryDataItemDialog;
import dev.wizards.newAop.QueryTradeDialog;

public class TradeEditor extends EditorPart implements ISearch
{

	public static final String ID = "dev.editors.Trade.TradeEditor"; //$NON-NLS-1$
	// 查询部分
	//private Text upProjectText;
	private Text searchText;
	// 编辑部分
	//所属工程
	private Text upProjectText1;
	//交易级别
	private Text tradeLevelText;
	//交易标示符
	private Text tradeIdText1;	
	//交易名称
	private Text tradeNameText1;
	//所属服务程序
	private Text upServerText;
	//交易模式
	private Combo tradeModelCombo;
	//交易服务模式
	private Combo tradeServerModelCombo;
	//事务标志
	private Text trancationFlagText;
	//交易扩展点标志
	private Text tradeExtFlagText;
	//交易扩展点
	private Text tradeExtNodesText;
	//输入数据项
	private Text inputDataText;
	//输出数据项
	private Text outputDataText;
	//前置条件
	private Text preconditionText;
	//后置条件
	private Text postconditionText;
	//调用的交易
	private Text callServiceText;
	//交易描述
	private Text tradeDescText;
	//选择所属服务程序的按钮
	private Button upServerBtn;
	//选择输入数据项的按钮
	private Button inputSelBtn;
	//选择输出数据项的按钮
	private Button outputSelBtn;
	//选择调用的服务的按钮
	private Button callServiceBtn;
	//解锁按钮
	private Button unlockButton;
	//保存按钮
	private Button saveButton;
	//private Button tradeQueryBtn;
	//恢复按钮
	private Button recoverBtn;
	//编辑器污染标识
	private boolean dirty=false;
	//查询部分
	private Search search;
	//编辑器的输入
	private IEditorInput input;
	//需要解锁的控件集合
	private List<Control> controlsForLock = new ArrayList<Control>();
	//必须输入的控件集合
	private List<Control> controlsForInput = new ArrayList<Control>();
	//用于恢复按钮的原始数据
	private Map<String,String> recoverMap=new HashMap<String,String>();
	private Map<String, String> contentMap=new HashMap<String,String>();
	//用于查询时提供数据库的链接地址
	private PreferenceStore ps = null;
	//编辑器对应导航中的树节点
	private ResourceLeafNode resourceLeafNode;
	

	public TradeEditor()
	{
	}

	/**
	 * Create contents of the editor part.
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		createControls(parent);
		// 初始化数据和状态
		initDataAndState(input);
		setPartName(" 交易" + " " + input.getName()+" "+"所属工程" + " " + upProjectText1.getText() );
		//
	}
	/**
	 * 设置dirty的状态
	 * @param dirty
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	@Override
	public void setFocus()
	{
		// Set the focus
		setSelectionNode(((TradeEditorInput)input).getSource());
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		// Do the Save operation
		saveMethod();
	}

	@Override
	public void doSaveAs()
	{
		// Do the Save As operation
		
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException
	{
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		this.input = input;
		this.resourceLeafNode = ((TradeEditorInput) input).getSource();
		initps();
	}

	private void initps() {
		// TODO Auto-generated method stub
		try {
			ps = PropertiesUtil.getPropertiesByRln(resourceLeafNode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 判定编辑器是否被污染
	 */
	@Override
	public boolean isDirty()
	{
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		return false;
	}

	/** 初始化控件数据和状态
	 * 首先会设定页面当前工程的id，
	 * 然后在数据库查询页面的内容，
	 * 将内容放置到控件内，
	 * 设定控件的解锁状态
	 * 为可编辑的控件增加modify侦听
	 * @param input 当前编辑器的input
	 */
	private void initDataAndState(IEditorInput input)
	{
		// 设置所属工程
		String prjid = getProjectData(input);
		//upProjectText.setText(prjid);
		upProjectText1.setText(prjid);
		contentMap = getTradeData(input);
		// 设置控件数据
		setTradeData(contentMap);
		// 获得可以解锁的控件
		getControls();
		// 设置解锁按钮状态
		if (tradeLevelText.getText().equals("0-APP"))
		{
			unlockButton.setEnabled(true);
		}
		//为可编辑的控件增加modify侦听
		addModify();
	}

	/** 获得工程id
	 * @param input 当前编辑器的input，这个对象用于获得当前工程的配置信息，以供数据库访问程序使用
	 * @return  返回一个String类型的对象，这个对象就是通过查询数据库得到的工程id
	 */
	private String getProjectData(IEditorInput input)
	{
		String prjid = "";
		
		EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
		try
		{
			
			prjid = editorsDaoServiceImpl.queryProjectId(ps);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return prjid;
	}

	/** 
	 * 获得交易编辑页面的数据
	 * @param input 当前编辑器的input，这个对象用于获得当前工程的数据库配置信息，以供数据库访问程序使用
	 * @return 返回一个map，如果为空则map的内容为空，但不是null
	 */
	private Map<String, String> getTradeData(IEditorInput input)
	{
		Map<String, String> map = new HashMap<String, String>();
		EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();

		try
		{
			map = editorsDaoServiceImpl.queryTradeByIdOrName(
						input.getName(), "", ps);
			recoverMap=map;

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return map;
	}

	/**  
	 * 设置交易页面数据，传入的map是查询数据库返回得到的结果
	 * @param map 传入一个存放页面内容的map，如果不为空就依次放置内容到页面控件中，否则给出用户提示
	 */
	private void setTradeData(Map<String, String> map)
	{
		if (!map.isEmpty())
		{
			// 重置查询组的内容
			//searchText.setText("");
			// 设置编辑组的内容
			tradeLevelText
					.setText(map.get("tradeLevelText").equals("0") ? "0-APP"
							: "1-GOLP");
			tradeIdText1.setText(map.get("ID"));
			tradeNameText1.setText(map.get("NAME"));
			upServerText.setText(map.get("upServerCombo"));
			tradeModelCombo.setItems(new String[] { "0-流程图", "1-手工编码" });
			tradeModelCombo
					.select(Integer.parseInt(map.get("tradeModelCombo")));

			tradeServerModelCombo.setItems(new String[] { "0-模式0", "1-模式1" });
			tradeServerModelCombo.select(Integer.parseInt(map
					.get("tradeServerModelCombo")));

			trancationFlagText.setText(map.get("trancationFlagText"));
			tradeExtFlagText.setText(map.get("tradeExtFlagText"));
			tradeExtNodesText.setText(map.get("tradeExtNodesText"));
			inputDataText.setText(map.get("inputDataText"));
			outputDataText.setText(map.get("outputDataText"));
			preconditionText.setText(map.get("preconditionText"));
			postconditionText.setText(map.get("postconditionText"));
			callServiceText.setText(map.get("callServiceText"));
			tradeDescText.setText(map.get("tradeDescText"));
		} else
		{
			showMessage("未找到对应的记录", SWT.ICON_WARNING);
			// 重置查询组的内容
			searchText.setText("");
			return;
		}

	}

	/** 
	 * 获得要解锁的控件列表
	 */
	private void getControls()
	{
		// 获得可解锁的控件
		controlsForLock.add(tradeNameText1);
		controlsForLock.add(upServerText);
		controlsForLock.add(upServerBtn);
		controlsForLock.add(tradeModelCombo);
		controlsForLock.add(tradeServerModelCombo);
		controlsForLock.add(trancationFlagText);
		// controls.add(tradeExtFlagText);
		controlsForLock.add(inputSelBtn);
		controlsForLock.add(outputSelBtn);
		controlsForLock.add(callServiceBtn);
		controlsForLock.add(preconditionText);
		controlsForLock.add(postconditionText);
		controlsForLock.add(tradeDescText);

		// 获得必填项控件
		controlsForInput.add(tradeNameText1);
		controlsForInput.add(upServerText);
		controlsForInput.add(tradeModelCombo);
		controlsForInput.add(tradeServerModelCombo);
		controlsForInput.add(inputDataText);
		controlsForInput.add(outputDataText);
		/*
		 * controlsForInput.add(callServiceText);
		 * controlsForInput.add(tradeDescText);
		 * controlsForInput.add(preconditionText);
		 * controlsForInput.add(postconditionText);
		 */
	}
	/**
	 * 给可编辑的对象增加ModifyListener,用于在值改变的时候给编辑器设定dirty属性
	 */
	private void addModify(){
		List<Control> controlsCanModify=controlsForInput;
		controlsCanModify.add(callServiceText);
		controlsCanModify.add(tradeDescText);
		controlsCanModify.add(preconditionText);
		controlsCanModify.add(postconditionText);
		for (Control o:controlsCanModify){
			if(o instanceof Text){
				
				((Text) o).addModifyListener(new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent e) {
						if(!isDirty()){
							setDirty(true);
							saveButton.setEnabled(true);
						}
						firePropertyChange(IEditorPart.PROP_DIRTY);
					}
				});
			}
			if(o instanceof Combo){
				((Combo) o).addModifyListener(new ModifyListener() {
					
					@Override
					public void modifyText(ModifyEvent e) {
						// TODO Auto-generated method stub
						if(!isDirty()){
							setDirty(true);
							saveButton.setEnabled(true);
						}
						firePropertyChange(IEditorPart.PROP_DIRTY);
					}
				});
			}
		}
	}
	
	/** 
	 * 解锁控件
	 */
	private void enableControls()
	{
		for (Control o : controlsForLock)
		{
			o.setEnabled(true);
		}
		
	}
	
	/**
	 *  锁定控件
	 */
	private void disableControls()
	{
		for (Control o : controlsForLock)
		{
			o.setEnabled(false);
		}
		saveButton.setEnabled(false);
	}

	/** 
	 * 校验控件数据 
	 * @return 返回一个布尔值，true为校验通过，false为校验失败
	 */
	private boolean checkDataisBlank()
	{
		// 检查必填的控件是否填入内容
		for (Control control : controlsForInput)
		{
			if (control instanceof Text)
			{
				if (((Text) control).getText().equals(""))
				{
					return false;
				}
			}
			if (control instanceof Combo)
			{
				if (((Combo) control).getText().equals(""))
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 显示提示信息
	 * @param msg
	 * @param style
	 */
	private void showMessage(String msg, int style)
	{
		MessageBox box = new MessageBox(getSite().getShell(), style);
		box.setMessage(msg);
		box.open();
	}

	/**
	 * 创建面板控件 
	 * @param parent
	 */
	private void createControls(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));
		Group searchDllGroup = new Group(container, SWT.NONE);
		searchDllGroup.setLayout(new GridLayout(7, false));
		searchDllGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		searchDllGroup.setText("交易查询");
		
		search = new Search();
		search.setUpProject(resourceLeafNode.getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(searchDllGroup);

		Group group_1 = new Group(container, SWT.NONE);
		group_1.setLayout(new GridLayout(8, false));
		group_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1,
				1));
		group_1.setText("交易");

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
		tradeIdText1.setEnabled(false);
		GridData gd_tradeIdText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeIdText1.widthHint = 70;
		tradeIdText1.setLayoutData(gd_tradeIdText1);

		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		Label tradeNameLabel1 = new Label(group_1, SWT.NONE);
		tradeNameLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeNameLabel1.setText("*交易名称");

		tradeNameText1 = new Text(group_1, SWT.BORDER);
		tradeNameText1.setEnabled(false);
		GridData gd_tradeNameText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeNameText1.widthHint = 70;
		tradeNameText1.setLayoutData(gd_tradeNameText1);
		
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);
		Label upServerLabel = new Label(group_1, SWT.NONE);
		upServerLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		upServerLabel.setText("*所属服务程序");

		upServerText = new Text(group_1, SWT.READ_ONLY | SWT.BORDER);
		
		upServerText.setEnabled(false);
		GridData gd_upServerCombo = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_upServerCombo.widthHint = 53;
		upServerText.setLayoutData(gd_upServerCombo);

		upServerBtn = new Button(group_1, SWT.NONE);
		upServerBtn.setEnabled(false);
		upServerBtn.setText("...");
		new Label(group_1, SWT.NONE);

		Label tradeModelLabel = new Label(group_1, SWT.NONE);
		tradeModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeModelLabel.setText("*交易类型");

		tradeModelCombo = new Combo(group_1, SWT.READ_ONLY);
		tradeModelCombo.setEnabled(false);
		GridData gd_tradeModelCombo = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tradeModelCombo.widthHint = 53;
		tradeModelCombo.setLayoutData(gd_tradeModelCombo);
		
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label tradeServerModelLabel = new Label(group_1, SWT.NONE);
		tradeServerModelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		tradeServerModelLabel.setText("*交易服务模式");

		tradeServerModelCombo = new Combo(group_1, SWT.READ_ONLY);
		tradeServerModelCombo.setEnabled(false);
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
		trancationFlagText.setEnabled(false);
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
		tradeExtNodesBtn.setEnabled(false);
		GridData gd_tradeExtNodesBtn = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_tradeExtNodesBtn.widthHint = 80;
		tradeExtNodesBtn.setLayoutData(gd_tradeExtNodesBtn);
		tradeExtNodesBtn.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
			}
		});
		tradeExtNodesBtn.setText("详情");
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label inputDataLabel = new Label(group_1, SWT.NONE);
		inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		inputDataLabel.setText("*输入数据");

		inputDataText = new Text(group_1, SWT.BORDER);
		inputDataText.setEnabled(false);
		inputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));
		

		inputSelBtn = new Button(group_1, SWT.NONE);
		inputSelBtn.setEnabled(false);
		GridData gd_inputSelBtn = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_inputSelBtn.widthHint = 80;
		inputSelBtn.setLayoutData(gd_inputSelBtn);
		inputSelBtn.setText("...");
		inputSelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						// TODO Auto-generated method stub
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						inputDataText.setText(s);
					}
				});
				queryAop.open();
			}
		});
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label outputDataLabel = new Label(group_1, SWT.NONE);
		outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		outputDataLabel.setText("*输出数据");

		outputDataText = new Text(group_1, SWT.BORDER);
		outputDataText.setEnabled(false);
		outputDataText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 4, 1));
		
		outputSelBtn = new Button(group_1, SWT.NONE);
		outputSelBtn.setEnabled(false);
		GridData gd_outputSelBtn = new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1);
		gd_outputSelBtn.widthHint = 80;
		outputSelBtn.setLayoutData(gd_outputSelBtn);
		outputSelBtn.setText("...");
		outputSelBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryDataItemDialog queryAop=new QueryDataItemDialog(e.display.getActiveShell(),e.getSource());
				queryAop.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						// TODO Auto-generated method stub
						java.util.List<String> l=((QueryDataItemDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						outputDataText.setText(s);
					}
				});
				queryAop.open();
			}
		});
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
		
		callServiceBtn = new Button(group_1, SWT.NONE);
		callServiceBtn.setEnabled(false);
		GridData gd_callServiceBtn = new GridData(SWT.CENTER, SWT.CENTER,
				false, false, 1, 1);
		gd_callServiceBtn.widthHint = 80;
		callServiceBtn.setLayoutData(gd_callServiceBtn);
		callServiceBtn.setText("...");
		callServiceBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				QueryTradeDialog queryTradeDialog=new QueryTradeDialog(e.display.getActiveShell(),e.getSource());
				queryTradeDialog.addInformDialogListener(new InformDialogListener() {
					
					@Override
					public void handleEvent(InformDialogEvent dm) {
						java.util.List<String> l=((QueryTradeDialog)dm.getdm()).getListForReturn();
						String s="";
						for (String string : l) {
							if(s.equals("")){
								s+=string;
							}else{
								s+="|"+string;
							}
						}
						callServiceText.setText(s);
					}
				});
				queryTradeDialog.open();
			}
			
		});
		new Label(group_1, SWT.NONE);
		new Label(group_1, SWT.NONE);

		Label preconditionLabel = new Label(group_1, SWT.NONE);
		preconditionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		preconditionLabel.setText("前置条件");

		preconditionText = new Text(group_1, SWT.BORDER | SWT.V_SCROLL);
		preconditionText.setEnabled(false);
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
		postconditionText.setEnabled(false);
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
		tradeDescText.setEnabled(false);
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

		unlockButton = new Button(group_1, SWT.NONE);
		unlockButton.setEnabled(false);
		GridData gd_unlockButton = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_unlockButton.widthHint = 80;
		unlockButton.setLayoutData(gd_unlockButton);
		unlockButton.setText("解锁");
		unlockButton.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseUp(MouseEvent e)
			{
				String name = ((Button) e.getSource()).getText();
				if (name.equals("解锁"))
				{
					enableControls();
					((Button) e.getSource()).setText("锁定");
				} else
				{
					disableControls();
					((Button) e.getSource()).setText("解锁");
				}
			}

		});

		saveButton = new Button(group_1, SWT.NONE);
		saveButton.setEnabled(false);
		GridData gd_saveButton = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1);
		gd_saveButton.widthHint = 80;
		saveButton.setLayoutData(gd_saveButton);
		saveButton.setText("保存");
		saveButton.addMouseListener(new MouseAdapter()
		{

			@Override
			public void mouseUp(MouseEvent e)
			{
				saveMethod();
			}

		});
		
		recoverBtn = new Button(group_1, SWT.NONE);
		GridData gd_recoverBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_recoverBtn.widthHint = 80;
		recoverBtn.setLayoutData(gd_recoverBtn);
		recoverBtn.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				setTradeData(recoverMap);
				setDirty(false);
				firePropertyChange(IEditorPart.PROP_DIRTY);
				saveButton.setEnabled(false);
			}
			
		});
		recoverBtn.setText("恢复");
		new Label(group_1, SWT.NONE);
	}

	/**
	 * 更新数据库的操作
	 * 需要更新的内容放置到一个list中传递给数据库操作对象
	 * @return
	 */
	private boolean updateDb()
	{
		List<String> dataToUp = new ArrayList<String>();
		/*
		 * tradeName=?,upServer=?,tradeModel=?," +
		 * "tradeServerModel=?,inputData=?,outputData=?," +
		 * "callServices=?,precondition=?,postcondition=?,tradeDesc=?"
		 */
		dataToUp.add(tradeNameText1.getText());
		dataToUp.add(upServerText.getText());
		dataToUp.add(tradeModelCombo.getText().substring(0, 1));
		dataToUp.add(tradeServerModelCombo.getText().substring(0, 1));
		dataToUp.add(inputDataText.getText());
		dataToUp.add(outputDataText.getText());
		//dataToUp.add(callServiceText.getText());
		dataToUp.add("");
		dataToUp.add(preconditionText.getText());
		dataToUp.add(postconditionText.getText());
		dataToUp.add(tradeDescText.getText());
		EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
		try
		{
			editorsDaoServiceImpl.updateTradeById(tradeIdText1.getText(),
					dataToUp, ps);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 设定编辑器对应导航窗口树中的节点为焦点
	 * @param rln 传入一ResourceLeafNode 对象
	 */
	private void setSelectionNode(ResourceLeafNode rln){
		NavView view=(NavView) getSite().getWorkbenchWindow().getActivePage().findView(NavView.ID);
		TreeViewer tv=view.getTreeViewer();
		StructuredSelection structuredSelection=new StructuredSelection(rln);
		tv.setSelection(structuredSelection,true);
	}
	/**
	 * 保存按钮和doSave()调用的保存方法，会先检查页面内容，
	 * 如果符合要求则更新数据库，并重置用于恢复的recoverMap，
	 * 设定页面为未更改，设定保存按钮状态不可用
	 * 如果不符合要求，则给用户提示
	 */
	private void saveMethod() {
		// 检查合法性 不合法给出提示 合法就继续
		if (checkDataisBlank()) {
			// 插入数据库做保存
			System.out.println("can save");
			// 调用更新数据库的方法
			updateDb();
			recoverMap = getTradeData(input);
			// 设置编辑器污染属性
			setDirty(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
			// 设置保存按钮可用
			saveButton.setEnabled(false);

		} else {
			System.out.println("can not save");
		}
	}
	//以下为实现ISearch接口中的方法	
	@Override
	public String getTargetId() {
		return tradeIdText1.getText();
	}

	@Override
	public String getTargetName() {
		return tradeNameText1.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		contentMap=map;
		recoverMap=map;
	}

	@Override
	public PreferenceStore getPs() {
		return ps;
	}

	@Override
	public void setUnLockButtonText(String text) {
		unlockButton.setText(text);
	}

	@Override
	public void mySave() {
		saveMethod();
	}

	@Override
	public void setThisNode(ResourceLeafNode node) {
		resourceLeafNode=node;
	}

	@Override
	public ResourceLeafNode getThisNode() {
		return  resourceLeafNode;
	}

	@Override
	public void setInputNode(ResourceLeafNode node) {
		// TODO Auto-generated method stub
		((TradeEditorInput)input).setSource(node);
	}

	@Override
	public int showMessage(int style, String title, String message) {
		MessageBox box = new MessageBox(getSite().getShell(),
				style);
		box.setText(title);
		box.setMessage(message);
		return box.open();
	}

	@Override
	public Search getSearch() {
		return search;
	}

	@Override
	public void setControlsText() {
		setTradeData(contentMap);
//		if (contentMap.get().equals("0-APP"))
//		{
//			unlockButton.setEnabled(true);
//		}
	}

	@Override
	public void setMyPartName(String name) {
		this.setPartName(name);
	}

	@Override
	public void setEnable(boolean b) {
		disableControls();
	}

	@Override
	public void setSelectNode(ResourceLeafNode node) {
		setSelectionNode(node);
	}

}
