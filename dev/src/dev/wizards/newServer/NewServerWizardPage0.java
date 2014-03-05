package dev.wizards.newServer;

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

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class NewServerWizardPage0 extends WizardPage {

	private Combo upProjectCombo;
	private Combo serverLevelCombo;
	private Text svrIdText;
	private Text svrNameText;
	private Text svrDescText;
	private ISelection selection;

	public NewServerWizardPage0(ISelection selection) {
		super("NewServerWizardPage0");
		setTitle("新建服务程序向导");
		setDescription("这个向导将指导你完成GOLP服务程序的创建");
		this.selection = selection;
	}

	public Combo getUpProjectCombo() {
		return upProjectCombo;
	}

	public Combo getServerLevelCombo() {
		return serverLevelCombo;
	}

	public Text getSvrIdText() {
		return svrIdText;
	}

	public Text getSvrNameText() {
		return svrNameText;
	}

	public Text getSvrDescText() {
		return svrDescText;
	}

	@Override
	/**创建向导页面的控件*/
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label upProjectLabel = new Label(container, SWT.NONE);
		upProjectLabel.setText("*所属工程：");

		upProjectCombo = new Combo(container, SWT.READ_ONLY);
		upProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		IViewPart viewPart = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		if (viewPart != null) {
			NavView v = (NavView) viewPart;
			TreeViewer treeViewer = v.getTreeViewer();
			RootNode root = (RootNode) treeViewer.getInput();
			List<TreeNode> projectNodes = root.getChildren();
			for (TreeNode treeNode : projectNodes) {
				upProjectCombo.add(treeNode.getName());
			}
		}

		Label serverLevelLabel = new Label(container, SWT.NONE);
		serverLevelLabel.setText("*服务程序级别：");

		serverLevelCombo = new Combo(container, SWT.READ_ONLY);
		serverLevelCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		serverLevelCombo.add("0-APP");

		Label prjIdLabel = new Label(container, SWT.NONE);
		prjIdLabel.setText("*服务程序标识符：");

		svrIdText = new Text(container, SWT.BORDER);
		svrIdText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label prjNameLabel = new Label(container, SWT.NONE);
		prjNameLabel.setText("*服务程序名称：");

		svrNameText = new Text(container, SWT.BORDER);
		svrNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label prjDescLabel = new Label(container, SWT.NONE);
		prjDescLabel.setText("*描述：");

		svrDescText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL
				| SWT.MULTI);
		svrDescText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));

		upProjectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		serverLevelCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		svrIdText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		svrNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		svrDescText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
	}

	// 输入项校验
	private void dialogChanged() {
		// if ( upProjectCombo.getText().isEmpty()
		// || serverLevelCombo.getText().isEmpty()
		// || svrIdText.getText().isEmpty()
		// || svrDescText.getText().isEmpty()
		// || svrNameText.getText().isEmpty())
		// {
		// updateStatus("请输入所有必填项");
		// return;
		// }
		// updateStatus(null);
		setPageComplete(true);
	}

	// // 更新状态
	// private void updateStatus(String message)
	// {
	// setErrorMessage(message);
	// setPageComplete(message == null);
	// }

	@Override
	public boolean canFlipToNextPage() {
		if (getUpProjectCombo().getText().isEmpty()
				|| getServerLevelCombo().getText().isEmpty()
				|| getSvrDescText().getText().isEmpty()
				|| getSvrIdText().getText().isEmpty()
				|| getSvrNameText().getText().isEmpty()) {
			return false;
		}
		return true;
	}

}
