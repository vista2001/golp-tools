package dev.wizards.newRetCode;

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

public class NewRetCodeWizardPage0 extends WizardPage
{
	private Text retCodeIdText;
	private Text retCodeNameText;
	private Text retCodeDescText;
	private ISelection selection;

	public NewRetCodeWizardPage0(ISelection selection)
	{
		super("NewRetCodeWizardPage0");
		setTitle("新建响应码向导");
		setDescription("这个向导将指导你完成GOLP响应码的创建");
		this.selection = selection;
	}


	public Text getRetCodeIdText() {
		return retCodeIdText;
	}


	public Text getRetCodeNameText() {
		return retCodeNameText;
	}


	public Text getRetCodeDescText() {
		return retCodeDescText;
	}


	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label retCodeIdLabel = new Label(container, SWT.NONE);
		retCodeIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		retCodeIdLabel.setText("*响应码标识符：");

		retCodeIdText = new Text(container, SWT.BORDER);
		retCodeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label retCodeNameLabel = new Label(container, SWT.NONE);
		retCodeNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeNameLabel.setText("*响应码名称：");

		retCodeNameText = new Text(container, SWT.BORDER);
		retCodeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label retCodeDescLabel = new Label(container, SWT.NONE);
		retCodeDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		retCodeDescLabel.setText("*描述：");

		retCodeDescText = new Text(container, SWT.BORDER);
		retCodeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		retCodeIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		retCodeNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		retCodeDescText.addModifyListener(new ModifyListener()
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
		// TODO 自动生成的方法存根
		if (getRetCodeIdText().getText().length() == 0
				|| getRetCodeNameText().getText().length() == 0
				|| getRetCodeDescText().getText().length() == 0)
			return false;
		return true;
	}

}
