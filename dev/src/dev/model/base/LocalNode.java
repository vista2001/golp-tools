package dev.model.base;

import java.util.ArrayList;
import java.util.List;

import dev.model.resource.ProjectNode;


public class LocalNode extends TreeNode{

	public LocalNode(String name, String id, TreeNode parent) {
		super(name, id, parent);
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
		return this.parent;
	}

	@Override
	public TreeNode getRootProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getChildren() {
		// TODO Auto-generated method stub
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
	
	public void add(ProjectNode prj){
		if(children==null){
			children=new ArrayList<TreeNode>();
			children.add(prj);
		}else{
			children.add(prj);
		}
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		//System.out.println("root is empty="+this.children.isEmpty());
		return !this.children.isEmpty();
	}
}
