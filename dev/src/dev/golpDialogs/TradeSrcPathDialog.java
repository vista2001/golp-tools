/* 文件名：       TradeSrcPathDialog.java
 * 描述：           该文件定义了类TradeSrcPathDialog，该类实现了在新建交易向导中，
 *         让用户输入源文件路径的功能。
 * 创建人：       rxy
 * 创建时间：   2013.12.3
 */

package dev.golpDialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import dev.golpEvent.InformDialogEvent;
import dev.golpEvent.InformDialogListener;
import dev.remote.RemoteDialog;

/**
 * TradeSrcPathDialog类，提供了用户在新建交易向导中，输入源文件路径的功能
 * 
 * @author rxy
 */
public class TradeSrcPathDialog extends TitleAreaDialog {
	private List tradeSrcPathList;
	java.util.List<String> listForReturn = null;
	java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();
	private Text tradeSrcPathText;
	private Button delButton;
	private Button upButton;
	private Button downButton;
	private String prjId;
	private java.util.List<String> already = new ArrayList<String>();

	public java.util.List<String> getListForReturn() {
		return listForReturn;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public TradeSrcPathDialog(Shell parentShell, Object obj, String string,
			String prjId) {
		super(parentShell);
		String[] tmp = string.split("\\|");
		for (String s : tmp) {
			already.add(s);
		}
		this.prjId = prjId;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		new Label(area, SWT.NONE);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		this.setMessage("请输入所有交易源文件路径");

		tradeSrcPathText = new Text(container, SWT.BORDER);
		tradeSrcPathText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		tradeSrcPathList = new List(container, SWT.BORDER | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.MULTI);
		tradeSrcPathList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 3));
		for (String s : already) {
			if (s != "") {
				tradeSrcPathList.add(s);
			}
		}

		delButton = new Button(container, SWT.NONE);
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");

		upButton = new Button(container, SWT.NONE);
		GridData gd_upButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_upButton.widthHint = 60;
		upButton.setLayoutData(gd_upButton);
		upButton.setText("上移");

		downButton = new Button(container, SWT.NONE);
		GridData gd_downButton = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_downButton.widthHint = 60;
		downButton.setLayoutData(gd_downButton);
		downButton.setText("下移");

		tradeSrcPathText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((tradeSrcPathText.getText().trim().isEmpty() == false)
						&& (e.keyCode == SWT.CR)) {
					tradeSrcPathList.add(tradeSrcPathText.getText());
					tradeSrcPathText.setText("");
				}
			}
		});

		tradeSrcPathList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tradeSrcPathList.getSelectionIndices().length > 0) {
					delButton.setEnabled(true);
					if (tradeSrcPathList.getSelectionIndices().length == 1) {
						upButton.setEnabled(true);
						downButton.setEnabled(true);
					} else {
						upButton.setEnabled(false);
						downButton.setEnabled(false);
					}
				}
			}
		});

		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<String> paths = new ArrayList<String>();
				RemoteDialog remoteDialog = new RemoteDialog(getShell(), prjId,
						null, RemoteDialog.REMOTEDIALOG_FILE,
						RemoteDialog.REMOTEDIALOG_MULTI, paths);
				remoteDialog.open();
				for (String str : paths) {
					tradeSrcPathList.add(str);
				}
			}
		});

		delButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] indices = tradeSrcPathList.getSelectionIndices();
				tradeSrcPathList.remove(indices);
				delButton.setEnabled(false);
				upButton.setEnabled(false);
				downButton.setEnabled(false);
			}
		});

		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tradeSrcPathList.getSelectionIndex();
				if (index > 0) {
					String tmp = tradeSrcPathList.getItem(index);
					tradeSrcPathList.setItem(index,
							tradeSrcPathList.getItem(index - 1));
					tradeSrcPathList.setItem(index - 1, tmp);
					tradeSrcPathList.setSelection(index - 1);
				}
			}
		});

		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = tradeSrcPathList.getSelectionIndex();
				if (index < tradeSrcPathList.getItemCount() - 1) {
					String tmp = tradeSrcPathList.getItem(index);
					tradeSrcPathList.setItem(index,
							tradeSrcPathList.getItem(index + 1));
					tradeSrcPathList.setItem(index + 1, tmp);
					tradeSrcPathList.setSelection(index + 1);
				}
			}
		});

		return area;

	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				false);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		// DebugOut.println(listInDialog.isDisposed());
		String[] tradeSrcPaths = tradeSrcPathList.getItems();
		listForReturn = new ArrayList<String>();
		for (int i = 0; i < tradeSrcPaths.length; i++) {
			listForReturn.add(tradeSrcPaths[i]);
		}
		notifyInformDialogEvent();
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(550, 400);
	}

	public void addInformDialogListener(InformDialogListener dl) {
		listeners.add(dl);
		// DebugOut.println("demo add");
	}

	public void notifyInformDialogEvent() {
		for (InformDialogListener idl : listeners) {
			idl.handleEvent(new InformDialogEvent(this));
			// DebugOut.println(idl.toString() + " is informed");
		}
	}
}