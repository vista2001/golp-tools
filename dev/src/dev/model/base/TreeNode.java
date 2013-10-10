package dev.model.base;

import java.util.List;


public abstract class TreeNode implements ITreeNode{
	public String name;
	public String id;
	public TreeNode parent;
	public List<TreeNode> children;

	public TreeNode() {
	}

	public TreeNode(String name, String id,TreeNode parent) {
		this.name = name;
		this.id = id;
		this.parent=parent;
	}

}
