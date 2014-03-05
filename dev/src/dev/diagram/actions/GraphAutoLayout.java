package dev.diagram.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.ContentsModel;

/**
 * �Զ����ֶ���
 * 
 */
public class GraphAutoLayout extends SelectionAction {
	// �Զ����ֵ�ID
	// �Զ����ֵĶ���contentsModel
	private ContentsModel contentsModel;
	public final static String ID = "Action.GraphAutoLayout";

	/**
	 * ��ʼ����Ϣ
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
		// ���ݰ�ť��ѡ���״̬�����Զ�����
		setChecked(!isChecked());
		contentsModel.setAutoLayout(isChecked());
	}

	@Override
	protected boolean calculateEnabled() {

		return true;
	}

}
