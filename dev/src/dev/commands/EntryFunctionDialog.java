package dev.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.model.resource.TradeNodes;
import dev.views.NavView;

public class EntryFunctionDialog extends Dialog {

	protected EntryFunctionDialog(Shell parentShell) {
		super(parentShell);
	}
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("选择交易");
		setShellStyle(SWT.RESIZE);
	}

	protected Control createDialogArea(Composite parent) {

		Composite comp = (Composite) super.createDialogArea(parent);
		comp.setLayout(new GridLayout(3, false));
		// group = new Group(comp, SWT.NONE);
		// group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
		// 1));
		// group.setLayout(new GridLayout(4, false));
		Label choselabel = new Label(comp, SWT.NONE);
		choselabel.setText("选择工程");

		Combo choseCombo = new Combo(comp, SWT.READ_ONLY);
		choseCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		choseCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				if (index == ((Combo) e.getSource()).getSelectionIndex())
					return;
				index = ((Combo) e.getSource()).getSelectionIndex();
				allTrade = getServer(projects.get(index));
				trade.removeAll();
				for (int i = 0; i < allTrade.size(); i++) {
					trade.add(allTrade.get(i).id, i);
				}
				trade.redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		new Label(comp, SWT.NONE);
		trade = new org.eclipse.swt.widgets.List(comp, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		trade.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 6));
		NavView view = (NavView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		projects = ((RootNode) view.getTreeViewer().getInput()).getChildren();
		for (int i = 0; i < projects.size(); i++) {
			choseCombo.add(projects.get(i).id, i);
		}
		return parent;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.OK_ID == buttonId) {

			for (int i : trade.getSelectionIndices()) {
				selected.add(allTrade.get(i));
			}
			close();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			close();
		}
	}

	protected Point getInitialSize() {
		return new Point(300, 300);
	}

	private List<TreeNode> getServer(TreeNode project) {

		List<TreeNode> types = project.getChildren();
		for (int i = 0; i < types.size(); i++) {
			if (types.get(i) instanceof TradeNodes) {
				return types.get(i).getChildren();
			}
		}
		return null;
	}

	public List<TreeNode> getSelected() {
		return selected;
	}

	List<TreeNode> selected = new ArrayList<TreeNode>();
	List<TreeNode> allTrade;
	List<TreeNode> projects;
	int index = -1;
	org.eclipse.swt.widgets.List trade;
	String[] str;
}

