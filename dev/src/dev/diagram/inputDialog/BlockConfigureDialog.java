package dev.diagram.inputDialog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
import dev.diagram.beans.TfmBlock;
import dev.diagram.beans.TfmExtendAop;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;

/**
 * TFM,AOP，返回节点信息配置对话框
 * 
 * @author 木木
 * 
 */
public class BlockConfigureDialog extends Dialog {
	private Text tfmid, nodetype, nodeid, dllid, desc;
	private Combo funcname;
	private Text extenddllid, extendseqno, extendname, extenddesc;
	private Button up, down, del, newb, save;
	private Combo extendfuncname;
	private TfmBlock block;
	private Table table;
	private Button endextend;
	private Button firstextend;
	SelectionAdapter listener;
	ModifyListener listenerSaveActive;
	SelectionListener extentsSelectLististner;
	private ContentsModel contentsModel;
	// map保存子对话框选择的信息
	Map<String, String> map = new HashMap<String, String>();
	// tfmExtendsMap用来保存已经配置的扩展点
	Map<Integer, TfmExtendAop> tfmExtendsMap = new TreeMap<Integer, TfmExtendAop>();
	private TableColumn t_seqno;
	// 工具栏保存按钮
	public static final int iAPPLICATION_ID = 100001;
	private CommonModel commonModel;

