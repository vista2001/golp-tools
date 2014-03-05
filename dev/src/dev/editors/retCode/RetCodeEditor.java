/* 文件名：       RetcodeEditor.java
 * 修改人：       rxy
 * 修改时间：   2013.11.29
 * 修改内容：   1.将retCodeDesc的属性更改为SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI；
 *         2.修改init方法中的setPartName，增加对所属工程的显示，这样就解决了从导航中打开一个编辑器后，点击该编辑器
 *         的关闭按钮时，编辑器并未关闭，而是在编辑器的标题中显示了更多的内容，产生该问题的原因在于init方法和setFocus
 *         方法在调用setPartName时参数不一致；
 *         3.修改了部分布局；
 *         4.覆盖父类中的dispose方法，使得在所有编辑器都关闭时，恢复工具的标题“GOLP TOOL”；
 *         5.在进行级别（APP/GOLP）的判断时，不再直接使用0、1等数字，改为使用dev.util.Constants类的常量；
 *         6.在UI中统一使用0-APP、1-GOLP这种表示；
 *         7.用DebugOut.println方法替换System.out.println方法；
 *         8.增加编辑器的滚动功能；
 *         9.统一使用File.separator。
 * 修改人：       zxh
 * 修改时间：   2013.12.2
 * 修改内容：   1.修改变量命名。
 */

package dev.editors.retCode;

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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import dev.db.service.EditorRetcodeServiceImpl;
import dev.editors.IGetUpProject;
import dev.editors.ISearch;
import dev.editors.Search;
import dev.model.base.ResourceLeafNode;
import dev.util.Constants;
import dev.util.DevLogger;
import dev.util.RegExpCheck;
import dev.views.NavView;

