package dev.editors.aop;

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

import dev.db.service.EditorAopServiceImpl;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
/**
 * Aop表编辑器类
 * <p>这个类继承了EditorPart类，与AopEditorInput类一起完成Aop编辑器的功能<br>
 * 在编辑器初始化的时候，通过AopEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个
 * 编辑器的初始化。
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#addText
 * @see#doSave
 * @see#setFocus
 * @see#lock
 * @see#unlock
 * @see#saveData
 * */
public class AopEditor extends EditorPart implements ISearch {
	public static final String ID="dev.editor.Aop.AopEditor";//编辑器类的标识
	//private Text upProject1;								 //查询部分所属工程文本框
	//private Text searchText;								 //搜索文本框
	private Text upProject;									 //表项部分所属工程文本框
	private Text AopLevel;                                   //原子交易级别文本框
	private Text upDll;                                      //所属动态库文本框
	private Text AopId;                                      //原子交易标识文本框
	private Text AopName;                                    //原子交易名称文本框
	private Text AopExts;                                    //扩展点列表文本框
	private Text AopDesc;                                    //原子交易说明文本框
	private Button saveBtn;                                  //修改按钮
	private Button clearBtn;                                 //解锁按钮
	//private Button aopNameButton;                            //单选按钮（名称）
	//private Button aopIDButton;                              //单选按钮（标识）
	private Button restoreButton;							 //恢复按钮
	private PreferenceStore ps;                              //数据库配置信息
	private EditorAopServiceImpl impl;                       //数据库操作类对象
	private Map<String, String> map;                         //存储查询到数据的Map
	private Map<String, String> restoreMap;					 //用于恢复的数据的Map
	String[] ErrRecoverItem={"1-first","2-second","3-third"};//下拉菜单选项字符串数组
	private Combo AopErrRecover;                             //下拉菜单
	private List<Text> list=new ArrayList<Text>();           //可编辑控件列表
	private AopEditorInput input;                            //Input类对象
	private Text inputData;                                  //输入数据项文本框
	private Text outputData;                                 //输出数据项文本框
	private Text preCondition;                               //前置条件文本框
	private Text postCondition;                              //后置条件文本框
	private boolean bDirty=false;							 //是否修改判断
	private Search search;
	public AopEditor() {
	}

	/**
	 * 用于在关闭编辑器时保存编辑器的内容<br>
	 * 当isDirty为true时，关闭编辑器会提示是否保存修改，如果选择"是"，则会执行saveData方法。
	 * @return 没有返回值
	 */
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
		this.setPartName("原子交易"+input.getName()); 	//设置编辑器标题
		this.input=(AopEditorInput)input;				//对Input初始化		
	}
	/**
	 * /**
	 * 将要解锁的控件放入一个List里
	 * @param list
	 * @return 没有返回值
	 */
	private void addText(List<Text> list){
		list.add(AopName);
		list.add(AopDesc);
		list.add(preCondition);
		list.add(postCondition);
	}

	/**
	 * 判断是否编辑器被修改<br>
	 * 如果返回值是true，关闭编辑器时会提示是否保存。
	 * @return 如果编辑器被修改，返回“true”，否则返回“false”
	 */
	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return bDirty;
	}
	/**
	 * 设置编辑器的状态<br>
	 * 将isDirty(),“修改”按钮修改成b的状态。
	 * @param b 要修改成的状态
	 * @return 没有返回值
	 */
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
		Group searchAopGroup = new Group(composite, SWT.NONE);
		searchAopGroup.setText("原子交易查询");
		searchAopGroup.setLayout(new GridLayout(7, false));
		searchAopGroup.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(searchAopGroup);
