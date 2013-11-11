package dev.wizards.newDataItem;

import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.util.RegExpCheck;
/**
 * ���ඨ�����½��������򵼵� ��2ҳ
 */
public class NewDataItemWizardPage1 extends WizardPage
{

	private ISelection selection;
	private Combo dataItemTypeCombo;
	private Text dataItemlenText;
	private Text dataItemAOPText;
	//private Combo dataItemLenFixedCombo;

	public Combo getDataItemTypeCombo()
	{
		return dataItemTypeCombo;
	}

//	public Combo getDataItemLenFixedCombo()
//	{
//		return dataItemLenFixedCombo;
//	}

	public Text getDataItemlenText()
	{
		return dataItemlenText;
	}

	public Text getDataItemAOPText()
	{
		return dataItemAOPText;
	}

	public NewDataItemWizardPage1(ISelection selection)
	{
		super("NewDataItemWizardPage0");
		setTitle("�½���������");
		setDescription("����򵼽�ָ�������GOLP������Ĵ���");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent)
	{
		// TODO �Զ����ɵķ������
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemTypeLabel = new Label(container, SWT.NONE);
		dataItemTypeLabel.setText("*��������:");

		dataItemTypeCombo = new Combo(container, SWT.READ_ONLY);
		dataItemTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		dataItemTypeCombo.add("1-short");
		dataItemTypeCombo.add("2-long");
		dataItemTypeCombo.add("3-float");
		dataItemTypeCombo.add("4-double");
		dataItemTypeCombo.add("5-char");
		dataItemTypeCombo.add("6-String");
		
//		Label dataItemLenFixedLabel = new Label(container, SWT.NONE);
//		dataItemLenFixedLabel.setText("*�����Ƿ񶨳���");
		
//		dataItemLenFixedCombo = new Combo(container, SWT.READ_ONLY);
//		dataItemLenFixedCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
//		dataItemLenFixedCombo.add("0-��");
//		dataItemLenFixedCombo.add("1-��");

		Label dataItemlenLabel = new Label(container, SWT.NONE);
		dataItemlenLabel.setText("*������ȣ�");

		dataItemlenText = new Text(container, SWT.BORDER);
		dataItemlenText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dataItemlenText.setEnabled(false);

		Label dataItemAOPLabel = new Label(container, SWT.NONE);
		dataItemAOPLabel.setText("����Լ����麯����");

		dataItemAOPText = new Text(container, SWT.BORDER);
		dataItemAOPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		dataItemTypeCombo.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(    dataItemTypeCombo.getText().equals("5-char")
					|| dataItemTypeCombo.getText().equals("6-String"))
				{
					dataItemlenText.setEnabled(true);
				}
				else
				{
					dataItemlenText.setEnabled(false);
					dataItemlenText.setText("");
					setErrorMessage(null);
					
				}
				dialogChanged();
			}
		});

//		dataItemLenFixedCombo.addModifyListener(new ModifyListener()
//		{
//			public void modifyText(ModifyEvent e)
//			{
//				dialogChanged();
//			}
//		});
		
		dataItemlenText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				if(dataItemlenText.isEnabled())
				{
					if(RegExpCheck.isNum(dataItemlenText.getText()))
					{
						setErrorMessage(null);
					}
					else
					{
						setErrorMessage("�������Ϊ������");
					}
					dialogChanged();
				}
				
			}
		});

	}

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged()
	{
		setPageComplete(true);
	}
	
	public boolean validInput()
	{
		if(dataItemlenText.isEnabled())
		{
			if (       getDataItemTypeCombo().getText().length() == 0
					||!(RegExpCheck.isNum(getDataItemlenText().getText())))
				return false;
			return true;
		}
		else
		{
			if (getDataItemTypeCombo().getText().length() == 0)
				return false;
			return true;
		}

	}

	@Override
	public boolean canFlipToNextPage()
	{
		// TODO �Զ����ɵķ������
		return false;
	}

}
