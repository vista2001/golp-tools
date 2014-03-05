package dev.diagram.commands;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.AbstractConnectionModel;

/**
 * ��ɾ������
 * 
 * @author ľľ
 * 
 */
public class DeleteConnectionCommand extends Command {

	private AbstractConnectionModel conn;

	public void setConnection(Object connx) {
		conn = (AbstractConnectionModel) connx;
	}

	/**
	 * �����ӵ������ڵ㶼ɾ���˱�
	 */
	public void execute() {
		conn.detachSource();
		conn.detachTarget();
	}

	public void undo() {
		conn.attachSource();
		conn.attachTarget();
	}

}
