/* �ļ�����       NewServerWizardPage4.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.11.27
 * �޸����ݣ�   1. ���û�������ѡ��callBackSourceʱ�������ļ��Ի�������޸ģ�
 *         ���ӹ�������ʹ���û�ֻ��ѡ��*.cpp�ļ���
 *         2.���ٵ����ļ��Ի��򣬸�Ϊ���û����룻
 *         3.���ӷ���Զ��Ŀ¼�Ĺ��ܡ�
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

public class NewServerWizardPage4 extends WizardPage {
	private ISelection selection;
	private List callBackSourceList;
	private Button delButton;

	private Text callBackSourceText;
	private Button upButton;
	private Button downButton;

	public List getCallBackSourceList() {
		return callBackSourceList;
	}

	public NewServerWizardPage4(ISelection selection) {
		super("NewServerWizardPage4");
		setTitle("�½����������");
		setDescription("����򵼽�ָ�������GOLP�������Ĵ���");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(3, false));

		Label callBackSourceLabel = new Label(container, SWT.NONE);
		callBackSourceLabel.setText("��ѡ��CallBackSource��");

		callBackSourceText = new Text(container, SWT.BORDER);
		callBackSourceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		callBackSourceText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((callBackSourceText.getText().trim().isEmpty() == false)
						&& (e.keyCode == SWT.CR)) {
					callBackSourceList.add(callBackSourceText.getText());
					callBackSourceText.setText("");
				}
			}
		});

		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("���");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String upProject = ((NewServerWizardPage0) getWizard().getPage(
						"NewServerWizardPage0")).getUpProjectCombo().getText();
				ArrayList<String> paths = new ArrayList<String>();
				RemoteDialog remoteDialog = new RemoteDialog(getShell(),
						upProject, null, RemoteDialog.REMOTEDIALOG_FILE,
						RemoteDialog.REMOTEDIALOG_MULTI, paths);
				remoteDialog.open();
				for (String str : paths) {
					callBackSourceList.add(str);
				}
			}
		});
		new Label(container, SWT.NONE);

		callBackSourceList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		callBackSourceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 3));
		callBackSourceList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (callBackSourceList.getSelectionIndices().length > 0) {
					delButton.setEnabled(true);
					if (callBackSourceList.getSelectionIndices().length == 1) {
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
		delButton.setText("�Ƴ�");
		new Label(container, SWT.NONE);

		delButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] indices = callBackSourceList.getSelectionIndices();
				callBackSourceList.remove(indices);
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
		upButton.setText("����");
		new Label(container, SWT.NONE);
		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = callBackSourceList.getSelectionIndex();
				if (index > 0) {
					String tmp = callBackSourceList.getItem(index);
					callBackSourceList.setItem(index,
							callBackSourceList.getItem(index - 1));
					callBackSourceList.setItem(index - 1, tmp);
					callBackSourceList.setSelection(index - 1);
				}
			}
		});

		downButton = new Button(container, SWT.NONE);
		downButton.setEnabled(false);
		GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_downButton.widthHint = 60;
		downButton.setLayoutData(gd_downButton);
		downButton.setText("����");
		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = callBackSourceList.getSelectionIndex();
				if (index < callBackSourceList.getItemCount() - 1) {
					String tmp = callBackSourceList.getItem(index);
					callBackSourceList.setItem(index,
							callBackSourceList.getItem(index + 1));
					callBackSourceList.setItem(index + 1, tmp);
					callBackSourceList.setSelection(index + 1);
				}
			}
		});
	}

	@Override
	public boolean canFlipToNextPage() {

		return true;
	}

}
