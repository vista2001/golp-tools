package dev;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

import dev.console.ConsoleFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		//addFastViews(layout);
		String editorArea=layout.getEditorArea();
		IFolderLayout left=layout.createFolder("left", IPageLayout.LEFT, 0.3f, editorArea);
		left.addView("dev.views.NavView");
		layout.addView("dev.views.queryView", IPageLayout.BOTTOM, 0.5f, "left");
		//layout.addView("org.eclipse.ui.console.ConsoleView", IPageLayout.BOTTOM, 0.67f, IPageLayout.ID_EDITOR_AREA);
		//layout.addView("org.eclipse.pde.runtime.LogView", IPageLayout.BOTTOM, 0.5f, IPageLayout.ID_EDITOR_AREA);
		/*ConsoleFactory cf = new ConsoleFactory();
		  cf.openConsole();*/
		//layout.addView(IConsoleConstants.ID_CONSOLE_VIEW, IPageLayout.BOTTOM, 0.67f, IPageLayout.ID_EDITOR_AREA);
	}
	private void addFastViews(IPageLayout layout) {
		//layout.addFastView("org.eclipse.ui.console.ConsoleView");
	}
}
