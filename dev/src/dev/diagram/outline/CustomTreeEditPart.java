package dev.diagram.outline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import dev.diagram.model.AbstractModel;

public class CustomTreeEditPart extends AbstractTreeEditPart implements PropertyChangeListener{

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		
	}

	@Override
	public void activate() {
		
		((AbstractModel)getModel()).addPropertyChangeListener(this);
		super.activate();
	}

	@Override
	public void deactivate() {
		
		((AbstractModel)getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
	
}
