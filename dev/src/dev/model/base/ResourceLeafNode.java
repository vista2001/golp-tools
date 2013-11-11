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
		return this.parent;
	}

	@Override
	public TreeNode getRootProject() {
		// TODO Auto-generated method stub
		return this.parent.getRootProject();
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

	@Override
	public void removeAllChildren() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int hashCode()
	{
		// TODO 自动生成的方法存根
		int result = 17;
		result = 31 * result + this.id.hashCode();
		result = 31 * result + this.name.hashCode();
		result = 31 * result + this.parent.getName().hashCode();
		result = 31 * result + getRootProject().getId().hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj){
		// TODO 自动生成的方法存根
		if(null == obj) return false;
		if(!(obj instanceof ResourceLeafNode)) return false;
		ResourceLeafNode node = (ResourceLeafNode) obj;
		if (this.id.equals(node.getId())
				&& this.name.equals(node.getName())
				&& this.parent.getName().equals(node.getParent().getName())
				&& this.getRootProject().getId()
						.equals(node.getRootProject().getId())){
			return true;
		}
		return false;
	}


	
}
