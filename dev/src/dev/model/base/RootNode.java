/* �ļ�����       RootNode.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������ 
 */

package dev.model.base;

import java.util.ArrayList;
import java.util.List;

import dev.model.resource.ProjectNode;

public class RootNode extends TreeNode {

	public RootNode(String name, String id, TreeNode parent) {
		super(name, id, parent);
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
		if (this.children == null) {
			return new ArrayList<TreeNode>();
		}
		return this.children;
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	public void add(ProjectNode prj) {
		if (children == null) {
			children = new ArrayList<TreeNode>();
			children.add(prj);
		} else {
			children.add(prj);
		}
	}

	@Override
	public boolean hasChildren() {
		// DebugOut.println("root is empty="+this.children.isEmpty());
		return !this.children.isEmpty();
	}

	@Override
	public void removeAllChildren() {

	}
}
