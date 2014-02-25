package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import dev.diagram.commands.DeleteConnectionCommand;

/**
 * 边删除策略
 * @author 木木
 * 
 */
public class CustomConnectionEditPolicy extends ConnectionEditPolicy
{
	/**
	 * 实现边的删除
	 */
	@Override
	protected Command getDeleteCommand(GroupRequest request)
	{
		//创建新的边删除命令
		DeleteConnectionCommand command = new DeleteConnectionCommand();
		//设置被删除的边
		command.setConnection(getHost().getModel());
		return command;
	}

}
