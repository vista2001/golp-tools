/* �ļ�����       NewDataItemWizardPage0.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.wizards.newDataItem;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.db.pojo.TDataItem;
import dev.db.service.CommonDialogServiceImpl;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.util.DevLogger;
import dev.views.NavView;

/**
 * ���ඨ�����½��������򵼵� ��1ҳ
 */
public class NewDataItemWizardPage0 extends WizardPage {
	private Combo dataItemUpProjectCombo;
	private Combo dataItemLvLCombo;
	private Text dataItemIdText;
	private Text dataItemNameText;
	private Text dataItemDescText;
	private ISelection selection;

	public NewDataItemWizardPage0(ISelection selection) {
		super("NewDataItemWizardPage0");
		setTitle("�½���������");
		setDescription("����򵼽�ָ�������GOLP������Ĵ���");
		this.selection = selection;
	}

	public Combo getDataItemUpProjectCombo() {
		return dataItemUpProjectCombo;
	}

	public Combo getDataItemLvLCombo() {
		return dataItemLvLCombo;
	}

	public Text getDataItemIdText() {
		return dataItemIdText;
	}

	public Text getDataItemNameText() {
		return dataItemNameText;
	}

	public Text getDataItemDescText() {
		return dataItemDescText;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemUpProjectLabel = new Label(container, SWT.NONE);
		dataItemUpProjectLabel.setText("*�������̣�");

		dataItemUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		dataItemUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		IViewPart viewPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		if (viewPart != null) {
			NavView v = (NavView) viewPart;
			TreeViewer treeViewer = v.getTreeViewer();
			RootNode root = (RootNode) treeViewer.getInput();
			List<TreeNode> projectNodes = root.getChildren();
			for (TreeNode treeNode : projectNodes) {
				dataItemUpProjectCombo.add(treeNode.getName());
			}
		}

		Label dataItemLvLLabel = new Label(container, SWT.NONE);
		dataItemLvLLabel.setText("*�������");

		dataItemLvLCombo = new Combo(container, SWT.READ_ONLY);
		dataItemLvLCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		dataItemLvLCombo.add("0-APP");

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setText("*�������ʶ����");

		dataItemIdText = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		dataItemIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		// �����ϱߵ����ݿ���δ�ҵ����ʵļ�¼�����ȡ���� �������ļ�������dataItemId
		// if(dataItemIdText.getText().equals(""))
		// {
		// IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// IWorkspaceRoot root = workspace.getRoot();
		// String s = root.getLocationURI().toString();
		// String filePath = root.getLocationURI().toString().substring(6,
		// s.lastIndexOf(File.separator)) + File.separator +
		// "GOLPCFG/golpcfg.properties";
		// DebugOut.println(filePath);
		// PreferenceStore ps = new PreferenceStore(filePath);
		// ps.setDefault("DataIdStart","��ȡDataIdStartʧ��");
		// try
		// {
		// ps.load();
		// dataItemIdText.setText(ps.getString("DataIdStart"));
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();DevLogger.printError(e);
		// }
		//
		// }

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*���������ƣ�");

		dataItemNameText = new Text(container, SWT.BORDER);
		dataItemNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setText("*������");

		dataItemDescText = new Text(container, SWT.BORDER | SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		dataItemDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));

		dataItemUpProjectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
				if (dataItemUpProjectCombo.getText().isEmpty() == false) {
					setDataItemIdText();
				}
			}
		});

		dataItemLvLCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dataItemIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dataItemNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		dataItemDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
	private void dialogChanged() {
		setPageComplete(true);
	}

	@Override
	public boolean canFlipToNextPage() {
		if (getDataItemUpProjectCombo().getText().length() == 0
				|| getDataItemLvLCombo().getText().length() == 0
				|| getDataItemIdText().getText().length() == 0
				|| getDataItemNameText().getText().length() == 0
				|| getDataItemDescText().getText().length() == 0)
			return false;
		return true;
	}

	// ��ȡ���ݿ⣬�鿴DATAITEM���Ƿ�����DATALVLΪAPP�ļ�¼�����У��õ���Щ��¼��DATAITEMID�����ֵ,
	// Ȼ���1������dataItemIdText�����ޣ����project���ж�ȡdataidstart�ֶε�ֵ������dataItemIdText
	private void setDataItemIdText() {
		CommonDialogServiceImpl commonDialogServiceImpl = new CommonDialogServiceImpl();
		try {
			TDataItem dataItem = commonDialogServiceImpl
					.getNewDataItemId(dataItemUpProjectCombo.getText());
			dataItemIdText.setText(dataItem.getDataItemId() + "");
		} catch (SQLException e) {
			e.printStackTrace();
			DevLogger.printError(e);
		}
	}
}
