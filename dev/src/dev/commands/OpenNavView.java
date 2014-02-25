/* 文件名：       OpenNavView.java
 * 修改人：       rxy
 * 修改时间：   2013.12.6
 * 修改内容：   注释掉以下语句：
 *         page.showView("org.eclipse.ui.views.PropertySheet");
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.views.NavView;

public class OpenNavView extends AbstractHandler {

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
//			page.showView("org.eclipse.ui.views.PropertySheet");
			page.showView(NavView.ID);
		} catch (PartInitException e) {
			//FavoritesLog.logError("Failed to open the Favorites view", e);
			e.printStackTrace();
		}
		return null;
	}

}
