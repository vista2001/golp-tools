/* 文件名：       OpenConsoleView.java
 * 修改人：       rxy
 * 修改时间：   2013.12.11
 * 修改内容：   用DebugOut.println方法替换System.out.println方法。
 */

package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenConsoleView extends AbstractHandler {

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

//		try {
//			//page.showView("");
//			ConsoleFactory cf = new ConsoleFactory();
//			  cf.openConsole();
//			  DebugOut.println("hello");
//			  page.showView("org.eclipse.ui.console.ConsoleView");
//		} catch (PartInitException e) {
//			//FavoritesLog.logError("Failed to open the Favorites view", e);
//			e.printStackTrace();
//		}
		return null;
	}

}
