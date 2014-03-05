/* �ļ�����       RetcodeEditor.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.29
 * �޸����ݣ�   1.��retCodeDesc�����Ը���ΪSWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI��
 *         2.�޸�init�����е�setPartName�����Ӷ��������̵���ʾ�������ͽ���˴ӵ����д�һ���༭���󣬵���ñ༭��
 *         �Ĺرհ�ťʱ���༭����δ�رգ������ڱ༭���ı�������ʾ�˸�������ݣ������������ԭ������init������setFocus
 *         �����ڵ���setPartNameʱ������һ�£�
 *         3.�޸��˲��ֲ��֣�
 *         4.���Ǹ����е�dispose������ʹ�������б༭�����ر�ʱ���ָ����ߵı��⡰GOLP TOOL����
 *         5.�ڽ��м���APP/GOLP�����ж�ʱ������ֱ��ʹ��0��1�����֣���Ϊʹ��dev.util.Constants��ĳ�����
 *         6.��UI��ͳһʹ��0-APP��1-GOLP���ֱ�ʾ��
 *         7.��DebugOut.println�����滻System.out.println������
 *         8.���ӱ༭���Ĺ������ܣ�
 *         9.ͳһʹ��File.separator��
 * �޸��ˣ�       zxh
 * �޸�ʱ�䣺   2013.12.2
 * �޸����ݣ�   1.�޸ı���������
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
 * Retcode��༭����
 * <p>
 * �����̳���EditorPart�࣬��RetcodeEditorInput��һ�����Retcode�༭���Ĺ���<br>
 * �ڱ༭����ʼ����ʱ��ͨ��RetcodeEditorInput�ഫ���������Init�����жԱ༭��
 * ���г�ʼ����Ȼ����createPartControl��������ɶԱ༭���Ŀؼ��Ĺ��죬������
 * �ÿؼ�����Ҫ�ǡ���ѯ�������������޸ġ���ť������Ϊ���ı�����������ƣ���createPartControl
 * ������������dataInit������ɴ����ݿ��л�����ݲ������ı����У���ɶ����� �༭���ĳ�ʼ����
 * 
 * @see#init
 * @see#createPartControl
 * @see#datainit
 * @see#setFocus
 * */
public class RetCodeEditor extends EditorPart implements ISearch, IGetUpProject {
	public static final String ID = "dev.editor.retcode.RetCodeEditor"; // //�༭����ı�ʶ
	private Text retCodeIdText; // ��Ӧ���ʶ�ı���
	private Text retCodeValueText; // ��Ӧ��ֵ�ı���
	private Text retCodeDescText; // ��Ӧ��˵���ı���
	private Text upProjectText; // ��������������ı���
	private Text retCodeLevelText; // ��Ӧ�뼶���ı���
	private RetCodeEditorInput input; // inpiu����
	private Button saveButton; // �޸İ�ť
	private Button unlockButton; // �༭��ť
	private Button restoreButton;
	private EditorRetcodeServiceImpl impl; // ���ݿ���������
	private Map<String, String> map; // �洢��ѯ�����ݵ�Map
	private Map<String, String> restoreMap;
	private PreferenceStore ps; // ���ݿ�������Ϣ
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
	 * ���ڶԱ༭�����г�ʼ��<br>
	 * ����site,input����Ա༭�����г�ʼ�����򿪱༭����ʱ�����ȵ��õķ���
	 * 
	 * @param site
	 *            �༭����site
	 * @param input
	 *            �༭�����׵�input
	 * @return û�з���ֵ
	 * */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.setSite(site); // ����site
		this.setInput(input); // ����input
		this.input = (RetCodeEditorInput) input; // ��Input��ʼ��
		this.setPartName("��Ӧ��" + " " + this.input.getName() + " " + "��������"
				+ " " + this.input.getSource().getRootProject().getId()); // ���ñ༭������
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
	 * �ڱ༭���ϴ������ֿռ䣬���ò��֣��Լ��Կռ���г�ʼ��<br>
	 * ����Ҫ�Ŀؼ�������parent����пؼ�����Ϊ������������ﱻ�趨����������ڵ�����Init�󱳵���
	 * 
	 * @param parent
	 *            ���пؼ���parent.
	 * @return û�з���ֵ
	 * */
	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent,
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		// �����༭����Ŀؼ�
		Group retcodeSearchGroup = new Group(composite, SWT.NONE);
		retcodeSearchGroup.setText("��Ӧ���ѯ");
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
		retcodeGroup.setText("��Ӧ���");
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
		upProjectLabel.setText("��������");

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
		retCodeLevelLabel.setText("��Ӧ�뼶��");

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
		retCodeIdLabel.setText("��Ӧ���ʶ");

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
		retCodeValueLabel.setText("*��Ӧ��ֵ");

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
		retCodeDescLabel.setText("*����");

