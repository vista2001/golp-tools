package dev.editors.retCode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import dev.db.service.EditorRetcodeServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
/**
 * Retcode表编辑器类
 * <p>这个类继承了EditorPart类，与RetcodeEditorInput类一起完成Retcode编辑器的功能<br>
 * 在编辑器初始化的时候，通过RetcodeEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个
 * 编辑器的初始化。
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class RetCodeEditor extends EditorPart implements ISearch{
	public static final String ID="dev.editor.retcode.RetCodeEditor";	////编辑器类的标识
	private Text upProject1;											//查询部分所属工程文本框
	private Text searchText;											//搜索文本框
	private Text retCodeId;												//响应码标识文本框
	private Text retCodeValue;											//响应码值文本框
	private Text retCodeDesc;											//响应码说明文本框
	private Text upProject;												//表项部分所属工程文本框
	private Text retCodeLevel;											//响应码级别文本框
	private RetCodeInput input;											//inpiu对象
	private Button saveBtn;												//修改按钮
	private Button clearBtn;											//解锁按钮
	private Button restoreBtn;
	private Button retcodeValueButton;									//单选按钮（根据值）
	private Button retcodeIDButton;										//单选按钮（根据标识）
	private EditorRetcodeServiceImpl impl;								//数据库操作类对象
	private Map<String,String> map;										//存储查询到数据的Map
	private Map<String, String> restoreMap;
	private PreferenceStore ps;											//数据库配置信息
	private boolean bDirty=false;
	private Search search;
	public RetCodeEditor() {	
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		saveData();
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 用于对编辑器进行初始化<br>
	 * 根据site,input对象对编辑器进行初始化，打开编辑器的时候首先调用的方法
	 * @param site    编辑器的site
	 * @param input	    编辑器配套的input
	 * @return 没有返回值
	 * */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// TODO Auto-generated method stub
		this.setSite(site);							//设置site
		this.setInput(input);						//设置input
		this.setPartName("响应码"+input.getName());	//设置编辑器标题
		this.input=(RetCodeInput)input;				//对Input初始化
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return bDirty;
	}
	
	public void setDirty(boolean b){
		bDirty=b;
		saveBtn.setEnabled(b);
		if(b)
			firePropertyChange(PROP_DIRTY);
		else
			firePropertyChange(PROP_INPUT);
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 在编辑器上创建各种空间，设置布局，以及对空间进行初始化<br>
	 * 将需要的控件创建在parent里，所有控件的行为都在这个方法里被设定。这个方法在调用完Init后背调用
	 * @param parent    所有控件的parent.
	 * @return 没有返回值
	 * */
	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//创建编辑器里的控件
		Group retcodeSearchGroup = new Group(composite, SWT.NONE);
		retcodeSearchGroup.setText("响应码查询");
		retcodeSearchGroup.setLayout(new GridLayout(7, false));
		retcodeSearchGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(retcodeSearchGroup);
/*		Label upProjectLabel1 = new Label(retcodeSearchGroup, SWT.NONE);
		upProjectLabel1.setText("所属工程");
		
		upProject1 = new Text(retcodeSearchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		retcodeValueButton = new Button(retcodeSearchGroup, SWT.RADIO);
		GridData gd_retcodeValueButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_retcodeValueButton.widthHint = 80;
		retcodeValueButton.setLayoutData(gd_retcodeValueButton);
		retcodeValueButton.setText("响应码值");
		
		retcodeIDButton = new Button(retcodeSearchGroup, SWT.RADIO);
		retcodeIDButton.setSelection(true);
		GridData gd_retcodeIDButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_retcodeIDButton.widthHint = 80;
		retcodeIDButton.setLayoutData(gd_retcodeIDButton);
		retcodeIDButton.setText("响应码标识");
		
		searchText = new Text(retcodeSearchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		searchText.addVerifyListener(new VerifyListener() {
			//设置文本框输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=searchText.getText().length()<=32;
			}
		});
		
		Button Search = new Button(retcodeSearchGroup, SWT.NONE);
		GridData gd_Search = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_Search.widthHint = 80;
		Search.setLayoutData(gd_Search);
		Search.setText("查询");
		//设置查询按钮的行为
		Search.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	RetCodeEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					RetCodeEditor retcodeEditor = (RetCodeEditor)tmp.getEditor(false);
					if(retcodeEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(retcodeIDButton.getSelection())
						{
							if(retcodeEditor.retCodeId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(retcodeEditor);
								return;
							}
						}
						else
						{
							if(retcodeEditor.retCodeValue.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(retcodeEditor);
								return;
							}
						}
					}
				}
					//判断是根据标识查询还是根据名称查询
					if(retcodeValueButton.getSelection())
						try {
							//将查询的结果放入一个Map里
							map=impl.queryRetcodeByIdOrName("", searchText.getText(), ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					else
						try {
							map=impl.queryRetcodeByIdOrName(searchText.getText(),"", ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					restoreMap=map;
					//当查询到数据时，将数据填入编辑器对应的文本框里
					if(!map.isEmpty()){
						retCodeId.setText(map.get("retcodeid"));
						retCodeValue.setText(map.get("retcodevalue"));
						retCodeDesc.setText(map.get("retcodedesc"));
						
			//			 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
			//			否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
					
						if(map.get("retcodelevel").equals("0")){
							saveBtn.setVisible(true);
							clearBtn.setVisible(true);
							restoreBtn.setVisible(true);
							retCodeLevel.setText("APP");
						}
						else{
							retCodeLevel.setText("GOLP");
							saveBtn.setVisible(false);
							clearBtn.setVisible(false);
							restoreBtn.setVisible(false);
						}
						setPartName("响应码"+retCodeId.getText()+"所属工程"+upProject.getText());//修改编辑器标题
						getSite().getShell().setText("GOLP tool "+"响应码"+input.getName()+"所属工程"+upProject.getText());//修改工具标题
						input.setName(retCodeId.getText());//改变编辑器标识
						setDirty(false);
					}
					//查询不到数据，则弹出警告对话框
					else{
						MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
						box.setText("警告");
						box.setMessage("找不到查询的记录");
						box.open();
					}
				}
		});*/
		
		Group retcodeGroup = new Group(composite, SWT.NONE);
		GridData gd_retcodeGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_retcodeGroup.heightHint = 365;
		gd_retcodeGroup.widthHint = 575;
		retcodeGroup.setLayoutData(gd_retcodeGroup);
		retcodeGroup.setText("响应码表");
		retcodeGroup.setLayout(new GridLayout(8, false));
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(retcodeGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(retcodeGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("所属工程");
		
		upProject = new Text(retcodeGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(retcodeGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label retCodeLevelLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		retCodeLevelLabel.setText("数据项级别");
		
		retCodeLevel = new Text(retcodeGroup, SWT.BORDER);
		retCodeLevel.setEnabled(false);
		GridData gd_retCodeLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeLevel.widthHint = 70;
		retCodeLevel.setLayoutData(gd_retCodeLevel);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeIdLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeIdLabel.setText("响应码标识");
		
		retCodeId = new Text(retcodeGroup, SWT.BORDER | SWT.READ_ONLY);
		GridData gd_retCodeId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeId.widthHint = 70;
		retCodeId.setLayoutData(gd_retCodeId);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeValueLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		retCodeValueLabel.setText("响应码值");
		
		retCodeValue = new Text(retcodeGroup, SWT.BORDER);
		retCodeValue.setEnabled(false);
		retCodeValue.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		GridData gd_retCodeValue = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_retCodeValue.widthHint = 70;
		retCodeValue.setLayoutData(gd_retCodeValue);
		//设置文本框的输入限制
		retCodeValue.addVerifyListener(new VerifyListener() { 
		      public void verifyText(VerifyEvent event) { 
		          event.doit=event.text.length()<=5&&event.text.matches("^[0-9]+$");
		      }
		});
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		Label retCodeDescLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeDescLabel.setText("响应码说明");
		
		retCodeDesc = new Text(retcodeGroup, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		retCodeDesc.setEnabled(false);
		GridData gd_retCodeDesc = new GridData(SWT.FILL, SWT.CENTER, false, false, 5, 3);
		gd_retCodeDesc.heightHint = 83;
		retCodeDesc.setLayoutData(gd_retCodeDesc);
		//设置文本框的输入限制
		retCodeDesc.addVerifyListener(new VerifyListener() { 
		      public void verifyText(VerifyEvent event) { 
		    	  if(event.character!=8)
		          event.doit=retCodeDesc.getText().length()<=32;
		      }
		});
		retCodeDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);
		
		clearBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("编辑");
		/*
		 * 为“解锁”按钮设置行为，当按下按钮时，若按钮为“解锁”，则改为锁定，并将可以
		 * 编辑的控件设置为可用，将“修改”按钮设置为可用；否则改为解锁，将可以编辑的
		 * 控件设置为不可用，将修改按钮设置为不可用。
		 */
		clearBtn.addSelectionListener(new  SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="编辑"){
				nButton.setText("锁定");
				retCodeValue.setEnabled(true);
				retCodeDesc.setEnabled(true);
				saveBtn.setEnabled(true);
			}
				else{
					nButton.setText("编辑");
					retCodeValue.setEnabled(false);
					retCodeDesc.setEnabled(false);
					saveBtn.setEnabled(false);
				}
			}
		});
		new Label(retcodeGroup, SWT.NONE);
		
		saveBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_savaBtn);
		saveBtn.setText("修改");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				saveData();
			}
		});
		/*
		 * 为“修改”按钮设置行为，按下修改按钮，若响应码值文本框为空，则弹出警告对话框，
		 * 否则将可编辑的文本框里的数据放入一个List中调用EditorretCodeServiceImpl类的
		 * updateretCodeById方法将List里的数据替换标识为AopId
		 * 的数据。
		 * */
		
		restoreBtn = new Button(retcodeGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("恢复");
		//设置恢复按钮行为
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				retCodeValue.setText(restoreMap.get("name"));
				retCodeDesc.setText(restoreMap.get("retcodedesc"));
				setDirty(false);
			}
		});
		new Label(retcodeGroup, SWT.NONE);
		// TODO Auto-generated method stub
		try {
			datainit(input.getName());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * 当编辑器选中的时候调用<br>
	 * 当编辑器的窗口被选中时会自动调用这个方法，执行方法中的语句
	 * @return 没有返回值
	 * */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		getSite().getShell().setText("GOLP tool "+"响应码"+input.getName()+"所属工程"+upProject.getText());
		setPartName("动态库"+input.getName()+"所属工程"+upProject.getText());
		setSelectNode(input.getSource());
	}
	/**
	 * 对编辑器的控件进行初始化<br>
	 * 首先找到数据库的配置信息，然后根据name与配置信息调用EditorAopServiceImpl类
	 * 的queryAopByIdOrName方法得到初始化的数据放入Map，再一次填入控件中。
	 * @param name   要查询的数据的标识
	 * @return 没有返回值
	 * */
	private void datainit(String name) throws SQLException{
		//获得数据库的配置信息
		ResourceLeafNode rln = ((RetCodeInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'+ prjId + ".properties";
		System.out.println("dbfiles==="+dbfiles);
		//对数据库进行查询，放入Map
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		impl=new EditorRetcodeServiceImpl();
		map=impl.queryRetcodeByIdOrName(name, "", ps);
		restoreMap=map;
		//依次给控件赋值
		retCodeId.setText(map.get("ID"));
		retCodeValue.setText(map.get("NAME"));
		retCodeDesc.setText(map.get("retcodedesc"));
		//若aoplevel为零则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if(map.get("retcodelevel").equals("0"))
			retCodeLevel.setText("APP");
		else{
			retCodeLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		upProject.setText(input.getSource().getRootProject().getId());
//		upProject1.setText(upProject.getText());
		setDirty(false);
	} 
	/**
	 * 保存数据的方法
	 * 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
	 * 否则将可编辑的文本框里的数据放入一个List中调用EditorRetcodeServiceImpl类的updateRetcodeById方法将
	 * List里的数据替换标识为RetcodeId的数据。
	 * @throws SQLException
	 */
	private void saveData(){
		if(retCodeValue.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("警告");
			box.setMessage("数据项名称不能为空");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(retCodeValue.getText());
			datalist.add(retCodeDesc.getText());
			try {
				impl.updateRetcodeById(input.getName(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				restoreMap=impl.queryRetcodeByIdOrName(retCodeId.getText(), "", ps);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setDirty(false);
			try {
				restoreMap=impl.queryRetcodeByIdOrName(retCodeId.getText(), "", ps);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setSelectNode(ResourceLeafNode node)
	{
		IViewPart view = getSite().getWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		if (view != null)
		{
			NavView v = (NavView) view;
			TreeViewer tv = v.getTreeViewer();
			StructuredSelection s = new StructuredSelection(node);
			tv.setSelection(s, true);
		}
	}

	@Override
	public String getTargetId() {
		// TODO Auto-generated method stub
		return retCodeId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return retCodeValue.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		this.map=map;
		this.restoreMap=map;
	}

	@Override
	public PreferenceStore getPs() {
		// TODO Auto-generated method stub
		return ps;
	}

	@Override
	public void setUnLockButtonText(String text) {
		// TODO Auto-generated method stub
		clearBtn.setText(text);
	}

	@Override
	public void setThisNode(ResourceLeafNode node) {
		// TODO Auto-generated method stub
		input.setSource(node);
	}

	@Override
	public ResourceLeafNode getThisNode() {
		// TODO Auto-generated method stub
		return input.getSource();
	}

	@Override
	public void setInputNode(ResourceLeafNode node) {
		// TODO Auto-generated method stub
		input.setSource(node);
	}

	@Override
	public int showMessage(int style, String title, String message) {
		// TODO Auto-generated method stub
		MessageBox box=new MessageBox(getSite().getShell(), style);
		box.setText(title);
		box.setMessage(message);
		box.open();
		return 1;
	}

	@Override
	public Search getSearch() {
		// TODO Auto-generated method stub
		return search;
	}

	@Override
	public void setControlsText() {
		// TODO Auto-generated method stub
		retCodeId.setText(map.get("ID"));
		retCodeValue.setText(map.get("NAME"));
		retCodeDesc.setText(map.get("retcodedesc"));
		/*
		 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
		*/
		if(map.get("retcodelevel").equals("0")){
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
			retCodeLevel.setText("APP");
		}
		else{
			retCodeLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		input.setName(retCodeId.getText());
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName(name);
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b)
		{
			clearBtn.setText("编辑");
			retCodeValue.setEnabled(false);
			retCodeDesc.setEnabled(false);
		}
		else{
			clearBtn.setText("锁定");
			retCodeValue.setEnabled(true);
			retCodeDesc.setEnabled(true);
		}
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		saveData();
	}
}
