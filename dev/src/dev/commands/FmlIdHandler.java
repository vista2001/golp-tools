package dev.commands;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import dev.generate.fml.FmlId;
import dev.model.base.RootNode;
import dev.model.base.TreeNode;
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
			MessageBox box = new MessageBox(Display.getCurrent()
					.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
			box.setMessage("还没有创建工程。\n请创建完工程后再执行此操作");
			box.setText("找不到工程");
			box.open();
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
				MessageBox box = new MessageBox(Display.getCurrent()
						.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
				box.setMessage("出现问题。\n生成成功"+i+"个");
				box.setText("错误");
				box.open();
				return event;
			}
				
		}
		MessageBox box = new MessageBox(Display.getCurrent()
				.getActiveShell(), SWT.ICON_INFORMATION | SWT.OK);
		box.setMessage("fml生成成功");
		box.setText("完成");
		box.open();
		return event;
	}

}
