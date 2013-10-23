package dev.wizards.newAop;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewAopWizardPage0 extends WizardPage
{
	private Combo upProjectCombo;
	private Combo aopLvlCombo;
	private Text aopIdText;
	private Text aopNameText;
	private Text aopDescText;

	private Text preCondition;
	private Text postCondition;
	private Text aopErrRecover;
	private Text aopExts;
	private Text aopUpDll;
	private ISelection selection;

	public NewAopWizardPage0(ISelection selection)
	{
		super("NewAopWizardPage0");
		setTitle("新建原子交易向导");
		setDescription("这个向导将指导你完成GOLP原子交易的创建");
		this.selection = selection;
	}
	
	public Combo getUpProjectCombo() {
		return upProjectCombo;
	}

	public Combo getAopLvlCombo() {
		return aopLvlCombo;
	}
	public Text getAopIdText()
	{
		return aopIdText;
	}

	public Text getAopNameText()
	{
		return aopNameText;
	}

	public Text getAopDescText()
	{
		return aopDescText;
	}

	public Text getPreCondition() {
		return preCondition;
	}

	public Text getPostCondition() {
		return postCondition;
	}

	public Text getAopErrRecover() {
		return aopErrRecover;
	}

	public Text getAopExts() {
		return aopExts;
	}

	public Text getAopUpDll() {
		return aopUpDll;
	}


	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label upProjectLabel = new Label(container, SWT.NONE);
		upProjectLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		upProjectLabel.setText("*所属工程：");
		upProjectCombo = new Combo(container, SWT.READ_ONLY);
		upProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		upProjectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label aopLvlLabel = new Label(container, SWT.NONE);
		aopLvlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		aopLvlLabel.setText("*AOP级别：");
		aopLvlCombo = new Combo(container, SWT.READ_ONLY);
		aopLvlCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		aopLvlCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		aopLvlCombo.setItems(new String[]{"App"});
		
		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		prjIdLabel.setText("*原子交易标识符：");

		aopIdText = new Text(container, SWT.BORDER);
		aopIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		aopIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,false, 1, 1));
		prjNameLabel.setText("*原子交易名称：");

		aopNameText = new Text(container, SWT.BORDER);
		aopNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,false, 1, 1));
		aopNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		prjDescLabel.setText("*描述：");

		aopDescText = new Text(container, SWT.BORDER);
		aopDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		aopDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initData();
	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		if (getAopLvlCombo().getText().length() == 0
				|| getUpProjectCombo().getText().length() == 0
				||getAopIdText().getText().length() == 0
				|| getAopNameText().getText().length() == 0
				|| getAopDescText().getText().length() == 0)
			return false;
		return true;
	}

	private void initData() {
		// 初始化所属工程内容
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject[] projects = prjRoot.getProjects();
		for (IProject iProject : projects) {
			upProjectCombo.add(iProject.getName());
		}
	}
}
