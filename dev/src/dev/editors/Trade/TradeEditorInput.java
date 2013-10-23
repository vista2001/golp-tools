package dev.editors.Trade;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import dev.Activator;
import dev.model.base.ResourceLeafNode;


public class TradeEditorInput implements IEditorInput{
	private String input;
	private ResourceLeafNode source;
	
	public ResourceLeafNode getSource() {
		return source;
	}
	
	public TradeEditorInput(String input){
		this.input=input;
	}
	public TradeEditorInput(ResourceLeafNode obj){
		this.input=obj.getId();
		this.source=obj;
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
	   
	  if(!(obj instanceof TradeEditorInput)) return false;
	   
	  if(!getName().equals(((TradeEditorInput)obj).getName())) return false;
	   
	  return true;
	 }
}
