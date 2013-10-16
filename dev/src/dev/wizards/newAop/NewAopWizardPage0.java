package dev.wizards.newAop;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewAopWizardPage0 extends WizardPage
{
	private Text aopIdText;
	private Text aopNameText;
	private Text aopDescText;

	private Text preCondition;
	private Text postCondition;
	private Text aopErrRecover;
	private Text aopExts;
	private Text aopUpDll;
	private Text aopUpProject;
	private Text aopLvL;
	private ISelection selection;

	public NewAopWizardPage0(ISelection selection)
	{
		super("NewAopWizardPage0");
		setTitle("新建原子交易向导");
		setDescription("这个向导将指导你完成GOLP原子交易的创建");
		this.selection = selection;
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

	public Text getAopUpProject() {
		return aopUpProject;
	}

	public Text getAopLvL() {
		return aopLvL;
	}

	@Override
	public void createControl(Composite parent)
	{
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjIdLabel.setText("*原子交易标识符：");

		aopIdText = new Text(container, SWT.BORDER);
		aopIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*原子交易名称：");

		aopNameText = new Text(container, SWT.BORDER);
		aopNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjDescLabel.setText("*描述：");

		aopDescText = new Text(container, SWT.BORDER);
		aopDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		aopIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		aopNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		aopDescText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		if (getAopIdText().getText().length() == 0
				|| getAopNameText().getText().length() == 0
				|| getAopDescText().getText().length() == 0)
			return false;
		return true;
	}

}
