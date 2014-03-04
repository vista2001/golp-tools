package dev.diagram.inputDialog;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;

public class DiagramListDialog extends Dialog
{

	Shell shell;
	private Table table;
	private String text;
	private ContentsModel contentsModel;

	protected DiagramListDialog(Shell parentShell, ContentsModel contentsModel)
	{
		super(parentShell);
		this.shell = parentShell;
		this.contentsModel = contentsModel;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		super.createButtonsForButtonBar(parent);
		getButton(OK).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite area = (Composite) super.createDialogArea(parent);
		table = new Table(area, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_table.widthHint = 410;
		gd_table.heightHint = 190;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{

				if (table.getSelectionIndex() >= 0)
					getButton(OK).setEnabled(true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}
		});
		TableColumn dllid = new TableColumn(table, SWT.NONE);
		dllid.setWidth(155);
		dllid.setText("\u6D41\u7A0B\u56FE\u7F16\u53F7");

		TableColumn dllname = new TableColumn(table, SWT.NONE);
		dllname.setWidth(211);
		dllname.setText("\u6D41\u7A0B\u56FE\u540D\u79F0");

		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

		String sql = "select tfmid,tfmname from t_tfm where tradeid = -1";
		ResultSet rs;
		try
		{
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			rs = dbConnectImpl.retrive(sql);
			while (rs.next())
			{
				int id = rs.getInt(1);
				String name = rs.getString(2);
				TableItem tableItem = new TableItem(table, SWT.NONE);
				tableItem.setText(new String[] { id + "", name });
			}
			dbConnectImpl.closeConn();
		} catch (SQLException e)
		{
			e.printStackTrace();

		}
		return area;
	}

	@Override
	protected void okPressed()
	{

		int id = table.getSelectionIndex();
		text = table.getItem(id).getText(0);
		super.okPressed();
	}

	public String getText()
	{
		return text;
	}
}
