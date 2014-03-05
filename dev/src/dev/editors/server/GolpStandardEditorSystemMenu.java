package dev.editors.server;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.presentations.util.StandardViewSystemMenu;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;

public class GolpStandardEditorSystemMenu extends StandardViewSystemMenu {
	public GolpStandardEditorSystemMenu(IStackPresentationSite site) {
		super(site);
	}

	String getMoveMenuText() {
		return WorkbenchMessages.EditorPane_moveEditor;
	}

	public void show(Control parent, Point displayCoordinates,
			IPresentablePart currentSelection) {
		super.show(parent, displayCoordinates, currentSelection);
	}
}