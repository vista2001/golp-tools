package dev.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenPreference extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window=HandlerUtil.getActiveWorkbenchWindow(event);
		//IWorkbenchAction ref=
		ActionFactory.PREFERENCES.create(window).run();
		//ActionFactory.PREFERENCES.create(getSite().getWorkbenchWindow()).run();
		return null;
	}

}
