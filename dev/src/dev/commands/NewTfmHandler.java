/* �ļ�����       NewTfmHandler.java
 * ������           ���ļ���������NewTfmHandler������ʵ���˴��½�����ͼ�򵼡�
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.10
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

import dev.wizards.newTfm.NewTfmWizard;

public class NewTfmHandler extends AbstractHandler
{

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        
        IWorkbenchWindow window = HandlerUtil
                .getActiveWorkbenchWindowChecked(event);
        
        ISelection selection=HandlerUtil.getCurrentSelection(event);
        {
            NewTfmWizard wizard = new NewTfmWizard();
            wizard.init(
                    window.getWorkbench(),
                    selection instanceof IStructuredSelection ? 
                            (IStructuredSelection) selection: StructuredSelection.EMPTY);
            WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
            dialog.open();
        }
        return null;
    }

}
