package dev.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.views.NavView;

public class EntryFunctionPushTest extends PropertyTester {

	public EntryFunctionPushTest() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		IStructuredSelection temselection;
		ISelection selection;
		// 获得导航视图中选中的控件
		Object obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		selection = ((NavView) obj).getTreeViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			temselection = (IStructuredSelection) selection;
			// 选中的是不是叶子节点
			for (Object one : temselection.toArray()) {
				if (one instanceof ResourceLeafNode) {
					ResourceLeafNode node = (ResourceLeafNode) one;
					// 判断选中节点的父节点是不是服务程序
					if (!node.parent.name.equals("交易")) {
						return false;
					}
				}
				else
					return false;
			}
			return true;
		}

		return false;
	}
}
