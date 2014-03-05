/* 文件名：       PrjPropertyPage1.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.统一使用File.separator。 
 */

package dev.properties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import dev.db.service.ProjectDaoServiceImpl;
import dev.model.base.TreeNode;
import dev.util.CommonUtil;
import dev.util.DevLogger;
import dev.views.NavView;

/**
 * 该类定义了工程的第一个属性页，包括工程的标识、名称和描述
 */
public class PrjPropertyPage1 extends PropertyPage implements
		IWorkbenchPropertyPage {
	private Text prjIdText;
	private Text prjNameText;
	private Text prjDescText;
	private PreferenceStore ps;
	private Map<String, String> map;

	public PrjPropertyPage1() {

		setMessage("工程常规属性");
		initPs();
		try {
			ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
			map = projectDaoServiceImpl.queryProject(ps);
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}

	/**
	 * 该方法用于加载工程对应的properties文件到ps
	 */
	public void initPs() {
		TreeViewer treeViewer = ((NavView) Activator.getDefault()
				.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID)).getTreeViewer();
		StructuredSelection select = (StructuredSelection) treeViewer
				.getSelection();
		String prjId = ((TreeNode) select.getFirstElement()).getName();
		ps = CommonUtil.initPs(prjId);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setText("*工程标识符：");

		prjIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		prjIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		prjIdText.setText(map.get("PRJID"));

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*工程名称：");

		prjNameText = new Text(container, SWT.BORDER);
		prjNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		prjNameText.setText(map.get("PRJNAME"));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setText("*描述：");

		prjDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		prjDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		prjDescText.setText(map.get("PRJDESC"));

		return container;
	}

	@Override
	public boolean performOk() {

		if (prjNameText.getText().isEmpty() || prjDescText.getText().isEmpty()) {
			setErrorMessage("请将信息输入完整");
			return false;
		}
		setErrorMessage(null);

		List<String> datalist = new ArrayList<String>();
		datalist.add(prjNameText.getText());
		datalist.add(prjDescText.getText());
		try {
			ProjectDaoServiceImpl projectDaoServiceImpl = new ProjectDaoServiceImpl();
			projectDaoServiceImpl.updateProject1(datalist, ps);
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}

		DevLogger.printDebugMsg("OK");

		return true;

	}

	@Override
	protected void performDefaults() {
		// prjIdText.setText("工程名不允许修改");
		prjNameText.setText("默认工程名称");
		prjDescText.setText("默认工程描述");
	}

}
