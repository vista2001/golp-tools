package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import dev.model.base.ResourceNode;
import dev.model.base.TreeNode;


public class MessageNodes extends ResourceNode{

	
	public MessageNodes(String name, String id, TreeNode parent) {
		super(name, id, parent);
		this.children=new ArrayList<TreeNode>();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public TreeNode getParent() {
		return this.parent;
	}

	@Override
	public TreeNode getRootProject() {
		return this.parent;
	}

	@Override
	public List<TreeNode> getChildren() {
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if(adapter.isInstance(this)){
			return this;
		}
		return null;
	}

	@Override
	public boolean hasChildren() {
		if (this.children == null || this.children.isEmpty())
		{
			return false;
		}
		return !this.children.isEmpty();
	}
	
}
