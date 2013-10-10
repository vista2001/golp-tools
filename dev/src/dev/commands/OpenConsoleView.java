package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import dev.console.ConsoleFactory;

public class OpenConsoleView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
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
			//page.showView("");
			ConsoleFactory cf = new ConsoleFactory();
			  cf.openConsole();
			System.out.println("hello");
			  page.showView("org.eclipse.ui.console.ConsoleView");
		} catch (PartInitException e) {
			//FavoritesLog.logError("Failed to open the Favorites view", e);
			e.printStackTrace();
		}
		return null;
	}

}
