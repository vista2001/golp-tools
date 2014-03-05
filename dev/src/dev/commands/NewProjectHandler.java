/* �ļ�����       NewProjectHandler.java
 * �޸��ˣ�       rxy
 * �޸�ʱ�䣺   2013.12.11
 * �޸����ݣ�   ��DebugOut.println�����滻System.out.println������
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.util.DevLogger;
import dev.wizards.newProject.NewProjectWizard;

public class NewProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DevLogger.printDebugMsg("NewProjectHandler is called");
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		// MessageDialog.openConfirm(window.getShell(), "Add",
		// "The \"Add to Favorites\" handler was called");
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		{
			NewProjectWizard wizard = new NewProjectWizard();
			wizard.init(
					window.getWorkbench(),
					selection instanceof IStructuredSelection ? (IStructuredSelection) selection
							: StructuredSelection.EMPTY);
			WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
			dialog.open();
		}
		return null;
	}

}
