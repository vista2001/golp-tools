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
		setTitle("�½���̬����");
		setDescription("����򵼽�ָ�������GOLP��̬��Ĵ���");
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
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dllIdLabel = new Label(container, SWT.NONE);
		dllIdLabel.setText("*��̬���ʶ����");

		dllIdText = new Text(container, SWT.BORDER);
		dllIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllNameLabel = new Label(container, SWT.NONE);
		dllNameLabel.setText("*��̬�����ƣ�");

		dllNameText = new Text(container, SWT.BORDER);
		dllNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dllDescLabel = new Label(container, SWT.NONE);
		dllDescLabel.setText("*������");

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

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		// TODO �Զ����ɵķ������
		if (getDllIdText().getText().length() == 0
				|| getDllNameText().getText().length() == 0
				|| getDllDescText().getText().length() == 0)
			return false;
		return true;
	}

}
