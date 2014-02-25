package dev.diagram.policies;


import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import dev.diagram.commands.DeleteCommand;
/**
 * ʵ��ģ��ɾ���Ĳ���
 * @author ľľ
 *
 */
public class CustomComponentEditPolicy extends ComponentEditPolicy{
	/**
	 * ʵ��ɾ��ģ��
	 */
	protected Command createDeleteCommand(GroupRequest deleteRequset)
	{
		//�½�ɾ������
		DeleteCommand command = new DeleteCommand();
		//���ñ�ɾ��ģ�͵ĸ�ģ��
		command.setContentsModel(getHost().getParent().getModel());
		//���ñ�ɾ��ģ��
		command.setModel(getHost().getModel());
		return command;
	}

}
