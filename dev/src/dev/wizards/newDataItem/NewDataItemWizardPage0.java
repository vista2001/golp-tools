package dev.wizards.newDataItem;

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

public class NewDataItemWizardPage0 extends WizardPage
{
	private Text dataItemIdText;
	private Text dataItemNameText;
	private Text dataItemDescText;
	private ISelection selection;

	public NewDataItemWizardPage0(ISelection selection)
	{
		super("NewDataItemWizardPage0");
		setTitle("新建数据项向导");
		setDescription("这个向导将指导你完成GOLP数据项的创建");
		this.selection = selection;
	}

	public Text getDataItemIdText()
	{
		return dataItemIdText;
	}

	public Text getDataItemNameText()
	{
		return dataItemNameText;
	}

	public Text getDataItemDescText()
	{
		return dataItemDescText;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO 自动生成的方法存根
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjIdLabel.setText("*数据项标识符：");

		dataItemIdText = new Text(container, SWT.BORDER);
		dataItemIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*数据项名称：");

		dataItemNameText = new Text(container, SWT.BORDER);
		dataItemNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		prjDescLabel.setText("*描述：");

		dataItemDescText = new Text(container, SWT.BORDER);
		dataItemDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		dataItemIdText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		dataItemNameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				dialogChanged();
			}
		});
		dataItemDescText.addModifyListener(new ModifyListener()
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
		if (getDataItemIdText().getText().length() == 0
				|| getDataItemNameText().getText().length() == 0
				|| getDataItemDescText().getText().length() == 0)
			return false;
		return true;
	}

}
