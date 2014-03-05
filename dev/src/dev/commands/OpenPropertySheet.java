/* �ļ�����       OpenPropertySheet.java
 * ������           ���ļ���������OpenPropertySheet������ʵ������ʾ���Ա��Ĺ��ܡ�
 * �����ˣ�       rxy
 * ����ʱ�䣺   2013.12.9
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.util.DevLogger;

public class OpenPropertySheet extends AbstractHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		if (window == null)
			return null;

		// Get the active page

		IWorkbenchPage page = window.getActivePage();
		if (page == null)
			return null;

		// Open and activate the Favorites view

		try {
			page.showView("org.eclipse.ui.views.PropertySheet");
		} catch (PartInitException e) {
			// FavoritesLog.logError("Failed to open the Favorites view", e);

			e.printStackTrace();
			DevLogger.printError(e);
		}
		return null;
	}
}
