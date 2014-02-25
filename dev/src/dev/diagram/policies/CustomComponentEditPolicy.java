package dev.diagram.policies;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import dev.diagram.commands.DeleteCommand;
/**
 * 实现模型删除的策略
 * @author 木木
 *
 */
public class CustomComponentEditPolicy extends ComponentEditPolicy{
	/**
	 * 实现删除模型
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequset)
	{
		//新建删除命令
		DeleteCommand command = new DeleteCommand();
		//设置被删除模型的父模型
		command.setContentsModel(getHost().getParent().getModel());
		//设置被删除模型
		command.setModel(getHost().getModel());
		return command;
	}

}
