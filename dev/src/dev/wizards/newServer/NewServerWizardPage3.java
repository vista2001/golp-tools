/* 文件名：       NewServerWizardPage3.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：  1.用DebugOut.println方法替换System.out.println方法； 
 *         2.增加访问远程目录的功能。
 */

package dev.wizards.newServer;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import dev.remote.RemoteDialog;

public class NewServerWizardPage3 extends WizardPage {
	private ISelection selection;
	private Text serverSpecIncludePathText;
	private List serverSpecIncludePathList;
	private Button delButton;
	private Button upButton;
	private Button downButton;

	public ISelection getSelection() {
		return selection;
	}

	public List getServerSpecIncludePathList() {
		return serverSpecIncludePathList;
	}

	public NewServerWizardPage3(ISelection selection) {
		super("NewServerWizardPage3");
		setTitle("新建服务程序向导");
		setDescription("这个向导将指导你完成GOLP服务程序的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label serverSpecIncludePathLabel = new Label(container, SWT.NONE);
		serverSpecIncludePathLabel.setText("服务程序个性依赖头文件路径：");

		serverSpecIncludePathText = new Text(container, SWT.BORDER);
		serverSpecIncludePathText.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 1, 1));
		serverSpecIncludePathText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((serverSpecIncludePathText.getText().trim().isEmpty() == false)
						&& (e.keyCode == SWT.CR)) {
					serverSpecIncludePathList.add(serverSpecIncludePathText
							.getText());
					serverSpecIncludePathText.setText("");
				}
			}
		});

		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// if (!serverSpecIncludePathText.getText().isEmpty())
				// {
				// serverSpecIncludePathList.add(serverSpecIncludePathText.getText());
				// serverSpecIncludePathText.setText("");
				// }
				String upProject = ((NewServerWizardPage0) getWizard().getPage(
						"NewServerWizardPage0")).getUpProjectCombo().getText();
				ArrayList<String> paths = new ArrayList<String>();
				RemoteDialog remoteDialog = new RemoteDialog(getShell(),
						upProject, null, RemoteDialog.REMOTEDIALOG_DIRECTORY,
						RemoteDialog.REMOTEDIALOG_MULTI, paths);
				remoteDialog.open();
				for (String str : paths) {
					serverSpecIncludePathList.add(str);
				}
			}
		});
		new Label(container, SWT.NONE);

		serverSpecIncludePathList = new List(container, SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI);
		serverSpecIncludePathList.setLayoutData(new GridData(SWT.FILL,
				SWT.FILL, true, true, 1, 3));
		serverSpecIncludePathList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (serverSpecIncludePathList.getSelectionIndices().length > 0) {
					delButton.setEnabled(true);
					if (serverSpecIncludePathList.getSelectionIndices().length == 1) {
						upButton.setEnabled(true);
						downButton.setEnabled(true);
					} else {
						upButton.setEnabled(false);
						downButton.setEnabled(false);
					}
				}
			}
		});

		delButton = new Button(container, SWT.NONE);
		delButton.setEnabled(false);
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");
		new Label(container, SWT.NONE);

		delButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] indices = serverSpecIncludePathList.getSelectionIndices();
				serverSpecIncludePathList.remove(indices);
				delButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		});

		upButton = new Button(container, SWT.NONE);
		upButton.setEnabled(false);
		GridData gd_upButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_upButton.widthHint = 60;
		upButton.setLayoutData(gd_upButton);
		upButton.setText("上移");
		new Label(container, SWT.NONE);
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = serverSpecIncludePathList.getSelectionIndex();
				if (index > 0) {
					String tmp = serverSpecIncludePathList.getItem(index);
					serverSpecIncludePathList.setItem(index,
							serverSpecIncludePathList.getItem(index - 1));
					serverSpecIncludePathList.setItem(index - 1, tmp);
					serverSpecIncludePathList.setSelection(index - 1);
				}
			}
		});

		downButton = new Button(container, SWT.NONE);
		downButton.setEnabled(false);
		GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_downButton.widthHint = 60;
		downButton.setLayoutData(gd_downButton);
		downButton.setText("下移");
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = serverSpecIncludePathList.getSelectionIndex();
				if (index < serverSpecIncludePathList.getItemCount() - 1) {
					String tmp = serverSpecIncludePathList.getItem(index);
					serverSpecIncludePathList.setItem(index,
							serverSpecIncludePathList.getItem(index + 1));
					serverSpecIncludePathList.setItem(index + 1, tmp);
					serverSpecIncludePathList.setSelection(index + 1);
				}
			}
		});

	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法

	@Override
	public boolean canFlipToNextPage() {

		// DebugOut.println(getServerSpeclibText().getText().length());

		return true;

	}

}
