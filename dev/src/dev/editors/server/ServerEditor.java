package dev.editors.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

import dev.db.service.EditorsDaoServiceImpl;
import dev.model.base.ResourceLeafNode;
import dev.views.NavView;
import dev.wizards.newAop.InformDialogEvent;
import dev.wizards.newAop.InformDialogListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
/**
 * 该类定义了服务程序所对应的编辑器
 */
public class ServerEditor extends EditorPart implements ISearch
{
	public static final String ID = "dev.editors.server.serverEditor";
	/**
	 * 该ps用来装载工程对应的properties文件
	 */
	private PreferenceStore ps;
	private ServerEditorInput input;
	/**
	 * 该thisNode用来保存从ServerEditorInput获取到的ResourceLeafNode
	 */
	private ResourceLeafNode thisNode;
	private boolean dirty = false;
	private String serverId;
	private String upProject;
	private Search search;
	/**
	 * 该map用来接收查询或初始化编辑器时从数据库得到的服务程序对应的信息
	 */
	private HashMap<String, String> map;
	private Text serverUpProjectText1;
	private Combo serverLevelCombo;
	private Text serverIdText1;
	private Text serverNameText1;
	private Text serverSpeclibPathText;
	private Text serverSpeclibNameText;
	private Text serverSpecIncludePathText;
	private Text callBackSourceText;
	private Text othFunSourceText;
	private Text serverDescText;
	private Button serverSpeclibPathButton;
	private Button serverSpeclibNameButton;
	private Button serverSpecIncludePathButton;
	private Button callBackSourceButton;
	private Button othFunSourceButton;
	private Button unLockButton;
	private Button saveButton;
	private Button recoverButton;
	/**
	 * 该controls用来引用当前编辑器页面中，所有受“锁定/编辑”按钮控制的控件
	 */
	private List<Control> controls;
	public Text getServerUpProjectText1()
	{
		return serverUpProjectText1;
	}
	
	public Text getServerIdText1()
	{
		return serverIdText1;
	}

