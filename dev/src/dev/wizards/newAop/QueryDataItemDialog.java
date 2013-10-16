package dev.wizards.newAop;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;

public class QueryDataItemDialog extends TitleAreaDialog {
	List listInDialog;
	java.util.List<String> listForReturn = null;
	java.util.List<InformDialogListener> listeners = new ArrayList<InformDialogListener>();

	public java.util.List<String> getListForReturn() {
		return listForReturn;
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public QueryDataItemDialog(Shell parentShell, Object obj) {
		super(parentShell);
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
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		listInDialog = new List(container, SWT.BORDER | SWT.MULTI);
		listInDialog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		initData();
		this.setMessage("请选择数据项");

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
		String[] aops = listInDialog.getSelection();
		listForReturn = new ArrayList<String>();
		for (int i = 0; i < aops.length; i++) {
			listForReturn.add(aops[i]);
			//System.out.println(aops[i]);
		}
		notifyInformDialogEvent();
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
	
	private void initData() {
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		ResultSet rs = null;
		dbConnectImpl.openConn();
		try {
			rs = dbConnectImpl.retrive("select * from dataItem order by dataItemId");
			java.util.List<String> contents = new ArrayList<String>();
			while (rs.next()) {
				contents.add(rs.getString(1));
			}
			listInDialog.setItems(contents.toArray(new String[0]));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				dbConnectImpl.closeConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
