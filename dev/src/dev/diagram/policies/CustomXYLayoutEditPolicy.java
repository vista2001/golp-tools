package dev.diagram.policies;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import dev.diagram.commands.ChangeConstraintCommand;
import dev.diagram.commands.CreateCommand;
import dev.diagram.model.ContentsModel;
import dev.diagram.model.ElementModel;

/**
 * 创建新模型，改变位置和大小策略
 * 
 * @author 木木
 * 
 */
public class CustomXYLayoutEditPolicy extends XYLayoutEditPolicy {
	/**
	 * 创建新模型
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {

		CreateCommand command = new CreateCommand();
		// 要创建的模型
		ElementModel model = (ElementModel) request.getNewObject();
		// 要创建模型的父模型
		ContentsModel contentsModel = (ContentsModel) getHost().getModel();
		// 新模型的位置的大小
		Rectangle constraint = (Rectangle) getConstraintFor(request);
		Rectangle rec = new Rectangle(constraint.x, constraint.y, 60, 40);
		// 设置新建模型命令的父模型
		command.setContentsModel(contentsModel);
		// 设置新建模型命令的模型
		command.setModel(model);
		model.setConstraint(rec);

		return command;
	}

	/**
	 * 实现模型的改变
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// 得到改变命令
		ChangeConstraintCommand command = new ChangeConstraintCommand();
		// 设置被改变的模型
		command.setModel(child.getModel());
		// 设置改变后新的位置和大小
		command.setConstraint((Rectangle) constraint);
		return command;

	}
}
