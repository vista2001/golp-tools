package dev.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import dev.generate.fml.FmlId;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
import dev.util.DevLogger;
import dev.views.NavView;

public class FmlIdHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		List<String> obj;
		NavView view = (NavView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.findView(NavView.ID);
		List<TreeNode> project = ((RootNode) view.getTreeViewer().getInput())
				.getChildren();
		if (project.isEmpty()) {
			DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "�Ҳ�������", "��û�д������̡�\n�봴���깤�̺���ִ�д˲���");
			return null;
		}
		FmlDialog dialog = new FmlDialog(Display.getCurrent().getActiveShell());
		dialog.open();
		obj = dialog.getSelected();
		if (obj.isEmpty()) {
			return null;
		}
		for (int i = 0; i < obj.size(); i++) {
			try {
				FmlId.generateFml(obj.get(i));
			} catch (SQLException | IOException | InterruptedException e) {
				DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "����", "�������⡣\n���ɳɹ�" + i + "��");
				return event;
			}

		}
		DevLogger.showMessage(SWT.ICON_INFORMATION | SWT.OK, "fml���ɳɹ�", "���");
		return event;
	}

}
