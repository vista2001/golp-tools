package dev.wizards.newTrade;

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

public class NewTradeWizardPage0 extends WizardPage{
	private Combo tradeUpProjectCombo;
	private Combo TradeLvlCombo;
	private Text tradeIdText;
	private Text tradeNameText;
	private Text tradeDescText;
	private ISelection selection;

	public NewTradeWizardPage0(ISelection selection) {
		super("wizardPage");
		setTitle("新建交易向导");
		setDescription("这个向导将指导你完成GOLP交易的创建");
		this.selection = selection;
	}

	public Combo getTradeUpProjectCombo() {
		return tradeUpProjectCombo;
	}

	public Combo getTradeLvlCombo() {
		return TradeLvlCombo;
	}

	public Text getTradeIdText() {
	return tradeIdText;
	}


	public Text getTradeNameText() {
		return tradeNameText;
	}


	public Text getTradeDescText() {
		return tradeDescText;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label tradeUpProjectLabel = new Label(container, SWT.NONE);
		tradeUpProjectLabel.setText("*所属工程");
		tradeUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		tradeUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label aopLvlLabel = new Label(container, SWT.NONE);
		aopLvlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		aopLvlLabel.setText("*Trade级别：");
		TradeLvlCombo = new Combo(container, SWT.READ_ONLY);
		TradeLvlCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		TradeLvlCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		TradeLvlCombo.setItems(new String[]{"App"});
		
		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjIdLabel.setText("*交易标识符");
		
		tradeIdText = new Text(container, SWT.BORDER);
		tradeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		tradeIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*交易名称");
		
		tradeNameText = new Text(container, SWT.BORDER);
		tradeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		tradeNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjDescLabel.setText("*描述");
		
		tradeDescText = new Text(container, SWT.BORDER);
		tradeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tradeDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		initData();
	}

	// 输入项校验
	private void dialogChanged() {
		/*if (getPrjDescText().getText().isEmpty()||getPrjIdText().getText().isEmpty()||getPrjNameText().getText().isEmpty()) {
			updateStatus("File container must be specified");
			return;
		}*/
		updateStatus(null);
	}

	// 更新状态
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public boolean canFlipToNextPage() {
		if(getTradeDescText().getText().isEmpty()||getTradeIdText().getText().isEmpty()||getTradeNameText().getText().isEmpty())
			return false;
		return true;
	}

	private void initData() {
		// 初始化所属工程内容
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot prjRoot = workspace.getRoot();
		IProject[] projects = prjRoot.getProjects();
		for (IProject iProject : projects) {
			tradeUpProjectCombo.add(iProject.getName());
		}
	}
}
