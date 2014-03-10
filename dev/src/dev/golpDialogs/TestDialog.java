package dev.golpDialogs;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.FilteredList;

import dev.editors.dataItem.DataItemEditor;
import dev.editors.dataItem.DataItemEditorInput;
import dev.util.DevLogger;

public class TestDialog extends ElementListSelectionDialog {

	public TestDialog(Shell parent, ILabelProvider renderer) {
		super(parent, renderer);
	}

	@Override
	public void setElements(Object[] elements) {
		super.setElements(elements);
	}

	@Override
	protected void computeResult() {
		super.computeResult();
	}

	@Override
	protected FilteredList createFilteredList(Composite parent) {
		return super.createFilteredList(parent);
	}

	@Override
	public void setFilter(String filter) {
		super.setFilter(filter);
	}

	@Override
	public void setTitle(String title) {
		super.setTitle(title);
	}

	@Override
	protected void okPressed() {
		// TODO Auto-generated method stub
		DevLogger.printDebugMsg("op pressed");
		this.getParentShell();
		DataItemEditorInput editorInput = new DataItemEditorInput(((String)this.getSelectedElements()[0]).split("-")[0]);
		
		IWorkbenchPage page=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.openEditor(editorInput, DataItemEditor.ID, true);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		//super.okPressed();
	}

	
	

}
