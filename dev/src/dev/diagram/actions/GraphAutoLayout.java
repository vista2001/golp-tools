package dev.diagram.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.ContentsModel;

/**
 * 自动布局动作
 * 
 */
public class GraphAutoLayout extends SelectionAction {
	// 自动布局的ID
	// 自动布局的对象contentsModel
	private ContentsModel contentsModel;
	public final static String ID = "Action.GraphAutoLayout";

	/**
	 * 初始化信息
	 */
	public GraphAutoLayout(IWorkbenchPart part, ContentsModel contentsModel) {
		// TODO Auto-generated constructor stub
		super(part);
		this.contentsModel = contentsModel;
		setId(ID);
		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/actor.gif");
		setImageDescriptor(descriptor);

	}

	/*
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		// 根据按钮被选择的状态进行自动布局
		setChecked(!isChecked());
		contentsModel.setAutoLayout(isChecked());
	}

	@Override
	protected boolean calculateEnabled() {

		return true;
	}

}
