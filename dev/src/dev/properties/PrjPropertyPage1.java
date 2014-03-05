/* �ļ�����       PrjPropertyPage1.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   1.��DebugOut.println�����滻System.out.println������
 *         2.ͳһʹ��File.separator�� 
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
 * ���ඨ���˹��̵ĵ�һ������ҳ���������̵ı�ʶ�����ƺ�����
 */
public class PrjPropertyPage1 extends PropertyPage implements
		IWorkbenchPropertyPage {
	private Text prjIdText;
	private Text prjNameText;
	private Text prjDescText;
	private PreferenceStore ps;
	private Map<String, String> map;

	public PrjPropertyPage1() {

		setMessage("���̳�������");
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
	 * �÷������ڼ��ع��̶�Ӧ��properties�ļ���ps
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
		prjIdLabel.setText("*���̱�ʶ����");

		prjIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		prjIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		prjIdText.setText(map.get("PRJID"));

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*�������ƣ�");

		prjNameText = new Text(container, SWT.BORDER);
		prjNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		prjNameText.setText(map.get("PRJNAME"));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setText("*������");

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
			setErrorMessage("�뽫��Ϣ��������");
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
		// prjIdText.setText("�������������޸�");
		prjNameText.setText("Ĭ�Ϲ�������");
		prjDescText.setText("Ĭ�Ϲ�������");
	}

}
