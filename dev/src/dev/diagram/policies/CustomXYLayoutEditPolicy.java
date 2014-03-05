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
 * ������ģ�ͣ��ı�λ�úʹ�С����
 * 
 * @author ľľ
 * 
 */
public class CustomXYLayoutEditPolicy extends XYLayoutEditPolicy {
	/**
	 * ������ģ��
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {

		CreateCommand command = new CreateCommand();
		// Ҫ������ģ��
		ElementModel model = (ElementModel) request.getNewObject();
		// Ҫ����ģ�͵ĸ�ģ��
		ContentsModel contentsModel = (ContentsModel) getHost().getModel();
		// ��ģ�͵�λ�õĴ�С
		Rectangle constraint = (Rectangle) getConstraintFor(request);
		Rectangle rec = new Rectangle(constraint.x, constraint.y, 60, 40);
		// �����½�ģ������ĸ�ģ��
		command.setContentsModel(contentsModel);
		// �����½�ģ�������ģ��
		command.setModel(model);
		model.setConstraint(rec);

		return command;
	}

	/**
	 * ʵ��ģ�͵ĸı�
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		// �õ��ı�����
		ChangeConstraintCommand command = new ChangeConstraintCommand();
		// ���ñ��ı��ģ��
		command.setModel(child.getModel());
		// ���øı���µ�λ�úʹ�С
		command.setConstraint((Rectangle) constraint);
		return command;

	}
}
