package dev.diagram.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import dev.diagram.commands.CreateConnectionCommand;

/**
 * �ڵ�ͱߵ����Ӳ���
 * 
 * @author ľľ
 * 
 */
public class CustomGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy
{
	/**
	 * ʵ�ֱ����յ������
	 */
	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request)
	{
		//��ջ�еõ���������
		CreateConnectionCommand command = (CreateConnectionCommand) request
				.getStartCommand();
		//��������������յ�ڵ�
		command.setTarget(getHost().getModel());
		return command;
	}

	/**
	 * ʵ�ֱߺ���������
	 */
	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request)
	{
		//�����µ���������
		CreateConnectionCommand command = new CreateConnectionCommand();
		//�������ӵı�
		command.setConnection(request.getNewObject());
		//�����������������ģ��
		command.setSource(getHost().getModel());
		//�����ջ����ʾ�ߵ�����Ѿ��ͽڵ�����
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request)
	{

		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request)
	{

		return null;
	}

}
