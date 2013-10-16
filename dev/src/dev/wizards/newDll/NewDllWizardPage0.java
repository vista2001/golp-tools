package dev.wizards.newDll;

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

public class NewDllWizardPage0 extends WizardPage
{
	private Text dllIdText;
	private Text dllNameText;
	private Text dllDescText;
	private ISelection selection;

	public NewDllWizardPage0(ISelection selection)
	{
		super("NewDllWizardPage0");
		setTitle("新建动态库向导");
		setDescription("这个向导将指导你完成GOLP动态库的创建");
		this.selection = selection;
	}

	public Text getDllIdText()
	{
		return dllIdText;
	}

	public Text getDllNameText()
	{
		return dllNameText;
	}

	public Text getDllDescText()
	{
		return dllDescText;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dllIdLabel = new Label(container, SWT.NONE);
		dllIdLabel.setText("*动态库标识符：");

		dllIdText = new Text(container, SWT.BORDER);
		dllIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllNameLabel = new Label(container, SWT.NONE);
		dllNameLabel.setText("*动态库名称：");

		dllNameText = new Text(container, SWT.BORDER);
		dllNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllDescLabel = new Label(container, SWT.NONE);
		dllDescLabel.setText("*描述：");

		dllDescText = new Text(container, SWT.BORDER);
		dllDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		dllIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		dllNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		dllDescText.addModifyListener(new ModifyListener()
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
		if (getDllIdText().getText().length() == 0
				|| getDllNameText().getText().length() == 0
				|| getDllDescText().getText().length() == 0)
			return false;
		return true;
	}

}
