package dev.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;

import dev.Activator;

public class EditorMatch implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		// TODO Auto-generated method stub
		IWorkbenchPage page = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage();  
/*		IEditorPart editor = page.findEditor(input);
		if(editor==null){
			System.out.println(input.getName());
			System.out.println(editor.getTitle());
			return true;
		}*/
		return false;
	}

}
