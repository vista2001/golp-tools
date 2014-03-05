package dev.diagram.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

import dev.model.base.ResourceLeafNode;
import dev.views.NavView;

/**
 * ��ͼ�༭����������
 * 
 * @author ľľ
 * 
 */
public class DiagramEditorInput implements IEditorInput {
	// ����ͼ��ţ�Id��
	private String diagramId;
	// ��������
	private String projectName;
	// ���̱�ţ�Id��
	private String projectId;

	// ��node�ڵ��л�ù������ƣ����̱�ţ�Id��������ͼ��ţ�Id��
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
