/* �ļ�����       OpenProperty.java
 * �����ˣ�       rxy
 * ����ʱ�䣺   2014.1.10
 * ������           �� OpenPropertyΪһHandler������򿪹��̵����ԶԻ��� 
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.actions.PrjPropertyAction;

public class OpenProperty extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        if (window == null)
            return null;
        PrjPropertyAction action = new PrjPropertyAction(window);
        action.run();
        return null;
    }

}
