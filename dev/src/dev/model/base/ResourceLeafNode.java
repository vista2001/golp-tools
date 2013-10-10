package dev.model.base;

import java.util.List;


public class ResourceLeafNode extends TreeNode{

	
	public ResourceLeafNode(String name, String id,TreeNode treeNode) {
		super(name, id, treeNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public TreeNode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeNode getRootProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
