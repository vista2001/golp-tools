package dev.diagram.inputDialog;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;
import dev.util.DevLogger;

public class EdgeListLimitDialog extends Dialog {
	private CommonModel commonModel;
	private ContentsModel contentsModel;
	private Table table;
	private String text;

	public EdgeListLimitDialog(Shell parentShell, CommonModel commonModel) {
		super(parentShell);
		this.commonModel = commonModel;
		this.contentsModel = commonModel.getContentModel();
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText("��Ȩֵѡ��");
		super.configureShell(newShell);

	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getButton(OK).setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(area, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_tabFolder.widthHint = 298;
		gd_tabFolder.heightHint = 128;
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setText("\u8FB9\u6743\u503C\u9009\u62E9");

		table = new Table(tabFolder, SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		tabItem.setControl(table);

		TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn.setWidth(171);
		tblclmnNewColumn.setText("\u8FB9\u6743\u503C");

		TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
		tblclmnNewColumn_1.setWidth(100);
		tblclmnNewColumn_1.setText("\u72B6\u6001");
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if (index >= 0
						&& !table.getItem(index).getBackground()
								.equals(ColorConstants.gray)) {
					text = table.getItem(index).getText(0);
					getButton(OK).setEnabled(true);
				} else
					getButton(OK).setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		String[] edgeList = SetEdgeList().split("\\|");
		for (int i = 0; i < edgeList.length; i++) {
			TableItem tableItem = new TableItem(table, SWT.NONE);
			tableItem.setText(edgeList[i]);
			if (findEdge(edgeList[i])) {
				tableItem.setBackground(ColorConstants.gray);
				tableItem.setText(1, "�Ѵ���");
			} else {
				tableItem.setText(1, "����ѡ��");
			}
		}
		return area;
	}

	public String SetEdgeList() {
		String str = "999|";
		String aopId = commonModel.getTfmBlock().getAopName();
		DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
		String sql = "select aopretval from aop where aopid ='" + aopId + "'";
		ResultSet rs;
		try {
			dbConnectImpl.openConn(CommonUtil.initPs(contentsModel.projectId));
			rs = dbConnectImpl.retrive(sql);
			if (rs.next()) {
				str += rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		return new String(str);
	}

	public String getText() {
		return text;
	}

	private boolean findEdge(String val) {
		for (AbstractConnectionModel edge : commonModel.getSourceConnection()) {
			if (val.equals((edge.getWeight() + "")))
				return true;
		}
		return false;
	}

	@Override
	protected void okPressed() {

		super.okPressed();
	}

}