		retCodeDescText = new Text(retcodeGroup, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		retCodeDescText.setEnabled(false);
		GridData gd_retCodeDesc = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 5, 3);
		gd_retCodeDesc.heightHint = 83;
		retCodeDescText.setLayoutData(gd_retCodeDesc);
		// �����ı������������
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
		unlockButton.setText("�༭");
		/*
		 * Ϊ����������ť������Ϊ�������°�ťʱ������ťΪ�������������Ϊ��������������
		 * �༭�Ŀؼ�����Ϊ���ã������޸ġ���ť����Ϊ���ã������Ϊ�����������Ա༭�� �ؼ�����Ϊ�����ã����޸İ�ť����Ϊ�����á�
		 */
		unlockButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Button nButton = ((Button) e.getSource());
				if (nButton.getText() == "�༭") {
					nButton.setText("����");
					retCodeValueText.setEnabled(true);
					retCodeDescText.setEnabled(true);
					// saveButton.setEnabled(true);
				} else {
					nButton.setText("�༭");
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
		saveButton.setText("����");
		saveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// super.widgetSelected(e);
				saveData();
			}
		});
		/*
		 * Ϊ���޸ġ���ť������Ϊ�������޸İ�ť������Ӧ��ֵ�ı���Ϊ�գ��򵯳�����Ի���
		 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorretCodeServiceImpl���
		 * updateretCodeById������List��������滻��ʶΪAopId �����ݡ�
		 */

		restoreButton = new Button(retcodeGroup, SWT.NONE);
		GridData gd_restoreBtn = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_restoreBtn.widthHint = 80;
		restoreButton.setLayoutData(gd_restoreBtn);
		restoreButton.setText("�ָ�");
		// ���ûָ���ť��Ϊ
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
	 * ���༭��ѡ�е�ʱ�����<br>
	 * ���༭���Ĵ��ڱ�ѡ��ʱ���Զ��������������ִ�з����е����
	 * 
	 * @return û�з���ֵ
	 * */
	@Override
	public void setFocus() {
		getSite().getShell().setText(
				"GOLP TOOL" + " " + "��Ӧ��" + " " + input.getName() + " "
						+ "��������" + " " + upProjectText.getText());
		setPartName("��Ӧ��" + " " + input.getName() + " " + "��������" + " "
				+ upProjectText.getText());
		setSelectNode(input.getSource());
	}

	/**
	 * �Ա༭���Ŀؼ����г�ʼ��<br>
	 * �����ҵ����ݿ��������Ϣ��Ȼ�����name��������Ϣ����EditorAopServiceImpl��
	 * ��queryAopByIdOrName�����õ���ʼ�������ݷ���Map����һ������ؼ��С�
	 * 
	 * @param name
	 *            Ҫ��ѯ�����ݵı�ʶ
	 * @return û�з���ֵ
	 * */
	private void datainit(String name) throws SQLException {
		// ������ݿ��������Ϣ
		ResourceLeafNode rln = ((RetCodeEditorInput) input).getSource();
		String prjId = rln.getRootProject().getId();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject project = prjRoot.getProject(prjId);
		String dbfiles = project.getLocationURI().toString().substring(6)
				+ File.separator + prjId + ".properties";
		DevLogger.printDebugMsg("dbfiles===" + dbfiles);
		// �����ݿ���в�ѯ������Map
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
		// ���θ��ؼ���ֵ
		retCodeIdText.setText(map.get("ID"));
		retCodeValueText.setText(map.get("NAME"));
		retCodeDescText.setText(map.get("retcodedesc"));
		// ��aoplevelΪConstants.APP������Ϊ��APP������������Ϊ��GOLP���������������롰�޸ġ���ť��Ϊ���ɼ�
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
	 * �������ݵķ��� ���ڱ��水ť��doSave�����ı�������������޸İ�ť����ԭ�ӽ��������ı���Ϊ�գ��򵯳�����Ի���
	 * ���򽫿ɱ༭���ı���������ݷ���һ��List�е���EditorRetcodeServiceImpl���updateRetcodeById������
	 * List��������滻��ʶΪRetcodeId�����ݡ�
	 * 
	 * @throws SQLException
	 */
	private void saveData() {
		if (RegExpCheck.isRetCodeValue(retCodeValueText.getText()) == false) {
			DevLogger.showMessage(SWT.ICON_WARNING | SWT.YES, "����", "��Ӧ��ֵΪ5λ����");
		} else if (retCodeDescText.getText().trim().isEmpty()) {
			DevLogger.showMessage(SWT.ICON_WARNING | SWT.YES, "����", "�������Ϊ��");
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
		// �ñ༭�����رպ󣬻ָ����ߵı���
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
		 * �ж�aoplevel�Ƿ�ΪConstants.APP����������Ϊ"AOP"�������������ڡ��޸ġ���ť����Ϊ�ɼ���
		 * ����Ϊ"GOLP",�����������롰�޸ġ���ť����Ϊ���ɼ�
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
			unlockButton.setText("�༭");
			retCodeValueText.setEnabled(false);
			retCodeDescText.setEnabled(false);
		} else {
			unlockButton.setText("����");
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
