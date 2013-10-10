package dev.editors;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;


public class ServerEditorInput implements IEditorInput{
	private String input;
	public ServerEditorInput(String input){
		this.input=input;
	}

	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/sample.gif");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return input;
	}
	 @Override
	 public boolean equals(Object obj) {
	  if(null == obj) return false;
	   
	  if(!(obj instanceof ServerEditorInput)) return false;
	   
	  if(!getName().equals(((ServerEditorInput)obj).getName())) return false;
	   
	  return true;
	 }
}
