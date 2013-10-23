package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

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
		return this.parent;
	}

	@Override
	public TreeNode getRootProject() {
		return null;
	}

	@Override
	public List<TreeNode> getChildren() {
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter) {
		IWorkbenchAdapter a=new IWorkbenchAdapter() {
			
			@Override
			public Object getParent(Object o) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getLabel(Object o) {
				return ((ProjectNode)o).getName();
			}
			
			@Override
			public ImageDescriptor getImageDescriptor(Object object) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] getChildren(Object o) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return a;
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
		if(this.children==null||this.children.isEmpty()){
			return false;
		}
		return !this.children.isEmpty();
	}
}
