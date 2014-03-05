package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import dev.diagram.commands.DirectEditCommand;

/**
 * 直接编辑策略
 * 
 * @author 木木
 * 
 */
public class CustomDirectEditPolicy extends DirectEditPolicy {
	/**
	 * 实现直接编辑
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// 创建新的命令
		DirectEditCommand command = new DirectEditCommand();
		// 设置被编辑的模型
		command.setModel(getHost().getModel());
		// 设置新的文本内容
		command.setText((String) request.getCellEditor().getValue());
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {

	}

}
