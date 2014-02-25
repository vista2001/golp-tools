package dev.diagram.parts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import dev.diagram.model.AbstractModel;

/**
 * �����������࣬���п������ĸ��࣬�̳���
 * <p>
 * AbstractGraphicalEditPart
 * </p>
 * ʵ�ֽӿ�
 * <p>
 * PropertyChangeListener
 * </p>
 * 
 * @author ľľ
 * 
 */
abstract public class EditPartWithListener extends AbstractGraphicalEditPart
		implements PropertyChangeListener
{
	@Override
	public void activate()
	{
		super.activate();
		//��ģ����Ӽ�����
		((AbstractModel) getModel()).addPropertyChangeListener(this);

	}
	@Override
	public void deactivate()
	{
		super.deactivate();
		//��ģ����Ӽ�����
		((AbstractModel) getModel()).removePropertyChangeListener(this);
	}

}
