package dev.wizards.newTrade;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

public class NewTradeWizardPage0 extends WizardPage{
	private Text tradeIdText;
	private Text tradeNameText;
	private Text tradeDescText;
	private ISelection selection;

	public NewTradeWizardPage0(ISelection selection) {
		super("wizardPage");
		setTitle("�½�������");
		setDescription("����򵼽�ָ�������GOLP���׵Ĵ���");
		this.selection = selection;
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
	/**������ҳ��Ŀؼ�*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjIdLabel.setText("*���ױ�ʶ��");
		
		tradeIdText = new Text(container, SWT.BORDER);
		tradeIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*��������");
		
		tradeNameText = new Text(container, SWT.BORDER);
		tradeNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjDescLabel.setText("*����");
		
		tradeDescText = new Text(container, SWT.BORDER);
		tradeDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		tradeIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		tradeNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		tradeDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}

	// ������У��
	private void dialogChanged() {
		/*if (getPrjDescText().getText().isEmpty()||getPrjIdText().getText().isEmpty()||getPrjNameText().getText().isEmpty()) {
			updateStatus("File container must be specified");
			return;
		}*/
		updateStatus(null);
	}

	// ����״̬
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

}