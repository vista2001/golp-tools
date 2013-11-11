package dev.editors.server;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import dev.wizards.newAop.InformDialogEvent;
import dev.wizards.newAop.InformDialogListener;

public class CallBackSourceDialog extends TitleAreaDialog
{
	List callBackSourceList;
	java.util.List<String> listForReturn = null;
	java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();
	private Button delButton;
	private java.util.List<String> already = new ArrayList<String>();

	public java.util.List<String> getListForReturn() {
		return listForReturn;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CallBackSourceDialog(Shell parentShell, Object obj, String string) {
		super(parentShell);
		String[] tmp = string.split("\\|");
		for(String s : tmp)
		{
			already.add(s);
		}
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
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		this.setMessage("请选择回调的源程序");
		
		callBackSourceList = new List(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		callBackSourceList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		for(String s : already)
		{
			if(s != "")
			{
				callBackSourceList.add(s);
			}
		}
		
		Button addButton = new Button(container, SWT.NONE);
		GridData gd_addButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_addButton.widthHint = 60;
		addButton.setLayoutData(gd_addButton);
		addButton.setText("添加");
		
		delButton = new Button(container, SWT.NONE);
		delButton.setEnabled(false);
		GridData gd_delButton = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
		gd_delButton.widthHint = 60;
		delButton.setLayoutData(gd_delButton);
		delButton.setText("移除");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		callBackSourceList.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (callBackSourceList.getSelectionIndices().length > 0)
				{
					delButton.setEnabled(true);
				}
			}
		});
		
		addButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN
						| SWT.MULTI);
				dialog.setFilterPath("C:\\");
				dialog.setFilterExtensions(new String[]{"*.*","*.c","*.cpp"});
				dialog.open();
				String[] files = dialog.getFileNames();
				for (String s : files)
				{
						if(dialog.getFilterPath().endsWith(File.separator))
						{
							callBackSourceList.add(dialog.getFilterPath() + s);
						}
						//callBackSourceList.add(dialog.getFilterPath() + s);
					else
						callBackSourceList.add(dialog.getFilterPath() + File.separator + s);
				}
			}
		});
		
		delButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				int[] indices = callBackSourceList.getSelectionIndices();
				callBackSourceList.remove(indices);
				delButton.setEnabled(false);
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
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		//System.out.println(listInDialog.isDisposed());
		String[] serverSpecIncludePaths = callBackSourceList.getItems();
		listForReturn = new ArrayList<String>();
		for (int i = 0; i < serverSpecIncludePaths.length; i++) {
			listForReturn.add(serverSpecIncludePaths[i]);
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
		//System.out.println("demo add");
	}

	public void notifyInformDialogEvent() {
		for (InformDialogListener idl : listeners) {
			idl.handleEvent(new InformDialogEvent(this));
			//System.out.println(idl.toString() + " is informed");
		}
	}
}
