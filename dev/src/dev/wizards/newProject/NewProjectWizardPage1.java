/* �ļ�����       NewProjectWizardPage1.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.14
 * �޸����ݣ�   1.�޸Ĺ��캯���У�����super����ʱ�Ĳ���Ϊ�����������
 *         2.ȡ����dbType�Ĵ���������Ҫ�����ԣ�
 *         3.���ӷ���addModifyListener()��Ϊ���е��ı������ModifyListener��
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

public class NewProjectWizardPage1 extends WizardPage {
	private ISelection selection;
	private Text dbAddressText;
	private Text dbPortText;
	private Text dbInstanceText;
	private Text dbUserText;
	private Text dbPwdText;

	private List<Text> list = new ArrayList<Text>();

	public ISelection getSelection() {
		return selection;
	}

	public Text getDbAddressText() {
		return dbAddressText;
	}

	public Text getDbPortText() {
		return dbPortText;
	}

	public Text getDbInstanceText() {
		return dbInstanceText;
	}

	public Text getDbUserText() {
		return dbUserText;
	}

	public Text getDbPwdText() {
		return dbPwdText;
	}

	public NewProjectWizardPage1(ISelection selection) {
		super("NewProjectWizardPage1");
		setTitle("�½�����");
		setDescription("�������½����̵����ݿ�������Ϣ");
		this.selection = selection;
	}

	@Override
	/**������ҳ��Ŀؼ�*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dbAddressLabel = new Label(container, SWT.NONE);
		dbAddressLabel.setText("*���ݿ��ַ��");

		dbAddressText = new Text(container, SWT.BORDER);
		dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dbPortLabel = new Label(container, SWT.NONE);
		dbPortLabel.setText("*���ݿ�˿ڣ�");

		dbPortText = new Text(container, SWT.BORDER);
		dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dbInstanceLabel = new Label(container, SWT.NONE);
		dbInstanceLabel.setText("*���ݿ�ʵ����");

		dbInstanceText = new Text(container, SWT.BORDER);
		dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dbUserLabel = new Label(container, SWT.NONE);
		dbUserLabel.setText("*���ݿ��û���");

		dbUserText = new Text(container, SWT.BORDER);
		dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label dbPwdLabel = new Label(container, SWT.NONE);
		dbPwdLabel.setText("*���ݿ���");

		dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		list.add(dbAddressText);
		list.add(dbInstanceText);
		list.add(dbPortText);
		list.add(dbUserText);
		list.add(dbPwdText);

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
