package dev.model.provider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import dev.model.base.TreeNode;

public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof TreeNode) {
			return ((TreeNode) inputElement).getChildren().toArray();
		}
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		if (parentElement instanceof TreeNode) {
			return ((TreeNode) parentElement).getChildren().toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {

		if (element instanceof TreeNode) {
			return ((TreeNode) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {

		return ((TreeNode) element).hasChildren();
	}

}
