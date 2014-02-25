/* 文件名：       NewTfmHandler.java
 * 描述：           该文件定义了类NewTfmHandler，该类实现了打开新建流程图向导。
 * 创建人：       rxy
 * 创建时间：   2013.12.10
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