	public Text getServerNameText1()
	{
		return serverNameText1;
	}
	/**
	 * 当编辑器中的内容被修改后，手动调用该方法，其中设置了dirty标志位的状态、“保存”按钮是否可用并调用firePropertyChange()函数
	 * @param dirty 根据该值来进行具体设置
	 */
	@Override
	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
		saveButton.setEnabled(dirty);
		recoverButton.setEnabled(dirty);
		firePropertyChange(PROP_DIRTY);
	}

	public ServerEditor()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doSave(IProgressMonitor monitor)
	{
		// TODO Auto-generated method stub
		mySave();
	}

	@Override
	public void doSaveAs()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException
	{
		// TODO Auto-generated method stub
		this.setSite(site);
		this.setInput(input);
		serverId = input.getName();
		thisNode = ((ServerEditorInput) input).getNode();
		this.input = (ServerEditorInput) input;
		//初始化ps
		initps();
		//初始化该编辑器初次显示时，所应提供的数据
		initData();
		this.setPartName("服务程序" + " " + input.getName() + " " + "所属工程" + " " + upProject);
	}
	/**
	 * 该方法先计算出该服务程序所属工程的配置文件路径，然后用该路径初始化ps,并加载ps
	 */
	private void initps()
	{
		String prjId = thisNode.getParent().getParent().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    IWorkspaceRoot root = workspace.getRoot();
	    IProject project = root.getProject(prjId);
	    String propertyPath = project.getLocationURI().toString().substring(6) + '/' + prjId +".properties";
	    //System.out.println(propertyPath);
	    ps = new PreferenceStore(propertyPath);
		try
		 {
			 ps.load();
		 } 
		 catch (IOException e)
		 {
				// TODO 自动生成的 catch 块
			 e.printStackTrace();
		 }
	}

	@Override
	public boolean isDirty()
	{
		// TODO Auto-generated method stub
		return dirty;
	}

	@Override
	public boolean isSaveAsAllowed()
	{
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * 该方法将编辑器页面中所有受“锁定/编辑”按钮控制的控件放入变量controls中
	 */
	private void getControls()
	{
		controls = new ArrayList<Control>();
		controls.add(serverLevelCombo);
		controls.add(serverNameText1);
		controls.add(serverSpeclibPathText);
		controls.add(serverSpeclibPathButton);
		controls.add(serverSpeclibNameText);
		controls.add(serverSpeclibNameButton);
		controls.add(serverSpecIncludePathText);
		controls.add(serverSpecIncludePathButton);
		controls.add(callBackSourceText);
		controls.add(callBackSourceButton);
		controls.add(othFunSourceText);
		controls.add(othFunSourceButton);
		controls.add(serverDescText);
	}
	/**
	 * 设置页面内所有受“锁定/编辑”按钮控制的控件的可用性
	 * @param b 若该值为true,则相应的按钮可用,false则相反
	 */
	@Override
	public void setEnable(boolean b)
	{
		for (Control control : controls)
		{
			control.setEnabled(b);
		}
	}
	/**
	 * 初始化该编辑器初次显示时，所应提供的数据
	 */
	private void initData()
	{
		EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
		try
		{
			map = editorsDaoServiceImpl.queryServerByIdOrName(serverId, 0,ps);
		} catch (SQLException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		upProject = thisNode.getParent().getParent().getId();
		
	}

	@Override
	public void createPartControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));

		Group serverSearchGroup = new Group(composite, SWT.NONE);
		serverSearchGroup.setText("服务程序查询");
		serverSearchGroup.setLayout(new GridLayout(7, false));
		serverSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false, 1, 1));
		
		search = new Search();
		search.setUpProject(upProject);
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(serverSearchGroup);
		
		Group serverEditGroup = new Group(composite, SWT.NONE);
		serverEditGroup.setText("服务程序");
		serverEditGroup.setLayout(new GridLayout(7, false));
		serverEditGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		Label spaceLabel1 = new Label(serverEditGroup, SWT.NONE);
		GridData gd_spaceLabel1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1);
		gd_spaceLabel1.widthHint = 80;
		spaceLabel1.setLayoutData(gd_spaceLabel1);

		Label serverUpProjectLabel1 = new Label(serverEditGroup, SWT.NONE);
		serverUpProjectLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		serverUpProjectLabel1.setText("所属工程");

		serverUpProjectText1 = new Text(serverEditGroup, SWT.BORDER);
		serverUpProjectText1.setEnabled(false);
		GridData gd_serverUpProjectText1 = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1);
		gd_serverUpProjectText1.widthHint = 70;
		serverUpProjectText1.setLayoutData(gd_serverUpProjectText1);
		serverUpProjectText1.setText(upProject);

		Label spaceLabel = new Label(serverEditGroup, SWT.NONE);
		GridData gd_spaceLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_spaceLabel.widthHint = 80;
		spaceLabel.setLayoutData(gd_spaceLabel);

		Label serverLevelLabel = new Label(serverEditGroup, SWT.NONE);
		serverLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		serverLevelLabel.setText("服务程序级别");

		serverLevelCombo = new Combo(serverEditGroup, SWT.READ_ONLY);
		serverLevelCombo.setEnabled(false);
		GridData gd_serverLevelCombo = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_serverLevelCombo.widthHint = 53;
		serverLevelCombo.setLayoutData(gd_serverLevelCombo);
		serverLevelCombo.add("0-APP");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label serverIdLabel1 = new Label(serverEditGroup, SWT.NONE);
		serverIdLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		serverIdLabel1.setText("服务程序标识");

		serverIdText1 = new Text(serverEditGroup, SWT.BORDER);
		serverIdText1.setEnabled(false);
		GridData gd_serverIdText1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_serverIdText1.widthHint = 70;
		serverIdText1.setLayoutData(gd_serverIdText1);
		new Label(serverEditGroup, SWT.NONE);

		Label serverNameLabel1 = new Label(serverEditGroup, SWT.NONE);
		serverNameLabel1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		serverNameLabel1.setText("服务程序名称");

		serverNameText1 = new Text(serverEditGroup, SWT.BORDER);
		serverNameText1.setEnabled(false);
		GridData gd_serverNameText1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_serverNameText1.widthHint = 70;
		serverNameText1.setLayoutData(gd_serverNameText1);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label serverSpeclibPathLabel = new Label(serverEditGroup, SWT.NONE);
		serverSpeclibPathLabel.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		serverSpeclibPathLabel.setText("个性依赖库路径");

		serverSpeclibPathText = new Text(serverEditGroup, SWT.BORDER);
		serverSpeclibPathText.setEnabled(false);
		GridData gd_serverSpeclibPathText = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		gd_serverSpeclibPathText.widthHint = 241;
		serverSpeclibPathText.setLayoutData(gd_serverSpeclibPathText);

		serverSpeclibPathButton = new Button(serverEditGroup, SWT.NONE);
		serverSpeclibPathButton.setEnabled(false);
		GridData gd_serverSpeclibPathButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_serverSpeclibPathButton.widthHint = 80;
		serverSpeclibPathButton.setLayoutData(gd_serverSpeclibPathButton);
		serverSpeclibPathButton.setText("选择");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label serverSpeclibNameLabel = new Label(serverEditGroup, SWT.NONE);
		serverSpeclibNameLabel.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		serverSpeclibNameLabel.setText("个性依赖库名称");

		serverSpeclibNameText = new Text(serverEditGroup, SWT.BORDER);
		serverSpeclibNameText.setEnabled(false);
		GridData gd_serverSpeclibNameText = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		gd_serverSpeclibNameText.widthHint = 241;
		serverSpeclibNameText.setLayoutData(gd_serverSpeclibNameText);

		serverSpeclibNameButton = new Button(serverEditGroup, SWT.NONE);
		serverSpeclibNameButton.setEnabled(false);
		GridData gd_serverSpeclibNameButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_serverSpeclibNameButton.widthHint = 80;
		serverSpeclibNameButton.setLayoutData(gd_serverSpeclibNameButton);
		serverSpeclibNameButton.setText("选择");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label serverSpecIncludePathLabel = new Label(serverEditGroup, SWT.NONE);
		serverSpecIncludePathLabel.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		serverSpecIncludePathLabel.setText("个性依赖头文件路径");

		serverSpecIncludePathText = new Text(serverEditGroup, SWT.BORDER);
		serverSpecIncludePathText.setEnabled(false);
		GridData gd_serverSpecIncludePathText = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 3, 1);
		gd_serverSpecIncludePathText.widthHint = 241;
		serverSpecIncludePathText.setLayoutData(gd_serverSpecIncludePathText);

		serverSpecIncludePathButton = new Button(serverEditGroup, SWT.NONE);
		serverSpecIncludePathButton.setEnabled(false);
		GridData gd_serverSpecIncludePathButton = new GridData(SWT.LEFT,
				SWT.CENTER, false, false, 1, 1);
		gd_serverSpecIncludePathButton.widthHint = 80;
		serverSpecIncludePathButton
				.setLayoutData(gd_serverSpecIncludePathButton);
		serverSpecIncludePathButton.setText("选择");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label callBackSourceLabel = new Label(serverEditGroup, SWT.NONE);
		callBackSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		callBackSourceLabel.setText("回调的源程序");

		callBackSourceText = new Text(serverEditGroup, SWT.BORDER);
		callBackSourceText.setEnabled(false);
		GridData gd_callBackSourceText = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		gd_callBackSourceText.widthHint = 241;
		callBackSourceText.setLayoutData(gd_callBackSourceText);

		callBackSourceButton = new Button(serverEditGroup, SWT.NONE);
		callBackSourceButton.setEnabled(false);
		GridData gd_callBackSourceButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_callBackSourceButton.widthHint = 80;
		callBackSourceButton.setLayoutData(gd_callBackSourceButton);
		callBackSourceButton.setText("选择");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label othFunSourceLabel = new Label(serverEditGroup, SWT.NONE);
		othFunSourceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		othFunSourceLabel.setText("其他函数程序");

		othFunSourceText = new Text(serverEditGroup, SWT.BORDER);
		othFunSourceText.setEnabled(false);
		GridData gd_othFunSourceText = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 3, 1);
		gd_othFunSourceText.widthHint = 241;
		othFunSourceText.setLayoutData(gd_othFunSourceText);

		othFunSourceButton = new Button(serverEditGroup, SWT.NONE);
		othFunSourceButton.setEnabled(false);
		GridData gd_othFunSourceButton = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_othFunSourceButton.widthHint = 80;
		othFunSourceButton.setLayoutData(gd_othFunSourceButton);
		othFunSourceButton.setText("选择");
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		Label serverDescLabel = new Label(serverEditGroup, SWT.NONE);
		serverDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		serverDescLabel.setText("描述");

		serverDescText = new Text(serverEditGroup, SWT.BORDER | SWT.V_SCROLL);
		serverDescText.setEnabled(false);
		GridData gd_serverDescText = new GridData(SWT.LEFT, SWT.FILL, false,
				false, 4, 3);
		gd_serverDescText.widthHint = 307;
		serverDescText.setLayoutData(gd_serverDescText);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);
		new Label(serverEditGroup, SWT.NONE);

		unLockButton = new Button(serverEditGroup, SWT.NONE);
		GridData gd_unLockButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_unLockButton.widthHint = 80;
		unLockButton.setLayoutData(gd_unLockButton);
		unLockButton.setText("编辑");

		saveButton = new Button(serverEditGroup, SWT.NONE);
		saveButton.setEnabled(false);
		GridData gd_saveButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_saveButton.widthHint = 80;
		saveButton.setLayoutData(gd_saveButton);
		saveButton.setText("保存");

		recoverButton = new Button(serverEditGroup, SWT.NONE);
		recoverButton.setEnabled(false);
		GridData gd_recoverButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_recoverButton.widthHint = 80;
		recoverButton.setLayoutData(gd_recoverButton);
		recoverButton.setText("恢复");
		new Label(serverEditGroup, SWT.NONE);

		serverNameText1.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		serverSpeclibPathText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		serverSpeclibNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		serverSpecIncludePathText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		callBackSourceText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		othFunSourceText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		serverDescText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				setDirty(true);
			}
		});

		serverSpeclibPathButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ServerSpeclibPathDialog serverSpeclibPathDialog = new ServerSpeclibPathDialog(
						e.display.getActiveShell(), e.getSource(),
						serverSpeclibPathText.getText().trim());
				serverSpeclibPathDialog
						.addInformDialogListener(new InformDialogListener()
						{

							@Override
							public void handleEvent(InformDialogEvent dm)
							{
								java.util.List<String> l = ((ServerSpeclibPathDialog) dm
										.getdm()).listForReturn;
								String s = "";
								for (String string : l)
								{
									if (s.equals(""))
									{
										s += string;
									} else
									{
										s += "|" + string;
									}
								}
								serverSpeclibPathText.setText(s);
							}
						});
				serverSpeclibPathDialog.open();
			}
		});

		serverSpeclibNameButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ServerSpeclibNameDialog serverSpeclibNameDialog = new ServerSpeclibNameDialog(
						e.display.getActiveShell(), e.getSource(),
						serverSpeclibNameText.getText().trim());
				serverSpeclibNameDialog
						.addInformDialogListener(new InformDialogListener()
						{

							@Override
							public void handleEvent(InformDialogEvent dm)
							{
								java.util.List<String> l = ((ServerSpeclibNameDialog) dm
										.getdm()).listForReturn;
								String s = "";
								for (String string : l)
								{
									if (s.equals(""))
									{
										s += string;
									} else
									{
										s += "|" + string;
									}
								}
								serverSpeclibNameText.setText(s);
							}
						});
				serverSpeclibNameDialog.open();
			}
		});

		serverSpecIncludePathButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				ServerSpecIncludePathDialog serverSpecIncludePathDialog = new ServerSpecIncludePathDialog(
						e.display.getActiveShell(), e.getSource(),
						serverSpecIncludePathText.getText().trim());
				serverSpecIncludePathDialog
						.addInformDialogListener(new InformDialogListener()
						{

							@Override
							public void handleEvent(InformDialogEvent dm)
							{
								java.util.List<String> l = ((ServerSpecIncludePathDialog) dm
										.getdm()).listForReturn;
								String s = "";
								for (String string : l)
								{
									if (s.equals(""))
									{
										s += string;
									} else
									{
										s += "|" + string;
									}
								}
								serverSpecIncludePathText.setText(s);
							}
						});
				serverSpecIncludePathDialog.open();
			}
		});

		callBackSourceButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				CallBackSourceDialog callBackSourceDialog = new CallBackSourceDialog(
						e.display.getActiveShell(), e.getSource(),
						callBackSourceText.getText().trim());
				callBackSourceDialog
						.addInformDialogListener(new InformDialogListener()
						{

							@Override
							public void handleEvent(InformDialogEvent dm)
							{
								java.util.List<String> l = ((CallBackSourceDialog) dm
										.getdm()).listForReturn;
								String s = "";
								for (String string : l)
								{
									if (s.equals(""))
									{
										s += string;
									} else
									{
										s += "|" + string;
									}
								}
								callBackSourceText.setText(s);
							}
						});
				callBackSourceDialog.open();
			}
		});

		othFunSourceButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				OthFunSourceDialog othFunSourceDialog = new OthFunSourceDialog(
						e.display.getActiveShell(), e.getSource(),
						othFunSourceText.getText().trim());
				othFunSourceDialog
						.addInformDialogListener(new InformDialogListener()
						{

							@Override
							public void handleEvent(InformDialogEvent dm)
							{
								java.util.List<String> l = ((OthFunSourceDialog) dm
										.getdm()).listForReturn;
								String s = "";
								for (String string : l)
								{
									if (s.equals(""))
									{
										s += string;
									} else
									{
										s += "|" + string;
									}
								}
								othFunSourceText.setText(s);
							}
						});
				othFunSourceDialog.open();
			}
		});

		unLockButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				
				if (unLockButton.getText().equals("编辑"))
				{
					setEnable(true);
					unLockButton.setText("锁定");
				} else
				{
					setEnable(false);
					unLockButton.setText("编辑");
				}
			}
		});
		
		saveButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				mySave();
			}
		});
		
		recoverButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setControlsText();
				setDirty(false);
			}
		});

		getControls();
		setControlsText();
		
	}
	/**
	 * 用成员变量map中的值，填充页面的控件
	 */
	@Override
	public void setControlsText()
	{
		if (map.get("SERVERLEVEL").equals("0"))
		{
			serverLevelCombo.setText("0-APP");
		} else if (map.get("SERVERLEVEL").equals("1"))
		{
			serverLevelCombo.setText("1-GOLP");
			unLockButton.setEnabled(false);
		}
//		serverIdText1.setText(map.get("SERVERID"));
		serverIdText1.setText(map.get("ID"));
//		serverNameText1.setText(map.get("SERVERNAME"));
		serverNameText1.setText(map.get("NAME"));
		serverSpeclibPathText.setText(map.get("SERVERSPECLIBPATH"));
		serverSpeclibNameText.setText(map.get("SERVERSPECLIBNAME"));
		serverSpecIncludePathText.setText(map.get("SERVERSPECAOPDLLS"));
		callBackSourceText.setText(map.get("CALLBACKSOURCE"));
		othFunSourceText.setText(map.get("OTHFUNSOURCE"));
		serverDescText.setText(map.get("SERVERDESC"));
		setDirty(false);
	}

	@Override
	public void setFocus()
	{
		// TODO Auto-generated method stub
		getSite().getShell().setText("GOLP TOOL" + " " + "服务程序" + " " + getServerIdText1().getText()+ " " + "所属工程" + " " + upProject);
		//在导航视图中，将thisNode设为焦点
		setSelectNode(thisNode);
	}
	/**
	 * 根据传入的node，将其在导航视图中设为焦点
	 * @param node 想要设为焦点的ResourceLeafNode对象
	 */
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
	/**
	 * 根据传入的参数，弹出需要的对话框
	 * @param style 对话框的风格
	 * @param title 对话框的标题
	 * @param message 对话框的内容
	 * @return 用户在对话框中所点击的按钮的值
	 */
	@Override
	public int showMessage(int style,String title,String message)
	{
		MessageBox box = new MessageBox(getSite().getShell(),
				style);
		box.setText(title);
		box.setMessage(message);
		return box.open();
	}
	/**
	 * 实现了保存的操作
	 */
	@Override
	public void mySave()
	{
		if(serverNameText1.getText().isEmpty())
		{
			showMessage(SWT.ICON_WARNING | SWT.YES,"警告","服务程序名称不能为空！");
			return;
		}
		if(serverDescText.getText().isEmpty())
		{
			showMessage(SWT.ICON_WARNING | SWT.YES,"警告","描述不能为空！");
			return;
		}
		if ((serverSpeclibPathText.getText().isEmpty() && serverSpeclibNameText
				.getText().isEmpty())
				|| (!serverSpeclibPathText.getText().isEmpty() && !serverSpeclibNameText
						.getText().isEmpty()))
		{
			List<String> dataList = new ArrayList<String>();
			dataList.add(serverNameText1.getText());
			dataList.add(serverDescText.getText());
			String serverSpeclib = "";
			if (!serverSpeclibPathText.getText().isEmpty())
			{
				serverSpeclib = "[" + serverSpeclibPathText.getText()
						+ "]" + "[" + serverSpeclibNameText.getText()
						+ "]";
			}
			dataList.add(serverSpeclib);
			dataList.add(serverSpecIncludePathText.getText());
			dataList.add(callBackSourceText.getText());
			dataList.add(othFunSourceText.getText());
			dataList.add(serverIdText1.getText());
			EditorsDaoServiceImpl editorsDaoServiceImpl = new EditorsDaoServiceImpl();
			try
			{
				editorsDaoServiceImpl.updateServerById(dataList, ps);
				map = editorsDaoServiceImpl.queryServerByIdOrName(serverIdText1.getText(), 0,ps);
			} catch (SQLException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			setDirty(false);
		} else
		{
			showMessage(SWT.ICON_WARNING | SWT.YES,"警告","个性依赖库路径和个性依赖库名称必须全部输入或全部为空！");
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
	
	//以下为实现ISearch接口中的方法	
	@Override
	public String getTargetId()
	{
		// TODO 自动生成的方法存根
		return serverIdText1.getText();
	}

	@Override
	public String getTargetName()
	{
		// TODO 自动生成的方法存根
		return serverNameText1.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map)
	{
		// TODO 自动生成的方法存根
		this.map = (HashMap)map;
	}

	@Override
	public PreferenceStore getPs()
	{
		// TODO 自动生成的方法存根
		return ps;
	}

//	@Override
//	public void setSaveButtonEnable(boolean b)
//	{
//		// TODO 自动生成的方法存根
//		saveButton.setEnabled(b);
//	}

	@Override
	public void setUnLockButtonText(String text)
	{
		// TODO 自动生成的方法存根
		unLockButton.setText(text);
	}

	@Override
	public void setThisNode(ResourceLeafNode node)
	{
		// TODO 自动生成的方法存根
		thisNode = node;
	}

	@Override
	public void setInputNode(ResourceLeafNode node)
	{
		// TODO 自动生成的方法存根
		input.setNode(node);
	}

	@Override
	public Search getSearch()
	{
		// TODO 自动生成的方法存根
		return search;
	}

	@Override
	public void setMyPartName(String name)
	{
		// TODO 自动生成的方法存根
		this.setPartName(name);
	}

	@Override
	public ResourceLeafNode getThisNode()
	{
		// TODO 自动生成的方法存根
		return thisNode;
	}
}
