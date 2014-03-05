/* 文件名：       PrjPropertyPage2.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.统一使用File.separator；
 *         3.增加对服务器地址、服务器用户和服务器口令的处理。
 */

package dev.properties;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

import dev.Activator;
import dev.model.base.TreeNode;
import dev.util.DevLogger;
import dev.views.NavView;

/**
 * 该类定义了工程的第二个属性页，包括工程对用的数据库的全部信息
 */
public class PrjPropertyPage2 extends PropertyPage implements
		IWorkbenchPropertyPage {

	private Text dbAddressText;
	private Text dbPortText;
	private Text dbInstanceText;
	private Text dbUserText;
	private Text dbPwdText;
	private Text remoteAddressText;
	private Text remoteUserText;
	private Text remotePwdText;

	private PreferenceStore ps;
	private String propertyPath = "";

	// private LinkedHashMap<String,String> map;

	public PrjPropertyPage2() {
		setMessage("工程数据库/服务器属性");
		initPropertyPath();
		initPreferenceStore();
	}

	/**
	 * 该方法用于获取工程对应的properties文件的路径
	 */
	private void initPropertyPath() {
		TreeViewer treeViewer = ((NavView) Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID)).getTreeViewer();
		StructuredSelection select = (StructuredSelection) treeViewer
				.getSelection();
		String prjId = ((TreeNode) select.getFirstElement()).getName();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(prjId);
		propertyPath = project.getLocationURI().toString().substring(6)
				+ File.separator + prjId + ".properties";
	}

	/**
	 * 该方法用于加载工程对应的properties文件到ps
	 */
	public void initPreferenceStore() {
		ps = new PreferenceStore(propertyPath);
		ps.setDefault("dbAddress", "");
		ps.setDefault("dbPort", "");
		ps.setDefault("dbInstance", "");
		ps.setDefault("dbUser", "");
		ps.setDefault("dbPwd", "");
		ps.setDefault("remoteAddress", "");
		ps.setDefault("remoteUser", "");
		ps.setDefault("remotePwd", "");
		try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		Label dbAddressLabel = new Label(container, SWT.NONE);
		dbAddressLabel.setText("*数据库地址:");

		dbAddressText = new Text(container, SWT.BORDER);
		dbAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dbAddressText.setText(ps.getString("dbAddress"));

		Label dbPortLabel = new Label(container, SWT.NONE);
		dbPortLabel.setText("*数据库端口:");

		dbPortText = new Text(container, SWT.BORDER);
		dbPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dbPortText.setText(ps.getString("dbPort"));

		Label dbInstanceLabel = new Label(container, SWT.NONE);
		dbInstanceLabel.setText("*数据库实例:");

		dbInstanceText = new Text(container, SWT.BORDER);
		dbInstanceText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dbInstanceText.setText(ps.getString("dbInstance"));

		Label dbUserLabel = new Label(container, SWT.NONE);
		dbUserLabel.setText("*数据库用户:");

		dbUserText = new Text(container, SWT.BORDER);
		dbUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dbUserText.setText(ps.getString("dbUser"));

		Label dbPwdLabel = new Label(container, SWT.NONE);
		dbPwdLabel.setText("*数据库口令:");

		dbPwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		dbPwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		dbPwdText.setText(ps.getString("dbPwd"));

		Label remoteAddressLabel = new Label(container, SWT.NONE);
		remoteAddressLabel.setText("*服务器地址：");

		remoteAddressText = new Text(container, SWT.BORDER);
		remoteAddressText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		remoteAddressText.setText(ps.getString("remoteAddress"));

		Label remoteUserLabel = new Label(container, SWT.NONE);
		remoteUserLabel.setText("*服务器用户：");

		remoteUserText = new Text(container, SWT.BORDER);
		remoteUserText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		remoteUserText.setText(ps.getString("remoteUser"));

		Label remotePwdLabel = new Label(container, SWT.NONE);
		remotePwdLabel.setText("*服务器口令：");

		remotePwdText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		remotePwdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		remotePwdText.setText(ps.getString("remotePwd"));

		try {
			ps.save();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		return container;
	}

	@Override
	public boolean performOk() {
		if (dbAddressText.getText().isEmpty() || dbPortText.getText().isEmpty()
				|| dbInstanceText.getText().isEmpty()
				|| dbUserText.getText().isEmpty()
				|| dbPwdText.getText().isEmpty()
				|| remoteAddressText.getText().isEmpty()
				|| remoteUserText.getText().isEmpty()
				|| remotePwdText.getText().isEmpty()) {
			setErrorMessage("请将信息输入完整");
			return false;
		}
		setErrorMessage(null);

		try {
			ps.load();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		ps.setValue("dbAddress", dbAddressText.getText().trim());
		ps.setValue("dbPort", dbPortText.getText().trim());
		ps.setValue("dbInstance", dbInstanceText.getText().trim());
		ps.setValue("dbUser", dbUserText.getText().trim());
		ps.setValue("dbPwd", dbPwdText.getText().trim());
		ps.setValue("remoteAddress", remoteAddressText.getText().trim());
		ps.setValue("remoteUser", remoteUserText.getText().trim());
		ps.setValue("remotePwd", remotePwdText.getText().trim());

		try {
			ps.save();
		} catch (IOException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
		DevLogger.printDebugMsg("OK");
		return true;

	}

	@Override
	protected void performDefaults() {
		dbAddressText.setText("127.0.0.1");
		dbPortText.setText("1521");
		dbInstanceText.setText("xe");
		dbUserText.setText("golp");
		dbPwdText.setText("");
		remoteAddressText.setText("none");
		remoteUserText.setText("none");
		remotePwdText.setText("");
	}

}
