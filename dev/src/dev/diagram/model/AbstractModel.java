package dev.diagram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * ����ģ�ͣ�����ģ�͵ĸ��࣬���ģ�͵���Ӽ�����֪ͨ�������Ƴ��������Ժ���������ͼ����ʾ���Թ���
 * 
 * @author ľľ
 * 
 */
public abstract class AbstractModel implements IPropertySource
{
	private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void firePropertyChange(String propName, Object oldValue,
			Object newValue)
	{
		listeners.firePropertyChange(propName, oldValue, newValue);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		listeners.removePropertyChangeListener(listener);
	}

	@Override
	public Object getEditableValue()
	{

		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors()
	{

		return null;
	}

	@Override
	public Object getPropertyValue(Object id)
	{

		return null;
	}

	@Override
	public boolean isPropertySet(Object id)
	{

		return false;
	}

	@Override
	public void resetPropertyValue(Object id)
	{

	}

	@Override
	public void setPropertyValue(Object id, Object value)
	{

	}

}
