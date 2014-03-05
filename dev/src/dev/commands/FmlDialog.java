package dev.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class FmlDialog extends Dialog {

	public FmlDialog(Shell parentShell) {
		super(parentShell);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("选择工程");
		setShellStyle(SWT.RESIZE);
	}

	protected Control createDialogArea(Composite parent) {

		Composite comp = (Composite) super.createDialogArea(parent);
		GridLayout gl_comp = new GridLayout(1, true);
		gl_comp.horizontalSpacing = -1;
		gl_comp.verticalSpacing = -1;
		comp.setLayout(gl_comp);

		list = new org.eclipse.swt.widgets.List(comp, SWT.BORDER | SWT.V_SCROLL
				| SWT.MULTI);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 9));

		NavView view = (NavView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		projects = ((RootNode) view.getTreeViewer().getInput()).getChildren();
		for (int i = 0; i < projects.size(); i++) {
			list.add(projects.get(i).id, i);
		}
		return parent;
	}

	protected Button createButton(Composite parent, int id, String label,
			boolean defaultButton) {
		return null;
	}

	protected void initializeBounds() {
		// 我们可以利用原有的ID创建按钮,也可以用自定义的ID创建按钮
		// 但是调用的都是父类的createButton方法.
		super.createButton((Composite) getButtonBar(), IDialogConstants.OK_ID,
				"确定", false);
		super.createButton((Composite) getButtonBar(),
				IDialogConstants.CANCEL_ID, "取消", false);
		// 下面这个方法一定要加上,并且要放在创建按钮之后,否则就不会创建按钮
		super.initializeBounds();
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {
			for (int i : list.getSelectionIndices()) {
				selected.add(projects.get(i).getId());
			}
			close();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			close();
		}
	}

	protected Point getInitialSize() {
		return new Point(300, 300);
	}

	public List<String> getSelected() {
		return selected;
	}

	private List<TreeNode> projects;
	private List<String> selected = new ArrayList<String>();
	private org.eclipse.swt.widgets.List list;
}
