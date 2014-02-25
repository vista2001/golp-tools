package dev.diagram.outline;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;

public class TreeEditPartFactory implements EditPartFactory{

	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		
		EditPart part = null;
		if(model instanceof ElementModel)
			part = new CommonTreeViewerPart();
		else if(model instanceof ContentsModel)
			part = new ContentsTreeEditPart();
		if(part != null)
			part.setModel(model);
		return part;
	}
	
}
