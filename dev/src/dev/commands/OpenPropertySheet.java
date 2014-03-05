/* 文件名：       OpenPropertySheet.java
 * 描述：           该文件定义了类OpenPropertySheet，该类实现了显示属性表单的功能。
 * 创建人：       rxy
 * 创建时间：   2013.12.9
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
