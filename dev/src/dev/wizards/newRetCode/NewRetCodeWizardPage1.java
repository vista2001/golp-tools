/* �ļ�����       NewRetCodeWizardPage1.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.14
 * �޸����ݣ�   �޸Ĺ��캯���У�����super����ʱ�Ĳ���Ϊ����������� 
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
		setTitle("�½���Ӧ����");
		setDescription("����򵼽�ָ�������GOLP��Ӧ��Ĵ���");
		this.selection = selection;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label dataItemUpProjectLabel = new Label(container, SWT.NONE);
		dataItemUpProjectLabel.setText("*�������̣�");

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

	// �˴�������Ϊtrue�������ǻ�����±ߵ�canFlipToNextPage()����
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