	/**
	 * 创建工具栏按钮，包括 确定，保存，取消
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "确定", false);
		createButton(parent, iAPPLICATION_ID, "保存", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		getButton(OK).setEnabled(false);
		getButton(iAPPLICATION_ID).setEnabled(false);
		getButton(CANCEL).setEnabled(true);
		getButton(iAPPLICATION_ID).addSelectionListener(
				new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						store();
						getButton(iAPPLICATION_ID).setEnabled(false);
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {

					}
				});
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(block.getName());
	}

	public BlockConfigureDialog(Shell parentShell, CommonModel commonModel,
			ContentsModel contentsModel) {
		super(parentShell);
		this.commonModel = commonModel;
		this.block = commonModel.getTfmBlock();
		this.contentsModel = contentsModel;

	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		area.setLayout(null);

		TabFolder tabFolder = new TabFolder(area, SWT.NONE);
		tabFolder.setBounds(10, 10, 418, 351);

		TabItem tablefold_1 = new TabItem(tabFolder, SWT.NONE);
		tablefold_1.setText("\u57FA\u672C\u4FE1\u606F");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tablefold_1.setControl(composite);

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 72, 17);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel.setText("\u6240\u5C5E\u56FE\u7F16\u53F7\uFF1A");

		tfmid = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		tfmid.setBounds(88, 7, 110, 23);
		tfmid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Button addbt = new Button(composite, SWT.NONE);
		addbt.setBounds(200, 65, 19, 23);
		addbt.setText("...");

		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBounds(229, 10, 53, 17);
		lblNewLabel_2.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblNewLabel_2.setText("\u5757\u7F16\u53F7\uFF1A");

		nodeid = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		nodeid.setBounds(286, 7, 114, 23);
		nodeid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		nodeid.setEditable(false);

		Label lblNewLabel_5 = new Label(composite, SWT.NONE);
		lblNewLabel_5.setBounds(229, 68, 53, 17);
		lblNewLabel_5.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblNewLabel_5.setText("AOP\uFF1A");

		funcname = new Combo(composite, SWT.READ_ONLY);
		funcname.setBounds(286, 65, 114, 25);
		funcname.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

		Label label1 = new Label(composite, SWT.NONE);
		label1.setBounds(10, 91, 38, 17);
		label1.setText("\u63CF\u8FF0\uFF1A");

		desc = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		desc.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		desc.setBounds(10, 114, 390, 197);
		desc.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

		Label lblNewLabel_1 = new Label(composite, SWT.NONE);
		lblNewLabel_1.setBounds(10, 39, 72, 17);
		lblNewLabel_1.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblNewLabel_1.setText("\u8282\u70B9\u7C7B\u578B\uFF1A");

		nodetype = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		nodetype.setBounds(88, 36, 110, 23);
		nodetype.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		nodetype.setEditable(false);

		Label lblNewLabel_3 = new Label(composite, SWT.NONE);
		lblNewLabel_3.setBounds(10, 68, 72, 17);
		lblNewLabel_3.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WHITE));
		lblNewLabel_3.setText("\u52A8\u6001\u5E93\uFF1A");

		dllid = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
		dllid.setBounds(88, 65, 110, 23);
		dllid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		dllid.setEditable(false);
		addbt.addSelectionListener(new SelectionListener() {
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
		// dllid增加modifyListener监听器，即配置了dllid后，根据配置的dllid，提供函数名称
		dllid.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				funcname.removeAll();
				DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

				if (dllid.getText().length() != 0) {
					String sql = "select aopid from aop where upaopdll= "
							+ dllid.getText();
					ResultSet rs;
					try {
						dbConnectImpl.openConn(CommonUtil
								.initPs(contentsModel.projectId));
						rs = dbConnectImpl.retrive(sql);
						while (rs.next())
							// 根据dllid，添加函数名称
							funcname.add(rs.getString(1));
						dbConnectImpl.closeConn();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		nodeid.setText(block.getNodeId());
		dllid.setText(block.getDllId());
		nodetype.setText(commonModel.getTypeName());
		funcname.setText(block.getAopName());
		tfmid.setText(contentsModel.diagramId + "");

		TabItem tablefold_2 = new TabItem(tabFolder, SWT.NONE);
		tablefold_2.setText("\u6269\u5C55\u70B9");

		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tablefold_2.setControl(composite_1);

		Label label = new Label(composite_1, SWT.NONE);
		label.setBounds(10, 178, 97, 17);
		label.setText("\u5DF2\u5B9A\u4E49\u7684\u6269\u5C55\u70B9");

		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setBounds(242, 59, 45, 15);
		lblNewLabel_4.setText("\u52A8\u6001\u5E93\uFF1A");

		extenddllid = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY);
		extenddllid.setBounds(310, 56, 79, 22);

		Label lblNewLabel_7 = new Label(composite_1, SWT.NONE);
		lblNewLabel_7.setBounds(242, 84, 58, 15);
		lblNewLabel_7.setText("\u51FD\u6570\u540D\u79F0\uFF1A");

		extendfuncname = new Combo(composite_1, SWT.READ_ONLY);
		extendfuncname.setBounds(310, 81, 79, 17);

		Label lblNewLabel_9 = new Label(composite_1, SWT.NONE);
		lblNewLabel_9.setBounds(242, 36, 58, 17);
		lblNewLabel_9.setText("\u987A\u5E8F\u53F7\uFF1A");

		extendseqno = new Text(composite_1, SWT.BORDER | SWT.READ_ONLY);
		extendseqno.setBounds(310, 30, 79, 23);

		final Button exbt = new Button(composite_1, SWT.NONE);
		exbt.setBounds(390, 56, 16, 22);
		exbt.setText("...");

		up = new Button(composite_1, SWT.NONE);
		up.setBounds(116, 175, 28, 23);
		up.setText("\u4E0A\u79FB");
		up.setEnabled(false);

		del = new Button(composite_1, SWT.NONE);
		del.setBounds(344, 175, 45, 23);
		del.setText("\u5220\u9664");
		del.setEnabled(false);

		down = new Button(composite_1, SWT.NONE);
		down.setBounds(151, 175, 28, 23);
		down.setText("\u4E0B\u79FB");
		down.setEnabled(false);

		Label lblNewLabel_8 = new Label(composite_1, SWT.NONE);
		lblNewLabel_8.setBounds(10, 10, 72, 17);
		lblNewLabel_8.setText("\u6269\u5C55\u70B9\u7C7B\u578B\uFF1A");

		firstextend = new Button(composite_1, SWT.RADIO);
		firstextend.setBounds(88, 10, 72, 17);
		firstextend.setText("\u524D\u6269\u5C55\u70B9");
		firstextend.setSelection(true);

		endextend = new Button(composite_1, SWT.RADIO);
		endextend.setBounds(164, 10, 72, 17);
		endextend.setText("\u540E\u6269\u5C55\u70B9");

		Label lblNewLabel_10 = new Label(composite_1, SWT.NONE);
		lblNewLabel_10.setBounds(10, 33, 85, 17);
		lblNewLabel_10.setText("\u6269\u5C55\u70B9\u540D\u79F0\uFF1A");

		extendname = new Text(composite_1, SWT.BORDER);
		extendname.setBounds(10, 56, 226, 28);

		extenddesc = new Text(composite_1, SWT.BORDER | SWT.V_SCROLL);
		extenddesc.setBounds(10, 112, 379, 60);

		newb = new Button(composite_1, SWT.NONE);
		newb.setBounds(242, 175, 45, 23);
		newb.setText("\u65B0\u5EFA");

		save = new Button(composite_1, SWT.NONE);
		save.setBounds(293, 175, 45, 23);
		save.setText("\u4FDD\u5B58");
		save.setEnabled(false);

		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setBounds(10, 201, 379, 110);

		t_seqno = new TableColumn(table, SWT.NONE);
		t_seqno.setWidth(100);
		t_seqno.setText("\u987A\u5E8F\u53F7");
		table.setSortColumn(t_seqno);
		table.setSortDirection(SWT.UP);

		TableColumn t_name = new TableColumn(table, SWT.NONE);
		t_name.setWidth(89);
		t_name.setText("\u540D\u79F0");

		TableColumn t_dll = new TableColumn(table, SWT.NONE);
		t_dll.setWidth(76);
		t_dll.setText("\u52A8\u6001\u5E93");

		TableColumn t_func = new TableColumn(table, SWT.NONE);
		t_func.setWidth(100);
		t_func.setText("\u51FD\u6570\u540D\u79F0");

		Label lblNewLabel_6 = new Label(composite_1, SWT.NONE);
		lblNewLabel_6.setBounds(10, 89, 61, 17);
		lblNewLabel_6.setText("\u63CF\u8FF0\uFF1A");
		/**
		 * 节点dll配置，打开一个子对话框，选择dll并保存在map里面，用于根据当前dll，提供原子交易名称
		 */
		// 前扩展点和后扩展点选择监听器
		extentsSelectLististner = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				extendname.setText("");
				extenddesc.setText("");
				extendseqno.setText("");
				extenddllid.setText("");
				extendfuncname.setText("");
				Button temp = (Button) e.widget;
				if (temp.getSelection())
					// 当前选中了前扩展点
					if (temp.getText().equals("前扩展点")) {
						// 清空table
						table.removeAll();
						// 显示当前扩展点类型的扩展点信息
						for (int curr : tfmExtendsMap.keySet())
							// 前扩展点编号小于100，过滤编号大于100的
							if (Integer.parseInt(tfmExtendsMap.get(curr)
									.getSeqNo()) < 100) {
								TableItem tabItem = new TableItem(table,
										SWT.NONE);
								tabItem.setText(new String[] {
										tfmExtendsMap.get(curr).getSeqNo(),
										tfmExtendsMap.get(curr).getName(),
										tfmExtendsMap.get(curr).getDllId(),
										tfmExtendsMap.get(curr).getFuncname() });
							}
					}
					// 选择了后扩展点
					else {
						table.removeAll();
						for (int curr : tfmExtendsMap.keySet())
							if (Integer.parseInt(tfmExtendsMap.get(curr)
									.getSeqNo()) >= 100) {
								TableItem tabItem = new TableItem(table,
										SWT.NONE);
								tabItem.setText(new String[] {
										tfmExtendsMap.get(curr).getSeqNo(),
										tfmExtendsMap.get(curr).getName(),
										tfmExtendsMap.get(curr).getDllId(),
										tfmExtendsMap.get(curr).getFuncname() });
							}
					}
				// extendseqno.setText(String.valueOf(table.getItemCount() +1));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}

		};
		// 为前扩展点按钮增加选择监听器
		firstextend.addSelectionListener(extentsSelectLististner);
		// 为后扩展点按钮增加选择监听器
		endextend.addSelectionListener(extentsSelectLististner);
		// 扩展节点的dllid监听器 同节点的dllidModifyListener
		extenddllid.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (extenddllid.getText().isEmpty())
					return;
				extendfuncname.removeAll();
				DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();

				String sql = "select aopid from aop where upaopdll= "
						+ extenddllid.getText();
				ResultSet rs;
				try {
					dbConnectImpl.openConn(CommonUtil
							.initPs(contentsModel.projectId));
					rs = dbConnectImpl.retrive(sql);
					while (rs.next())
						extendfuncname.add(rs.getString(1));
					dbConnectImpl.closeConn();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

		exbt.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DllIdConfigureDialog dialog = new DllIdConfigureDialog(
						getShell(), contentsModel);
				if (Window.OK == dialog.open()) {
					extenddllid.setText(dialog.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		// 扩展点的ModifyListener监听器，当所有配置信息都配置后，激活保存按钮
		listenerSaveActive = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				if (extenddllid.getText().length() != 0
						&& extendfuncname.getText().length() != 0
						&& extendname.getText().length() != 0
						&& extendseqno.getText().length() != 0) {
					save.setEnabled(true);
				} else
					save.setEnabled(false);
			}
		};
		extenddesc.addModifyListener(listenerSaveActive);
		extendfuncname.addModifyListener(listenerSaveActive);
		extendname.addModifyListener(listenerSaveActive);
		extendseqno.addModifyListener(listenerSaveActive);

		// 上移，下移，删除，保存按钮的选择监听器
		listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				super.widgetSelected(e);
				int index = table.getSelectionIndex();
				Button b = (Button) e.widget;
				// 点击上移
				if (b.getText().equals("上移")) {
					// table中的已选行的索引

					int no = Integer.parseInt(table.getItem(index).getText(0));
					if (index <= 0)
						return;
					// 上移后index-1行的信息
					String[] down = new String[] {
							table.getItem(index - 1).getText(0),
							table.getItem(index).getText(1),
							table.getItem(index).getText(2),
							table.getItem(index).getText(3) };
					// 上移后index行的信息
					String[] up = new String[] {
							table.getItem(index).getText(0),
							table.getItem(index - 1).getText(1),
							table.getItem(index - 1).getText(2),
							table.getItem(index - 1).getText(3) };
					table.getItem(index - 1).setText(down);
					table.getItem(index).setText(up);
					table.setSelection(index - 1);
					// 删除Treemap里面要交换的俩个节点并保存，目的是修改她们里面的序列号
					TfmExtendAop _down = tfmExtendsMap.remove(no);
					TfmExtendAop _up = tfmExtendsMap.remove(no - 1);
					_down.setSeqNo(no - 1 + "");
					_up.setSeqNo(no + "");
					// 修改后存入TreeMap里面
					tfmExtendsMap.put(no - 1, _down);
					tfmExtendsMap.put(no, _up);
					// 点击下移，所做的操作类似上移，请参考上移操作，其实此处可以写个函数来操作移动，上移和下移时传递不同的参数即可
				} else if (b.getText().equals("下移")) {
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
					table.setSelection(index + 1);
					TfmExtendAop _down = tfmExtendsMap.remove(no + 1);
					TfmExtendAop _up = tfmExtendsMap.remove(no);
					_down.setSeqNo(no + "");
					_up.setSeqNo(no + 1 + "");
					tfmExtendsMap.put(no, _down);
					tfmExtendsMap.put(no + 1, _up);
					// 点击删除
				} else if (b.getText().equals("删除")) {
					if (index < 0)
						return;
					else {
						// 先删除TfmExtendsMap里面的节点
						tfmExtendsMap.remove(Integer.parseInt(table.getItem(
								index).getText(0)));
						// 删除table中的选中的行
						table.remove(index);
						// 修改被删除行后面的各行的序列
						for (int i = index; i <= table.getItemCount() - 1; i++) {
							int no = Integer.parseInt(table.getItem(i).getText(
									0));
							TfmExtendAop temp = tfmExtendsMap.remove(no);
							temp.setSeqNo(no - 1 + "");
							tfmExtendsMap.put(no - 1, temp);
							table.getItem(i).setText(0, no - 1 + "");

						}
					}
					// 点击新建
				} else if (b.getText().equals("新建")) {
					// 如果前扩展点，序列号从1开始
					if (firstextend.getSelection())
						extendseqno
								.setText(String.valueOf(table.getItemCount() + 1));
					// 后扩展点，序列号从101开始
					else
						extendseqno
								.setText(String.valueOf(table.getItemCount() + 101));
					extendname.setText("");
					extenddesc.setText("");
					extenddllid.setText("");
					extendfuncname.removeAll();
					extendname.setFocus();
					return;

				}
				// 点击保存
				else if (b.getText().equals("保存")) {
					// 判断是新增加扩展点
					if (Integer.parseInt(extendseqno.getText()) == table
							.getItemCount() + 1
							|| Integer.parseInt(extendseqno.getText()) == table
									.getItemCount() + 101) {
						TfmExtendAop newExtend = new TfmExtendAop();
						newExtend.setDesc(extenddesc.getText());
						newExtend.setDllId(extenddllid.getText());
						newExtend.setFuncname(extendfuncname.getText());
						newExtend.setNodeid(block.getNodeId());
						newExtend.setSeqNo(extendseqno.getText());
						newExtend.setName(extendname.getText());
						newExtend.setTfmId(block.getTfmId());
						int NO = Integer.parseInt(extendseqno.getText());
						// tfmExtendsMap新增加一项
						tfmExtendsMap.put(NO, newExtend);
						// table中新增加一行
						TableItem curr = new TableItem(table, SWT.NONE);
						curr.setText(new String[] {
								tfmExtendsMap.get(NO).getSeqNo(),
								tfmExtendsMap.get(NO).getName(),
								tfmExtendsMap.get(NO).getDllId(),
								tfmExtendsMap.get(NO).getFuncname() });
					}
					// 判断是对已存在信息的修改
					else {
						// 修改table中的行
						table.getItem(index).setText(
								new String[] { extendseqno.getText(),
										extendname.getText(),
										extenddllid.getText(),
										extendfuncname.getText() });
						int NO = Integer.parseInt(extendseqno.getText());
						// 修改tfmExtendsmap
						tfmExtendsMap.get(NO).setDesc(extenddesc.getText());
						tfmExtendsMap.get(NO).setDllId(extenddllid.getText());
						tfmExtendsMap.get(NO).setFuncname(
								extendfuncname.getText());
						tfmExtendsMap.get(NO).setName(extendname.getText());
					}
					extendname.setText("");
					extenddesc.setText("");
					extendseqno.setText("");
					save.setEnabled(false);
					Button btn = getButton(iAPPLICATION_ID);
					Button btnOK = getButton(IDialogConstants.OK_ID);
					if ((btn != null) && (btnOK != null)) {
						getButton(iAPPLICATION_ID).setEnabled(true);
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
		// table的选择监听器
		table.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				int index = table.getSelectionIndex();
				if (index > 0 && index < table.getItemCount() - 1) {
					up.setEnabled(true);
					down.setEnabled(true);
				} else if (index == table.getItemCount() - 1 && index == 0) {
					up.setEnabled(false);
					down.setEnabled(false);
				} else if (index == 0) {
					up.setEnabled(false);
					down.setEnabled(true);
				} else if (index == table.getItemCount() - 1) {
					up.setEnabled(true);
					down.setEnabled(false);
				}
				// 显示选中项对应的各项信息
				if (index >= 0) {
					TfmExtendAop temp = tfmExtendsMap.get(Integer
							.parseInt(table.getItem(index).getText(0)));
					extenddesc.setText(temp.getDesc());
					extenddllid.setText(temp.getDllId());
					extendfuncname.setText(temp.getFuncname());
					extendname.setText(temp.getName());
					extendseqno.setText(temp.getSeqNo());
					if (Integer.parseInt(temp.getSeqNo()) < 100) {
						firstextend.setSelection(true);
						endextend.setSelection(false);
					} else {
						endextend.setSelection(true);
						firstextend.setSelection(false);
					}
					del.setEnabled(true);
				} else {
					del.setEnabled(false);
				}
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
		if (block.getDesc().isEmpty()) {
			desc.setText("描述信息...");
		} else {
			desc.setText(block.getDesc());
		}
		// 初始化扩展点信息
		for (TfmExtendAop curr : block.getTfmExtendAopsList()) {
			tfmExtendsMap.put(Integer.parseInt(curr.getSeqNo()), curr);
			// 显示前扩展点
			if (Integer.parseInt(curr.getSeqNo()) < 100) {

				TableItem currItem = new TableItem(table, SWT.NONE);
				int NO = Integer.parseInt(curr.getSeqNo());
				currItem.setText(new String[] {
						tfmExtendsMap.get(NO).getSeqNo(),
						tfmExtendsMap.get(NO).getName(),
						tfmExtendsMap.get(NO).getDllId(),
						tfmExtendsMap.get(NO).getFuncname() });
			}
		}
		ModifyListener confirm = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {

				if (funcname.getText() != "" && dllid.getText().length() != 0) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					getButton(iAPPLICATION_ID).setEnabled(true);
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					getButton(iAPPLICATION_ID).setEnabled(false);
				}
			}
		};
		funcname.addModifyListener(confirm);
		desc.addModifyListener(confirm);
		return area;
	}

	protected boolean check() {

		for (TfmExtendAop curr : tfmExtendsMap.values()) {
			if (curr.getDllId().equals(extenddllid.getText()))
				if (curr.getFuncname().equals(extendfuncname.getText()))
					return false;
		}
		return true;
	}

	@Override
	protected void buttonPressed(int buttonId) {

		if (IDialogConstants.OK_ID == buttonId) {
			store();
			setReturnCode(IDialogConstants.OK_ID);
			close();
		} else if (iAPPLICATION_ID == buttonId) {
			store();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			setReturnCode(IDialogConstants.CANCEL_ID);
			close();

		}
	}

	/**
	 * 将当前配置的节点信息写入块中
	 */
	protected void store() {

		block.setDllId(dllid.getText());
		block.setAopName(funcname.getText());
		block.setTfmId(tfmid.getText());
		block.setDesc(desc.getText());
		block.getTfmExtendAopsList().clear();
		for (TfmExtendAop curr : tfmExtendsMap.values())
			block.getTfmExtendAopsList().add(curr);
	}
}
