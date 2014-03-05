package dev.diagram.commands;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.ElementModel;

/**
 * ֱ�ӱ༭����
 * 
 * @author ľľ
 * 
 */
public class DirectEditCommand extends Command {
	// �޸�ǰ����Ϣ
	private String oldText;
	// �޸ĺ����Ϣ
	private String newText;
	// �޸ĵ�ģ��
	private ElementModel elementModel;

	/**
	 * ִ�����������¼ģ���޸�ǰ����Ϣ���������µ���Ϣ��ģ��
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