/**
 * Retcode表编辑器类
 * <p>
 * 这个类继承了EditorPart类，与RetcodeEditorInput类一起完成Retcode编辑器的功能<br>
 * 在编辑器初始化的时候，通过RetcodeEditorInput类传入的数据在Init方法中对编辑器
 * 进行初始化，然后在createPartControl方法中完成对编辑器的控件的构造，包括设
 * 置控件（主要是“查询”“解锁”“修改”按钮）的行为和文本框的输入限制，在createPartControl
 * 方法的最后调用dataInit方法完成从数据库中获得数据并填入文本框中，完成对整个 编辑器的初始化。
 * 
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class RetCodeEditor extends EditorPart implements ISearch, IGetUpProject {
	public static final String ID = "dev.editor.retcode.RetCodeEditor"; // //编辑器类的标识
	private Text retCodeIdText; // 响应码标识文本框
	private Text retCodeValueText; // 响应码值文本框
	private Text retCodeDescText; // 响应码说明文本框
	private Text upProjectText; // 表项部分所属工程文本框
	private Text retCodeLevelText; // 响应码级别文本框
	private RetCodeEditorInput input; // inpiu对象
	private Button saveButton; // 修改按钮
	private Button unlockButton; // 编辑按钮
	private Button restoreButton;
	private EditorRetcodeServiceImpl impl; // 数据库操作类对象
	private Map<String, String> map; // 存储查询到数据的Map
	private Map<String, String> restoreMap;
	private PreferenceStore ps; // 数据库配置信息
	private boolean bDirty = false;
	private Search search;

	public RetCodeEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		saveData();
	}

	@Override
	public void doSaveAs() {

	}

	/**
	 * 用于对编辑器进行初始化<br>
	 * 根据site,input对象对编辑器进行初始化，打开编辑器的时候首先调用的方法
	 * 
	 * @param site
	 *            编辑器的site
	 * @param input
	 *            编辑器配套的input
	 * @return 没有返回值
	 * */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site); // 设置site
		this.setInput(input); // 设置input
		this.input = (RetCodeEditorInput) input; // 对Input初始化
		this.setPartName("响应码" + " " + this.input.getName() + " " + "所属工程"
				+ " " + this.input.getSource().getRootProject().getId()); // 设置编辑器标题
	}

	@Override
	public boolean isDirty() {
		return bDirty;
	}

	public void setDirty(boolean b) {
		bDirty = b;
		saveButton.setEnabled(b);
		restoreButton.setEnabled(b);
		if (b)
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
	 * 
	 * @param parent
	 *            所有控件的parent.
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
		// 创建编辑器里的控件
		Group retcodeSearchGroup = new Group(composite, SWT.NONE);
		retcodeSearchGroup.setText("响应码查询");
		retcodeSearchGroup.setLayout(new GridLayout(7, false));
		retcodeSearchGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				false, false, 1, 1));

		search = new Search();
		search.setUpProject(input.getSource().getRootProject().getId());
		search.setEditorId(ID);
		search.setEditor(this);
		search.createPartControl(retcodeSearchGroup);

		Group retcodeGroup = new Group(composite, SWT.NONE);
		GridData gd_retcodeGroup = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
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
		GridData gd_lblNewLabel_1 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1);
		gd_lblNewLabel_1.widthHint = 80;
		lblNewLabel_1.setLayoutData(gd_lblNewLabel_1);

		Label upProjectLabel = new Label(retcodeGroup, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		upProjectLabel.setText("所属工程");

		upProjectText = new Text(retcodeGroup, SWT.BORDER);
		upProjectText.setEnabled(false);
		GridData gd_upProject = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_upProject.widthHint = 70;
		upProjectText.setLayoutData(gd_upProject);

		Label lblNewLabel = new Label(retcodeGroup, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 2, 1);
		gd_lblNewLabel.widthHint = 80;
		lblNewLabel.setLayoutData(gd_lblNewLabel);

		Label retCodeLevelLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeLevelLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		retCodeLevelLabel.setText("响应码级别");

		retCodeLevelText = new Text(retcodeGroup, SWT.BORDER);
		retCodeLevelText.setEnabled(false);
		GridData gd_retCodeLevel = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_retCodeLevel.widthHint = 70;
		retCodeLevelText.setLayoutData(gd_retCodeLevel);
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
		retCodeIdLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		retCodeIdLabel.setText("响应码标识");

		retCodeIdText = new Text(retcodeGroup, SWT.BORDER);
		retCodeIdText.setEnabled(false);
		GridData gd_retCodeId = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_retCodeId.widthHint = 70;
		retCodeIdText.setLayoutData(gd_retCodeId);
		new Label(retcodeGroup, SWT.NONE);
		new Label(retcodeGroup, SWT.NONE);

		Label retCodeValueLabel = new Label(retcodeGroup, SWT.NONE);
		retCodeValueLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		retCodeValueLabel.setText("*响应码值");

		retCodeValueText = new Text(retcodeGroup, SWT.BORDER);
		retCodeValueText.setEnabled(false);
		retCodeValueText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!isDirty()) {
					setDirty(true);
				}
			}
		});
		GridData gd_retCodeValue = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_retCodeValue.widthHint = 70;
		retCodeValueText.setLayoutData(gd_retCodeValue);

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
		retCodeDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		retCodeDescLabel.setText("*描述");

		retCodeDescText = new Text(retcodeGroup, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		retCodeDescText.setEnabled(false);
		GridData gd_retCodeDesc = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 5, 3);
		gd_retCodeDesc.heightHint = 83;
		retCodeDescText.setLayoutData(gd_retCodeDesc);
		// 设置文本框的输入限制
		// retCodeDescText.addVerifyListener(new VerifyListener()
		// {
		// public void verifyText(VerifyEvent event)
		// {
		// if (event.character != 8)
		// event.doit = retCodeDescText.getText().length() <= 32;
		// }
		// });
		retCodeDescText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!isDirty()) {
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

		unlockButton = new Button(retcodeGroup, SWT.NONE);
		GridData gd_clearBtn = new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1);
		gd_clearBtn.widthHint = 80;
		unlockButton.setLayoutData(gd_clearBtn);
		unlockButton.setText("编辑");
		/*
		 * 为“解锁”按钮设置行为，当按下按钮时，若按钮为“解锁”，则改为锁定，并将可以
		 * 编辑的控件设置为可用，将“修改”按钮设置为可用；否则改为解锁，将可以编辑的 控件设置为不可用，将修改按钮设置为不可用。
		 */
		unlockButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Button nButton = ((Button) e.getSource());
				if (nButton.getText() == "编辑") {
					nButton.setText("锁定");
					retCodeValueText.setEnabled(true);
					retCodeDescText.setEnabled(true);
					// saveButton.setEnabled(true);
				} else {
					nButton.setText("编辑");
					retCodeValueText.setEnabled(false);
					retCodeDescText.setEnabled(false);
					// saveButton.setEnabled(false);
				}
			}
		});
		new Label(retcodeGroup, SWT.NONE);

		saveButton = new Button(retcodeGroup, SWT.NONE);
		GridData gd_savaBtn = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_savaBtn.widthHint = 80;
		saveButton.setLayoutData(gd_savaBtn);
		saveButton.setText("保存");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// super.widgetSelected(e);
				saveData();
			}
		});
		/*
		 * 为“修改”按钮设置行为，按下修改按钮，若响应码值文本框为空，则弹出警告对话框，
		 * 否则将可编辑的文本框里的数据放入一个List中调用EditorretCodeServiceImpl类的
		 * updateretCodeById方法将List里的数据替换标识为AopId 的数据。
		 */

		restoreButton = new Button(retcodeGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreBtn);
		restoreButton.setText("恢复");
		// 设置恢复按钮行为
		restoreButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// super.widgetSelected(e);
				retCodeValueText.setText(restoreMap.get("NAME"));
				retCodeDescText.setText(restoreMap.get("retcodedesc"));
				setDirty(false);
			}
		});
		new Label(retcodeGroup, SWT.NONE);

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
	 * 
	 * @return 没有返回值
	 * */
	@Override
	public void setFocus() {
		getSite().getShell().setText(
				"GOLP TOOL" + " " + "响应码" + " " + input.getName() + " "
						+ "所属工程" + " " + upProjectText.getText());
		setPartName("响应码" + " " + input.getName() + " " + "所属工程" + " "
				+ upProjectText.getText());
		setSelectNode(input.getSource());
	}

	/**
	 * 对编辑器的控件进行初始化<br>
	 * 首先找到数据库的配置信息，然后根据name与配置信息调用EditorAopServiceImpl类
	 * 的queryAopByIdOrName方法得到初始化的数据放入Map，再一次填入控件中。
	 * 
	 * @param name
	 *            要查询的数据的标识
	 * @return 没有返回值
	 * */
	private void datainit(String name) throws SQLException {
		// 获得数据库的配置信息
		ResourceLeafNode rln = ((RetCodeEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6)
				+ File.separator + prjId + ".properties";
		DevLogger.printDebugMsg("dbfiles===" + dbfiles);
		// 对数据库进行查询，放入Map
		ps = new PreferenceStore(dbfiles);
		try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		impl = new EditorRetcodeServiceImpl();
		map = impl.queryRetcodeByIdOrName(name, "", ps);
		restoreMap = map;
		// 依次给控件赋值
		retCodeIdText.setText(map.get("ID"));
		retCodeValueText.setText(map.get("NAME"));
		retCodeDescText.setText(map.get("retcodedesc"));
		// 若aoplevel为Constants.APP则设置为“APP”，否则设置为“GOLP”并将“解锁”与“修改”按钮设为不可见
		if (map.get("retcodelevel").equals(Constants.APP))
			retCodeLevelText.setText("0-APP");
		else {
			retCodeLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
		}
		upProjectText.setText(input.getSource().getRootProject().getId());
		// upProject1.setText(upProject.getText());
		setDirty(false);
	}

	/**
	 * 保存数据的方法 用于保存按钮和doSave方法的保存操作，按下修改按钮，若原子交易名称文本框为空，则弹出警告对话框，
	 * 否则将可编辑的文本框里的数据放入一个List中调用EditorRetcodeServiceImpl类的updateRetcodeById方法将
	 * List里的数据替换标识为RetcodeId的数据。
	 * 
	 * @throws SQLException
	 */
	private void saveData() {
		if (RegExpCheck.isRetCodeValue(retCodeValueText.getText()) == false) {
			DevLogger.showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "响应码值为5位整数");
		} else if (retCodeDescText.getText().trim().isEmpty()) {
			DevLogger.showMessage(SWT.ICON_WARNING | SWT.YES, "警告", "必填项不能为空");
		} else {
			List<String> datalist = new ArrayList<String>();
			datalist.add(retCodeValueText.getText());
			datalist.add(retCodeDescText.getText());
			try {
				impl.updateRetcodeById(input.getName(), datalist, ps);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				restoreMap = impl.queryRetcodeByIdOrName(
						retCodeIdText.getText(), "", ps);
			} catch (SQLException e) {
				e.printStackTrace();
				DevLogger.printError(e);
			}
			setDirty(false);
			try {
				restoreMap = impl.queryRetcodeByIdOrName(
						retCodeIdText.getText(), "", ps);
			} catch (SQLException e) {
				e.printStackTrace();
				DevLogger.printError(e);
			}
		}
	}

	public void setSelectNode(ResourceLeafNode node) {
		IViewPart view = getSite().getWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		if (view != null) {
			NavView v = (NavView) view;
			TreeViewer tv = v.getTreeViewer();
			StructuredSelection s = new StructuredSelection(node);
			tv.setSelection(s, true);
		}
	}

	@Override
	public void dispose() {
		// 该编辑器被关闭后，恢复工具的标题
		if (getEditorSite().getPage().getActiveEditor() == null) {
			getSite().getShell().setText("GOLP TOOL");
		}
	}

	@Override
	public String getTargetId() {
		return retCodeIdText.getText();
	}

	@Override
	public String getTargetName() {
		return retCodeValueText.getText();
	}

	@Override
	public void setTargetMap(Map<String, String> map) {
		this.map = map;
		this.restoreMap = map;
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
	public Search getSearch() {
		return search;
	}

	@Override
	public void setControlsText() {
		retCodeIdText.setText(map.get("ID"));
		retCodeValueText.setText(map.get("NAME"));
		retCodeDescText.setText(map.get("retcodedesc"));
		/*
		 * 判断aoplevel是否为Constants.APP，是则设置为"AOP"，将“解锁”于“修改”按钮设置为可见，
		 * 否则为"GOLP",将“解锁”与“修改”按钮设置为不可见
		 */
		if (map.get("retcodelevel").equals(Constants.APP)) {
			saveButton.setVisible(true);
			unlockButton.setVisible(true);
			restoreButton.setVisible(true);
			retCodeLevelText.setText("0-APP");
		} else {
			retCodeLevelText.setText("1-GOLP");
			saveButton.setVisible(false);
			unlockButton.setVisible(false);
			restoreButton.setVisible(false);
		}
		input.setName(retCodeIdText.getText());
	}

	@Override
	public void setEditorPartName(String name) {
		setPartName(name);
	}

	@Override
	public void setEnable(boolean b) {
		if (!b) {
			unlockButton.setText("编辑");
			retCodeValueText.setEnabled(false);
			retCodeDescText.setEnabled(false);
		} else {
			unlockButton.setText("锁定");
			retCodeValueText.setEnabled(true);
			retCodeDescText.setEnabled(true);
		}
	}

	@Override
	public void save() {
		saveData();
	}

	@Override
	public String getUpProject() {
		return upProjectText.getText();
	}
}
