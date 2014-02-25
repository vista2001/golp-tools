/* 文件名：       TradeEditor.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：   1.将dataItemDesc的属性更改为SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI；
 *         2.修改init方法中的setPartName，增加对所属工程的显示，这样就解决了从导航中打开一个编辑器后，点击该编辑器
 *         的关闭按钮时，编辑器并未关闭，而是在编辑器的标题中显示了更多的内容，产生该问题的原因在于init方法和setFocus
 *         方法在调用setPartName时参数不一致；
 *         3.修改了部分布局；
 *         4.覆盖父类中的dispose方法，使得在所有编辑器都关闭时，恢复工具的标题“GOLP TOOL”；
 *         5.在进行级别（APP/GOLP）的判断时，不再直接使用0、1等数字，改为使用dev.util.Constants类的常量；
 *         6.在UI中统一使用0-APP、1-GOLP这种表示；
 *         7.用DebugOut.println方法替换System.out.println方法；
 *         8.增加编辑器滚动功能；
 *         9.统一使用File.separator。
 * 修改人：        zxh
 * 修改时间：    2013.12.2
 * 修改内容：    1.修改变量命名     
 *      
 */

package dev.editors.dataItem;

import java.io.File;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorDataitemServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.generate.fml.FmlId;
import dev.model.base.ResourceLeafNode;
import dev.util.Constants;
import dev.util.DebugOut;
import dev.util.RegExpCheck;
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
public class DataItemEditor extends EditorPart implements ISearch, IGetUpProject {
	public DataItemEditor() {
	}
	
	public static final String ID="dev.editor.DataItem.DataItem";	//编辑器类的标识
	public DataItemEditorInput input;									 	//Input类对象
	private Text upProjectText;											//表项部分所属工程文本框
	private Text dataItemIdText;										//数据项标识文本框
	private Text FMLIDText;												//FML编号文本框
	private Text dataItemDescText;										//数据项说明文本框
	private Text dataItemLevelText;										//数据项级别文本框
	private Text dataItemNameText;										//数据项名称文本框
	private Text dataItemLenText;										//数据项类型长度文本框
	private Text dataItemAopText;										//数据项AOP文本框
	private Combo dataItemTypeCombo;								//数据项类型下拉菜单
	private Button saveButton;											//修改按钮
	private Button unlockButton;										//解锁按钮
	private PreferenceStore ps;										//数据库配置信息
	private EditorDataitemServiceImpl impl;							//数据库操作类对象
	private Map< String, String> map;								//存储查询的数据的Map
	private Map<String, String> restoreMap;
	private String[] dataItemTypeItem={"0-int", "1-long", "2-double",
	                                   "3-char", "4-char[]", "5-String"};
	private Button restoreButton;
	private boolean bDirty;
	private Search search;
	//数据项类型下拉菜单选项字符串数组
	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			saveData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() {
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
		this.setSite(site);								//设置Site
		this.setInput(input);							//设置Input
		this.input=(DataItemEditorInput)input;              //对Input初始化
        this.setPartName("数据项" + " " + this.input.getName() + " " + "所属工程"
                + " " + this.input.getSource().getRootProject().getId()); // 设置编辑器标题
	}

	@Override
	public boolean isDirty() {
		return bDirty;
	}

