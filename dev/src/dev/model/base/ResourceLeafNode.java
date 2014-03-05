package dev.model.base;

import java.util.List;

public class ResourceLeafNode extends TreeNode {

	public ResourceLeafNode(String name, String id, TreeNode treeNode) {
		super(name, id, treeNode);

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

		return this.parent.getRootProject();
	}

	@Override
	public List<TreeNode> getChildren() {

		return null;
	}

	@Override
	public boolean hasChildren() {

		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {

		/*
		 * if (adapter.equals(IPropertySourceProvider.class)) {
		 * IPropertySourceProvider propertySourceProvider = new
		 * IPropertySourceProvider() {
		 * 
		 * @Override public IPropertySource getPropertySource(Object object) {
		 * IPropertySource source = new IPropertySource() {
		 * 
		 * @Override public void setPropertyValue(Object id, Object value) {
		 * 
		 * }
		 * 
		 * @Override public void resetPropertyValue(Object id) {
		 * 
		 * }
		 * 
		 * @Override public boolean isPropertySet(Object id) { return false; }
		 * 
		 * @Override public Object getPropertyValue(Object id) { String
		 * property=(String)id; if(property.equals("id")){ return "AAA"; }
		 * if(property.equals("name")){ return "BBB"; } //return null; return
		 * null; }
		 * 
		 * @Override public IPropertyDescriptor[] getPropertyDescriptors() {
		 * 
		 * return new IPropertyDescriptor[] { new PropertyDescriptor("id",
		 * "ID"), new PropertyDescriptor("name", "Ãû³Æ")
		 * 
		 * }; //return new IPropertyDescriptor[0]; }
		 * 
		 * @Override public Object getEditableValue() { return true; } }; return
		 * source; }
		 * 
		 * }; return propertySourceProvider; }
		 */
		return null;
	}

	@Override
	public void removeAllChildren() {

	}

	@Override
	public int hashCode() {

		int result = 17;
		result = 31 * result + this.id.hashCode();
		result = 31 * result + this.name.hashCode();
		result = 31 * result + this.parent.getName().hashCode();
		result = 31 * result + getRootProject().getId().hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj)
			return false;
		if (!(obj instanceof ResourceLeafNode))
			return false;
		ResourceLeafNode node = (ResourceLeafNode) obj;
		if (this.id.equals(node.getId())
				&& this.name.equals(node.getName())
				&& this.parent.getName().equals(node.getParent().getName())
				&& this.getRootProject().getId()
						.equals(node.getRootProject().getId())) {
			return true;
		}
		return false;
	}

}
