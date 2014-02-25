package dev.diagram.inputDialog;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import dev.db.DbConnFactory;
import dev.db.DbConnectImpl;
import dev.diagram.beans.TfmBlock;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ContentsModel;
import dev.util.CommonUtil;

/**
 * 开始和结束块对应的对话框，具体说明请参考
 * <p>
 * BlockConfigureDialog.java
 * </p>
 * 
 * @author 木木
 * 
 */
public class StartAndEndDialog extends Dialog
{
	private Text tfmid, nodetype, nodeid, dllid, desc;
	private Combo funcname;
	private TfmBlock block;
	SelectionAdapter listener;
	VerifyListener listenerSaveActive;
	private CommonModel commonModel;
	private ContentsModel contentsModel;
	public static final int iAPPLICATION_ID = 100001;

	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, "确定", false);
		createButton(parent, iAPPLICATION_ID, "保存", false);
		createButton(parent, IDialogConstants.CANCEL_ID, "取消", false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		getButton(iAPPLICATION_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(true);
		getButton(iAPPLICATION_ID).addSelectionListener(new SelectionListener()
		{

			@Override
			public void widgetSelected(SelectionEvent e)
			{

				// getButton(IOK_ID).setEnabled(false);
				store();
				getButton(iAPPLICATION_ID).setEnabled(false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}
		});
	}

	@Override
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText(block.getName());
	}

	public StartAndEndDialog(Shell parentShell, CommonModel commonModel,
			ContentsModel contentsModel)
	{
		super(parentShell);
		this.commonModel = commonModel;
		this.block = commonModel.getTfmBlock();
		this.contentsModel = contentsModel;
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{

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
		tfmid.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

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
		desc.setText("\u63CF\u8FF0\u4FE1\u606F...");

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
		addbt.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{

				DllIdConfigureDialog dialog = new DllIdConfigureDialog(
						getShell(),contentsModel);
				if (Window.OK == dialog.open())
				{
					dllid.setText(dialog.getText());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e)
			{

			}
		});
		dllid.addModifyListener(new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{
				if (dllid.getText().isEmpty() == false)
				{

					funcname.removeAll();
					DbConnectImpl dbConnectImpl = DbConnFactory.dbConnCreator();
					String sql = "select aopid from aop where upaopdll= "
							+ dllid.getText();
					ResultSet rs;
					try
					{
						dbConnectImpl.openConn(CommonUtil
								.initPs(contentsModel.projectId));
						rs = dbConnectImpl.retrive(sql);
						while (rs.next())
							funcname.add(rs.getString(1));
						dbConnectImpl.closeConn();
					} catch (SQLException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		});
		dllid.setText(block.getDllId());
		nodeid.setText(block.getNodeId());
		nodetype.setText(commonModel.getTypeName());
		tfmid.setText(block.getTfmId());
		funcname.setText(block.getAopName());
		if (block.getDesc().isEmpty())
		{
			desc.setText("描述信息...");
		} else
		{
			desc.setText(block.getDesc());
		}
		ModifyListener confirm = new ModifyListener()
		{

			@Override
			public void modifyText(ModifyEvent e)
			{

				if (dllid.getText().length() != 0
						&& funcname.getText().length() != 0)
				{
					Button btn = getButton(iAPPLICATION_ID);
					Button btnOK = getButton(IDialogConstants.OK_ID);
					if ((btn != null) && (btnOK != null))
					{
						getButton(iAPPLICATION_ID).setEnabled(true);
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				} else
				{
					Button btn = getButton(iAPPLICATION_ID);
					Button btnOK = getButton(IDialogConstants.OK_ID);
					if ((btn != null) && (btnOK != null))
					{
						getButton(iAPPLICATION_ID).setEnabled(false);
						getButton(IDialogConstants.OK_ID).setEnabled(false);
					}
				}
			}
		};
		funcname.addModifyListener(confirm);
		desc.addModifyListener(confirm);
		return area;
	}

	@Override
	protected void buttonPressed(int buttonId)
	{

		if (IDialogConstants.OK_ID == buttonId)
		{
			store();
			setReturnCode(IDialogConstants.OK_ID);
			close();
		} else if (iAPPLICATION_ID == buttonId)
		{
			store();
		} else if (IDialogConstants.CANCEL_ID == buttonId)
		{
			setReturnCode(IDialogConstants.CANCEL_ID);
			close();
		}
	}

	protected void store()
	{

		block.setDllId(dllid.getText());
		block.setAopName(funcname.getText());
		block.setTfmId(tfmid.getText());
		block.setDesc(desc.getText());
		block.getTfmExtendAopsList().clear();
	}
}