	public void setDirty(boolean b){
		bDirty=b;
		saveButton.setEnabled(b);
		restoreButton.setEnabled(b);
		if(b)
			firePropertyChange(PROP_DIRTY);
		else
			firePropertyChange(PROP_INPUT);
	} 
	@Override
	public boolean isSaveAsAllowed() {
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
	    ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
	    
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		//创建编辑器里的控件
		Group dataItemSearchGroup = new Group(composite, SWT.NONE);
		dataItemSearchGroup.setText("数据项查询");
		dataItemSearchGroup.setLayout(new GridLayout(7, false));
		dataItemSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(dataItemSearchGroup);

		Group dataItemGroup = new Group(composite, SWT.NONE);
		dataItemGroup.setLayout(new GridLayout(8, false));
		GridData gd_dataItemGroup = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
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
		
		upProjectText = new Text(dataItemGroup, SWT.BORDER);
		upProjectText.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_upProject.widthHint = 70;
		upProjectText.setLayoutData(gd_upProject);
		
		Label lblNewLabel = new Label(dataItemGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);
		
		Label dataItemLevelLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLevelLabel.setText("数据项级别");
		
		dataItemLevelText = new Text(dataItemGroup, SWT.BORDER);
		dataItemLevelText.setEnabled(false);
		GridData gd_dataItemLevel = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLevel.widthHint = 70;
		dataItemLevelText.setLayoutData(gd_dataItemLevel);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemIdLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemIdLabel.setText("数据项标识");
		
		dataItemIdText = new Text(dataItemGroup, SWT.BORDER);
		dataItemIdText.setEnabled(false);
		GridData gd_dataItemId = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemId.widthHint = 70;
		dataItemIdText.setLayoutData(gd_dataItemId);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemNameLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemNameLabel.setText("*数据项名称");
		
		dataItemNameText = new Text(dataItemGroup, SWT.BORDER);
		dataItemNameText.setEnabled(false);
		GridData gd_dataItemName = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemName.widthHint = 70;
		dataItemNameText.setLayoutData(gd_dataItemName);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
//		dataItemName.addVerifyListener(new VerifyListener() {
//			//设置文本框输入限制
//			@Override
//			public void verifyText(VerifyEvent e) {
//				if(e.character!=8)
//					e.doit=dataItemName.getText().length()<=32;
//			}
//		});
		dataItemNameText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label dataItemTypeLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemTypeLabel.setText("*数据项类型");
		
		dataItemTypeCombo = new Combo(dataItemGroup, SWT.READ_ONLY);
		dataItemTypeCombo.setEnabled(false);
		GridData gd_dataItemType = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemType.widthHint = 53;
		dataItemTypeCombo.setLayoutData(gd_dataItemType);
		dataItemTypeCombo.setItems(dataItemTypeItem);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		//设置下拉菜单行为
//		dataItemTypeCombo.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e){
//				//当菜单项是“4-char[]”或者“5-String”时，数据项长度变为可编辑
//				if ( dataItemTypeCombo.getText().equals("4-char[]")
//				   ||dataItemTypeCombo.getText().equals("5-String"))
//				{
//				    dataItemLenText.setText("");
//				    dataItemLenText.setEnabled(true);
//				}
//				//否则，不可编辑，长度变-1
//				else{
//				    dataItemLenText.setEnabled(false);
//				    dataItemLenText.setText("-1");
//				}
//			}
//		});
		dataItemTypeCombo.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
			    
			    // 在“编辑”状态下，该监听器才生效。
			    if(unlockButton.getText().equals("锁定"))
			    {
			      //当菜单项是“4-char[]”或者“5-String”时，数据项长度变为可编辑
	                if ( dataItemTypeCombo.getText().equals("4-char[]")
	                   ||dataItemTypeCombo.getText().equals("5-String"))
	                {
	                    //当类型不是char[]和String时，dataItemLenText的内容是-1，
	                    //此时，若类型变为char[]或String，则应将之前的内容（-1）清空。
	                    if(dataItemLenText.getText().equals("-1"))
	                    {
	                        dataItemLenText.setText("");
	                    }
	                    dataItemLenText.setEnabled(true);
	                }
	                //否则，不可编辑，长度变-1
	                else
	                {
	                    dataItemLenText.setEnabled(false);
	                    dataItemLenText.setText("-1");
	                }
	                setDirty(true);
			    }
				
			}
		});
		
		Label dataItemLenLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemLenLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemLenLabel.setText("*数据项长度");
	
		dataItemLenText= new Text(dataItemGroup, SWT.BORDER);
		dataItemLenText.setEnabled(false);
		
		GridData gd_dataItemLen = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemLen.widthHint = 70;
		dataItemLenText.setLayoutData(gd_dataItemLen);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
