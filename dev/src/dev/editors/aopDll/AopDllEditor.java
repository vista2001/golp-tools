package dev.editors.aopDll;

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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
import dev.db.service.EditorAopDllServiceImpl;
/**
 * AopDll表编辑器类
 * <p>这个类继承了EditorPart类，与AopDllEditorInput类一起完成AopDll编辑器的功能<br>
 * 在编辑器初始化的时候，通过AopDllEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个
 * 编辑器的初始化。
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class AopDllEditor extends EditorPart implements ISearch{
	public AopDllEditor() {
	}

	public static final String ID="dev.editor.AopDll.AopDllEditor"; //编辑器类的标识
	//private Text upProject1;										//查询部分所属工程文本框
	//private Text searchText;										//搜索文本框
	private Text upProject;											//表项部分所属工程文本框
	private Text dllLevel;											//原子交易库级别文本框
	private Text dllId;												//原子交易库标识文本框	
	private Text dllType;											//原子交易库类型文本框	
	private Text dllName;											//原子交易库名称文本框
	private Text dllDesc;											//原子交易库说明文本框
	private Button saveBtn;											//修改按钮
	private Button clearBtn;										//解锁按钮
	private Button restoreBtn;										//恢复按钮
	//private Button dllIDButton;										//单选按钮（根据标识）
	//private Button dllNameButton;									//单选按钮（根据名称）
	private AopDllEditorInput input;								//Input对象
	private EditorAopDllServiceImpl impl;							//数据库操作类对象
	private PreferenceStore ps;										//数据库配置信息
	private Map<String, String> map;								//存储查询的数据的Map
	private Map<String, String> restoreMap;							//恢复按钮的数据的Map
	private boolean bDirty=false;									//是否修改的标识
	private Search search;
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		try {
			saveDate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		this.setSite(site);							//设置Site
		this.setInput(input);						//设置Input
		this.setPartName("动态库"+input.getName());	//设置编辑器标题
		this.input=(AopDllEditorInput)input;		//对Input初始化
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
		Group searchDllGroup = new Group(composite, SWT.NONE);
		searchDllGroup.setText("动态库查询");
		searchDllGroup.setLayout(new GridLayout(7, false));
		searchDllGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(searchDllGroup);

/*		
		Label upProjectLabel1 = new Label(searchDllGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("所属工程");
		
		upProject1 = new Text(searchDllGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		dllNameButton = new Button(searchDllGroup, SWT.RADIO);
		dllNameButton.setText("动态库名称");
		
		dllIDButton = new Button(searchDllGroup, SWT.RADIO);
		dllIDButton.setSelection(true);
		dllIDButton.setText("动态库标识");
		
		searchText = new Text(searchDllGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		
		Button searchBtn = new Button(searchDllGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("查询");
		//设置查询按钮的行为
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	AopDllEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					AopDllEditor dllEditor = (AopDllEditor)tmp.getEditor(false);
					if(dllEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(dllIDButton.getSelection())
						{
							if(dllEditor.dllId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dllEditor);
								return;
							}
						}
						else
						{
							if(dllEditor.dllName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dllEditor);
								return;
							}
						}
					}
				}
					//判断是根据标识查询还是根据名称查询
					try {
						//将查询的结果放入一个Map里
						if(dllNameButton.getSelection())
							map=impl.queryAopDllByIdOrName("", searchText.getText(), ps);
						else
							map=impl.queryAopDllByIdOrName(searchText.getText(),"", ps);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					//当查询到数据时，将数据填入编辑器对应的文本框里
					if(!map.isEmpty()){
						dllId.setText(map.get("dllid"));
						dllDesc.setText(map.get("dlldesc"));
						dllName.setText(map.get("dllname"));
						dllType.setText(map.get("dlltype"));
						
					//	 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
					//	否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
						
						if(map.get("dlllevel").equals("0")){
							dllLevel.setText("APP");
							saveBtn.setVisible(true);
							clearBtn.setVisible(true);
							restoreBtn.setVisible(true);
						}
						else{
							dllLevel.setText("GOLP");
							saveBtn.setVisible(false);
							clearBtn.setVisible(false);
							restoreBtn.setVisible(false);
						}
						setPartName("动态库"+searchText.getText()+"所属工程"+upProject.getText());				  //修改编辑器标题
						getSite().getShell().setText("GOLP tool "+"动态库"+dllId+"所属工程"+upProject.getText());//修改工具标题
						input.setName(dllId.getText());							  //改变编辑器标识
						setDirty(false);
						restoreMap=map;
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
		Group dllGroup = new Group(composite, SWT.NONE);
		GridData gd_dllGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_dllGroup.heightHint = 377;
		gd_dllGroup.widthHint = 545;
		dllGroup.setLayoutData(gd_dllGroup);
		dllGroup.setLayout(new GridLayout(8, false));
		dllGroup.setText("原子交易库");
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(dllGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(dllGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("所属工程");
		
		upProject = new Text(dllGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dllGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dllLevelLabel = new Label(dllGroup, SWT.NONE);
		dllLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllLevelLabel.setText("数据项级别");
		
		dllLevel = new Text(dllGroup, SWT.BORDER);
		dllLevel.setEnabled(false);
		GridData gd_dllLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllLevel.widthHint = 70;
		dllLevel.setLayoutData(gd_dllLevel);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label dllIdLabel = new Label(dllGroup, SWT.NONE);
		dllIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllIdLabel.setText("动态库标识");
		
		dllId = new Text(dllGroup, SWT.BORDER);
		dllId.setEnabled(false);
		GridData gd_dllId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllId.widthHint = 70;
		dllId.setLayoutData(gd_dllId);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label dllTypeLabel = new Label(dllGroup, SWT.NONE);
		dllTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllTypeLabel.setText("动态库类型");
		
		dllType = new Text(dllGroup, SWT.BORDER);
		dllType.setEnabled(false);
		GridData gd_dllType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllType.widthHint = 70;
		dllType.setLayoutData(gd_dllType);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label dllNameLabel = new Label(dllGroup, SWT.NONE);
		dllNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dllNameLabel.setText("动态库名称");
		
		dllName = new Text(dllGroup, SWT.BORDER);
		dllName.setEnabled(false);
		GridData gd_dllName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dllName.widthHint = 70;
		dllName.setLayoutData(gd_dllName);
		dllName.addVerifyListener(new VerifyListener() {
			//设置文本框的输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dllName.getText().length()<=32;
			}
		});
		dllName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		Label dllDescLabel = new Label(dllGroup, SWT.NONE);
		dllDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		dllDescLabel.setText("动态库说明");
		
		dllDesc = new Text(dllGroup, SWT.BORDER | SWT.V_SCROLL);
		dllDesc.setEnabled(false);
		GridData gd_dllDesc = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
		gd_dllDesc.widthHint = 81;
		gd_dllDesc.heightHint = 29;
		dllDesc.setLayoutData(gd_dllDesc);
		dllDesc.addVerifyListener(new VerifyListener() {
			//设置文本框的输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dllName.getText().length()<=128;
			}
		});
		dllDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		new Label(dllGroup, SWT.NONE);
		
		clearBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("编辑");
		/*
		 * 为“解锁”按钮设置行为，当按下按钮时，若按钮为“解锁”，则改为锁定，并将可以
		 * 编辑的控件设置为可用，将“修改”按钮设置为可用；否则改为解锁，将可以编辑的
		 * 控件设置为不可用，将修改按钮设置为不可用。
		 */
		clearBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="编辑"){
					nButton.setText("锁定");
					dllName.setEnabled(true);
					dllDesc.setEnabled(true);
				}
				else{
					nButton.setText("编辑");
					dllName.setEnabled(false);
					dllDesc.setEnabled(false);
				}
				
			}
		});
		new Label(dllGroup, SWT.NONE);
		
		saveBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_saveBtn = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_saveBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_saveBtn);
		saveBtn.setText("修改");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				try {
					saveDate();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		/*
		 * 为“修改”按钮设置行为，按下修改按钮，若动态库名称文本框为空，则弹出警告对话框，
		 * 否则将可编辑的文本框里的数据放入一个List中调用EditorAopDllServiceImpl类的
		 * updateAopDllById方法将List里的数据替换标识为AopId
		 * 的数据。
		 * */
		
		restoreBtn = new Button(dllGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("恢复");
		//设置恢复按钮行为
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				dllName.setText(restoreMap.get("name"));
				dllDesc.setText(restoreMap.get("dlldesc"));
				setDirty(false);
			}
		});
		new Label(dllGroup, SWT.NONE);
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
		getSite().getShell().setText("GOLP tool "+"动态库"+input.getName()+"所属工程"+upProject.getText());//设置工具的名称
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
		ResourceLeafNode rln = ((AopDllEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + '/'+ prjId + ".properties";
		System.out.println("dbfiles==="+dbfiles);
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//对数据库进行查询，放入Map
		impl=new EditorAopDllServiceImpl();
		map=impl.queryAopDllByIdOrName(name, "", ps);
		restoreMap=map;
		//依次给控件赋值
		dllId.setText(map.get("id"));
		dllDesc.setText(map.get("dlldesc"));
		dllName.setText(map.get("name"));
		dllType.setText(map.get("dlltype"));
		//若aoplevel为零则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if(map.get("dlllevel").equals("0"))
			dllLevel.setText("APP");
		else{
			dllLevel.setText("GOLP");
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
	 * 否则将可编辑的文本框里的数据放入一个List中调用EditorDllServiceImpl类的updateDllById方法将
	 * List里的数据替换标识为DllId的数据。
	 * @throws SQLException
	 */
	private void saveDate() throws SQLException
	{
		if(dllName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("警告");
			box.setMessage("数据项名称不能为空");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(dllName.getText());
			datalist.add(dllDesc.getText());
			try {
				impl.updateAopDllById(dllId.getText(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		restoreMap=impl.queryAopDllByIdOrName(dllId.getText(), "", ps);
		setDirty(false);
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
		return dllId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return dllName.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		this.map=map;
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
		dllId.setText(map.get("id"));
		dllDesc.setText(map.get("dlldesc"));
		dllName.setText(map.get("name"));
		dllType.setText(map.get("dlltype"));
		/*
		 判断dlllevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
		*/
		if(map.get("dlllevel").equals("0")){
			dllLevel.setText("APP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
		}
		else{
			dllLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		input.setName(dllId.getText());
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("动态库"+dllId.getText()+"所属工程"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(b){
			clearBtn.setText("锁定");
			dllName.setEnabled(true);
			dllDesc.setEnabled(true);
		}
		else{
			clearBtn.setText("编辑");
			dllName.setEnabled(false);
			dllDesc.setEnabled(false);
		}
		setDirty(false);
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		try {
			saveDate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
