package dev.wizards.newProject;

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

public class NewProjectWizardPage0 extends WizardPage{
	private Text prjIdText;
	private Text prjNameText;
	private Text prjDescText;
	private ISelection selection;

	public NewProjectWizardPage0(ISelection selection) {
		super("wizardPage");
		setTitle("�½�������");
		setDescription("����򵼽�ָ�������GOLP���̵Ĵ���");
		this.selection = selection;
	}

	public Text getPrjIdText() {
		return prjIdText;
	}

	public Text getPrjNameText() {
		return prjNameText;
	}

	public Text getPrjDescText() {
		return prjDescText;
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
		prjIdLabel.setText("*���̱�ʶ��");
		
		prjIdText = new Text(container, SWT.BORDER);
		prjIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjNameLabel.setText("*��������");
		
		prjNameText = new Text(container, SWT.BORDER);
		prjNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
		prjDescLabel.setText("*����");
		
		prjDescText = new Text(container, SWT.BORDER);
		prjDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		prjIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		prjNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		prjDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}

	// ������У��
	private void dialogChanged() {
		if (getPrjDescText().getText().length() == 0||getPrjIdText().getText().length()==0||getPrjNameText().getText().length()==0) {
			updateStatus("File container must be specified");
			return;
		}
		updateStatus(null);
	}

	// ����״̬
	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	@Override
	public boolean canFlipToNextPage() {
		if(getPrjDescText().getText().length()==0||getPrjIdText().getText().length()==0||getPrjNameText().getText().length()==0)
			return false;
		return true;
	}

}
