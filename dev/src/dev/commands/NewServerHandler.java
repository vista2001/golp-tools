/* 文件名：       NewServerHandler.java
 * 修改人：       rxy
 * 修改时间：   2013.12.1
 * 修改内容：   1.用DebugOut.println方法替换System.out.println方法；
 *         2.用GolpWizardDialog类替换WizardDialog类。GolpWizardDialog
 *         类与WizardDialog类唯一的不同在不向导对话框中没有将“完成”按钮设为默认。
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.util.DevLogger;
import dev.util.GolpWizardDialog;
import dev.wizards.newServer.NewServerWizard;

public class NewServerHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DevLogger.printDebugMsg("NewServerHandler is called");
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		// MessageDialog.openConfirm(window.getShell(), "Add",
		// "The \"Add to Favorites\" handler was called");
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		{
			NewServerWizard wizard = new NewServerWizard();
			wizard.init(
					window.getWorkbench(),
					selection instanceof IStructuredSelection ? (IStructuredSelection) selection
							: StructuredSelection.EMPTY);
			// WizardDialog dialog = new WizardDialog(window.getShell(),
			// wizard);
			GolpWizardDialog dialog = new GolpWizardDialog(window.getShell(),
					wizard);
			dialog.open();
		}
		return null;
	}

}
