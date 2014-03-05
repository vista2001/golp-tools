package dev.diagram.commands;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.ElementModel;

/**
 * 直接编辑命令
 * 
 * @author 木木
 * 
 */
public class DirectEditCommand extends Command {
	// 修改前的信息
	private String oldText;
	// 修改后的信息
	private String newText;
	// 修改的模型
	private ElementModel elementModel;

	/**
	 * 执行命令，包括记录模型修改前的信息，和设置新的信息到模型
	 */
	public void execute() {
		oldText = elementModel.getText();
		elementModel.setText(newText);
	}

	public void setModel(Object model) {
		elementModel = (ElementModel) model;
	}

	public void setText(Object text) {
		newText = (String) text;
	}

	public void undo() {
		elementModel.setText(oldText);
	}
}