//		dataItemLen.addVerifyListener(new VerifyListener() {
//			//设置文本框的输入限制
//			@Override
//			public void verifyText(VerifyEvent e) {
//				if(e.character!=8)
//					e.doit=e.text.length()<=5&&e.text.matches("^[0-9]+$");
//			}
//		});
		dataItemLenText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		
		Label FMLIDLabel = new Label(dataItemGroup, SWT.NONE);
		FMLIDLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		FMLIDLabel.setText("FML标识");
		
		FMLIDText = new Text(dataItemGroup, SWT.BORDER);
		FMLIDText.setEnabled(false);
		GridData gd_FMLID = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_FMLID.widthHint = 70;
		FMLIDText.setLayoutData(gd_FMLID);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemAopLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemAopLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemAopLabel.setText("数据项检查函数");
		
		dataItemAopText = new Text(dataItemGroup, SWT.BORDER);
		dataItemAopText.setEnabled(false);
		GridData gd_dataItemAop = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_dataItemAop.widthHint = 70;
		dataItemAopText.setLayoutData(gd_dataItemAop);
		dataItemAopText.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent e)
            {
                if (!isDirty())
                {
                    setDirty(true);
                }
            }
        });
		
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		Label dataItemDescLabel = new Label(dataItemGroup, SWT.NONE);
		dataItemDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dataItemDescLabel.setText("*描述");
		
		dataItemDescText = new Text(dataItemGroup, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		dataItemDescText.setEnabled(false);
		dataItemDescText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				if(!isDirty())
				{
					setDirty(true);
				}
			}
		});
		dataItemDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 5, 3));
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		new Label(dataItemGroup, SWT.NONE);
		
		unlockButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_clearBtn.widthHint = 80;
		unlockButton.setLayoutData(gd_clearBtn);
		unlockButton.setText("编辑");
		/*
		 * 为“解锁”按钮设置行为，当按下按钮时，若按钮为“解锁”，则改为锁定，并将可以
		 * 编辑的控件设置为可用，将“修改”按钮设置为可用；否则改为解锁，将可以编辑的
		 * 控件设置为不可用，将修改按钮设置为不可用。
		 */
		unlockButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				Button nButton=((Button)e.getSource());
				if(nButton.getText()=="编辑"){
					nButton.setText("锁定");
					dataItemNameText.setEnabled(true);
					dataItemDescText.setEnabled(true);
					dataItemTypeCombo.setEnabled(true);
					if(dataItemTypeCombo.getText().equals(dataItemTypeItem[4])||dataItemTypeCombo.getText().equals(dataItemTypeItem[5]))
					{
						dataItemLenText.setEnabled(true);
					}
					dataItemAopText.setEnabled(true);
				}
				else{
					nButton.setText("编辑");
					dataItemNameText.setEnabled(false);
					dataItemDescText.setEnabled(false);
					dataItemTypeCombo.setEnabled(false);
					dataItemLenText.setEnabled(false);
					dataItemAopText.setEnabled(false);
				}
				
			}
		});
		new Label(dataItemGroup, SWT.NONE);
		
		saveButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_savaBtn.widthHint = 80;
		saveButton.setLayoutData(gd_savaBtn);
		saveButton.setText("保存");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				try {
					saveData();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		restoreButton = new Button(dataItemGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreBtn);
		restoreButton.setText("恢复");
		//设置恢复按钮行为
		restoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				dataItemNameText.setText(restoreMap.get("NAME"));
				dataItemDescText.setText(restoreMap.get("datadesc"));
				dataItemTypeCombo.setText(dataItemTypeItem[new Integer(restoreMap.get("datatype"))]);
				dataItemLenText.setText(restoreMap.get("datalen"));
				dataItemAopText.setText(restoreMap.get("dataaop"));
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
		scrolledComposite.setContent(composite);
        scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT,
                SWT.DEFAULT));
		
		try {
			datainit(input.getName());
		} catch (SQLException e1) {
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
        getSite().getShell().setText(
                "GOLP TOOL" + " " + "数据项" + " " + input.getName() + " "
                        + "所属工程" + " " + upProjectText.getText());// 设置工具的名称
		setPartName("数据项" + " " + input.getName() + " " + "所属工程" + " " + upProjectText.getText());
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
		ResourceLeafNode rln = ((DataItemEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6) + File.separator+ prjId + ".properties";
		DebugOut.println("dbfiles==="+dbfiles);
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//对数据库进行查询，放入Map
		impl=new EditorDataitemServiceImpl();
		map=impl.queryDataitemByIdOrName(name, "", ps);
		restoreMap=map;
		//依次给控件赋值
		dataItemIdText.setText(map.get("ID"));
		dataItemNameText.setText(map.get("NAME"));
		dataItemDescText.setText(map.get("datadesc"));
		//若aoplevel为常量Constants.APP，则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if(map.get("datalvl").equals(Constants.APP)){
			dataItemLevelText.setText("0-APP");
		}
		else
		{
			dataItemLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
		}
		dataItemTypeCombo.setText(dataItemTypeItem[new Integer(map.get("datatype"))]);
		dataItemLenText.setText(map.get("datalen"));
		dataItemAopText.setText(map.get("dataaop"));
		FMLIDText.setText(map.get("fmlid"));
		upProjectText.setText(input.getSource().getRootProject().getId());
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
    private void saveData() throws SQLException
    {
        if (dataItemNameText.getText().trim().isEmpty()
            || dataItemDescText.getText().trim().isEmpty())
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "必填项不能为空");
        }
        else if(( dataItemTypeCombo.getText().equals("4-char[]")
                || dataItemTypeCombo.getText().equals("5-String"))
                && (RegExpCheck.isPositiveInteger(dataItemLenText.getText()) == false))
        {
            showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "数据项长度为正整数");
        }
        else
        {
            List<String> datalist = new ArrayList<String>();
            datalist.add(dataItemNameText.getText());
            datalist.add(dataItemDescText.getText());
            datalist.add(dataItemTypeCombo.getText().substring(0, 1));
            datalist.add(dataItemLenText.getText());
            datalist.add(dataItemAopText.getText());
            
            // 局部变量fmlId初始化0L
            Long fmlId = 0L;
            try
            {
                //通过编辑器中的数据项名称，数据项Id和数据类型，计算fmlId
                fmlId = FmlId.getFmlId(dataItemNameText.getText(), 
                                            Integer.parseInt(dataItemIdText.getText()), 
                                            dataItemTypeCombo.getText().substring(2));
            }
            catch (NumberFormatException | IOException | InterruptedException e)
            {
                e.printStackTrace();
                
                //若上述计算抛出异常，则将fmlId赋为-1L
                fmlId = -1L;
            }
            datalist.add(fmlId + "");
            
            // 在计算出fmlId后，更新编辑器中的fmlId
            FMLIDText.setText(fmlId + "");
            
            try
            {
                impl.updateDataitemById(dataItemIdText.getText(), datalist, ps);
            } catch (SQLException e1)
            {
                e1.printStackTrace();
            }
            restoreMap = impl.queryDataitemByIdOrName(dataItemIdText.getText(),
                    "", ps);
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
		return dataItemIdText.getText();
	}

	@Override
	public String getTargetName() {
		return dataItemNameText.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		this.map=map;
		this.restoreMap=map;
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
	public void setThisNode(ResourceLeafNode node) {
		input.setSource(node);
	}

	@Override
	public ResourceLeafNode getThisNode() {
		return input.getSource();
	}

	@Override
	public void setInputNode(ResourceLeafNode node) {
		input.setSource(node);
	}

	@Override
	public int showMessage(int style, String title, String message) {
		MessageBox box=new MessageBox(getSite().getShell(), style);
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
		dataItemIdText.setText(map.get("ID"));
		dataItemNameText.setText(map.get("NAME"));
		dataItemDescText.setText(map.get("datadesc"));
		/*
		 判断aoplevel是否为Constants.APP，是则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		否则为"GOLP",将“解锁”与“修改”按钮设置为不可见 
		*/
		if(map.get("datalvl").equals(Constants.GOLP)){
			dataItemLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
			dataItemNameText.setEnabled(false);
			dataItemLenText.setEnabled(false);
			dataItemTypeCombo.setEnabled(false);
			dataItemDescText.setEnabled(false);
			dataItemAopText.setEnabled(false);
		}
		else
		{
			dataItemLevelText.setText("0-APP");
			saveButton.setVisible(true);
			unlockButton.setVisible(true);
			restoreButton.setVisible(true);
		}
	
		dataItemTypeCombo.setText(dataItemTypeItem[Integer.parseInt(map.get("datatype"))]);
		dataItemLenText.setText(map.get("datalen"));
		dataItemAopText.setText(map.get("dataaop"));
		FMLIDText.setText(map.get("fmlid"));
		input.setName(dataItemIdText.getText());
		setDirty(false);
	}

	@Override
	public void setEditorPartName(String name) {
		setPartName("原子交易"+dataItemIdText.getText()+"所属工程"+upProjectText.getText());
	}

	@Override
	public void setEnable(boolean b) {
		if(!b){
			unlockButton.setText("编辑");
			dataItemNameText.setEnabled(false);
			dataItemDescText.setEnabled(false);
			dataItemTypeCombo.setEnabled(false);
			dataItemLenText.setEnabled(false);
			dataItemAopText.setEnabled(false);
			saveButton.setEnabled(false);
		}
		else{
			unlockButton.setText("锁定");
			dataItemNameText.setEnabled(true);
			dataItemDescText.setEnabled(true);
			dataItemTypeCombo.setEnabled(true);
			dataItemLenText.setEnabled(true);
			dataItemAopText.setEnabled(true);
			saveButton.setEnabled(true);
		}
	}

	@Override
	public void save() {
		try {
			saveData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void dispose()
    {
        // 该编辑器被关闭后，恢复工具的标题
        if (getEditorSite().getPage().getActiveEditor() == null)
        {
            getSite().getShell().setText("GOLP TOOL");
        }
    }

    @Override
    public String getUpProject()
    {
        return upProjectText.getText();
    }
}
