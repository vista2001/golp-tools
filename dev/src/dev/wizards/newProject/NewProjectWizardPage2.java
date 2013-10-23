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
	
	private List<Object> list = new ArrayList<Object>();

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
		super("wizardPage");
		setTitle("新建工程");
		setDescription("请输入新建工程的远程连接信息");
		this.selection = selection;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label remoteAddressLabel = new Label(container, SWT.NONE);
		remoteAddressLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		remoteAddressLabel.setText("服务器地址*");
				
		remoteAddressText = new Text(container, SWT.BORDER);
		remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label remoteUserLabel = new Label(container, SWT.NONE);
		remoteUserLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		remoteUserLabel.setText("服务器用户*");
				
		remoteUserText = new Text(container, SWT.BORDER);
		remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label remotePwdLabel = new Label(container, SWT.NONE);
		remotePwdLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		remotePwdLabel.setText("服务器口令*");
		
		remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		remotePwdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		list.add(remoteUserText);
		list.add(remotePwdText);
		list.add(remoteAddressText);
	}

	// 判断是否可以进入下一页
	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete();
	}

	// 检测页面输入是否完成
	@Override
	public boolean isPageComplete() {
		return isFieldComplete(list);
	}

	// 检测本页输入信息是否为空
	private boolean isFieldComplete(List<Object> l) {
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
		/*if (getPrjDescText().getText().isEmpty()||getPrjIdText().getText().isEmpty()||getPrjNameText().getText().isEmpty()) {
			updateStatus("File container must be specified");
			return;
		}*/
		updateStatus(null);
	}
}
