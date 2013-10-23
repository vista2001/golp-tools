package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import dev.model.base.ResourceNode;
import dev.model.base.TreeNode;


public class TFMNodes extends ResourceNode{

	
	public TFMNodes(String name, String id, TreeNode parent) {
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
		if(this.children==null){
			return new ArrayList<TreeNode>();
		}
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		if(this.children==null||this.children.isEmpty()){
			return false;
		}
		return !this.children.isEmpty();
	}
	
}
