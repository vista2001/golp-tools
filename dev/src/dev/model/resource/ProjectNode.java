package dev.model.resource;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

import dev.model.base.ResourceNode;
import dev.model.base.ResourcesNodeType;
import dev.model.base.TreeNode;

public class ProjectNode extends ResourceNode {

	public ProjectNode(String name, String id, TreeNode parent) {
		super(name, id, parent);
		ResourcesNodeType type = new ResourcesNodeType(this);
		this.children = new ArrayList<TreeNode>();
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
		IWorkbenchAdapter a = new IWorkbenchAdapter() {

			@Override
			public Object getParent(Object o) {

				return null;
			}

			@Override
			public String getLabel(Object o) {
				return ((ProjectNode) o).getName();
			}

			@Override
			public ImageDescriptor getImageDescriptor(Object object) {

				return null;
			}

			@Override
			public Object[] getChildren(Object o) {

				return null;
			}
		};
		if (adapter.equals(IPropertySourceProvider.class)) {
			IPropertySourceProvider propertySourceProvider = new IPropertySourceProvider() {

				@Override
				public IPropertySource getPropertySource(Object object) {
					IPropertySource source = new IPropertySource() {

						@Override
						public void setPropertyValue(Object id, Object value) {
						}

						@Override
						public void resetPropertyValue(Object id) {

						}

						@Override
						public boolean isPropertySet(Object id) {
							return false;
						}

						@Override
						public Object getPropertyValue(Object id) {
							return null;
						}

						@Override
						public IPropertyDescriptor[] getPropertyDescriptors() {

							/*return new IPropertyDescriptor[] {
									new TextPropertyDescriptor("prjId", "ID"),
									new TextPropertyDescriptor("dbAddress", "数据库地址"),
									new TextPropertyDescriptor("dbInstance", "数据库实例"),
									new TextPropertyDescriptor("dbUser", "数据库用户"),
									new TextPropertyDescriptor("dbPwd", "数据库口令"),
									new TextPropertyDescriptor("dbPort", "数据库端口")

							};*/
							return new IPropertyDescriptor[]{};
						}

						@Override
						public Object getEditableValue() {
							return true;
						}
					};
					return source;
				}

			};
			return propertySourceProvider;
		}
		return a;
	}

	public void add(ServerNodes svr) {
		if (children == null) {
			children = new ArrayList<TreeNode>();
			children.add(svr);
		} else {
			children.add(svr);
		}
	}

	@Override
	public boolean hasChildren() {
		if (this.children == null || this.children.isEmpty()) {
			return false;
		}
		return !this.children.isEmpty();
	}
}
