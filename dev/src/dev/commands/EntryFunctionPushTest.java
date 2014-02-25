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
		// ��õ�����ͼ��ѡ�еĿؼ�
		Object obj = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		selection = ((NavView) obj).getTreeViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			temselection = (IStructuredSelection) selection;
			// ѡ�е��ǲ���Ҷ�ӽڵ�
			for (Object one : temselection.toArray()) {
				if (one instanceof ResourceLeafNode) {
					ResourceLeafNode node = (ResourceLeafNode) one;
					// �ж�ѡ�нڵ�ĸ��ڵ��ǲ��Ƿ������
					if (!node.parent.name.equals("����")) {
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
