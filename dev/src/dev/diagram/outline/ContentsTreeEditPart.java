package dev.diagram.outline;

import java.beans.PropertyChangeEvent;
import java.util.List;

import dev.diagram.model.ContentsModel;

public class ContentsTreeEditPart extends CustomTreeEditPart {
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(ContentsModel.CHILDREN))
			refreshVisuals();

	}

	@Override
	protected List<?> getModelChildren() {

		return ((ContentsModel) getModel()).getChildren();
	}

	@Override
	protected void refreshVisuals() {

		setWidgetText("Á÷³ÌÍ¼");
		refreshChildren();
	}

}
