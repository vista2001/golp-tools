/* 文件名：       NewServerWizardPage2.java
 * 修改人：       rxy
 * 修改时间：   2013.12.30
 * 修改内容：   1.为输入文本框增加监听器，使其可以响应键盘回车事件。
 */

package dev.wizards.newServer;

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

public class NewServerWizardPage2 extends WizardPage {
	private ISelection selection;
	private Text serverSpeclibNameText;
	private List serverSpeclibNameList;
	private Button delButton;
	private Button upButton;
	private Button downButton;

	// private boolean page2Enable = false;

	public ISelection getSelection() {
		return selection;
	}

	public List getServerSpeclibNameList() {
		return serverSpeclibNameList;
	}

	public NewServerWizardPage2(ISelection selection) {
		super("NewServerWizardPage2");
		setTitle("新建服务程序向导");
		setDescription("这个向导将指导你完成GOLP服务程序的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {

		NewServerWizardPage1 previousPage = (NewServerWizardPage1) getPreviousPage();
		// if(previousPage.getServerSpeclibPathList().getSelectionIndices().length
		// > 0)
		// {
		// page2Enable = true;
		// }
		// else
		// {
		// page2Enable = false;
		// }

		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label serverSpeclibNameLabel = new Label(container, SWT.NONE);
		serverSpeclibNameLabel.setText("服务程序个性依赖库名称：");

		serverSpeclibNameText = new Text(container, SWT.BORDER);
		// serverSpeclibNameText.setEnabled(page2Enable);
		serverSpeclibNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		serverSpeclibNameText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((serverSpeclibNameText.getText().trim().isEmpty() == false)
						&& (e.keyCode == SWT.CR)) {
					serverSpeclibNameList.add(serverSpeclibNameText.getText());
					serverSpeclibNameText.setText("");
					dialogChanged();
				}
			}
		});

		Button addButton = new Button(container, SWT.NONE);
		// addButton.setEnabled(page2Enable);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!serverSpeclibNameText.getText().isEmpty()) {
					serverSpeclibNameList.add(serverSpeclibNameText.getText());
					serverSpeclibNameText.setText("");
					dialogChanged();
				}
			}
		});
		new Label(container, SWT.NONE);

		serverSpeclibNameList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		// serverSpeclibNameList.setEnabled(page2Enable);
		serverSpeclibNameList.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 3));
		serverSpeclibNameList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (serverSpeclibNameList.getSelectionIndices().length > 0) {
					// dialogChanged();
					delButton.setEnabled(true);
					if (serverSpeclibNameList.getSelectionIndices().length == 1) {
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
				int[] indices = serverSpeclibNameList.getSelectionIndices();
				serverSpeclibNameList.remove(indices);
				delButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
				dialogChanged();
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
				int index = serverSpeclibNameList.getSelectionIndex();
				if (index > 0) {
					String tmp = serverSpeclibNameList.getItem(index);
					serverSpeclibNameList.setItem(index,
							serverSpeclibNameList.getItem(index - 1));
					serverSpeclibNameList.setItem(index - 1, tmp);
					serverSpeclibNameList.setSelection(index - 1);
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
				int index = serverSpeclibNameList.getSelectionIndex();
				if (index < serverSpeclibNameList.getItemCount() - 1) {
					String tmp = serverSpeclibNameList.getItem(index);
					serverSpeclibNameList.setItem(index,
							serverSpeclibNameList.getItem(index + 1));
					serverSpeclibNameList.setItem(index + 1, tmp);
					serverSpeclibNameList.setSelection(index + 1);
				}
			}
		});

	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged() {
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {

		if (serverSpeclibNameList.getItemCount() > 0) {
			return true;
		} else {
			return false;
		}

	}

}
