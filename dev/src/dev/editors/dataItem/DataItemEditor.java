package dev.editors.dataItem;

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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

import dev.db.service.EditorDataitemServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
/**
 * Dataitem表编辑器类
 * <p>这个类继承了EditorPart类，与DataitemEditorInput类一起完成Dataitem编辑器的功能<br>
 * 在编辑器初始化的时候，通过DataitemEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个
 * 编辑器的初始化。
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class DataItemEditor extends EditorPart implements ISearch {
	public DataItemEditor() {
	}
	
	public static final String ID="dev.editor.DataItem.DataItem";	//编辑器类的标识
	public DataItemInput input;									 	//Input类对象
	private Text upProject1;										//查询部分所属工程文本框
	private Text searchText;										//搜索文本框
	private Text upProject;											//表项部分所属工程文本框
	private Text dataItemId;										//数据项标识文本框
	private Text FMLID;												//FML编号文本框
	private Text dataItemDesc;										//数据项说明文本框
	private Text dataItemLevel;										//数据项级别文本框
	private Text dataItemName;										//数据项名称文本框
	private Text dataItemLen;										//数据项类型长度文本框
	private Text dataItemAop;										//数据项AOP文本框
	private Combo dataItemType;										//数据项类型下拉菜单
	private Button saveBtn;											//修改按钮
	private Button clearBtn;										//解锁按钮
	private Button dataitemNameButton;								//单选按钮（根据名称）
	private Button dataitemIDButton;								//单选按钮（根据标识）
	private PreferenceStore ps;										//数据库配置信息
	private EditorDataitemServiceImpl impl;							//数据库操作类对象
	private Map< String, String> map;								//存储查询的数据的Map
	private Map<String, String> restoreMap;
	private String[] dataItemTypeItem={"1-short","2-long","3-float","4-double","5-char","6-String"};
	private Button restoreBtn;
	private boolean bDirty;
	private Search search;
	//数据项类型下拉菜单选项字符串数组
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		try {
			saveData();
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
		this.setSite(site);								//设置Site
		this.setInput(input);							//设置Input
		this.setPartName("数据项"+input.getName());		//设置编辑器标题
		this.input=(DataItemInput)input;				//对Input初始化
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
		Group dataItemSearchGroup = new Group(composite, SWT.NONE);
		dataItemSearchGroup.setText("数据项查询");
		dataItemSearchGroup.setLayout(new GridLayout(7, false));
		dataItemSearchGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(dataItemSearchGroup);
/*		Label upProjectLabel1 = new Label(dataItemSearchGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("所属工程");
		
		upProject1 = new Text(dataItemSearchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		dataitemNameButton = new Button(dataItemSearchGroup, SWT.RADIO);
		GridData gd_dataitemNameButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dataitemNameButton.widthHint = 80;
		dataitemNameButton.setLayoutData(gd_dataitemNameButton);
		dataitemNameButton.setText("数据项名称");
		
		dataitemIDButton = new Button(dataItemSearchGroup, SWT.RADIO);
		dataitemIDButton.setSelection(true);
		GridData gd_dataitemIDButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_dataitemIDButton.widthHint = 80;
		dataitemIDButton.setLayoutData(gd_dataitemIDButton);
		dataitemIDButton.setText("数据项标识");
		
		searchText = new Text(dataItemSearchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
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
		
		Button searchBtn = new Button(dataItemSearchGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("查询");
		//设置查询按钮的行为
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				//super.widgetSelected(e);
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	DataItem.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					DataItem dataEditor = (DataItem)tmp.getEditor(false);
					if(dataEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(dataitemIDButton.getSelection())
						{
							if(dataEditor.dataItemId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dataEditor);
								return;
							}
						}
						else
						{
							if(dataEditor.dataItemName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(dataEditor);
								return;
							}
						}
					}
				}
				try {
				//判断是根据标识查询还是根据名称查询
				if(dataitemNameButton.getSelection())
					//将查询的结果放入一个Map里
						map=impl.queryDataitemByIdOrName("", searchText.getText(), ps);
				else
						map=impl.queryDataitemByIdOrName(searchText.getText(), "", ps);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				restoreMap=map;
				//当查询到数据时，将数据填入编辑器对应的文本框里
				if(!map.isEmpty()){
					dataItemId.setText(map.get("dataitemid"));
					dataItemName.setText(map.get("dataname"));
					dataItemDesc.setText(map.get("datadesc"));
					
			//		 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
			//		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
					
					if(map.get("datalvl").equals("1")){
						dataItemLevel.setText("GOLP");
						saveBtn.setVisible(false);
						clearBtn.setVisible(false);
						restoreBtn.setVisible(false);
						dataItemName.setEnabled(false);
						dataItemLen.setEnabled(false);
						dataItemType.setEnabled(false);
						dataItemDesc.setEnabled(false);
						dataItemAop.setEnabled(false);
					}
					else
					{
						dataItemLevel.setText("APP");
						saveBtn.setVisible(true);
						clearBtn.setVisible(true);
						restoreBtn.setVisible(true);
					}
				
					dataItemType.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))-1]);
					dataItemLen.setText(map.get("datalen"));
					dataItemAop.setText(map.get("dataaop"));
					FMLID.setText(map.get("fmlid"));
					setPartName("数据项"+dataItemId.getText()+"所属工程"+upProject.getText());//修改编辑器标题
					getSite().getShell().setText("GOLP tool "+"数据项"+dataItemId.getText()+"所属工程"+upProject.getText());//修改工具标题
					input.setName(dataItemId.getText());//改变编辑器标识
				}
				//查询不到数据，则弹出警告对话框
				else{
					MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
					box.setText("警告");
					box.setMessage("找不到查询的记录");
					box.open();
				}	
			}
		});
		*/
		Group dataItemGroup = new Group(composite, SWT.NONE);
		dataItemGroup.setLayout(new GridLayout(8, false));
		GridData gd_dataItemGroup = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
		gd_dataItemGroup.heightHint = 351;
		gd_dataItemGroup.widthHint = 584;
		dataItemGroup.setLayoutData(gd_dataItemGroup);
		dataItemGroup.setText("数据项表");
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(dataItemGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("所属工程");
		
		upProject = new Text(dataItemGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dataItemLevelLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLevelLabel.setText("数据项级别");
		
		dataItemLevel = new Text(dataItemGroup, SWT.BORDER);
		dataItemLevel.setEnabled(false);
		GridData gd_dataItemLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLevel.widthHint = 70;
		dataItemLevel.setLayoutData(gd_dataItemLevel);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemIdLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemIdLabel.setText("数据项标识");
		
		dataItemId = new Text(dataItemGroup, SWT.BORDER);
		dataItemId.setEnabled(false);
		GridData gd_dataItemId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemId.widthHint = 70;
		dataItemId.setLayoutData(gd_dataItemId);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemNameLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemNameLabel.setText("数据项名称");
		
		dataItemName = new Text(dataItemGroup, SWT.BORDER);
		dataItemName.setEnabled(false);
		GridData gd_dataItemName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemName.widthHint = 70;
		dataItemName.setLayoutData(gd_dataItemName);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		dataItemName.addVerifyListener(new VerifyListener() {
			//设置文本框输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=dataItemName.getText().length()<=32;
			}
		});
		dataItemName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemTypeLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemTypeLabel.setText("数据项类型");
		
		dataItemType = new Combo(dataItemGroup, SWT.READ_ONLY);
		dataItemType.setEnabled(false);
		GridData gd_dataItemType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemType.widthHint = 53;
		dataItemType.setLayoutData(gd_dataItemType);
		dataItemType.setItems(dataItemTypeItem);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		//设置下拉菜单行为
		dataItemType.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				//当菜单项是“5-char”或者“6-String”时，数据项长度变为可编辑
				if(((Combo)e.getSource()).getText().equals("5-char")||((Combo)e.getSource()).getText().equals("6-String"))
					dataItemLen.setEnabled(true);
				//否则，不可编辑，长度设置为0
				else{
					dataItemLen.setEnabled(false);
					dataItemLen.setText("0");
				}
			}
		});
		dataItemType.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemLenLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLenLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLenLabel.setText("数据项长度");
		
		dataItemLen = new Text(dataItemGroup, SWT.BORDER);
		dataItemLen.setEnabled(false);
		GridData gd_dataItemLen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLen.widthHint = 70;
		dataItemLen.setLayoutData(gd_dataItemLen);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		dataItemLen.addVerifyListener(new VerifyListener() {
			//设置文本框的输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=e.text.length()<=5&&e.text.matches("^[0-9]+$");
			}
		});
		dataItemLen.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label FMLIDLabel = new Label(dataItemGroup, SWT.NONE);
		FMLIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		FMLIDLabel.setText("FML标识");
		
		FMLID = new Text(dataItemGroup, SWT.BORDER);
		FMLID.setEnabled(false);
		GridData gd_FMLID = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_FMLID.widthHint = 70;
		FMLID.setLayoutData(gd_FMLID);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemAopLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemAopLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemAopLabel.setText("数据项检查函数");
		
		dataItemAop = new Text(dataItemGroup, SWT.BORDER);
		dataItemAop.setEnabled(false);
		GridData gd_dataItemAop = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemAop.widthHint = 70;
		dataItemAop.setLayoutData(gd_dataItemAop);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemDescLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemDescLabel.setText("数据项说明");
		
		dataItemDesc = new Text(dataItemGroup, SWT.BORDER | SWT.V_SCROLL);
		dataItemDesc.setEnabled(false);
		dataItemDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		dataItemDesc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3));
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		clearBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("解锁");
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
					dataItemName.setEnabled(true);
					dataItemDesc.setEnabled(true);
					dataItemType.setEnabled(true);
					if(dataItemType.getText().equals(dataItemTypeItem[4])||dataItemType.getText().equals(dataItemTypeItem[5]))
					{
						dataItemLen.setEnabled(true);
					}
					saveBtn.setEnabled(true);
				}
				else{
					nButton.setText("编辑");
					dataItemName.setEnabled(false);
					dataItemDesc.setEnabled(false);
					dataItemType.setEnabled(false);
					dataItemLen.setEnabled(false);
					saveBtn.setEnabled(false);
				}
				
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		
		saveBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_savaBtn);
		saveBtn.setText("修改");
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				try {
					saveData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		restoreBtn = new Button(dataItemGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreBtn.setLayoutData(gd_restoreBtn);
		restoreBtn.setText("恢复");
		//设置恢复按钮行为
		restoreBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				dataItemName.setText(restoreMap.get("name"));
				dataItemDesc.setText(restoreMap.get("datadesc"));
				dataItemType.setText(dataItemTypeItem[new Integer(restoreMap.get("datatype"))-1]);
				dataItemLen.setText(restoreMap.get("datalen"));
				setDirty(false);
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		/*
		 * 为“修改”按钮设置行为，按下修改按钮，若数据项名称文本框为空，则弹出警告对话框，
		 * 否则将可编辑的文本框里的数据放入一个List中调用EditordataItemServiceImpl类的
		 * updatedataItemById方法将List里的数据替换标识为AopId
		 * 的数据。
		 * */
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
		getSite().getShell().setText("GOLP tool "+"数据项"+input.getName()+"所属工程"+upProject.getText());//设置工具的名称
		setPartName("数据项"+input.getName()+"所属工程"+upProject.getText());
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
		ResourceLeafNode rln = ((DataItemInput) input).getSource();
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
		impl=new EditorDataitemServiceImpl();
		map=impl.queryDataitemByIdOrName(name, "", ps);
		restoreMap=map;
		//依次给控件赋值
		dataItemId.setText(map.get("ID"));
		dataItemName.setText(map.get("NAME"));
		dataItemDesc.setText(map.get("datadesc"));
		//若aoplevel为零则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if(map.get("datalvl").equals("0")){
			dataItemLevel.setText("APP");
		}
		else
		{
			dataItemLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
		}
		dataItemType.setText(dataItemTypeItem[new Integer(map.get("datatype"))-1]);
		dataItemLen.setText(map.get("datalen"));
		dataItemAop.setText(map.get("dataaop"));
		FMLID.setText(map.get("fmlid"));
		upProject.setText(input.getSource().getRootProject().getId());
//		upProject1.setText(upProject.getText());
		setDirty(false);
	}
	/**
	 * 保存数据的方法
	 * 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
	 * 否则将可编辑的文本框里的数据放入一个List中调用EditorDataitemServiceImpl类的updateDataitemById方法将
	 * List里的数据替换标识为dataitemId的数据。
	 * @throws SQLException
	 */
	private void saveData() throws SQLException{
		if(dataItemName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("警告");
			box.setMessage("数据项名称不能为空");
			box.open();
		}
		else{
		List<String> datalist=new ArrayList<String>();
		datalist.add(dataItemName.getText());
		datalist.add(dataItemDesc.getText());
		datalist.add(dataItemType.getText().substring(0, 1));
		datalist.add(dataItemLen.getText());
		datalist.add(dataItemAop.getText());
		try {
			impl.updateDataitemById(dataItemId.getText(), datalist, ps);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		restoreMap=impl.queryDataitemByIdOrName(dataItemId.getText(), "", ps);
		setDirty(false);
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
		return dataItemId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return dataItemName.getText();
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
		dataItemId.setText(map.get("ID"));
		dataItemName.setText(map.get("NAME"));
		dataItemDesc.setText(map.get("datadesc"));
		/*
		 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
		*/
		if(map.get("datalvl").equals("1")){
			dataItemLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreBtn.setVisible(false);
			dataItemName.setEnabled(false);
			dataItemLen.setEnabled(false);
			dataItemType.setEnabled(false);
			dataItemDesc.setEnabled(false);
			dataItemAop.setEnabled(false);
		}
		else
		{
			dataItemLevel.setText("APP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreBtn.setVisible(true);
		}
	
		dataItemType.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))-1]);
		dataItemLen.setText(map.get("datalen"));
		dataItemAop.setText(map.get("dataaop"));
		FMLID.setText(map.get("fmlid"));
		input.setName(dataItemId.getText());
		setDirty(false);
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("原子交易"+dataItemId.getText()+"所属工程"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b){
			clearBtn.setText("编辑");
			dataItemName.setEnabled(false);
			dataItemDesc.setEnabled(false);
			dataItemType.setEnabled(false);
			dataItemLen.setEnabled(false);
			saveBtn.setEnabled(false);
		}
		else{
			clearBtn.setText("锁定");
			dataItemName.setEnabled(true);
			dataItemDesc.setEnabled(true);
			dataItemType.setEnabled(true);
			dataItemLen.setEnabled(true);
			saveBtn.setEnabled(true);
		}
	}

	@Override
	public void mySave() {
		// TODO Auto-generated method stub
		try {
			saveData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
