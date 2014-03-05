package dev.diagram.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.views.NavView;

/**
 * 视图编辑器的输入类
 * 
 * @author 木木
 * 
 */
public class DiagramEditorInput implements IEditorInput {
	// 流程图编号（Id）
	private String diagramId;
	// 工程名称
	private String projectName;
	// 工程编号（Id）
	private String projectId;

	// 从node节点中获得工程名称，工程编号（Id），流程图编号（Id）
	public DiagramEditorInput(ResourceLeafNode node) {
		projectName = node.getRootProject().getName();
		projectId = node.getRootProject().getId();
		diagramId = node.getId();
	}

	public String getDiagramId() {
		return diagramId;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public boolean exists() {

		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {

		return null;
	}

	@Override
	public String getName() {

		NavView view = (NavView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		view.getTreeViewer().refresh();
		return diagramId + "";
	}

	@Override
	public IPersistableElement getPersistable() {

		return null;
	}

	@Override
	public String getToolTipText() {

		return diagramId + "";
	}

	@Override
	public Object getAdapter(Class adapter) {

		return null;
	}

}
