package dev.diagram.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.ContentsModel;
import dev.diagram.ui.DiagramEditor;

public class SaveAction extends Action {
	public static final String ID = "SAVE_ACTION";
	private ContentsModel contentsModel;
	private DiagramEditor editor;

	public SaveAction(ContentsModel model, DiagramEditor editor) {
		contentsModel = model;
		this.editor = editor;
		setId(ID);
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/save.gif");
		setImageDescriptor(descriptor);
	}

	@Override
	public void run() {
		if (WriteToXML.writeToXML(contentsModel))
			MessageDialog.openInformation(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), "提示信息", "已保存" + "");
		editor.setIsDirty(false);
	}

	@Override
	public void setId(String id) {
		super.setId(id);
	}

}
