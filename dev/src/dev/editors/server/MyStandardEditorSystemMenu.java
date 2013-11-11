package dev.editors.server;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.presentations.SystemMenuClose;
import org.eclipse.ui.internal.presentations.SystemMenuMaximize;
import org.eclipse.ui.internal.presentations.SystemMenuMinimize;
import org.eclipse.ui.internal.presentations.SystemMenuMove;
import org.eclipse.ui.internal.presentations.SystemMenuRestore;
import org.eclipse.ui.internal.presentations.UpdatingActionContributionItem;
import org.eclipse.ui.internal.presentations.util.StandardViewSystemMenu;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;

public class MyStandardEditorSystemMenu extends StandardViewSystemMenu
{
	public MyStandardEditorSystemMenu(IStackPresentationSite site)
	{
		super(site);
	}

	String getMoveMenuText()
	{
		return WorkbenchMessages.EditorPane_moveEditor;
	}

	public void show(Control parent, Point displayCoordinates,
			IPresentablePart currentSelection)
	{
		super.show(parent, displayCoordinates, currentSelection);
	}
}