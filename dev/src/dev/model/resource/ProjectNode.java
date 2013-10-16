package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import dev.model.base.ResourceNode;
import dev.model.base.ResourcesNodeType;
import dev.model.base.TreeNode;


public class ProjectNode extends ResourceNode{

	public ProjectNode(String name, String id, TreeNode parent) {
		super(name, id, parent);
		ResourcesNodeType type=new ResourcesNodeType(this);
		this.children=new ArrayList<TreeNode>();
		for (ResourceNode resourceNode : type.getType()) {
			this.children.add(resourceNode);
		}
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
		System.out.println("prj get parent");
		return this.parent;
	}

	@Override
	public TreeNode getRootProject() {
		return null;
	}

	@Override
	public List<TreeNode> getChildren() {
		/*if(this.children==null){
			this.children = new ArrayList<TreeNode>();
		}*/
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void add(ServerNodes svr){
		if(children==null){
			children=new ArrayList<TreeNode>();
			children.add(svr);
		}else{
			children.add(svr);
		}
	}

	@Override
	public boolean hasChildren() {
		//System.out.println("this children is"+ this.children);
		if(this.children==null||this.children.isEmpty()){
			return false;
		}
		return !this.children.isEmpty();
	}
}
