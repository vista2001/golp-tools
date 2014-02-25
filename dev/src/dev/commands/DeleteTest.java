package dev.commands;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.model.base.TreeNode;
import dev.views.NavView;

public class DeleteTest extends PropertyTester {

	public DeleteTest() {
		
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		
		ISelection selection;
		Object obj= PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(NavView.ID);
		if(!(obj instanceof NavView))
		    return false;
		selection=((NavView)obj).getTreeViewer().getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection temselection = (IStructuredSelection) selection;
			// ѡ�е��ǲ���Ҷ�ӽڵ�
			if (temselection.getFirstElement() instanceof ResourceLeafNode) {
				List<TreeNode> nodes = temselection.toList();
				// �ж�ѡ�нڵ�ĸ��ڵ��ǲ��Ƿ������
				if(nodes.size()>1){
					String name=nodes.get(0).parent.name;
					for(int i=1;i<nodes.size();i++){
						if(!nodes.get(i).parent.name.equals(name))
							return false;
					}
					return true;
				}
				else
					return true;
			}
		}
		return false;
	}

}
