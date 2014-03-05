/* �ļ�����       NewProjectWizardPage2.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.14
 * �޸����ݣ�   1.�޸Ĺ��캯���У�����super����ʱ�Ĳ���Ϊ�����������
 *         2.���ӷ���addModifyListener()��Ϊ���е��ı������ModifyListener�� 
 */

package dev.wizards.newProject;

import java.util.ArrayList;
import java.util.List;

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

public class NewProjectWizardPage2 extends WizardPage {
	private ISelection selection;
	private Text remoteUserText;
	private Text remotePwdText;
	private Text remoteAddressText;

	private List<Text> list = new ArrayList<Text>();

	public ISelection getSelection() {
		return selection;
	}

	public Text getRemoteUserText() {
		return remoteUserText;
	}

	public Text getRemotePwdText() {
		return remotePwdText;
	}

	public Text getRemoteAddressText() {
		return remoteAddressText;
	}

	public NewProjectWizardPage2(ISelection selection) {
		super("NewProjectWizardPage2");
		setTitle("�½�����");
		setDescription("�������½����̵�Զ��������Ϣ");
		this.selection = selection;
	}

	@Override
	/**������ҳ��Ŀؼ�*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label remoteAddressLabel = new Label(container, SWT.NONE);
		remoteAddressLabel.setText("*��������ַ��");

		remoteAddressText = new Text(container, SWT.BORDER);
		remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label remoteUserLabel = new Label(container, SWT.NONE);
		remoteUserLabel.setText("*�������û���");

		remoteUserText = new Text(container, SWT.BORDER);
		remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label remotePwdLabel = new Label(container, SWT.NONE);
		remotePwdLabel.setText("*���������");

		remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		list.add(remoteUserText);
		list.add(remotePwdText);
		list.add(remoteAddressText);

		addModifyListener();
	}

	/**
	 * Ϊ��Ա����list�е�����Ԫ�أ����ModifyListener��
	 */
	private void addModifyListener() {
		for (Text text : list) {
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					dialogChanged();
				}
			});
		}
	}

	// �ж��Ƿ���Խ�����һҳ
	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	// ���ҳ�������Ƿ����
	@Override
	public boolean isPageComplete() {
		return isFieldComplete(list);
	}

	// ��Ȿҳ������Ϣ�Ƿ�Ϊ��
	private boolean isFieldComplete(List<Text> l) {
		for (Object object : l) {
			if (object instanceof Text) {
				if (((Text) object).getText() == null
						|| ((Text) object).getText().isEmpty())
					return false;
			}
			if (object instanceof Combo) {
				if (((Combo) object).getText() == null
						|| ((Combo) object).getText().isEmpty())
					return false;
			}
		}
		return true;
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private void dialogChanged() {
		/*
		 * if
		 * (getPrjDescText().getText().isEmpty()||getPrjIdText().getText().isEmpty
		 * ()||getPrjNameText().getText().isEmpty()) {
		 * updateStatus("File container must be specified"); return; }
		 */
		updateStatus(null);
	}
}
