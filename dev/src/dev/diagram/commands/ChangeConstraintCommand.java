package dev.diagram.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import dev.diagram.model.ElementModel;
import dev.diagram.model.ExAndComModel;

/**
 * ģ��λ�úʹ�С�ı�����
 * 
 * @author ľľ
 * 
 */
public class ChangeConstraintCommand extends Command {
	// �����ı��ģ��
	private ElementModel cModel;
	// �ı���λ�úʹ�С
	private Rectangle constraint;
	// �ı�ǰ��λ�úʹ�С
	private Rectangle oldConstraint;

	/**
	 * ��ģ��ִ�иı�
	 */
	@Override
	public void execute() {
		cModel.setConstraint(constraint);

	}

	/**
	 * �����µ�λ�úʹ�С
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
	 * ����Ҫ�ı��ģ��
	 * 
	 * @param model
	 */
	public void setModel(Object model) {
		cModel = (ElementModel) model;
		// ����ԭ����λ�úʹ�С�����ڳ���
		oldConstraint = cModel.getConstraint();
	}

	/**
	 * ����
	 */
	public void undo() {
		cModel.setConstraint(oldConstraint);

	}

}
