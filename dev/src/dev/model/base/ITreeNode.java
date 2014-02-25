package dev.model.base;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;

public  interface ITreeNode extends IAdaptable{
	public  String getName();
	public  String getId();
	public  TreeNode getParent();
	public  TreeNode getRootProject();
	public  List<TreeNode> getChildren();
	public  void removeAllChildren();
	public boolean hasChildren();
}
