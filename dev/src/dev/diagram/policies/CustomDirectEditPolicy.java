package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import dev.diagram.commands.DirectEditCommand;

/**
 * ֱ�ӱ༭����
 * 
 * @author ľľ
 * 
 */
public class CustomDirectEditPolicy extends DirectEditPolicy {
	/**
	 * ʵ��ֱ�ӱ༭
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		// �����µ�����
		DirectEditCommand command = new DirectEditCommand();
		// ���ñ��༭��ģ��
		command.setModel(getHost().getModel());
		// �����µ��ı�����
		command.setText((String) request.getCellEditor().getValue());
		return command;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {

	}

}
