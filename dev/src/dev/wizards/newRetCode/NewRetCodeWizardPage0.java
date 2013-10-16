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
		setTitle("�½���Ӧ����");
		setDescription("����򵼽�ָ�������GOLP��Ӧ��Ĵ���");
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
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label retCodeIdLabel = new Label(container, SWT.NONE);
		retCodeIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		retCodeIdLabel.setText("*��Ӧ���ʶ����");

		retCodeIdText = new Text(container, SWT.BORDER);
		retCodeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label retCodeNameLabel = new Label(container, SWT.NONE);
		retCodeNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		retCodeNameLabel.setText("*��Ӧ�����ƣ�");

		retCodeNameText = new Text(container, SWT.BORDER);
		retCodeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label retCodeDescLabel = new Label(container, SWT.NONE);
		retCodeDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,
				false, 1, 1));
		retCodeDescLabel.setText("*������");

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

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged()
	{
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage()
	{
		// TODO �Զ����ɵķ������
		if (getRetCodeIdText().getText().length() == 0
				|| getRetCodeNameText().getText().length() == 0
				|| getRetCodeDescText().getText().length() == 0)
			return false;
		return true;
	}

}
