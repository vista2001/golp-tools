package dev.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import dev.diagram.model.ElementModel;
import dev.diagram.model.ExAndComModel;

/**
 * 模型位置和大小改变命令
 * 
 * @author 木木
 * 
 */
public class ChangeConstraintCommand extends Command {
	// 发生改变的模型
	private ElementModel cModel;
	// 改变后的位置和大小
	private Rectangle constraint;
	// 改变前的位置和大小
	private Rectangle oldConstraint;

	/**
	 * 对模型执行改变
	 */
	@Override
	public void execute() {
		cModel.setConstraint(constraint);

	}

	/**
	 * 设置新的位置和大小
	 * 
	 * @param rect
	 */
	public void setConstraint(Rectangle rect) {
		constraint = rect;
		if (cModel instanceof ExAndComModel)
			constraint = new Rectangle(rect.x, rect.y, oldConstraint.width,
					oldConstraint.height);
	}

	/**
	 * 设置要改变的模型
	 * 
	 * @param model
	 */
	public void setModel(Object model) {
		cModel = (ElementModel) model;
		// 保存原来的位置和大小，用于撤销
		oldConstraint = cModel.getConstraint();
	}

	/**
	 * 撤销
	 */
	public void undo() {
		cModel.setConstraint(oldConstraint);

	}

}
