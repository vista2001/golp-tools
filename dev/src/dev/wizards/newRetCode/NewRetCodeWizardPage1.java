/* 文件名：       NewRetCodeWizardPage1.java
 * 修改人：       rxy
 * 修改时间：   2013.12.14
 * 修改内容：   修改构造函数中，调用super方法时的参数为本类的类名。 
 */

package dev.wizards.newRetCode;

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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class NewRetCodeWizardPage1 extends WizardPage {

	private ISelection selection;
	private Combo retCodeUpProjectCombo;

	public Combo getRetCodeUpProjectCombo() {
		return retCodeUpProjectCombo;
	}

	public NewRetCodeWizardPage1(ISelection selection) {
		super("NewRetCodeWizardPage1");
		setTitle("新建响应码向导");
		setDescription("这个向导将指导你完成GOLP响应码的创建");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemUpProjectLabel = new Label(container, SWT.NONE);
		dataItemUpProjectLabel.setText("*所属工程：");

		retCodeUpProjectCombo = new Combo(container, SWT.READ_ONLY);
		retCodeUpProjectCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
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
				retCodeUpProjectCombo.add(treeNode.getName());
			}
		}

		retCodeUpProjectCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

	}

	// 此处虽设置为true，但还是会调用下边的canFlipToNextPage()方法
	private void dialogChanged() {
		setPageComplete(true);
	}

	public boolean validInput() {
		if (getRetCodeUpProjectCombo().getText().length() == 0)
			return false;
		return true;
	}

	@Override
	public boolean canFlipToNextPage() {
		return false;
	}

}
