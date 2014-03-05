package dev.diagram.outline;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import dev.diagram.model.CommonModel;
import dev.diagram.model.ElementModel;
import dev.diagram.policies.CustomComponentEditPolicy;

public class CommonTreeViewerPart extends CustomTreeEditPart {

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		refreshVisuals();

	}

	@Override
	protected void refreshVisuals() {

		ElementModel elementModel = (ElementModel) getModel();
		if (elementModel instanceof CommonModel)
			setWidgetText(elementModel.getText() + "("
					+ elementModel.getTypeName() + "½Úµã)");
		else
			setWidgetText(elementModel.getText() + "("
					+ elementModel.getTypeName() + ")");
		refreshChildren();
	}

	@Override
	protected void createEditPolicies() {

		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new CustomComponentEditPolicy());
	}

}
