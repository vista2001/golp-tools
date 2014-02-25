package dev.model.provider;


import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import dev.model.base.TreeNode;

public class TreeLabelProvider implements ILabelProvider {

	@Override
	public void addListener(ILabelProviderListener listener) {
		
		
	}

	@Override
	public void dispose() {
		
		
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		
		
	}

	@Override
	public Image getImage(Object element) {
		
		return null;
	}

	@Override
	public String getText(Object element) {
		
		if(element instanceof TreeNode){
			return ((TreeNode) element).getName();
		}
		return null;
	}


}
