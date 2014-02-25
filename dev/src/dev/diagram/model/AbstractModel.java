package dev.diagram.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * 抽象模型，所有模型的父类，完成模型的添加监听，通知监听，移除监听属性和在属性视图中显示属性功能
 * 
 * @author 木木
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
