package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import dev.diagram.commands.CreateConnectionCommand;
import dev.diagram.commands.ReconnectConnectionCommand;
import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;

/**
 * 节点和边的连接策略
 * 
 * @author 木木
 * 
 */
public class CustomGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy
{
	/**
	 * 实现边与终点的连接
	 */
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request)
	{
		// 从栈中得到连接命令
		CreateConnectionCommand command = (CreateConnectionCommand) request
				.getStartCommand();
		// 设置连接命令的终点节点
		command.setTarget(getHost().getModel());
		return command;
	}

	/**
	 * 实现边和起点的连接
	 */
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request)
	{
		// 创建新的连接命令
		CreateConnectionCommand command = new CreateConnectionCommand();
		// 设置连接的边
		command.setConnection(request.getNewObject());
		// 设置与边相连的起点的模型
		command.setSource(getHost().getModel());
		// 命令进栈，表示边的起点已经和节点相连
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request)
	{
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel((AbstractConnectionModel) request
				.getConnectionEditPart().getModel());
		command.setNewTargetModel((CommonModel) getHost().getModel());
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request)
	{
		ReconnectConnectionCommand command = new ReconnectConnectionCommand();
		command.setConnectionModel(request
				.getConnectionEditPart().getModel());
		command.setNewSourceModel(getHost().getModel());
		return command;
	}

}
