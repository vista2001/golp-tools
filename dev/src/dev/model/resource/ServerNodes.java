package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import dev.model.base.ResourceLeafNode;
import dev.model.base.ResourceNode;
import dev.model.base.TreeNode;

public class ServerNodes extends ResourceNode
{

	public ServerNodes(String name, String id, TreeNode parent)
	{
		super(name, id, parent);
		this.children = new ArrayList<TreeNode>();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public TreeNode getParent()
	{
		// TODO Auto-generated method stub
		return this.parent;
	}

	@Override
	public TreeNode getRootProject()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TreeNode> getChildren()
	{
		// TODO Auto-generated method stub
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren()
	{
		// TODO Auto-generated method stub
		System.out.println("this children is=" + this.children);
		if (this.children == null || this.children.isEmpty())
		{
			return false;
		}
		return !this.children.isEmpty();
	}

	public void add(ResourceLeafNode rln)
	{

		children.add(rln);
	}
}
