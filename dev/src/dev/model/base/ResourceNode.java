/* 文件名：       ResourceNode.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。 
 */

package dev.model.base;

import java.util.ArrayList;
import java.util.List;

import dev.model.base.TreeNode;


public class ResourceNode extends TreeNode{

	
	public ResourceNode(String name, String id, TreeNode parent) {
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
		
		return null;
	}

	@Override
	public boolean hasChildren() {
		
		//DebugOut.println("this children is="+this.children);
		if(this.children==null||this.children.isEmpty()){
			return false;
		}
		return !this.children.isEmpty();
	}

	@Override
	public void removeAllChildren() {
		this.children.clear();
	}
	
}
