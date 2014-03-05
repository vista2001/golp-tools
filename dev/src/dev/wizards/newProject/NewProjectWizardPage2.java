/* 文件名：       NewProjectWizardPage2.java
 * 修改人：       rxy
 * 修改时间：   2013.12.14
 * 修改内容：   1.修改构造函数中，调用super方法时的参数为本类的类名；
 *         2.增加方法addModifyListener()，为所有的文本框添加ModifyListener。 
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
		remoteAddressLabel.setText("*服务器地址：");

		remoteAddressText = new Text(container, SWT.BORDER);
		remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label remoteUserLabel = new Label(container, SWT.NONE);
		remoteUserLabel.setText("*服务器用户：");

		remoteUserText = new Text(container, SWT.BORDER);
		remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label remotePwdLabel = new Label(container, SWT.NONE);
		remotePwdLabel.setText("*服务器口令：");

		remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		list.add(remoteUserText);
		list.add(remotePwdText);
		list.add(remoteAddressText);

		addModifyListener();
	}

	/**
	 * 为成员变量list中的所有元素，添加ModifyListener。
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
