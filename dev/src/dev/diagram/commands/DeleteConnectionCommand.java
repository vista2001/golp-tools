package dev.diagram.commands;

import org.eclipse.gef.commands.Command;

import dev.diagram.model.AbstractConnectionModel;

/**
 * 边删除命令
 * 
 * @author 木木
 * 
 */
public class DeleteConnectionCommand extends Command {

	private AbstractConnectionModel conn;

	public void setConnection(Object connx) {
		conn = (AbstractConnectionModel) connx;
	}

	/**
	 * 边连接的俩个节点都删除此边
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
