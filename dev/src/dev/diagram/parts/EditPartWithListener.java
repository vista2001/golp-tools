package dev.diagram.parts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import dev.diagram.model.AbstractModel;

/**
 * 控制器抽象类，所有控制器的父类，继承于
 * <p>
 * AbstractGraphicalEditPart
 * </p>
 * 实现接口
 * <p>
 * PropertyChangeListener
 * </p>
 * 
 * @author 木木
 * 
 */
abstract public class EditPartWithListener extends AbstractGraphicalEditPart
		implements PropertyChangeListener
{
	@Override
	public void activate()
	{
		super.activate();
		//给模型添加监听器
		((AbstractModel) getModel()).addPropertyChangeListener(this);

	}
	@Override
	public void deactivate()
	{
		super.deactivate();
		//给模型添加监听器
		((AbstractModel) getModel()).removePropertyChangeListener(this);
	}

}
