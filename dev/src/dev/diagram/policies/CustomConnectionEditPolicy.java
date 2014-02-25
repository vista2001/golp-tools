package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import dev.diagram.commands.DeleteConnectionCommand;

/**
 * ��ɾ������
 * @author ľľ
 * 
 */
public class CustomConnectionEditPolicy extends ConnectionEditPolicy
{
	/**
	 * ʵ�ֱߵ�ɾ��
	 */
	@Override
	protected Command getDeleteCommand(GroupRequest request)
	{
		//�����µı�ɾ������
		DeleteConnectionCommand command = new DeleteConnectionCommand();
		//���ñ�ɾ���ı�
		command.setConnection(getHost().getModel());
		return command;
	}

}
