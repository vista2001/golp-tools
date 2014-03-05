package dev.diagram.inputDialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.beans.TfmCommDeal;
import dev.diagram.beans.TfmCompersation;
import dev.diagram.beans.TfmException;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;

/**
 * 异常和补偿对应的对话框，具体说明请参考
 * <p>
 * BlockConfigureDialog.java
 * </p>
 * 
 * @author 木木
 * 
 */
public class ExceptionAndCompsationDialog extends Dialog {
	public ExceptionAndCompsationDialog(Shell parentShell,
			ContentsModel contentsModel) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM);
		this.shell = parentShell;
		this.contentsModel = contentsModel;
	}

	Shell shell;
	private Text dllid, seqno, name, desc;
	private Button up, down, del, newb, save;
	private Combo funcname;
	private Table table;
	private Button comprasation;
	private Button exception;
	private ContentsModel contentsModel;
	SelectionAdapter listener;
	ModifyListener listenerSaveActive;
	SelectionListener listenerSelect;
	Map<Integer, TfmException> tfmExceptionMap = new TreeMap<Integer, TfmException>();
	Map<Integer, TfmCompersation> tfmComprasationMap = new TreeMap<Integer, TfmCompersation>();
	private Text tfmid;
	private static final int APPLICATION_ID = 10000;

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", false);
		createButton(parent, APPLICATION_ID, "保存", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		getButton(APPLICATION_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(true);
		getButton(APPLICATION_ID).addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				// getButton(IOK_ID).setEnabled(false);
				getButton(APPLICATION_ID).setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("异常和补偿");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(new GridLayout(1, false));

		TabFolder tabFolder = new TabFolder(area, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1);
		gd_tabFolder.widthHint = 437;
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem tabItem_3 = new TabItem(tabFolder, SWT.NONE);
		tabItem_3.setText("\u6D41\u7A0B\u56FE");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tabItem_3.setControl(composite_1);

		Label label = new Label(composite_1, SWT.NONE);
		label.setBounds(10, 183, 97, 17);
		label.setText("\u5DF2\u5B9A\u4E49");

		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setBounds(218, 59, 45, 15);
		lblNewLabel_4.setText("\u52A8\u6001\u5E93\uFF1A");

		dllid = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY);
		dllid.setBounds(289, 56, 106, 22);

		Label lblNewLabel_7 = new Label(composite_1, SWT.NONE);
		lblNewLabel_7.setBounds(218, 84, 58, 15);
		lblNewLabel_7.setText("\u51FD\u6570\u540D\u79F0\uFF1A");

		funcname = new Combo(composite_1, SWT.READ_ONLY);
		funcname.setBounds(289, 81, 106, 25);

		Label lblNewLabel_9 = new Label(composite_1, SWT.NONE);
		lblNewLabel_9.setBounds(218, 36, 48, 17);
		lblNewLabel_9.setText("\u987A\u5E8F\u53F7\uFF1A");

		seqno = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY);
		seqno.setBounds(289, 30, 106, 23);

		final Button exbt = new Button(composite_1, SWT.NONE);
		exbt.setBounds(399, 56, 16, 22);
		exbt.setText("...");

		up = new Button(composite_1, SWT.ARROW_UP);
		up.setBounds(113, 180, 28, 23);
		up.setText("\u4E0A\u79FB");
		up.setEnabled(false);

		del = new Button(composite_1, SWT.NONE);
		del.setBounds(350, 177, 45, 23);
		del.setText("\u5220\u9664");
		del.setEnabled(false);

		down = new Button(composite_1, SWT.ARROW_DOWN);
		down.setBounds(147, 180, 28, 23);
		down.setText("\u4E0B\u79FB");
		down.setEnabled(false);

		Label lblNewLabel_8 = new Label(composite_1, SWT.NONE);
		lblNewLabel_8.setBounds(218, 8, 58, 17);
		lblNewLabel_8.setText("\u914D\u7F6E\u7C7B\u578B\uFF1A");

		exception = new Button(composite_1, SWT.RADIO);
		exception.setBounds(289, 8, 43, 17);
		exception.setText("\u5F02\u5E38");
		exception.setSelection(true);

		comprasation = new Button(composite_1, SWT.RADIO);
		comprasation.setBounds(344, 8, 45, 17);
		comprasation.setText("\u8865\u507F");

		Label lblNewLabel_10 = new Label(composite_1, SWT.NONE);
		lblNewLabel_10.setBounds(10, 36, 85, 17);
		lblNewLabel_10.setText("\u540D\u79F0\uFF1A");

		name = new Text(composite_1, SWT.BORDER);
		name.setBounds(10, 56, 202, 46);

		desc = new Text(composite_1, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		desc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		desc.setBounds(10, 125, 385, 52);

		newb = new Button(composite_1, SWT.NONE);
		newb.setBounds(233, 180, 45, 23);
		newb.setText("\u65B0\u5EFA");

		save = new Button(composite_1, SWT.NONE);
		save.setBounds(289, 180, 45, 23);
		save.setText("\u4FDD\u5B58");
		save.setEnabled(false);

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(9, 206, 385, 127);

		final TableColumn t_seqno = new TableColumn(table, SWT.NONE);
		t_seqno.setWidth(100);
		t_seqno.setText("\u987A\u5E8F\u53F7");

		TableColumn t_name = new TableColumn(table, SWT.NONE);
		t_name.setWidth(89);
		t_name.setText("\u540D\u79F0");

		TableColumn t_dll = new TableColumn(table, SWT.NONE);
		t_dll.setWidth(76);
		t_dll.setText("\u52A8\u6001\u5E93");

		TableColumn t_func = new TableColumn(table, SWT.NONE);
		t_func.setWidth(100);
		t_func.setText("\u51FD\u6570\u540D\u79F0");

		Label lblNewLabel = new Label(composite_1, SWT.NONE);
		lblNewLabel.setBounds(9, 8, 97, 17);
		lblNewLabel.setText("\u6240\u5C5E\u6D41\u7A0B\u56FE\u7F16\u53F7\uFF1A");

		tfmid = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY);
		tfmid.setBounds(112, 4, 100, 23);
		listenerSelect = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				name.setText("");
				desc.setText("");
				seqno.setText("");
				table.removeAll();
				Button temp = (Button) e.widget;
				if (temp.getSelection())
					if (temp.getText().equals("异常")) {

						for (int curr : tfmExceptionMap.keySet()) {
							TableItem tabItem = new TableItem(table, SWT.NONE);
							tabItem.setText(new String[] {
									tfmExceptionMap.get(curr).getSeqNo(),
									tfmExceptionMap.get(curr).getName(),
									tfmExceptionMap.get(curr).getDllId(),
									tfmExceptionMap.get(curr).getFuncname() });
						}
					} else {
						for (int curr : tfmComprasationMap.keySet()) {
							TableItem tabItem = new TableItem(table, SWT.NONE);
							tabItem.setText(new String[] {
									tfmComprasationMap.get(curr).getSeqNo(),
									tfmComprasationMap.get(curr).getName(),
									tfmComprasationMap.get(curr).getDllId(),
									tfmComprasationMap.get(curr).getFuncname() });
						}
					}
				seqno.setText(Integer.toString(table.getItemCount() + 1));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		};
		exception.addSelectionListener(listenerSelect);
		comprasation.addSelectionListener(listenerSelect);
		dllid.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (dllid.getText().length() != 0) {
					funcname.removeAll();
					DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

					String sql = "select aopname from aop where upaopdll= "
							+ dllid.getText();
					ResultSet rs;
					try {
						dbConnectImpl.openConn(CommonUtil
								.initPs(contentsModel.projectId));
						rs = dbConnectImpl.retrive(sql);
						while (rs.next())
							funcname.add(rs.getString(1));
						dbConnectImpl.closeConn();
					} catch (SQLException e1) {

						e1.printStackTrace();
					}
				}
			}
		});

		exbt.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DllIdConfigureDialog dialog = new DllIdConfigureDialog(
						getShell(), contentsModel);
				if (Window.OK == dialog.open()) {
					dllid.setText(dialog.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		listenerSaveActive = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				if (dllid.getText() != "" && funcname.getText() != ""
						&& name.getText() != "" && seqno.getText() != "") {
					save.setEnabled(true);
				} else
					save.setEnabled(false);
			}
		};
		desc.addModifyListener(listenerSaveActive);
		funcname.addModifyListener(listenerSaveActive);
		name.addModifyListener(listenerSaveActive);
		seqno.addModifyListener(listenerSaveActive);

		listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				super.widgetSelected(e);
				Button b = (Button) e.widget;
				if (b.getText().equals("上移")) {
					int index = table.getSelectionIndex();
					int no = Integer.parseInt(table.getItem(index).getText(0));
					if (index <= 0)
						return;
					String[] down = new String[] {
							table.getItem(index - 1).getText(0),
							table.getItem(index).getText(1),
							table.getItem(index).getText(2),
							table.getItem(index).getText(3) };
					String[] up = new String[] {
							table.getItem(index).getText(0),
							table.getItem(index - 1).getText(1),
							table.getItem(index - 1).getText(2),
							table.getItem(index - 1).getText(3) };
					table.getItem(index - 1).setText(down);
					table.getItem(index).setText(up);
					if (exception.getSelection()) {
						TfmException _up = tfmExceptionMap.remove(no - 1);
						TfmException _down = tfmExceptionMap.remove(no);
						_up.setSeqNo(no + "");
						_down.setSeqNo(no - 1 + "");
						tfmExceptionMap.put(no, _up);
						tfmExceptionMap.put(no - 1, _down);
					} else {
						TfmCompersation _up = tfmComprasationMap.remove(no - 1);
						TfmCompersation _down = tfmComprasationMap.remove(no);
						_up.setSeqNo(no + "");
						_down.setSeqNo(no - 1 + "");
						tfmComprasationMap.put(no, _up);
						tfmComprasationMap.put(no - 1, _down);
					}
					table.setSelection(index - 1);
				} else if (b.getText().equals("下移")) {
					int index = table.getSelectionIndex();
					int no = Integer.parseInt(table.getItem(index).getText(0));
					if (index == table.getItemCount() - 1)
						return;
					String[] up = { table.getItem(index + 1).getText(0),
							table.getItem(index).getText(1),
							table.getItem(index).getText(2),
							table.getItem(index).getText(3) };
					String[] down = { table.getItem(index).getText(0),
							table.getItem(index + 1).getText(1),
							table.getItem(index + 1).getText(2),
							table.getItem(index + 1).getText(3) };
					table.getItem(index).setText(down);
					table.getItem(index + 1).setText(up);
					if (exception.getSelection()) {
						TfmException _up = tfmExceptionMap.remove(no);
						TfmException _down = tfmExceptionMap.remove(no + 1);
						_up.setSeqNo(no + 1 + "");
						_down.setSeqNo(no + "");
						tfmExceptionMap.put(no + 1, _up);
						tfmExceptionMap.put(no, _down);
					} else {
						TfmCompersation _up = tfmComprasationMap.remove(no);
						TfmCompersation _down = tfmComprasationMap
								.remove(no + 1);
						_up.setSeqNo(no + 1 + "");
						_down.setSeqNo(no + "");
						tfmComprasationMap.put(no + 1, _up);
						tfmComprasationMap.put(no, _down);
					}
					table.setSelection(index + 1);

				} else if (b.getText().equals("删除")) {
					int index = table.getSelectionIndex();

					if (index < 0)
						return;
					else {

						if (exception.getSelection()) {
							tfmExceptionMap.remove(Integer.parseInt(table
									.getItem(index).getText(0)));
							table.remove(index);
							for (int i = index; i <= table.getItemCount() - 1; i++) {
								int no = Integer.parseInt(table.getItem(i)
										.getText(0));
								TfmException temp = tfmExceptionMap.remove(no);
								temp.setSeqNo(no - 1 + "");
								tfmExceptionMap.put(no - 1, temp);
								table.getItem(i).setText(0, no - 1 + "");
							}
						} else {
							tfmComprasationMap.remove(Integer.parseInt(table
									.getItem(index).getText(0)));
							table.remove(index);
							for (int i = index; i <= table.getItemCount() - 1; i++) {
								int no = Integer.parseInt(table.getItem(i)
										.getText(0));
								TfmCompersation temp = tfmComprasationMap
										.remove(no);
								temp.setSeqNo(no - 1 + "");
								tfmComprasationMap.put(no - 1, temp);
								table.getItem(i).setText(0, no - 1 + "");
							}
						}

					}
				} else if (b.getText().equals("新建")) {
					seqno.setText(Integer.toString(table.getItemCount() + 1));
					name.setText("");
					desc.setText("");
					dllid.setText("");
					funcname.removeAll();
					name.setFocus();
					return;
				} else if (b.getText().equals("保存")) {
					int no = Integer.parseInt(seqno.getText());
					if (exception.getSelection()) {
						TfmException newExtend = new TfmException();
						newExtend.setDesc(desc.getText());
						newExtend.setDllId(dllid.getText());
						newExtend.setFuncname(funcname.getText());
						newExtend.setSeqNo(seqno.getText());
						newExtend.setName(name.getText());
						newExtend.setTfmId(tfmid.getText());
						if (tfmExceptionMap.containsKey(no))
							tfmExceptionMap.remove(no);
						tfmExceptionMap.put(no, newExtend);
						if (no <= table.getItemCount())
							table.remove(no - 1);
						TableItem curr = new TableItem(table, SWT.NONE);
						curr.setText(new String[] {
								tfmExceptionMap.get(no).getSeqNo(),
								tfmExceptionMap.get(no).getName(),
								tfmExceptionMap.get(no).getDllId(),
								tfmExceptionMap.get(no).getFuncname() });
					} else {
						TfmCompersation newExtend = new TfmCompersation();
						newExtend.setDesc(desc.getText());
						newExtend.setDllId(dllid.getText());
						newExtend.setFuncname(funcname.getText());
						newExtend.setSeqNo(seqno.getText());
						newExtend.setName(name.getText());
						newExtend.setTfmId(tfmid.getText());
						if (tfmComprasationMap.containsKey(no)) {
							tfmComprasationMap.remove(no);
						}
						tfmComprasationMap.put(no, (TfmCompersation) newExtend);
						if (no <= table.getItemCount())
							table.remove(no - 1);
						TableItem curr = new TableItem(table, SWT.NONE);
						curr.setText(new String[] {
								tfmComprasationMap.get(no).getSeqNo(),
								tfmComprasationMap.get(no).getName(),
								tfmComprasationMap.get(no).getDllId(),
								tfmComprasationMap.get(no).getFuncname() });
					}
					name.setText("");
					desc.setText("");
					seqno.setText("");
					save.setEnabled(false);
					Button btn = getButton(APPLICATION_ID);
					Button btnOK = getButton(IDialogConstants.OK_ID);
					if ((btn != null) && (btnOK != null)) {
						getButton(APPLICATION_ID).setEnabled(true);
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				}
				up.setEnabled(true);
				down.setEnabled(true);
				if (table.getSelectionIndex() == 0 && table.getItemCount() == 1) {
					up.setEnabled(false);
					down.setEnabled(false);
				} else if (table.getSelectionIndex() == table.getItemCount() - 1
						&& table.getItemCount() > 1) {
					down.setEnabled(false);
				} else if (table.getSelectionIndex() == 0
						&& table.getItemCount() > 1) {
					up.setEnabled(false);
				} else {
					up.setEnabled(true);
					down.setEnabled(true);
				}

			}
		};
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int index = table.getSelectionIndex();
				if (index == 0 && table.getItemCount() > 1) {
					up.setEnabled(false);
					down.setEnabled(true);

				} else if (index == table.getItemCount() - 1 && index != 0) {
					up.setEnabled(true);
					down.setEnabled(false);

				} else if (index > 0 && index < table.getItemCount() - 1) {
					up.setEnabled(true);
					down.setEnabled(true);

				} else {
					up.setEnabled(false);
					down.setEnabled(false);
				}
				if (index >= 0) {
					TfmCommDeal temp;
					int no = Integer.parseInt(table.getItem(index).getText(0));
					if (exception.getSelection())
						temp = tfmExceptionMap.get(no);
					else
						temp = tfmComprasationMap.get(no);
					desc.setText(temp.getDesc());
					dllid.setText(temp.getDllId());
					funcname.setText(temp.getFuncname());
					name.setText(temp.getName());
					seqno.setText(temp.getSeqNo());
					del.setEnabled(true);
				} else
					del.setEnabled(false);
				save.setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		table.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

				// del.setEnabled(false);
				// up.setEnabled(false);
				// down.setEnabled(false);
			}

			@Override
			public void focusGained(FocusEvent e) {

				save.setEnabled(false);
			}
		});
		del.addSelectionListener(listener);
		down.addSelectionListener(listener);
		up.addSelectionListener(listener);
		newb.addSelectionListener(listener);
		save.addSelectionListener(listener);
		tfmid.setText(contentsModel.diagramId + "");

		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setBounds(10, 108, 61, 17);
		lblNewLabel_1.setText("\u63CF\u8FF0\uFF1A");
		for (TfmException curr : contentsModel.getTfmBean()
				.getTfmExceptionList()) {
			int no = Integer.parseInt(curr.getSeqNo());
			tfmExceptionMap.put(no, curr);
			TableItem currItem = new TableItem(table, SWT.NONE);
			currItem.setText(new String[] { tfmExceptionMap.get(no).getSeqNo(),
					tfmExceptionMap.get(no).getName(),
					tfmExceptionMap.get(no).getDllId(),
					tfmExceptionMap.get(no).getFuncname() });
			// currItem.setBackground(ColorConstants.gray);
		}
		for (TfmCompersation curr : contentsModel.getTfmBean()
				.getTfmCompersationList()) {
			int no = Integer.parseInt(curr.getSeqNo());
			tfmComprasationMap.put(no, curr);
			// TableItem currItem = new TableItem(table, SWT.NONE);
			// currItem.setText(new String[]{
			// tfmComprasationMap.get(no).getSeqNo(),
			// tfmComprasationMap.get(no).getName(),
			// tfmComprasationMap.get(no).getDllId(),
			// tfmComprasationMap.get(no).getFuncname()
			// });
		}
		return area;
	}

	protected boolean check() {
		if (exception.getSelection()) {
			for (TfmException curr : tfmExceptionMap.values()) {
				if (curr.getDllId().equals(dllid.getText()))
					if (curr.getFuncname().equals(funcname.getText()))
						return false;
			}
		} else if (comprasation.getSelection()) {
			for (TfmCompersation curr : tfmComprasationMap.values()) {
				if (curr.getDllId().equals(dllid.getText()))
					if (curr.getFuncname().equals(funcname.getText()))
						return false;
			}
		}
		return true;

	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			store();
			setReturnCode(IDialogConstants.OK_ID);
			close();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			setReturnCode(IDialogConstants.CANCEL_ID);
			close();
		} else if (APPLICATION_ID == buttonId)
			store();
	}

	protected void store() {

		contentsModel.getTfmBean().getTfmExceptionList().clear();
		contentsModel.getTfmBean().getTfmCompersationList().clear();
		for (TfmException curr : tfmExceptionMap.values())
			contentsModel.getTfmBean().getTfmExceptionList().add(curr);
		for (TfmCompersation curr : tfmComprasationMap.values())
			contentsModel.getTfmBean().getTfmCompersationList().add(curr);
	}
}
