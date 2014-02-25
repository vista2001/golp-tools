package dev.editors.trade;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;
import dev.model.base.ResourceLeafNode;


public class TradeEditorInput implements IEditorInput{
	private ResourceLeafNode source;
	
	public ResourceLeafNode getSource() {
		return source;
	}
	
	public void setSource(ResourceLeafNode source) {
		this.source = source;
	}


	public TradeEditorInput(ResourceLeafNode obj) {
		this.source=obj;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		
		return null;
	}

	@Override
	public boolean exists() {
		
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return Activator.getImageDescriptor("icons/sample.gif");
	}

	@Override
	public String getName() {
		
		return source.getId();
	}

	@Override
	public IPersistableElement getPersistable() {
		
		return null;
	}

	@Override
	public String getToolTipText() {
		
		//return input;
		return source.getId();
	}
	
	 @Override
	 public boolean equals(Object obj) {
	  if(null == obj) return false;
	   
	  if(!(obj instanceof TradeEditorInput)) return false;
	   
	  if(!getName().equals(((TradeEditorInput)obj).getName())) return false;
	  if(!source.getParent().getName().equals(((TradeEditorInput)obj).getSource().getParent().getName()))
	  {
		  return false;
	  }
	  if(!source.getParent().getParent().getId().equals(((TradeEditorInput)obj).getSource().getParent().getParent().getId()))
	  {
		  return false;
	  } 
	  return true;
	 }
	 
}
