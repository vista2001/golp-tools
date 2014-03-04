package dev.diagram.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

import dev.diagram.inputDialog.EdgeListLimitDialog;
import dev.diagram.model.AbstractConnectionModel;
import dev.diagram.model.CommonModel;
import dev.diagram.model.ElementModel;
import dev.diagram.model.EndModel;
import dev.diagram.model.ExAndComModel;
import dev.diagram.model.StartModel;

/**
 * 连接命令,包括源节点，指向节点，边
 * 
 * @author 木木
 * 
 */
public class CreateConnectionCommand extends Command
{
	// 源节点（块）
	private ElementModel source;
	// 指向节点（块）
	private ElementModel target;
	// 边
	private AbstractConnectionModel connection;

	/**
	 * 执行前判断此连接命令是否可执行 如开始节点不容许作为目标节点，结束节点没有出边（不作为源节点）
	 */
	@Override
	public boolean canExecute()
	{
		if (source == null || target == null)
			return false;
		else if (source.equals(target))
			return false;
		else if (target instanceof StartModel
				|| target instanceof ExAndComModel
				|| source instanceof EndModel
				|| source instanceof ExAndComModel)
			return false;

		return true;
	}

	/**
	 * 返回源节点
	 * 
	 * @return
	 */
	public ElementModel getSource()
	{
		return source;
	}

	/**
	 * 返回指向（目标）节点
	 * 
	 * @return
	 */
	public ElementModel getTarget()
	{
		return target;
	}

	/**
	 * 返回边
	 * 
	 * @return
	 */
	public AbstractConnectionModel getConnection()
	{
		return connection;
	}

	/**
	 * 设置边
	 * 
	 * @param connection
	 */
	public void setConnection(Object connection)
	{
		this.connection = (AbstractConnectionModel) connection;
	}

	/**
	 * 执行命令，即对当前的边的连接的俩个节点加入边的信息
	 */
	public void execute()
	{
		EdgeListLimitDialog dialog = new EdgeListLimitDialog(PlatformUI
				.getWorkbench().getActiveWorkbenchWindow().getShell(),
				(CommonModel) source);
		if(dialog.open()==Window.OK)
		{
			connection.attachSource();
			connection.attachTarget();
			connection.setWeight(dialog.getText());
			
		}

	}

	/**
	 * 设置源节点
	 * 
	 * @param model
	 */
	public void setSource(Object model)
	{
		source = (ElementModel) model;
		connection.setSource(source);

	}

	/**
	 * 设置目标节点
	 * 
	 * @param model
	 */
	public void setTarget(Object model)
	{
		target = (ElementModel) model;
		connection.setTarget(target);
	}

	/**
	 * 撤销
	 */
	public void undo()
	{
		connection.detachSource();
		connection.detachTarget();
	}

}
