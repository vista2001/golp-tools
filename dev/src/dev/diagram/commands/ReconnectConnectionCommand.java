package dev.diagram.commands;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ElementModel;
import dev.diagram.model.EndModel;
import dev.diagram.model.ExAndComModel;
import dev.diagram.model.StartModel;

public class ReconnectConnectionCommand extends Command
{
	private AbstractConnectionModel connectionModel;
	private ElementModel newTargetModel;
	private ElementModel newSourceModel;
	private ElementModel oldSourceModel;
	private ElementModel oldTargetModel;

	public AbstractConnectionModel getConnectionModel()
	{
		return connectionModel;
	}

	public void setConnectionModel(Object connectionModel)
	{
		this.connectionModel = (AbstractConnectionModel) connectionModel;
	}

	public ElementModel getNewTargetModel()
	{
		return newTargetModel;
	}

	public void setNewTargetModel(Object newTargetModel)
	{
		this.newTargetModel = (ElementModel) newTargetModel;
		oldTargetModel=connectionModel.getTarget();
	}

	public ElementModel getNewSourceModel()
	{
		return newSourceModel;
	}

	public void setNewSourceModel(Object newSourceModel)
	{
		this.newSourceModel = (ElementModel) newSourceModel;
		oldSourceModel=connectionModel.getSource();
	}

	@Override
	public boolean canExecute()
	{
		if (newTargetModel instanceof StartModel || newTargetModel instanceof ExAndComModel
				|| newSourceModel instanceof EndModel
				|| newSourceModel instanceof ExAndComModel)
			return false;

		return true;
	}

	@Override
	public void execute()
	{
		if (newSourceModel != null)
		{
			connectionModel.detachSource();
			connectionModel.setSource(newSourceModel);
			connectionModel.attachSource();
		} else
		{
			connectionModel.detachTarget();
			connectionModel.setTarget(newTargetModel);
			connectionModel.attachTarget();
		}
	}

	@Override
	public void redo()
	{
		execute();
	}

	@Override
	public void undo()
	{
		if (oldSourceModel != null)
		{
			connectionModel.detachSource();
			connectionModel.setSource(oldSourceModel);
			connectionModel.attachSource();
		} else
		{
			connectionModel.detachTarget();
			connectionModel.setTarget(oldTargetModel);
			connectionModel.attachTarget();
		}
	}

}
