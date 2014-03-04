package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.golpDialogs.SetDataItemStateDialog;
import dev.model.base.ResourceLeafNode;
import dev.model.base.ResourceNode;
import dev.model.resource.ProjectNode;

public class SetDataItemState extends AbstractHandler {
	String prjId;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object obj = structuredSelection.getFirstElement();

		if (obj instanceof ProjectNode) {
			prjId = ((ProjectNode) obj).getId();
		} else if (obj instanceof ResourceNode) {
			prjId = ((ResourceNode) obj).getRootProject().getId();
		} else if (obj instanceof ResourceLeafNode) {
			prjId = ((ResourceLeafNode) obj).getRootProject().getId();
		}

		SetDataItemStateDialog setDataItemStateDialog = new SetDataItemStateDialog(
				HandlerUtil.getActiveShell(event), prjId);
		setDataItemStateDialog.open();
		return null;
	}

}