/*		Label upProjectLabel1 = new Label(searchGroup, SWT.NONE);
		upProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel1.setText("所属工程");
		
		upProject1 = new Text(searchGroup, SWT.BORDER);
		upProject1.setEnabled(false);
		GridData gd_upProject1 = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject1.widthHint = 70;
		upProject1.setLayoutData(gd_upProject1);
		
		aopNameButton = new Button(searchGroup, SWT.RADIO);
		aopNameButton.setText("原子交易名称");
		
		aopIDButton = new Button(searchGroup, SWT.RADIO);
		aopIDButton.setSelection(true);
		aopIDButton.setText("原子交易标识");
		
		searchText = new Text(searchGroup, SWT.BORDER);
		GridData gd_searchText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_searchText.widthHint = 70;
		searchText.setLayoutData(gd_searchText);
		
		Button searchBtn = new Button(searchGroup, SWT.NONE);
		GridData gd_searchBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_searchBtn.widthHint = 80;
		searchBtn.setLayoutData(gd_searchBtn);
		searchBtn.setText("查询");
		//设置查询按钮的行为
		searchBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				//判断节点的编辑器是否已经被打开，如果已经打开，则会转换焦点到已经打开的编辑器
				IEditorReference[] editorReference = getSite().getWorkbenchWindow().getActivePage().findEditors(null,  	AopEditor.ID, IWorkbenchPage.MATCH_ID);
				for(IEditorReference tmp : editorReference)
				{
					AopEditor aopEditor = (AopEditor)tmp.getEditor(false);
					if(aopEditor.upProject1.getText().equals(upProject.getText()))
					{
						if(aopIDButton.getSelection())
						{
							if(aopEditor.AopId.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(aopEditor);
								return;
							}
						}
						else
						{
							if(aopEditor.AopName.getText().equals(searchText.getText()))
							{
								getSite().getWorkbenchWindow().getActivePage().bringToTop(aopEditor);
								return;
							}
						}
					}
				}

				try {
					//判断是根据标识查询还是根据名称查询
					if(aopIDButton.getSelection())
						//将查询的结果放入一个Map里
						map=impl.queryAopByIdOrName(searchText.getText(), "", ps);
					else
						map=impl.queryAopByIdOrName("", searchText.getText(), ps);
				}catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				restoreMap=map;
				//当查询到数据时，将数据填入编辑器对应的文本框里
				if(!map.isEmpty()){
					AopId.setText(map.get("aopid"));
					AopName.setText(map.get("aopname"));
					AopDesc.setText(map.get("aopdesc"));
					AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
					AopExts.setText(map.get("aopexts"));
					
			//		 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
				//	否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
					
					if(map.get("aoplevel").equals("0")){
						AopLevel.setText("AOP");
						saveBtn.setVisible(true);
						clearBtn.setVisible(true);
						restoreButton.setVisible(true);
					}
					else{
						AopLevel.setText("GOLP");
						saveBtn.setVisible(false);
						clearBtn.setVisible(false);
						restoreButton.setVisible(false);
					}
					upDll.setText(map.get("updll"));
					preCondition.setText(map.get("precondition"));
					postCondition.setText(map.get("postcondition"));
					setPartName("原子交易"+AopId.getText()+"所属工程"+upProject.getText());//修改编辑器标题
					getSite().getShell().setText("GOLP tool "+"原子交易"+AopId.getText()+"所属工程"+upProject.getText());//修改工具标题
					input.setName(AopId.getText());//改变编辑器标识
					//找到树中与编辑器对应的节点，然后将树中的节点设置为焦点
					List<TreeNode> list=input.getSource().getParent().getChildren();
					for(TreeNode treenode:list){
						if(treenode.getName().equals(AopName.getText()))
							break;
					input.setNode((ResourceLeafNode)treenode);
					setSelectNode((ResourceLeafNode)treenode);
					setDirty(false);
					}
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
		//设置文本框的输入限制
		searchText.addVerifyListener(new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=searchText.getText().length()<=32;
			}
		});*/
		
		Group AOPGroup = new Group(composite, SWT.NONE);
		AOPGroup.setLayout(new GridLayout(8, false));
		GridData gd_AOPGroup = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_AOPGroup.heightHint = 414;
		gd_AOPGroup.widthHint = 562;
		AOPGroup.setLayoutData(gd_AOPGroup);
		AOPGroup.setText("原子交易表");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label lblNewLabel_1 = new Label(AOPGroup, SWT.NONE);
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);
		
		Label upProjectLabel = new Label(AOPGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("所属工程");
		
		upProject = new Text(AOPGroup, SWT.BORDER);
		upProject.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProject.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(AOPGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label AopLevelLabel = new Label(AOPGroup, SWT.NONE);
		AopLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopLevelLabel.setText("原子交易级别");
		
		AopLevel = new Text(AOPGroup, SWT.BORDER);
		AopLevel.setEnabled(false);
		GridData gd_AopLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopLevel.widthHint = 70;
		AopLevel.setLayoutData(gd_AopLevel);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopErrRecoverLabel = new Label(AOPGroup, SWT.DROP_DOWN);
		AopErrRecoverLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopErrRecoverLabel.setText("错误恢复机制");
		
		
		AopErrRecover = new Combo(AOPGroup, SWT.READ_ONLY);
		AopErrRecover.setEnabled(false);
		GridData gd_AopErrRecover = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopErrRecover.widthHint = 53;
		AopErrRecover.setLayoutData(gd_AopErrRecover);
		AopErrRecover.setItems(ErrRecoverItem);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label upDllLabel = new Label(AOPGroup, SWT.NONE);
		upDllLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upDllLabel.setText("所属动态库");
		
		upDll = new Text(AOPGroup, SWT.BORDER);
		upDll.setEnabled(false);
		GridData gd_upDll = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upDll.widthHint = 70;
		upDll.setLayoutData(gd_upDll);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopIdLabel = new Label(AOPGroup, SWT.NONE);
		AopIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopIdLabel.setText("原子交易标识");
		
		AopId = new Text(AOPGroup, SWT.BORDER);
		AopId.setEnabled(false);
		GridData gd_AopId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopId.widthHint = 70;
		AopId.setLayoutData(gd_AopId);
		AopId.addVerifyListener(new VerifyListener() {
			//设置文本框的输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopId.getText().length()<=32;
			}
		});
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopNameLabel = new Label(AOPGroup, SWT.NONE);
		AopNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		AopNameLabel.setText("原子交易名称");
		
		AopName = new Text(AOPGroup, SWT.BORDER);
		AopName.setEnabled(false);
		GridData gd_AopName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_AopName.widthHint = 70;
		AopName.setLayoutData(gd_AopName);
		AopName.addVerifyListener(new VerifyListener() {
			//设置文本框的输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopName.getText().length()<=32;
			}
		});
		//当文本框被修改时，设置编辑器的状态为被修改
		AopName.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label AopExtsLabel = new Label(AOPGroup, SWT.NONE);
		AopExtsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		AopExtsLabel.setText("扩展点列表");
		
		AopExts = new Text(AOPGroup, SWT.BORDER);
		AopExts.setEnabled(false);
		GridData gd_AopExts = new GridData(SWT.FILL, SWT.FILL, false, false, 4, 1);
		gd_AopExts.heightHint = 18;
		AopExts.setLayoutData(gd_AopExts);
		
		Button btnNewButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnNewButton.widthHint = 80;
		btnNewButton.setLayoutData(gd_btnNewButton);
		btnNewButton.setText("详情");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label inputDataLabel = new Label(AOPGroup, SWT.NONE);
		inputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		inputDataLabel.setText("输入数据项");
		
		inputData = new Text(AOPGroup, SWT.BORDER);
		inputData.setEnabled(false);
		inputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Button prePositionButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_prePositionButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_prePositionButton.widthHint = 80;
		prePositionButton.setLayoutData(gd_prePositionButton);
		prePositionButton.setText("...");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label outputDataLabel = new Label(AOPGroup, SWT.NONE);
		outputDataLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		outputDataLabel.setText("输出数据项");
		
		outputData = new Text(AOPGroup, SWT.BORDER);
		outputData.setEnabled(false);
		outputData.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 4, 1));
		
		Button postPositionButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_postPositionButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_postPositionButton.widthHint = 80;
		postPositionButton.setLayoutData(gd_postPositionButton);
		postPositionButton.setText("...");
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		Label prePositionLabel = new Label(AOPGroup, SWT.NONE);
		prePositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		prePositionLabel.setText("前置条件");
		
		preCondition = new Text(AOPGroup, SWT.BORDER);
		preCondition.setEnabled(false);
		preCondition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2));
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		preCondition.addVerifyListener(new VerifyListener() {
			//设置文本框输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//当文本框被修改时，设置编辑器的状态为被修改
		preCondition.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
					firePropertyChange(PROP_DIRTY);
					saveBtn.setEnabled(true);
				}
			}
		});
		
		Label postPositionLabel = new Label(AOPGroup, SWT.NONE);
		postPositionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		postPositionLabel.setText("后置条件");
		
		postCondition = new Text(AOPGroup, SWT.BORDER);
		postCondition.setEnabled(false);
		postCondition.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 2));
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		postCondition.addVerifyListener(new VerifyListener() {
			//设置文本框输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//当文本框被修改时，设置编辑器的状态为被修改
		postCondition.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label AopDescLabel = new Label(AOPGroup, SWT.NONE);
		AopDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1));
		AopDescLabel.setText("原子交易说明");
		
		AopDesc = new Text(AOPGroup, SWT.BORDER);
		AopDesc.setEnabled(false);
		AopDesc.addVerifyListener(new VerifyListener() {
			//设置文本框输入限制
			@Override
			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				if(e.character!=8)
					e.doit=AopDesc.getText().length()<=128;
			}
		});
		//当文本框被修改时，设置编辑器的状态为被修改
		AopDesc.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		GridData gd_AopDesc = new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3);
		gd_AopDesc.heightHint = 24;
		AopDesc.setLayoutData(gd_AopDesc);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		new Label(AOPGroup, SWT.NONE);
		
		clearBtn = new Button(AOPGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		clearBtn.setLayoutData(gd_clearBtn);
		clearBtn.setText("编辑");
		/*
		 * 为“编辑”按钮设置行为，当按下按钮时，若按钮为“编辑”，则改为锁定，并将可以
		 * 编辑的控件设置为可用，否则改为“编辑”，将可以编辑的控件设置为不可用，将修
		 * 改按钮设置为不可用。
		 */
		clearBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="编辑"){
					nButton.setText("锁定");
					AopErrRecover.setEnabled(true);
					unlock(list);
				}
				else{
					nButton.setText("编辑");
					AopErrRecover.setEnabled(false);
					lock(list);
				}
				
			}
		});
		new Label(AOPGroup, SWT.NONE);
		
		saveBtn = new Button(AOPGroup, SWT.NONE);
		saveBtn.setEnabled(false);
		GridData gd_saveBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_saveBtn.widthHint = 80;
		saveBtn.setLayoutData(gd_saveBtn);
		saveBtn.setText("修改");
		/*
		 * 为修改按钮设置行为，当按下按钮时，执行saveData方法
		 * 
		 * */
		saveBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				try {
					saveData();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		restoreButton = new Button(AOPGroup, SWT.NONE);
		GridData gd_restoreButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreButton.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreButton);
		restoreButton.setText("恢复");
		//设置恢复按钮行为
		restoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e)
			{
				AopName.setText(restoreMap.get("name"));
				AopDesc.setText(restoreMap.get("aopdesc"));
				AopExts.setText(restoreMap.get("aopexts"));
				preCondition.setText(restoreMap.get("precondition"));
				postCondition.setText(restoreMap.get("postcondition"));
				setDirty(false);
			}
		});
		new Label(AOPGroup, SWT.NONE);
		// TODO Auto-generated method stub
		addText(list);
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
		getSite().getShell().setText("GOLP tool "+"原子交易"+input.getName()+"所属工程"+upProject.getText());//设置工具的标题
		setSelectNode(input.getSource());
		setPartName("原子交易"+input.getName()+"所属工程"+upProject.getText());
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
		ResourceLeafNode rln = ((AopEditorInput) input).getSource();
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
		impl=new EditorAopServiceImpl();
		//对数据库进行查询，放入Map
		map=impl.queryAopByIdOrName(name, "", ps);
		restoreMap=map;
		//依次给控件赋值
		AopId.setText(map.get("id"));
		AopName.setText(map.get("name"));
		AopDesc.setText(map.get("aopdesc"));
		AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
		AopExts.setText(map.get("aopexts"));
		//若aoplevel为零则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if(map.get("aoplevel").equals("0"))
			AopLevel.setText("AOP");
		else{
			AopLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreButton.setVisible(false);
		}
		upDll.setText(map.get("updll"));
		preCondition.setText(map.get("precondition"));
		postCondition.setText(map.get("postcondition"));
		upProject.setText(input.getSource().getRootProject().getId());
		setDirty(false);
		saveBtn.setEnabled(isDirty());
	}
	/**
	 * 将控件设置为不可用<br>
	 * 用for each循环将List中的空间依次设为不可用。
	 * @param list  要设置的空间的列表
	 * @return 没有返回值
	 * */
	 private void lock(List<Text> list){
		 for(Text i : list)
		 {
			 i.setEnabled(false);
		 }
	 }
	 /**
		 * 将控件设置为可用<br>
		 * 用for each循环将List中的空间依次设为可用。
		 * @param list  要设置的空间的列表
		 * @return 没有返回值
		 * */
	 private void unlock(List<Text> list)
	 {
		 for(Text i : list)
			 i.setEnabled(true);
	 }

	/**
	 * 保存数据的方法
	 * 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
	 * 否则将可编辑的文本框里的数据放入一个List中调用EditorAopServiceImpl类的updateAopById方法将
	 * List里的数据替换标识为AopId的数据。
	 * @throws SQLException
	 */
	private void saveData() throws SQLException{
		if(AopName.getText().equals(null)){
			MessageBox box=new MessageBox(getSite().getShell(), SWT.ICON_WARNING|SWT.YES);
			box.setText("警告");
			box.setMessage("数据项名称不能为空");
			box.open();
		}
		else{
			List<String> datalist=new ArrayList<String>();
			datalist.add(AopName.getText());
			datalist.add(AopDesc.getText());
			datalist.add(AopErrRecover.getText().substring(0, 1));
			datalist.add(preCondition.getText());
			datalist.add(postCondition.getText());
			try {
				impl.updateAopById(AopId.getText(), datalist, ps);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setDirty(false);
			}
		restoreMap=impl.queryAopByIdOrName(AopId.getText(), "", ps);
	}

	@Override
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
		return AopId.getText();
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return AopName.getText();
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
		AopId.setText(map.get("id"));
		AopName.setText(map.get("name"));
		AopDesc.setText(map.get("aopdesc"));
		AopErrRecover.setText(ErrRecoverItem[Integer.parseInt(map.get("aoperrrecover"))-1]);
		AopExts.setText(map.get("aopexts"));
		/*
		 判断aoplevel是否为0，为零则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
		*/
		if(map.get("aoplevel").equals("0")){
			AopLevel.setText("AOP");
			saveBtn.setVisible(true);
			clearBtn.setVisible(true);
			restoreButton.setVisible(true);
		}
		else{
			AopLevel.setText("GOLP");
			saveBtn.setVisible(false);
			clearBtn.setVisible(false);
			restoreButton.setVisible(false);
		}
		upDll.setText(map.get("updll"));
		preCondition.setText(map.get("precondition"));
		postCondition.setText(map.get("postcondition"));
		input.setName(AopId.getText());
		setDirty(false);
	}

	@Override
	public void setMyPartName(String name) {
		// TODO Auto-generated method stub
		setPartName("原子交易"+AopId.getText()+"所属工程"+upProject.getText());
	}

	@Override
	public void setEnable(boolean b) {
		// TODO Auto-generated method stub
		if(!b){
			clearBtn.setText("锁定");
			AopErrRecover.setEnabled(true);
			unlock(list);
		}
		else{
			clearBtn.setText("编辑");
			AopErrRecover.setEnabled(false);
			lock(list);
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
