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
 * ��������,����Դ�ڵ㣬ָ��ڵ㣬��
 * 
 * @author ľľ
 * 
 */
public class CreateConnectionCommand extends Command
{
	// Դ�ڵ㣨�飩
	private ElementModel source;
	// ָ��ڵ㣨�飩
	private ElementModel target;
	// ��
	private AbstractConnectionModel connection;

	/**
	 * ִ��ǰ�жϴ����������Ƿ��ִ�� �翪ʼ�ڵ㲻������ΪĿ��ڵ㣬�����ڵ�û�г��ߣ�����ΪԴ�ڵ㣩
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
	 * ����Դ�ڵ�
	 * 
	 * @return
	 */
	public ElementModel getSource()
	{
		return source;
	}

	/**
	 * ����ָ��Ŀ�꣩�ڵ�
	 * 
	 * @return
	 */
	public ElementModel getTarget()
	{
		return target;
	}

	/**
	 * ���ر�
	 * 
	 * @return
	 */
	public AbstractConnectionModel getConnection()
	{
		return connection;
	}

	/**
	 * ���ñ�
	 * 
	 * @param connection
	 */
	public void setConnection(Object connection)
	{
		this.connection = (AbstractConnectionModel) connection;
	}

	/**
	 * ִ��������Ե�ǰ�ıߵ����ӵ������ڵ����ߵ���Ϣ
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
	 * ����Դ�ڵ�
	 * 
	 * @param model
	 */
	public void setSource(Object model)
	{
		source = (ElementModel) model;
		connection.setSource(source);

	}

	/**
	 * ����Ŀ��ڵ�
	 * 
	 * @param model
	 */
	public void setTarget(Object model)
	{
		target = (ElementModel) model;
		connection.setTarget(target);
	}

	/**
	 * ����
	 */
	public void undo()
	{
		connection.detachSource();
		connection.detachTarget();
	}

}
